package com.nbu.logistics.exceptions;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(message);
    }
}
