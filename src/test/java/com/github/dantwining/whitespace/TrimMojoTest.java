/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
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
