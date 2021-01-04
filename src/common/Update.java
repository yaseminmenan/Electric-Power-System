package common;

import consumer.InputConsumer;
import distributor.InputDistributor;
import input.Input;
import producer.Producer;

import java.util.List;

public class Update {
    private List<InputConsumer> newConsumers;
    //distributorChanges
    private List<InputDistributor> distributorChanges;
    private List<Producer> producerChanges;

    public Update(final List<InputConsumer> newConsumers,
                  final List<InputDistributor> distributorChanges,
                  final List<Producer> producerChanges) {
        this.newConsumers = newConsumers;
        this.distributorChanges = distributorChanges;
        this.producerChanges = producerChanges;
    }

    /**
     * Get list of new consumers
     */
    public List<InputConsumer> getNewConsumers() {
        return newConsumers;
    }
    /**
     * Get list of distributor costs changes
     */
    public List<InputDistributor> getDistributorChanges() {
        return distributorChanges;
    }

    /**
     * Update the consumers and distributor lists from input
     * with the lists from update
     */
    public void updateLists(final Input input) {
        // Check for new consumers
        if (this.getNewConsumers().size() != 0) {
            for (InputConsumer consumer : this.getNewConsumers()) {
                // Add new consumer to the list of consumers
                input.getConsumers().add(consumer);
            }
        }

        // Check for distributor cost changes
        if (this.getDistributorChanges().size() != 0) {

            for (InputDistributor inputDistributorChange : this.getDistributorChanges()) {
                // Get the distributor
                InputDistributor inputDistributor =
                        input.getDistributor(inputDistributorChange.getId());
                // Change infrastructure cost
                inputDistributor.setInfrastructureCost(
                        inputDistributorChange.getInfrastructureCost());
                // Change productions cost
               // inputDistributor.setProductionCost(inputDistributorChange.getProductionCost());
            }
        }

        if (this.producerChanges.size() != 0) {
            for( Producer producerChange : this.producerChanges) {
                Producer producer = input.getProducer(producerChange.getId());

                producer.setEnergyPerDistributor(producerChange.getEnergyPerDistributor());
            }
        }
    }

    @Override
    public String toString() {
        return "Update{" +
                "newConsumers=" + newConsumers +
                "\n, distributorChanges=" + distributorChanges +
                "\n, producerChanges=" + producerChanges +
                '}';
    }
}
