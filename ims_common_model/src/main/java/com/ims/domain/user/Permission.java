package com.ims.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "u_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;
    /**
     * MENU 1
     * BTN 2
     * API 3
     */
    private Integer type;
    private String code;
    private String description;
    private String pid;
    private Integer state;
}
