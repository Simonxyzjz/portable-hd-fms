package simonxyzjz.phdfms.mongo.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import simonxyzjz.phdfms.mongo.model.Resources;
import simonxyzjz.phdfms.mongo.service.ResourcesService;
import simonxyzjz.phdfms.mongo.shiro.ShiroService;

@RestController
@RequestMapping("/resources")
public class ResourcesController {

	@Resource
	private ResourcesService resourcesService;
	@Resource
	private ShiroService shiroService;

	@RequestMapping
	public Map<String, Object> getAll(Resources resources, String draw,
			@RequestParam(required = false, defaultValue = "1") int start,
			@RequestParam(required = false, defaultValue = "10") int length) {
		Map<String, Object> map = new HashMap<>();
		PageInfo<Resources> pageInfo = resourcesService.selectByPage(resources, start, length);
		System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
		map.put("draw", draw);
		map.put("recordsTotal", pageInfo.getTotal());
		map.put("recordsFiltered", pageInfo.getTotal());
		map.put("data", pageInfo.getList());
		return map;
	}

	@RequestMapping("/resourcesWithSelected")
	public List<Resources> resourcesWithSelected(Integer rid) {
		return resourcesService.queryResourcesListWithSelected(rid);
	}

	@RequestMapping("/loadMenu")
	public List<Resources> loadMenu() {
		Map<String, Object> map = new HashMap<>();
		Integer userid = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
		map.put("type", 1);
		map.put("userid", userid);
		List<Resources> resourcesList = resourcesService.loadUserResources(map);
		// 只取类型为"菜单"的项（type=1）
		return resourcesList.stream().filter(e -> 
			e.getType()==1).collect(Collectors.toList());
		
	}

	@CacheEvict(cacheNames = "resources", allEntries = true)
	@RequestMapping(value = "/add")
	public String add(Resources resources) {
		try {
//			resourcesService.save(resources);
//			// 更新权限
//			shiroService.updatePermission();
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@CacheEvict(cacheNames = "resources", allEntries = true)
	@RequestMapping(value = "/delete")
	public String delete(Integer id) {
		try {
//			resourcesService.delete(id);
//			// 更新权限
//			shiroService.updatePermission();
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
}
