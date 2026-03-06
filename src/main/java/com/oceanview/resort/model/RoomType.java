package com.oceanview.resort.model;

import java.math.BigDecimal;

public class RoomType {
  private Long id;
  private String name;
  private BigDecimal basePrice;
  private Integer capacity;
  private String description;

  public RoomType() {
  }

  public RoomType(Long id, String name, BigDecimal basePrice, Integer capacity, String description) {
    this.id = id;
    this.name = name;
    this.basePrice = basePrice;
    this.capacity = capacity;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(BigDecimal basePrice) {
    this.basePrice = basePrice;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
