package com.ims.user.service;

import com.ims.common.utils.IdWorker;
import com.ims.domain.user.Role;
import com.ims.domain.user.User;
import com.ims.user.dao.RoleDao;
import com.ims.user.dao.UserDao;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private IdWorker idWorker;

    public void save(User user){
        user.setId(idWorker.nextId() + "");
        user.setLevel("user");
        user.setState(user.getState());
        user.setPassword("password");
        userDao.save(user);
    }

    public void update(User user){ userDao.save(user);}

    public User findById(String id){
        return userDao.findById(id).get();
    }

    public void deleteById(String id){
        if (userDao.findById(id).get().getLevel().equals("user")){
            userDao.deleteById(id);
        }
    }

    public void assignRoles(String userId, List<String> roleIds) {
        User user = userDao.findById(userId).get();
        Set<Role> roles = new HashSet<>();
        if (roleIds.size()>0){
            user.setRoles(roles);
            userDao.save(user);
        }
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        user.setRoles(roles);
        userDao.save(user);
    }

    @Transactional
    public void saveAll(List<User> list, String companyId, String companyName) {
        for (User user : list) {
            user.setPassword("123456");
            user.setId(idWorker.nextId() + "");
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setState(1);
            user.setLevel("user");
            userDao.save(user);
        }
    }

    public Page<User> findAll(Map map, int page, int size){
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("level").as(String.class) , "user"));
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(cb.equal(root.get("companyId").as(String.class) , (String)map.get("companyId")));
                }
                if (!StringUtils.isEmpty(map.get("roleId")) && !((String)map.get("roleId")).equals("0")){
                    Join<User, Role> rolesJoin = root.join("roles", JoinType.INNER);
                    list.add(cb.equal(rolesJoin.get("id"), (String)map.get("roleId")));
                }
                if (!StringUtils.isEmpty(map.get("keyword"))){
                    list.add(cb.like(root.get("name"), "%" + ((String)map.get("keyword")).trim() + "%"));
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return userDao.findAll(spec, PageRequest.of(page - 1, size));
    }

    public void saveAdmin(String id, String companyName, String companyId){
        User user = new User();
        user.setId(id);
        user.setName(companyName);
        user.setCompanyName(companyName);
        user.setCompanyId(companyId);
        user.setPassword("password");
        user.setLevel("coAdmin");
        user.setState(1);
        user.setCreateTime(new Date());
        userDao.save(user);
    }
}

