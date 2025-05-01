package com.bookstore.webbff.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponseDto {

    @JsonProperty("ISBN")
    private String isbn;

    @JsonProperty("AUthor")
    private String authors;

    private String title;
}
