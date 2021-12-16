package br.com.als;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import br.com.als.model.VBAAttributes;
import br.com.als.model.VBAClass;

public class App {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.print("Digite o nome da classe que deseja criar: ");
		String className = sc.nextLine();

		var vbaClass = new VBAClass(className);

		System.out.println("Caso deseje encerrar a criação, escreva 'exit'");
		while (true) {
			System.out.print(String.format(
					"Escreva o nome do atributo que será incluso na classe %s, seguido por um espaço e o seu tipo: ",
					className));
			String attributeNameAndType = sc.nextLine();

			if ("exit".equalsIgnoreCase(attributeNameAndType)) {
				break;
			}

			String[] values = attributeNameAndType.split(" ");
			String attrName = values[0];
			String attrType = values[1];

			vbaClass.getAttributes().add(new VBAAttributes(attrName, attrType));
		}

		System.out.println("Deseja exportar o arquivo ou somente exibir? exportar/exibir");
		String res = sc.nextLine();
		
		if("exibir".equalsIgnoreCase(res)) {
			System.out.println(vbaClass.writeClass());
			sc.close();
			return;
		}
		
		System.out.println(
				String.format("Escreva o caminho da pasta em que deseja salvar o arquivo %s.cls", vbaClass.getName()));
		String outPath = sc.nextLine();
		outPath += "\\" + vbaClass.getName() + ".cls";

		writeFile(outPath, vbaClass.writeClass());

		sc.close();
	}

	private static void writeFile(String outPath, String content) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {

			bw.write(content);
			System.out.println(outPath + " CREATED!");

		} catch (IOException e) {
			System.out.println("Erro ao escrever o arquivo: " + e.getMessage());
		}
	}

}
