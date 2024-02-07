package com.ims.domain.company;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "c_company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;
    private String managerId;
    private String mobile;
    private String email;
    private String description;
    private Integer state;
    private Date createTime;
}
