package org.altervista.paspiz85.game.dama;

import java.io.IOException;
import java.net.InetAddress;

import org.altervista.paspiz85.game.Game;

/**
 * Gioco su una scacchiera.
 * 
 * @author Pasquale Pizzuti
 * @version 22/dic/07
 * 
 */
public interface Scacchiera extends Game, ScacchieraColors {

	/**
	 * Casella di una scacchiera.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	public interface Casella {

		/**
		 * Restituisce l'indice della colonna.
		 * 
		 * @return l'indice della colonna.
		 */
		public int getColonna();

		/**
		 * Restituisce il colore della casella.
		 * 
		 * @return vero se la casella è nera.
		 */
		public boolean getColore();

		/**
		 * Restituisce il pezzo sulla casella.
		 * 
		 * @return il pezzo sulla casella.
		 */
		public Pezzo getPezzo();

		/**
		 * Restituisce l'indice della riga.
		 * 
		 * @return l'indice della riga.
		 */
		public int getRiga();

		/**
		 * Restituisce la scacchiera a cui appartiene.
		 * 
		 * @return la scacchiera a cui appartiene.
		 */
		public Scacchiera getScacchiera();

	}

	/**
	 * Giocatore di una scacchiera.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	public interface Giocatore extends Player {

		/**
		 * Restituisce il colore del giocatore.
		 * 
		 * @return il colore del giocatore.
		 */
		public boolean getColore();

		public Scacchiera getGame();

		/**
		 * Effettua una mossa del giocatore.
		 * 
		 * @param mossa
		 *            mossa del giocatore.
		 * @throws ScacchieraException
		 * @throws IOException
		 */
		public boolean gioca(Mossa mossa) throws ScacchieraException,
				IOException;

	}

	/**
	 * Pezzo di una scacchiera.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	public interface Pezzo {

		/**
		 * Restituisce la casella su cui si trova il pezzo.
		 * 
		 * @return la casella su cui si trova il pezzo.
		 */
		public Casella getCasella();

		/**
		 * Restituisce il colore del pezzo.
		 * 
		 * @return il colore del pezzo.
		 */
		public boolean getColore();

		/**
		 * Restituisce la scacchiera a cui appartiene.
		 * 
		 * @return la scacchiera a cui appartiene.
		 */
		public Scacchiera getScacchiera();

	}

	/**
	 * Aggiunge un ascoltatore della scacchiera.
	 * 
	 * @param listener
	 *            ascoltatore della scacchiera.
	 */
	public void addListener(ScacchieraListener listener);

	/**
	 * Restituisce una casella della scacchiera;
	 * 
	 * @param riga
	 *            riga della casella.
	 * @param colonna
	 *            colonna della casella.
	 * @return la casella indicata.
	 */
	public Casella getCasella(int riga, int colonna);

	/**
	 * Restituisce gli ascoltatori della scacchiera.
	 * 
	 * @return gli ascoltatori della scacchiera.
	 */
	public ScacchieraListener[] getListeners();

	/**
	 * Restituisce il numero di pezzi bianchi sulla scacchiera.
	 * 
	 * @return il numero di pezzi bianchi sulla scacchiera.
	 */
	public int getPezziBianchi();

	/**
	 * Restituisce il numero di pezzi neri sulla scacchiera.
	 * 
	 * @return il numero di pezzi neri sulla scacchiera.
	 */
	public int getPezziNeri();

	public Giocatore join(InetAddress address, int port) throws IOException;

	public Giocatore open(int port) throws IOException;

}
