package simonxyzjz.phdfms.mongo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import simonxyzjz.phdfms.mongo.model.Role;
import simonxyzjz.phdfms.mongo.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	
	private static List<Role> rolesList;
	
	static {
		/*
1	管理员
2	普通用户
3	超级管理员
		 */
		rolesList = new ArrayList<>();
		Role role1 = new Role();
		role1.setId(1);
		role1.setRoledesc("管理员");
		rolesList.add(role1);
		Role role2 = new Role();
		role2.setId(2);
		role2.setRoledesc("普通用户");
		rolesList.add(role2);
		Role role3 = new Role();
		role3.setId(3);
		role3.setRoledesc("超级管理员");
		rolesList.add(role3);
	}

    @Override
    public List<Role> queryRoleListWithSelected(Integer uid) {
        return rolesList;
    }

    @Override
    public PageInfo<Role> selectByPage(Role role, int start, int length) {
        return new PageInfo<>(rolesList);
    }
}
