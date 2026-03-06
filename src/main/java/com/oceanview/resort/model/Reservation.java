package com.oceanview.resort.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
  private Long id;
  private Long guestId;
  private Long roomId;
  private LocalDate checkIn;
  private LocalDate checkOut;
  private Integer adults;
  private Integer children;
  private String status;
  private Long bookedBy;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Reservation() {
  }

  public Reservation(Long id, Long guestId, Long roomId, LocalDate checkIn, LocalDate checkOut,
                     Integer adults, Integer children, String status, Long bookedBy,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.guestId = guestId;
    this.roomId = roomId;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.adults = adults;
    this.children = children;
    this.status = status;
    this.bookedBy = bookedBy;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGuestId() {
    return guestId;
  }

  public void setGuestId(Long guestId) {
    this.guestId = guestId;
  }

  public Long getRoomId() {
    return roomId;
  }

  public void setRoomId(Long roomId) {
    this.roomId = roomId;
  }

  public LocalDate getCheckIn() {
    return checkIn;
  }

  public void setCheckIn(LocalDate checkIn) {
    this.checkIn = checkIn;
  }

  public LocalDate getCheckOut() {
    return checkOut;
  }

  public void setCheckOut(LocalDate checkOut) {
    this.checkOut = checkOut;
  }

  public Integer getAdults() {
    return adults;
  }

  public void setAdults(Integer adults) {
    this.adults = adults;
  }

  public Integer getChildren() {
    return children;
  }

  public void setChildren(Integer children) {
    this.children = children;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getBookedBy() {
    return bookedBy;
  }

  public void setBookedBy(Long bookedBy) {
    this.bookedBy = bookedBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
