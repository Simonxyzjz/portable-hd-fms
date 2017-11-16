package simonxyzjz.phdfms.mongo.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import simonxyzjz.phdfms.mongo.model.Resources;

public interface ResourcesService {
	
    PageInfo<Resources> selectByPage(Resources resources, int start, int length);

    public List<Resources> queryAll();

    public List<Resources> loadUserResources(Map<String,Object> map);

    public List<Resources> queryResourcesListWithSelected(Integer rid);
}
