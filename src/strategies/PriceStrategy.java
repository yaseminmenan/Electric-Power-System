package strategies;

import producer.Producer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PriceStrategy implements EnergyStrategy {
    final double THRESHOLD =  0.000001;

    // pret > cantitate > id
    public List<Producer> getBestProducer(List<Producer> producers){
        List<Producer> sortedList = new ArrayList<>(producers);
        Collections.sort(sortedList, (p1, p2) -> {
            // int value1 = p2.campus.compareTo(p1.campus);
            if (Math.abs(p2.getPriceKW() - p1.getPriceKW()) <= THRESHOLD) {
                return Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor());
            }
            return Double.compare(p1.getPriceKW(), p2.getPriceKW());
        });
        return sortedList;
    }
}
