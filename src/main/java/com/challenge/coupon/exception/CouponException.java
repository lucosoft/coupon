package com.challenge.coupon.exception;

import org.springframework.http.HttpStatus;

public class CouponException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private HttpStatus status = null;
    private String code;
    private String description;
    private String detailMessage;

    public CouponException(String code, String description, String detailMessage, HttpStatus httpStatus) {
        this.code=code;
        this.description=description;
        this.status=httpStatus;
        this.detailMessage=detailMessage;
    }

    public CouponException(String code, String description, HttpStatus httpStatus) {
        this.code=code;
        this.description=description;
        this.status=httpStatus;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

}
