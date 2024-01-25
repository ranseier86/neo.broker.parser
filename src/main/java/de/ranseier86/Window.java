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
	private Button btnTransactionsScalable;
	private Button btnTransactionsTradeRepublic;
	private Button btnScroll;
	private Button btnJavaScript;
	private Button btnExportScalable;
	private Button btnExportTradeRepublic;
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
			//-Dorg.eclipse.swt.browser.DefaultType=edge
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
		browser.setUrl("about:blank");
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
        			"Remark;" +
        			"Transaction Reference;" +
        			"ISIN;" +
        			"Security;" +
        			"List Security;" +
        			"Action;" +
        			"Trading Venue;" +        			
        			"List Date;" +
        			"List Year;" +
        			"List Month;" +
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
	        			transaction.getBemerkung() + ";" +
	        			transaction.getReferenz() + ";" +
	        			transaction.getIsin() + ";" +
	        			transaction.getWertpapiername() + ";" +
	        			transaction.getListeWertpapiername() + ";" +
	        			transaction.getAktionsart() + ";" +
	        			transaction.getHandelsplatz() + ";" +	        			
	        			transaction.getListeDatum() + ";" +
	        			transaction.getListeJahr() + ";" +
	        			transaction.getListeMonat() + ";" +
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
		textAmount.setEnabled(state);
		btnScroll.setEnabled(state);
		btnTransactionsScalable.setEnabled(state);
		btnTransactionsTradeRepublic.setEnabled(state);
		btnExportScalable.setEnabled(state);
		btnExportTradeRepublic.setEnabled(state);
		btnJavaScript.setEnabled(state);
		btnOpenExport.setEnabled(state);
	}
	
	private String removeSomething(String input) {
		return input.replace("+", "").replace("�", "").replace(",", "").replace(".", ",").trim();
	}
	
	private void entriesTraderepublic() {
		boolean goOn = true;
		if (!checkProfileLanguage("traderepublic")) {
			JOptionPane.showMessageDialog(null, "Language not english");
			goOn = false;
		}
		if (!testFile()) {
			JOptionPane.showMessageDialog(null, "File not valid");
			goOn = false;
		}
		
		if (goOn) {
			try {
				setState(false);
				Thread.sleep(100);
				
				ArrayList<Transaction> arrayTransactions = new ArrayList<Transaction>();
				
				//read the document
				Document doc = Jsoup.parse(browser.getText());
				Element body = doc.body();
				Elements root = body.getElementsByClass("timelineEventAction");
				////////////////
				
				///// values from the overview list
				// also init of all transaction elements
				int amount = Integer.valueOf(textAmount.getText());
				System.out.println("getting " + amount + " transactions");
				for (int i = 0; i <= root.size() - 1; i++) {
					Transaction transaction = new Transaction();
					transaction.setPageindex(i);
					transaction.setListeWertpapiername(root.get(i).getElementsByClass("timelineEvent__title").attr("title"));
					transaction.setWertpapiername(root.get(i).getElementsByClass("timelineEvent__title").text());
					String subtitleText = root.get(i).getElementsByClass("timelineEvent__subtitle").text();
					String Tag = subtitleText.split("/")[0];
					String Monat = subtitleText.split("/")[1].substring(0, 2);
					String Datum = Tag + "." + Monat + ".";
					transaction.setListeDatum("traderepublic", Datum);
					transaction.setGesamtbetrag(removeSomething( root.get(i).getElementsByClass("timelineEvent__price").text() ));
					arrayTransactions.add(transaction);
					if (amount > 0) {
						if (arrayTransactions.size() >= amount) {
							break;
						}
					}
				}
				System.out.println("arrayTransactions: " + arrayTransactions.size() + " entries");
				System.out.println("--------------------------");			
				////////////////////////////////				
				
				boolean first = true;
				int counter = 1;
				for (Transaction entry : arrayTransactions) {
					System.out.println(counter);
					counter++;
					Thread.sleep(200);
					browser.execute("window.scrollTo(0, 0);");
					Thread.sleep(200);
					browser.execute("child = document.getElementsByClassName('timelineEventAction');");
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
					
					Thread.sleep(1000);
					
					//after click on transaction the overlay page opens and fresh information is given
					//that is why the document gets read again
					Document listEntryDoc = Jsoup.parse(browser.getText());
					Element listEntryBody = listEntryDoc.body();
					Elements listRoot = listEntryBody.getElementsByClass("sideModalLayout__content");
					////////////////////////////////
					for (Element elem : listRoot.get(0).getElementsByClass("timelineDetail__section")) {
						String key = elem.getElementsByClass("detailHeader__heading").text();
						String value = elem.getElementsByClass("detailHeader__subheading -time").text();
						//System.out.println("key: " + key + "   value: " + value);
						if (key.length() > 0) {
							entry.setBemerkung(key);
							if (key.contains("You added")) {
								entry.setAktionsart("Added");
								if (entry.getWertpapiername().equals("Cash in")) {
									entry.setAktionsart(entry.getWertpapiername());
								}
							}
							else if (key.contains("You received")) {
								entry.setAktionsart("Received");
								if (entry.getWertpapiername().equals("Interest")) {
									entry.setAktionsart(entry.getWertpapiername());
								}
							}														
						}
						if (value.length() > 0) {
							switch (value.split(" ")[0]) {
								case "Jan":
									entry.setListeMonat("01");
									break;
								case "Dec":
									entry.setListeMonat("12");
									break;									
							}
							entry.setListeJahr("20" + value.split(" ")[1]);
							entry.setListeDatum("traderepublic", entry.getListeDatum() + entry.getListeJahr());
							entry.setListeUhrzeit(value.split(" ")[2]);
						}
					}
					
					for (Element elem : listRoot.get(0).getElementsByClass("detailTable__row")) {
						String key = elem.getElementsByClass("detailTable__label").text();
						String value = elem.getElementsByClass("detailTable__value").text();
						//System.out.println("key: " + key + "   value: " + value);
						//System.out.println(entry);
						switch (key) {
							case "Order Type":
								entry.setAktionsart(value);
								break;
							case "Fee":
								entry.setOrdergebuehr(removeSomething(value));
								break;
							case "Shares":
								entry.setAusfuehrungsstueckzahl(removeSomething(value));
								break;
							case "Share price":
								entry.setAusfuehrungspreis(removeSomething(value));
								break;
							case "Total":
								entry.setGesamtbetrag(removeSomething(value));
								break;									
						}
					}
					
					browser.execute("end = document.getElementsByClassName('closeButton sideModal__close');");
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
	
	private void entriesScalable() {
		boolean goOn = true;
		if (!checkProfileLanguage("scalable")) {
			JOptionPane.showMessageDialog(null, "Language not english");
			goOn = false;
		}
		if (!testFile()) {
			JOptionPane.showMessageDialog(null, "File not valid");
			goOn = false;
		}
		
		if (goOn) {
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
							transaction.setListeDatum("scalable", att.getValue().substring(0, 19).split("T")[0]);
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
								entry.setGesamtbetrag( single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
	///////////////////////////////////////////////////////////	
							case "value-Execution price":
								entry.setAusfuehrungspreis(single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Executed quantity":
								entry.setAusfuehrungsstueckzahl(removeSomething( single.text() ));
								break;
							case "value-Limit price":
								entry.setLimitpreis( single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Trading venue":
								entry.setHandelsplatz(single.text());
								break;
							case "value-Transaction reference":
								entry.setReferenz(single.text());
								break;
							case "value-Order fee":
								entry.setOrdergebuehr(single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Market valuation":
								entry.setKurswert( single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
	///////////////////////////////////////////////////////////							
							case "value-Ausf�hrungspreis":
								entry.setAusfuehrungspreis(single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Ausgef�hrte St�ckzahl":
								entry.setAusfuehrungsstueckzahl(single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Limitpreis":
								entry.setLimitpreis( single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Handelsplatz":
								entry.setHandelsplatz(single.text());
								break;
							case "value-Referenz der Transaktion":
								entry.setReferenz(single.text());
								break;
							case "value-Ordergeb�hr":
								entry.setOrdergebuehr(single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
								break;
							case "value-Kurswert":
								entry.setKurswert( single.text().replace("�", "").replace(",", "").replace(".", ",").trim() );
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
	
	private boolean checkProfileLanguage(String broker) {
		//read the document
		Document doc = Jsoup.parse(browser.getText());
		Element body = doc.body();
		////////////////
		if (broker.equals("traderepublic")) {
			if (body.getElementsByClass("cashBalance__title").text().equals("Cash")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (broker.equals("scalable")) {
			if (body.getElementsByClass("MuiListItemText-primary").text().split(" ")[0].equals("Home")) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlScalablecapitalparserV = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
		shlScalablecapitalparserV.setBackground(SWTResourceManager.getColor(0, 0, 0));
		shlScalablecapitalparserV.setSize(1028, 970);
		shlScalablecapitalparserV.setText("neo.broker.parser v0.3");
		
		browser = new Browser(shlScalablecapitalparserV, SWT.NONE);
		browser.setBounds(10, 10, 990, 698);
		shlScalablecapitalparserV.setText(shlScalablecapitalparserV.getText() + " mode: " + browser.getBrowserType());
		
		
		if (browser.getBrowserType() == "ie") {
			JOptionPane.showMessageDialog(null, "mode is 'ie'. this tool will not work. did you run with correct vm arguments?");
		}
		else if (browser.getBrowserType() != "edge") {
			JOptionPane.showMessageDialog(null, "mode is not 'edge'. this tool might not work");
		}
		
		btnTransactionsScalable = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnTransactionsScalable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("https://scalable.capital/broker/transactions");
			}
		});
		btnTransactionsScalable.setBounds(10, 807, 105, 25);
		btnTransactionsScalable.setText("transactions S");
		
		btnTransactionsTradeRepublic = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnTransactionsTradeRepublic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("https://app.traderepublic.com/profile/transactions");
			}
		});
		btnTransactionsTradeRepublic.setBounds(10, 777, 105, 25);
		btnTransactionsTradeRepublic.setText("transactions TR");
		
		btnScroll = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnScroll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				scroll();
			}
		});
		btnScroll.setBounds(121, 807, 90, 25);
		btnScroll.setText("scroll");
		
		btnJavaScript = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnJavaScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				click();
			}
		});
		btnJavaScript.setBounds(883, 807, 117, 25);
		btnJavaScript.setText("JavaScript");
		
		txtJavascriptCodeHere = new Text(shlScalablecapitalparserV, SWT.BORDER);
		txtJavascriptCodeHere.setText("JavaScript Code here (you do not need this)");
		txtJavascriptCodeHere.setBounds(10, 714, 990, 21);

		textFile = new Text(shlScalablecapitalparserV, SWT.BORDER);
		textFile.setText("C:\\Users\\Public\\scalable_export.csv");
		textFile.setBounds(62, 744, 938, 21);
		
		btnExportScalable = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnExportScalable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				entriesScalable();
			}
		});
		btnExportScalable.setBounds(217, 807, 85, 25);
		btnExportScalable.setText("export S");
		
		btnExportTradeRepublic = new Button(shlScalablecapitalparserV, SWT.NONE);
		btnExportTradeRepublic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				entriesTraderepublic();
			}
		});
		btnExportTradeRepublic.setBounds(217, 777, 85, 25);
		btnExportTradeRepublic.setText("export TR");		
		
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
		btnOpenExport.setBounds(308, 807, 85, 25);
		btnOpenExport.setText("open file");		
		
		Label lblNewLabel = new Label(shlScalablecapitalparserV, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(10, 745, 46, 20);
		lblNewLabel.setText("file");
		lblNewLabel.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lblNewLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
		
		Label lblDesc = new Label(shlScalablecapitalparserV, SWT.NONE);
		lblDesc.setAlignment(SWT.LEFT);
		lblDesc.setBounds(10, 838, 990, 75);
		lblDesc.setText(
				"'amount of transactions' defines amount of export (0 = all).\n" +
				"login. press 'transaction'. press 'scroll' to load all transactions (wait until done).\n" +
				"press 'export'. you get a 'done' message box when done.");
		lblDesc.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lblDesc.setForeground(SWTResourceManager.getColor(255, 0, 0));
		
		textAmount = new Text(shlScalablecapitalparserV, SWT.BORDER);
		textAmount.setText("0");
		textAmount.setBounds(625, 807, 55, 25);
		
		Label lblAmount = new Label(shlScalablecapitalparserV, SWT.NONE);
		lblAmount.setAlignment(SWT.LEFT);
		lblAmount.setBounds(465, 807, 160, 25);
		lblAmount.setText("amount of transactions");
		lblAmount.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lblAmount.setForeground(SWTResourceManager.getColor(255, 255, 255));		
	}
}
