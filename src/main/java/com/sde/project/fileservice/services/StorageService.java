package com.sde.project.fileservice.services;

import com.sde.project.fileservice.models.responses.FileApiErrorResponse;
import com.sde.project.fileservice.models.responses.FileApiResponse;
import com.sde.project.fileservice.models.tables.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetailsNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StorageService {
    @Value("${storage.api.key}")
    private String apiKey;

    @Value("${account.id}")
    private String accountId;

    private final RestTemplate restTemplate;

    public StorageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public FileApiResponse uploadFile(MultipartFile file){
        if (Objects.requireNonNull(file.getOriginalFilename()).contains("/")){
            throw new IllegalArgumentException("File name cannot contain /");
        }
        String url = "https://api.upload.io/v2/accounts/" + accountId+ "/uploads/binary?folderPath=/uploads&fileName=" + file.getOriginalFilename();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);

        RequestEntity<?> requestEntity = new RequestEntity<>(file.getResource(), headers, HttpMethod.POST, URI.create(url));

        try {
            ResponseEntity<FileApiResponse> response = restTemplate.exchange(requestEntity, FileApiResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException.BadRequest e) {
            FileApiErrorResponse errorResponse = e.getResponseBodyAs(FileApiErrorResponse.class);
            throw new ConnectionDetailsNotFoundException(errorResponse != null ? errorResponse.error().message() : "File upload failed");
        }
    }

    public byte[] downloadFile(File file) {
        String url = String.format("https://upcdn.io/%s/raw%s", accountId, file.getPath());

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
        return response.getBody();


    }

    public void deleteFile(File file) {
        String url = String.format("https://api.upload.io/v2/accounts/%s/files/batch", accountId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);
        headers.add("Content-Type", "application/json");

        Map<String, List<String>> body = Map.of("files", List.of(file.getPath()));
        HttpEntity<?> request = new HttpEntity<>( body, headers);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, request, Object.class);
        } catch (HttpClientErrorException.BadRequest e) {
            FileApiErrorResponse errorResponse = e.getResponseBodyAs(FileApiErrorResponse.class);
            throw new ConnectionDetailsNotFoundException(errorResponse != null ? errorResponse.error().message() : "File deletion failed");
        }

    }
}
