package com.ims.domain.user.response;

import com.ims.domain.user.Permission;
import com.ims.domain.user.Role;
import com.ims.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
public class ProfileResult implements Serializable {
    private String userId;
    private String mobile;
    private String email;
    private String username;
    private String company;
    private String companyId;
    private Map<String,Object> roles = new HashMap<>();

    /**
     *
     * @param user
     */
    public ProfileResult(User user, Map<String,Object> roles) {
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.username = user.getName();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();
        this.userId = user.getId();
        this.roles = roles;

    }


    public ProfileResult(User user) {
        this.mobile = user.getMobile();
        this.username = user.getName();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();
        this.userId = user.getId();
        Set<Role> roles = user.getRoles();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Role role : roles) {
            if (role.getState().equals("1")){
                Set<Permission> perms = role.getPermissions();
                for (Permission perm : perms) {
                    String code = perm.getCode();
                    if (perm.getState().equals("1")){
                        if(perm.getType() == 1) {
                            menus.add(code);
                        }else if(perm.getType() == 2) {
                            points.add(code);
                        }else {
                            apis.add(code);
                        }
                    }
                }
            }
        }
        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }
}
