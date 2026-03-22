/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.dantwining.whitespace;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which detects whitespace in all requested extension files.
 */
@Mojo(name = "verify", defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class VerifyMojo extends AbstractMojo {

    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.basedir}/src", required = true)
    private File projectBasedir;

    /**
     * The character encoding scheme to be applied when filtering resources.
     *
     * @since 1.3.0
     */
    @Parameter(property = "encoding", defaultValue = "${project.build.sourceEncoding}")
    private String encoding;

    /**
     * File extensions to process.
     */
    @Parameter(defaultValue = Defaults.WHITESPACE_DEFAULT_EXTENSIONS)
    private String extensions;

    /**
     * The fail on read error.
     *
     * @since 1.6.0
     */
    @Parameter(property = "failOnReadError", defaultValue = "false")
    private boolean failOnReadError;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        boolean verify = true;
        Log mavenLog = getLog();
        WhitespaceUtils.detectWhitespace(verify, projectBasedir, extensions, mavenLog, encoding, failOnReadError);
    }

}
