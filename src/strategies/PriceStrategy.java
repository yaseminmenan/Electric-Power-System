package strategies;

import common.Constants;
import entities.producer.InputProducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PriceStrategy implements EnergyStrategy {
    /**
     * Return a list of producers sorted by price, and then by quantity
     */
    public List<InputProducer> getBestProducer(List<InputProducer> producers) {
        // Copy the list given in the parameters
        List<InputProducer> sortedList = new ArrayList<>(producers);

        // Sort the list by price per KW, and if they are equal, by energy quantity
        Collections.sort(sortedList, (p1, p2) -> {
            // int value1 = p2.campus.compareTo(p1.campus);
            if (Math.abs(p2.getPriceKW() - p1.getPriceKW()) <= Constants.THRESHOLD) {
                return Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor());
            }
            return Double.compare(p1.getPriceKW(), p2.getPriceKW());
        });
        return sortedList;
    }
}
