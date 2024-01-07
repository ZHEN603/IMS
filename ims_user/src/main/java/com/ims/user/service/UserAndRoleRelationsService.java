package com.ims.user.service;

import com.ims.common.service.BaseService;
import com.ims.domain.user.Role;
import com.ims.domain.user.RoleAndUserRelations;
import com.ims.user.dao.UserAndRoleRelationsDao;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: hyl
 * @date: 2020/05/25
 **/
@Service
public class UserAndRoleRelationsService extends BaseService {

    @Resource
    private UserAndRoleRelationsDao userAndRoleRelationsDao;

    @Resource
    private RoleService roleService;

    public List<RoleAndUserRelations> findRoleByUserId(String userId){
        return userAndRoleRelationsDao.findByUserId(userId);
    }


    public List<Role> getRoleDetailByRoleId(List<RoleAndUserRelations> roleByUserId) {
        List<Role> res = new ArrayList<>();
        for (RoleAndUserRelations userAndRoleRea : roleByUserId) {
            Role role = roleService.findById(userAndRoleRea.getRoleId());
            if (!ObjectUtils.isEmpty(role)){
                res.add(role);
            }
        }
        return res;
    }
}
