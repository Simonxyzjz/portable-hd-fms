package simonxyzjz.phdfms.mongo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import simonxyzjz.phdfms.mongo.model.Resources;
import simonxyzjz.phdfms.mongo.service.ResourcesService;

@Slf4j
@Service
public class ResourcesServiceImpl implements ResourcesService {
	
	private static List<Resources> resourcesList;
	
	static {
		log.info("**********静态数据初始化**********");
		/*
1	系统设置	/system						0	0	1
2	用户管理	/usersPage					1	1	2
3	角色管理	/rolesPage					1	1	3
4	资源管理	/resourcesPage				1	1	4
5	添加用户	/users/add					2	2	5
6	删除用户	/users/delete				2	2	6
7	添加角色	/roles/add					2	3	7
8	删除角色	/roles/delete				2	3	8
9	添加资源	/resources/add				2	4	9
10	删除资源	/resources/delete			2	4	10
11	分配角色	/users/saveUserRoles		2	2	11
13	分配权限	/roles/saveRoleResources	2	3	12
		 * 
		 */
		
		resourcesList = new ArrayList<>();
		Resources res1 = new Resources();
		res1.setId(1);
		res1.setName("P-HD-FMS");
		res1.setResurl("/fmsPage");
		res1.setType(0);
		res1.setParentid(0);
		res1.setSort(1);
		resourcesList.add(res1);
	}

	@Override
	public PageInfo<Resources> selectByPage(Resources resources, int start, int length) {
		
		return new PageInfo<>(resourcesList);
	}

	@Override
	public List<Resources> queryAll() {
		return resourcesList;
	}

	@Override
	@Cacheable(cacheNames = "resources", key = "#map['userid'].toString()+#map['type']")
	public List<Resources> loadUserResources(Map<String, Object> map) {
		return resourcesList;
	}

	@Override
	public List<Resources> queryResourcesListWithSelected(Integer rid) {
		return resourcesList;
	}
}
