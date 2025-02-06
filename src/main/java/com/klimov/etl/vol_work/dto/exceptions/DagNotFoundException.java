package com.klimov.etl.vol_work.dto.exceptions;

public class DagNotFoundException extends RuntimeException {
    public DagNotFoundException(String message) {
        super(message);
    }
}
