package com.bobbyprod.common.Tasks;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Assets.AssetType;
import com.bobbyprod.common.Products.Part;
import com.bobbyprod.common.Products.Product;

/**
 * Represents a task to be handled by an asset. This task can contain either a part or a product
 * depending on the stage of the production process. This design allows tasks to dynamically
 * switch focus between moving/handling parts and products.
 */
public class Task {
    private ActionType actionType;
    private TaskStatus status;
    private AssetType compatibleAssetType;
    private Product product;
    private Part part;
    private Asset processedBy, destination;

    /**
     * Default constructor for creating an empty task.
     */
    public Task() {
    }

    public Task(ActionType actionType, Asset processedBy) {
        this.actionType = actionType;
        this.processedBy = processedBy;
    }

    /**
     * Constructs a task with a specific action type, compatible asset type, and a product.
     * This constructor is used when the task involves handling products.
     *
     * @param actionType the type of action this task involves
     * @param compatibleAssetType the type of asset that can handle this task
     * @param product the product involved in this task
     */
    public Task(ActionType actionType, AssetType compatibleAssetType, Product product) {
        this.actionType = actionType;
        this.status = TaskStatus.TASK_UNASSIGNED;
        this.compatibleAssetType = compatibleAssetType;
        this.product = product;
        this.part = null; // Ensure part is null when product is used
    }

    /**
     * Constructs a task with a specific action type, compatible asset type, and a part.
     * This constructor is used when the task involves handling parts.
     *
     * @param actionType the type of action this task involves
     * @param compatibleAssetType the type of asset that can handle this task
     //* @param part the part involved in this task
     */
    public Task(ActionType actionType, AssetType compatibleAssetType) {
        this.actionType = actionType;
        this.compatibleAssetType = compatibleAssetType;
        //this.part = part;
        this.product = null; // Ensure product is null when part is used
    }

    // Getters and setters for all fields
    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
        this.product = null; // Clear product when setting part
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.part = null; // Clear part when setting product
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public AssetType getCompatibleAssetType() {
        return compatibleAssetType;
    }

    public void setCompatibleAssetType(AssetType compatibleAssetType) {
        this.compatibleAssetType = compatibleAssetType;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Asset getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Asset processedBy) {
        this.processedBy = processedBy;
    }

    public Asset getDestination() {
        return destination;
    }

    public void setDestination(Asset destination) {
        this.destination = destination;
    }
}
