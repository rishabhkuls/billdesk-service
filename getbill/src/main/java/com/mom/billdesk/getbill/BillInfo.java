package com.mom.billdesk.getbill;

import org.json.JSONObject;

public class BillInfo {
    String amount;   //without taxes and discount

    boolean isTaxEnabled = false;
    String taxes;

    boolean isDiscountEnabled = false;
    String discount;

    String total;  //with taxes and discount

    BillInfo(String billDetails){
        JSONObject reqObj = new JSONObject(billDetails);
        this.amount = ""+reqObj.get("amount");
        this.isTaxEnabled = (boolean) reqObj.get("isTaxEnabled");
        this.isDiscountEnabled = (boolean) reqObj.get("isDiscountEnabled");
        this.taxes = ""+reqObj.get("taxes");
        this.discount = ""+reqObj.get("discount");
        this.total = ""+reqObj.get("total");
    }

}
