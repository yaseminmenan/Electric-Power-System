package strategies;

import producer.InputProducer;

import java.util.List;

public interface EnergyStrategy {
    List<InputProducer> getBestProducer(List<InputProducer> producers);
}
