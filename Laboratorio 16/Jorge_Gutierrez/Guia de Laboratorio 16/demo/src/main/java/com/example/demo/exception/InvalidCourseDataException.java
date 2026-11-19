package com.example.demo.exception;

public class InvalidCourseDataException extends RuntimeException {

    public InvalidCourseDataException(String message) {
        super(message);
    }
}
