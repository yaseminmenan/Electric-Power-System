package common;

import entities.consumer.InputConsumer;
import entities.distributor.InputDistributor;
import input.Input;
import entities.producer.InputProducer;

import java.util.List;

public class Update {
    private List<InputConsumer> newConsumers;
    private List<InputDistributor> distributorChanges;
    private List<InputProducer> producerChanges;

    public Update(final List<InputConsumer> newConsumers,
                  final List<InputDistributor> distributorChanges,
                  final List<InputProducer> producerChanges) {
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
            }
        }

    }

    /**
     * Return list of producer energy per distributor changes
     */
    public List<InputProducer> getProducerChanges() {
        return producerChanges;
    }

    /**
     * Set list of producer changes
     */
    public void setProducerChanges(List<InputProducer> producerChanges) {
        this.producerChanges = producerChanges;
    }

    /**
     * Update the list of producers
     */
    public void updateProducers(final Input input) {
        if (this.producerChanges.size() != 0) {
            for (InputProducer producerChange : this.producerChanges) {
                InputProducer producer = input.getProducer(producerChange.getId());
                producer.setChanged(producerChange);
            }
        }
    }

}
