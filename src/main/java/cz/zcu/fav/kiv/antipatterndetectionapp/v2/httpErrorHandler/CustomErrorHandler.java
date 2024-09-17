package cz.zcu.fav.kiv.antipatterndetectionapp.v2.httpErrorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Jiri Trefil
 * Custom HTTP error handler for RestTemplate class
 *
 */
public class CustomErrorHandler extends DefaultResponseErrorHandler {
    /**
     *
     * Override default custom behavior of Response Handler to be able to read body of error responses
     * because default error handle can only parse body of generic errors.
     * @param response Http response containing error code
     * @throws IOException if file descriptor (socket) is no longer valid in the response
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            final InputStream inputStream = response.getBody();
            byte[] responseBody = inputStream.readAllBytes();
            inputStream.close();
            //String responseBody = StreamUtils.copyToString(response.getBody(),StandardCharsets.UTF_8);
            throw new HttpClientErrorException(response.getStatusCode(),response.getStatusText(),responseBody,StandardCharsets.UTF_8);
        } else {
            super.handleError(response);
        }
    }


}
