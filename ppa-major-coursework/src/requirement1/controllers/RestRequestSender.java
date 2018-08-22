package requirement1.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

/**
 * Used to send REST requests
 */
public class RestRequestSender {

    private static String URI_PREFIX = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=ufo%20sightings&key=AIzaSyB2CiyupZX4lRcR1tmW58Ow7ML3Tc5Vmp0";

    /**
     * Retrieves the number of videos from youtube with text "ufo sightings" in a specific range
     * @param yearFrom begin range
     * @param yearTo end range
     * @return the number of videos found
     */
	public static Integer getNumberOfUFOSigthingsYoutube(int yearFrom, int yearTo) {

	    try {
	        String yearFromEnc = yearFrom + "-01-01T00:00:00Z";
	        String yearToEnc = yearTo + "-12-31T23:59:59Z";
            String newUrl = RestRequestSender.URI_PREFIX + "&publishedAfter=" + yearFromEnc + "&publishedBefore=" + yearToEnc;

            String response = sendGet(newUrl);
            JSONObject obj = new JSONObject(response);
            return obj.getJSONObject("pageInfo").getInt("totalResults");
        }
        catch(Exception ex) {
	        ex.printStackTrace();
	        return 0;
        }
    }

    /**
     * Sends a generic GET request to a provided url
     * @param url url to send GET
     * @return the response in String format
     * @throws Exception
     */
    private static String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        System.out.println("\nSending 'GET' request to URL : " + url + "\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }
}
