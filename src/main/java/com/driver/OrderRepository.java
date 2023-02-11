package com.driver;

import java.util.*;

public class OrderRepository {

    private HashMap<String, Order> orderDb;
    private HashMap<String, DeliveryPartner> partnerDb;
    private HashMap<String, String> orderPartnerDb;
    private HashMap<String, Set<String>> partnerOrderDb;

    public OrderRepository(){

        this.orderDb = new HashMap<>();
        this.partnerDb = new HashMap<>();
        this.orderPartnerDb = new HashMap<>();
        this.partnerOrderDb = new HashMap<>();

    }

    public void addOrder(Order order){

        orderDb.put(order.getId(), order);
    }


    public void addPartner(String partnerId){

        partnerDb.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId){

        //This is basically assigning that order to that partnerId
        Set<String> orders = new HashSet<>();

        if(partnerOrderDb.containsKey(partnerId)){
            orders = partnerOrderDb.get(partnerId);
        }

        orders.add(orderId);
        partnerOrderDb.put(partnerId, orders);
        DeliveryPartner partner = partnerDb.get(partnerId);
        partner.setNumberOfOrders(orders.size());
        orderPartnerDb.put(orderId, partnerId);
    }


    public Order getOrderById(String orderId){
        return orderDb.get(orderId);
    }


    public DeliveryPartner getPartnerById(String partnerId){
        return partnerDb.get(partnerId);
    }


    public Integer getOrderCountByPartnerId(String partnerId){

        Integer orderCount = 0;
        //orderCount should denote the orders given by a partner-id
        if(partnerOrderDb.containsKey(partnerId)){
            orderCount = partnerOrderDb.get(partnerId).size();
        }

        return orderCount;
    }


    public List<String> getOrdersByPartnerId(String partnerId){

        Set<String> orders = new HashSet<>();

        //orders should contain a list of orders by PartnerId
        if(partnerOrderDb.containsKey(partnerId)){
            orders = partnerOrderDb.get(partnerId);
        }

        return new ArrayList<>(orders);
    }


    public List<String> getAllOrders(){
        return new ArrayList<>(orderDb.keySet());
    }


    public Integer getCountOfUnassignedOrders(){
        Integer countOfOrders = 0;

        //Count of orders that have not been assigned to any DeliveryPartner
        List<String> orderList = new ArrayList<>(orderDb.keySet());
        for(String oderId : orderList){
            if(!orderPartnerDb.containsKey(oderId)){
                countOfOrders++;
            }
        }

        return countOfOrders;
    }


    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){

        Integer countOfOrders = 0;

        //countOfOrders that are left after a particular time of a DeliveryPartner
        int H = Integer.parseInt(time.substring(0, 2));
        int M = Integer.parseInt(time.substring(3));
        int T = H*60 + M;

        if(partnerOrderDb.containsKey(partnerId)){
            Set<String> orderSet = partnerOrderDb.get(partnerId);

            for(String order : orderSet){
                if(orderDb.containsKey(order)){
                    if(orderDb.get(order).getDeliveryTime() > T){
                        countOfOrders++;
                    }
                }
            }
        }

        return countOfOrders;
    }


    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String time = null;

        //Return the time when that partnerId will deliver his last delivery order.
        int T = 0;
        if(partnerOrderDb.containsKey(partnerId)){
            Set<String> orderSet = partnerOrderDb.get(partnerId);

            for(String order: orderSet){
                if(orderDb.containsKey(order)){
                    Order cuurentOrder = orderDb.get(order);
                    T = Math.max(T, cuurentOrder.getDeliveryTime());
                }
            }
        }

        int H = T/60;
        int M = T%60;

        String hour = String.valueOf(H);
        String minutes = String.valueOf(M);

        if(hour.length()==1){
            hour = "0" + hour;
        }

        if(minutes.length()==1){
            minutes = "0" + minutes;
        }

        return hour+":"+minutes;
    }

    public void deletePartnerById(String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        Set<String> orderSet = new HashSet<>();
        if(partnerOrderDb.containsKey(partnerId)){
            orderSet = partnerOrderDb.get(partnerId);

            for(String order : orderSet){
                if(orderPartnerDb.containsKey(order)){
                    orderPartnerDb.remove(order);
                }
            }

            partnerOrderDb.remove(partnerId);
        }

        if(partnerDb.containsKey(partnerId)){
            partnerDb.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
       if(orderPartnerDb.containsKey(orderId)){
           String partnerId = orderPartnerDb.get(orderId);

           Set<String> orderSet = partnerOrderDb.get(partnerId);
           orderSet.remove(orderId);
           partnerOrderDb.put(partnerId, orderSet);

           // Reset partner's order number
           DeliveryPartner partner = partnerDb.get(partnerId);
           partner.setNumberOfOrders(orderSet.size());
       }

       if(orderDb.containsKey(orderId)){
           orderDb.remove(orderId);
       }
    }
}
