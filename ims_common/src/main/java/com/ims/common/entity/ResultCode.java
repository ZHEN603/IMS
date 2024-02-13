package com.ims.common.entity;

public enum ResultCode {

    SUCCESS(true,10000,"Successful！"),

    FAIL(false,10001,"Failed"),
    UNAUTHENTICATED(false,10002,"Please log in"),
    UNAUTHORISE(false,10003,"Authorization denied"),
    SERVER_ERROR(false,99999,"Sorry, we cannot do this now."),

    LOGIN_ERROR(false,20000,"Sorry, wrong ID or Password");

    boolean success;
    int code;
    String message;

    ResultCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
