package br.com.cesarschool.poo.titulos.mediator;
/*
 * Deve ser um singleton.
 * 
 * Deve ter um atributo repositorioEntidadeOperadora, do tipo RepositorioEntidadeOperadora, que deve 
 * ser inicializado na sua declara��o. Este atributo ser� usado exclusivamente
 * pela classe, n�o tendo, portanto, m�todos set e get.
 * 
 * M�todos: 
 * 
 * pivate String validar(EntidadeOperadora): deve validar os dados do objeto recebido nos seguintes termos: 
 * identificador: deve ser maior que zero e menor que 100000 (1)
 * nome: deve ser preenchido, diferente de branco e de null (2). deve ter entre 5 e 60 caracteres (3).
 * data de validade: deve ser maior do que a data atual mais 180 dias (4). 
 * valorUnitario: deve ser maior que zero (5). 
 * O m�todo validar deve retornar null se o objeto estiver v�lido, e uma mensagem pertinente (ver abaixo)
 * se algum valor de atributo estiver inv�lido.
 * 
 * (1) - Identificador deve estar entre 100 e 1000000.
 * (2) - Nome deve ser preenchido.
 * (3) - Nome deve ter entre 10 e 100 caracteres.
 *
 * public String incluir(EntidadeOperadora entidade): deve chamar o m�todo validar. Se ele 
 * retornar null, deve incluir entidade no reposit�rio. Retornos poss�veis:
 * (1) null, se o retorno do validar for null e o retorno do incluir do 
 * reposit�rio for true.
 * (2) a mensagem retornada pelo validar, se o retorno deste for diferente
 * de null.
 * (3) A mensagem "Entidade j� existente", se o retorno do validar for null
 * e o retorno do reposit�rio for false. 
 *
 * public String alterar(EntidadeOperadora entidade): deve chamar o m�todo validar. Se ele 
 * retornar null, deve alterar entidade no reposit�rio. Retornos poss�veis:
 * (1) null, se o retorno do validar for null e o retorno do alterar do 
 * reposit�rio for true.
 * (2) a mensagem retornada pelo validar, se o retorno deste for diferente
 * de null.
 * (3) A mensagem "Entidade inexistente", se o retorno do validar for null
 * e o retorno do reposit�rio for false.
 * 
 * public String excluir(int identificador): deve validar o identificador. 
 * Se este for v�lido, deve chamar o excluir do reposit�rio. Retornos poss�veis:
 * (1) null, se o retorno do excluir do reposit�rio for true.
 * (2) A mensagem "Entidade inexistente", se o retorno do reposit�rio for false
 * ou se o identificador for inv�lido.
 * 
 * public EntidadeOperadora buscar(int identificador): deve validar o identificador.
 * Se este for v�lido, deve chamar o buscar do reposit�rio, retornando o 
 * que ele retornar. Se o identificador for inv�lido, retornar null. 
 */
import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEntidadeOperadora {

    private final Path caminhoArquivo = Paths.get("EntidadeOperadora.txt");

    // Método para incluir uma nova EntidadeOperadora no arquivo
    public boolean adicionar(EntidadeOperadora entidade) {
        // Verificar se o identificador já existe no arquivo
        if (existeIdentificador(entidade.getIdentificador())) {
            return false;
        }

        // Adicionar uma nova linha ao arquivo
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(caminhoArquivo.toFile(), true))) {
            escritor.write(formatarLinha(entidade));
            escritor.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Método para alterar uma EntidadeOperadora existente no arquivo
    public boolean atualizar(EntidadeOperadora entidade) {
        List<String> linhasModificadas = new ArrayList<>();
        boolean alterado = false;

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo.toFile()))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados[0].equals(String.valueOf(entidade.getIdentificador()))) {
                    linhasModificadas.add(formatarLinha(entidade)); // Atualiza a linha com os novos dados
                    alterado = true;
                } else {
                    linhasModificadas.add(linha);
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (alterado) {
            escreverNovasLinhas(linhasModificadas);
            return true;
        }

        return false;
    }

    // Método para excluir uma EntidadeOperadora com base no identificador
    public boolean remover(int identificador) {
        List<String> linhasModificadas = new ArrayList<>();
        boolean removido = false;

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo.toFile()))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] dados = linha.split(";");
                if (!dados[0].equals(String.valueOf(identificador))) {
                    linhasModificadas.add(linha);
                } else {
                    removido = true;
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (removido) {
            escreverNovasLinhas(linhasModificadas);
            return true;
        }

        return false;
    }

    // Método para buscar uma EntidadeOperadora pelo identificador
    public EntidadeOperadora procurar(int identificador) {
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo.toFile()))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados[0].equals(String.valueOf(identificador))) {
                    return new EntidadeOperadora(
                            Integer.parseInt(dados[0]),
                            dados[1],
                            Boolean.parseBoolean(dados[2])
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Retorna null se o identificador não for encontrado
    }

    // Método auxiliar para verificar se um identificador já existe no arquivo
    private boolean existeIdentificador(int identificador) {
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo.toFile()))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados[0].equals(String.valueOf(identificador))) {
                    return true; // Identificador já existe
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    // Método auxiliar para escrever todas as linhas modificadas de volta no arquivo
    private void escreverNovasLinhas(List<String> linhasModificadas) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(caminhoArquivo.toFile()))) {
            for (String linha : linhasModificadas) {
                escritor.write(linha);
                escritor.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para formatar os dados de uma EntidadeOperadora em formato de linha de texto
    private String formatarLinha(EntidadeOperadora entidade) {
        return entidade.getIdentificador() + ";" + entidade.getNome() + ";" + entidade.getAutorizadoAcao();
    }
}