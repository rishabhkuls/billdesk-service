package com.mom.billdesk.getbill;

import org.json.JSONObject;

public class CustomerInfo {
    String custName;
    String custAdd1;
    String custAdd2;
    String custAdd3;

    CustomerInfo(String custDetails){
        JSONObject reqObj = new JSONObject(custDetails);
        this.custName = ""+reqObj.get("custName");
        this.custAdd1 = ""+reqObj.get("addLine1");
        this.custAdd2 = ""+reqObj.get("addLine2");
        this.custAdd3 = ""+reqObj.get("addLine3");
    }
}
