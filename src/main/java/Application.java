import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class Application {
    final static ObjectMapper uncheckedObjectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, URISyntaxException, ExecutionException, InterruptedException {


        HttpRequest request = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/todos/1"))
                .header("Accept", "application/json")

                .build();

        Model model = HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return uncheckedObjectMapper.readValue(body, Model.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .get();
        System.out.println(model);

    }

}
