package com.bobbyprod.warehouse;

import com.bobbyprod.common.Interfaces.Observable;
import com.bobbyprod.common.Interfaces.Observer;
import com.bobbyprod.common.States.AssetState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class WarehouseController implements Observable {
    private String wsURL = "http://localhost:8081/Service.asmx";
    private List<Observer> observers;
    private AssetState state;
    private final ScheduledExecutorService executorService;

    @Autowired
    public WarehouseController(){
        this.observers = new ArrayList<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
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
        return message.equals("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <s:Body>    <PickItemResponse xmlns=\"http://tempuri.org/\">      <PickItemResult>Received pick operation.</PickItemResult>    </PickItemResponse>  </s:Body></s:Envelope>");
    }

    public boolean insertItem(String name, int trayId){
        String xmlInput =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<Body>" +
                        "<InsertItem xmlns=\"http://tempuri.org/\">" + "<trayId>" + trayId + "</trayId>" +
                        "<name>" + name + "</name>" + "</InsertItem>" + "</Body>" + "</Envelope>";

        String message = sendSoapRequest(xmlInput);
        return message.equals("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <s:Body>    <InsertItemResponse xmlns=\"http://tempuri.org/\">      <InsertItemResult>Received insert operation.</InsertItemResult>    </InsertItemResponse>  </s:Body></s:Envelope>");
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

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer: observers){
            observer.update();
        }
    }

    public void pollWarehouseStatus(){
        String jsonPart = getInventory();

        if (jsonPart == null) {
            System.out.println("JSON part not found in the XML.");
            this.state = AssetState.ERROR;
        }

        JSONObject jsonResponse = new JSONObject(jsonPart);
        int stateValue = jsonResponse.getInt("State");

        this.state = stateValue == 0 ? AssetState.IDLE : stateValue == 1 ? AssetState.BUSY : AssetState.ERROR;
        System.out.println("POLL POLL POLL");
        notifyObservers();
    }

    @Scheduled(fixedRate = 1000)
    public void scheduledPolling(){
        executorService.scheduleAtFixedRate(this::pollWarehouseStatus,0,1, TimeUnit.SECONDS);
    }

}
