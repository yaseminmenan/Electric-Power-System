package consumer;

public interface Consumer {
    /**
     * Method that inserts data in consumer
     */
    void insertInitialData(long id, long budget,  long monthlyIncome);
}
