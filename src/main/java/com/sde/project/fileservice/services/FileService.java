package com.sde.project.fileservice.services;

import com.sde.project.fileservice.models.responses.FileApiResponse;
import com.sde.project.fileservice.models.tables.File;
import com.sde.project.fileservice.repositories.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;

    private final StorageService storageService;

    public FileService(FileRepository fileRepository, StorageService storageService) {
        this.fileRepository = fileRepository;
        this.storageService = storageService;
    }

    public void uploadFile(MultipartFile file, String sessionId) {
        FileApiResponse fileApiResponse = storageService.uploadFile(file);

        fileRepository.save(new File(
                file.getOriginalFilename(),
                fileApiResponse.fileUrl(),
                UUID.fromString(sessionId)));
    }
}
