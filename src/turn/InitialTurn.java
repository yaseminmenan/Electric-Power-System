package turn;

import entities.distributor.InputDistributor;
import input.Input;
import operations.ConsumerOperations;
import operations.DistributorOperations;
import strategies.EnergyStrategyFactory;

public class InitialTurn {
    public InitialTurn() {

    }
    /**
     * Initial turn with only operations for the consumer
     * and distributor entities
     * @param input - object that contains the file data
     */
    public void doTurn(final Input input, EnergyStrategyFactory factory) {
        // Choose producers for each distributor and calculate production and contract cost
        for (InputDistributor distributor : input.getDistributors()) {
            distributor.chooseProducers(input, factory);
            distributor.calculateProductionCost();
            distributor.calculateContractCost();

        }
        // Do consumer operations
        new ConsumerOperations().doOperations(input);

        // Do distributor operations
        new DistributorOperations().doOperations(input);
    }
}
