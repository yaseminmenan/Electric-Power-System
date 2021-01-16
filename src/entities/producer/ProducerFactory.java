package entities.producer;

public final class ProducerFactory {
        private static ProducerFactory instance;

        private ProducerFactory() {
        }

        /**
         * Get the factory instance
         */
        public static ProducerFactory getInstance() {
            if (instance == null) {
                instance = new ProducerFactory();
            }
            return instance;
        }

        /**
         * Create an entity with the given type
         */
        public static InputProducer createEntity() {
            return new InputProducer();
        }
}
