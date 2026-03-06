package com.oceanview.resort.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bill {
  private Long id;
  private Long reservationId;
  private BigDecimal subtotal;
  private BigDecimal tax;
  private BigDecimal discount;
  private BigDecimal total;
  private Long issuedBy;
  private LocalDateTime issuedAt;
  private String paymentStatus;

  public Bill() {
  }

  public Bill(Long id, Long reservationId, BigDecimal subtotal, BigDecimal tax, BigDecimal discount,
              BigDecimal total, Long issuedBy, LocalDateTime issuedAt, String paymentStatus) {
    this.id = id;
    this.reservationId = reservationId;
    this.subtotal = subtotal;
    this.tax = tax;
    this.discount = discount;
    this.total = total;
    this.issuedBy = issuedBy;
    this.issuedAt = issuedAt;
    this.paymentStatus = paymentStatus;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public void setReservationId(Long reservationId) {
    this.reservationId = reservationId;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public Long getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(Long issuedBy) {
    this.issuedBy = issuedBy;
  }

  public LocalDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(LocalDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }
}
