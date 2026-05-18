package com.angul_ar.booking.application;

import com.angul_ar.booking.adapters.web.dto.CinemaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CinemaClient {

  private final WebClient webClient;

  public CinemaClient(@Value("${cinema.service.url}") String baseUrl, WebClient.Builder builder) {
    System.out.println("CinemaClient baseUrl: " + baseUrl);
    this.webClient = builder.baseUrl(baseUrl).build();
  }

//  public CinemaClient(WebClient.Builder builder) {
//    this.webClient = builder.baseUrl("http://cinema-service").build();
//  }

  public CinemaDto getCinema(Long id) {
    System.out.println("Calling /cinemas/" + id);
    return webClient.get()
        .uri("/cinemas/{id}", id)
        .retrieve()
        .bodyToMono(CinemaDto.class)
        .block();
  }

  public Boolean isSeatAvailable(Long cinemaId, Integer seatNumber) {
    System.out.println("Calling /cinemas/" + cinemaId + "/seats/" + seatNumber + "/available");
    return webClient.get()
        .uri("/cinemas/{cinemaId}/seats/{seatNumber}/available", cinemaId, seatNumber)
        .retrieve()
        .bodyToMono(Boolean.class)
        .block();
  }
}
