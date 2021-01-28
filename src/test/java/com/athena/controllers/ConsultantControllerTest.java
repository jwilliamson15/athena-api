package com.athena.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.repository.ConsultantRepository;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ConsultantControllerTest {

    private ConsultantRepository mockRepository;
    private ConsultantsController controller;

    private static Logger LOGGER = LoggerFactory.getLogger(ConsultantControllerTest.class);

    @BeforeEach
    void setup() {
        mockRepository = mock(ConsultantRepository.class);
        controller = new ConsultantsController(mockRepository);
    }

    @Test
    void isNotNullOrEmptyTest () {
        List<String> emptyList = new ArrayList<>();
        List<String> populatedList = new ArrayList<>();
        populatedList.add("Hi");

        assertFalse(controller.isNotNullOrEmpty(null));
        assertFalse(controller.isNotNullOrEmpty(emptyList));
        assertTrue(controller.isNotNullOrEmpty(populatedList));
    }
}