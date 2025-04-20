package com.openclassrooms.starterjwt.payload.request;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenEmailIsBlank_thenShouldFailValidation() {
        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("password");

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenPasswordIsBlank_thenShouldFailValidation() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("");

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenAllFieldsValid_thenShouldPassValidation() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}