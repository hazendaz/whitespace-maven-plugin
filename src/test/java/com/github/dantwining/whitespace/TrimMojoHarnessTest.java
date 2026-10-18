/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoParameter;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests {@link TrimMojo} using the Maven Plugin Testing Harness JUnit 5 extension.
 */
@MojoTest
class TrimMojoHarnessTest {

    /**
     * Harness test: trim mojo is correctly instantiated from POM configuration.
     *
     * @param mojo
     *            the configured mojo
     */
    @Test
    @InjectMojo(goal = "trim", pom = "src/test/resources/trim/pom.xml")
    void lookupTrimMojoFromPom(TrimMojo mojo) {
        assertNotNull(mojo, "TrimMojo should not be null when injected from POM");
    }

    /**
     * Harness test: trim mojo executes successfully against a temporary directory.
     *
     * @param mojo
     *            the configured mojo
     * @param tempDir
     *            a temporary directory populated with test XML files
     *
     * @throws IOException
     *             if file creation fails
     */
    @Test
    @InjectMojo(goal = "trim", pom = "src/test/resources/trim/pom.xml")
    @MojoParameter(name = "extensions", value = "xml")
    @MojoParameter(name = "encoding", value = "UTF-8")
    @MojoParameter(name = "skip", value = "false")
    @MojoParameter(name = "failOnReadError", value = "false")
    void trimMojoExecutesSuccessfully(TrimMojo mojo, @TempDir Path tempDir) throws IOException {
        populateWithTrailingWhitespace(tempDir, "trim.xml");
        setProjectBasedir(mojo, tempDir.toFile());

        assertNotNull(mojo);
        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Harness test: trim mojo skips execution when skip is set to true.
     *
     * @param mojo
     *            the configured mojo
     * @param tempDir
     *            a temporary directory
     *
     * @throws IOException
     *             if file creation fails
     */
    @Test
    @InjectMojo(goal = "trim", pom = "src/test/resources/trim/pom.xml")
    @MojoParameter(name = "extensions", value = "xml")
    @MojoParameter(name = "encoding", value = "UTF-8")
    @MojoParameter(name = "skip", value = "true")
    void trimMojoSkipsWhenConfigured(TrimMojo mojo, @TempDir Path tempDir) throws IOException {
        populateWithTrailingWhitespace(tempDir, "skip.xml");
        setProjectBasedir(mojo, tempDir.toFile());

        assertNotNull(mojo);
        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Harness test: trim mojo handles a non-existent directory gracefully.
     *
     * @param mojo
     *            the configured mojo
     * @param tempDir
     *            a temporary directory used to derive a non-existent sub-path
     */
    @Test
    @InjectMojo(goal = "trim", pom = "src/test/resources/trim/pom.xml")
    @MojoParameter(name = "extensions", value = "xml")
    @MojoParameter(name = "encoding", value = "UTF-8")
    @MojoParameter(name = "skip", value = "false")
    void trimMojoHandlesNonExistentDirectory(TrimMojo mojo, @TempDir Path tempDir) {
        setProjectBasedir(mojo, tempDir.resolve("nonexistent").toFile());

        assertNotNull(mojo);
        assertDoesNotThrow(mojo::execute);
    }

    /**
     * Sets the mojo project base directory.
     *
     * @param mojo
     *            the mojo to configure
     * @param projectBasedir
     *            the directory to process
     */
    private static void setProjectBasedir(TrimMojo mojo, File projectBasedir) {
        try {
            Field field = TrimMojo.class.getDeclaredField("projectBasedir");
            field.setAccessible(true);
            field.set(mojo, projectBasedir);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to set projectBasedir", exception);
        }
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
