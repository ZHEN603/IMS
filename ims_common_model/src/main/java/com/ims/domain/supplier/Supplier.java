package com.ims.domain.supplier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "su_supplier")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    private String id;
    private String companyId;
    private String name;
    private String email;
    private String mobile;
    private String description;
    private Date createTime;
}
