package com.springboot.blog.springboot_blog_rest_api.implementations;

import com.springboot.blog.springboot_blog_rest_api.services.OllamaService;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class OllamaServiceImpl implements OllamaService {

    private final Dotenv dotenv = Dotenv.load();

    private String OLLAMA_API_URL;
    private String MODEL_NAME;

    private final RestTemplate restTemplate;

    public OllamaServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.OLLAMA_API_URL = dotenv.get("OLLAMA_API_URL");
        this.MODEL_NAME = dotenv.get("OLLAMA_MODEL_NAME");
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
