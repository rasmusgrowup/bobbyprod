package com.bobbyprod.warehouse;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class WarehouseController {
    String wsURL = "http://localhost:8081/Service.asmx";

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

    public void pickItem(int trayId){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<PickItem xmlns=\"http://tempuri.org/\">" + "<trayId>" + trayId + "</trayId>" +
                        "</PickItem>" + "</Body>" + "</Envelope>";

        sendSoapRequest(xmlInput);
    }

    public void insertItem(String name, int trayId){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<InsertItem xmlns=\"http://tempuri.org/\">" + "<trayId>" + trayId + "</trayId>" +
                        "<name>" + name + "</name>" + "</InsertItem>" + "</Body>" + "</Envelope>";

        sendSoapRequest(xmlInput);
    }

    public String extractJsonFromXml(String xml) throws Exception {
        if (xml != null && !xml.trim().startsWith("<")) {
            throw new IllegalArgumentException("Invalid XML content: Does not start with '<'");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource source = new InputSource(new StringReader(xml.trim()));
        Document doc = builder.parse(source);

        NodeList nl = doc.getElementsByTagName("GetInventoryResult");
        if (nl.getLength() > 0) {
            return nl.item(0).getTextContent();
        }
        return null; // Return null if no JSON found
    }
    private String removeBOM(String data) {
        if (data != null && data.startsWith("\uFEFF")) {
            return data.substring(1);
        }
        return data;
    }
}
