package org.logview4j.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * @author Jefty Negapatan
 */
public class FileProcessor extends DataProcessor {

	protected File file;

	public FileProcessor(File file) {
		this.file = file;
	}

	public void run() {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			readData(fileInputStream, false);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		if (fileInputStream != null) {
			try {
				fileInputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			fileInputStream = null;
		}
	}
}
