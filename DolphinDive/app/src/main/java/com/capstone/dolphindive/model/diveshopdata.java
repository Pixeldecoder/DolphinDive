package com.capstone.dolphindive.model;

public class diveshopdata {

    private String placeName;
    private String countryName;
    private String price;
    private String rate;
    private String popular;
    private Integer imageUrl;



    public diveshopdata(String placeName, String countryName, String price, String rate, String popular,Integer imageUrl) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rate = rate;
        this.popular=popular;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }
    public String getPlaceName() {
        return placeName;
    }

    public String getCountryName() {
        return countryName;
    }


    public String getPrice() {
        return price;
    }

    public String getRate() { return rate;}
    public String getPopular() { return popular;}

}
