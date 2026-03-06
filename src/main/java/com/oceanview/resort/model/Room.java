package com.oceanview.resort.model;

public class Room {
  private Long id;
  private String roomNumber;
  private Long roomTypeId;
  private String status;
  private Integer floor;

  public Room() {
  }

  public Room(Long id, String roomNumber, Long roomTypeId, String status, Integer floor) {
    this.id = id;
    this.roomNumber = roomNumber;
    this.roomTypeId = roomTypeId;
    this.status = status;
    this.floor = floor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRoomNumber() {
    return roomNumber;
  }

  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
  }

  public Long getRoomTypeId() {
    return roomTypeId;
  }

  public void setRoomTypeId(Long roomTypeId) {
    this.roomTypeId = roomTypeId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getFloor() {
    return floor;
  }

  public void setFloor(Integer floor) {
    this.floor = floor;
  }
}
