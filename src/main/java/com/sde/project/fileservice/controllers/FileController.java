package com.sde.project.fileservice.controllers;

import com.sde.project.fileservice.models.responses.FileResponse;
import com.sde.project.fileservice.models.tables.File;
import com.sde.project.fileservice.services.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("sessionId") String sessionId) {
        fileService.uploadFile(file, sessionId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<FileResponse> getFiles(@RequestParam("sessionId") String sessionId) {
        List<File> files = fileService.getFiles(sessionId);
        return files.stream()
                .map(file -> new FileResponse(file.getId(), file.getName(), file.getUrl()))
                .toList();
    }

    @GetMapping(path = "/{id}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public byte[] downloadFile(@PathVariable("id") String id) {
        return fileService.downloadFile(id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable("id") String id) {
        fileService.deleteFile(id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSessionFiles(@RequestParam("sessionId") String sessionId) {
        fileService.deleteSessionFiles(UUID.fromString(sessionId));
    }
}
