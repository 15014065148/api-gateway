package com.eveb.gateway.game.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.aspect.UnityAspect;
import com.eveb.gateway.constants.SysEnum;
import com.eveb.gateway.game.model.RequestLog;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.Authenticator;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OkHttpService {

    @Autowired
    private OKHttpClientService service;


    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";
    public final static String PATCH = "PATCH";
    public final static String APPLICATION_JSON = "application/json";
    public final static String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private final static String UTF8 = "UTF-8";
    private final static String GBK = "GBK";
    private final static String DEFAULT_CHARSET = UTF8;
    private final static String DEFAULT_METHOD = GET;
    private final static boolean DEFAULT_LOG = true;


    public OkHttpClient client;

    public OkHttpClient proxyClient;


    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(60, 5, TimeUnit.MINUTES))
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS).build();
        proxyClient = setProxys(new OkHttpClient.Builder()).connectionPool(new ConnectionPool(60, 5, TimeUnit.MINUTES))
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS).build();
    }


    /***
     * 初始化PT连接
     */
    public OkHttpClient initPtClient(String X_ENTITY_KEY) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        InputStream stream = this.getClass().getResourceAsStream("/key/VBETCNYTLE.p12");
        File file = new File("VBETCNYTLE.p12");
        FileUtils.copyInputStreamToFile(stream, file);
        FileInputStream fis = new FileInputStream(file);
        ks.load(fis, "iQ3xuZrS".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "iQ3xuZrS".toCharArray());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        OkHttpClient.Builder ptbuilder = setProxys(new OkHttpClient.Builder()).hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        }).sslSocketFactory(sc.getSocketFactory(), new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        });
        ptbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("X_ENTITY_KEY", X_ENTITY_KEY).build();
                return chain.proceed(request);
            }
        });
        ptbuilder.readTimeout(60, TimeUnit.SECONDS);
        ptbuilder.connectTimeout(20, TimeUnit.SECONDS);
        return ptbuilder.build();
    }

    /***
     * 初始化连接
     * @param token
     * @return
     */
    public OkHttpClient initPt2Client(String token) {
        /**使用代理**/
        OkHttpClient.Builder pt2builder = setProxys(new OkHttpClient.Builder());
        pt2builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("x-access-token", token).build();
                return chain.proceed(request);
            }
        });
        return pt2builder.build();
    }

    /***
     * 初始化连接
     * @param token
     * @return
     */
    public OkHttpClient initMgClient(String token) {
        /**使用代理**/
        OkHttpClient.Builder mgbuilder = setProxys(new OkHttpClient.Builder());
        mgbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("X-Api-Auth", token)
                        .addHeader("X-Requested-With", "X-Api-Client")
                        .addHeader("X-Api-Call", "X-Api-Client")
                        .addHeader("Content-Type", "application/json").build();
                return chain.proceed(request);
            }
        });
        return mgbuilder.build();
    }

    public OkHttpClient initHeadClient(Map<String,String > headMap) {
        /**使用代理**/
        OkHttpClient.Builder gnsbuilder = setProxys(new OkHttpClient.Builder());
        gnsbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder=chain.request().newBuilder();
                if (MapUtils.isNotEmpty(headMap)) {
                    headMap.forEach(builder::addHeader);
                }
                Request request=builder.build();
                return chain.proceed(request);
            }
        });
        return gnsbuilder.build();
    }


    public OkHttpClient.Builder setProxys(OkHttpClient.Builder builder) {
        List<String> proxylist = service.getProxys(SysEnum.enumProxy.getKey());
        if (proxylist.size() > 0) {
            builder.proxySelector(new ProxySelector() {
                @Override
                public List<Proxy> select(URI uri) {
                    List<Proxy> list = new ArrayList<>();
                    proxylist.forEach(proxystr -> {
                        Map map = JSON.parseObject(proxystr);
                        switch (map.get("proxyType").toString().toUpperCase()) {
                            case "SOCKS":
                                list.add(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(map.get("ip").toString(), (Integer) map.get("port"))));
                                break;
                            case "HTTP":
                                list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(map.get("ip").toString(), (Integer) map.get("port"))));
                                break;
                        }
                    });
                    return list;
                }
                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    log.error("uri: {} socketAddress: {} IOException: {}", uri, sa, ioe);
                }
            }).proxyAuthenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    /**默认*/
                    String user = "eveada";
                    String password = "hALTendi";
                    for (String str : proxylist) {
                        Map map = JSON.parseObject(str);
                        if ("HTTP".equals(map.get("proxyType").toString().toUpperCase())) {
                            user = map.get("user").toString();
                            password = map.get("password").toString();
                        }
                    }
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", Credentials.basic(user, password))
                            .build();
                }
            });
        }
        return builder;
    }

    /**
     * GET请求
     *
     * @param url URL地址
     * @return
     */
    public String get(OkHttpClient httpClient, String url) throws Exception {
        return execute(OkHttp.builder().url(url).build(), httpClient);
    }

    public Response getRsp(OkHttpClient httpClient, String url) throws Exception {
        return executeRsp(OkHttp.builder().url(url).build(), httpClient);
    }

    /**
     * GET请求
     *
     * @param url URL地址
     * @return
     */
    public String get(OkHttpClient httpClient, String url, String charset) throws Exception {
        return execute(OkHttp.builder().url(url).responseCharset(charset).build(), httpClient);
    }

    /**
     * 带查询参数的GET查询
     *
     * @param url      URL地址
     * @param queryMap 查询参数
     * @return
     */
    public String get(OkHttpClient httpClient, String url, Map<String, String> queryMap) throws Exception {
        return execute(OkHttp.builder().url(url).queryMap(queryMap).build(), httpClient);
    }

    /**
     * 带查询参数的GET查询
     *
     * @param url      URL地址
     * @param queryMap 查询参数
     * @return
     */
    public String get(OkHttpClient httpClient, String url, Map<String, String> queryMap, String charset) throws Exception {
        return execute(OkHttp.builder().url(url).queryMap(queryMap).responseCharset(charset).build(), httpClient);
    }

    /**
     * delete
     * @param httpClient
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public Response deleteRsp(OkHttpClient httpClient, String url, String data)throws Exception {
        return executeRsp(OkHttp.builder().url(url).method(DELETE).data(data).mediaType(APPLICATION_FORM_URLENCODED).build(), httpClient);
    }

    /**
     * POST
     * application/String
     *
     * @param url
     * @param obj
     * @return
     */
    public String post(OkHttpClient httpClient, String url, Object obj) throws Exception {
        return execute(OkHttp.builder().url(url).method(POST).data(obj.toString()).build(), httpClient);
    }

    public String put(OkHttpClient httpClient, String url, Object obj) throws Exception {
        return execute(OkHttp.builder().url(url).method(PUT).data(obj.toString()).build(), httpClient);
    }


    /**
     * POST
     * application/json
     *
     * @param url
     * @param obj
     * @return
     */
    public String postJson(OkHttpClient httpClient, String url, Object obj) throws Exception {
        return execute(OkHttp.builder().url(url).method(POST).data(JSON.toJSONString(obj)).mediaType(APPLICATION_JSON).build(), httpClient);
    }

    public String putJson(OkHttpClient httpClient, String url, String parmsJosn){
        String responseBody = "";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parmsJosn);
        Request.Builder builder = new Request.Builder();
        builder.url(url).method(PUT, requestBody);
        Request request = builder.build();
        Response response = null;
        try {
            OkHttpClient okHttpClient =httpClient;
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.debug("post 方法请求url地址{}参数为{},请求头为{},错误信息为{}", url, parmsJosn, JSON.toJSONString(parmsJosn), e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

    /**
     * POST
     * application/x-www-form-urlencoded
     *
     * @param url
     * @param formMap
     * @return
     */
    public String postForm(OkHttpClient httpClient, String url, Map<String, String> formMap) throws Exception {
        String data = "";
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors
                    .joining("&"));
        }
        return execute(OkHttp.builder().url(url).method(POST).data(data).mediaType(APPLICATION_FORM_URLENCODED).build(), httpClient);
    }

    public Response postFormRsp(OkHttpClient httpClient, String url, Map<String, String> formMap) throws Exception {
        String data = "";
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors
                    .joining("&"));
        }
        return executeRsp(OkHttp.builder().url(url).method(POST).data(data).mediaType(APPLICATION_FORM_URLENCODED).build(), httpClient);
    }

    public Response putFormRsp(OkHttpClient httpClient, String url, Map<String, String> formMap) throws Exception {
        String data = "";
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors
                    .joining("&"));
        }
        return executeRsp(OkHttp.builder().url(url).method(PUT).data(data).mediaType(APPLICATION_FORM_URLENCODED).build(), httpClient);
    }

    public String postXml(String url, String param) {
        Map<String, String> head = new HashMap<String, String>();
        head.put("Content-Type", ContentType.APPLICATION_XML.toString());
        return postXmlParams(proxyClient,url, param, head);
    }

    public String postXml(OkHttpClient client,String url, String param) {
        Map<String, String> head = new HashMap<String, String>();
        head.put("Content-Type", ContentType.APPLICATION_XML.toString());
        return postXmlParams(client,url, param, head);
    }

    public String postXml(String url, String param,Map<String, String> head) {
        return postXmlParams(proxyClient,url, param, head);
    }

    public Response postXmlRsp(OkHttpClient client,String url, String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), param);
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(requestBody);
        try {
        return client.newCall(builder.build()).execute();
        } catch (Exception e) {
            log.debug("post方法url地址{}参数为{}", url, param);
        }
        return null;
    }


    /**
     * @param url
     * @param xml
     * @param headParams
     * @return
     */
    public String postXmlParams(OkHttpClient client,String url, String xml, Map<String, String> headParams) {
        String responseBody = "";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), xml);
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(requestBody);
        if (MapUtils.isNotEmpty(headParams)) {
            headParams.forEach(builder::addHeader);
        }
        Request request = builder.build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                return response.body().string();
            }else
            {
                log.error(response.message());
            }
        } catch (Exception e) {
            log.debug("post方法url地址{}参数为{},请求头为{}", url, xml, JSON.toJSONString(headParams));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

    /**
     * 通用执行方法
     */


    private String execute(OkHttp okHttp, OkHttpClient httpClient) throws Exception {
        RequestLog rlog=UnityAspect.threadLocalRequestLog.get();
        String result;
        try {
            log.info(String.format("Got request->%s,%s ", okHttp.url,okHttp.data));
            Response response = executeRsp(okHttp,httpClient);
            result = response.body().string();
            log.info(String.format("Got response->%s", result));
            rlog.setResponseMsg(result);
        } catch (Exception e) {
            log.error(okHttp.toString(), e);
            rlog.setResponseMsg(e.getMessage());
            return null;
        }
        return result;
    }

    private Response executeRsp(OkHttp okHttp, OkHttpClient httpClient) throws Exception {
        RequestLog rlog=UnityAspect.threadLocalRequestLog.get();
        if (StringUtils.isBlank(okHttp.requestCharset)) {
            okHttp.requestCharset = DEFAULT_CHARSET;
        }
        if (StringUtils.isBlank(okHttp.responseCharset)) {
            okHttp.responseCharset = DEFAULT_CHARSET;
        }
        if (StringUtils.isBlank(okHttp.method)) {
            okHttp.method = DEFAULT_METHOD;
        }
        if (StringUtils.isBlank(okHttp.mediaType)) {
            okHttp.mediaType = APPLICATION_JSON;
        }
        if (okHttp.requestLog) {
            log.info(okHttp.toString());
        }

        String url = okHttp.url;
        rlog.setRequestUrl(okHttp.url);
        rlog.setRequestData(okHttp.data);
        Request.Builder builder = new Request.Builder();

        if (MapUtils.isNotEmpty(okHttp.queryMap)) {
            String queryParams = okHttp.queryMap.entrySet().stream()
                    .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining("&"));
            url = String.format("%s%s%s", url, url.contains("?") ? "&" : "?", queryParams);
        }
        builder.url(url);

        if (MapUtils.isNotEmpty(okHttp.headerMap)) {
            okHttp.headerMap.forEach(builder::addHeader);
        }

        String method = okHttp.method.toUpperCase();
        String mediaType = String.format("%s;charset=%s", okHttp.mediaType, okHttp.requestCharset);

        if (StringUtils.equals(method, GET)) {
            builder.get();
        } else if (ArrayUtils.contains(new String[]{POST, PUT, DELETE, PATCH}, method)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), okHttp.data);
            builder.method(method, requestBody);
        } else {
            throw new Exception(String.format("http method:%s not support!", method));
        }
        UnityAspect.threadLocalRequestLog.set(rlog);
        return httpClient.newCall(builder.build()).execute();
    }

    @Builder
    @ToString(exclude = {"requestCharset", "responseCharset", "requestLog", "responseLog"})
    static class OkHttp {
        private String url;
        private String method = DEFAULT_METHOD;
        private String data;
        private String mediaType = APPLICATION_JSON;
        private Map<String, String> queryMap;
        private Map<String, String> headerMap;
        private String requestCharset = DEFAULT_CHARSET;
        private boolean requestLog = DEFAULT_LOG;

        private String responseCharset = DEFAULT_CHARSET;
        private boolean responseLog = DEFAULT_LOG;
    }
}
