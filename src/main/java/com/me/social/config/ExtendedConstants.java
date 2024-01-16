package com.me.social.config;

public class ExtendedConstants {

    public enum ResponseCode {

        SUCCESS("00","Successful"),
        FAIL("99","Request Failed"),
        PROCESSING("98","Request Processing"),
        NOT_FOUND("97","Resource Not Found"),
        DUPLICATE_RECORD("96","Duplicate Record Found"),
        USER_NOT_FOUND("95","User Not Found"),
        INVALID_USER("94","Invalid User"),
        INVALID_POST("93","Invalid Post"),
        ;

        ResponseCode(String status,String message){
            this.status = status;
            this.message = message;
        }
        String status;
        String message;

        public String getStatus(){
            return status;
        }

        public String getMessage(){
            return message;
        }
    }
}
