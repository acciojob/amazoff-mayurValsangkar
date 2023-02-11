package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order() {
    }

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        String hr = deliveryTime.substring(0, 2);
        String min = deliveryTime.substring(3);
        int H = Integer.parseInt(hr);
        int M = Integer.parseInt(min);
        this.deliveryTime = (H*60) + M;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}

    public void setId(String id) {
        this.id = id;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
