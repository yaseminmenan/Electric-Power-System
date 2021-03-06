package strategies;

/**
 * Strategy types for distributors to choose their producers
 */
public enum EnergyChoiceStrategyType {
    NONE("NONE"),
    GREEN("GREEN"),
    PRICE("PRICE"),
    QUANTITY("QUANTITY");

    private final String label;

    EnergyChoiceStrategyType(String label) {
        this.label = label;
    }
}
