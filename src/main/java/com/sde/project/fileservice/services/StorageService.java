package com.sde.project.fileservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sde.project.fileservice.models.responses.FileApiErrorResponse;
import com.sde.project.fileservice.models.responses.FileApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetailsNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class StorageService {
    @Value("${storage.api.key}")
    private String apiKey;

    @Value("${account.id}")
    private String accountId;
    private final String basePath = "https://api.upload.io";


    public FileApiResponse uploadFile(MultipartFile file){
        String url = basePath + "/v2/accounts/" + accountId+ "/uploads/binary";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);

        RequestEntity<?> requestEntity = new RequestEntity<>(file.getResource(), headers, HttpMethod.POST, URI.create(url));

        try {
            ResponseEntity<FileApiResponse> response = restTemplate.exchange(requestEntity, FileApiResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException.BadRequest e) {
            FileApiErrorResponse errorResponse = e.getResponseBodyAs(FileApiErrorResponse.class);
            throw new RuntimeException(errorResponse.error().message());
        }
    }
}
