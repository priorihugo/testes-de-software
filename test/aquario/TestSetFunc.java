package aquario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * TestSet-Func: os 68 casos de teste funcionais da Parte I, sem
 * acréscimos (Parte II-A). Cada método corresponde ao caso de mesmo ID na
 * Tabela 2 do relatório da Parte I.
 */
public class TestSetFunc {

	/** Executa a partida com as opções digitadas e devolve tudo o que foi exibido. */
	static String jogar(Aquario aquario, String opcoes) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		PrintStream saida = new PrintStream(bytes, true, "UTF-8");
		int pontuacao = Jogo.executar(aquario, new BufferedReader(new StringReader(opcoes)), saida);
		assertEquals(aquario.getPontuacao(), pontuacao);
		return bytes.toString("UTF-8");
	}

	/** Verifica a matriz exibida após cada iteração, na ordem. */
	static void assertIteracoes(String saida, String... esperadas) {
		List<String> obtidas = new ArrayList<String>();
		for (String linha : saida.split("\\r?\\n")) {
			int k = linha.indexOf("Iteração ");
			if (k >= 0) {
				obtidas.add(linha.substring(linha.indexOf(": ", k) + 2));
			}
		}
		List<String> lista = new ArrayList<String>();
		for (String e : esperadas) {
			lista.add(e);
		}
		assertEquals(lista, obtidas);
	}

	/** Verifica o tipo de encerramento e a pontuação final. */
	static void assertFim(String saida, boolean automatico, int pontuacao) {
		if (automatico) {
			assertTrue(saida.contains("Não há mais peixes B. Fim de jogo."));
		} else {
			assertTrue(saida.contains("Jogo encerrado pelo jogador."));
			assertFalse(saida.contains("Não há mais peixes B."));
		}
		assertTrue(saida.contains("Pontuação final: " + pontuacao));
	}

	static int ocorrencias(String texto, String trecho) {
		int n = 0;
		for (int k = texto.indexOf(trecho); k >= 0; k = texto.indexOf(trecho, k + 1)) {
			n++;
		}
		return n;
	}

	/** CT01: V1–V7, V10, V15–V23, V25, V27, V29, V31, V33, V35. */
	@Test
	public void ct01() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "3", "A . . B"), "E\n");
		assertIteracoes(saida, "[. A B .]");
		assertFim(saida, false, 1);
	}

	/** CT02: V1–V7, V15–V22, V24, V35. */
	@Test
	public void ct02() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "1", "3", "3", "3", "A . . B"), "E\n");
		assertIteracoes(saida, "[A A B .]");
		assertFim(saida, false, 1);
	}

	/** CT04: V1–V6, V9, V14–V22, V24, V29. */
	@Test
	public void ct04() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "2", "A . . B"), "C\nE\n");
		assertIteracoes(saida, "[. A B .]", "[A B . .]");
		assertFim(saida, false, 2);
	}

	/** CT05: V1–V6, V14, V16–V23, V30, V34. */
	@Test
	public void ct05() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "3", "3", "3", "2", "A . . B"), "C\n");
		assertIteracoes(saida, "[. A B .]", "[A . . .]");
		assertFim(saida, true, 2);
	}

	/** CT06: V1–V6, V14–V24, V35. */
	@Test
	public void ct06() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "8", "1", "1", "3", "9", "9", "9", "A . . . . . . B"), "C\nC\nE\n");
		assertIteracoes(saida, "[. A . . . . B .]", "[A . . . . B . .]", "[A A . . B . . .]");
		assertFim(saida, false, 3);
	}

	/** CT07: V1–V8, V10, V15–V22, V26. */
	@Test
	public void ct07() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "2", "1", "3", "1", "2", "3", "B . A A"), "E\n");
		assertIteracoes(saida, "[. B A .]");
		assertFim(saida, false, 1);
	}

	/** CT08: V1–V9, V12, V14–V22, V25, V26, V28, V31. */
	@Test
	public void ct08() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "6", "3", "1", "9", "2", "1", "9", "A A A . . B"), "C\nC\nC\nC\nE\n");
		assertIteracoes(saida, "[A A . A B .]", "[A . A B . .]", "[. A B B . .]", "[A B B . . .]", "[B B . B . .]");
		assertFim(saida, false, 5);
	}

	/** CT09: V1–V6, V8, V12, V14–V22, V25, V28, V31. */
	@Test
	public void ct09() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "6", "3", "1", "9", "3", "1", "9", "A A A . . B"), "C\nE\n");
		assertIteracoes(saida, "[A A . A B .]", "[A . A B B .]");
		assertFim(saida, false, 2);
	}

	/** CT10: V1–V8, V14–V25. */
	@Test
	public void ct10() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "8", "1", "1", "2", "9", "9", "9", "A . . . . . . B"), "C\nC\nC\nE\n");
		assertIteracoes(saida, "[. A . . . . B .]", "[A A . . . B . .]", "[A . A . B . . .]", "[. A A B . . . .]");
		assertFim(saida, false, 4);
	}

	/** CT11: V1–V6, V9, V14–V24, V35. */
	@Test
	public void ct11() throws IOException {
		String saida = jogar(Aquario.inicializar("3", "3", "1", "1", "2", "9", "9", "9", "A . .; . . .; . . B"), "C\nC\nE\n");
		assertIteracoes(saida, "[. . .; A . B; . . .]", "[A . B; A . .; . . .]", "[A B .; . . .; . . .]");
		assertFim(saida, false, 3);
	}

	/** CT12: V1–V6, V9, V12, V15–V22, V27, V31. */
	@Test
	public void ct12() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "3", "1", "1", "3", "3", "2", "3", "B A ."), "E\n");
		assertIteracoes(saida, "[. B .]");
		assertFim(saida, false, 1);
	}

	/** CT13: V1–V6, V8, V9, V15–V22, V27. */
	@Test
	public void ct13() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "3", "1", "1", "3", "3", "2", "3", "A B ."), "E\n");
		assertIteracoes(saida, "[B . .]");
		assertFim(saida, false, 1);
	}

	/** CT14: V1–V6, V8, V9, V12, V15–V20, V22, V25, V28, V31. */
	@Test
	public void ct14() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "3", "1", "9", "9", "1", "9", "B A A A"), "E\n");
		assertIteracoes(saida, "[B B A A]");
		assertFim(saida, false, 1);
	}

	/** CT15: V1–V6, V9, V12, V13, V15–V20, V22, V28, V31. */
	@Test
	public void ct15() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "2", "2", "3", "3", "1", "3", "B A B A"), "E\n");
		assertIteracoes(saida, "[. B B B]");
		assertFim(saida, false, 1);
	}

	/** CT16: V1–V6, V10, V16–V22, V30, V34. */
	@Test
	public void ct16() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "5", "1", "1", "9", "9", "9", "1", "B . . . A"), "");
		assertIteracoes(saida, "[. . . A .]");
		assertFim(saida, true, 1);
	}

	/** CT17: V1–V6, V14, V16–V22, V29, V30, V34. */
	@Test
	public void ct17() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "5", "1", "1", "9", "9", "9", "2", "B . . . A"), "C\n");
		assertIteracoes(saida, "[. B . A .]", "[. . A . .]");
		assertFim(saida, true, 2);
	}

	/** CT18: V1–V6, V14, V16–V22, V29, V30, V34. */
	@Test
	public void ct18() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "5", "1", "1", "9", "9", "9", "3", "B . . . A"), "C\nC\n");
		assertIteracoes(saida, "[. B . A .]", "[B . A . .]", "[. A . . .]");
		assertFim(saida, true, 3);
	}

	/** CT19: V1–V6, V9, V10, V14, V16–V22, V27, V29, V30, V33, V34. */
	@Test
	public void ct19() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "2", "B . . A"), "C\nC\nC\n");
		assertIteracoes(saida, "[. B A .]", "[. . B .]", "[. B . .]", "[. . . .]");
		assertFim(saida, true, 4);
	}

	/** CT20: V1–V6, V8, V11, V13, V15–V22, V25, V29. */
	@Test
	public void ct20() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "2", "3", "3", "3", "3", "B B . A"), "E\n");
		assertIteracoes(saida, "[B . B A]");
		assertFim(saida, false, 1);
	}

	/** CT21: V1–V6, V12–V22, V27, V31. */
	@Test
	public void ct21() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "2", "2", "9", "9", "3", "9", "A . B; A B ."), "C\nC\nC\nE\n");
		assertIteracoes(saida, "[A B .; B . .]", "[B . .; . B .]", "[. B .; B . .]", "[B . .; . B .]");
		assertFim(saida, false, 4);
	}

	/** CT22: V1–V6, V12–V22, V27, V28, V31. */
	@Test
	public void ct22() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "2", "2", "9", "9", "2", "9", "A . B; A B ."), "C\nC\nC\nE\n");
		assertIteracoes(saida, "[A B .; B . .]", "[B . .; . B .]", "[. B .; B . .]", "[B . .; B B .]");
		assertFim(saida, false, 4);
	}

	/** CT23: V1–V6, V12–V22, V28, V31. */
	@Test
	public void ct23() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "2", "2", "9", "9", "1", "9", "A . B; A B ."), "C\nC\nC\nE\n");
		assertIteracoes(saida, "[A B .; B . .]", "[B . .; . B .]", "[. B .; B . .]", "[B . .; B B .]");
		assertFim(saida, false, 4);
	}

	/** CT24: V1–V6, V14, V16–V22, V28, V30, V34. */
	@Test
	public void ct24() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "2", "2", "9", "9", "1", "1", "A . B; A B ."), "C\n");
		assertIteracoes(saida, "[A B .; . . .]", "[. . .; A . .]");
		assertFim(saida, true, 2);
	}

	/** CT25: V1–V6, V14–V23, V27, V33, I20. */
	@Test
	public void ct25() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "3", "3", "2", "3", "A . . B"), "Z\nC\nC\nE\n");
		assertIteracoes(saida, "[. A B .]", "[A B . .]", "[B . . .]");
		assertFim(saida, false, 3);
		assertEquals(1, ocorrencias(saida, "Opção inválida."));
	}

	/** CT26: V1–V6, V14, V16–V22, V29, V30, V33, V34. */
	@Test
	public void ct26() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "3", "A . . B"), "C\nC\nC\nC\nC\n");
		assertIteracoes(saida, "[. A B .]", "[A B . .]", "[B . . .]", "[. B . .]", "[B . . .]", "[. . . .]");
		assertFim(saida, true, 6);
	}

	/** CT27: V1–V7, V10, V15–V23. */
	@Test
	public void ct27() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "2", "1", "1", "3", "3", "3", "3", "B .; . A"), "E\n");
		assertIteracoes(saida, "[. A; B .]");
		assertFim(saida, false, 1);
	}

	/** CT28: V1–V7, V9, V15–V22, V27. */
	@Test
	public void ct28() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "2", "1", "1", "9", "9", "9", "9", ". A; B ."), "E\n");
		assertIteracoes(saida, "[. .; . B]");
		assertFim(saida, false, 1);
	}

	/** CT29: V1–V6, V8, V9, V15–V20, V22, V25, V27. */
	@Test
	public void ct29() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "2", "1", "1", "3", "3", "3", "3", "A B"), "E\n");
		assertIteracoes(saida, "[B .]");
		assertFim(saida, false, 1);
	}

	/** CT30: V1–V6, V8, V9, V15–V20, V22. */
	@Test
	public void ct30() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "1", "1", "1", "3", "3", "3", "3", "A; B"), "E\n");
		assertIteracoes(saida, "[B; .]");
		assertFim(saida, false, 1);
	}

	/** CT31: V1–V9, V15–V22, V27. */
	@Test
	public void ct31() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "2", "1", "2", "3", "3", "3", "A A . B"), "E\n");
		assertIteracoes(saida, "[A . B .]");
		assertFim(saida, false, 1);
	}

	/** CT32: V1–V9, V15–V22. */
	@Test
	public void ct32() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "3", "2", "9", "9", "9", "9", "A A A; B B ."), "E\n");
		assertIteracoes(saida, "[B B .; . . A]");
		assertFim(saida, false, 1);
	}

	/** CT33: V1–V6, V8, V9, V15–V20, V22. */
	@Test
	public void ct33() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "4", "2", "9", "9", "9", "9", "A A A; A B B"), "E\n");
		assertIteracoes(saida, "[A B B; A . .]");
		assertFim(saida, false, 1);
	}

	/** CT34: V1–V6, V8–V10, V15–V22. */
	@Test
	public void ct34() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "2", "3", "9", "9", "9", "9", "A A B; B B ."), "E\n");
		assertIteracoes(saida, "[B B .; B . .]");
		assertFim(saida, false, 1);
	}

	/** CT35: V1–V6, V8–V10, V15–V20, V22. */
	@Test
	public void ct35() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "2", "4", "9", "9", "9", "9", "A A B; B B B"), "E\n");
		assertIteracoes(saida, "[B B B; B . .]");
		assertFim(saida, false, 1);
	}

	/** CT61: V1–V7, V14–V22, V25, V26, V28. */
	@Test
	public void ct61() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "8", "5", "1", "9", "3", "1", "9", "A A A A A . . B"), "C\nC\nE\n");
		assertIteracoes(saida, "[A A A A . A B .]", "[A A A . A B B .]", "[A A . A B B . .]");
		assertFim(saida, false, 3);
	}

	/** CT62: V1–V6, V12, V14–V20, V22, V27, V28, V31. */
	@Test
	public void ct62() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "3", "1", "9", "9", "3", "9", "B A A A"), "C\nC\nE\n");
		assertIteracoes(saida, "[. B A A]", "[. . B A]", "[. . B B]");
		assertFim(saida, false, 3);
	}

	/** CT63: V1–V7, V10, V15–V22, V29. */
	@Test
	public void ct63() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "9", "9", "9", "9", "B . . A"), "E\n");
		assertIteracoes(saida, "[. B A .]");
		assertFim(saida, false, 1);
	}

	/** CT64: V1–V6, V9, V15–V22, V24, V27, V35. */
	@Test
	public void ct64() throws IOException {
		String saida = jogar(Aquario.inicializar("3", "3", "1", "1", "1", "9", "9", "9", ". A .; . B .; . . ."), "E\n");
		assertIteracoes(saida, "[A . .; B . .; . . .]");
		assertFim(saida, false, 1);
	}

	/** CT65: V1–V6, V12, V14–V20, V22, V27, V28, V31. */
	@Test
	public void ct65() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "6", "5", "1", "9", "9", "2", "9", "B A A A A A"), "C\nC\nE\n");
		assertIteracoes(saida, "[. B A A A A]", "[. B B A A A]", "[B . . B A A]");
		assertFim(saida, false, 3);
	}

	/** CT66: V1–V9, V14–V25, V27, V35, V36. */
	@Test
	public void ct66() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "4", "1", "2", "9", "9", "9", "A . A; A A B"), "C\nC\nC\nE\n");
		assertIteracoes(saida, "[A A B; A . .]", "[A B .; A A .]", "[A . .; A B .]", "[A B .; . . .]");
		assertFim(saida, false, 4);
	}

	/** CT67: V1–V6, V12–V14, V16–V22, V28–V31, V33, V34. */
	@Test
	public void ct67() throws IOException {
		String saida = jogar(Aquario.inicializar("2", "3", "2", "2", "9", "9", "1", "2", "A . B; A B ."), "C\nC\nC\nC\n");
		assertIteracoes(saida, "[A B .; B . .]", "[B . .; . . .]", "[B . .; B . .]", "[. B .; . . .]", "[. . .; . . .]");
		assertFim(saida, true, 5);
	}

	/** CT68: V1–V9, V15–V20, V22, V27. */
	@Test
	public void ct68() throws IOException {
		String saida = jogar(Aquario.inicializar("1", "3", "2", "1", "9", "9", "9", "9", "A B A"), "E\n");
		assertIteracoes(saida, "[B A .]");
		assertFim(saida, false, 1);
	}

	/** CT69: V1–V9, V15–V20, V22, V27. */
	@Test
	public void ct69() throws IOException {
		String saida = jogar(Aquario.inicializar("3", "1", "2", "1", "9", "9", "9", "9", "A; B; A"), "E\n");
		assertIteracoes(saida, "[B; A; .]");
		assertFim(saida, false, 1);
	}

	/** CT36: I1. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct36() {
		Aquario.inicializar("1", "4", "0", "1", "2", "3", "3", "3", ". . . B");
	}

	/** CT37: I2. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct37() {
		Aquario.inicializar("1", "4", "1.5", "1", "2", "3", "3", "3", "A . . B");
	}

	/** CT38: I3. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct38() {
		Aquario.inicializar("1", "4", "1", "0", "2", "3", "3", "3", "A . . .");
	}

	/** CT39: I4. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct39() {
		Aquario.inicializar("1", "4", "1", "1.5", "2", "3", "3", "3", "A . . B");
	}

	/** CT40: I5. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct40() {
		Aquario.inicializar("1", "4", "1", "1", "0", "3", "3", "3", "A . . B");
	}

	/** CT41: I6. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct41() {
		Aquario.inicializar("1", "4", "1", "1", "1.5", "3", "3", "3", "A . . B");
	}

	/** CT42: I7. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct42() {
		Aquario.inicializar("1", "4", "1", "1", "2", "0", "3", "3", "A . . B");
	}

	/** CT43: I8. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct43() {
		Aquario.inicializar("1", "4", "1", "1", "2", "1.5", "3", "3", "A . . B");
	}

	/** CT44: I9. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct44() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "0", "3", "A . . B");
	}

	/** CT45: I10. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct45() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "1.5", "3", "A . . B");
	}

	/** CT46: I11. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct46() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "0", "A . . B");
	}

	/** CT47: I12. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct47() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "1.5", "A . . B");
	}

	/** CT48: I13. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct48() {
		Aquario.inicializar("0", "4", "1", "1", "2", "3", "3", "3", "A . . B");
	}

	/** CT49: I14. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct49() {
		Aquario.inicializar("1.5", "4", "1", "1", "2", "3", "3", "3", "A . . B");
	}

	/** CT50: I15. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct50() {
		Aquario.inicializar("1", "0", "1", "1", "2", "3", "3", "3", "A . . B");
	}

	/** CT51: I16. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct51() {
		Aquario.inicializar("1", "1.5", "1", "1", "2", "3", "3", "3", "A . . B");
	}

	/** CT52: I17. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct52() {
		Aquario.inicializar("2", "4", "1", "1", "2", "3", "3", "3", "A . . B");
	}

	/** CT53: I17. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct53() {
		Aquario.inicializar("2", "2", "1", "1", "2", "3", "3", "3", "A .; B");
	}

	/** CT54: I18. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct54() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "3", "A ? . B");
	}

	/** CT55: I19. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct55() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "3", "A A . B");
	}

	/** CT56: I19. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct56() {
		Aquario.inicializar("1", "4", "3", "1", "2", "3", "3", "3", "A A . B");
	}

	/** CT57: I19. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct57() {
		Aquario.inicializar("2", "3", "5", "2", "9", "9", "9", "9", "A A A; A B B");
	}

	/** CT58: I19. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct58() {
		Aquario.inicializar("2", "3", "2", "5", "9", "9", "9", "9", "A A B; B B B");
	}

	/** CT59: I19. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct59() {
		Aquario.inicializar("1", "1", "1", "1", "3", "3", "3", "3", "A");
	}

	/** CT60: I19. */
	@Test(expected = EntradaInvalidaException.class)
	public void ct60() {
		Aquario.inicializar("1", "4", "1", "1", "2", "3", "3", "3", "A B B .");
	}
}
