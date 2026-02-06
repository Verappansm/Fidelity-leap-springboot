package org.example;

import org.example.dto.TransferRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferRequestValidationTest {

    @Test
    void testValidRequest() {
        TransferRequest req = new TransferRequest("A1", "A2", 100.0, "key123");
        assertNotNull(req);
    }

    @Test
    void testInvalidAmount() {
        assertThrows(Exception.class, () ->
                new TransferRequest("A1", "A2", -10.0, "key123"));
    }

    @Test
    void testNullFields() {
        assertThrows(Exception.class, () ->
                new TransferRequest(null, null, null, null));
    }
}
