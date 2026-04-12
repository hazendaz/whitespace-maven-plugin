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

    /**
     * Verify test with non-existent directory does not throw an exception.
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     * @throws MojoFailureException
     *             the mojo failure exception
     */
    @Test
    void executeNonExistentDirectory() throws MojoExecutionException, MojoFailureException {
        mojo = new VerifyMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", Path.of("junk-verify").toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        mojo.execute();
    }

    /**
     * Verify test throws MojoExecutionException when failOnReadError is true and file has invalid encoding.
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

        mojo = new VerifyMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", tempDir.toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        Whitebox.setInternalState(mojo, "failOnReadError", true);
        Assertions.assertThrows(MojoExecutionException.class, () -> mojo.execute());
    }

    /**
     * Verify test continues without exception when failOnReadError is false and file has invalid encoding.
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

        mojo = new VerifyMojo();
        Whitebox.setInternalState(mojo, "projectBasedir", tempDir.toFile());
        Whitebox.setInternalState(mojo, "extensions", "xml");
        Whitebox.setInternalState(mojo, "encoding", "UTF-8");
        Whitebox.setInternalState(mojo, "failOnReadError", false);
        Assertions.assertDoesNotThrow(() -> mojo.execute());
    }

}
