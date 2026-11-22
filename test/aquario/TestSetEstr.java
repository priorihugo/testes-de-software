package aquario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.Test;

/**
 * TestSet-Estr: casos acrescentados na Parte II-B para cobrir os requisitos de
 * fluxo de controle (JaCoCo/EclEmma) e de fluxo de dados (BA-DUA/Baduíno) que o
 * TestSet-Func não exercita. Cada caso indica o requisito estrutural que motivou
 * sua criação.
 */
public class TestSetEstr {

	private static String executarMain(String entrada, int retornoEsperado) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		PrintStream saida = new PrintStream(bytes, true, "UTF-8");
		assertEquals(retornoEsperado, Main.executar(new BufferedReader(new StringReader(entrada)), saida));
		return bytes.toString("UTF-8");
	}

	/** CE01: parâmetro não numérico (Aquario.inteiroPositivo, captura de NumberFormatException). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce01ParametroNaoNumerico() {
		Aquario.inicializar("1", "4", "abc", "1", "2", "3", "3", "3", "A . . B");
	}

	/** CE02: inteiro positivo que não cabe em int (Aquario.inteiroPositivo, captura de ArithmeticException). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce02ParametroGrandeDemais() {
		Aquario.inicializar("1", "4", "1", "1", "99999999999", "3", "3", "3", "A . . B");
	}

	/** CE03: matriz entre colchetes (Aquario.lerMatriz, desvio verdadeiro da remoção de colchetes; du de texto). */
	@Test
	public void ce03MatrizComColchetes() {
		Aquario aquario = Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "3", "[A . . B]");
		assertEquals("[A . . B]", aquario.getMatriz());
	}

	/** CE04: colchete aberto sem fechamento (Aquario.lerMatriz, desvio startsWith verdadeiro e endsWith falso). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce04ColcheteNaoFechado() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "3", "[A . . B");
	}

	/** CE05: linha vazia na matriz (Aquario.lerMatriz, desvio linha.isEmpty() verdadeiro). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce05LinhaVazia() {
		Aquario.inicializar("2", "2", "1", "1", "2", "3", "3", "3", "A B;");
	}

	/** CE06: primeira linha com tamanho diferente de N (Aquario.inicializar, du de i inicial até o lançamento). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce06PrimeiraLinhaComTamanhoErrado() {
		Aquario.inicializar("2", "2", "1", "1", "2", "3", "3", "3", "A; B .");
	}

	/** CE07: matriz sem nenhum A (Aquario.inicializar, du de contaA = 0 até a comparação com X). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce07MatrizSemPeixeA() {
		Aquario.inicializar("1", "3", "1", "1", "2", "3", "3", "3", "B . .");
	}

	/** CE08: símbolo inválido na célula (1,1) (Aquario.inicializar, du de i e j iniciais na mensagem de erro). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce08SimboloInvalidoNaPrimeiraCelula() {
		Aquario.inicializar("2", "2", "1", "1", "2", "3", "3", "3", "? A; . B");
	}

	/** CE09: entrada termina quando o jogo pede a opção (Jogo.lerOpcao, desvio linha == null). */
	@Test
	public void ce09FimDaEntradaEquivaleAEncerrar() throws IOException {
		String saida = TestSetFunc.jogar(Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "3", "A . . B"), "");
		TestSetFunc.assertIteracoes(saida, "[. A B .]");
		TestSetFunc.assertFim(saida, false, 1);
	}

	/** CE10: partida completa pelo console (Main.executar, caminho de inicialização válida). */
	@Test
	public void ce10MainPartidaValida() throws IOException {
		String saida = executarMain("1\n4\n1\n1\n2\n3\n3\n3\nA . . B\nC\nE\n", 2);
		assertTrue(saida.contains("Matriz inicial: [A . . B]"));
		assertTrue(saida.contains("Iteração 2: [A B . .]"));
		assertTrue(saida.contains("Pontuação final: 2"));
	}

	/** CE11: inicialização recusada pelo console (Main.executar, captura de EntradaInvalidaException). */
	@Test
	public void ce11MainInicializacaoRecusada() throws IOException {
		String saida = executarMain("1\n4\n0\n1\n2\n3\n3\n3\n. . . B\n", -1);
		assertTrue(saida.contains("Inicialização recusada: X=0 não é positivo"));
	}

	/** CE12: M não numérico no console (Main.executar, captura de NumberFormatException; nenhuma linha lida). */
	@Test
	public void ce12MainMNaoNumerico() throws IOException {
		String saida = executarMain("abc\n4\n1\n1\n2\n3\n3\n3\n", -1);
		assertTrue(saida.contains("Inicialização recusada: M=abc não é um número"));
	}

	/** CE13: entrada termina durante a leitura dos parâmetros (Main.lerLinha, desvio linha == null). */
	@Test
	public void ce13MainEntradaIncompleta() throws IOException {
		String saida = executarMain("1\n", -1);
		assertTrue(saida.contains("Inicialização recusada"));
	}

	/** CE14: método main lendo de System.in e escrevendo em System.out (Main.main). */
	@Test
	public void ce14MetodoMain() throws IOException {
		InputStream entradaOriginal = System.in;
		PrintStream saidaOriginal = System.out;
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			System.setIn(new ByteArrayInputStream("1\n2\n1\n1\n3\n3\n3\n3\nA B\nE\n".getBytes("UTF-8")));
			System.setOut(new PrintStream(bytes, true, "UTF-8"));
			Main.main(new String[0]);
		} finally {
			System.setIn(entradaOriginal);
			System.setOut(saidaOriginal);
		}
		assertTrue(bytes.toString("UTF-8").contains("Iteração 1: [B .]"));
		assertTrue(bytes.toString("UTF-8").contains("Pontuação final: 1"));
	}

	/** CE15: matriz de duas linhas lida pelo console (Main.executar, desvio i > 0 verdadeiro; du de i e matriz). */
	@Test
	public void ce15MainMatrizComDuasLinhas() throws IOException {
		String saida = executarMain("2\n2\n1\n1\n9\n9\n9\n9\nB .\n. A\nE\n", 1);
		assertTrue(saida.contains("Matriz inicial: [B .; . A]"));
		assertTrue(saida.contains("Iteração 1: [. A; B .]"));
	}

	/** CE16: símbolo inválido na célula (2,1) (Aquario.inicializar, du de i incrementado até a mensagem de erro). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce16SimboloInvalidoNaSegundaLinha() {
		Aquario.inicializar("2", "2", "1", "1", "2", "3", "3", "3", "A B; ? .");
	}

	/** CE17: inteiro escrito com casa decimal é recusado (decisão D13, revisão 1.2 da Parte I). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce17ParametroComCasaDecimalZero() {
		Aquario.inicializar("1", "4", "1", "1", "2.0", "3", "3", "3", "A . . B");
	}

	/** CE18: notação científica é recusada (decisão D13, revisão 1.2 da Parte I). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce18ParametroEmNotacaoCientifica() {
		Aquario.inicializar("1", "4", "1", "1", "2", "1e1", "3", "3", "A . . B");
	}

	/** CE19: sinal de mais é recusado (decisão D13, revisão 1.2 da Parte I). */
	@Test(expected = EntradaInvalidaException.class)
	public void ce19ParametroComSinalDeMais() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "+3", "3", "A . . B");
	}
}
