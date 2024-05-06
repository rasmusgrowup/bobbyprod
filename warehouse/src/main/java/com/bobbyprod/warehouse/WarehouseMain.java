package com.bobbyprod.warehouse;

import org.springframework.stereotype.Service;

/**
 * Hello world!
 *
 */
@Service
public class WarehouseMain
{
    public static void main( String[] args )
    {
        WarehouseController warehousc = new WarehouseController();
        System.out.println(warehousc.pickItem(1));
        //System.out.println(warehousc.insertItem("name",1));
    }
}
