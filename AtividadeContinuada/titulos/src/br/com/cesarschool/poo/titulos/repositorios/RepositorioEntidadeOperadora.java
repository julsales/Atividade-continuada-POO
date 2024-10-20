package br.com.cesarschool.poo.titulos.repositorios;
import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
/*
 * Deve gravar em e ler de um arquivo texto chamado Acao.txt os dados dos objetos do tipo
 * Acao. Seguem abaixo exemplos de linhas.
 *
    1;PETROBRAS;2024-12-12;30.33
    2;BANCO DO BRASIL;2026-01-01;21.21
    3;CORREIOS;2027-11-11;6.12 
 * 
 * A inclus�o deve adicionar uma nova linha ao arquivo. N�o � permitido incluir 
 * identificador repetido. Neste caso, o m�todo deve retornar false. Inclus�o com 
 * sucesso, retorno true.
 * 
 * A altera��o deve substituir a linha atual por uma nova linha. A linha deve ser 
 * localizada por identificador que, quando n�o encontrado, enseja retorno false. 
 * Altera��o com sucesso, retorno true.  
 *   
 * A exclus�o deve apagar a linha atual do arquivo. A linha deve ser 
 * localizada por identificador que, quando n�o encontrado, enseja retorno false. 
 * Exclus�o com sucesso, retorno true.
 * 
 * A busca deve localizar uma linha por identificador, materializar e retornar um 
 * objeto. Caso o identificador n�o seja encontrado no arquivo, retornar null.   
 */

public class RepositorioEntidadeOperadora {

    static Path text = Paths.get("EntidadeOperadora.txt");

    public boolean incluir(EntidadeOperadora entidadeOperadora) {
        try (BufferedReader reader = new BufferedReader(new FileReader(text.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] atributos = line.split(";");
                if (atributos[0].equals(String.valueOf(entidadeOperadora.getIdentificador()))) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(text.toFile(), true))) {
            String newLine = entidadeOperadora.getIdentificador() + " " + entidadeOperadora.getNome() + " " + entidadeOperadora.getAutorizadoAcao();
            writer.write(newLine);
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean alterar(EntidadeOperadora entidadeOperadora) {
        List<String> lines = new ArrayList<>();
        boolean trocou = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(text.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] atributos = line.split(";");
                if (atributos[0].equals(String.valueOf(entidadeOperadora.getIdentificador()))) {
                    lines.add(entidadeOperadora.getIdentificador() + " " + entidadeOperadora.getNome() + " " + entidadeOperadora.getAutorizadoAcao());
                    trocou = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (trocou) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(text.toFile(), false))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean excluir(int identificador) {
        List<String> lines = new ArrayList<>();
        boolean excluido = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(text.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] atributos = line.split(";");
                if (Integer.parseInt(atributos[0]) != identificador) {
                    lines.add(line);
                } else {
                    excluido = true;
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (excluido) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(text.toFile(), false))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public EntidadeOperadora buscar(int identificador) {
        try (BufferedReader reader = new BufferedReader(new FileReader(text.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] atributos = line.split(";");
                if (Integer.parseInt(atributos[0]) == identificador) {
                    return new EntidadeOperadora(Integer.parseInt(atributos[0]), atributos[1], Double.parseDouble(atributos[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

