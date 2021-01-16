package strategies;
import static strategies.EnergyChoiceStrategyType.GREEN;
import static strategies.EnergyChoiceStrategyType.PRICE;
import static strategies.EnergyChoiceStrategyType.QUANTITY;

public class EnergyStrategyFactory {
    /**
     * Create energy strategy by type
     */
    public EnergyStrategy createStrategy(EnergyChoiceStrategyType strategyType) {
        if (strategyType.equals(GREEN)) {
            return new GreenStrategy();
        } else if (strategyType.equals(PRICE)) {
            return new PriceStrategy();
        } else if (strategyType.equals(QUANTITY)) {
            return new QuantityStrategy();
        }
        return null;
    }
}
