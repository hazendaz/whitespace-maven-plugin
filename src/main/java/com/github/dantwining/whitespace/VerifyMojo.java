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
    @Parameter(defaultValue = "css,groovy,html,java,js,json,kt,md,properties,scala,sh,wsdl,xhtml,xml,xsd,yaml,yml")
    private String extensions;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        boolean verify = true;
        Log mavenLog = getLog();
        WhitespaceUtils.detectWhitespace(verify, projectBasedir, extensions, mavenLog, encoding);
    }

}
