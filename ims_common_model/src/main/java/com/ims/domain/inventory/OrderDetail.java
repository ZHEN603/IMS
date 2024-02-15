package com.ims.domain.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "in_order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    private String id;
    private String companyId;
    private String productId;
    private String productName;
    private double cost;
    private double price;
    private Integer type;
    private Integer quantity;
    private double amount;
    private Date createTime;
    private Date updateTime;
    private Date completeTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
}
