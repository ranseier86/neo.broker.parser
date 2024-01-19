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
import org.eclipse.wb.swt.SWTResourceManager;

public class Window {

	protected Shell shlScalablecapitalparserV;
	private Browser browser;
	private Button btnTransactions;
	private Button btnScroll;
	private Button btnJavaScript;
	private Button btnExport;
	private Button btnOpenExport;
	private Text txtJavascriptCodeHere;
	private Text textFile;
	private Text textAmount;
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
			setState(false);			
			int last = 0;
			while (last != browser.getText().length()) {
				last = browser.getText().length();
				browser.execute("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(1000);
			}
			browser.execute("window.scrollTo(0, 0);");
			setState(true);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		JOptionPane.showMessageDialog(null, "scroll done");
	}
	
	private void setState(boolean state) {
		textFile.setEnabled(state);
		txtJavascriptCodeHere.setEnabled(state);
		btnScroll.setEnabled(state);
		btnTransactions.setEnabled(state);
		btnExport.setEnabled(state);
		btnJavaScript.setEnabled(state);
		btnOpenExport.setEnabled(state);
	}
	
	private void entries() {
		if (!testFile()) {
			JOptionPane.showMessageDialog(null, "File not valid");
		}
		else {
			try {
				setState(false);
				Thread.sleep(100);
				
				ArrayList<Transaction> arrayTransactions = new ArrayList<Transaction>();
				
				//read the document
				Document doc = Jsoup.parse(browser.getText());
				Element body = doc.body();
				Elements root = body.getElementsByClass("MuiGrid-root");
				////////////////
				
				///// values from the overview list
				// also init of all transaction elements
				int amount = Integer.valueOf(textAmount.getText());
				System.out.println("getting " + amount + " transactions");
				for (int i = 0; i <= root.size() - 1; i++) {
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
					if (amount > 0) {
						System.out.println("size: " + arrayTransactions.size());
						if (arrayTransactions.size() >= amount) {
							break;
						}
					}
				}
				System.out.println("arrayTransactions: " + arrayTransactions.size() + " entries");
				System.out.println("--------------------------");			
				////////////////////////////////
				
				boolean first = true;
				for (Transaction entry : arrayTransactions) {
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
				setState(true);
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	private void click() {
		browser.execute(txtJavascriptCodeHere.getText());
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlScalablecapitalparserV = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
		shlScalablecapitalparserV.setBackground(SWTResourceManager.getColor(0, 0, 0));
		shlScalablecapitalparserV.setSize(1028, 866);
		shlScalablecapitalparserV.setText("scalable.capital.parser v0.1");
		
		browser = new Browser(shlScalablecapitalparserV, SWT.NONE);
		browser.setBounds(10, 10, 990, 632);
		System.out.println(browser.getBrowserType());
		
		btnTransactions = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnTransactions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("https://scalable.capital/broker/transactions");
			}
		});
		btnTransactions.setBounds(10, 707, 105, 25);
		btnTransactions.setText("transactions");
		
		btnScroll = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnScroll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				scroll();
			}
		});
		btnScroll.setBounds(121, 707, 90, 25);
		btnScroll.setText("scroll");
		
		btnJavaScript = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnJavaScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				click();
			}
		});
		btnJavaScript.setBounds(883, 707, 117, 25);
		btnJavaScript.setText("JavaScript");
		
		txtJavascriptCodeHere = new Text(shlScalablecapitalparserV, SWT.BORDER);
		txtJavascriptCodeHere.setText("JavaScript Code here (you do not need this)");
		txtJavascriptCodeHere.setBounds(10, 648, 990, 21);

		textFile = new Text(shlScalablecapitalparserV, SWT.BORDER);
		textFile.setText("C:\\Users\\Public\\scalable_export.csv");
		textFile.setBounds(62, 678, 938, 21);
		
		btnExport = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				entries();
			}
		});
		btnExport.setBounds(217, 707, 85, 25);
		btnExport.setText("export");
		
		btnOpenExport = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnOpenExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Runtime.getRuntime().exec("explorer.exe " + textFile.getText());					
				}
				catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		btnOpenExport.setBounds(308, 707, 85, 25);
		btnOpenExport.setText("open file");		
		
		Label lblNewLabel = new Label(shlScalablecapitalparserV, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(10, 681, 46, 20);
		lblNewLabel.setText("file");
		lblNewLabel.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lblNewLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
		
		Label lblDesc = new Label(shlScalablecapitalparserV, SWT.NONE);
		lblDesc.setAlignment(SWT.LEFT);
		lblDesc.setBounds(10, 740, 681, 75);
		lblDesc.setText(
				"'amount of transactions' defines amount of export (0 = all).\n" +
				"login. press 'transaction'. press 'scroll' to load all transactions (wait until done).\n" +
				"press 'export'. you get a 'done' message box when done.");
		lblDesc.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lblDesc.setForeground(SWTResourceManager.getColor(255, 0, 0));
		
		textAmount = new Text(shlScalablecapitalparserV, SWT.BORDER);
		textAmount.setText("0");
		textAmount.setBounds(625, 707, 55, 25);
		
		Label lblAmount = new Label(shlScalablecapitalparserV, SWT.NONE);
		lblAmount.setAlignment(SWT.LEFT);
		lblAmount.setBounds(465, 707, 160, 25);
		lblAmount.setText("amount of transactions");
		lblAmount.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lblAmount.setForeground(SWTResourceManager.getColor(255, 255, 255));		
	}
}
