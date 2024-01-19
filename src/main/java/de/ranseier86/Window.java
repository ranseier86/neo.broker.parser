package de.ranseier86;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.browser.Browser;

public class Window {

	protected Shell shell;
	private Browser browser;
	private Button btnTransactions;
	private Button btnScroll;
	private Text text;

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

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void writeToFile(ArrayList<String> transactions) {
	    try {
	        FileWriter myWriter = new FileWriter("C:\\Users\\Public\\scalable.csv");
	        
	        for (String transaction : transactions) {
	        	myWriter.write(transaction + "\n");
	        }
	        
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }			
	}	
	
	private void logic() {
		ArrayList<String> transactions = new ArrayList<String>();
		String transaction;
		boolean  firstLine = true;
		try {
			//File input = new File("C:\\Users\\Public\\scalable.htm");
			//Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			System.out.println(browser.getText());
			Document doc = Jsoup.parse(browser.getText());
			Element body = doc.body();
			//ueberschrift tage class = "jss392"
			//jss407
			
			Elements root = body.getElementsByClass("MuiGrid-root");
			for (Element single : root ) {
				transaction = "";
				for ( Attribute att : single.attributes().asList() ) {
					if (att.getKey().equals("aria-labelledby")) {
						transaction = transaction + att.getValue().substring(0, 19).split("T")[0] + ";";
						transaction = transaction + att.getValue().substring(0, 19).split("T")[1] + ";";
						
						Elements link = single.getElementsByAttributeValue("role", "link");
						for ( Element element : link) {
							transaction = transaction + element.text().replace(" Stk.", "").replace(" €", "") + ";";
						}	
						
						Elements listitem = single.getElementsByAttributeValue("role", "listitem");
						for ( Element element : listitem) {
							transaction = transaction + element.text().replace(" Stk.", "").replace(" €", "") + ";";
						}
						transactions.add(transaction);
						System.out.println(transaction);
					}

				}
				
				/*
				for (int i = 401; i <= 405; i++) {
					if (i != 403) {
						Elements div = single.getElementsByClass("jss" + i);
						for ( Element element : div) {
							//transaction = transaction + i + ":" + element.text();
							transaction = transaction + element.text().replace(" Stk.", "").replace(" €", "");
						}
						transaction = transaction  + ";";
					}
				}			
				*/

			}
			
			writeToFile(transactions);
			
			//System.out.println(jss394.size());
			//System.out.println(jss397.size());
			//System.out.println(jss398.size());
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
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
		try {
			Document doc = Jsoup.parse(browser.getText());
			Element body = doc.body();
			Elements root = body.getElementsByClass("MuiGrid-root");
			System.out.println(root.size());
			ArrayList<Integer> allEntries = new ArrayList<Integer>();
			for (int i = 0; i <= root.size()-1; i++) {
				for ( Attribute att : root.get(i).attributes().asList() ) {
					if (att.getKey().equals("aria-labelledby")) {
						allEntries.add(i);
					}
				}
			}
			System.out.println("found " + allEntries.size() + " entries");
			boolean first = true;
			for (int entry : allEntries) {
				browser.execute("child = document.getElementsByClassName('MuiGrid-root');");
				if (first) {
					first = false;
					browser.execute("firstPos = child[" + entry + "].getBoundingClientRect();");
					Thread.sleep(100);
				}
				browser.execute("nextPos = child[" + entry + "].getBoundingClientRect();");
				browser.execute("scrollPos = nextPos.top - firstPos.top;");
				browser.execute("window.scrollTo(0, scrollPos);");
				Thread.sleep(100);
				browser.execute("child[" + entry + "].style.backgroundColor = 'red';");
				Thread.sleep(1000);
				browser.execute("child[" + entry + "].click();");
				Thread.sleep(1000);
				browser.execute("end = document.getElementsByClassName('MuiButton-endIcon');");
				browser.execute("end[1].click();");
				Thread.sleep(1000);
				browser.execute("child[" + entry + "].style.backgroundColor = 'blue';");
				Thread.sleep(1000);
				browser.execute("window.scrollTo(0, 0);");
			}
				
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void click() {
		//browser.execute("var asdf = document.getElementsByClassName('MuiGrid-root'); asdf.click();");
		//browser.execute("var asdf = document.getElementsByAttributeValue('role', 'listitem'); asdf.click();");
		//browser.execute("asdf = document.getElementsByClassName('MuiGrid-root'); asdf[38].style.backgroundColor = 'red'; asdf[38].click();");
		browser.execute(text.getText());
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(669, 703);
		shell.setText("SWT Application");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logic();
			}
		});
		btnNewButton.setBounds(374, 629, 75, 25);
		btnNewButton.setText("export");
		
		browser = new Browser(shell, SWT.NONE);
		browser.setBounds(10, 10, 633, 586);
		System.out.println(browser.getBrowserType());
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("https://secure.scalable.capital/u/login");
			}
		});
		btnNewButton_1.setBounds(10, 629, 39, 25);
		btnNewButton_1.setText("login");
		
		btnTransactions = new Button(shell, SWT.NONE);
		btnTransactions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl("https://de.scalable.capital/broker/transactions");
			}
		});
		btnTransactions.setBounds(55, 629, 75, 25);
		btnTransactions.setText("transactions");
		
		btnScroll = new Button(shell, SWT.NONE);
		btnScroll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				scroll();
			}
		});
		btnScroll.setBounds(136, 629, 49, 25);
		btnScroll.setText("scroll");
		
		Button btnClieck = new Button(shell, SWT.NONE);
		btnClieck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				click();
			}
		});
		btnClieck.setBounds(251, 629, 39, 25);
		btnClieck.setText("click");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 602, 633, 21);
		
		Button btnEntries = new Button(shell, SWT.NONE);
		btnEntries.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				entries();
			}
		});
		btnEntries.setBounds(191, 629, 54, 25);
		btnEntries.setText("entries");
	}
}
