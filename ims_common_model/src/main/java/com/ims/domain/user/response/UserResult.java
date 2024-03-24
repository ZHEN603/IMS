package com.ims.domain.user.response;

import com.ims.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResult{

    private String id;
    private String name;
    private String email;
    private String mobile;
    private Integer state;
    private Date createTime;

    private String companyId;
    private String companyName;
    private String level;

    private Set<Role> roles = new HashSet<Role>();

}
