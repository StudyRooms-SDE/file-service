package com.sde.project.fileservice.models.responses;

public record FileApiErrorResponse (Error error) {
    public static record Error(String message, String code, Details details) {
        public static record Details(String triggeredBy, String submittedValue) {}
    }
    public static record Details(String triggeredBy, String submittedValue) {}
}
