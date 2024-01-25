package de.ranseier86;

public class Transaction {
	private int pageindex;
	private String wertpapiername;
	private String gesamtbetrag;
	private String ausfuehrungspreis;
	private String ausfuehrungsstueckzahl;
	private String kurswert;
	private String ordergebuehr;
	private String limitpreis;
	private String referenz;
	private String handelsplatz;
	private String listeWertpapiername;
	private String listeDatum;
	private String listeJahr;
	private String listeMonat;
	private String listeUhrzeit;
	private String aktionsart;
	private String isin;
	private String bemerkung;
	
	public Transaction () {
		this.pageindex = 0;
		this.wertpapiername = "";
		this.gesamtbetrag = "";
		this.ausfuehrungspreis = "";
		this.ausfuehrungsstueckzahl = "";
		this.kurswert = "";
		this.ordergebuehr = "";
		this.limitpreis = "";
		this.referenz = "";
		this.handelsplatz = "";
		this.listeWertpapiername = "";
		this.listeDatum = "";
		this.listeJahr = "";
		this.listeMonat = "";
		this.listeUhrzeit = "";
		this.aktionsart = "";
		this.isin = "";
		this.bemerkung = "";
	}

	public String toString() {
		return String.format(
				"#############################\n"
				+ "Transaction:\n"
				+ " pageindex: %s\n"
				+ " wertpapiername: %s\n"
				+ " gesamtbetrag: %s\n"
				+ " ausfuehrungspreis: %s\n"
				+ " ausfuehrungsstueckzahl: %s\n"
				+ " kurswert: %s\n"
				+ " ordergebuehr: %s\n"
				+ " limitpreis: %s\n"
				+ " referenz: %s\n"
				+ " handelsplatz: %s\n"
				+ " listeWertpapiername: %s\n"
				+ " listeDatum: %s (Jahr: %s / Monat: %s)\n"
				+ " listeUhrzeit: %s\n"
				+ " aktionsart: %s\n"
				+ " isin: %s\n"
				+ " bemerkung: %s\n"
				+ "#############################\n"
				+ "", this.pageindex,
				this.wertpapiername, 
				this.gesamtbetrag,
				this.ausfuehrungspreis,
				this.ausfuehrungsstueckzahl,
				this.kurswert,
				this.ordergebuehr,
				this.limitpreis,
				this.referenz,
				this.handelsplatz,
				this.listeWertpapiername,
				this.listeDatum,
				this.listeJahr,
				this.listeMonat,
				this.listeUhrzeit,
				this.aktionsart,
				this.isin,
				this.bemerkung
				);
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public String getListeJahr() {
		return listeJahr;
	}

	public void setListeJahr(String listeJahr) {
		this.listeJahr = listeJahr;
	}

	public String getListeMonat() {
		return listeMonat;
	}

	public void setListeMonat(String listeMonat) {
		this.listeMonat = listeMonat;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getListeWertpapiername() {
		return listeWertpapiername;
	}

	public void setListeWertpapiername(String listeWertpapiername) {
		this.listeWertpapiername = listeWertpapiername;
	}

	public int getPageindex() {
		return pageindex;
	}

	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}

	public String getWertpapiername() {
		return wertpapiername;
	}

	public void setWertpapiername(String wertpapiername) {
		this.wertpapiername = wertpapiername;
	}

	public String getGesamtbetrag() {
		return gesamtbetrag;
	}

	public void setGesamtbetrag(String gesamtbetrag) {
		this.gesamtbetrag = gesamtbetrag;
	}

	public String getAusfuehrungspreis() {
		return ausfuehrungspreis;
	}

	public void setAusfuehrungspreis(String ausfuehrungspreis) {
		this.ausfuehrungspreis = ausfuehrungspreis;
	}

	public String getAusfuehrungsstueckzahl() {
		return ausfuehrungsstueckzahl;
	}

	public void setAusfuehrungsstueckzahl(String ausfuehrungsstueckzahl) {
		this.ausfuehrungsstueckzahl = ausfuehrungsstueckzahl;
	}

	public String getKurswert() {
		return kurswert;
	}

	public void setKurswert(String kurswert) {
		this.kurswert = kurswert;
	}

	public String getOrdergebuehr() {
		return ordergebuehr;
	}

	public void setOrdergebuehr(String ordergebuehr) {
		this.ordergebuehr = ordergebuehr;
	}

	public String getLimitpreis() {
		return limitpreis;
	}

	public void setLimitpreis(String limitpreis) {
		this.limitpreis = limitpreis;
	}

	public String getReferenz() {
		return referenz;
	}

	public void setReferenz(String referenz) {
		this.referenz = referenz;
	}

	public String getHandelsplatz() {
		return handelsplatz;
	}

	public void setHandelsplatz(String handelsplatz) {
		this.handelsplatz = handelsplatz;
	}

	public String getListeDatum() {
		return listeDatum;
	}

	public void setListeDatum(String broker, String listeDatum) {
		if (broker.equals("scaleable")) {
			this.listeDatum = listeDatum;
			this.listeJahr = listeDatum.substring(0,4);
			this.listeMonat = listeDatum.substring(5,7);			
		}
		else if (broker.equals("traderepublic")) {
			this.listeDatum = listeDatum;
		}
		else {
			this.listeDatum = "no broker set";
		}

	}

	public String getListeUhrzeit() {
		return listeUhrzeit;
	}

	public void setListeUhrzeit(String listeUhrzeit) {
		this.listeUhrzeit = listeUhrzeit;
	}

	public String getAktionsart() {
		return aktionsart;
	}

	public void setAktionsart(String aktionsart) {
		this.aktionsart = aktionsart;
	}
	

}
