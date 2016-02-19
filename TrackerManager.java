package pricetracker;

import java.util.Set;
import java.util.TreeMap;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to handle the various PriceTracker instances.
 * 
 * @author Florian Kiss
 * @version 0.1
 */

@XmlRootElement
public class TrackerManager {

	private TreeMap<String, PriceTracker> priceTrackers;

	TrackerManager() {
		priceTrackers = new TreeMap<>();
	}

	public void addTracker(String trackerName, String parserUrl) {
		this.priceTrackers.put(trackerName, new PriceTracker(trackerName, parserUrl));
	}

	public PriceTracker getTracker(String trackerName) {
		return this.priceTrackers.get(trackerName);
	}

	@XmlElement
	public TreeMap<String, PriceTracker> getTrackers() {
		return this.priceTrackers;
	}

	public Set<String> getAllNames() {
		return this.priceTrackers.keySet();
	}

	public void removeTracker(String key) {
		try {
			priceTrackers.remove(key);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void updatePrices() {
		for (String name : priceTrackers.keySet()) {
			getTracker(name).addPoint();
		}
	}

}
