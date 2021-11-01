package com.mom.billdesk.getbill;


import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.*;

import static com.mom.billdesk.getbill.MakeTablePdf.getPdfFile;


@RestController
public class FileController {

    @GetMapping(value = "/welcome")
    @Produces("text/plain")
    public String getClichedMessage() {
        System.out.println("welcome");
        return "Hello World";
    }

    @PostMapping(value = "/getFiles")
    @CrossOrigin
    @Produces("application/pdf")
        public ResponseEntity<byte[]> getFiles(@RequestBody String fileParams){

        FileInputParams fip = new FileInputParams(fileParams);
        ByteArrayOutputStream baos = getPdfFile(fip);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=thefilename.pdf");
        headers.add("Access-Control-Expose-Headers","Apiproxy-Session-Id");
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        }
    }



