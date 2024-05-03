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
        WarehouseService warehouseService = new WarehouseService();
        warehouseService.pickItem();
    }
}
