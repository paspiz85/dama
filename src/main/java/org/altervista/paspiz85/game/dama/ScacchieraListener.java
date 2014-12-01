package org.altervista.paspiz85.game.dama;

/**
 * Ascoltatore della scacchiera. Cattura gli eventi di mosse sulla scacchiera.
 * 
 * @author Pasquale Pizzuti
 * @version 22/dic/07
 *
 */
public interface ScacchieraListener {
	
	/**
	 * Segnala l'aggiornamento della scacchiera.
	 * 
	 * @param mossa ultima mossa.
	 */
	public void mossaPerformed(Mossa mossa);
	
	/**
	 * Partita terminata.
	 * 
	 * @param winner vincitore della partita.
	 */
	public void scacchieraClosed(Boolean winner);

}
