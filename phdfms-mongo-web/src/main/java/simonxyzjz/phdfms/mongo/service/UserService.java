package simonxyzjz.phdfms.mongo.service;

import com.github.pagehelper.PageInfo;

import simonxyzjz.phdfms.mongo.model.User; 

public interface UserService {
    PageInfo<User> selectByPage(User user, int start, int length);
    User selectByUsername(String username);

}
