package simonxyzjz.phdfms.mongo.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import simonxyzjz.phdfms.mongo.model.Role;

public interface RoleService {

    public List<Role> queryRoleListWithSelected(Integer uid);

    PageInfo<Role> selectByPage(Role role, int start, int length);
}
