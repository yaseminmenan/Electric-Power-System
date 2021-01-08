package consumer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import common.Constants;
import common.Debt;
import common.Contract;
import distributor.InputDistributor;

public class InputConsumer implements Consumer {
    private long id;
    private boolean isBankrupt;
    private long budget;
    @JsonIgnore
    private long monthlyIncome;
    @JsonIgnore
    private Contract contract;
    @JsonIgnore
    private Debt debt;

    public InputConsumer() {

    }
    /**
     * Insert initial data
     */
    public void insertInitialData(final long id, final long budget, final long monthlyIncome) {
        this.id = id;
        this.budget = budget;
        this.monthlyIncome = monthlyIncome;
    }

    /**
     * Get id
     */
    public long getId() {
        return id;
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
     * Get monthly income
     */
    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    /**
     * Set monthly income
     */
    public void setMonthlyIncome(final long monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    /**
     * Get isBankrupt value (true or false)
     */
    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    /**
     * Set isBankrupt value
     */
    public void setIsBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    /**
     * Return debt
     */
    public Debt getDebt() {
        return debt;
    }
    /**
     * Set debt
     */
    public void setDebt(final Debt debt) {
        this.debt = debt;
    }

    /**
     * Get contract
     */
    public Contract getContract() {
        return contract;
    }

    /**
     * Set contract
     */
    public void setContract(final Contract contract) {
        this.contract = contract;
    }

    /**
     * Create or update a contract with the given distributor and cost
     */
    public void makeContract(final InputDistributor distributor, final long contractCost) {
        for (Contract contract : distributor.getContracts()) {
            // If the consumer already has a contract with the distributor
            // Update cost and remaining months
            if (contract.getConsumerId() == this.getId()) {
                contract.setPrice(contractCost);
                contract.setRemainedContractMonths(distributor.getContractLength());
                return;
            }
        }

        // There is no existing contract, create a new one
        Contract contract = new Contract(distributor.getId(), this.getId(), contractCost,
                distributor.getContractLength());
        this.setContract(contract);
        distributor.getContracts().add(contract);
    }

    /**
     * Calculate debt
     */
    public long calculateDebt(final long cost) {
        return Math.round(Math.floor(Constants.DEBT * cost)) + this.getContract().getPrice();
    }
}
