/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * The Class VerifyMojoHarnessTest uses the Maven Plugin Testing Harness to test VerifyMojo.
 * <p>
 * Tests are written using JUnit 5 while leveraging {@link org.apache.maven.plugin.testing.AbstractMojoTestCase} for
 * Plexus container setup and mojo lookup by goal name from a POM file.
 * </p>
 */
public class VerifyMojoHarnessTest {

    /** The harness delegate providing Plexus container and mojo lookup utilities. */
    private final MojoTestHarness harness = new MojoTestHarness();

    /**
     * Instantiates a new verify mojo harness test.
     */
    public VerifyMojoHarnessTest() {
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
     * Harness test: verify mojo is correctly looked up from POM configuration.
     *
     * @throws Exception
     *             if the mojo lookup fails
     */
    @Test
    void lookupVerifyMojoFromPom() throws Exception {
        File pom = harness.resolveTestFile("src/test/resources/verify/pom.xml");
        assertNotNull(pom);

        VerifyMojo mojo = (VerifyMojo) harness.lookupMojo("verify", pom);
        assertNotNull(mojo, "VerifyMojo should not be null when looked up from POM");
    }

    /**
     * Harness test: verify mojo passes when all files are clean.
     *
     * @param tempDir
     *            a temporary directory with clean XML files
     *
     * @throws Exception
     *             if the mojo lookup or execution fails
     */
    @Test
    void verifyMojoPassesWithCleanFiles(@TempDir Path tempDir) throws Exception {
        Files.writeString(tempDir.resolve("clean.xml"), "<root>clean content</root>\n", StandardCharsets.UTF_8);

        File pom = harness.resolveTestFile("src/test/resources/verify/pom.xml");
        VerifyMojo mojo = (VerifyMojo) harness.lookupMojo("verify", pom);
        assertNotNull(mojo);

        harness.setVariableValueToObject(mojo, "projectBasedir", tempDir.toFile());
        harness.setVariableValueToObject(mojo, "extensions", "xml");
        harness.setVariableValueToObject(mojo, "encoding", "UTF-8");
        harness.setVariableValueToObject(mojo, "failOnReadError", false);

        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Harness test: verify mojo fails when trailing whitespace is found.
     *
     * @param tempDir
     *            a temporary directory with dirty XML files
     *
     * @throws Exception
     *             if the mojo lookup fails
     */
    @Test
    void verifyMojoFailsWithTrailingWhitespace(@TempDir Path tempDir) throws Exception {
        Files.writeString(tempDir.resolve("dirty.xml"), "<root>trailing   \n</root>\n", StandardCharsets.UTF_8);

        File pom = harness.resolveTestFile("src/test/resources/verify/pom.xml");
        VerifyMojo mojo = (VerifyMojo) harness.lookupMojo("verify", pom);
        assertNotNull(mojo);

        harness.setVariableValueToObject(mojo, "projectBasedir", tempDir.toFile());
        harness.setVariableValueToObject(mojo, "extensions", "xml");
        harness.setVariableValueToObject(mojo, "encoding", "UTF-8");
        harness.setVariableValueToObject(mojo, "failOnReadError", false);

        assertThrows(MojoFailureException.class, mojo::execute);
    }

    /**
     * Harness test: verify mojo handles non-existent directory gracefully.
     *
     * @param tempDir
     *            a temporary directory used to derive a non-existent sub-path
     *
     * @throws Exception
     *             if the mojo lookup fails
     */
    @Test
    void verifyMojoHandlesNonExistentDirectory(@TempDir Path tempDir) throws Exception {
        File pom = harness.resolveTestFile("src/test/resources/verify/pom.xml");
        VerifyMojo mojo = (VerifyMojo) harness.lookupMojo("verify", pom);
        assertNotNull(mojo);

        harness.setVariableValueToObject(mojo, "projectBasedir", tempDir.resolve("nonexistent").toFile());
        harness.setVariableValueToObject(mojo, "extensions", "xml");
        harness.setVariableValueToObject(mojo, "encoding", "UTF-8");
        harness.setVariableValueToObject(mojo, "failOnReadError", false);

        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Harness test: verify mojo processes multiple extensions and detects whitespace in any matched file.
     *
     * @param tempDir
     *            a temporary directory with mixed file types
     *
     * @throws IOException
     *             if file creation fails
     * @throws Exception
     *             if the mojo lookup fails
     */
    @Test
    void verifyMojoProcessesMultipleExtensions(@TempDir Path tempDir) throws Exception {
        Files.writeString(tempDir.resolve("clean.xml"), "<root>clean</root>\n", StandardCharsets.UTF_8);
        Files.writeString(tempDir.resolve("dirty.properties"), "key=value   \n", StandardCharsets.UTF_8);

        File pom = harness.resolveTestFile("src/test/resources/verify/pom.xml");
        VerifyMojo mojo = (VerifyMojo) harness.lookupMojo("verify", pom);
        assertNotNull(mojo);

        harness.setVariableValueToObject(mojo, "projectBasedir", tempDir.toFile());
        harness.setVariableValueToObject(mojo, "extensions", "xml,properties");
        harness.setVariableValueToObject(mojo, "encoding", "UTF-8");
        harness.setVariableValueToObject(mojo, "failOnReadError", false);

        assertThrows(MojoFailureException.class, mojo::execute);
    }

}
