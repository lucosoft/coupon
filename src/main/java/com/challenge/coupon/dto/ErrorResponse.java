package com.challenge.coupon.dto;

import com.challenge.coupon.exception.CouponException;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private String code;

    private String status;

    private String description;

    private String detail;

    public ErrorResponse(String code,
                         String description,
                         String detail,
                         HttpStatus httpStatus) {
        this.code = code;
        this.status = httpStatus.name();
        this.description = description;
        this.detail = detail;
    }

    public ErrorResponse(CouponException e) {
        this.code = e.getCode();
        this.status = e.getStatus().name();
        this.description = e.getDescription();
        this.detail = e.getDetailMessage();
    }

    public Date getTimestamp() {
        return new Date();
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
