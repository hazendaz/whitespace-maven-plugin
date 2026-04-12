/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * The Class TrimMojoHarnessTest uses the Maven Plugin Testing Harness to test TrimMojo.
 * <p>
 * Tests are written using JUnit 5 while leveraging {@link org.apache.maven.plugin.testing.AbstractMojoTestCase} for
 * Plexus container setup and mojo lookup by goal name from a POM file.
 * </p>
 */
public class TrimMojoHarnessTest {

    /** The harness delegate providing Plexus container and mojo lookup utilities. */
    private final MojoTestHarness harness = new MojoTestHarness();

    /**
     * Instantiates a new trim mojo harness test.
     */
    public TrimMojoHarnessTest() {
        // Default constructor
    }

    /**
     * Sets up the Plexus container before each test.
     *
     * @throws Exception
     *             if the harness setup fails
     */
    @BeforeEach
    void setUp() throws Exception {
        harness.setUp();
    }

    /**
     * Tears down the Plexus container after each test.
     *
     * @throws Exception
     *             if the harness teardown fails
     */
    @AfterEach
    void tearDown() throws Exception {
        harness.tearDown();
    }

    /**
     * Harness test: trim mojo is correctly looked up from POM configuration.
     *
     * @throws Exception
     *             if the mojo lookup fails
     */
    @Test
    void lookupTrimMojoFromPom() throws Exception {
        File pom = harness.resolveTestFile("src/test/resources/trim/pom.xml");
        assertNotNull(pom);

        TrimMojo mojo = (TrimMojo) harness.lookupMojo("trim", pom);
        assertNotNull(mojo, "TrimMojo should not be null when looked up from POM");
    }

    /**
     * Harness test: trim mojo executes successfully against a directory with whitespace files.
     *
     * @param tempDir
     *            a temporary directory populated with test XML files
     *
     * @throws Exception
     *             if the mojo lookup or execution fails
     */
    @Test
    void trimMojoExecutesSuccessfully(@TempDir Path tempDir) throws Exception {
        populateWithTrailingWhitespace(tempDir, "trim.xml");

        File pom = harness.resolveTestFile("src/test/resources/trim/pom.xml");
        TrimMojo mojo = (TrimMojo) harness.lookupMojo("trim", pom);
        assertNotNull(mojo);

        harness.setVariableValueToObject(mojo, "projectBasedir", tempDir.toFile());
        harness.setVariableValueToObject(mojo, "extensions", "xml");
        harness.setVariableValueToObject(mojo, "encoding", "UTF-8");
        harness.setVariableValueToObject(mojo, "skip", false);
        harness.setVariableValueToObject(mojo, "failOnReadError", false);

        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Harness test: trim mojo skips execution when skip is set to true.
     *
     * @param tempDir
     *            a temporary directory
     *
     * @throws Exception
     *             if the mojo lookup fails
     */
    @Test
    void trimMojoSkipsWhenConfigured(@TempDir Path tempDir) throws Exception {
        populateWithTrailingWhitespace(tempDir, "skip.xml");

        File pom = harness.resolveTestFile("src/test/resources/trim/pom.xml");
        TrimMojo mojo = (TrimMojo) harness.lookupMojo("trim", pom);
        assertNotNull(mojo);

        harness.setVariableValueToObject(mojo, "projectBasedir", tempDir.toFile());
        harness.setVariableValueToObject(mojo, "extensions", "xml");
        harness.setVariableValueToObject(mojo, "encoding", "UTF-8");
        harness.setVariableValueToObject(mojo, "skip", true);

        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Harness test: trim mojo handles non-existent directory gracefully.
     *
     * @param tempDir
     *            a temporary directory used to derive a non-existent sub-path
     *
     * @throws Exception
     *             if the mojo lookup fails
     */
    @Test
    void trimMojoHandlesNonExistentDirectory(@TempDir Path tempDir) throws Exception {
        File pom = harness.resolveTestFile("src/test/resources/trim/pom.xml");
        TrimMojo mojo = (TrimMojo) harness.lookupMojo("trim", pom);
        assertNotNull(mojo);

        harness.setVariableValueToObject(mojo, "projectBasedir", tempDir.resolve("nonexistent").toFile());
        harness.setVariableValueToObject(mojo, "extensions", "xml");
        harness.setVariableValueToObject(mojo, "encoding", "UTF-8");
        harness.setVariableValueToObject(mojo, "skip", false);

        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Populates the given directory with a single XML file containing trailing whitespace.
     *
     * @param dir
     *            the target directory
     * @param filename
     *            the filename to create
     *
     * @throws IOException
     *             if file creation fails
     */
    private void populateWithTrailingWhitespace(Path dir, String filename) throws IOException {
        Files.writeString(dir.resolve(filename), "<root>trailing   \n</root>\n");
    }

}
