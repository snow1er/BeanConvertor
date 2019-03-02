package dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Created by lixinyao on 2019/3/2.
 */
public class SourceDTO {


    private Long id;

    private String orderCode;

    private Integer deliveryStatus;

    private BigDecimal qutity;

    private Boolean pending;

    private Date latestDeliveryTime;

    private BigInteger status;

    private OffsetDateTime deliveryTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public BigDecimal getQutity() {
        return qutity;
    }

    public void setQutity(BigDecimal qutity) {
        this.qutity = qutity;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public Date getLatestDeliveryTime() {
        return latestDeliveryTime;
    }

    public void setLatestDeliveryTime(Date latestDeliveryTime) {
        this.latestDeliveryTime = latestDeliveryTime;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public OffsetDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(OffsetDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
