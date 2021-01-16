package entities.distributor;

public final class DistributorFactory {
    private static DistributorFactory instance;

    private DistributorFactory() {
    }

    /**
     * Get the factory instance
     */
    public static DistributorFactory getInstance() {
        if (instance == null) {
            instance = new DistributorFactory();
        }
        return instance;
    }

    /**
     * Create an entity with the given type
     */
    public static InputDistributor createEntity() {
        return new InputDistributor();
    }
}
