package com.example;

import com.example.dto.TransferRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransferRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Valid TransferRequest passes all validations")
    void testValidRequest() {
        TransferRequest request = new TransferRequest(1L, 2L, new BigDecimal("500.00"), "KEY-001");

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Amount of zero or negative fails @DecimalMin validation")
    void testInvalidAmount() {
        TransferRequest zeroAmount = new TransferRequest(1L, 2L, new BigDecimal("0.00"), "KEY-002");
        TransferRequest negativeAmount = new TransferRequest(1L, 2L, new BigDecimal("-100.00"), "KEY-003");

        Set<ConstraintViolation<TransferRequest>> zeroViolations = validator.validate(zeroAmount);
        Set<ConstraintViolation<TransferRequest>> negativeViolations = validator.validate(negativeAmount);

        assertFalse(zeroViolations.isEmpty());
        assertFalse(negativeViolations.isEmpty());
    }

    @Test
    @DisplayName("Null fields fail @NotNull and @NotBlank validations")
    void testNullFields() {
        TransferRequest request = new TransferRequest(null, null, null, null);

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        assertEquals(4, violations.size());
    }
}
