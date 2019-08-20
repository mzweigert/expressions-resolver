package com.mzweigert.expressions_resolver.configuration;

import com.mzweigert.expressions_resolver.TestUtilsIT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FileManagerIT {


	private FileManager manager = new FileManager();

	private File outputDir;

	@Before
	public void setUp() {
		outputDir = new File("output_test");
		outputDir.mkdir();
	}

	@After
	public void clean() {
		TestUtilsIT.deleteFiles(outputDir);
	}

	@Test
	public void saveErrorToFile() throws IOException {
		//GIVEN
		JAXBException exception = new JAXBException("Error");
		File file = new File(outputDir, "test.xml");
		file.createNewFile();

		//WHEN
		manager.saveErrorToFile(exception, file);

		//THEN
		File[] files = outputDir.listFiles();
		assertThat(files).isNotEmpty();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			assertThat(reader.readLine()).isNotNull();
		}
	}

	@Test
	public void createOutputFile() {
		//GIVEN
		String inputFileName = "test_create_file.xml";

		//WHEN
		Optional<File> outputFile = manager.createOutputFile(outputDir, inputFileName);

		//THEN
		assertThat(outputFile.isPresent()).isTrue();
		assertThat(outputFile.get().getName()).isEqualTo("test_create_file_result.xml");
		assertThat(outputFile.get().exists()).isTrue();
	}


	@Test
	public void createFileWithContent() throws IOException {
		//GIVEN
		String fileName = "new_file";
		String content = "content";

		//WHEN
		Optional<File> outputFile = manager.createFileWithContent(outputDir + "/" + fileName, content);

		//THEN
		assertThat(outputFile.isPresent()).isTrue();
		assertThat(outputFile.get().exists()).isTrue();
		File file = new File(outputDir, "new_file");
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();
			assertThat(line).isNotNull();
			assertThat(line).isEqualTo("content");
		}
	}

	@Test
	public void checkDirectory_whenDirectoryExists() {

		//WHEN
		boolean result = manager.checkDirectory(outputDir, false);

		//THEN
		assertThat(result).isTrue();
	}

	@Test
	public void checkDirectory_whenDirectoryNotExists() {

		//WHEN
		boolean result = manager.checkDirectory(new File(outputDir, "new_dir"), false);

		//THEN
		assertThat(result).isFalse();
	}

	@Test
	public void checkDirectory_whenDirectoryNotExistsAndCreate() {
		File newDir = new File(outputDir,"new_dir");

		//WHEN
		boolean result = manager.checkDirectory(newDir, true);

		//THEN
		assertThat(result).isTrue();
		newDir.delete();
	}

	@Test
	public void checkDirectory_whenDirectoryNotExistsAndNotCreate() {
		File newDir = new File(outputDir,"new_dir");

		//WHEN
		boolean result = manager.checkDirectory(newDir, false);

		//THEN
		assertThat(result).isFalse();
	}

}