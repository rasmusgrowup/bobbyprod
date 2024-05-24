package com.bobbyprod.warehouse;

import com.bobbyprod.common.States.AssetState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Controller
public class WarehouseController {
    private String wsURL = "http://localhost:8081/Service.asmx";

    public WarehouseController(){
    }

    public String sendSoapRequest(String xmlInput){
        URL url = null;
        URLConnection connection = null;
        HttpURLConnection httpConn = null;
        String responseString = null;
        String outputString="";
        OutputStream out = null;
        InputStreamReader isr = null;
        BufferedReader in = null;

        try {
            url = new URL(wsURL);
            connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;

            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();

            String SOAPAction = "";
            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String
                    .valueOf(buffer.length));
            httpConn.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");


            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            out = httpConn.getOutputStream();
            out.write(buffer);
            out.close();

            // Read the response and write it to standard out.
            isr = new InputStreamReader(httpConn.getInputStream());
            in = new BufferedReader(isr);

            while ((responseString = in.readLine()) != null) {
                outputString = outputString + responseString;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        outputString = removeBOM(outputString);
        return outputString;
    }
    public String getInventory(){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<GetInventory xmlns=\"http://tempuri.org/\"/>" + "</Body>" + "</Envelope>";
        try {
            return extractJsonFromXml(sendSoapRequest(xmlInput));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean pickItem(int trayId){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<PickItem xmlns=\"http://tempuri.org/\">" + "<trayId>" + trayId + "</trayId>" +
                        "</PickItem>" + "</Body>" + "</Envelope>";

        String message = sendSoapRequest(xmlInput);
        int firstXmlTagIndex = message.indexOf("<");
        if (firstXmlTagIndex == -1) {
            throw new IllegalArgumentException("Invalid XML content: No XML tag found");
        }
        message = message.substring(firstXmlTagIndex);
        return message.equals("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <s:Body>    <PickItemResponse xmlns=\"http://tempuri.org/\">      <PickItemResult>Received pick operation.</PickItemResult>    </PickItemResponse>  </s:Body></s:Envelope>");
    }

    public boolean insertItem(String name, int trayId){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<InsertItem xmlns=\"http://tempuri.org/\">" + "<trayId>" + trayId + "</trayId>" +
                        "<name>" + name + "</name>" + "</InsertItem>" + "</Body>" + "</Envelope>";

        String message = sendSoapRequest(xmlInput);
        int firstXmlTagIndex = message.indexOf("<");
        if (firstXmlTagIndex == -1) {
            throw new IllegalArgumentException("Invalid XML content: No XML tag found");
        }
        message = message.substring(firstXmlTagIndex);
        return message.equals("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <s:Body>    <InsertItemResponse xmlns=\"http://tempuri.org/\">      <InsertItemResult>Received insert operation.</InsertItemResult>    </InsertItemResponse>  </s:Body></s:Envelope>");
    }

    public String extractJsonFromXml(String xml) throws IllegalArgumentException {
        xml = removeBOM(xml);

        int firstXmlTagIndex = xml.indexOf("<");
        if (firstXmlTagIndex == -1) {
            throw new IllegalArgumentException("Invalid XML content: No XML tag found");
        }
        xml = xml.substring(firstXmlTagIndex);

        int jsonStartIndex = xml.indexOf("{");
        int jsonEndIndex = xml.lastIndexOf("}") + 1;

        if (jsonStartIndex == -1 || jsonEndIndex == -1) {
            throw new IllegalArgumentException("Invalid XML content: JSON part not found");
        }

        return xml.substring(jsonStartIndex, jsonEndIndex);
    }

    private String removeBOM(String data) {
        if (data != null && data.startsWith("\uFEFF")) {
            return data.substring(1);
        }
        return data;
    }

    public AssetState pollWarehouseStatus(){
        String jsonPart = getInventory();

        if (jsonPart == null) {
            System.out.println("JSON part not found in the XML.");
            return AssetState.ERROR;
        } else {
            JSONObject jsonResponse = new JSONObject(jsonPart);
            int stateValue = jsonResponse.getInt("State");

            return stateValue == 0 ? AssetState.IDLE : stateValue == 1 ? AssetState.BUSY : AssetState.ERROR;
        }
    }
}
