package com.eveb.gateway.game.cq9.constants;

public class Cq9Constants {

    public static class Status {
        public static final String SUCCESS = "Success";
        public static final String PENDING = "Pending";
        public static final String FAILED = "Failed";
        public static final String Refund = "Refund";
    }

    public static enum Code{
        /**成功時反回編碼0*/
        CODE_0("0","Success"),
        /**動作錯誤， 執行的action錯誤。
         [“bet”,“endround”,“rollout”,“takeall”,“rollin”,“debit”,“credit”,“refund”]
         當不在上述動作時就回該錯誤編碼*/
        CODE_1002("1002","動作錯誤,執行的action錯誤!"),
        /**當帶入參數錯誤時或未帶入必要參數時 ，回傳該錯誤編碼*/
        CODE_1003("1003","帶入參數錯誤或未帶入必要參數!"),
        /**時間格式錯誤時回傳該編碼*/
        CODE_1004("1004","時間格式錯誤!"),
        /**餘額不足時回傳該編碼*/
        CODE_1005("code_1005","餘額不足!"),
        /**查無玩家時回傳該編碼*/
        CODE_1006("1006","查無玩家!"),
        /**當未查詢到紀錄時回傳該錯誤編碼*/
        CODE_1014("1014","未查詢到紀錄!"),
        /**當該mtcode已經被refund時回傳該錯誤編碼*/
        CODE_1015("1015","mtcode已經被refund!"),
        /**伺服器錯誤時回傳該編碼*/
        CODE_1100("1100","伺服器錯誤!"),
        /**MTCode重複時回傳該編碼*/
        CODE_2009("2009","MTCode重複!");

        private String key;
        private String value;

        Code(String key, String value) {
            this.key = key;
            this.value = value;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }
}
