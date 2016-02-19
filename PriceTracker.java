package pricetracker;

import java.util.Date;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;

/**
 * Class to track the prices on specific web pages.
 * 
 * @author Florian Kiss
 * @version 0.1
 */
public class PriceTracker {

	private String name;
	private TreeMap<Date, String> timeSeries;
	private HtmlParser parser;

	PriceTracker(String trackerName, String url) {
		setName(trackerName);
		setParser(new HtmlParser(url));
		timeSeries = new TreeMap<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addPoint() {
		Date newDate = new Date(); //FIXME: do we need this?!
		// if (!timeSeries.isEmpty() && newDate.getDay() == timeSeries.lastKey().getDay()) {
		// 	timeSeries.remove(timeSeries.lastEntry());
		// }
		timeSeries.put(newDate, parser.getNewPrice());
	}

	@XmlElement
	public TreeMap<Date, String> getTimeSeries() {
		return this.timeSeries;
	}

	public HtmlParser getParser() {
		return parser;
	}

	public void setParser(HtmlParser parser) {
		this.parser = parser;
	}

}
