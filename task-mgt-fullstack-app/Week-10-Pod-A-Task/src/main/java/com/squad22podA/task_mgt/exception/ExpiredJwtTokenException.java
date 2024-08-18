package com.squad22podA.task_mgt.exception;

public class ExpiredJwtTokenException extends RuntimeException {
    public ExpiredJwtTokenException(String message) {
        super(message);
    }
    public ExpiredJwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
