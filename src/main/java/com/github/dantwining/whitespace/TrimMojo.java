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

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which trims whitespace from all requested extension files.
 */
@Mojo(name = "trim", defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class TrimMojo extends AbstractMojo {

    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.basedir}/src", required = true)
    private File projectBasedir;

    /**
     * File extensions to process.
     *
     * @since 1.1.0
     */
    @Parameter(defaultValue = "css,groovy,html,java,js,json,jsp,jspx,kt,md,properties,scala,sh,wsdl,xhtml,xml,xsd,yaml,yml")
    private String extensions;

    /**
     * Skip run of plugin.
     *
     * @since 1.2.0
     */
    @Parameter(defaultValue = "false", property = "whitespace.skip")
    private boolean skip;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // Check if plugin run should be skipped
        if (this.skip) {
            getLog().info("Whitespace is skipped");
            return;
        }

        boolean verify = false;
        Log mavenLog = getLog();
        WhitespaceUtils.detectWhitespace(verify, projectBasedir, extensions, mavenLog);
    }

}
