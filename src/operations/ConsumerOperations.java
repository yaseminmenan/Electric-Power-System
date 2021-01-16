package operations;

import common.Debt;
import entities.consumer.InputConsumer;
import entities.distributor.InputDistributor;
import input.Input;

public class ConsumerOperations implements Operations {
    /**
     * Operations to be done to the list of consumers
     * @param input - object that contains the file data
     */
    public void doOperations(final Input input) {
        // Find the distributor with the cheapest contract cost
        InputDistributor cheapestDistributor = input.chooseCheapestDistributor();
        cheapestDistributor.calculateContractCost();
        long contractCost = cheapestDistributor.getContractCost();
        // Consumers receive income, choose contract, pay rates
        for (InputConsumer consumer : input.getConsumers()) {
            // Skip the consumer if it is bankrupt
            if (consumer.getIsBankrupt()) {
                continue;
            }

            // Add monthly income to the budget
            consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());

            // Check if the consumer has no contract or the contract is about to end
            // And if so, make a contract with the cheapest distributor
            if (consumer.getContract() == null
                    || consumer.getContract().getRemainedContractMonths() == 0) {
                consumer.makeContract(cheapestDistributor, contractCost);
            }

            // If the consumer has a contract already, get the distributor
            if (consumer.getContract() != null) {
                InputDistributor distributor =
                        input.getDistributor(consumer.getContract().getDistributorId());

                // If the distributor is bankrupt, clear the debt if it exists and make
                // a contract with the cheapest distributor
                if (distributor.getIsBankrupt()) {
                    consumer.setDebt(null);
                    consumer.makeContract(cheapestDistributor, contractCost);
                }
            }

            // Get the distributor with which the contract has been made
            InputDistributor distributor =
                    input.getDistributor(consumer.getContract().getDistributorId());
            // If the consumer is in debt, pay the debt
            if (consumer.getDebt() != null) {
                Debt debt = consumer.getDebt();

                // If the consumer cannot pay the debt, it is bankrupt
                if (consumer.getBudget() < debt.getCost()) {
                    consumer.setIsBankrupt(true);
                } else {
                    // Else the consumer pays the debt
                    consumer.setBudget(consumer.getBudget() - debt.getCost());
                    // Distributor receives debt
                    distributor.setBudget(distributor.getBudget() + debt.getCost());
                }
            } else {
                // Consumer pays contract cost
                // If the consumer cannot pay, it is in debt
                if (consumer.getBudget() < consumer.getContract().getPrice()) {
                    // Create and set debt
                    Debt debt = new Debt(consumer.calculateDebt(consumer.getContract().getPrice()),
                            cheapestDistributor.getId());
                    consumer.setDebt(debt);
                } else {
                    // Else the consumer pays normally
                    consumer.setBudget(consumer.getBudget() - consumer.getContract().getPrice());
                    // Distributor receives contract cost
                    distributor.setBudget(distributor.getBudget()
                            + consumer.getContract().getPrice());
                }
            }

        }
    }
}
