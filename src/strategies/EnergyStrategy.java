package strategies;

import entities.producer.InputProducer;

import java.util.List;

public interface EnergyStrategy {
    /**
     *  Returns a list of sorted producers
     */
    List<InputProducer> getBestProducer(List<InputProducer> producers);
}
