package distributor;

public interface Distributor {
   /**
    * Method that inserts data in distributor
    */
   void insertInitialData(long id, long contractLength, long budget, long infrastructureCost,
                          long energyNeededKW, String producerStrategy);
}
