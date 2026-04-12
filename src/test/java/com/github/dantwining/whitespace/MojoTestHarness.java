/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import java.io.File;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.PlexusTestCase;

/**
 * The Class MojoTestHarness wraps {@link org.apache.maven.plugin.testing.AbstractMojoTestCase} and exposes its
 * protected methods publicly so that JUnit 5 test classes can use delegation instead of inheritance.
 * <p>
 * {@link org.apache.maven.plugin.testing.AbstractMojoTestCase} extends JUnit 3's {@code TestCase}, which is
 * incompatible with JUnit 5's lifecycle annotations. This wrapper class allows JUnit 5 tests to still benefit from the
 * Plexus container setup and mojo lookup utilities provided by the Maven Plugin Testing Harness 3.5.1.
 * </p>
 */
public class MojoTestHarness extends AbstractMojoTestCase {

    /**
     * Instantiates a new mojo test harness.
     */
    public MojoTestHarness() {
        // Default constructor
    }

    /**
     * Initializes the Plexus container. Must be called before any mojo lookups.
     *
     * @throws Exception
     *             if container setup fails
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Shuts down the Plexus container. Must be called after each test.
     *
     * @throws Exception
     *             if container teardown fails
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Looks up a mojo by goal name from the plugin configuration in the given POM file.
     *
     * @param <T>
     *            the mojo type
     * @param goal
     *            the goal name
     * @param pom
     *            the POM file containing plugin configuration
     *
     * @return the configured mojo instance
     *
     * @throws Exception
     *             if the lookup fails
     */
    @Override
    public <T extends Mojo> T lookupMojo(String goal, File pom) throws Exception {
        return super.lookupMojo(goal, pom);
    }

    /**
     * Returns the test file relative to the project base directory using {@link PlexusTestCase#getTestFile(String)}.
     *
     * @param path
     *            the relative path
     *
     * @return the resolved File
     */
    public File resolveTestFile(String path) {
        return PlexusTestCase.getTestFile(path);
    }

    /**
     * Sets a field value on the given object using reflection.
     *
     * @param <T>
     *            the value type
     * @param object
     *            the target object
     * @param variable
     *            the field name
     * @param value
     *            the value to set
     *
     * @throws IllegalAccessException
     *             if the field cannot be accessed
     */
    @Override
    public <T> void setVariableValueToObject(Object object, String variable, T value) throws IllegalAccessException {
        super.setVariableValueToObject(object, variable, value);
    }

}
