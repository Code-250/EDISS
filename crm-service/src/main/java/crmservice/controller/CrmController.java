package crmservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "CRM API", description = "Operations related to customer relationship management")
public class CrmController {

    @Operation(
            summary = "Get service status",
            description = "Check if the CRM service is up and running"
    )
    @ApiResponse(responseCode = "200", description = "Service is up")
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("UP");
    }
}