package strategies;


import static strategies.EnergyChoiceStrategyType.*;

public class EnergyStrategyFactory {
    public EnergyStrategy createStrategy(EnergyChoiceStrategyType strategyType) {
        if(strategyType.equals(GREEN)) {
            return new GreenStrategy();
        } else if(strategyType.equals(PRICE)) {
            return new PriceStrategy();
        } else if(strategyType.equals(QUANTITY)) {
            return new QuantityStrategy();
        }
        return null;
    }
}
