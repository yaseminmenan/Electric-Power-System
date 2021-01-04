package common;

public class Debt {
    private long cost;
    private long distributorId;

    public Debt(final long cost, final long distributorId) {
        this.cost = cost;
        this.distributorId = distributorId;
    }

    /**
     * Get cost
     */
    public long getCost() {
        return cost;
    }

}
