package com.github.dantwining.whitespace;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitespaceUtils {

	private WhitespaceUtils() {
		throw new AssertionError("Utility class: should never be instantiated");
	}

	public static void detectWhitespace(boolean verify, File searchBaseDirectory) throws MojoExecutionException, MojoFailureException {

		String[] extensions = {"java", "xml"};
		Collection<File> matchingFiles = FileUtils.listFiles(searchBaseDirectory, extensions, true);

		for (File matchingFile : matchingFiles) {
			System.out.println("matching file: " + matchingFile.getAbsolutePath());

			List<String> lines;
			try {
				lines = FileUtils.readLines(matchingFile, "UTF-8");
			} catch (IOException e) {
				throw new MojoExecutionException("Failed to read lines from " + matchingFile.getAbsolutePath(), e);
			}

			Boolean isFileModified = false;
			List<String> trimmedLines = new ArrayList<String>(lines.size());
			for (String line : lines) {

				String trimmedLine = StringUtils.stripEnd(line, null);

				Boolean isLineModified = (trimmedLine.equals(line));
				isFileModified = (isFileModified || isLineModified);

				trimmedLines.add(trimmedLine);

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