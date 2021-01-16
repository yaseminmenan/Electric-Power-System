package input;

import entities.consumer.ConsumerFactory;
import entities.consumer.InputConsumer;
import entities.distributor.DistributorFactory;
import entities.distributor.InputDistributor;
import entities.EnergyType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import common.Constants;
import common.Update;
import entities.producer.InputProducer;
import entities.producer.ProducerFactory;

/**
 * The class reads and parses the data from the tests
 */
public final class InputLoader {
    /**
     * The path to the input file
     */
    private final String inputPath;

    public InputLoader(final String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * Read the data from input file
     * @return an Input object
     */
    public Input readData() {
        JSONParser jsonParser = new JSONParser();

        // Initialise input data
        long numberOfTurns = 0;
        List<Update> monthlyUpdates = new ArrayList<>();
        List<InputConsumer> consumers = new ArrayList<>();
        List<InputDistributor> distributors = new ArrayList<>();
        List<InputProducer> producers = new ArrayList<>();

        ConsumerFactory consumerFactory = ConsumerFactory.getInstance();
        DistributorFactory distributorFactory = DistributorFactory.getInstance();
        ProducerFactory producerFactory = ProducerFactory.getInstance();

        try {
            // Parsing the contents of the JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));

            // Get the number of turns
            numberOfTurns = (long) jsonObject.get(Constants.NUMBEROFTURNS);

            JSONObject jsonInitialData = (JSONObject) jsonObject.get(Constants.INITIALDATA);
            JSONArray jsonConsumers = (JSONArray) jsonInitialData.get(Constants.CONSUMERS);
            JSONArray jsonDistributors = (JSONArray) jsonInitialData.get(Constants.DISTRIBUTORS);
            JSONArray jsonProducers = (JSONArray) jsonInitialData.get(Constants.PRODUCERS);
            JSONArray jsonMonthlyUpdates = (JSONArray) jsonObject.get(Constants.MONTHLYUPDATES);

            // Create list of consumers
            if (jsonConsumers != null) {
                readConsumerData(consumers, jsonConsumers, consumerFactory);
            }

            // Create list of distributors
            if (jsonDistributors != null) {
                readDistributorData(distributors, jsonDistributors, distributorFactory);
            }

            // Create list of producers
            if (jsonProducers != null) {
                 readProducerData(producers, jsonProducers, producerFactory);
            }

            // Create list of monthly updates
            if (jsonMonthlyUpdates != null) {
                for (Object jsonMonthlyUpdate : jsonMonthlyUpdates) {
                    // Initialise list of new consumers for the update
                    List<InputConsumer> newConsumers = new ArrayList<>();
                    // Initialise list of distributor costs changes for the update
                    List<InputDistributor> distributorChanges = new ArrayList<>();

                    List<InputProducer> producerChanges = new ArrayList<>();

                    JSONArray jsonNewConsumers =
                          (JSONArray) ((JSONObject) jsonMonthlyUpdate).get(Constants.NEWCONSUMERS);
                    // Create list of new consumers
                    if (jsonNewConsumers != null) {
                        readConsumerData(newConsumers, jsonNewConsumers, consumerFactory);
                    } else {
                        newConsumers = null;
                    }

                    JSONArray jsonDistributorChanges =
                          (JSONArray) ((JSONObject) jsonMonthlyUpdate).get(Constants.DISTRCHANGES);
                    // Create list of distributor costs changes
                    if (jsonDistributorChanges != null) {
                        readDistributorChangesData(distributorChanges, jsonDistributorChanges,
                                distributorFactory);
                    } else {
                        distributorChanges = null;
                    }

                    JSONArray jsonProducerChanges = (JSONArray) ((JSONObject)
                                    jsonMonthlyUpdate).get(Constants.PRODUCERCHANGES);

                    if (jsonProducerChanges != null) {
                        readProducerChangesData(producerChanges, jsonProducerChanges, producerFactory);
                    } else {
                        producerChanges = null;
                    }

                    // Create an update
                    Update update = new Update(newConsumers, distributorChanges, producerChanges);

                    // Add update to list of monthly updates
                    monthlyUpdates.add(update);

                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        // Create and return input
       return new Input(numberOfTurns, consumers, distributors, producers, monthlyUpdates);
    }

    /**
     * Reads consumers from file and adds them to the list
     */
    public void readConsumerData(final List<InputConsumer> consumers,
                                 final JSONArray jsonConsumers,
                                 final ConsumerFactory consumerFactory) {
        for (Object jsonConsumer : jsonConsumers) {
            // Create consumer
            InputConsumer consumer = consumerFactory.createEntity();
            // Insert data to consumer
            consumer.insertData(
                    (long) ((JSONObject) jsonConsumer).get(Constants.ID),
                    (long) ((JSONObject) jsonConsumer).get(Constants.INITIALBUDGET),
                    (long) ((JSONObject) jsonConsumer).get(Constants.MONTHLYINCOME));
            // Add to list of consumers
            consumers.add(consumer);
        }
    }

    /**
     * Reads distributors from file and adds them to the list
     */
    public void readDistributorData(final List<InputDistributor> distributors,
                                    final JSONArray jsonDistributors,
                                    final DistributorFactory distributorFactory) {
        for (Object jsonDistributor : jsonDistributors) {
            // Create distributor
            InputDistributor distributor = distributorFactory.createEntity();
            // Insert data to distributor
            distributor.insertData(
                    (long) ((JSONObject) jsonDistributor).get(Constants.ID),
                    (long) ((JSONObject) jsonDistributor).get(Constants.CONTRACTLENGTH),
                    (long) ((JSONObject) jsonDistributor).get(Constants.INITIALBUDGET),
                    (long) ((JSONObject) jsonDistributor).get(Constants.INITIALINFRCOST),
                    (long) ((JSONObject) jsonDistributor).get(Constants.ENERGYNEEDEDKW),
                    (String) ((JSONObject) jsonDistributor).get(Constants.PRODUCERSTRATEGY));
            // Add distributor to list
            distributors.add(distributor);
        }
    }

    /**
     * Reads distributor costs changes from file and adds them to the list
     */
    public void readDistributorChangesData(final List<InputDistributor> distributorChanges,
                                           final JSONArray jsonDistributorChanges,
                                           final DistributorFactory distributorFactory) {
        for (Object jsonCostChanges : jsonDistributorChanges) {
            // Create distributor
            InputDistributor distributor = distributorFactory.createEntity();
            // Insert data to distributor
            distributor.insertData(
                    (long) ((JSONObject) jsonCostChanges).get(Constants.ID), 0,
                    0, (long) ((JSONObject) jsonCostChanges).get(Constants.INFRCOST),
                    0, "NONE");
            // Add to list of distributor changes
            distributorChanges.add(distributor);
        }
    }

    /**
     * Reads producers from file and adds them to the list
     */
    public void readProducerData(final List<InputProducer> producers,
                                 final JSONArray jsonProducers,
                                 final ProducerFactory producerFactory) {
        for (Object jsonProducer : jsonProducers) {
            // Create producer
            InputProducer producer = producerFactory.createEntity();
            // Insert data to producer
            producer.insertData((long) ((JSONObject) jsonProducer).get(Constants.ID),
                    EnergyType.valueOf(
                            (String) ((JSONObject) jsonProducer).get(Constants.ENERGYTYPE)),
                    (long) ((JSONObject) jsonProducer).get(Constants.MAXDISTRIBUTORS),
                    (double) ((JSONObject) jsonProducer).get(Constants.PRICEKW),
                    (long) ((JSONObject) jsonProducer).get(Constants.ENERGYPERDISTR));
            // Add to list of producers
            producers.add(producer);
        }
    }

    /**
     * Reads producer energy quantity changes from file and adds them to the list
     */
    public void readProducerChangesData(final List<InputProducer> producerChanges,
                                        final JSONArray jsonProducerChanges,
                                        final ProducerFactory producerFactory) {
        for (Object jsonProducer : jsonProducerChanges) {
            // Create producer
            InputProducer producer = producerFactory.createEntity();
            // Insert data
            producer.insertData(
                    (long) ((JSONObject) jsonProducer).get(Constants.ID),
                    EnergyType.valueOf("NONE"), 0, 0,
                    (long) ((JSONObject) jsonProducer).get(Constants.ENERGYPERDISTR));
            // Add to list of producer changes
            producerChanges.add(producer);
        }
    }
}
