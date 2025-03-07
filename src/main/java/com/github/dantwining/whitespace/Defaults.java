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

public final class Defaults {

    /**
     * Default extension list for both trim and verify mojos.
     */
    public static final String WHITESPACE_DEFAULT_EXTENSIONS = "4gl,42m,c,cb,cfg,cpp,css,dat,groovy,h,html,ini,java,js,json,jsonp,jsp,jspx,jsx,ksh,kt,kts,md,mk,msg,pl,pm,properties,reg,scala,scss,sh,sql,toml,ts,tsx,wadl,wsdl,xhtml,xml,xsd,yaml,yml";

    /**
     * Prevent instantiation of this class.
     */
    private Defaults() {
        // Do not allow instantiation
    }

}
