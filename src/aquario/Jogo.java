package aquario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/** Controle da partida: iterações, opção do jogador e pontuação (RF11–RF13, D11). */
public class Jogo {

	private Jogo() {
	}

	/**
	 * Executa iterações até não haver B ou o jogador escolher E. Após cada
	 * iteração mostra a matriz; com B remanescente pede C (continuar) ou E
	 * (encerrar). Opção inválida apenas repete a pergunta. Fim da entrada
	 * equivale a E.
	 *
	 * @return pontuação final (iterações concluídas)
	 */
	public static int executar(Aquario aquario, BufferedReader entrada, PrintStream saida) throws IOException {
		while (true) {
			aquario.iterar();
			saida.println("Iteração " + aquario.getPontuacao() + ": " + aquario.getMatriz());
			if (!aquario.temPeixeB()) {
				saida.println("Não há mais peixes B. Fim de jogo.");
				break;
			}
			if (lerOpcao(entrada, saida).equals("E")) {
				saida.println("Jogo encerrado pelo jogador.");
				break;
			}
		}
		saida.println("Pontuação final: " + aquario.getPontuacao());
		return aquario.getPontuacao();
	}

	private static String lerOpcao(BufferedReader entrada, PrintStream saida) throws IOException {
		while (true) {
			saida.print("Digite C para continuar ou E para encerrar: ");
			String linha = entrada.readLine();
			if (linha == null) {
				return "E";
			}
			String opcao = linha.trim();
			if (opcao.equals("C") || opcao.equals("E")) {
				return opcao;
			}
			saida.println("Opção inválida.");
		}
	}
}
