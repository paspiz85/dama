package org.altervista.paspiz85.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import java.awt.Toolkit;

/**
 * Finestra per la visualizzazione di guide in linea in formato HTML.
 * 
 * @author Pasquale Pizzuti
 * @version 18/dic/07
 * 
 */
public class HelpBrowser {

	private String current = null;

	private JFrame frame = null;

	private String home; // @jve:decl-index=0:

	private JLabel homeLabel = null;

	private JPanel jContentPane = null;

	private JEditorPane jEditorPane = null;

	private JScrollPane jScrollPane = null;

	private Stack<String> next;

	private JLabel nextLabel = null;

	private Stack<String> prev;

	private JLabel prevLabel = null;

	private JLabel statusLabel = null;

	private JPanel statusPanel = null;

	private JPanel toolPanel = null;

	/**
	 * Costruttore per oggetti della classe HelpBrowser.
	 * 
	 * @param home
	 *            prima pagina da visualizzare.
	 * 
	 */
	public HelpBrowser(String home) {
		setHome(home);
		prev = new Stack<String>();
		next = new Stack<String>();
	}

	/**
	 * This method initializes frame
	 * 
	 * @return javax.swing.JFrame
	 */
	public JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame();
			frame.setTitle("Guida in linea"); //$NON-NLS-1$
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
					getClass().getResource("Help.png"))); //$NON-NLS-1$
			frame.setContentPane(getJContentPane());
			frame.pack();
			frame.setLocationRelativeTo(null);
		}
		return frame;
	}

	/**
	 * Restituisce l'URL della prima pagina da visualizzare.
	 * 
	 * @return la prima pagina da visualizzare.
	 * 
	 */
	public String getHome() {
		return home;
	}

	private JLabel getHomeLabel() {
		if (homeLabel == null) {
			homeLabel = new JLabel();
			homeLabel
					.setIcon(new ImageIcon(getClass().getResource("home.png"))); //$NON-NLS-1$
			homeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (current.equals(home))
						return;
					String p = current;
					if (setPage(home)) {
						prev.push(p);
						if (prev.size() > 10)
							prev.remove(0);
						next.removeAllElements();
						if (prev.size() == 0)
							getPrevLabel().setEnabled(false);
						else
							getPrevLabel().setEnabled(true);
						if (next.size() == 0)
							getNextLabel().setEnabled(false);
						else
							getNextLabel().setEnabled(true);
					}
				}
			});
		}
		return homeLabel;
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
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
			jContentPane.add(getStatusPanel(), BorderLayout.SOUTH);
			jContentPane.add(getToolPanel(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jEditorPane
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JEditorPane getJEditorPane() {
		if (jEditorPane == null) {
			jEditorPane = new JEditorPane();
			jEditorPane.setEditable(false);
			jEditorPane
					.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
						public void hyperlinkUpdate(
								javax.swing.event.HyperlinkEvent e) {
							if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
								String url = current.substring(0, current
										.lastIndexOf("/") + 1); //$NON-NLS-1$
								url += e.getDescription();
								String p = current;
								if (setPage(url)) {
									prev.push(p);
									if (prev.size() > 10)
										prev.remove(0);
									next.removeAllElements();
									if (prev.size() == 0)
										getPrevLabel().setEnabled(false);
									else
										getPrevLabel().setEnabled(true);
									if (next.size() == 0)
										getNextLabel().setEnabled(false);
									else
										getNextLabel().setEnabled(true);
								}
							}
						}
					});
			setPage(home);
		}
		return jEditorPane;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJEditorPane());
		}
		return jScrollPane;
	}

	private JLabel getNextLabel() {
		if (nextLabel == null) {
			nextLabel = new JLabel();
			nextLabel.setIcon(new ImageIcon(getClass().getResource(
					"right_symbol.png"))); //$NON-NLS-1$
			nextLabel.setEnabled(false);
			nextLabel.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (next.size() == 0)
						return;
					prev.push(current);
					if (prev.size() > 10)
						prev.remove(0);
					setPage(next.pop());
					if (prev.size() == 0)
						getPrevLabel().setEnabled(false);
					else
						getPrevLabel().setEnabled(true);
					if (next.size() == 0)
						getNextLabel().setEnabled(false);
					else
						getNextLabel().setEnabled(true);
				}
			});
		}
		return nextLabel;
	}

	private JLabel getPrevLabel() {
		if (prevLabel == null) {
			prevLabel = new JLabel();
			prevLabel.setIcon(new ImageIcon(getClass().getResource(
					"left_symbol.png"))); //$NON-NLS-1$
			prevLabel.setEnabled(false);
			prevLabel.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (prev.size() == 0)
						return;
					next.push(current);
					if (next.size() > 10)
						next.remove(0);
					setPage(prev.pop());
					if (prev.size() == 0)
						getPrevLabel().setEnabled(false);
					else
						getPrevLabel().setEnabled(true);
					if (next.size() == 0)
						getNextLabel().setEnabled(false);
					else
						getNextLabel().setEnabled(true);
				}
			});
		}
		return prevLabel;
	}

	/**
	 * This method initializes statusPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStatusPanel() {
		if (statusPanel == null) {
			statusLabel = new JLabel();
			statusLabel.setText(""); //$NON-NLS-1$
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			statusPanel = new JPanel();
			statusPanel.setLayout(flowLayout);
			statusPanel.add(statusLabel, null);
		}
		return statusPanel;
	}

	/**
	 * This method initializes toolPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getToolPanel() {
		if (toolPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(FlowLayout.LEFT);
			toolPanel = new JPanel();
			toolPanel.setLayout(flowLayout1);
			toolPanel.add(getHomeLabel(), null);
			toolPanel.add(getPrevLabel(), null);
			toolPanel.add(getNextLabel(), null);
		}
		return toolPanel;
	}

	/**
	 * Imposta l'URL della prima pagina da visualizzare.
	 * 
	 * @param home
	 *            prima pagina da visualizzare.
	 * 
	 */
	public void setHome(String home) {
		this.home = home;
	}

	private boolean setPage(String url) {
		getStatusPanel();
		try {
			getJEditorPane().setPage(getClass().getResource(url));
			current = url;
			statusLabel.setForeground(new Color(51, 51, 51));
			statusLabel.setText("Indirizzo corrente: " + current);
			return true;
		} catch (Exception ex) {
			statusLabel.setForeground(Color.RED);
			statusLabel.setText("Impossibile visualizzare la pagina: " + url);
			ex.printStackTrace();
			return false;
		}
	}

}
