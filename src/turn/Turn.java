package turn;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import common.Debt;
import consumer.InputConsumer;
import distributor.Distributor;
import distributor.InputDistributor;
import input.Input;
import common.Contract;
import common.Update;
import producer.MonthlyStat;
import producer.InputProducer;
import strategies.EnergyChoiceStrategyType;
import strategies.EnergyStrategyFactory;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Turn {
    public Turn() {
    }

    /**
     * Method to do turns for data in input
     * @param input - object that contains the file data
     */
    public void doTurns(final Input input) {
        // Do initial turn
        EnergyStrategyFactory factory = new EnergyStrategyFactory();
        this.doInitialTurn(input, factory);
      //  System.out.println(input);

      //  System.out.println();
        // Do the normal turns
        for (int i = 0; i < input.getNumberOfTurns(); i++) {
            this.doNormalTurn(input, i, factory);

            // Calculate the number of bankrupt distributors
            int bankruptNr = 0;
            for (InputDistributor distributor : input.getDistributors()) {
                if (distributor.getIsBankrupt()) {
                    bankruptNr++;
                }
            }
            // If all the distributors are bankrupt, the simulation has ended
            if (bankruptNr == input.getDistributors().size()) {
                break;
            }

            //System.out.println(input);

           // System.out.println();
        }
    }

    /**
     * Initial turn with only operations for the consumer
     * and distributor entities
     * @param input - object that contains the file data
     */
    public void doInitialTurn(final Input input, EnergyStrategyFactory factory) {
        for (InputDistributor distributor : input.getDistributors()) {
            long energy = 0;
            var strategy = factory.createStrategy(distributor.getProducerStrategy());
            List<InputProducer> producerList = strategy.getBestProducer(input.getEnergyProducers());

            for (InputProducer producer : producerList) {
                if (energy < distributor.getEnergyNeededKW() &&
                        producer.getDistributors().size() < producer.getMaxDistributors()) {
                    energy += producer.getEnergyPerDistributor();
                   // producer.addObserver(distributor);
                    producer.getDistributors().add(distributor);
                    distributor.getProducers().add(producer);

                }
                else if (energy >= distributor.getEnergyNeededKW()) {
                    break;
                }
            }
            distributor.calculateProductionCost();
            distributor.calculateContractCost();

        }
        consumerOperations(input);
        distributorOperations(input);

    }

    /**
     * Normal turn that updates the entities lists and
     * does operations on the consumer and distributor entities
     * @param input - object that contains the file data
     * @param turnNumber - number of the current turn that is simulated
     */
    public void doNormalTurn(final Input input, final int turnNumber, EnergyStrategyFactory factory) {
      //  System.out.println("TURN " + (turnNumber +1 ));
        // Update consumers and distributors lists for the given turn
        Update update = input.getMonthlyUpdates().get(turnNumber);
        update.updateLists(input);

        // Do consumer operations
        consumerOperations(input);

        // Do distributor operations
        distributorOperations(input);

        update.updateProducers(input);
      //  System.out.println("turn " + (turnNumber +1));
        for (InputDistributor distributor : input.getDistributors()) {
            if (distributor.getChangedProducers().size() != 0) {
                for (InputProducer producer : distributor.getProducers()) {
                    producer.getDistributors().remove(distributor);
                    //distributor.getProducers().remove(producer);
                }
                //System.out.println(distributor.getProducers());
                long energy = 0;
                distributor.getProducers().removeAll(distributor.getProducers());

                var strategy = factory.createStrategy(distributor.getProducerStrategy());
                List<InputProducer> producerList = strategy.getBestProducer(input.getEnergyProducers());
//                if (distributor.getProducerStrategy().equals(EnergyChoiceStrategyType.valueOf("GREEN"))) {
//                    System.out.println("distr id: " + distributor.getId());
//                    System.out.println("sorted list: " + producerList);
//                    for (InputProducer producer : producerList) {
//                        System.out.println("prod id: " + producer.getId() + " " +producer.getDistributors());
//                    }
//                    System.out.println();
//                }
                for (InputProducer producer : producerList) {
                    if (producer.getDistributors().size() < producer.getMaxDistributors()) {
                        if (energy < distributor.getEnergyNeededKW()) {
                            energy += producer.getEnergyPerDistributor();
                            // producer.addObserver(distributor);
                            producer.getDistributors().add(distributor);
                            distributor.getProducers().add(producer);

                        }
                        else if (energy >= distributor.getEnergyNeededKW()) {
                            break;
                        }
                    }
                }
                distributor.calculateProductionCost();
              //  System.out.println(distributor.getProducers());

                distributor.getChangedProducers().removeAll(distributor.getChangedProducers());
            }
        }

        for (InputProducer producer : input.getEnergyProducers()) {
            List<Long> distributorsIds = new ArrayList<>();
            for (InputDistributor distributor : producer.getDistributors()) {
                // if(!distributorsIds.contains(distributor.getId())){
                distributorsIds.add(distributor.getId());
                //  }
            }
            Collections.sort(distributorsIds);
            MonthlyStat monthlyStat = new MonthlyStat(turnNumber + 1, distributorsIds);
            producer.getMonthlyStats().add(monthlyStat);
            producer.getDistributors().removeIf(InputDistributor::getIsBankrupt);
        }
    }

    /**
     * Operations to be done to the list of consumers
     * @param input - object that contains the file data
     */
    public void consumerOperations(final Input input) {
        // Find the distributor with the cheapest contract cost
        InputDistributor cheapestDistributor = input.chooseCheapestDistributor();
        cheapestDistributor.calculateContractCost();
        long contractCost = cheapestDistributor.getContractCost();
        // Consumers receive income, choose contract, pay rates
        for (InputConsumer consumer : input.getConsumers()) {
          //  System.out.println("consumer id: "+consumer.getId());
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
                    // Distributor receives contract price
                    distributor.setBudget(distributor.getBudget()
                            + consumer.getContract().getPrice());
                }
            }

        }
    }

    /**
     * Operations to be done to the list of distributors
     * @param input - object that contains the file data
     */
    public void distributorOperations(final Input input) {

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
