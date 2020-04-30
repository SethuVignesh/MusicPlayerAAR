package com.example.thales.designpfp.com.example.mpa;

public class CardProfile {
    @SerializedName("name")
    private String name;
    @SerializedName("cardNo")
    private String cardNo;

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getName() {
        return name;
    }
}
