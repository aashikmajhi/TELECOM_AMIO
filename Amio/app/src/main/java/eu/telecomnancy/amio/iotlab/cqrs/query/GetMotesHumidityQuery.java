package eu.telecomnancy.amio.iotlab.cqrs.query;

/**
 * Query to get the humidity of all motes within the desired range
 */
public class GetMotesHumidityQuery extends MotesQuery {

    /**
     * Default constructor
     * @param numberOfQueriedMotes Number of queried motes, should be more than 0
     */
    public GetMotesHumidityQuery(int numberOfQueriedMotes) {
        super(numberOfQueriedMotes);
    }

}
