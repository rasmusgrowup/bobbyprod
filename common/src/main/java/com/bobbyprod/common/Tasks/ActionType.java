package com.bobbyprod.common.Tasks;

public enum ActionType {
    //AGV actions
    MOVE_TO_WAREHOUSE,
    MOVE_TO_ASSEMBLY_STATION,
    MOVE_TO_CHARGER,
    PICK_ITEM_FROM_WAREHOUSE,
    PUT_ITEM_TO_WAREHOUSE,
    PICK_ITEM_FROM_ASSEMBLY_STATION,
    PUT_ITEM_TO_ASSEMBLY_STATION,
    //AssemblyStation actions
    ASSEMBLE_ITEM,
    //Warehouse actions
    INSERT_ITEM,
    PICK_ITEM,
    FILL_PARTS,
}
