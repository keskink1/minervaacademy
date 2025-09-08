package com.keskin.minerva.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDateConstraint {
    String message() default "Date of birth must be after 01-01-1900";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
