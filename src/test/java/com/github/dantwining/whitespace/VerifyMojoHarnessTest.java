/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoParameter;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests {@link VerifyMojo} using the Maven Plugin Testing Harness JUnit 5 extension.
 */
@MojoTest
class VerifyMojoHarnessTest {

    /**
     * Instantiates a new verify mojo harness test.
     */
    VerifyMojoHarnessTest() {
        // Default constructor
    }

    /**
     * Harness test: verify mojo is correctly instantiated from POM configuration.
     *
     * @param mojo
     *            the configured mojo
     */
    @Test
    @InjectMojo(goal = "verify", pom = "src/test/resources/verify/pom.xml")
    void lookupVerifyMojoFromPom(VerifyMojo mojo) {
        assertNotNull(mojo, "VerifyMojo should not be null when injected from POM");
    }

    /**
     * Harness test: verify mojo passes when all files are clean.
     *
     * @param mojo
     *            the configured mojo
     * @param tempDir
     *            a temporary directory with clean XML files
     *
     * @throws IOException
     *             if file creation fails
     */
    @Test
    @InjectMojo(goal = "verify", pom = "src/test/resources/verify/pom.xml")
    @MojoParameter(name = "extensions", value = "xml")
    @MojoParameter(name = "encoding", value = "UTF-8")
    @MojoParameter(name = "failOnReadError", value = "false")
    void verifyMojoPassesWithCleanFiles(VerifyMojo mojo, @TempDir Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("clean.xml"), "<root>clean content</root>\n", StandardCharsets.UTF_8);
        setField(mojo, "projectBasedir", tempDir.toFile());

        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Harness test: verify mojo fails when trailing whitespace is found.
     *
     * @param mojo
     *            the configured mojo
     * @param tempDir
     *            a temporary directory with dirty XML files
     *
     * @throws IOException
     *             if file creation fails
     * @throws MojoFailureException
     *             if verification detects trailing whitespace
     */
    @Test
    @InjectMojo(goal = "verify", pom = "src/test/resources/verify/pom.xml")
    @MojoParameter(name = "extensions", value = "xml")
    @MojoParameter(name = "encoding", value = "UTF-8")
    @MojoParameter(name = "failOnReadError", value = "false")
    void verifyMojoFailsWithTrailingWhitespace(VerifyMojo mojo, @TempDir Path tempDir)
            throws IOException, MojoFailureException {
        Files.writeString(tempDir.resolve("dirty.xml"), "<root>trailing   \n</root>\n", StandardCharsets.UTF_8);
        setField(mojo, "projectBasedir", tempDir.toFile());

        assertThrows(MojoFailureException.class, mojo::execute);
    }

    /**
     * Harness test: verify mojo handles a non-existent directory gracefully.
     *
     * @param mojo
     *            the configured mojo
     * @param tempDir
     *            a temporary directory used to derive a non-existent sub-path
     */
    @Test
    @InjectMojo(goal = "verify", pom = "src/test/resources/verify/pom.xml")
    @MojoParameter(name = "extensions", value = "xml")
    @MojoParameter(name = "encoding", value = "UTF-8")
    @MojoParameter(name = "failOnReadError", value = "false")
    void verifyMojoHandlesNonExistentDirectory(VerifyMojo mojo, @TempDir Path tempDir) {
        setField(mojo, "projectBasedir", tempDir.resolve("nonexistent").toFile());

        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Harness test: verify mojo processes multiple extensions and detects whitespace in any matched file.
     *
     * @param mojo
     *            the configured mojo
     * @param tempDir
     *            a temporary directory with mixed file types
     *
     * @throws IOException
     *             if file creation fails
     * @throws MojoFailureException
     *             if verification detects trailing whitespace
     */
    @Test
    @InjectMojo(goal = "verify", pom = "src/test/resources/verify/pom.xml")
    @MojoParameter(name = "extensions", value = "xml,properties")
    @MojoParameter(name = "encoding", value = "UTF-8")
    @MojoParameter(name = "failOnReadError", value = "false")
    void verifyMojoProcessesMultipleExtensions(VerifyMojo mojo, @TempDir Path tempDir)
            throws IOException, MojoFailureException {
        Files.writeString(tempDir.resolve("clean.xml"), "<root>clean</root>\n", StandardCharsets.UTF_8);
        Files.writeString(tempDir.resolve("dirty.properties"), "key=value   \n", StandardCharsets.UTF_8);
        setField(mojo, "projectBasedir", tempDir.toFile());

        assertThrows(MojoFailureException.class, mojo::execute);
    }

    /**
     * Sets a field value using reflection.
     *
     * @param target
     *            the object to modify
     * @param name
     *            the field name
     * @param value
     *            the value to assign
     */
    private static void setField(Object target, String name, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to set field '" + name + "'", exception);
        }
    }
}
