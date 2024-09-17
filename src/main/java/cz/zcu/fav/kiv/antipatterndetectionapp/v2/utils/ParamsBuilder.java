package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class ParamsBuilder {

    /**
     * Method creates body of post request
     * ie returns json representation of parameters
     * @param parameters Key: value pair parameters
     * @return String JSON representation
     */
    public static String createPostParams(HashMap<String,Object> parameters) {
        return JSONBuilder.buildJSON(parameters);
    }

    /**
     * Method creates get parameters from parameters
     * @param params parameters
     * @return String GET representation of parameters passed into the function
     * @throws UnsupportedEncodingException if unknown encoding standard is passed as an argument to the function
     */
    public static String createGetParams(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            builder.append("&");
        }

        String resultString = builder.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;

    }




}
