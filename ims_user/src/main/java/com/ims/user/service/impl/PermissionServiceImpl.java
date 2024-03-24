package com.ims.user.service.impl;

import com.ims.common.utils.IdWorker;
import com.ims.domain.user.Permission;
import com.ims.user.dao.PermissionDao;
import com.ims.user.service.PermissionService;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private IdWorker idWorker;
    @Override
    public void save(Permission permission) {
        String id = idWorker.nextId() + "";
        permission.setId(id);
        permissionDao.save(permission);
    }
    @Override
    public void update(Permission permission) {
        permissionDao.save(permission);
        List<Permission> subs = permissionDao.findAll((Specification<Permission>) (Specification) (root, query, cb) -> cb.equal(root.get("pid").as(String.class) , permission.getId()));
        System.out.println(subs.size());
        for (Permission sub : subs) {
            sub.setState(permission.getState());
            update(sub);
        }
    }
    @Override
    public Permission findById(String id) {
        return permissionDao.findById(id).get();
    }
    @Override
    public List<Permission> findAll(Map<String,Object> map){
        Specification<Permission> spec = new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("pid"))){
                     list.add(cb.equal(root.get("pid").as(String.class) , (String)map.get("pid")));
                }
                if (!StringUtils.isEmpty(map.get("state"))){
                    list.add(cb.equal(root.get("state") , Integer.parseInt((String)map.get("state"))));
                }
                if (!StringUtils.isEmpty(map.get("type"))){
                    String type = (String) map.get("type");
                    CriteriaBuilder.In<Object> in = cb.in(root.get("type"));
                    if ("0".equals(type)){
                        in.value(1).value(2);
                    }else {
                        in.value(Integer.parseInt(type));
                    }
                    list.add(in);
                }
                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return permissionDao.findAll(spec);
    }
    @Override
    public void deleteById(String id) {
        permissionDao.deleteById(id);
        List<Permission> subs = permissionDao.findAll((Specification<Permission>) (Specification) (root, query, cb) -> cb.equal(root.get("pid").as(String.class) , id));
        System.out.println(subs.size());
        for (Permission sub : subs) {
            deleteById(sub.getId());
        }
    }
}

