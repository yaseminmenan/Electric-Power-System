package common;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Contract {
    @JsonIgnore
    private long distributorId;
    private long consumerId;
    private long price;
    private long remainedContractMonths;

    public Contract(final long distributorId, final long consumerId, final long price,
                    final long remainedContractMonths) {
        this.distributorId = distributorId;
        this.consumerId = consumerId;
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
    }

    /**
     * Get consumer id
     */
    public long getConsumerId() {
        return consumerId;
    }

    /**
     * Set consumer id
     */
    public void setConsumerId(final long consumerId) {
        this.consumerId = consumerId;
    }

    /**
     * Get distributor id
     */
    public long getDistributorId() {
        return distributorId;
    }

    /**
     * Get price
     */
    public long getPrice() {
        return price;
    }

    /**
     * Set price
     */
    public void setPrice(final long price) {
        this.price = price;
    }

    /**
     * Get remaining contract months
     */
    public long getRemainedContractMonths() {
        return remainedContractMonths;
    }

    /**
     * Set remaining contract months
     */
    public void setRemainedContractMonths(final long remainedContractMonths) {
        this.remainedContractMonths = remainedContractMonths;
    }

}
