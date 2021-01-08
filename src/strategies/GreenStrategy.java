package strategies;

import producer.InputProducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GreenStrategy implements EnergyStrategy {
    final double THRESHOLD =  0.000001;
    // renewable energy > pret > cantitate > id
    public List<InputProducer> getBestProducer(List<InputProducer> producers){
        List<InputProducer> sortedList = new ArrayList<>();
       // sortedList = producers;
        List<InputProducer> renewableList = new ArrayList<>();
        List<InputProducer> notRenewableList = new ArrayList<>();
        for (InputProducer producer : producers) {
            if (producer.getEnergyType().isRenewable()) {
                renewableList.add(producer);
            } else {
                notRenewableList.add(producer);
            }
        }
        Collections.sort(renewableList, (p1, p2) -> {
            if (Math.abs(p2.getPriceKW() - p1.getPriceKW()) <= THRESHOLD ) {
                return Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor());
            }
            return Double.compare(p1.getPriceKW(), p2.getPriceKW());
        });
        Collections.sort(notRenewableList, (p1, p2) -> {
            if (Math.abs(p2.getPriceKW() - p1.getPriceKW()) <= THRESHOLD) {
                return Long.compare(p2.getEnergyPerDistributor(), p1.getEnergyPerDistributor());
            }
            return Double.compare(p1.getPriceKW(), p2.getPriceKW());
        });
        sortedList = Stream.concat(renewableList.stream(), notRenewableList.stream())
                .collect(Collectors.toList());
        return sortedList;
    }
}
