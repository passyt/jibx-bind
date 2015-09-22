package org.jibx.binding.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * 
 * @author Passyt
 *
 */
public class IOUtils {

	public static String toString(InputStream in) {
		try {
			char[] buffer = new char[1024 * 4];
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			StringWriter writer = new StringWriter();
			for (int n = 0; -1 != (n = reader.read(buffer));) {
				writer.write(buffer, 0, n);
			}
			return writer.toString();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			closeQuiet(in);
		}
	}

	public static void closeQuiet(InputStream in) {
		if (in == null) {
			return;
		}

		try {
			in.close();
		} catch (IOException e) {
		}
	}

	public static void closeQuiet(OutputStream out) {
		if (out == null) {
			return;
		}

		try {
			out.close();
		} catch (IOException e) {
		}
	}

}
