package com.ims.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pe_permission")
@Data
@NoArgsConstructor
@DynamicInsert()
@DynamicUpdate()
public class Permission implements Serializable {
    private static final long serialVersionUID = -4990810027542971546L;
    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限类型 1为菜单 2为功能 3为API
     */
    private Integer type;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限描述
     */
    private String description;

    private String pid;

    //可见状态
    private String enVisible;

    public Permission(String name, Integer type, String code, String description) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.description = description;
    }


}
