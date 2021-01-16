package turn;

import common.Update;
import entities.distributor.InputDistributor;
import entities.producer.InputProducer;
import common.MonthlyStat;
import input.Input;
import operations.ConsumerOperations;
import operations.DistributorOperations;
import strategies.EnergyStrategyFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NormalTurn {
    public NormalTurn() {

    }
    /**
     * Normal turn that updates the entities lists and
     * does operations on the consumer and distributor entities
     * @param input - object that contains the file data
     * @param turnNumber - number of the current turn that is simulated
     * @param factory - factory that creates strategies to get the best producer for distributor
     */
    public void doTurn(final Input input, final int turnNumber,
                             EnergyStrategyFactory factory) {
        // Update consumers and distributors lists for the given turn
        Update update = input.getMonthlyUpdates().get(turnNumber);
        update.updateLists(input);

        // Do consumer operations
        new ConsumerOperations().doOperations(input);
        // Do distributor operations
        new DistributorOperations().doOperations(input);

        // Update the producers list
        update.updateProducers(input);

        // If any producer has changed its energy quantity, then its
        // distributors must choose new producers
        for (InputDistributor distributor : input.getDistributors()) {
            // If the list of changed producers has at least one producer, remove
            if (distributor.getChangedProducers().size() != 0) {
                // Remove the distributor from each producer's list
                for (InputProducer producer : distributor.getProducers()) {
                    producer.getDistributors().remove(distributor);
                }
                // Remove all producers from the distributor's list
                distributor.getProducers().removeAll(distributor.getProducers());

                // Choose new producers
                distributor.chooseProducers(input, factory);
                // Calculate the new production cost
                distributor.calculateProductionCost();

                // Empty the list of changed producers
                distributor.getChangedProducers().removeAll(distributor.getChangedProducers());
            }
        }

        // Do the monthly stats for each producer
        for (InputProducer producer : input.getEnergyProducers()) {
            List<Long> distributorsIds = new ArrayList<>();

            // Add the ids of each distributor from the producer's list
            for (InputDistributor distributor : producer.getDistributors()) {
                distributorsIds.add(distributor.getId());
            }

            // Sort the list in ascending order by id
            Collections.sort(distributorsIds);

            // Create a monthly stat
            MonthlyStat monthlyStat = new MonthlyStat(turnNumber + 1, distributorsIds);
            // Add the monthly stat to list of monthly stats
            producer.getMonthlyStats().add(monthlyStat);

            // Remove all bankrupt distributors
            producer.getDistributors().removeIf(InputDistributor::getIsBankrupt);
        }
    }

}
