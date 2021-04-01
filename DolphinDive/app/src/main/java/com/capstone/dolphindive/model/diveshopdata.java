package com.capstone.dolphindive.model;

public class diveshopdata {

    private String placeName;
    private String countryName;
    private String price;
    private String rate;
    private String popular;
    private Integer imageUrl;
    private String cellphone;
    private String email;
    private String about;
    private int sizeavail;
    private int roomavail;
    private String address;
    private String policy;



    public diveshopdata(String placeName, String countryName, String price, String rate, String popular,Integer imageUrl, String cellphone, String email, String about, int sizeavail,int roomavail,String address, String policy) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rate = rate;
        this.popular=popular;
        this.cellphone=cellphone;
        this.email=email;
        this.about=about;;
        this.sizeavail=sizeavail;
        this.roomavail=roomavail;
        this.address=address;
        this.policy=policy;
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
    public String getCellphone(){return cellphone;}
    public String getEmail(){return email;}
    public String getAbout(){return about;}
    public Integer getSizeavail(){return sizeavail;}
    public Integer getRoomavail(){return roomavail;}
    public String getAddress(){return address;}
    public String getPolicy(){return policy;}


}
