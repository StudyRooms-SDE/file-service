package com.sde.project.fileservice.models.tables;

import jakarta.persistence.*;

import java.util.UUID;

@Table(name = "files")
@Entity(name = "File")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  UUID id;
    @Column(name = "name")
    private  String name;

    @Column(name = "url", nullable = false)
    private  String url;

    @Column(name = "session_id", nullable = false)
    private  UUID sessionId;


    public File(String name, String url, UUID sessionId) {
        this.name = name;
        this.url = url;
        this.sessionId = sessionId;
    }

    public File() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
