package distributor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import common.Constants;
import common.Contract;
import input.Input;
import producer.InputProducer;
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


    //private boolean hasProducer;
    @JsonIgnore
    private List<InputProducer> producers;

    public InputDistributor() {

    }
    /**
     * Insert initial data
     */
    public void insertInitialData(final long id, final long contractLength, final long budget,
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

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(long energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public List<InputProducer> getProducers() {
        return producers;
    }

    public void setProducers(List<InputProducer> producers) {
        this.producers = producers;
    }

    public long getContractCost() {
        return contractCost;
    }

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
        // Calculate and return the contract cost if the distributor has no contracts
        if (this.getContracts().size() == 0) {
            cost = this.getInfrastructureCost() + this.getProductionCost() + profit;
            this.contractCost = cost;
            //return this.getInfrastructureCost() + this.getProductionCost() + profit;
        } else {
            // Calculate and return the contract cost, dividing the infrastructure costs
            // by the number of current contracts
            cost = Math.round(Math.floor((float) this.getInfrastructureCost()
                    / this.getContracts().size()) + this.getProductionCost() + profit);
            this.contractCost = cost;
           // return Math.round(Math.floor((float) this.getInfrastructureCost()
           //         / this.getContracts().size()) + this.getProductionCost() + profit);
        }
    }

    /**
     * Calculate total cost
     */
    public long calculateTotalCost() {
        return this.getInfrastructureCost() + this.getProductionCost()
                * this.getContracts().size();
    }

    public void calculateProductionCost() {
        //cost = sum (cantitate energie de la producator * pret pe Kw de la producator)
        //productionCost = Math.round(Math.floor(cost / 10));
        float cost = 0;
        for (InputProducer producer : producers) {
           // System.out.println("producer id: " +producer.getId());
            cost += producer.getEnergyPerDistributor() * producer.getPriceKW();
        }
        this.productionCost =  Math.round(Math.floor(cost / 10));
    }

    public void update(final long producerId, final Input input) {
        this.producers.removeIf(producer -> producer.getId() == producerId);
            long energy = 0;
            for (InputProducer producer : this.getProducers()) {
                energy += producer.getEnergyPerDistributor();
            }
            EnergyStrategyFactory factory = new EnergyStrategyFactory();
            var strategy = factory.createStrategy(this.getProducerStrategy());
            List<InputProducer> producerList = strategy.getBestProducer(input.getEnergyProducers());
            for (InputProducer producer : producerList) {
                if (!this.getProducers().contains(producer) &&
                        producer.getDistributors().size() < producer.getMaxDistributors()) {
                    if (energy < this.getEnergyNeededKW()) {
                        energy += producer.getEnergyPerDistributor();
                        // producer.addObserver(distributor);
                        producer.getDistributors().add(this);
                        this.getProducers().add(producer);

                    }
                    else if (energy >= this.getEnergyNeededKW()) {
                        break;
                    }
                }
            }
            this.calculateProductionCost();
          //  this.calculateContractCost();

    }

    @Override
    public String toString() {
        return "InputDistributor{" +
                "id=" + id +
                ", budget=" + budget +
                ", contractCost=" + contractCost +
                ", isBankrupt=" + isBankrupt +
                ", contracts=" + contracts +
                ", contractLength=" + contractLength +
                ", infrastructureCost=" + infrastructureCost +
                ", productionCost=" + productionCost +
                ", energyNeededKW=" + energyNeededKW +
                ", producers=" + producers +
                ", producerStrategy=" + producerStrategy +
                '}';
    }
}
