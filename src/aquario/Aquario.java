package aquario;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Estado e regras do Jogo do Aquário.
 *
 * A matriz usa a notação da Parte I: linhas separadas por ";", células separadas
 * por espaço, "A" e "B" para os peixes e "." para célula vazia, por exemplo
 * "[A . .; . . B]". Os colchetes são opcionais na entrada.
 */
public class Aquario {

	/** Ordem de desempate dos destinos: acima, abaixo, esquerda, direita (D01, D05). */
	private static final int[][] DIRECOES = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

	private final int linhas;
	private final int colunas;
	private final int ra;
	private final int ma;
	private final int rb;
	private final int mb;
	private final Peixe[][] grade;
	private int pontuacao;

	private Aquario(int linhas, int colunas, int ra, int ma, int rb, int mb, Peixe[][] grade) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.ra = ra;
		this.ma = ma;
		this.rb = rb;
		this.mb = mb;
		this.grade = grade;
	}

	/**
	 * Valida os parâmetros e cria o aquário (D02, D03). A validação segue a ordem:
	 * domínio numérico, estrutura da matriz, conteúdo das células e contagens.
	 *
	 * @throws EntradaInvalidaException se alguma condição de entrada for inválida
	 */
	public static Aquario inicializar(String m, String n, String x, String y, String ra, String ma,
			String rb, String mb, String matriz) {
		int vx = inteiroPositivo("X", x);
		int vy = inteiroPositivo("Y", y);
		int vra = inteiroPositivo("RA", ra);
		int vma = inteiroPositivo("MA", ma);
		int vrb = inteiroPositivo("RB", rb);
		int vmb = inteiroPositivo("MB", mb);
		int vm = inteiroPositivo("M", m);
		int vn = inteiroPositivo("N", n);

		String[][] celulas = lerMatriz(matriz);
		if (celulas.length != vm) {
			throw new EntradaInvalidaException("A matriz tem " + celulas.length + " linha(s), mas M=" + vm);
		}
		for (int i = 0; i < vm; i++) {
			if (celulas[i].length != vn) {
				throw new EntradaInvalidaException(
						"A linha " + (i + 1) + " tem " + celulas[i].length + " célula(s), mas N=" + vn);
			}
		}

		Peixe[][] grade = new Peixe[vm][vn];
		int contaA = 0;
		int contaB = 0;
		for (int i = 0; i < vm; i++) {
			for (int j = 0; j < vn; j++) {
				String s = celulas[i][j];
				if (s.equals("A")) {
					grade[i][j] = new Peixe(Tipo.A, i, j);
					contaA++;
				} else if (s.equals("B")) {
					grade[i][j] = new Peixe(Tipo.B, i, j);
					contaB++;
				} else if (!s.equals(".")) {
					throw new EntradaInvalidaException(
							"Símbolo inválido \"" + s + "\" na célula (" + (i + 1) + "," + (j + 1) + ")");
				}
			}
		}
		if (contaA != vx) {
			throw new EntradaInvalidaException("A matriz tem " + contaA + " peixe(s) A, mas X=" + vx);
		}
		if (contaB != vy) {
			throw new EntradaInvalidaException("A matriz tem " + contaB + " peixe(s) B, mas Y=" + vy);
		}
		return new Aquario(vm, vn, vra, vma, vrb, vmb, grade);
	}

	/** Aceita apenas inteiros positivos escritos só com dígitos (D02, D13). */
	private static int inteiroPositivo(String nome, String valor) {
		String texto = valor.trim();
		if (texto.matches("-?\\d+")) {
			BigInteger numero = new BigInteger(texto);
			if (numero.signum() <= 0) {
				throw new EntradaInvalidaException(nome + "=" + valor + " não é positivo");
			}
			if (numero.bitLength() > 31) {
				throw new EntradaInvalidaException(nome + "=" + valor + " é grande demais");
			}
			return numero.intValue();
		}
		if (texto.matches("-?\\d*[.,]\\d+")) {
			throw new EntradaInvalidaException(nome + "=" + valor + " não é inteiro");
		}
		throw new EntradaInvalidaException(nome + "=" + valor + " não é um inteiro escrito só com dígitos");
	}

	private static String[][] lerMatriz(String matriz) {
		String texto = matriz.trim();
		if (texto.startsWith("[") && texto.endsWith("]")) {
			texto = texto.substring(1, texto.length() - 1);
		}
		String[] partes = texto.split(";", -1);
		String[][] celulas = new String[partes.length][];
		for (int i = 0; i < partes.length; i++) {
			String linha = partes[i].trim();
			celulas[i] = linha.isEmpty() ? new String[0] : linha.split("\\s+");
		}
		return celulas;
	}

	/**
	 * Executa uma iteração (D04): cada peixe existente no início age uma vez, em
	 * ordem de linha e coluna, sobre a matriz já atualizada pelos anteriores.
	 * Peixes mortos ou consumidos não agem; filhos só agem na próxima iteração.
	 */
	public void iterar() {
		List<Peixe> ordem = new ArrayList<Peixe>();
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				if (grade[i][j] != null) {
					ordem.add(grade[i][j]);
				}
			}
		}
		for (Peixe p : ordem) {
			if (!p.vivo) {
				continue;
			}
			if (p.tipo == Tipo.A) {
				agirA(p);
			} else {
				agirB(p);
			}
		}
		pontuacao++;
	}

	/** Movimento, morte e reprodução de A (D06, D09, D10). */
	private void agirA(Peixe p) {
		int[] livre = vizinhoVazio(p);
		if (livre != null) {
			mover(p, livre);
			p.movimentos++;
			p.fome = 0;
		} else {
			p.movimentos = 0;
			p.fome++;
		}
		if (p.fome >= ma) {
			remover(p);
			return;
		}
		if (p.movimentos >= ra) {
			// Atingir RA exige ter se movido nesta ação, o que deixou a origem livre:
			// sempre há célula para o filho (restrição R(C3, C4), seção 6.5 da Parte I).
			nascer(Tipo.A, vizinhoVazio(p));
			p.movimentos = 0;
		}
	}

	/** Alimentação, movimento, morte e reprodução de B (D07, D08, D09, D10). */
	private void agirB(Peixe p) {
		int[] presa = vizinhoDoTipo(p, Tipo.A);
		if (presa != null) {
			grade[presa[0]][presa[1]].vivo = false;
			mover(p, presa);
			p.consumos++;
			p.fome = 0;
		} else {
			int[] livre = vizinhoVazio(p);
			if (livre != null) {
				mover(p, livre);
			}
			p.fome++;
		}
		if (p.fome >= mb) {
			remover(p);
			return;
		}
		if (p.consumos >= rb && vizinhoDoTipo(p, Tipo.B) == null) {
			// Sem outro B vizinho há sempre célula livre: B que comeu ou se moveu deixou
			// a origem livre, e B que ficou parado está cercado por outros B
			// (restrição I(C9, C10), seção 6.5 da Parte I).
			nascer(Tipo.B, vizinhoVazio(p));
			p.consumos = 0;
		}
	}

	private int[] vizinhoVazio(Peixe p) {
		for (int[] d : DIRECOES) {
			int i = p.linha + d[0];
			int j = p.coluna + d[1];
			if (dentro(i, j) && grade[i][j] == null) {
				return new int[] { i, j };
			}
		}
		return null;
	}

	private int[] vizinhoDoTipo(Peixe p, Tipo tipo) {
		for (int[] d : DIRECOES) {
			int i = p.linha + d[0];
			int j = p.coluna + d[1];
			if (dentro(i, j) && grade[i][j] != null && grade[i][j].tipo == tipo) {
				return new int[] { i, j };
			}
		}
		return null;
	}

	private boolean dentro(int i, int j) {
		return i >= 0 && i < linhas && j >= 0 && j < colunas;
	}

	private void mover(Peixe p, int[] destino) {
		grade[p.linha][p.coluna] = null;
		p.linha = destino[0];
		p.coluna = destino[1];
		grade[p.linha][p.coluna] = p;
	}

	private void remover(Peixe p) {
		p.vivo = false;
		grade[p.linha][p.coluna] = null;
	}

	private void nascer(Tipo tipo, int[] destino) {
		grade[destino[0]][destino[1]] = new Peixe(tipo, destino[0], destino[1]);
	}

	/** Indica se ainda há peixe B; sem B o jogo termina (RF12). */
	public boolean temPeixeB() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				if (grade[i][j] != null && grade[i][j].tipo == Tipo.B) {
					return true;
				}
			}
		}
		return false;
	}

	/** Número de iterações concluídas (RF13). */
	public int getPontuacao() {
		return pontuacao;
	}

	/** Matriz atual na notação da Parte I, por exemplo "[A . .; . . B]". */
	public String getMatriz() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < linhas; i++) {
			if (i > 0) {
				sb.append("; ");
			}
			for (int j = 0; j < colunas; j++) {
				if (j > 0) {
					sb.append(' ');
				}
				sb.append(grade[i][j] == null ? "." : grade[i][j].tipo.name());
			}
		}
		return sb.append(']').toString();
	}
}
