package consumer;

public final class ConsumerFactory {
    private static ConsumerFactory instance;

    private ConsumerFactory() {
    }

    /**
     * Get the factory instance
     */
    public static ConsumerFactory getInstance() {
        if (instance == null) {
            instance = new ConsumerFactory();
        }
        return instance;
    }

    /**
     * Create an entity with the given type
     */
    public static InputConsumer createEntity() {
        return new InputConsumer();
    }
}
