/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
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

    /**
     * Trim test throws MojoExecutionException when failOnReadError is true and file has invalid encoding.
     *
     * @param tempDir
     *            the temporary directory
     *
     * @throws IOException
     *             if temp file creation fails
     */
    @Test
    void executeFailOnReadErrorTrue(@TempDir Path tempDir) throws IOException {
        Path badFile = tempDir.resolve("bad.xml");
        Files.write(badFile, new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00 });

        mojo = new TrimMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", tempDir.toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        Whitebox.setInternalState(mojo, "failOnReadError", true);
        Assertions.assertThrows(MojoExecutionException.class, () -> mojo.execute());
    }

    /**
     * Trim test continues without exception when failOnReadError is false and file has invalid encoding.
     *
     * @param tempDir
     *            the temporary directory
     *
     * @throws IOException
     *             if temp file creation fails
     */
    @Test
    void executeFailOnReadErrorFalse(@TempDir Path tempDir) throws IOException {
        Path badFile = tempDir.resolve("bad.xml");
        Files.write(badFile, new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00 });

        mojo = new TrimMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", tempDir.toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        Whitebox.setInternalState(mojo, "failOnReadError", false);
        Assertions.assertDoesNotThrow(() -> mojo.execute());
    }

}
