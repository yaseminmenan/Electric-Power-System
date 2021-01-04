package producer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import distributor.InputDistributor;
import entities.EnergyType;
import input.Input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InputProducer {
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

    public void insertInitialData(long id, EnergyType energyType, long maxDistributors, double priceKW, long energyPerDistributor) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.monthlyStats = new ArrayList<>();
        this.distributors = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(final EnergyType energyType) {
        this.energyType = energyType;
    }

    public long getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(final long maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(final double priceKW) {
        this.priceKW = priceKW;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public List<MonthlyStat> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(final List<MonthlyStat> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    public List<InputDistributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(final List<InputDistributor> distributors) {
        this.distributors = distributors;
    }

    public void addObserver(InputDistributor distributor) {
        this.distributors.add(distributor);
    }

    public void setChanged(final InputProducer producerChanges, final Input input) {
        this.energyPerDistributor = producerChanges.getEnergyPerDistributor();
        List<InputDistributor> distributors = new ArrayList<>(this.distributors);
        this.distributors.removeAll(this.distributors);
        for (InputDistributor distributor : distributors) {
            if (!distributor.getIsBankrupt()) {
               distributor.update(this.id, input);
            }
        }

    }
    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", energyType=" + energyType +
                ", maxDistributors=" + maxDistributors +
                ", priceKW=" + priceKW +
                ", energyPerDistributor=" + energyPerDistributor +
                ", monthlyStats=" + monthlyStats +
                '}';
    }
}
