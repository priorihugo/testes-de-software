package aquario;

/** Lançada quando os parâmetros de inicialização são recusados (D03). */
public class EntradaInvalidaException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	public EntradaInvalidaException(String mensagem) {
		super(mensagem);
	}
}
