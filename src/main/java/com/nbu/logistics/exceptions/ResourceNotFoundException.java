package com.nbu.logistics.exceptions;

class ResourceNotFoundException  extends ApiException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
