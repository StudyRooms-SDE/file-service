package com.sde.project.fileservice.models.responses;

import java.util.UUID;

public record FileResponse(UUID id, String name, String url) {

}
