package myjarfiles.file;

import java.io.File;
import java.util.Vector;

public class DirectoryReader
{
	public static Vector<File> listFilesFrom(File directory)
	{
		Vector<File> result = new Vector<File>();
		if (!directory.isDirectory())
		{
			return result;
		}
		File[] listOfFiles = directory.listFiles();
		if (listOfFiles == null)
		{
			return result;
		}
		for (File file: listOfFiles)
		{
			if (file.isFile())
			{
				result.add(file);
			}
			else if (file.isDirectory())
			{
				Vector<File> subresult = listFilesFrom(file);
				result.addAll(subresult);
			}
		}
		return result;
	}
}

