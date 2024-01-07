package com.ims.user.service;

import com.ims.common.utils.IdWorker;
import com.ims.domain.user.Role;
import com.ims.domain.user.User;
import com.ims.user.dao.RoleDao;
import com.ims.user.dao.UserDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.*;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private IdWorker idWorker;

    /**
     * 保存用户
     */
    public void save(User user){
        //设置主键
        String id = idWorker.nextId() + "";
        user.setLevel("user");
        //设置初始密码
        user.setPassword("password");
        user.setEnableState(1);
        user.setId(id);
        //调用dao保存用户
        userDao.save(user);
    }

    /**
     * 更新用户
     */
    public void update(String id , User user){
        User tempUser = userDao.findById(id).get();
        if (!ObjectUtils.isEmpty(tempUser) && !ObjectUtils.isEmpty(user)){
            tempUser.setUsername(user.getUsername());
            tempUser.setPassword(user.getPassword());
            tempUser.setWorkNumber(user.getWorkNumber());
            tempUser.setTimeOfEntry(user.getTimeOfEntry());
        }
        //更新用户
        userDao.save(tempUser);
    }

    /**
     * 根据id查询用户
     */
    public User findById(String id){
        return userDao.findById(id).get();
    }

    /**
     * 查询全部用户列表
     */
    public Page<User> findAll(Map<String,Object> map, int page, int size){

        //查询条件
        Specification<User> spec = new Specification<User>() {

            /**
             * 动态拼接查询条件
             * @param root
             * @param query
             * @param cb
             * @return
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("companyId").as(String.class) , (String)map.get("companyId"));
            }
        };

        //分页
        return userDao.findAll(spec, PageRequest.of(page - 1, size));
    }

    /**
     * 根据id删除用户
     */
    public void deleteById(String id){
        userDao.deleteById(id);
    }

    /**
     * 分配角色
     * @param userId    用户id
     * @param roleIds   要分配的角色id
     */
    public void assignRoles(String userId, List<String> roleIds) {
        //根据id查询用户
        User user = userDao.findById(userId).get();
        //设置用户的角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //更新用户
        userDao.save(user);
    }

    /**
     *  批量用户保存
     * @param list  用户list
     * @param companyId 用户所属公司id
     * @param companyName   用户所属公司名称
     */
    @Transactional
    public void saveAll(List<User> list, String companyId, String companyName) {
        for (User user : list) {
            //默认密码
            user.setPassword("123456");
            //id
            user.setId(idWorker.nextId() + "");
            //基本属性
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setLevel("user");


            userDao.save(user);
        }
    }



}

