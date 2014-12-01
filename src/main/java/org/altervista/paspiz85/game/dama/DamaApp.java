package org.altervista.paspiz85.game.dama;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementazione del gioco della dama.
 * 
 * @author Pasquale Pizzuti
 * @version 19/dic/07
 * 
 */
public class DamaApp extends ScacchieraImpl implements Dama {

	/**
	 * Pezzo di tipo Dama.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	class Dama extends PezzoImpl {

		/**
		 * Costruttore per oggetti della classe Dama.
		 * 
		 * @param colore
		 *            colore del pezzo.
		 */
		public Dama(boolean colore) {
			super(colore);
		}

		/**
		 * Controlla se una mossa è una mangiata.
		 * 
		 * @param casella
		 *            la casella destinazione.
		 * @return la casella saltata oppure null.
		 */
		protected CasellaImpl checkMangiata(Casella casella) {
			if (casella.getPezzo() != null)
				return null;
			if (Math.abs(getCasella().getRiga() - casella.getRiga()) != 2
					|| Math.abs(getCasella().getColonna()
							- casella.getColonna()) != 2)
				return null;
			int riga = (getCasella().getRiga() + casella.getRiga()) / 2;
			int colonna = (getCasella().getColonna() + casella.getColonna()) / 2;
			CasellaImpl c = getScacchiera().getCasella(riga, colonna);
			Pezzo p = c.getPezzo();
			if (p == null || p.getColore() == getColore())
				return null;
			if (!(p instanceof Dama))
				return null;
			return c;
		}

