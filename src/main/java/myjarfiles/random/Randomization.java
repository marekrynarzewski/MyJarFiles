package myjarfiles.random;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class Randomization 
{
	public static Map<String, Long> files = new HashMap<String, Long>();

	public static final class ProcessFile extends SimpleFileVisitor<Path>
	{
		@Override
		public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException
		{
			File toFile = aFile.toFile();
			files.put(toFile.getName(), toFile.length());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException
		{
			return FileVisitResult.CONTINUE;
		}
	}
}
