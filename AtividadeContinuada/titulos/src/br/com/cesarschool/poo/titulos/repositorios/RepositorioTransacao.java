package br.com.cesarschool.poo.titulos.repositorios;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.com.cesarschool.poo.titulos.entidades.*;

public class RepositorioTransacao {
	private static final String FILE_NAME = "Transacao.txt";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	// Método para incluir uma nova transação no arquivo
	public void incluir(Transacao transacao) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
			String linha = gerarLinhaTransacao(transacao);
			writer.write(linha);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Método para buscar transações por identificador da entidade credora
	public Transacao[] buscarPorEntidadeCredora(int identificadorEntidadeCredito) {
		List<Transacao> transacoesEncontradas = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				Transacao transacao = converterLinhaParaTransacao(linha);
				if (transacao != null && transacao.getEntidadeCredito().getIdentificador() == identificadorEntidadeCredito) {
					transacoesEncontradas.add(transacao);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return transacoesEncontradas.toArray(new Transacao[0]);
	}

	// Método para converter uma transação em uma linha de string no formato especificado
	private String gerarLinhaTransacao(Transacao transacao) {
		EntidadeOperadora entidadeCredito = transacao.getEntidadeCredito();
		EntidadeOperadora entidadeDebito = transacao.getEntidadeDebito();
		Acao acao = transacao.getAcao();
		TituloDivida tituloDivida = transacao.getTituloDivida();
		double valorOperacao = transacao.getValorOperacao();
		LocalDateTime dataHoraOperacao = transacao.getDataHoraOperacao();

		return String.format("%s;%s;%s;%s;%.2f;%s",
				entidadeCredito.getIdentificador() + ";" + entidadeCredito.getNome() + ";" + entidadeCredito.isAutorizadoAcao() + ";" + entidadeCredito.getSaldoAcao() + ";" + entidadeCredito.getSaldoTituloDivida(),
				entidadeDebito.getIdentificador() + ";" + entidadeDebito.getNome() + ";" + entidadeDebito.isAutorizadoAcao() + ";" + entidadeDebito.getSaldoAcao() + ";" + entidadeDebito.getSaldoTituloDivida(),
				acao != null ? acao.getIdentificador() + ";" + acao.getNome() + ";" + acao.getDataValidade() + ";" + acao.getValorUnitario() : "null",
				tituloDivida != null ? tituloDivida.getIdentificador() + ";" + tituloDivida.getNome() + ";" + tituloDivida.getDataValidade() + ";" + tituloDivida.getTaxaJuros() : "null",
				valorOperacao,
				dataHoraOperacao.format(formatter)
		);
	}

	// Método para converter uma linha de string em uma transação
	private Transacao converterLinhaParaTransacao(String linha) {
		String[] campos = linha.split(";");
		try {
			// Dados da entidade de crédito
			int identificadorCredito = Integer.parseInt(campos[0]);
			String nomeCredito = campos[1];
			boolean autorizadoCredito = Boolean.parseBoolean(campos[2]);
			double saldoAcaoCredito = Double.parseDouble(campos[3]);
			double saldoTituloDividaCredito = Double.parseDouble(campos[4]);
			EntidadeOperadora entidadeCredito = new EntidadeOperadora(identificadorCredito, nomeCredito, autorizadoCredito, saldoAcaoCredito, saldoTituloDividaCredito);

			// Dados da entidade de débito
			int identificadorDebito = Integer.parseInt(campos[5]);
			String nomeDebito = campos[6];
			boolean autorizadoDebito = Boolean.parseBoolean(campos[7]);
			double saldoAcaoDebito = Double.parseDouble(campos[8]);
			double saldoTituloDividaDebito = Double.parseDouble(campos[9]);
			EntidadeOperadora entidadeDebito = new EntidadeOperadora(identificadorDebito, nomeDebito, autorizadoDebito, saldoAcaoDebito, saldoTituloDividaDebito);

			// Dados da ação
			Acao acao = null;
			if (!"null".equals(campos[10])) {
				int identificadorAcao = Integer.parseInt(campos[10]);
				String nomeAcao = campos[11];
				LocalDate dataValidadeAcao = LocalDate.parse(campos[12]);
				double valorUnitarioAcao = Double.parseDouble(campos[13]);
				acao = new Acao(identificadorAcao, nomeAcao, dataValidadeAcao, valorUnitarioAcao);
			}

			// Dados do título de dívida
			TituloDivida tituloDivida = null;
			if (!"null".equals(campos[14])) {
				int identificadorTituloDivida = Integer.parseInt(campos[14]);
				String nomeTituloDivida = campos[15];
				LocalDate dataValidadeTituloDivida = LocalDate.parse(campos[16]);
				double taxaJurosTituloDivida = Double.parseDouble(campos[17]);
				tituloDivida = new TituloDivida(identificadorTituloDivida, nomeTituloDivida, dataValidadeTituloDivida, taxaJurosTituloDivida);
			}

			// Dados da operação
			double valorOperacao = Double.parseDouble(campos[18]);
			LocalDateTime dataHoraOperacao = LocalDateTime.parse(campos[19], formatter);

			return new Transacao(entidadeCredito, entidadeDebito, acao, tituloDivida, valorOperacao, dataHoraOperacao);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
