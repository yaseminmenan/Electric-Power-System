package strategies;

import common.Constants;
import entities.producer.InputProducer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GreenStrategy implements EnergyStrategy {
    /**
     * Return a list of producers sorted by energy type, then by price,
     * and then by quantity
     */
    public List<InputProducer> getBestProducer(List<InputProducer> producers) {
        List<InputProducer> renewableList = new ArrayList<>();
        List<InputProducer> notRenewableList = new ArrayList<>();

        // Split the list of producers given in the parameters in two lists
        // renewable and not renewable
        for (InputProducer producer : producers) {
            if (producer.getEnergyType().isRenewable()) {
                renewableList.add(producer);
            } else {
                notRenewableList.add(producer);
            }
        }
        // Sort the renewable list by price, and, if the prices are equal, by quantity
        Collections.sort(renewableList, (p1, p2) -> {
            if (Math.abs(p2.getPriceKW() - p1.getPriceKW()) <= Constants.THRESHOLD) {
                return Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor());
            }
            return Double.compare(p1.getPriceKW(), p2.getPriceKW());
        });

        // Sort the nonRenewable list by price, and, if the prices are equal, by quantity
        Collections.sort(notRenewableList, (p1, p2) -> {
            if (Math.abs(p2.getPriceKW() - p1.getPriceKW()) <= Constants.THRESHOLD) {
                return Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor());
            }
            return Double.compare(p1.getPriceKW(), p2.getPriceKW());
        });

        // Concatenate the two lists,
        List<InputProducer> sortedList = Stream.concat(renewableList.stream(),
                notRenewableList.stream()).collect(Collectors.toList());
        return sortedList;
    }
}
