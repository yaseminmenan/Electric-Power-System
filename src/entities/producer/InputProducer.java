package entities.producer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import common.MonthlyStat;
import entities.distributor.InputDistributor;
import entities.EnergyType;
import java.util.ArrayList;
import java.util.List;

public class InputProducer implements Producer {
    private long id;
    private long maxDistributors;
    private double priceKW;
    private EnergyType energyType;
    private long energyPerDistributor;
    @JsonIgnore
    private List<InputDistributor> distributors;
    private List<MonthlyStat> monthlyStats;

    public InputProducer() {

    }
    /**
     * Insert initial data from input
     */
    public void insertData(final long id, final EnergyType energyType,
                                  final long maxDistributors, final double priceKW,
                                  final long energyPerDistributor) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.monthlyStats = new ArrayList<>();
        this.distributors = new ArrayList<>();
    }

    /**
     * Get producer id
     */
    public long getId() {
        return id;
    }

    /**
     * Get producer energy type
     */
    public EnergyType getEnergyType() {
        return energyType;
    }

    /**
     * Set energy type
     */
    public void setEnergyType(final EnergyType energyType) {
        this.energyType = energyType;
    }

    /**
     * Get number of maximum distributors
     */
    public long getMaxDistributors() {
        return maxDistributors;
    }

    /**
     * Set number of maximum distributors
     */
    public void setMaxDistributors(final long maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    /**
     * Get price per KW
     */
    public double getPriceKW() {
        return priceKW;
    }

    /**
     * Set price per KW
     */
    public void setPriceKW(final double priceKW) {
        this.priceKW = priceKW;
    }

    /**
     * Get energy per distributor
     */
    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    /**
     * Set energy per distributor
     */
    public void setEnergyPerDistributor(final long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    /**
     * Get list of monthly stats
     */
    public List<MonthlyStat> getMonthlyStats() {
        return monthlyStats;
    }

    /**
     * Set list of monthly stats
     */
    public void setMonthlyStats(final List<MonthlyStat> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    /**
     * Get the producer's list of distributors
     */
    public List<InputDistributor> getDistributors() {
        return distributors;
    }

    /**
     * Set the list of distributors
     */
    public void setDistributors(final List<InputDistributor> distributors) {
        this.distributors = distributors;
    }

    /**
     * The producer has changed its energy per distributor and notifies all distributors
     */
    public void setChanged(final InputProducer producerChanges) {
        this.energyPerDistributor = producerChanges.getEnergyPerDistributor();

        for (InputDistributor distributor : distributors) {
               distributor.update(this);
        }
    }

}
