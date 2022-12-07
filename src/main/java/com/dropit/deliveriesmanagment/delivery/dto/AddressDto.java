package com.dropit.deliveriesmanagment.delivery.dto;

public class AddressDto {

    private String country;
    private String street;
    private String postCode;
    private String line1;
    private String line2;

    public AddressDto(String country, String street, String postCode, String line1, String line2) {
        this.country = country;
        this.street = street;
        this.postCode = postCode;
        this.line1 = line1;
        this.line2 = line2;
    }

    public AddressDto() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

}
