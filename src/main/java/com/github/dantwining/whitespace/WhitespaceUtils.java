/*
 * whitespace-maven-plugin (https://github.com/hazendaz/whitespace-maven-plugin)
 *
 * Copyright 2011-2023 dantwining, Hazendaz.
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class WhitespaceUtils {

    private WhitespaceUtils() {
        throw new AssertionError("Utility class: should never be instantiated");
    }

    public static void detectWhitespace(boolean verify, File searchBaseDirectory, Log mavenLog) throws MojoExecutionException, MojoFailureException {

        if (!searchBaseDirectory.isDirectory()) {
            mavenLog.debug("Skipping non-existent directory: " + searchBaseDirectory.getAbsolutePath());
            return;
        }

        String[] extensions = {"java", "scala", "xml", "properties", "groovy", "kt", "yaml", "yml", "md", "sh"};
        Collection<File> matchingFiles = FileUtils.listFiles(searchBaseDirectory, extensions, true);

        for (File matchingFile : matchingFiles) {
            mavenLog.debug("Reading file: " + matchingFile.getAbsolutePath());

            List<String> lines;
            try {
                lines = Files.readAllLines(matchingFile.toPath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to read lines from " + matchingFile.getAbsolutePath(), e);
            }

            boolean isFileModified = false;
            List<String> trimmedLines = new ArrayList<>(lines.size());
            int lineNumber = 0;

            for (String line : lines) {

                if(mavenLog.isDebugEnabled()){
                    lineNumber++;
                }

                String trimmedLine = StringUtils.stripEnd(line, null);

                boolean isLineModified = (!trimmedLine.equals(line));

                if(mavenLog.isDebugEnabled()){
                    if(isLineModified){
                        mavenLog.debug("Whitespace found on line " + lineNumber);
                    }
                }

                trimmedLines.add(trimmedLine);

                isFileModified = (isFileModified || isLineModified);
            }

            if (isFileModified) {

                if (verify) {
                    throw new MojoFailureException("Trailing whitespace found in " + matchingFile.getAbsolutePath());
                }
                try {
                    FileUtils.writeLines(matchingFile, "UTF-8", trimmedLines);
                } catch (IOException e) {
                    throw new MojoExecutionException("Failed to write lines to " + matchingFile.getAbsolutePath(), e);
                }

            }
        }
    }

}
