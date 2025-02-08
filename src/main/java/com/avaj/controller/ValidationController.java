package com.avaj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationController {

    private static final Logger logger = LoggerFactory.getLogger(ValidationController.class);
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    /**
     * Validates any object using Bean Validation API.
     *
     * @param object Object to validate
     * @param <T>    Type of object
     * @return true if object is valid, otherwise false
     */
    public <T> boolean validate(T object)
    {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            logValidationErrors(violations);
            return false;
        }
        return true;
    }

    /**
     * Logs validation errors.
     *
     * @param violations Set of constraint violations
     * @param <T>        Type of object
     */
    private <T> void logValidationErrors(Set<ConstraintViolation<T>> violations) {
        List<String> errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        logger.warn("Validation failed with errors: {}", errorMessages);
    }
}
