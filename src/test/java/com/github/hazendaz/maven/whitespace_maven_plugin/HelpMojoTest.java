/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.github.hazendaz.maven.whitespace_maven_plugin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * The Class HelpMojoTest.
 */
public class HelpMojoTest {

    /**
     * Instantiates a new help mojo test.
     */
    public HelpMojoTest() {
        // Default constructor
    }

    /**
     * Execute covers invalid line and indent values and detailed output.
     */
    @Test
    void executeWithInvalidConfiguration() {
        HelpMojo mojo = new HelpMojo();
        Log mockLog = mock(Log.class);
        when(mockLog.isInfoEnabled()).thenReturn(true);
        mojo.setLog(mockLog);
        Whitebox.setInternalState(mojo, "lineLength", 0);
        Whitebox.setInternalState(mojo, "indentSize", 0);
        Whitebox.setInternalState(mojo, "detail", true);

        assertDoesNotThrow(mojo::execute);

        verify(mockLog).warn(contains("lineLength"));
        verify(mockLog).warn(contains("indentSize"));
        verify(mockLog).info(contains("Available parameters:"));
        verify(mockLog).info(contains("User property: project.build.sourceEncoding"));
    }

    /**
     * Execute with a specific goal should emit only that goal output.
     */
    @Test
    void executeWithSpecificGoal() {
        HelpMojo mojo = new HelpMojo();
        Log mockLog = mock(Log.class);
        when(mockLog.isInfoEnabled()).thenReturn(true);
        mojo.setLog(mockLog);
        Whitebox.setInternalState(mojo, "goal", "verify");
        Whitebox.setInternalState(mojo, "detail", false);

        assertDoesNotThrow(mojo::execute);

        verify(mockLog).info(contains("whitespace:verify"));
    }

    /**
     * getPropertyFromExpression handles valid and invalid expressions.
     */
    @Test
    void getPropertyFromExpression() throws Exception {
        assertEquals("some.property", invokePrivateStaticString("getPropertyFromExpression", "${some.property}"));
        assertNull(invokePrivateStaticString("getPropertyFromExpression", "some.property"));
        assertNull(invokePrivateStaticString("getPropertyFromExpression", "${some.${property}}"));
    }

    /**
     * toLines and getIndentLevel handle wrapping and indentation details.
     */
    @SuppressWarnings("unchecked")
    @Test
    void toLinesAndIndentLevel() throws Exception {
        Method toLines = HelpMojo.class.getDeclaredMethod("toLines", String.class, int.class, int.class, int.class);
        toLines.setAccessible(true);
        List<String> lines = (List<String>) toLines.invoke(null, "\tabc def ghi\u00A0jkl", 1, 2, 10);
        assertNotNull(lines);
        assertTrue(lines.size() >= 2);

        Method getIndentLevel = HelpMojo.class.getDeclaredMethod("getIndentLevel", String.class);
        getIndentLevel.setAccessible(true);
        int indent = (int) getIndentLevel.invoke(null, "\t\tvalue");
        assertTrue(indent >= 2);
    }

    /**
     * getSingleChild and findSingleChild throw on duplicates and missing child elements.
     */
    @Test
    void xmlChildHelpersErrorPaths() throws Exception {
        Document duplicateDoc = buildXml("<root><a>1</a><a>2</a></root>");
        Document missingDoc = buildXml("<root></root>");

        assertThrows(MojoExecutionException.class, () -> invokePrivateNode("getSingleChild", duplicateDoc, "a"));
        assertThrows(MojoExecutionException.class, () -> invokePrivateNode("getSingleChild", missingDoc, "a"));
        assertThrows(MojoExecutionException.class, () -> invokePrivateNode("findSingleChild", duplicateDoc, "a"));
        assertNull(invokePrivateNode("findSingleChild", missingDoc, "a"));
    }

    private static Document buildXml(String xml) throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new java.io.ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    private static String invokePrivateStaticString(String methodName, String argument) throws Exception {
        Method method = HelpMojo.class.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        return (String) method.invoke(null, argument);
    }

    private static Node invokePrivateNode(String methodName, Document doc, String childName) throws Exception {
        Method method = HelpMojo.class.getDeclaredMethod(methodName, Node.class, String.class);
        method.setAccessible(true);
        try {
            return (Node) method.invoke(null, doc.getDocumentElement(), childName);
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof Exception cause) {
                throw cause;
            }
            throw ex;
        }
    }
}
