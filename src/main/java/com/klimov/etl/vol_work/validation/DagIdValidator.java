package com.klimov.etl.vol_work.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class DagIdValidator implements ConstraintValidator<CheckDagId, String> {
    private String[] flowPrefixes;

    @Override
    public void initialize(CheckDagId checkDagId) {
        flowPrefixes = checkDagId.value();
    }

    @Override
    public boolean isValid(String enteredValue, ConstraintValidatorContext constraintValidatorContext) {
        return !Arrays.stream(flowPrefixes).filter(
                enteredValue::startsWith
        ).toList().isEmpty() && !enteredValue.endsWith("_") && !enteredValue.matches(".*[^a-zA-Z0-9_].*");
    }
}
