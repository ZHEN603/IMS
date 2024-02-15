package com.ims.domain.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "in_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;
    private String companyId;
    private Integer type;
    private String orderNo;
    private String supplierId;
    private String supplierName;
    private Integer state;
    private Integer quantity;
    private double amount;
    private double pay;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Date completeTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderItems;

}
