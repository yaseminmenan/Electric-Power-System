package turn;

import entities.distributor.InputDistributor;
import input.Input;
import strategies.EnergyStrategyFactory;

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

        InitialTurn initialTurn = new InitialTurn();
        // Do the initial turn
        initialTurn.doTurn(input, factory);

        NormalTurn normalTurn = new NormalTurn();
        // Do the normal turns
        for (int i = 0; i < input.getNumberOfTurns(); i++) {
           normalTurn.doTurn(input, i, factory);

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

        }
    }

}
