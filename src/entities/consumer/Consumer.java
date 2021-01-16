package entities.consumer;

public interface Consumer {
    /**
     * Method that inserts data in consumer
     */
    void insertData(long id, long budget,  long monthlyIncome);
}
