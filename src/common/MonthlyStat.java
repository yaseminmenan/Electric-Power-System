package common;

import java.util.List;

public class MonthlyStat {
    private int month;
    private List<Long> distributorsIds;

    public MonthlyStat(int month, List<Long> distributorsIds) {
        this.month = month;
        this.distributorsIds = distributorsIds;
    }

    /**
     *  Get month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Set month
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Get list of distributors that have received energy from the producer
     */
    public List<Long> getDistributorsIds() {
        return distributorsIds;
    }

/*
    @Override
    public String toString() {
        return "MonthlyStat{" +
                "month=" + month +
                ", distributorsIDs=" + distributorsIds +
                '}'+"\n";
    }*/
}
