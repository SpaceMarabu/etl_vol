package com.klimov.etl.vol_work.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DagIdValidator.class)
public @interface CheckDagId {

    public String[] value() default {"cf_", "wf_", "wrk_"};

    public String message() default "not a flow";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
