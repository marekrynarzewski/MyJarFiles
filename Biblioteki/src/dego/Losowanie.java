package dego;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import myutils.Plik;

public class Losowanie
{
	public static Map<String, Long> pliki = new HashMap<String, Long>();

	public static final class ProcessFile extends SimpleFileVisitor<Path>
	{
		@Override
		public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException
		{
			File toFile = aFile.toFile();
			System.out.println(toFile.getPath());
			pliki.put(toFile.getName(), toFile.length());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException
		{
			System.out.println("Processing directory:" + aDir);
			return FileVisitResult.CONTINUE;
		}
	}
}