		/**
		 * Controlla se c'è una mossa obbligatoria.
		 * 
		 * @param casella
		 *            la casella destinazione.
		 * @return vero se c'è una mossa obbligatoria.
		 */
		private boolean checkMossaObbligatoria(CasellaImpl casella) {
			boolean obbligo = false;
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					Dama p = (Dama) getScacchiera().getCasella(i, j).getPezzo();
					if (p != null && p.getColore() == getColore()) {
						for (CasellaImpl c : p.checkOther())
							if (p == this && c == casella)
								return false;
							else
								obbligo = true;
					}
				}
			return obbligo;
		}

		/**
		 * Controlla se ci sono altre mosse da fare.
		 * 
		 * @return vettore con le mosse disponibili.
		 */
		private Vector<CasellaImpl> checkOther() {
			Vector<CasellaImpl> vector = new Vector<CasellaImpl>();
			int i = getCasella().getRiga();
			int j = getCasella().getColonna();
			try {
				CasellaImpl c = getScacchiera().getCasella(i - 2, j - 2);
				if (checkMangiata(c) != null)
					vector.add(c);
			} catch (ArrayIndexOutOfBoundsException ex) {
			}
			try {
				CasellaImpl c = getScacchiera().getCasella(i - 2, j + 2);
				if (checkMangiata(c) != null)
					vector.add(c);
			} catch (ArrayIndexOutOfBoundsException ex) {
			}
			try {
				CasellaImpl c = getScacchiera().getCasella(i + 2, j - 2);
				if (checkMangiata(c) != null)
					vector.add(c);
			} catch (ArrayIndexOutOfBoundsException ex) {
			}
			try {
				CasellaImpl c = getScacchiera().getCasella(i + 2, j + 2);
				if (checkMangiata(c) != null)
					vector.add(c);
			} catch (ArrayIndexOutOfBoundsException ex) {
			}
			return vector;
		}

		/**
		 * Controlla se un mossa è di tipo semplice.
		 * 
		 * @param casella
		 *            la casella destinazione.
		 * @return vero se la mossa semplice è valida.
		 */
		protected boolean checkSemplice(Casella casella) {
			if (casella.getPezzo() != null)
				return false;
			if (Math.abs(getCasella().getRiga() - casella.getRiga()) != 1
					|| Math.abs(getCasella().getColonna()
							- casella.getColonna()) != 1)
				return false;
			return true;
		}

		public boolean muovi(CasellaImpl casella) throws DamaException {
			CasellaImpl src = getCasella();
			if (checkMossaObbligatoria(casella))
				throw new DamaException("Devi mangiare una pedina");
			if (checkSemplice(casella)) {
				casella.setPezzo(this);
				src.setPezzo(null);
				upgrade();
				return false;
			}
			CasellaImpl c;
			if ((c = checkMangiata(casella)) != null) {
				c.setPezzo(null);
				casella.setPezzo(this);
				src.setPezzo(null);
				if (upgrade())
					return false;
				return checkOther().size() != 0;
			}
			throw new DamaException("Mossa non valida");
		}

		/**
		 * Effettua un upgrade del pezzo.
		 * 
		 * @return vero se c'è l'upgrade.
		 */
		protected boolean upgrade() {
			return false;
		}

	}

	/**
	 * Giocatore locale della dama.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	class GiocatoreDama extends GiocatoreImpl {

		/**
		 * Costruttore per oggetti della classe GiocatoreLocale.
		 * 
		 * @param colore
		 *            colore del giocatore.
		 */
		public GiocatoreDama(boolean colore) {
			super(colore);
		}

		public void close() throws IOException {
			if (this == locale)
				stub.close();
		}

		public boolean gioca(Mossa mossa) throws ScacchieraException,
				IOException {
			synchronized (DamaApp.this) {
				if (turno != this)
					throw new DamaException("Non è il tuo turno.");
				CasellaImpl c1 = (CasellaImpl) mossa.getCasella1(getGame());
				CasellaImpl c2 = (CasellaImpl) mossa.getCasella2(getGame());
				PezzoImpl pezzo = c1.getPezzo();
				if (pezzo == null || pezzo.getColore() != getColore())
					throw new DamaException("Pezzo non valido");
				boolean end = !pezzo.muovi(c2);
				if (end) {
					if (turno == locale)
						turno = remoto;
					else
						turno = locale;
				}
				if (this == locale)
					stub.sendMossa(mossa);
				for (ScacchieraListener listener : getListeners()) {
					listener.mossaPerformed(mossa);
					if (getPezziNeri() == 0) {
						listener.scacchieraClosed(BIANCO);
						close();
					}
					if (getPezziBianchi() == 0) {
						listener.scacchieraClosed(NERO);
						close();
					}
				}
				return end;
			}
		}

		public void setNickname(String nickname) throws IOException {
			super.setNickname(nickname);
			if (this == locale)
				stub.sendNickname(nickname);
		}

	}

	/**
	 * Stub per il giocatore remoto.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	class GiocatoreRemoto {

		private GiocatoreDama giocatore;

		private ObjectInputStream in;

		private Logger logger;

		private ObjectOutputStream out;

		private Socket socket;

		/**
		 * Costruttore per oggetti della classe GiocatoreRemoto.
		 * 
		 * @param address
		 *            indirizzo del giocatore.
		 * @param port
		 *            porta del giocatore.
		 * @param g
		 *            giocatore locale associato.
		 * @throws IOException
		 */
		public GiocatoreRemoto(InetAddress address, int port, GiocatoreDama g)
				throws IOException {
			this.giocatore = g;
			logger = Logger.getLogger("GiocatoreRemoto"); //$NON-NLS-1$
			logger.addHandler(new FileHandler("remote.log")); //$NON-NLS-1$
			if (address == null) {
				logger.info("In attesa di connessioni...");
				ServerSocket server = new ServerSocket(port);
				server.setSoTimeout(60000);
				socket = server.accept();
			} else {
				logger.info("Connessione in corso...");
				socket = new Socket(address, port);
			}
			logger.info("Connessione stabilita");
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			new Thread(new Runnable() {
				public void run() {
					try {
						while (true) {
							Object obj = in.readObject();
							if (obj instanceof String) {
								String nickname = (String) obj;
								logger.info("Nickname ricevuto: " + nickname);
								giocatore.setNickname(nickname);
							} else if (obj instanceof Mossa) {
								Mossa mossa = (Mossa) obj;
								logger.info("Mossa ricevuta: " + mossa);
								giocatore.gioca(mossa);
							} else if (obj instanceof Double) {
								logger.info("Partita terminata da remoto");
								for (ScacchieraListener listener : getListeners())
									listener.scacchieraClosed(null);
							} else {
								logger.warning("Ricevuto: " + obj);
							}
						}
					} catch (Exception e) {
						logger.log(Level.FINER, e.getMessage(), e);
					}
				}
			}).start();
		}

		public void close() throws IOException {
			try {
				out.writeObject(new Double(0));
				socket.close();
				logger.info("Partita terminata");
			} catch (IOException e) {
				logger.log(Level.FINER, e.getMessage(), e);
				throw e;
			}
		}

		public boolean sendMossa(Mossa mossa) throws ScacchieraException,
				IOException {
			try {
				out.writeObject(mossa);
				logger.info("Mossa inviata: " + mossa);
				return true;
			} catch (IOException e) {
				logger.log(Level.FINER, e.getMessage(), e);
				throw e;
			}
		}

		public void sendNickname(String nickname) throws IOException {
			try {
				out.writeObject(nickname);
				logger.info("Nickname inviato: " + nickname);
			} catch (IOException e) {
				logger.log(Level.FINER, e.getMessage(), e);
				throw e;
			}
		}

	}

	/**
	 * Pezzo di tipo Pedina.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 20/dic/07
	 * 
	 */
	class Pedina extends Dama {

		/**
		 * Costruttore per oggetti della classe Pedina.
		 * 
		 * @param colore
		 *            colore del pezzo.
		 */
		public Pedina(boolean colore) {
			super(colore);
		}

		protected CasellaImpl checkMangiata(Casella casella) {
			if (getColore() == BIANCO
					&& casella.getRiga() < getCasella().getRiga())
				return null;
			if (getColore() == NERO
					&& casella.getRiga() > getCasella().getRiga())
				return null;
			CasellaImpl c = super.checkMangiata(casella);
			if (c == null)
				return null;
			Pezzo p = c.getPezzo();
			if (!(p instanceof Pedina))
				return null;
			return c;
		}

		protected boolean checkSemplice(Casella casella) {
			if (getColore() == BIANCO
					&& casella.getRiga() < getCasella().getRiga())
				return false;
			if (getColore() == NERO
					&& casella.getRiga() > getCasella().getRiga())
				return false;
			return super.checkSemplice(casella);
		}

		protected boolean upgrade() {
			if (getColore() == BIANCO && getCasella().getRiga() == 7) {
				getCasella().setPezzo(new Dama(BIANCO));
				return true;
			}
			if (getColore() == NERO && getCasella().getRiga() == 0) {
				getCasella().setPezzo(new Dama(NERO));
				return true;
			}
			return false;
		}

	}

	private GiocatoreDama locale = null;

	private GiocatoreDama remoto = null;

	private GiocatoreRemoto stub = null;

	private GiocatoreDama turno = null;

	/**
	 * Costruttore per oggetti della classe DamaApp.
	 * 
	 */
	public DamaApp() {
		super();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (getCasella(i, j).getColore()) {
					if (i < 3)
						getCasella(i, j).setPezzo(new Pedina(BIANCO));
					if (i > 4)
						getCasella(i, j).setPezzo(new Pedina(NERO));
				}
			}
	}

	public String[] getRemoteNicknames() {
		if (stub == null)
			throw new IllegalStateException("Partita non iniziata");
		return new String[] { remoto.getNickname() };
	}

	public GiocatoreImpl join(InetAddress address, int port) throws IOException {
		synchronized (this) {
			if (address == null)
				throw new NullPointerException();
			if (stub == null) {
				locale = new GiocatoreDama(BIANCO);
				remoto = new GiocatoreDama(NERO);
				turno = locale;
				stub = new GiocatoreRemoto(address, port, remoto);
				return locale;
			}
			throw new IllegalStateException("Partita già completa");
		}
	}

	public GiocatoreImpl open(int port) throws IOException {
		synchronized (this) {
			if (stub == null) {
				remoto = new GiocatoreDama(BIANCO);
				locale = new GiocatoreDama(NERO);
				turno = remoto;
				stub = new GiocatoreRemoto(null, port, remoto);
				return locale;
			}
			throw new IllegalStateException("Partita già completa");
		}
	}
}
