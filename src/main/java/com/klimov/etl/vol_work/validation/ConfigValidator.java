package com.klimov.etl.vol_work.validation;

import com.klimov.etl.vol_work.entity.RunType;
import com.klimov.etl.vol_work.entity.UserTaskFromUI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class ConfigValidator implements ConstraintValidator<CheckConfNotNullIfRunWithConf, UserTaskFromUI> {

    private String runTypeFieldName;
    private RunType runTypeTargetValue;
    private String confFieldName;

    @Override
    public void initialize(CheckConfNotNullIfRunWithConf checkConfWithRunType) {
        this.runTypeFieldName = "runType"; //если runType
        this.runTypeTargetValue = RunType.CONFIG; // равно RunType.CONFIG
        this.confFieldName = "listConfRaw"; //то проверяем listConfRaw
    }

    @Override
    public boolean isValid(UserTaskFromUI value, ConstraintValidatorContext context) {
        // value — сам UserTaskFromUI

        try {
            Object runTypeFieldValue = new BeanWrapperImpl(value).getPropertyValue(runTypeFieldName);
            Object confFieldValue = new BeanWrapperImpl(value).getPropertyValue(confFieldName);

            if (runTypeTargetValue.equals(runTypeFieldValue)) {
                if (confFieldValue == null || confFieldValue.toString().trim().isEmpty()) {
                    // Формируем ошибку валидации, "привязывая" её к нужному полю
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                                    context.getDefaultConstraintMessageTemplate()
                            )
                            .addPropertyNode(confFieldName)
                            .addConstraintViolation();

                    return false;
                }
            }

        } catch (Exception e) {
            return false;
        }

        // Во всех прочих случаях (условие не выполнено) — валидно
        return true;
    }
}


