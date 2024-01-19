package de.ranseier86;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;

public class Window {

	protected Shell shlScalablecapitalparserV;
	private Browser browser;
	private Button btnTransactions;
	private Button btnScroll;
	private Button btnJavaScript;
	private Button btnLogin;
	private Button btnExport;
	private Text text;
	private Text textFile;
	private Display display;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Window window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void centerWindow() {
		Monitor mon = Display.getDefault().getMonitors()[0];
		int centerWidth = (mon.getBounds().width / 2) - (shlScalablecapitalparserV.getSize().x / 2);
		int centerHeight = (mon.getBounds().height / 2) - (shlScalablecapitalparserV.getSize().y / 2);
		shlScalablecapitalparserV.setLocation(centerWidth, centerHeight);
	}	

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlScalablecapitalparserV.open();
		shlScalablecapitalparserV.layout();
		centerWindow();
		browser.setUrl("https://scalable.capital/broker/transactions");
		while (!shlScalablecapitalparserV.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private boolean testFile() {
	    try {
	        FileWriter myWriter = new FileWriter(textFile.getText());
        	myWriter.write(
        			"Pageindex;" +
        			"Transaction Reference;" +
        			"ISIN;" +
        			"Security;" +
        			"List Security;" +
        			"Action;" +
        			"Trading Venue;" +        			
        			"List Date;" +
        			"List Time;" +        			
        			"Total Amount;" +
        			"Execution Price;" +
        			"Limit Price;" +        			
        			"Execution Quantity;" +
        			"Market Valuation;" +
        			"Order Fee;" +
        			"\n"
        			);
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	        return true;
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	        return false;
	      }
	}
	
	public void writeToFile(ArrayList<Transaction> transactions) {
	    try {
	        FileWriter myWriter = new FileWriter(textFile.getText());
        	myWriter.write(
        			"Pageindex;" +
        			"Transaction Reference;" +
        			"ISIN;" +
        			"Security;" +
        			"List Security;" +
        			"Action;" +
        			"Trading Venue;" +        			
        			"List Date;" +
        			"List Time;" +        			
        			"Total Amount;" +
        			"Execution Price;" +
        			"Limit Price;" +        			
        			"Execution Quantity;" +
        			"Market Valuation;" +
        			"Order Fee;" +
        			"\n"
        			);	        
	        for (Transaction transaction : transactions) {
	        	myWriter.write(
	        			transaction.getPageindex() + ";" +
	        			transaction.getReferenz() + ";" +
	        			transaction.getIsin() + ";" +
	        			transaction.getWertpapiername() + ";" +
	        			transaction.getListeWertpapiername() + ";" +
	        			transaction.getAktionsart() + ";" +
	        			transaction.getHandelsplatz() + ";" +	        			
	        			transaction.getListeDatum() + ";" +
	        			transaction.getListeUhrzeit() + ";" +	        			
	        			transaction.getGesamtbetrag() + ";" +
	        			transaction.getAusfuehrungspreis() + ";" +
	        			transaction.getLimitpreis() + ";" +	        			
	        			transaction.getAusfuehrungsstueckzahl() + ";" +
	        			transaction.getKurswert() + ";" +
	        			transaction.getOrdergebuehr() + ";" +
	        			"\n"
	        			);
	        }
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }			
	}	
	
	private void scroll() {
		//browser.execute("last = 0; while (true) { alert(document.body.scrollHeight); last = document.body.scrollHeight; window.scrollTo(0, document.body.scrollHeight); alert(document.body.scrollHeight); if ( document.body.scrollHeight == last ) { alert('end'); break; } } ");
		try {
			int last = 0;
			while (last != browser.getText().length()) {
				last = browser.getText().length();
				browser.execute("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(1000);
			}
			browser.execute("window.scrollTo(0, 0);");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("done");
	}
	
	private void entries() {
		if (!testFile()) {
			JOptionPane.showMessageDialog(null, "File not valid");
		}
		else {
			try {
				textFile.setEnabled(false);
				text.setEnabled(false);
				btnLogin.setEnabled(false);
				btnScroll.setEnabled(false);
				btnTransactions.setEnabled(false);
				btnExport.setEnabled(false);
				btnJavaScript.setEnabled(false);
				Thread.sleep(100);
				
				ArrayList<Transaction> arrayTransactions = new ArrayList<Transaction>();
				
				//read the document
				Document doc = Jsoup.parse(browser.getText());
				Element body = doc.body();
				Elements root = body.getElementsByClass("MuiGrid-root");
				////////////////
				
				///// values from the overview list
				// also init of all transaction elements
				for (int i = 0; i <= root.size()-1; i++) {
					for ( Attribute att : root.get(i).attributes().asList() ) {
						if (att.getKey().equals("aria-labelledby")) {
							Transaction transaction = new Transaction();
							transaction.setPageindex(i);
							if (root.get(i).getElementsByAttributeValue("role", "link").size() == 1) {
								transaction.setListeWertpapiername(root.get(i).getElementsByAttributeValue("role", "link").get(0).text());
							}
							else {
								transaction.setListeWertpapiername(root.get(i).getElementsByAttributeValue("role", "listitem").get(1).text());
							}
							transaction.setListeDatum(att.getValue().substring(0, 19).split("T")[0]);
							transaction.setListeUhrzeit(att.getValue().substring(0, 19).split("T")[1]);
							transaction.setAktionsart(root.get(i).getElementsByAttributeValue("role", "listitem").get(0).text());
							arrayTransactions.add(transaction);
							break;
						}
					}
				}
				System.out.println("arrayTransactions: " + arrayTransactions.size() + " entries");
				System.out.println("--------------------------");			
				////////////////////////////////
				
				boolean first = true;
				int stoptest = 0;
				for (Transaction entry : arrayTransactions) {
					if (stoptest >= 1) {                           /////////STOPTEST
						break;
					}
					//stoptest++;
					Thread.sleep(200);
					browser.execute("window.scrollTo(0, 0);");
					Thread.sleep(200);
					browser.execute("child = document.getElementsByClassName('MuiGrid-root');");
					if (first) {
						first = false;
						browser.execute("firstPos = child[" + entry.getPageindex() + "].getBoundingClientRect();");
						Thread.sleep(100);
					}
					browser.execute("nextPos = child[" + entry.getPageindex() + "].getBoundingClientRect();");
					browser.execute("scrollPos = nextPos.top - firstPos.top;");
					browser.execute("window.scrollTo(0, scrollPos);");
					Thread.sleep(200);
					browser.execute("child[" + entry.getPageindex() + "].style.backgroundColor = '#824110';");
					Thread.sleep(200);
					browser.execute("child[" + entry.getPageindex() + "].click();");
					
					Thread.sleep(2000);
					
					//after click on transaction the overlay page opens and fresh information is given
					//that is why the document gets read again
					Document listEntryDoc = Jsoup.parse(browser.getText());
					Element listEntryBody = listEntryDoc.body();
					Elements listRoot = listEntryBody.getElementsByClass("MuiDialog-paperFullScreen");
					////////////////////////////////
					
					//System.out.println("MuiDialog-paperFullScreen: " + listRoot.size());
					/*
					System.out.println("---------");
					for (Element single : listRoot.parents().get(0).getElementsByClass("MuiGrid-root") ) {
						for (Element inner : single.getElementsByClass("MuiGrid-item") ) {
							System.out.println("inner: " + inner.text());
						}
					}
					System.out.println("------------------------------");
					*/
					for (Element single : listRoot.parents().get(0).getElementsByAttribute("href") ) {
						entry.setWertpapiername(single.text());
						entry.setIsin(single.attr("href").replace("/broker/security?isin=", ""));
					}
					for (Element single : listRoot.parents().get(0).getElementsByAttribute("data-testid") ) {
						switch (single.attr("data-testid")) {
							case "total-amount":
								entry.setGesamtbetrag( single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
	///////////////////////////////////////////////////////////	
							case "value-Execution price":
								entry.setAusfuehrungspreis(single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Executed quantity":
								entry.setAusfuehrungsstueckzahl(single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Limit price":
								entry.setLimitpreis( single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Trading venue":
								entry.setHandelsplatz(single.text());
								break;
							case "value-Transaction reference":
								entry.setReferenz(single.text());
								break;
							case "value-Order fee":
								entry.setOrdergebuehr(single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Market valuation":
								entry.setKurswert( single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
	///////////////////////////////////////////////////////////							
							case "value-Ausführungspreis":
								entry.setAusfuehrungspreis(single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Ausgeführte Stückzahl":
								entry.setAusfuehrungsstueckzahl(single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Limitpreis":
								entry.setLimitpreis( single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Handelsplatz":
								entry.setHandelsplatz(single.text());
								break;
							case "value-Referenz der Transaktion":
								entry.setReferenz(single.text());
								break;
							case "value-Ordergebühr":
								entry.setOrdergebuehr(single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Kurswert":
								entry.setKurswert( single.text().replace("€", "").replace(",", "").replace(".", ",").trim() );
								break;
						}
					}
					System.out.println(entry);				
					browser.execute("end = document.getElementsByClassName('MuiButton-endIcon');");
					browser.execute("end[1].click();");
					Thread.sleep(200);
					browser.execute("child[" + entry.getPageindex() + "].style.backgroundColor = '#092904';");
				}
				writeToFile(arrayTransactions);
				JOptionPane.showMessageDialog(null, "done");
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	private void click() {
		browser.execute(text.getText());
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlScalablecapitalparserV = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
		shlScalablecapitalparserV.setSize(700, 800);
		shlScalablecapitalparserV.setText("scalable.capital.parser v0.1");
		
		browser = new Browser(shlScalablecapitalparserV, SWT.NONE);
		browser.setBounds(10, 10, 662, 632);
		System.out.println(browser.getBrowserType());
		
		btnLogin = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("https://secure.scalable.capital/u/login");
			}
		});
		btnLogin.setBounds(10, 707, 70, 25);
		btnLogin.setText("login");
		
		btnTransactions = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnTransactions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("https://scalable.capital/broker/transactions");
			}
		});
		btnTransactions.setBounds(86, 707, 105, 25);
		btnTransactions.setText("transactions");
		
		btnScroll = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnScroll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				scroll();
			}
		});
		btnScroll.setBounds(197, 707, 90, 25);
		btnScroll.setText("scroll");
		
		btnJavaScript = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnJavaScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				click();
			}
		});
		btnJavaScript.setBounds(555, 707, 117, 25);
		btnJavaScript.setText("JavaScript");
		
		text = new Text(shlScalablecapitalparserV, SWT.BORDER);
		text.setBounds(10, 648, 662, 21);

		textFile = new Text(shlScalablecapitalparserV, SWT.BORDER);
		textFile.setText("C:\\Users\\Public\\scalable_export.csv");
		textFile.setBounds(62, 678, 610, 21);
		
		btnExport = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				entries();
			}
		});
		btnExport.setBounds(293, 707, 85, 25);
		btnExport.setText("export");
		
		Label lblNewLabel = new Label(shlScalablecapitalparserV, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(10, 681, 46, 20);
		lblNewLabel.setText("file");		
	}
}
