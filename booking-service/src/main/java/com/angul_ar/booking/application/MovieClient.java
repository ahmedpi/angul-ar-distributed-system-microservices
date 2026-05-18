package com.angul_ar.booking.application;

import com.angul_ar.booking.adapters.web.dto.MovieDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MovieClient {

  private final WebClient webClient;

  public MovieClient(@Value("${movie.service.url}") String baseUrl, WebClient.Builder builder) {
    this.webClient = builder.baseUrl(baseUrl).build();
  }

//  public MovieClient(WebClient.Builder builder) {
//    this.webClient = builder.baseUrl("http://movie-service").build();
//  }

  public MovieDto getMovie(Long id) {
    return webClient.get()
        .uri("/movies/{id}", id)
        .retrieve()
        .bodyToMono(MovieDto.class)
        .block();
  }
}
