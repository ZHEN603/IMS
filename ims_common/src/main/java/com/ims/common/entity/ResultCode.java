package com.ims.common.entity;

public enum ResultCode {

    SUCCESS(true,10000,"Successful！"),

    FAIL(false,10001,"Failed"),
    UNAUTHENTICATED(false,10002,"Please log in"),

    UNAUTHORISED(false,10003,"Authorization denied"),

    PASSWORD(true,10004,"Please change your password"),

    SERVER_ERROR(false,99999,"Sorry, we cannot do this now."),

    LOGIN_ERROR(false,20000,"Sorry, wrong Email or Password"),

    EMAIL_ERROR(false,20001,"Sorry, The email is already in use."),

    PASSWORD_ERROR(false,20002,"Sorry, The password is not correct."),

    USER_ERROR(false,20003,"Sorry, The Account has been locked."),

    LOW_STOCK(false,30000,"Insufficient stock！"),

    UNAPPROVED(false,30001,"Sorry, The order was not approved.");



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
