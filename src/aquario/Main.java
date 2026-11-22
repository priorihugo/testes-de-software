package aquario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/** Interface de console: lê os parâmetros e a matriz e inicia o jogo. */
public class Main {

	private static final String[] PARAMETROS = { "M", "N", "X", "Y", "RA", "MA", "RB", "MB" };

	private Main() {
	}

	public static void main(String[] args) throws IOException {
		executar(new BufferedReader(new InputStreamReader(System.in, "UTF-8")), System.out);
	}

	/**
	 * Lê M, N, X, Y, RA, MA, RB, MB e as M linhas da matriz (células separadas
	 * por espaço, com A, B ou .) e executa a partida.
	 *
	 * @return pontuação final, ou -1 se a inicialização for recusada
	 */
	static int executar(BufferedReader entrada, PrintStream saida) throws IOException {
		String[] valores = new String[PARAMETROS.length];
		for (int k = 0; k < PARAMETROS.length; k++) {
			saida.print(PARAMETROS[k] + ": ");
			valores[k] = lerLinha(entrada);
		}
		int linhas;
		try {
			linhas = Integer.parseInt(valores[0].trim());
		} catch (NumberFormatException e) {
			linhas = 0;
		}
		StringBuilder matriz = new StringBuilder();
		for (int i = 0; i < linhas; i++) {
			saida.print("Linha " + (i + 1) + " da matriz: ");
			if (i > 0) {
				matriz.append(';');
			}
			matriz.append(lerLinha(entrada));
		}
		try {
			Aquario aquario = Aquario.inicializar(valores[0], valores[1], valores[2], valores[3], valores[4],
					valores[5], valores[6], valores[7], matriz.toString());
			saida.println("Matriz inicial: " + aquario.getMatriz());
			return Jogo.executar(aquario, entrada, saida);
		} catch (EntradaInvalidaException e) {
			saida.println("Inicialização recusada: " + e.getMessage());
			return -1;
		}
	}

	private static String lerLinha(BufferedReader entrada) throws IOException {
		String linha = entrada.readLine();
		return linha == null ? "" : linha;
	}
}
