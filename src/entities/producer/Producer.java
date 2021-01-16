package entities.producer;

import entities.EnergyType;

public interface Producer {
    /**
     * Method that inserts data in producer
     */
    void insertData(long id, EnergyType energyType,
                    long maxDistributors, double priceKW,
                    long energyPerDistributor);
}
