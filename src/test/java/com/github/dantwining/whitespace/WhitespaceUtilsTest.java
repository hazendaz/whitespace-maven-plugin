/*
 * whitespace-maven-plugin (https://github.com/hazendaz/whitespace-maven-plugin)
 *
 * Copyright 2011-2023 dantwining, Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * https://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     dantwining (dantwining).
 *     Hazendaz (Jeremy Landis).
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
