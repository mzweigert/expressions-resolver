/*
 * Copyright (c) 2019. BEST S.A. and/or its affiliates. All rights reserved.
 */
package com.mzweigert.expressions_resolver.resolver;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

public class FilesProcessingTaskResult {

	private Collection<File> successProcessedFiles;
	private Collection<File> failProcessedFiles;

	public FilesProcessingTaskResult(Collection<File> successProcessedFiles, Collection<File> failProcessedFiles) {
		this.successProcessedFiles = successProcessedFiles;
		this.failProcessedFiles = failProcessedFiles;
	}

	Collection<File> getSuccessProcessedFiles() {
		return successProcessedFiles;
	}

	Collection<File> getFailProcessedFiles() {
		return failProcessedFiles;
	}

	public int getSuccessProcessedFilesSize() {
		return successProcessedFiles.size();
	}

	public int getFailProcessedFilesSize() {
		return failProcessedFiles.size();
	}

	public Collection<String> getFailProcessedFilesNames() {
		return failProcessedFiles.stream()
				.map(File::getName)
				.collect(Collectors.toSet());
	}

}
