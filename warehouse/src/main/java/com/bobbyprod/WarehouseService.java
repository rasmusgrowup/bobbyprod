package com.bobbyprod;

import org.springframework.stereotype.Service;

/**
 * Hello world!
 *
 */
@Service
public class WarehouseService
{
    public static void main( String[] args )
    {
        SoapClient client = new SoapClient();
        Warehouse warehouse = new Warehouse();
        String[] array = warehouse.getInventoryArray();
        for (String content : array) {
            System.out.println("index:" + content);
        }
        warehouse.checkState();
        System.out.println(client.getInventory());
    }
}
