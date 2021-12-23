package br.com.als;

import java.util.Scanner;

import br.com.als.model.VBAAttribute;
import br.com.als.model.VBAClass;

public class App {

	private static final Scanner SCANNER = new Scanner(System.in);

	public static void main(String[] args) {

		var vbaClass = new VBAClass(getClassName());

		exitClassAssembly();
		exitSystem();

		while (true) {
			String attrNameAndTypeAndJsonName = attributeEntry(vbaClass.getName());

			if ("exit".equalsIgnoreCase(attrNameAndTypeAndJsonName)) {
				break;
			}

			if ("stop".equalsIgnoreCase(attrNameAndTypeAndJsonName)) {
				System.exit(0);
			}

			String[] values = attrNameAndTypeAndJsonName.trim().split(" ");
			String attrName = values[0];
			String attrType = values[1];

			String attrJsonName = "";
			if (values.length == 3)
				attrJsonName = values[2];

			var vbaAttribute = new VBAAttribute();
			vbaAttribute.setName(attrName);
			vbaAttribute.setType(attrType);
			if (!attrJsonName.isBlank())
				vbaAttribute.setJsonName(attrJsonName);

			vbaClass.getAttributes().add(vbaAttribute);
		}
		
		System.out.println(vbaClass.writeClass());
		SCANNER.close();
	}

	private static String attributeEntry(String className) {
		System.out.print(String.format(
				"Escreva o nome do atributo que será incluso na classe %s, seguido por um espaço, o seu tipo, espaço e o nome do atributo em JSON (opcional): ",
				className));
		String attributeNameAndTypeAndJsonName = SCANNER.nextLine();
		return attributeNameAndTypeAndJsonName;
	}

	private static void exitSystem() {
		System.out.println("Para encerrar o programa, escreva 'stop'");
	}

	private static String getClassName() {
		System.out.print("Digite o nome da classe que deseja criar: ");
		String className = SCANNER.nextLine();
		return className;
	}

	private static void exitClassAssembly() {
		System.out.println("Caso deseje encerrar a criação da classe, escreva 'exit'");
	}
}
