package best.azura.client.util.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtil {
	
	public static ArrayList<String> getContentFromFile(final File input) {
		try {
			return getContentFromInputStream(new FileInputStream(input));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public static String getContentFromFileAsString(final File input) {
		StringBuilder stringBuilder = new StringBuilder();

		try {
			for (String s : getContentFromInputStream(new FileInputStream(input))) {
				stringBuilder.append(s).append("\n");
			}
		} catch (FileNotFoundException ignored) {}
		return stringBuilder.toString();
	}
	
	public static ArrayList<String> getContentFromInputStream(final InputStream is) {
		ArrayList<String> list = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			if (reader != null) {
				for (String read = null; (read = reader.readLine()) != null; ) {
					list.add(read);
				}

				reader.close();
			}
		} catch (Exception ignored) {}
		return list;
	}
	
	public static boolean writeContentToFile(final File file, final ArrayList<String> content, final boolean printExceptions) {
		try {
			boolean written = false;
			final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (String c : content) {
				if (written) writer.append('\n').append(c);
				else writer.append(c);
				written = true;
			}
			writer.close();
			return true;
		} catch (Exception e) {
			if (printExceptions) e.printStackTrace();
			return false;
		}
	}

	public static boolean writeContentToFile(final File file, final String content, final  boolean printExceptions) {
		final ArrayList<String> strings = new ArrayList<>(Arrays.asList(content.split("\n")));
		if (strings.isEmpty()) strings.add(content);
		return writeContentToFile(file, strings, printExceptions);
	}

}