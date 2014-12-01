package org.altervista.paspiz85.game.dama;

import java.io.IOException;
import java.util.Vector;

/**
 * Implementazione di una scacchiera.
 * 
 * @author Pasquale Pizzuti
 * @version 22/dic/07
 * 
 */
public abstract class ScacchieraImpl implements Scacchiera {

	/**
	 * Implementazione di una casella.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	class CasellaImpl implements Casella {

		private int colonna;

		private PezzoImpl pezzo = null;

		private int riga;

		/**
		 * Costruttore per oggetti della classe CasellaImpl.
		 * 
		 * @param row
		 *            indice della riga.
		 * @param column
		 *            indice della colonna.
		 */
		public CasellaImpl(int row, int column) {
			this.riga = row;
			this.colonna = column;
		}

		public int getColonna() {
			return colonna;
		}

		public boolean getColore() {
			return (riga + colonna) % 2 != 0;
		}

		public PezzoImpl getPezzo() {
			return pezzo;
		}

		public int getRiga() {
			return riga;
		}

		public ScacchieraImpl getScacchiera() {
			return ScacchieraImpl.this;
		}

		/**
		 * Imposta il pezzo sulla casella.
		 * 
		 * @param pezzo
		 *            pezzo della casella.
		 */
		public void setPezzo(PezzoImpl pezzo) {
			if (this.pezzo == null && pezzo != null && !pezzo.getColore())
				bianchi++;
			if (this.pezzo == null && pezzo != null && pezzo.getColore())
				neri++;
			if (this.pezzo != null && pezzo == null && !this.pezzo.getColore())
				bianchi--;
			if (this.pezzo != null && pezzo == null && this.pezzo.getColore())
				neri--;
			this.pezzo = pezzo;
			if (pezzo != null)
				pezzo.setCasella(this);
		}

		public String toString() {
			return (char) ('a' + colonna) + "" + (8 - riga); //$NON-NLS-1$
		}

	}

	/**
	 * Implementazione di un giocatore.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	abstract class GiocatoreImpl implements Giocatore {

		private boolean colore;
		
		private String nickname;

		/**
		 * Costruttore per oggetti della classe GiocatoreImpl.
		 * 
		 * @param colore
		 *            colore del giocatore.
		 */
		public GiocatoreImpl(boolean colore) {
			this.colore = colore;
		}
		
		public boolean getColore() {
			return colore;
		}

		public ScacchieraImpl getGame() {
			return ScacchieraImpl.this;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) throws IOException {
			this.nickname = nickname;
		}

	}

	/**
	 * Implementazione di un pezzo.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	abstract class PezzoImpl implements Pezzo {

		private CasellaImpl casella;

		private boolean colore;

		/**
		 * Costruttore per oggetti della classe PezzoImpl.
		 * 
		 * @param colore
		 *            colore del pezzo.
		 */
		public PezzoImpl(boolean colore) {
			this.colore = colore;
		}

		public CasellaImpl getCasella() {
			return casella;
		}

		public boolean getColore() {
			return colore;
		}

		public ScacchieraImpl getScacchiera() {
			return getCasella().getScacchiera();
		}

		/**
		 * Muove il pezzo su una nuova casella.
		 * 
		 * @param casella
		 *            la casella destinazione.
		 * @return vero se ci sono altre mosse da fare.
		 * @throws ScacchieraException
		 */
		public abstract boolean muovi(CasellaImpl casella)
				throws ScacchieraException;

		/**
		 * Imposta la casella su cui si trova il pezzo.
		 * 
		 * @param casella
		 *            casella su cui si trova il pezzo.
		 * @deprecated
		 * @see CasellaImpl#setPezzo(org.altervista.paspiz85.dama.ScacchieraImpl.PezzoImpl)
		 */
		public void setCasella(CasellaImpl casella) {
			this.casella = casella;
		}

	}

	private int bianchi;

	private Vector<ScacchieraListener> listeners = new Vector<ScacchieraListener>();

	private int neri;

	private CasellaImpl[][] scacchiera;

	/**
	 * Costruttore per oggetti della classe ScacchieraApp.
	 * 
	 */
	public ScacchieraImpl() {
		scacchiera = new CasellaImpl[8][8];
		bianchi = 0;
		neri = 0;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				scacchiera[i][j] = new CasellaImpl(i, j);
	}

	public void addListener(ScacchieraListener listener) {
		listeners.add(listener);
	}

	public CasellaImpl getCasella(int riga, int colonna) {
		return scacchiera[riga][colonna];
	}

	public ScacchieraListener[] getListeners() {
		return listeners.toArray(new ScacchieraListener[0]);
	}

	public int getPezziBianchi() {
		return bianchi;
	}

	public int getPezziNeri() {
		return neri;
	}

}
