package com.sde.project.fileservice.models.responses;

public record FileApiErrorResponse (Error error) {
    public record Error(String message, String code, Details details) {
        public record Details(String triggeredBy, String submittedValue) {}
    }
    public record Details(String triggeredBy, String submittedValue) {}
}
