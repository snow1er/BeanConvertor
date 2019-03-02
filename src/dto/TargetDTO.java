package dto;


/**
 * Created by lixinyao on 2019/3/2.
 */
public class TargetDTO {

    private String id;

    private String orderCode;

    private String deliveryStatus;

    private String qutity;

    private String pending;

    private String latestDeliveryTime;

    private String status;

    private String deliveryTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getQutity() {
        return qutity;
    }

    public void setQutity(String qutity) {
        this.qutity = qutity;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getLatestDeliveryTime() {
        return latestDeliveryTime;
    }

    public void setLatestDeliveryTime(String latestDeliveryTime) {
        this.latestDeliveryTime = latestDeliveryTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString(){
        return "id : " + id
            +", orderCode : " + orderCode
            +", deliveryStatus : " + deliveryStatus
            +", qutity : " + qutity
            +", pending : " + pending
            +", latestDeliveryTime : " + latestDeliveryTime
            +", status : " + status
            +", deliveryTime : " + deliveryTime;
    }
}
