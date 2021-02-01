package com.dealhub.models;

public class MyCoupens {
    String shopname;
    String count;
    String crrdate;
    String phone;
    double price;

    public MyCoupens() {
    }

    public MyCoupens(String shopname, String count, String crrdate, String phone, double price) {
        this.shopname = shopname;
        this.count = count;
        this.crrdate = crrdate;
        this.phone = phone;
        this.price = price;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCrrdate() {
        return crrdate;
    }

    public void setCrrdate(String crrdate) {
        this.crrdate = crrdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
