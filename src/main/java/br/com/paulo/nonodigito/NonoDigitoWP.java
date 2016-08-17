package br.com.paulo.nonodigito;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

//celular começa com 9,8 ou 7
//pode ser nos formatos +553N(9|8|7)NNNNNNN, 0153N(9|8|7)NNNNNNN, (9|8|7)NNNNNNN, 3N(9|8|7)NNNNNNN, 03N(9|8|7)NNNNNNN
public class NonoDigitoWP {

	public static void main(String[] args) throws IOException {
		List<String> readLines = FileUtils.readLines(new File("src/main/resources/OutlookContacts.csv"), Charset.forName("UTF-8"));

		String regex1 = "\"\\+553\\d{1}(9|8|7)\\d{7}\"";
		String regex2 = "\"0153\\d{1}(9|8|7)\\d{7}\"";
		String regex3 = "\"3\\d{1}(9|8|7)\\d{7}\"";
		String regex4 = "\"03\\d{1}(9|8|7)\\d{7}\"";
		String regex5 = "\"(9|8|7)\\d{7}\"";
		
		
		String saida = "";

		Pattern fullPattern = Pattern.compile(regex1 + "|" + regex2 + "|" + regex3 + "|" + regex4 + "|" + regex5);

		int count = 0;
		for (String linha : readLines) {
			String[] split = linha.split(",");

			StringBuilder b = null;
			
			for (String coluna : split) {
				boolean found = false;

				if (fullPattern.matcher(coluna).matches()) {
					count++;
					found = true;
					coluna = coluna.replace("\"", "");

					b = new StringBuilder(coluna);
					
					if(coluna.length() == 8) { //(9|8|7)NNNNNNN
						b.insert(0, "9");

					} else if(coluna.length() == 10) { //3N(9|8|7)NNNNNNN
						b.insert(2, "9");
						
					} else if(coluna.length() == 11) { //03N(9|8|7)NNNNNNN
						b.insert(3, "9");
						
					} else if(coluna.length() == 13) { //0153N(9|8|7)NNNNNNN ou +553N(9|8|7)NNNNNNN
						b.insert(5, "9");
					}

					System.out.println(coluna + " -> " + b);
				}
				
				if(found) {
					linha = linha.replace(coluna, b.toString());
				}
			}

			saida += linha + "\n";
		}

		System.out.println("Trocou " + count + " telefones.");
		
		System.out.println(saida);
		
		FileUtils.write(new File("src/main/resources/contatos_" + System.currentTimeMillis() + ".csv"), saida, Charset.forName("UTF-8"));
	}
}
