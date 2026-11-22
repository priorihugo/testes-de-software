package aquario;

/** Um peixe com sua posição e contadores individuais (D06, D07, D09). */
class Peixe {
	final Tipo tipo;
	int linha;
	int coluna;
	/** Movimentos consecutivos de A desde o último bloqueio ou reprodução. */
	int movimentos;
	/** Ações consecutivas sem movimento (A) ou sem alimentação (B). */
	int fome;
	/** Peixes A consumidos por B desde a última reprodução. */
	int consumos;
	boolean vivo = true;

	Peixe(Tipo tipo, int linha, int coluna) {
		this.tipo = tipo;
		this.linha = linha;
		this.coluna = coluna;
	}
}
