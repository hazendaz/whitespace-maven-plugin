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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/**
 * The Class WhitespaceUtils.
 */
public final class WhitespaceUtils {

    /**
     * Instantiates a new whitespace utils.
     */
    private WhitespaceUtils() {
        // Do not instantiate
    }

    /**
     * Detect whitespace.
     *
     * @param verify
     *            the verify
     * @param searchBaseDirectory
     *            the search base directory
     * @param extensions
     *            the extensions
     * @param mavenLog
     *            the maven log
     * @param encoding
     *            the character encoding used for resources
     * @param failOnReadError
     *            the boolean flag to choose treatment for read errors
     *
     * @throws MojoExecutionException
     *             the mojo execution exception
     * @throws MojoFailureException
     *             the mojo failure exception
     */
    public static void detectWhitespace(boolean verify, File searchBaseDirectory, String extensions, Log mavenLog,
            String encoding, boolean failOnReadError) throws MojoExecutionException, MojoFailureException {

        if (!searchBaseDirectory.isDirectory()) {
            mavenLog.debug("Skipping non-existent directory: " + searchBaseDirectory.getAbsolutePath());
            return;
        }
        String cleanExtensions = StringUtils.deleteWhitespace(extensions);
        mavenLog.info("Processing the following extensions: " + cleanExtensions);

        Collection<File> matchingFiles = FileUtils.listFiles(searchBaseDirectory, cleanExtensions.split(","), true);

        List<String> verifyFailed = new ArrayList<>();

        for (File matchingFile : matchingFiles) {
            mavenLog.debug("Reading file: " + matchingFile.getAbsolutePath());

            List<String> lines;
            try {
                lines = Files.readAllLines(matchingFile.toPath(), Charset.forName(encoding));
            } catch (IOException e) {
                if (failOnReadError) {
                    throw new MojoExecutionException(
                            "Failed to read lines from " + matchingFile.getAbsolutePath() + ": " + e.getMessage(), e);
                } else {
                    mavenLog.warn(
                            "Failed to read lines from " + matchingFile.getAbsolutePath() + ": " + e.getMessage());
                 // Skip this file and continue processing others
                    continue;
                }
            }

            boolean isFileModified = false;
            List<String> trimmedLines = new ArrayList<>(lines.size());
            int lineNumber = 0;

            for (String line : lines) {

                if (mavenLog.isDebugEnabled()) {
                    lineNumber++;
                }

                String trimmedLine = StringUtils.stripEnd(line, null);

                boolean isLineModified = !trimmedLine.equals(line);

                if (mavenLog.isDebugEnabled() && isLineModified) {
                    mavenLog.debug("Whitespace found on line " + lineNumber);
                }

                trimmedLines.add(trimmedLine);

                isFileModified = isFileModified || isLineModified;
            }

            if (isFileModified) {

                if (verify) {
                    verifyFailed.add(matchingFile.getAbsolutePath());
                } else {
                    try {
                        Files.write(matchingFile.toPath(), trimmedLines, Charset.forName(encoding),
                                StandardOpenOption.TRUNCATE_EXISTING);
                    } catch (IOException e) {
                        throw new MojoExecutionException("Failed to write lines to " + matchingFile.getAbsolutePath(),
                                e);
                    }
                }

            }
        }

        if (!verifyFailed.isEmpty()) {
            throw new MojoFailureException("Trailing whitespace found in " + verifyFailed);
        }
    }

}
