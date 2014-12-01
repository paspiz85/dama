package org.altervista.paspiz85.game.dama;

/**
 * Eccezione della dama.
 * 
 * @author Pasquale Pizzuti
 * @version 22/dic/07
 * 
 */
public class DamaException extends ScacchieraException {

	private static final long serialVersionUID = 724006917522993490L;

	/**
	 * Costruttore per oggetti della classe DamaException.
	 * 
	 * @param message
	 *            messaggio di errore.
	 */
	public DamaException(String message) {
		super(message);
	}

}
