package br.com.als.vba.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.als.model.VBAAttribute;
import br.com.als.model.VBAClass;

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

	public static Map<String, String> readJson(String json) {
		String[] properties = json.split(",");
		Map<String, Object> keyValueOfProperty = new HashMap<>();
		for (String property : properties) {
			String[] pair = property.split(": ");
			String key = pair[0].replaceAll("\\W", "");
			String value = pair[1];

			if (value.contains("\"")) {
				keyValueOfProperty.put(key, ((String) value).replaceAll("\"", ""));
			} else if (value.equals("true") || value.equals("false")) {
				keyValueOfProperty.put(key, Boolean.parseBoolean(value));
			} else if (value.equals("null")) {
				keyValueOfProperty.put(key, null);
			} else if (value.contains(".")) {
				keyValueOfProperty.put(key, Double.parseDouble(value));
			} else {
				value = value.replace("}", "").replace("{", "").trim();
				try {
					keyValueOfProperty.put(key, Long.parseLong(value));
				} catch (NumberFormatException e) {
					System.out.println(
							String.format("Property %s could be read as a valid format. Value is:%s", key, value));
				}
			}
		}

		Map<String, String> result = new HashMap<String, String>();
		for (var entry : keyValueOfProperty.entrySet()) {
			if (entry.getValue() == null) {
				result.put(entry.getKey(), "String");
			} else if (entry.getValue() instanceof String) {
				result.put(entry.getKey(), "String");
			} else if (entry.getValue() instanceof Boolean) {
				result.put(entry.getKey(), "Boolean");
			} else if (entry.getValue() instanceof Double) {
				result.put(entry.getKey(), "Double");
			} else if (entry.getValue() instanceof Long) {
				result.put(entry.getKey(), "Long");
			}
		}

		return result;
	}

	public static void createVBAAttribute(Map<String, String> attributes) {
		var vbaClass = new VBAClass("MinhaClasse");

		for (var entry : attributes.entrySet()) {
			var vbaAttribute = new VBAAttribute();

			String attrName = String.join("",
					Arrays.asList(entry.getKey().split("_")).stream()
							.map(x -> x.substring(0, 1).toUpperCase() + x.substring(1, x.length()))
							.collect(Collectors.toList()));

			vbaAttribute.setName(attrName);
			vbaAttribute.setType(entry.getValue());
			vbaAttribute.setJsonName(entry.getKey());

			vbaClass.getAttributes().add(vbaAttribute);
		}

		System.out.println(vbaClass.writeClass());
	}

	public static void main(String[] args) {
		String path = "C:\\Users\\als_0\\OneDrive\\Trabalho\\Programação\\Nambaty\\Nambaty_202201221830\\UnicaNotaFiscal.json";
		String json = "";

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
			String line = br.readLine();
			while (line != null) {
				json += line + "\n";
				line = br.readLine();
			}
		} catch (Exception e) {
			System.out.println("Erro ao encontrar arquivo");
		}

		var result = readJson(json);
		createVBAAttribute(result);
	}
}