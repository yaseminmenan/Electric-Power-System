package entities.distributor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import common.Constants;
import common.Contract;
import entities.producer.InputProducer;
import input.Input;
import strategies.EnergyChoiceStrategyType;
import strategies.EnergyStrategyFactory;

import java.util.ArrayList;
import java.util.List;

public class InputDistributor implements Distributor {
    private long id;
    private long energyNeededKW;
    private long contractCost;
    private long budget;
    private EnergyChoiceStrategyType producerStrategy;
    private boolean isBankrupt;
    private List<Contract> contracts;
    @JsonIgnore
    private long contractLength;
    @JsonIgnore
    private long infrastructureCost;
    @JsonIgnore
    private long productionCost;
    @JsonIgnore
    private List<InputProducer> changedProducers;
    @JsonIgnore
    private List<InputProducer> producers;

    public InputDistributor() {

    }
    /**
     * Insert initial data
     */
    public void insertData(final long id, final long contractLength, final long budget,
                                  final long infrastructureCost,
                                  final long energyNeededKW, final String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = budget;
        this.infrastructureCost = infrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = EnergyChoiceStrategyType.valueOf(producerStrategy);
        this.contracts = new ArrayList<>();
        this.producers = new ArrayList<>();
        this.changedProducers = new ArrayList<>();
    }
    /**
     * Insert costs changes data
     */
    public void insertChangesData(final long id, final long infrastructureCost,
                                  final long productionCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
        this.productionCost = productionCost;
    }

    /**
     * Get id
     */
    public long getId() {
        return id;
    }

    /**
     * Get infrastructure cost
     */
    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    /**
     * Set infrastructure cost
     */
    public void setInfrastructureCost(final long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    /**
     * Get production cost
     */
    public long getProductionCost() {
        return productionCost;
    }

    /**
     * Set production cost
     */
    public void setProductionCost(final long productionCost) {
        this.productionCost = productionCost;
    }

    /**
     * Get budget
     */
    public long getBudget() {
        return budget;
    }

    /**
     * Set budget
     */
    public void setBudget(final long budget) {
        this.budget = budget;
    }

    /**
     * Get boolean value of isBankrupt
     */
    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    /**
     * Set boolean value of isBankrupt
     */
    public void setIsBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    /**
     * Return
     */
    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    /**
     * Set value of needed energy
     */
    public void setEnergyNeededKW(long energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    /**
     * Get type of producer strategy
     */
    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    /**
     * Set type of producer strategy
     */
    public void setProducerStrategy(EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    /**
     * Return the distributor's list of producers
     */
    public List<InputProducer> getProducers() {
        return producers;
    }

    /**
     * Set the distributor's list of producers
     */
    public void setProducers(List<InputProducer> producers) {
        this.producers = producers;
    }

    /**
     * Return cost of contract
     */
    public long getContractCost() {
        return contractCost;
    }

    /**
     * Set cost of contract
     */
    public void setContractCost(long contractCost) {
        this.contractCost = contractCost;
    }

    /**
     * Get list of contract
     */
    public List<Contract> getContracts() {
        return contracts;
    }

    /**
     * Get length of a contract
     */
    public long getContractLength() {
        return contractLength;
    }

    /**
     * Set length of a contract
     */
    public void setContractLength(final long contractLength) {
        this.contractLength = contractLength;
    }

    /**
     * Calculate cost of a contract at the beginning of a month
     */
    public void calculateContractCost() {
        // Calculate profit
        long profit = Math.round(Math.floor(Constants.PROFIT * this.getProductionCost()));
        long cost = 0;
        // Calculate and return the contract cost if the entities.distributor has no contracts
        if (this.getContracts().size() == 0) {
            cost = this.getInfrastructureCost() + this.getProductionCost() + profit;
            this.contractCost = cost;
        } else {
            // Calculate and return the contract cost, dividing the infrastructure costs
            // by the number of current contracts
            cost = Math.round(Math.floor((float) this.getInfrastructureCost()
                    / this.getContracts().size()) + this.getProductionCost() + profit);
            this.contractCost = cost;
        }
    }

    /**
     * Calculate total cost
     */
    public long calculateTotalCost() {
        return this.getInfrastructureCost() + this.getProductionCost()
                * this.getContracts().size();
    }

    /**
     * Calculate production cost
     */
    public void calculateProductionCost() {
        float cost = 0;
        for (InputProducer producer : this.producers) {
            cost += producer.getEnergyPerDistributor() * producer.getPriceKW();
        }
        this.productionCost =  Math.round(Math.floor(cost / Constants.TEN));
    }

    /**
     * Choose producers
     */
    public void chooseProducers(final Input input, EnergyStrategyFactory factory) {
        long energy = 0;
        var strategy = factory.createStrategy(this.getProducerStrategy());
        List<InputProducer> producerList =
                strategy.getBestProducer(input.getEnergyProducers());
        for (InputProducer producer : producerList) {
            if (energy < this.getEnergyNeededKW()
                    && producer.getDistributors().size() < producer.getMaxDistributors()) {
                energy += producer.getEnergyPerDistributor();
                producer.getDistributors().add(this);
                this.getProducers().add(producer);
            } else if (energy >= this.getEnergyNeededKW()) {
                break;
            }
        }
    }

    /**
     * Remembers the producer that has changed its quantity of energy per distributor
     * in the list of changed producers
     */
    public void update(final InputProducer producer) {
        this.changedProducers.add(producer);
    }

    /**
     * Return list of changed producers
     */
    public List<InputProducer> getChangedProducers() {
        return changedProducers;
    }

}
