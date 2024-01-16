package com.me.social.config;

public class ExtendedConstants {

    public enum ResponseCode {

        SUCCESSFUL("00","Successful"),
        FAIL("99","Request Failed"),
        PROCESSING("98","Request Processing"),
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
