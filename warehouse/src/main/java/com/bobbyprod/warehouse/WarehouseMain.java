package com.bobbyprod.warehouse;

import com.bobbyprod.common.Products.Part;
import com.bobbyprod.common.Tasks.ActionType;
import com.bobbyprod.common.Tasks.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Hello world!
 *
 */
@Service
public class WarehouseMain
{
    public static void main( String[] args )
    {
        // Create an instance of AgvService
        //Mediator mediator = new Mediator();
        Warehouse warehouse = new Warehouse("Warehouse - 1","Warehouse");

        // Create a new task
        Task task = new Task();
        task.setPart(new Part("Test Item","250",8));
        task.getPart().setTrayId(-1);
        task.setActionType(ActionType.INSERT_ITEM);

        // Call the handleTask method
        warehouse.processTask(task);
        System.out.println(task.getPart().getTrayId());
    }
}
