package com.ims.domain.user;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "pe_permission_menu")
@Getter
@Setter
public class PermissionMenu implements Serializable {
    private static final long serialVersionUID = -1002411490113957485L;

    /**
     * 主键
     */
    @Id
    private String id;

    //展示图标
    private String menuIcon;

    //排序号
    private String menuOrder;
}