package br.com.cesarschool.poo.titulos.repositorios;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;

/*
 * Deve gravar em e ler de um arquivo texto chamado TituloDivida.txt os dados dos objetos do tipo
 * TituloDivida. Seguem abaixo exemplos de linhas (identificador, nome, dataValidade, taxaJuros).
 *
    1;BRASIL;2024-12-12;10.5
    2;EUA;2026-01-01;1.5
    3;FRANCA;2027-11-11;2.5 
 * 
 * A inclusão deve adicionar uma nova linha ao arquivo. Não é permitido incluir 
 * identificador repetido. Neste caso, o método deve retornar false. Inclusão com 
 * sucesso, retorno true.
 * 
 * A alteração deve substituir a linha atual por uma nova linha. A linha deve ser 
 * localizada por identificador que, quando não encontrado, enseja retorno false. 
 * Alteração com sucesso, retorno true.  
 *   
 * A exclusão deve apagar a linha atual do arquivo. A linha deve ser 
 * localizada por identificador que, quando não encontrado, enseja retorno false. 
 * Exclusão com sucesso, retorno true.
 * 
 * A busca deve localizar uma linha por identificador, materializar e retornar um 
 * objeto. Caso o identificador não seja encontrado no arquivo, retornar null.   
 */
public class RepositorioTituloDivida {
	static Path text = Paths.get("TituloDivida.txt");
	public boolean incluir(TituloDivida tituloDivida) {
		try(BufferedReader reader = new BufferedReader(new FileReader(text.toFile()))) {
			String line;
			while((line=reader.readLine())!=null){
				String[] lines = line.split(";");
				if(lines[0].equals(String.valueOf(tituloDivida.getIdentificador()))){
					return false;
				}
			}
		}catch (IOException e){
			e.fillInStackTrace();
			return false;
		}
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(text.toFile(),true))){
			String newLine = tituloDivida.getIdentificador() + " " + tituloDivida.getNome() + " " + tituloDivida.getDataDeValidade() + tituloDivida.getTaxaJuros();
			writer.write(newLine);
			writer.newLine();
			return true;

		}catch (IOException e){
			return false;
		}
	}

	public boolean alterar(TituloDivida tituloDivida) {

		return false;
	}

	public boolean excluir(int identificador) {
		return false;
	}

	public TituloDivida buscar(int identificador) {
		try(BufferedReader reader = new BufferedReader(new FileReader(text.toFile()))) {
			String line;
			while((line=reader.readLine())!=null){
				String[] lines = line.split(";");
				if(Integer.parseInt(lines[0])==identificador){
					TituloDivida tituloVida = new TituloDivida(Integer.parseInt(lines[0]),lines[1],LocalDate.parse(lines[2]), Double.parseDouble(lines[3]));
					return tituloVida;
				}
			}
		}catch (IOException e){
			e.fillInStackTrace();
		}
		return null;
	}
}
