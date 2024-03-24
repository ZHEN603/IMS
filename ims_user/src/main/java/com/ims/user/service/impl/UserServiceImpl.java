package com.ims.user.service.impl;

import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.common.exception.CommonException;
import com.ims.common.utils.IdWorker;
import com.ims.common.utils.JwtUtils;
import com.ims.domain.company.Company;
import com.ims.domain.user.Permission;
import com.ims.domain.user.Role;
import com.ims.domain.user.User;
import com.ims.domain.user.response.ProfileResult;
import com.ims.domain.user.response.UserResult;
import com.ims.user.dao.UserDao;
import com.ims.user.service.PermissionService;
import com.ims.user.service.RoleService;
import com.ims.user.service.UserService;
import jakarta.persistence.criteria.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private IdWorker idWorker;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private ModelMapper modelMapper;

    @Override
    public void save(User user) throws CommonException {
        if (userDao.findByEmail(user.getEmail()) != null){
            throw new CommonException(ResultCode.EMAIL_ERROR);
        }
        user.setId(idWorker.nextId() + "");
        user.setLevel("user");
        user.setState(user.getState());
        user.setPassword("password");
        userDao.save(user);
    }

    @Override
    public void update(User user) throws CommonException {
        User checkEmail =userDao.findByEmail(user.getEmail());
        if (checkEmail != null && !checkEmail.getId().equals(user.getId())){
            throw new CommonException(ResultCode.EMAIL_ERROR);
        }
        if ((user.getPassword()==null)||(user.getPassword()=="")){
            user.setPassword(userDao.findById(user.getId()).get().getPassword());
        }
        userDao.save(user);
    }


    @Override
    public UserResult findById(String id){
        return modelMapper.map(userDao.findById(id).get(), UserResult.class);
    }

    @Override
    public void deleteById(String id){
        if (userDao.findById(id).get().getLevel().equals("user")){
            userDao.deleteById(id);
        }
    }

    @Override
    public void assignRoles(String userId, List<String> roleIds) {
        User user = userDao.findById(userId).get();
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleService.findById(roleId);
            roles.add(role);
        }
        user.setRoles(roles);
        userDao.save(user);
    }

    @Override
    public Page<UserResult> findByPage(int page, int size, Map<String, Object> map){
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    System.out.println(1);
                    if (map.get("companyId").toString().equals("null")){
                        System.out.println(2);
                        list.add(cb.equal(root.get("level").as(String.class) , "coAdmin"));
                    } else {
                        System.out.println(3);
                        list.add(cb.equal(root.get("level").as(String.class) , "user"));
                        list.add(cb.equal(root.get("companyId").as(String.class) , (String)map.get("companyId")));
                    }
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
        return userDao.findAll(spec, PageRequest.of(page - 1, size)).map(user -> modelMapper.map(user, UserResult.class));
    }

    @Override
    @Transactional
    public void saveAdmin(Company company){
        User user = new User();
        user.setId(company.getManagerId());
        user.setName(company.getName());
        user.setCompanyName(company.getName());
        user.setCompanyId(company.getId());
        user.setEmail(company.getEmail());
        user.setMobile(company.getMobile());
        user.setPassword("password");
        user.setLevel("coAdmin");
        user.setState(1);
        user.setCreateTime(new Date());
        userDao.save(user);
    }

    @Override
    public String login(Map<String,String> loginMap) {
        String email = loginMap.get("email");
        String password = loginMap.get("password");
        User user = userDao.findByEmail(email);
        //failed
        if(user == null || !user.getPassword().equals(password) || user.getState().equals(0)) {
            return null;
        }else {
            //success
            Set<String> permissions = new HashSet<String>();
            Map map = new HashMap();
            switch (user.getLevel()){
                case "user":
                    for (Role role : user.getRoles()) {
                        for (Permission perm : role.getPermissions()) {
                            if(perm.getType() == 3) {
                                permissions.add(perm.getCode());
                            }
                        }
                    }
                    break;
                case "coAdmin":
                    map.put("state","1");
                    List<Permission> perms = permissionService.findAll(map);
                    for (Permission perm : perms) {
                        if (perm.getState().equals(1) && perm.getType() == 3){
                            permissions.add(perm.getCode());
                        }
                    }
                    break;
                case "saasAdmin":
                    List<Permission> permsList = permissionService.findAll(map);
                    for (Permission perm : permsList) {
                        if (perm.getType() == 3){
                            permissions.add(perm.getCode());
                        }
                    }
            }
            String result = permissions.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            Map<String,Object> info = new HashMap<>();
            info.put("apis",result);
            info.put("companyId",user.getCompanyId());
            info.put("companyName",user.getCompanyName());
            String token = jwtUtils.createJwt(user.getId(), user.getName(), info);
            return token;
        }
    }

    @Override
    public ProfileResult profile(String userId){
        User user = userDao.findById(userId).get();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Map map = new HashMap();
        switch (user.getLevel()){
            case "user":
                Set<Role> roles = user.getRoles();
                for (Role role : roles) {
                    if (role.getState().equals(1)){
                        Set<Permission> perms = role.getPermissions();
                        for (Permission perm : perms) {
                            String code = perm.getCode();
                            if (perm.getState().equals(1)){
                                switch (perm.getType()){
                                    case 1:
                                        menus.add(code);
                                    case 2:
                                        points.add(code);
                                }
                            }
                        }
                    }
                }
                break;
            case "coAdmin":
                map.put("state","1");
                List<Permission> perms = permissionService.findAll(map);
                for (Permission perm : perms) {
                    String code = perm.getCode();
                    if (perm.getState().equals(1)){
                        switch (perm.getType()){
                            case 1:
                                menus.add(code);
                            case 2:
                                points.add(code);
                        }
                    }
                }
                break;
            case "saasAdmin":
                List<Permission> permsList = permissionService.findAll(map);
                for (Permission perm : permsList) {
                    String code = perm.getCode();
                    switch (perm.getType()){
                        case 1:
                            if (perm.getState()==0){
                                menus.add(code);
                            }
                        case 2:
                            points.add(code);
                    }
                }
        }
        Map<String,Object> rolesProfile = new HashMap<>();
        rolesProfile.put("menus",menus);
        rolesProfile.put("points",points);
        ProfileResult result = new ProfileResult(user,rolesProfile);
        return result;
    }
}

