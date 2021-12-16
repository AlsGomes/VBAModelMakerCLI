package br.com.als.vba.util;

public class VBALangUtils {
	public static String indent() {
		return "    ";
	}

	public static String indent(int value) {
		String indents = "";
		for (int i = 0; i < value; i++) {
			indents += indent();
		}
		return indents;
	}
}