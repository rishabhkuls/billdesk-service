package com.mom.billdesk.getbill;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileInputParams {
    String date;
    String billNo;
    CustomerInfo custDetails;
    BillItems billItems[];
    BillInfo billDetails;

    FileInputParams(String obj){
        System.out.println("obj-->"+obj);
        JSONObject reqObj = new JSONObject(obj);
        this.date = ""+reqObj.get("billDate");
        this.billNo = ""+reqObj.get("billNo");
        this.custDetails = new CustomerInfo(""+reqObj.get("custDetails"));
        this.billItems = new BillItems().getBillItemsArray((JSONArray) reqObj.get("billItems"));
        this.billDetails = new BillInfo(""+reqObj.get("billDetails"));
    }

}
