package com.mom.billdesk.getbill;

import org.json.JSONArray;
import org.json.JSONObject;

public class BillItems {
    String docHolder;
    String process;
    String amount;

    BillItems(){ }

    BillItems(JSONObject jobj){
        this.amount = ""+jobj.get("amount");
        this.docHolder = ""+jobj.get("docHolder");
        this.process = ""+jobj.get("process");
    }


    public BillItems[] getBillItemsArray(JSONArray billItemsJsonArray){
        BillItems billItems[] = new BillItems[billItemsJsonArray.length()];
        for(int i = 0 ; i < billItemsJsonArray.length(); i++){
            JSONObject tempJSONObj  = billItemsJsonArray.getJSONObject(i);
            billItems[i] = new BillItems(tempJSONObj);
        }
        return billItems;
    }

}
