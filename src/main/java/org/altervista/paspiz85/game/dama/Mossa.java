package org.altervista.paspiz85.game.dama;

import java.io.Serializable;

import org.altervista.paspiz85.game.dama.Scacchiera.Casella;

/**
 * Mossa sulla scacchiera.
 * 
 * @author Pasquale Pizzuti
 * @version 21/dic/07
 * 
 */
public class Mossa implements Serializable {

	private static final long serialVersionUID = 4799548173113339832L;

	private int i1;

	private int j1;

	private int i2;

	private int j2;

	private String str;

	/**
	 * Costruttore per oggetti della classe Mossa.
	 * 
	 * @param c1
	 *            prima casella.
	 * @param c2
	 *            seconda casella.
	 */
	public Mossa(Casella c1, Casella c2) {
		i1 = c1.getRiga();
		j1 = c1.getColonna();
		i2 = c2.getRiga();
		j2 = c2.getColonna();
		str = c1 + "-" + c2; //$NON-NLS-1$
	}

	/**
	 * Restituisce la prima casella.
	 * 
	 * @param scacchiera
	 *            scacchiera della casella.
	 * @return la prima casella.
	 */
	public Casella getCasella1(Scacchiera scacchiera) {
		return scacchiera.getCasella(i1, j1);
	}

	/**
	 * Restituisce la seconda casella.
	 * 
	 * @param scacchiera
	 *            scacchiera della casella.
	 * @return la seconda casella.
	 */
	public Casella getCasella2(Scacchiera scacchiera) {
		return scacchiera.getCasella(i2, j2);
	}

	public String toString() {
		return str;
	}

}
