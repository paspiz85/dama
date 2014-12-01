package org.altervista.paspiz85.game;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Interfaccia per un gioco in rete.
 * 
 * @author Pasquale Pizzuti
 * @version 22/dic/07
 * 
 */
public interface Game {

	/**
	 * Giocatore di un gioco in rete.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	public interface Player {
		
		/**
		 * Chiude la partita.
		 * 
		 * @throws IOException
		 */
		public void close() throws IOException;

		/**
		 * Restituisce il gioco a cui sta partecipando il giocatore.
		 * 
		 * @return il gioco a cui sta partecipando il giocatore.
		 */
		public Game getGame();

		/**
		 * Restituisce il nome del giocatore.
		 * 
		 * @return il nome del giocatore.
		 */
		public abstract String getNickname();

		/**
		 * Restituisce il nome del giocatore.
		 * 
		 * @param nickname
		 *            nome del giocatore.
		 * @throws IOException
		 */
		public abstract void setNickname(String nickname) throws IOException;

	}

	/**
	 * Restituisce i nickname dei giocatori avversari.
	 * 
	 * @return i nickname dei giocatori avversari.
	 */
	public String[] getRemoteNicknames();

	/**
	 * Permette ad un giocatore di collegarsi ad una partita.
	 * 
	 * @param address
	 *            indirizzo dell'avversario.
	 * @param port
	 *            porta dell'avversario.
	 * @return istanza del giocatore.
	 * @throws IOException
	 */
	public Player join(InetAddress address, int port) throws IOException;

	/**
	 * Permette ad un giocatore di iniziare una partita.
	 * 
	 * @param port
	 *            porta del giocatore.
	 * @return istanza del giocatore.
	 * @throws IOException
	 */
	public Player open(int port) throws IOException;

}
