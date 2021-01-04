package strategies;

import producer.InputProducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuantityStrategy implements EnergyStrategy {
    // cantitate > id
    public List<InputProducer> getBestProducer(List<InputProducer> producers){
        List<InputProducer> sortedList = new ArrayList<>(producers);
        Collections.sort(sortedList,
             (p1, p2) -> Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor()));
        return sortedList;
    }
}
