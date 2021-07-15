import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class ApacheHttpClientTest {

    private HttpClient httpClient;

    @Before
    public void initClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        httpClient = HttpClients
                .custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    @Test
    public void apacheHttpClient455Test() throws IOException {
        executeRequestAndVerifyStatusIsOk("https://expired.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://wrong.host.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://self-signed.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://untrusted-root.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://revoked.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://pinning-test.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://sha1-intermediate.badssl.com");
    }

    private void executeRequestAndVerifyStatusIsOk(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url);

        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(responseString);
        int statusCode = response.getStatusLine().getStatusCode();

        assert statusCode == 200;
    }
}