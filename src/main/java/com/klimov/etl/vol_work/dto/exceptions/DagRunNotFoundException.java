package com.klimov.etl.vol_work.dto.exceptions;

public class DagRunNotFoundException extends RuntimeException {
    public DagRunNotFoundException(String message) {
        super(message);
    }
}
