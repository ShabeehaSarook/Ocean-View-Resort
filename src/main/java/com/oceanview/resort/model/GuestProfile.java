package com.oceanview.resort.model;

public class GuestProfile {
  private Long id;
  private Long userId;
  private String fullName;
  private String email;
  private String phone;
  private String nicPassport;
  private String address;

  public GuestProfile() {
  }

  public GuestProfile(Long id, Long userId, String fullName, String email, String phone,
                      String nicPassport, String address) {
    this.id = id;
    this.userId = userId;
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.nicPassport = nicPassport;
    this.address = address;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getNicPassport() {
    return nicPassport;
  }

  public void setNicPassport(String nicPassport) {
    this.nicPassport = nicPassport;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
