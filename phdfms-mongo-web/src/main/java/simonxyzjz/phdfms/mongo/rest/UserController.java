package simonxyzjz.phdfms.mongo.rest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import simonxyzjz.phdfms.mongo.model.User;
import simonxyzjz.phdfms.mongo.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
	@Resource
	private UserService userService;

	@RequestMapping
	public Map<String, Object> getAll(User user, String draw,
			@RequestParam(required = false, defaultValue = "1") int start,
			@RequestParam(required = false, defaultValue = "10") int length) {
		Map<String, Object> map = new HashMap<>();
		PageInfo<User> pageInfo = userService.selectByPage(user, start, length);
		System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
		map.put("draw", draw);
		map.put("recordsTotal", pageInfo.getTotal());
		map.put("recordsFiltered", pageInfo.getTotal());
		map.put("data", pageInfo.getList());
		return map;
	}

}
