package com.klimov.etl.vol_work.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfigValidator.class)
public @interface CheckConfNotNullIfRunWithConf {

    String message() default "have to add config";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
