package com.syntaxphoenix.syntaxapi.utils.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper {

	public static File[] unzip(File zip, File directory, boolean deleteZipOnEnd) throws IOException {
		if (!zip.exists() || !zip.isFile()) {
			return null;
		}
		if (!directory.exists()) {
			directory.mkdirs();
		} else if (!directory.isDirectory()) {
			return null;
		}
		byte[] buffer = new byte[2048];
		ZipInputStream inputStream = new ZipInputStream(new FileInputStream(zip));
		ZipEntry entry = inputStream.getNextEntry();
		while (entry != null) {
			File file = new File(directory, entry.getName());
			FileOutputStream fileOutput = new FileOutputStream(file);
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, length);
			}
			fileOutput.close();
			entry = inputStream.getNextEntry();
		}
		inputStream.closeEntry();
		inputStream.close();
		if(deleteZipOnEnd) zip.delete();
		return directory.listFiles();
	}

	public File[] unzipp(File zip, File directory, boolean deleteZipOnEnd) throws IOException {
		return Zipper.unzip(zip, directory, deleteZipOnEnd);
	}

	public static void zip(String zipName, File directory, File... toZip) throws IOException {
		if (!directory.exists()) {
			directory.mkdirs();
		} else if (!directory.isDirectory()) {
			return;
		}
		zip(new File(directory, zipName));
	}

	public void zipp(String zipName, File directory, File... toZip) throws IOException {
		Zipper.zip(zipName, directory, toZip);
	}
	
	public static void zip(File zipFile, File... toZip) throws IOException {
		if(toZip == null) return;
		if(toZip.length == 0) return;
		if (!zipFile.exists()) {
			zipFile.createNewFile();
		}
		FileOutputStream fileOutput = new FileOutputStream(zipFile);
		ZipOutputStream zipOutput = new ZipOutputStream(fileOutput);
		int failed = 0;
		for (int index = 0; index < toZip.length; index++) {
			File file = toZip[index];
			if(file == null) {
				failed += 1;
				continue;
			}
			FileInputStream fileInput = new FileInputStream(file);
			ZipEntry entry = new ZipEntry(file.getName());
			zipOutput.putNextEntry(entry);
			byte[] bytes = new byte[2048];
			int length;
			while ((length = fileInput.read(bytes)) >= 0) {
				zipOutput.write(bytes, 0, length);
			}
			fileInput.close();
		}
		zipOutput.close();
		fileOutput.close();
		if(failed == toZip.length) {
			zipFile.delete();
		}
	}

	public void zipp(File zipFile, File... toZip) throws IOException {
		Zipper.zip(zipFile, toZip);
	}

}
