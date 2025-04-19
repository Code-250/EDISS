package customerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * StatusController handles HTTP requests related to system status.
 * It provides an endpoint to check the health of the application.
 */
@RestController
@RequestMapping("/status")
@Tag(name="Customer Service API Status", description = "Get service status")
public class StatusController {

    /**
     * Returns the status of the application.
     *
     * @return ResponseEntity with a plain text message indicating the status.
     */
    @Operation(
            summary = "Get service status",
            description = "Check if the Customer service is up and running"
    )
    @ApiResponse(responseCode = "200", description = "Service is up")
    @GetMapping
    public ResponseEntity<String> getStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<>("OK", headers, HttpStatus.OK);
    }
}
