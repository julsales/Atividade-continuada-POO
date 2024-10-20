package br.com.cesarschool.poo.titulos.repositorios;
import br.com.cesarschool.poo.titulos.entidades.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Deve gravar em e ler de um arquivo texto chamado Transacao.txt os dados dos objetos do tipo
 * Transacao. Seguem abaixo exemplos de linhas
 * De entidadeCredito: identificador, nome, autorizadoAcao, saldoAcao, saldoTituloDivida.
 * De entidadeDebito: identificador, nome, autorizadoAcao, saldoAcao, saldoTituloDivida.
 * De acao: identificador, nome, dataValidade, valorUnitario OU null
 * De tituloDivida: identificador, nome, dataValidade, taxaJuros OU null.
 * valorOperacao, dataHoraOperacao
 *
 *   002192;BCB;true;0.00;1890220034.0;001112;BOFA;true;12900000210.00;3564234127.0;1;PETROBRAS;2024-12-12;30.33;null;100000.0;2024-01-01 12:22:21
 *   002192;BCB;false;0.00;1890220034.0;001112;BOFA;true;12900000210.00;3564234127.0;null;3;FRANCA;2027-11-11;2.5;100000.0;2024-01-01 12:22:21
 *
 * A inclus�o deve adicionar uma nova linha ao arquivo.
 *
 * A busca deve retornar um array de transa��es cuja entidadeCredito tenha identificador igual ao
 * recebido como par�metro.
 */


public class RepositorioTransacao {
	private static final Path text = Paths.get("Transacao.txt");
	public boolean incluir(Transacao transacao) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(text.toFile(), true))) {
			writer.write(transacao.getEntidadeCredito().getIdentificador() + ";"
					+ transacao.getEntidadeCredito().getNome() + ";"
					+ transacao.getEntidadeCredito().getAutorizadoAcao() + ";"
					+ transacao.getEntidadeCredito().getSaldoAcao() + ";"
					+ transacao.getEntidadeCredito().getSaldoTituloDivida() + ";"
					+ transacao.getEntidadeDebito().getIdentificador() + ";"
					+ transacao.getEntidadeDebito().getNome() + ";"
					+ transacao.getEntidadeDebito().getAutorizadoAcao() + ";"
					+ transacao.getEntidadeDebito().getSaldoAcao() + ";"
					+ transacao.getEntidadeDebito().getSaldoTituloDivida() + ";"
					+ (transacao.getAcao() != null ? transacao.getAcao().getIdentificador() + ";"
					+ transacao.getAcao().getNome() + ";"
					+ transacao.getAcao().getDataDeValidade() + ";"
					+ transacao.getAcao().getValorUnitario() + ";"
					: "null;null;null;null;")
					+ (transacao.getTituloDivida() != null ? transacao.getTituloDivida().getIdentificador() + ";"
					+ transacao.getTituloDivida().getNome() + ";"
					+ transacao.getTituloDivida().getDataDeValidade() + ";"
					+ transacao.getTituloDivida().getTaxaJuros()
					: "null;null;null;null;")
					+ ";" + transacao.getValorOperacao() + ";"
					+ transacao.getDataHoraOperacao());
			writer.newLine();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Transacao> buscarPorEntidade(int identificador, boolean isCredito) {
		List<Transacao> transacoes = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(text.toFile()))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				String[] dados = linha.split(";");

				Acao acao = null;
				if (!"null".equals(dados[10])) {
					acao = RepositorioAcao.buscar(Integer.parseInt(dados[10]));
				}

				TituloDivida tituloDivida = null;
				if (!"null".equals(dados[14])) {
					tituloDivida = RepositorioTituloDivida.buscar(Integer.parseInt(dados[14]));
				}
				EntidadeOperadora entidadeDebito = RepositorioEntidadeOperadora.buscar(Integer.parseInt(dados[5]));
				EntidadeOperadora entidadeCredito = RepositorioEntidadeOperadora.buscar(Integer.parseInt(dados[0]));
				Transacao transacao = new Transacao(entidadeCredito, entidadeDebito, acao, tituloDivida, Double.parseDouble(dados[18]), LocalDateTime.parse(dados[19]));
				if ((isCredito && transacao.getEntidadeCredito().getIdentificador() == identificador) ||
						(!isCredito && transacao.getEntidadeDebito().getIdentificador() == identificador)) {
					transacoes.add(transacao);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transacoes;
	}

	public List<Transacao> buscarPorEntidadeCredora(int identificadorEntidadeCredito) {
		return buscarPorEntidade(identificadorEntidadeCredito, true);
	}

	public List<Transacao> buscarPorEntidadeDevedora(int identificadorEntidadeDebito) {
		return buscarPorEntidade(identificadorEntidadeDebito, false);
	}
}
