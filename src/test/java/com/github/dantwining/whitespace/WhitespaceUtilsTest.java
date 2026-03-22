/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * The Class WhitespaceUtilsTest.
 */
public class WhitespaceUtilsTest {

    /**
     * Javabean tester.
     */
    @Test
    void javabeanTester() {
        JavaBeanTester.builder(WhitespaceUtils.class).testPrivateConstructor();
    }

}
