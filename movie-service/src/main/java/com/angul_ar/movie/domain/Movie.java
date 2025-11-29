package com.angul_ar.movie.domain;

public class Movie {
  private Long id;
  private String title;
  private String genre;
  private int duration; // in minutes

  public Movie(Long id, String title, String genre, int duration) {
    this.id = id;
    this.title = title;
    this.genre = genre;
    this.duration = duration;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}