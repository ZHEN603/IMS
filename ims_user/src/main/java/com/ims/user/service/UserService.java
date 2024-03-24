package com.ims.user.service;

import com.ims.common.exception.CommonException;
import com.ims.domain.company.Company;
import com.ims.domain.user.User;
import com.ims.domain.user.response.ProfileResult;
import com.ims.domain.user.response.UserResult;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface UserService {
    public void save(User user) throws CommonException;

    public void update(User user) throws CommonException;


    public UserResult findById(String id);

    public void deleteById(String id);

    public void assignRoles(String userId, List<String> roleIds);

    public Page<UserResult> findByPage(int page, int size, Map<String, Object> map);

    public void saveAdmin(Company company);

    public String login(Map<String, String> loginMap) throws CommonException;

    public ProfileResult profile(String userId);
}
