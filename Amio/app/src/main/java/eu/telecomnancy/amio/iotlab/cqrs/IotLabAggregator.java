package eu.telecomnancy.amio.iotlab.cqrs;

import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.security.InvalidParameterException;

import eu.telecomnancy.amio.R;
import eu.telecomnancy.amio.iotlab.Constants;
import eu.telecomnancy.amio.iotlab.cqrs.query.IQuery;
import eu.telecomnancy.amio.iotlab.cqrs.query.mote.GetMotesDataTypeQuery;
import eu.telecomnancy.amio.iotlab.dto.MoteDtoCollection;
import eu.telecomnancy.amio.polling.contexts.PollingContext;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * IotLab CQRS aggregator to handle command and queries
 *
 * @see IQuery
 */
public class IotLabAggregator {

    private static String TAG = "IotLabAggregator";

    /**
     * Inner HttpClient used for HTTP requests
     */
    private final OkHttpClient _httpClient = new OkHttpClient();

    /**
     * (Pure) Generate the API route to fetch all data relative to the provided label
     * @param label Type of data to be fetched
     * @return The associated API route
     */
    private static String generateRouteForLabel(String label, PollingContext context) {
        String baseUrl = PreferenceManager
                .getDefaultSharedPreferences(context.androidContext)
                .getString(context.androidContext.getResources().getString(R.string.iot_lab_adress), null);
        return baseUrl + Constants.Urls.API + "/" + label + "/last";
    }

    /**
     * Handle the provided IQuery and execute it
     * @param query IQuery to handle
     * @return The MoteDtoCollection holding all requested data according to the provided query
     * @throws IOException If any issue happened with the HTTP call
     * @throws InvalidParameterException If the query can't be handled by the aggregator
     */
    public MoteDtoCollection handle(IQuery query, PollingContext context)
            throws IOException, InvalidParameterException {
        // Flag to check whether or not this query is forged to request a specific value from all
        // motes
        boolean isMoteDataTypeQuery = query instanceof GetMotesDataTypeQuery;

        // This aggregator is only fetching mote data, all other types are not handled by it
        if (!isMoteDataTypeQuery) {
            throw new InvalidParameterException(
                    "Unhandled query of type " + query.getClass().getTypeName());
        }

        // Cast the query to access the data type's label
        GetMotesDataTypeQuery getMotesDataTypeQuery = (GetMotesDataTypeQuery) query;

        // Generate the associated route from the query's label
        String associatedRoute = generateRouteForLabel(getMotesDataTypeQuery.label, context);

        // Return the fetched data from the route
        return fetchAndRetrieveMoteCollectionFromUrl(associatedRoute);
    }

    /**
     * Performs the HTTP Call to retrieve a mote collection
     * @param queryUrl Url to be queried
     * @return A mote collection with its object mapped to the JSON response of the request
     * @throws IOException If any issue happened with the HTTP call
     */
    private MoteDtoCollection fetchAndRetrieveMoteCollectionFromUrl(String queryUrl)
            throws IOException {
        // Build the HTTP Request based on the query
        MoteDtoCollection moteDtoCollection;
        try {
            Request request = new Request.Builder()
                    .url(queryUrl)
                    .build();

            // Send the HTTP GET request to the API
            Response response = _httpClient
                    .newCall(request)
                    .execute();

            // Retrieve the payload of the request
            ResponseBody payload = response.body();

            // If any data is fetched, deserialize it into the appropriate object


            moteDtoCollection = (payload != null)
                    ? new Gson().fromJson(payload.string(), MoteDtoCollection.class)
                    : new MoteDtoCollection();
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Unable to parse recieved data");
            moteDtoCollection = new MoteDtoCollection();
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Malformed Url");
            moteDtoCollection = new MoteDtoCollection();
        }
        return moteDtoCollection;
    }

}
