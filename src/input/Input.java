package input;
import com.fasterxml.jackson.annotation.JsonIgnore;
import common.Constants;
import consumer.InputConsumer;
import distributor.InputDistributor;
import common.Update;
import producer.InputProducer;

import java.util.List;

public class Input {
    @JsonIgnore
    private final long numberOfTurns;
    private List<InputConsumer> consumers;
    private List<InputDistributor> distributors;
    private List<InputProducer> energyProducers;
    @JsonIgnore
    private List<Update> monthlyUpdates;

    public Input(final long numberOfTurns, final List<InputConsumer> consumers,
                 final List<InputDistributor> distributors,
                 final List<InputProducer> producers,
                 final List<Update> updates) {
        this.numberOfTurns = numberOfTurns;
        this.consumers = consumers;
        this.distributors = distributors;
        this.monthlyUpdates = updates;
        this.energyProducers = producers;
    }

    /**
     * Get number of turns
     */
    public long getNumberOfTurns() {
        return numberOfTurns;
    }

    /**
     * Get list of monthly updates
     */
    public List<Update> getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public List<InputProducer> getEnergyProducers() {
        return energyProducers;
    }

    public void setEnergyProducers(List<InputProducer> producers) {
        this.energyProducers = producers;
    }

    /**
     * Get list of consumers
     */
    public List<InputConsumer> getConsumers() {
        return consumers;
    }

    /**
     * Get list of distributors
     */
    public List<InputDistributor> getDistributors() {
        return distributors;
    }

    /**
     * Get the distributor with a given id
     */
    public InputDistributor getDistributor(final long id) {
        for (InputDistributor distributor : this.getDistributors()) {
            if (distributor.getId() == id) {
                return distributor;
            }
        }
        return null;
    }

    /**
     * Get the consumer with a given id
     */
    public InputConsumer getConsumer(final long id) {
        for (InputConsumer consumer : this.getConsumers()) {
            if (consumer.getId() == id) {
                return consumer;
            }
        }
        return null;
    }

    public InputProducer getProducer(final long id) {
        for (InputProducer producer : this.getEnergyProducers()) {
            if (producer.getId() == id) {
                return producer;
            }
        }
        return null;
    }

    /**
     * Get the distributor with the cheapest contract cost
     */
    public InputDistributor chooseCheapestDistributor() {
        long contractCost = Constants.MAXVALUE;
        InputDistributor cheapestDistributor = null;

        for (InputDistributor distributor : this.getDistributors()) {
            // Skip the distributor if it is bankrupt
            if (distributor.getIsBankrupt()) {
                continue;
            }
            // Calculate contract cost
            distributor.calculateContractCost();
            long cost = distributor.getContractCost();
            // long cost = distributor.calculateContractCost();
            // If the newly calculated cost is cheaper, set it as the
            // cheapest contract cost and retain the distributor
            if (contractCost > cost) {
                contractCost = cost;
                cheapestDistributor = distributor;
            }
        }
        // Return the distributor with the cheapest cost
        return cheapestDistributor;
    }

}
