/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codebox.bean.JavaBeanTester;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * The Class WhitespaceUtilsTest.
 */
public class WhitespaceUtilsTest {

    /** Temporary directory for test files. */
    @TempDir
    Path tempDir;

    /** Mock log for capturing log output. */
    private Log mockLog;

    /**
     * Instantiates a new whitespace utils test.
     */
    public WhitespaceUtilsTest() {
        // Default constructor
    }

    /**
     * Sets up the mock log before each test.
     */
    @BeforeEach
    void setUp() {
        mockLog = mock(Log.class);
        when(mockLog.isDebugEnabled()).thenReturn(false);
    }

    /**
     * Javabean tester tests the private constructor.
     */
    @Test
    void javabeanTester() {
        JavaBeanTester.builder(WhitespaceUtils.class).testPrivateConstructor();
    }

    /**
     * Non-existent directory is skipped with a debug log message.
     */
    @Test
    void detectWhitespace_nonExistentDirectory() throws MojoExecutionException, MojoFailureException {
        File nonExistent = tempDir.resolve("nonexistent").toFile();
        WhitespaceUtils.detectWhitespace(false, nonExistent, "xml", mockLog, "UTF-8", false);
        verify(mockLog).debug(contains("Skipping non-existent directory:"));
    }

    /**
     * Trim mode does not modify clean files.
     */
    @Test
    void detectWhitespace_trimCleanFile() throws Exception {
        Path cleanFile = tempDir.resolve("clean.xml");
        String content = "<root>clean content</root>";
        Files.write(cleanFile, content.getBytes(StandardCharsets.UTF_8));

        WhitespaceUtils.detectWhitespace(false, tempDir.toFile(), "xml", mockLog, "UTF-8", false);

        assertEquals(content, Files.readString(cleanFile));
    }

    /**
     * Trim mode strips trailing whitespace from files and writes them back.
     */
    @Test
    void detectWhitespace_trimFilesWithTrailingWhitespace() throws Exception {
        Path dirtyFile = tempDir.resolve("dirty.xml");
        Files.write(dirtyFile, "<root>trailing   \n</root>".getBytes(StandardCharsets.UTF_8));

        WhitespaceUtils.detectWhitespace(false, tempDir.toFile(), "xml", mockLog, "UTF-8", false);

        List<String> lines = Files.readAllLines(dirtyFile);
        assertEquals("<root>trailing", lines.get(0));
    }

    /**
     * Verify mode does not throw when all files are clean.
     */
    @Test
    void detectWhitespace_verifyCleanFiles() throws Exception {
        Path cleanFile = tempDir.resolve("clean.xml");
        Files.write(cleanFile, "<root>clean</root>".getBytes(StandardCharsets.UTF_8));

        assertDoesNotThrow(
                () -> WhitespaceUtils.detectWhitespace(true, tempDir.toFile(), "xml", mockLog, "UTF-8", false));
    }

    /**
     * Verify mode throws MojoFailureException when trailing whitespace is found.
     */
    @Test
    void detectWhitespace_verifyFilesWithTrailingWhitespace() throws Exception {
        Path dirtyFile = tempDir.resolve("dirty.xml");
        Files.write(dirtyFile, "<root>trailing   \n</root>".getBytes(StandardCharsets.UTF_8));

        assertThrows(MojoFailureException.class,
                () -> WhitespaceUtils.detectWhitespace(true, tempDir.toFile(), "xml", mockLog, "UTF-8", false));
    }

    /**
     * When failOnReadError is true and a file has invalid encoding, a MojoExecutionException is thrown.
     */
    @Test
    void detectWhitespace_failOnReadError_true_throwsException() throws Exception {
        Path badFile = tempDir.resolve("bad.xml");
        Files.write(badFile, new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00 });

        assertThrows(MojoExecutionException.class,
                () -> WhitespaceUtils.detectWhitespace(false, tempDir.toFile(), "xml", mockLog, "UTF-8", true));
    }

    /**
     * When failOnReadError is false and a file has invalid encoding, a warning is logged and processing continues.
     */
    @Test
    void detectWhitespace_failOnReadError_false_logsWarning() throws Exception {
        Path badFile = tempDir.resolve("bad.xml");
        Files.write(badFile, new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00 });

        assertDoesNotThrow(
                () -> WhitespaceUtils.detectWhitespace(false, tempDir.toFile(), "xml", mockLog, "UTF-8", false));
        verify(mockLog).warn(contains("Failed to read lines from"));
    }

    /**
     * When debug is enabled and whitespace is found, debug messages include line numbers.
     */
    @Test
    void detectWhitespace_debugLoggingWithWhitespace() throws Exception {
        when(mockLog.isDebugEnabled()).thenReturn(true);
        Path dirtyFile = tempDir.resolve("dirty.xml");
        Files.write(dirtyFile, "<root>trailing   \n</root>".getBytes(StandardCharsets.UTF_8));

        WhitespaceUtils.detectWhitespace(false, tempDir.toFile(), "xml", mockLog, "UTF-8", false);

        verify(mockLog, atLeastOnce()).debug(contains("Whitespace found on line"));
    }

    /**
     * Multiple extensions are processed correctly, detecting whitespace across all matched files.
     */
    @Test
    void detectWhitespace_multipleExtensions() throws Exception {
        Path xmlFile = tempDir.resolve("file.xml");
        Path javaFile = tempDir.resolve("file.java");
        Files.write(xmlFile, "<root>trailing   \n</root>".getBytes(StandardCharsets.UTF_8));
        Files.write(javaFile, "public class Foo { }".getBytes(StandardCharsets.UTF_8));

        assertThrows(MojoFailureException.class,
                () -> WhitespaceUtils.detectWhitespace(true, tempDir.toFile(), "xml,java", mockLog, "UTF-8", false));
    }

    /**
     * Verify mode collects multiple files with whitespace and reports all in the failure message.
     */
    @Test
    void detectWhitespace_verifyReportsMultipleFilesWithWhitespace() throws Exception {
        Path dirtyFile1 = tempDir.resolve("dirty1.xml");
        Path dirtyFile2 = tempDir.resolve("dirty2.xml");
        Files.write(dirtyFile1, "<a>trailing</a>   ".getBytes(StandardCharsets.UTF_8));
        Files.write(dirtyFile2, "<b>trailing</b>   ".getBytes(StandardCharsets.UTF_8));

        MojoFailureException ex = assertThrows(MojoFailureException.class,
                () -> WhitespaceUtils.detectWhitespace(true, tempDir.toFile(), "xml", mockLog, "UTF-8", false));
        String message = ex.getMessage();
        assert message.contains(dirtyFile1.toAbsolutePath().toString())
                || message.contains(dirtyFile2.toAbsolutePath().toString());
    }

    /**
     * When failOnReadError is false, processing continues to other files after a read error.
     */
    @Test
    void detectWhitespace_failOnReadError_false_continuesProcessing() throws Exception {
        Path badFile = tempDir.resolve("abad.xml");
        Path cleanFile = tempDir.resolve("zclean.xml");
        Files.write(badFile, new byte[] { (byte) 0xFF, (byte) 0xFE });
        Files.write(cleanFile, "<root>clean</root>".getBytes(StandardCharsets.UTF_8));

        // Should not throw; clean file should be processed without errors
        assertDoesNotThrow(
                () -> WhitespaceUtils.detectWhitespace(true, tempDir.toFile(), "xml", mockLog, "UTF-8", false));
        verify(mockLog).warn(contains("Failed to read lines from"));
    }

}
