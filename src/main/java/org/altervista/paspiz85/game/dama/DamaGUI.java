package org.altervista.paspiz85.game.dama;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.altervista.paspiz85.game.dama.Scacchiera.Giocatore;
import org.altervista.paspiz85.gui.HelpBrowser;

/**
 * Interfaccia grafica per il gioco della dama.
 * 
 * @author Pasquale Pizzuti
 * @version 22/dic/07
 * 
 */
public class DamaGUI implements Dama, Giocatore, ScacchieraListener {

	/**
	 * Casella della scacchiera.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	class CasellaButton extends JButton {

		private static final long serialVersionUID = 4254411798769268311L;

		private Casella casella;

		/**
		 * Costruttore per oggetti della classe CasellaButton.
		 * 
		 * @param casella
		 *            casella associata.
		 */
		public CasellaButton(Casella casella) {
			this.casella = casella;
		}

		/**
		 * Restituisce la casella associata.
		 * 
		 * @return la casella associata.
		 */
		public Casella getCasella() {
			return casella;
		}
	}

	/**
	 * Eccezione che si verifica quando un giocatore annulla la partita.
	 * 
	 * @author Pasquale Pizzuti
	 * @version 22/dic/07
	 * 
	 */
	class NoGameException extends Exception {

		private static final long serialVersionUID = -6956764068919462914L;

	}

	/**
	 * Avvio dell'applicazione.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = null;
				boolean completed = false;
				try {
					DamaGUI gui = null;
					String str = null;
					String nickname = showInputDialog(null,
							"Inserisci il tuo nickname", null);
					if (nickname == null)
						return;
					int r = JOptionPane.showConfirmDialog(null,
							"Conosci un altro giocatore?", NAME,
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (r == JOptionPane.YES_OPTION) {
						str = showInputDialog(null,
								"Inserisci l'indirizzo IP dell'avversario",
								"paspiz85.homedns.org");
						if (str == null)
							return;
						InetAddress address = InetAddress.getByName(str);
						str = showInputDialog(null,
								"Inserisci il numero di porta dell'avversario",
								"8285"); //$NON-NLS-1$
						if (str == null)
							return;
						int port = Integer.parseInt(str);
						gui = new DamaGUI(nickname, address, port);
					} else if (r == JOptionPane.NO_OPTION) {
						str = showInputDialog(null,
								"Inserisci il tuo numero di porta", "8285"); //$NON-NLS-2$
						if (str == null)
							return;
						int port = Integer.parseInt(str);
						gui = new DamaGUI(nickname, port);
					} else {
						return;
					}
					gui.getFrame().setVisible(true);
					completed = true;
				} catch (NumberFormatException ex) {
					showErrorDialog(null, "Numero non valido");
				} catch (BindException ex) {
					showErrorDialog(null, "Numero di porta in uso");
				} catch (SocketTimeoutException ex) {
					showErrorDialog(null, "Nessun giocatore collegato");
				} catch (ConnectException ex) {
					showErrorDialog(null, "Nessun giocatore collegato");
				} catch (Exception ex) {
					ex.printStackTrace();
					showErrorDialog(null, ex.getMessage());
				} finally {
					if (!completed && frame != null)
						frame.dispose();
				}
			}
		});
	}

	private static void showErrorDialog(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, NAME,
				JOptionPane.ERROR_MESSAGE);
	}

	private static String showInputDialog(Component parent, String message,
			String initial) {
		JOptionPane pane = new JOptionPane();
		pane.setMessage(message);
		pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
		pane.setInitialSelectionValue(initial);
		pane.setWantsInput(true);
		JDialog dialog = pane.createDialog(parent, NAME);
		dialog.setVisible(true);
		dialog.dispose();
		if (pane.getValue().equals(JOptionPane.OK_OPTION))
			return (String) pane.getInputValue();
		else
			return null;
	}

	private CasellaButton[][] casellaButton = new CasellaButton[8][8];

	private JMenu fileMenu = null;

	private JFrame frame = null;

	private Giocatore giocatore;

	private JMenu helpMenu = null;

	private JMenuItem helpMenuItem = null;

	private JMenuItem infoMenuItem = null;

	private JPanel jContentPane = null;

	private JMenuBar menu = null;

	private JMenuItem nicknameMenuItem = null;

	private JMenuItem puntiMenuItem = null;

	private JMenuItem quitMenuItem = null;

	private JPanel scacchieraPanel = null;

	private Casella selected = null;

	private JLabel statusLabel = null;

	private JPanel statusPanel = null;

	public DamaGUI(String nickname, InetAddress address, int port)
			throws IOException {
		if (nickname == null)
			throw new NullPointerException();
		giocatore = join(address, port);
		giocatore.setNickname(nickname);
		addListener(this);
		getStatusLabel().setText("Fai la prima mossa");
	}

	public DamaGUI(String nickname, int port) throws IOException {
		if (nickname == null)
			throw new NullPointerException();
		giocatore = open(port);
		giocatore.setNickname(nickname);
		addListener(this);
		getStatusLabel().setText("Attendi la mossa dell'avversario");
	}

	public void close() {
		try {
			giocatore.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			getStatusLabel().setText(ex.getMessage());
		} finally {
			getFrame().dispose();
		}
	}

	public Casella getCasella(int i, int j) {
		return getGame().getCasella(i, j);
	}

	private CasellaButton getCasellaButton(int i, int j) {
		if (casellaButton[i][j] == null) {
			casellaButton[i][j] = new CasellaButton(getCasella(i, j));
			casellaButton[i][j].setPreferredSize(new Dimension(64, 64));
			casellaButton[i][j]
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							CasellaButton src = (CasellaButton) e.getSource();
							Casella casella = src.getCasella();
							if (casella.getColore() == BIANCO)
								return;
							if (selected == null) {
								src.setBackground(Color.ORANGE);
								getStatusLabel().setText(
										"Selezione corrente: " + casella);
								selected = casella;
								return;
							} else if (selected == casella) {
								getStatusLabel().setText(" "); //$NON-NLS-1$
								selected = null;
							} else {
								try {
									Mossa mossa = new Mossa(selected, casella);
									if (gioca(mossa)) {
										mossaPerformed(null);
										getStatusLabel().setText(
												"Attendi la mossa avversaria");
									} else {
										mossaPerformed(null);
										getStatusLabel().setText(
												"Hai un altra mossa da fare");
									}
								} catch (Exception ex) {
									ex.printStackTrace();
									mossaPerformed(null);
									getStatusLabel().setText(ex.getMessage());
								}
								selected = null;
							}
						}
					});
			casellaButton[i][j].setToolTipText(getCasella(i, j).toString());
			mossaPerformed(null);
		}
		return casellaButton[i][j];
	}

	public boolean getColore() {
		return giocatore.getColore();
	}

	/**
	 * This method initializes fileMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getPuntiMenuItem());
			fileMenu.add(getNicknameMenuItem());
			fileMenu.addSeparator();
			fileMenu.add(getQuitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes frame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame();
			frame.setTitle(NAME);
			setNicknameTitle(getNickname());
			frame.setResizable(false);
			frame.setJMenuBar(getMenu());
			frame.setContentPane(getJContentPane());
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					int r = JOptionPane.showConfirmDialog(getFrame(),
							"Vuoi abbandonare la partita in corso?", NAME,
							JOptionPane.OK_CANCEL_OPTION);
					if (r == JOptionPane.OK_OPTION)
						close();
				}
			});
			frame.pack();
			frame.setLocationRelativeTo(null);
		}
		return frame;
	}

	/**
	 * This method initializes helpMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("?");
			helpMenu.add(getHelpMenuItem());
			helpMenu.addSeparator();
			helpMenu.add(getInfoMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes helpMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getHelpMenuItem() {
		if (helpMenuItem == null) {
			helpMenuItem = new JMenuItem();
			helpMenuItem.setText("Guida in linea");
			helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
				HelpBrowser gui = new HelpBrowser(
						"/org/altervista/paspiz85/game/dama/help.html"); //$NON-NLS-1$

				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFrame frame = gui.getFrame();
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setSize(new Dimension(800, 600));
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				}
			});
		}
		return helpMenuItem;
	}

	/**
	 * This method initializes infoMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getInfoMenuItem() {
		if (infoMenuItem == null) {
			infoMenuItem = new JMenuItem();
			infoMenuItem.setText("Informazioni su " + NAME);
			infoMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JOptionPane
							.showMessageDialog(
									getFrame(),
									NAME
											+ "\n" //$NON-NLS-1$
											+ "version "
											+ VERSION
											+ "\n\n"
											+ "Questo programma è stato ideato e realizzato\n"
											+ "da Pasquale Pizzuti (paspiz85@gmail.com).\n\n"
											+ "Copyright (c) 2007-2008 Pasquale Pizzuti",
									"Info", JOptionPane.INFORMATION_MESSAGE,
									new ImageIcon(getClass().getResource(
											"dama_n.png"))); //$NON-NLS-1$
				}
			});
		}
		return infoMenuItem;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getScacchieraPanel(), BorderLayout.CENTER);
			jContentPane.add(getStatusPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes menu
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getMenu() {
		if (menu == null) {
			menu = new JMenuBar();
			menu.add(getFileMenu());
			menu.add(getHelpMenu());
		}
		return menu;
	}

	public String getNickname() {
		return giocatore.getNickname();
	}

	/**
	 * This method initializes nicknameMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getNicknameMenuItem() {
		if (nicknameMenuItem == null) {
			nicknameMenuItem = new JMenuItem();
			nicknameMenuItem.setText("Cambia nickname");
			nicknameMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							String nickname = showInputDialog(getFrame(),
									"Inserisci il nuovo nickname",
									getNickname());
							if (nickname == null)
								return;
							setNickname(nickname);
						}
					});
		}
		return nicknameMenuItem;
	}

	/**
	 * This method initializes puntiMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPuntiMenuItem() {
		if (puntiMenuItem == null) {
			puntiMenuItem = new JMenuItem();
			puntiMenuItem.setText("Punteggio");
			puntiMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							String message = getNickname() + ": "; //$NON-NLS-1$
							message += getColore() == BIANCO ? getPezziBianchi()
									: getPezziNeri();
							message += "\n" + getRemoteNicknames()[0] + ": "; //$NON-NLS-1$ //$NON-NLS-2$
							message += getColore() == NERO ? getPezziBianchi()
									: getPezziNeri();
							JOptionPane.showMessageDialog(getFrame(), message,
									NAME, JOptionPane.INFORMATION_MESSAGE);
						}
					});
		}
		return puntiMenuItem;
	}

	/**
	 * This method initializes quitMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getQuitMenuItem() {
		if (quitMenuItem == null) {
			quitMenuItem = new JMenuItem();
			quitMenuItem.setText("Esci");
			quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getFrame().getWindowListeners()[0].windowClosing(null);
				}
			});
		}
		return quitMenuItem;
	}

	/**
	 * This method initializes scacchieraPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getScacchieraPanel() {
		if (scacchieraPanel == null) {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(8);
			gridLayout1.setColumns(8);
			scacchieraPanel = new JPanel();
			scacchieraPanel.setLayout(gridLayout1);
			if (getColore() == NERO)
				for (int i = 0; i < 8; i++)
					for (int j = 0; j < 8; j++)
						scacchieraPanel.add(getCasellaButton(i, j), null);
			else
				for (int i = 7; i >= 0; i--)
					for (int j = 7; j >= 0; j--)
						scacchieraPanel.add(getCasellaButton(i, j), null);
		}
		return scacchieraPanel;
	}

	private JLabel getStatusLabel() {
		if (statusLabel == null) {
			statusLabel = new JLabel();
			statusLabel.setText("Dama pronta");
		}
		return statusLabel;
	}

	/**
	 * This method initializes statusPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStatusPanel() {
		if (statusPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			statusPanel = new JPanel();
			statusPanel.setLayout(flowLayout);
			statusPanel.add(getStatusLabel(), null);
		}
		return statusPanel;
	}

	public boolean gioca(Mossa mossa) throws ScacchieraException, IOException {
		return giocatore.gioca(mossa);
	}

	public void mossaPerformed(Mossa mossa) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (getCasellaButton(i, j).getCasella().getColore() == NERO)
					getCasellaButton(i, j).setBackground(new Color(30, 30, 30));
				else
					getCasellaButton(i, j).setBackground(
							new Color(240, 240, 240));
				Pezzo pezzo = getCasella(i, j).getPezzo();
				if (pezzo == null)
					getCasellaButton(i, j).setIcon(null);
				else {
					if (pezzo instanceof DamaApp.Dama
							&& pezzo.getColore() == BIANCO)
						getCasellaButton(i, j).setIcon(
								new ImageIcon(getClass().getResource(
										"dama_b.png"))); //$NON-NLS-1$
					if (pezzo instanceof DamaApp.Dama
							&& pezzo.getColore() == NERO)
						getCasellaButton(i, j).setIcon(
								new ImageIcon(getClass().getResource(
										"dama_n.png"))); //$NON-NLS-1$
					if (pezzo instanceof DamaApp.Pedina
							&& pezzo.getColore() == BIANCO)
						getCasellaButton(i, j).setIcon(
								new ImageIcon(getClass().getResource(
										"pedina_b.png"))); //$NON-NLS-1$
					if (pezzo instanceof DamaApp.Pedina
							&& pezzo.getColore() == NERO)
						getCasellaButton(i, j).setIcon(
								new ImageIcon(getClass().getResource(
										"pedina_n.png"))); //$NON-NLS-1$
				}
			}
		if (mossa != null)
			getStatusLabel().setText("Ultima mossa: " + mossa);
		getScacchieraPanel().updateUI();
	}

	public void scacchieraClosed(Boolean winner) {
		if (winner == null)
			JOptionPane.showMessageDialog(getFrame(),
					"Il giocatore avversario ha abbandonato la partita", NAME,
					JOptionPane.INFORMATION_MESSAGE);
		else if (winner == getColore())
			JOptionPane.showMessageDialog(getFrame(),
					"Complimenti, hai vinto!", NAME,
					JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(getFrame(),
					"Mi dispiace, hai perso!", NAME,
					JOptionPane.INFORMATION_MESSAGE);
		getFrame().dispose();
	}

	public void setNickname(String nickname) {
		try {
			giocatore.setNickname(nickname);
			setNicknameTitle(nickname);
		} catch (IOException ex) {
			ex.printStackTrace();
			showErrorDialog(getFrame(), ex.getMessage());
		}
	}

	public void setNicknameTitle(String nickname) {
		getFrame().setTitle(NAME + " - " //$NON-NLS-1$
				+ nickname + " (" //$NON-NLS-1$
				+ (getColore() == BIANCO ? "bianco" : "nero") + ")"); //$NON-NLS-3$
	}

	public Scacchiera getGame() {
		return giocatore.getGame();
	}

	public void addListener(ScacchieraListener listener) {
		getGame().addListener(listener);
	}

	public ScacchieraListener[] getListeners() {
		return getGame().getListeners();
	}

	public int getPezziBianchi() {
		return getGame().getPezziBianchi();
	}

	public int getPezziNeri() {
		return getGame().getPezziNeri();
	}

	public Giocatore join(InetAddress address, int port) throws IOException {
		return new DamaApp().join(address, port);
	}

	public Giocatore open(int port) throws IOException {
		return new DamaApp().open(port);
	}

	public String[] getRemoteNicknames() {
		return getGame().getRemoteNicknames();
	}

}
