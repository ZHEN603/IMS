package com.ims.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pro_product_backup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBackup implements Serializable {
    @Id
    private String id;
    private String companyId;
    private String name;
    private String categoryId;
    private String categoryName;
    private Double price;
    private Double discount;
    private Double cost;
    private String description;
    private Date createTime;
    private String code;
}

