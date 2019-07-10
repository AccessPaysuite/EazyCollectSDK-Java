package uk.co.eazycollect.eazysdk;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;

public class HttpController {

    public HttpClient httpClient() {
        return HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD)
                .build()).build();
    }

}
