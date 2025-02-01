package com.springboot.blog.springboot_blog_rest_api.implementations;

import com.springboot.blog.springboot_blog_rest_api.services.OllamaService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class OllamaServiceImpl implements OllamaService {

    private static final String OLLAMA_API_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "llama2-uncensored:latest"; // Change if using another model

    private final RestTemplate restTemplate;

    public OllamaServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String summarizeText(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL_NAME);
        requestBody.put("prompt", "Briefly Summarize this: " + text);
        requestBody.put("stream", false);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(OLLAMA_API_URL, HttpMethod.POST, requestEntity,
                String.class);

        JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
        return jsonResponse.getString("response");
    }

}
