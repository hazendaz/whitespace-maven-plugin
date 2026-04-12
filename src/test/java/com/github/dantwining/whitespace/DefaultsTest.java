/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * The Class DefaultsTest.
 */
public class DefaultsTest {

    /**
     * Instantiates a new defaults test.
     */
    public DefaultsTest() {
        // Default constructor
    }

    /**
     * Javabean tester tests the private constructor.
     */
    @Test
    void javabeanTester() {
        JavaBeanTester.builder(Defaults.class).testPrivateConstructor();
    }

    /**
     * Default extensions are not null or empty.
     */
    @Test
    void defaultExtensionsNotNullOrEmpty() {
        assertNotNull(Defaults.WHITESPACE_DEFAULT_EXTENSIONS);
        assertFalse(Defaults.WHITESPACE_DEFAULT_EXTENSIONS.isEmpty());
    }

    /**
     * Default extensions contain java.
     */
    @Test
    void defaultExtensionsContainsJava() {
        assertTrue(Defaults.WHITESPACE_DEFAULT_EXTENSIONS.contains("java"));
    }

    /**
     * Default extensions contain xml.
     */
    @Test
    void defaultExtensionsContainsXml() {
        assertTrue(Defaults.WHITESPACE_DEFAULT_EXTENSIONS.contains("xml"));
    }

    /**
     * Default extensions contain yaml.
     */
    @Test
    void defaultExtensionsContainsYaml() {
        assertTrue(Defaults.WHITESPACE_DEFAULT_EXTENSIONS.contains("yaml"));
    }

    /**
     * Default extensions do not contain spaces.
     */
    @Test
    void defaultExtensionsHaveNoSpaces() {
        assertFalse(Defaults.WHITESPACE_DEFAULT_EXTENSIONS.contains(" "));
    }

}
