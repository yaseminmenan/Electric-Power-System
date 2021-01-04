package strategies;

import producer.Producer;

import java.util.List;

public interface EnergyStrategy {
    List<Producer> getBestProducer(List<Producer> producers);
}
