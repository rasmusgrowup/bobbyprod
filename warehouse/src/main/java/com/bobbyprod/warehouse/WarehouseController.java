package com.bobbyprod.warehouse;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
        return outputString;
    }
    public String getInventory(){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<GetInventory xmlns=\"http://tempuri.org/\"/>" + "</Body>" + "</Envelope>";
        return sendSoapRequest(xmlInput);
    }

    public void pickItem(int trayId){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<PickItem xmlns=\"http://tempuri.org/\">" + "<trayId>" + trayId + "</trayId>" +
                        "</PickItem>" + "</Body>" + "</Envelope>";

        sendSoapRequest(xmlInput);
        org.w3c.dom.Document document = parseXmlFile(sendSoapRequest(xmlInput));
        NodeList nodeList = document.getElementsByTagName("PickItemResponse");
        String webServiceResponse = nodeList.item(0).getTextContent();
        System.out.println(webServiceResponse);
    }

    public void insertItem(String name, int trayId){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<InsertItem xmlns=\"http://tempuri.org/\">" + "<trayId>" + trayId + "</trayId>" +
                        "<name>" + name + "</name>" + "</InsertItem>" + "</Body>" + "</Envelope>";

        org.w3c.dom.Document document = parseXmlFile(sendSoapRequest(xmlInput));
        NodeList nodeList = document.getElementsByTagName("InsertItemResponse");
        String webServiceResponse = nodeList.item(0).getTextContent();
        System.out.println(webServiceResponse);
    }

//    private org.w3c.dom.Document parseXmlFile(String input){
//        if (input != null && input.startsWith("\uFEFF")) {
//            input = input.substring(1);
//        }
//        try {
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            InputSource is = new InputSource(new StringReader(input));
//            return db.parse(is);
//        } catch (ParserConfigurationException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (SAXException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public String extractJsonFromXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        NodeList nl = doc.getElementsByTagName("GetInventoryResult");
        if (nl.getLength() > 0) {
            return nl.item(0).getTextContent();
        }
        return null; // Return null if no JSON found
    }
}
