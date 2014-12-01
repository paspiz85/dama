package org.altervista.paspiz85.game.dama;

/**
 * Eccezione di un gioco sulla scacchiera.
 * 
 * @author Pasquale Pizzuti
 * @version 22/dic/07
 * 
 */
public class ScacchieraException extends Exception {

	private static final long serialVersionUID = 724006917522993490L;

	/**
	 * Costruttore per oggetti della classe ScacchieraException.
	 * 
	 * @param message
	 *            messaggio di errore.
	 */
	public ScacchieraException(String message) {
		super(message);
	}

}
