package strategies;

import producer.Producer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuantityStrategy implements EnergyStrategy {
    // cantitate > id
    public List<Producer> getBestProducer(List<Producer> producers){
        List<Producer> sortedList = new ArrayList<>(producers);
        Collections.sort(sortedList,
             (p1, p2) -> Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor()));
        return sortedList;
    }
}
