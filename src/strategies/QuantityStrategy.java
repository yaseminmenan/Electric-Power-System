package strategies;

import entities.producer.InputProducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class QuantityStrategy implements EnergyStrategy {
    /**
     * Return a list of producers sorted by energy quantity
     */
    public List<InputProducer> getBestProducer(List<InputProducer> producers) {
        // Copy the list given in the parameters
        List<InputProducer> sortedList = new ArrayList<>(producers);

        // Sort the list by energy quantity
        Collections.sort(sortedList, (p1, p2) ->
                Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor()));
        return sortedList;
    }
}
