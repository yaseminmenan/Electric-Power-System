package operations;

import common.Contract;
import entities.consumer.InputConsumer;
import entities.distributor.InputDistributor;
import input.Input;

import java.util.Comparator;
import java.util.List;

public class DistributorOperations implements Operations {
    /**
     * Operations to be done to the list of distributors
     * @param input - object that contains the file data
     */
    public void doOperations(final Input input) {

        for (InputDistributor distributor : input.getDistributors()) {
            // Skip the distributor if it is bankrupt
            if (distributor.getIsBankrupt()) {
                continue;
            }
            // Remove the contracts that have no remaining months
            distributor.getContracts().removeIf((contract ->
                    contract.getRemainedContractMonths() == 0));

            // Calculate total costs to be paid
            long totalCost = distributor.calculateTotalCost();

            // Pay costs
            distributor.setBudget(distributor.getBudget() - totalCost);

            // Check if distributor is bankrupt
            if (distributor.getBudget() < 0) {
                // Set bankrupt
                distributor.setIsBankrupt(true);
                // Eliminate all contracts
                for (Contract contract : distributor.getContracts()) {
                    InputConsumer consumer = input.getConsumer(contract.getConsumerId());
                    consumer.setContract(null);
                }
            }

            // Remove bankrupt consumers
            distributor.getContracts().removeIf(contract ->
                    input.getConsumer(contract.getConsumerId()).getIsBankrupt());

            // Subtract contract length by one month
            for (Contract contract : distributor.getContracts()) {
                contract.setRemainedContractMonths(contract.getRemainedContractMonths() - 1);
            }

            //Sort remaining contracts by month and then by id
            List<Contract> contracts = distributor.getContracts();
            contracts.sort(Comparator.comparing(Contract::getConsumerId));
            contracts.sort(Comparator.comparing(Contract::getRemainedContractMonths));

        }
    }
}
