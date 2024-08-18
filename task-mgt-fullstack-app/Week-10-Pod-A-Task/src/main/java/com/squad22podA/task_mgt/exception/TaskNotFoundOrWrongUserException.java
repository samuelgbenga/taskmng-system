package com.squad22podA.task_mgt.exception;

public class TaskNotFoundOrWrongUserException extends RuntimeException{

    public TaskNotFoundOrWrongUserException(String message) {
        super(message);
    }
}
