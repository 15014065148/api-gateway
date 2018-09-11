package com.eveb.gateway.game.elg.model;

import lombok.Data;

@Data
public class ElgParameterModel {

    private String key;
    private String pwd;
    private String system;
    private String currency;
    private String language;
    private String ip;
    private String server;
    private String password;

    private String userName;
    private String amount;
    private String page;//跳转游戏需要用到,调用者通过gameId传入


    //Hash:User/Add/[IP]/[TID]/[KEY]/[LOGIN]/[PASSWORD]/[CURRENCY]/[PWD]
    private String createUserHash = "User/Add/%s/%s/%s/%s/%s/%s/%s";
    //https://[SERVER]/System/Api/[KEY]/User/Add/?&Login=[Login]&Password=[Password]&TID=[TID]&Currency=[Currency]&Language=[Language]&RegistrationIP=[RegistrationIP]&Hash=[Hash]
    private String createUserUrl = "https://%s/System/Api/%s/User/Add/?&Login=%s&Password=%s&TID=%s&Currency=%s&Language=%s&RegistrationIP=%s&Hash=%s";
    //Balance/Set/[IP]/[TID]/[KEY]/[SYSTEM]/[AMOUNT]/[LOGIN]/[CURRENCY]/[PWD]
    private String transferHash = "Balance/Set/%s/%s/%s/%s/%s/%s/%s/%s";
    //https://[SERVER]/System/Api/[KEY]/Balance/Set/?&Login=[LOGIN]&System=[SYSTEM]&Amount=[AMOUNT]&TID=[TID]&Currency=[CURRENCY]&Hash=[HASH]
    private String transferUrl = "https://%s/System/Api/%s/Balance/Set/?&Login=%s&System=%s&Amount=%s&TID=%s&Currency=%s&Hash=%s";
    //Hash:Balance/Get/[IP]/[TID]/[KEY]/[SYSTEM]/[LOGIN]/[PWD]
    private String balanceHash = "Balance/Get/%s/%s/%s/%s/%s/%s";
    //https://[SERVER]/System/Api/[KEY]/Balance/Get/?&Login=[LOGIN]&System=[SYSTEM]&TID=[TID]&Hash=[HASH]
    private String balanceUrl = "https://%s/System/Api/%s/Balance/Get/?&Login=%s&System=%s&TID=%s&Hash=%s";
    //Hash:User/DirectAuth/[IP]/[TID]/[KEY]/[LOGIN]/[PASSWORD]/[SYSTEM]/[PWD]
    private String playGameHash="User/DirectAuth/%s/%s/%s/%s/%s/%s/%s";
    //https://[SERVER]/System/Api/[KEY]/User/DirectAuth/?&Login=[LOGIN]&Password=[PASSWORD]&System=[SYSTEM]&TID=[TID]&Hash=[HASH]&Page=[PAGE]&UserIP=[USERIP]
    private String playGameUrl="https://%s/System/Api/%s/User/DirectAuth/?&Login=%s&Password=%s&System=%s&TID=%s&Hash=%s&Page=%s&UserIP=%s";

}
