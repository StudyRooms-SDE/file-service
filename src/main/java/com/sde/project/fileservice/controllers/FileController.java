package com.sde.project.fileservice.controllers;

import com.sde.project.fileservice.models.responses.FileResponse;
import com.sde.project.fileservice.models.tables.File;
import com.sde.project.fileservice.services.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.util.List;

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

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<FileResponse> getFiles(@RequestParam("sessionId") String sessionId) {
        List<File> files = fileService.getFiles(sessionId);
        return files.stream()
                .map(file -> new FileResponse(file.getName(), file.getUrl()))
                .toList();
    }
}
