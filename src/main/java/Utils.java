import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class Utils {
    public static final ObjectMapper mapper = new ObjectMapper();
    static CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(30000)
                    .setRedirectsEnabled(false)
                    .build())
            .build();

    public static String getURL(String url) throws IOException {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        Nasa nasa = mapper.readValue(response.getEntity().getContent(), Nasa.class);
        return nasa.getUrl();
    }

    public static String getName(String url) throws IOException {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        Nasa nasa = mapper.readValue(response.getEntity().getContent(), Nasa.class);
        return nasa.getCopyright();
    }
}

