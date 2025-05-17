package ka15err.gtpapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController()
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class MainController {

    @Value("${gpt.api-url}")
    private String gptApiUrl;

    @Value("${gpt.api-key}")
    private String gptApiKey;

    private final RestTemplate restTemplate;

    public MainController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @PostMapping("/gpt")
    public ResponseEntity<?> proxyRequest(@RequestBody Map<String, Object> request) {

        System.out.println("Fetching Open AI");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + gptApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(gptApiUrl, entity, String.class);

            JsonObject json = JsonParser.parseString(response.getBody()).getAsJsonObject();

            return ResponseEntity.ok(json.get("output").getAsJsonArray().get(0).getAsJsonObject().get("content").getAsJsonArray().get(0).getAsJsonObject().toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при отправке запроса: " + e.getMessage());
        }
    }
}


