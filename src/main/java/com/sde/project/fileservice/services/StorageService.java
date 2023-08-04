package com.sde.project.fileservice.services;

import com.sde.project.fileservice.models.responses.FileApiErrorResponse;
import com.sde.project.fileservice.models.responses.FileApiResponse;
import com.sde.project.fileservice.models.tables.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetailsNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;

@Service
public class StorageService {
    @Value("${storage.api.key}")
    private String apiKey;

    @Value("${account.id}")
    private String accountId;


    public FileApiResponse uploadFile(MultipartFile file){
        if (Objects.requireNonNull(file.getOriginalFilename()).contains("/")){
            throw new IllegalArgumentException("File name cannot contain /");
        }
        String url = "https://api.upload.io/v2/accounts/" + accountId+ "/uploads/binary?folderPath=/uploads&fileName=" + file.getOriginalFilename();
        RestTemplate restTemplate = new RestTemplate();

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
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
        return response.getBody();


    }
}
