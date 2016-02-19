package pricetracker;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Class to extract the prices out of Html pages.
 * 
 * @author Florian Kiss
 * @version 0.1
 */
public class HtmlParser {

	private String url;
	private String priceIdentifier;

	HtmlParser(String url) {
		this.url = url;
		identifyUrl();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPriceIdentifier() {
		return priceIdentifier;
	}

	public void setPriceIdentifier(String priceIdentifier) {
		this.priceIdentifier = priceIdentifier;
	}

	public String getNewPrice() {
		String newPrice = "";
	
		try {
			Document doc = Jsoup.connect(url).get();
			Elements element = doc.select(priceIdentifier);
			newPrice = element.first().text().replace("EUR", "").trim();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Cannot conect to: " + url);
			e.printStackTrace();
		}
		return newPrice;
	}

	private void identifyUrl() {
		if (this.url.contains("whisky.de")) {
			// this.setPriceIdentifier("<div class=\"article-price\"
			// id=\"product-detail-price\">");
			// this.setPriceIdentifier("div.article-price #product-detail-price");
			this.setPriceIdentifier("#product-detail-price");
		} else {
			System.err.println(
					"Could not identify URL. Setting 'priceIdentifier' to empty String. You may run into trouble!");
			this.setPriceIdentifier("");
		}
	}
}
