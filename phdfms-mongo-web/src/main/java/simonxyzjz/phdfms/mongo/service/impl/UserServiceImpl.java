package simonxyzjz.phdfms.mongo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import simonxyzjz.phdfms.mongo.model.User;
import simonxyzjz.phdfms.mongo.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	private static User adminUser;
	
	static {
		/*
1	admin	3ef7164d1f6167cb9f2658c07d3c2f0a	1
		 */
		adminUser = new User();
		adminUser.setId(1);
		adminUser.setUsername("admin");
		adminUser.setPassword("3ef7164d1f6167cb9f2658c07d3c2f0a");
		adminUser.setEnable(1);
	}

    @Override
    public PageInfo<User> selectByPage(User user, int start, int length) {
        List<User> userList = new ArrayList<>();
        userList.add(adminUser);
        return new PageInfo<>(userList);
    }

    @Override
    public User selectByUsername(String username) {
        return adminUser;
    }

}
