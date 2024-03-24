package com.ims.domain.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "in_inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    private String id;
    private String companyId;
    private Integer quantity;
    private Integer lowStock;
    private Integer state;

    private String categoryId;
    private String productId;
    private String productName;

    @Formula("quantity - low_stock")
    private Integer difference;
}
