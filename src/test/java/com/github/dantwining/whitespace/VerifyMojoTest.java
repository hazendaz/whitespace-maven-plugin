/*
 * whitespace-maven-plugin (https://github.com/hazendaz/whitespace-maven-plugin)
 *
 * Copyright 2011-2025 dantwining, Hazendaz.
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

import java.nio.file.Path;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

/**
 * The Class VerifyMojoTest.
 */
public class VerifyMojoTest {

    /** The mojo. */
    private VerifyMojo mojo;

    /**
     * Verify test will pass due to no trailing spaces.
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     * @throws MojoFailureException
     *             the mojo failure exception
     */
    @Test
    void execute() throws MojoExecutionException, MojoFailureException {
        mojo = new VerifyMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", Path.of("target/test-classes/verify").toFile());
        Whitebox.setInternalState(mojo, "extensions", "properties");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        mojo.execute();
    }

    /**
     * Verify test will fail due to trailing spaces.
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     * @throws MojoFailureException
     *             the mojo failure exception
     */
    @Test
    void executeFailureTest() throws MojoExecutionException, MojoFailureException {
        mojo = new VerifyMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", Path.of("target/test-classes/verify").toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        Assertions.assertThrows(MojoFailureException.class, () -> {
            mojo.execute();
        });
    }

}
