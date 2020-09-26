package com.mfd.vidindexer;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class Utils {
	public static void deleteDir(Path tmpDir) throws IOException {
		if (!Files.isDirectory(tmpDir)) {
			throw new IOException("Expected directory, got file for Path.");
		}

		Files.walkFileTree(tmpDir, new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
				Files.delete(path);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
				throw e;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
				if (e != null)
					throw e;
				Files.delete(path);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
