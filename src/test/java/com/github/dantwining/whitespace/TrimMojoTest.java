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
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

/**
 * The Class TrimMojoTest.
 */
public class TrimMojoTest {

    /** The mojo. */
    private TrimMojo mojo;

    /**
     * Trim test will successfully trim files.
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     * @throws MojoFailureException
     *             the mojo failure exception
     */
    @Test
    void execute() throws MojoExecutionException, MojoFailureException {
        mojo = new TrimMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", Path.of("target/test-classes/trim").toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        mojo.execute();
    }

    /**
     * Trim test wrong directory.
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     * @throws MojoFailureException
     *             the mojo failure exception
     */
    @Test
    void executeNoProcessing() throws MojoExecutionException, MojoFailureException {
        mojo = new TrimMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", Path.of("junk").toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        mojo.execute();
    }

    /**
     * Trim test will skip processing.
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     * @throws MojoFailureException
     *             the mojo failure exception
     */
    @Test
    void executeSkip() throws MojoExecutionException, MojoFailureException {
        mojo = new TrimMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", Path.of("target/test-classes/trim").toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        Whitebox.setInternalState(mojo, "skip", true);
        mojo.execute();
    }

}
