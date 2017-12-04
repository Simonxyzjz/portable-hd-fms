package simonxyzjz.phdfms.mongo.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import lombok.extern.slf4j.Slf4j;
import simonxyzjz.phdfms.mongo.domain.FileEntity;
import simonxyzjz.phdfms.mongo.mapper.FileEntityMapper;
import simonxyzjz.phdfms.mongo.mapper.FileEntityVOMapper;
import simonxyzjz.phdfms.mongo.repository.MyPageable;
import simonxyzjz.phdfms.mongo.service.FileScanService;
import simonxyzjz.phdfms.mongo.vo.FileEntityVO;
import simonxyzjz.phdfms.mongo.vo.TempVO;

@Slf4j
@RestController
@RequestMapping("/fms")
public class MainController {


	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private FileScanService fileScanService;
	
    @RequestMapping
    public  Map<String,Object> getAll(TempVO param, String draw,
                             @RequestParam(required = false, defaultValue = "1") int start,
                             @RequestParam(required = false, defaultValue = "10") int length){

        Map<String,Object> map = new HashMap<>();
        map.put("draw",draw);
        List<Order> orders = new ArrayList<Order>();  //排序
        orders.add(new Order(Direction.DESC, "createdAt"));
        Sort sort = new Sort(orders);
        MyPageable page = new MyPageable();
        page.setNumber(start/length);
        page.setSize(length);
        page.setSort(sort);
        
        Query query = new Query();
        String id = StringUtils.trimToNull(param.getId());
        if(id != null) {
            query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        }
        
        String md5 = StringUtils.trimToNull(param.getMd5());
        if(md5 != null) {
            query.addCriteria(Criteria.where("md5").is(md5));
        }
        
        String name = StringUtils.trimToNull(param.getName());
        if(name != null) {
            query.addCriteria(Criteria.where("name").is(name));
        }
        
        String path = StringUtils.trimToNull(param.getPath());
        if(path != null) {
            query.addCriteria(Criteria.where("path").is(path));
        }
        
        
        Long total = mongoTemplate.count(query, FileEntity.class);

        map.put("recordsTotal", total);
        map.put("recordsFiltered", total);
        List<FileEntity> fileEntities = mongoTemplate.find(query.with(page), FileEntity.class);
        List<FileEntityVO> dataList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(fileEntities)) {
        	fileEntities.forEach(e -> {
        		FileEntityVO vo = FileEntityVOMapper.map(e);
        		dataList.add(vo);
        		});
        }
        map.put("data", dataList);
        return map;
    }

	@ResponseBody
	@RequestMapping(value = "/gfs/file", method = RequestMethod.POST)
	public ResponseEntity<?> storeFile(HttpServletRequest request) throws Exception {

		InputStream instream =null;
		try {
			File file = FileUtils.getFile("D:\\test.zip");
			instream = new FileInputStream(file);
			GridFSFile gFile = gridFsTemplate.store(instream, "test.zip", "zip");
			return ResponseEntity.ok(gFile.getMD5());
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(instream != null) {
				instream.close();
			}
		}
		return ResponseEntity.ok("ok");
	}
	
	@ResponseBody
	@RequestMapping(value = "/gfs/file", method = RequestMethod.GET)
	public ResponseEntity<?> findFile(HttpServletRequest request) throws Exception {

		try {
			String md5 = request.getParameter("md5");
			if(StringUtils.isNotBlank(md5)) {
				Query query = new Query();
				query.addCriteria(GridFsCriteria.where("md5").is(md5));
				List<GridFSDBFile> ret = gridFsTemplate.find(query);
				List<String> fileNames = new ArrayList<>();
				ret.forEach(e -> {fileNames.add(e.getId().toString());});
				return ResponseEntity.ok(fileNames);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("ok");
	}

	@ResponseBody
	@RequestMapping(value = "/dir/struct", method = RequestMethod.POST)
	public ResponseEntity<?> storeStruct(HttpServletRequest request) throws Exception {

		try {
			String path = request.getParameter("path");
			if(StringUtils.isNotBlank(path)) {
				File file = FileUtils.getFile(path);
				if(file.isDirectory()) {
					Query query = new Query();
					query.addCriteria(Criteria.where("path").is(path));
					FileEntity dbEntity = mongoTemplate.findOne(query, FileEntity.class);
					if(dbEntity == null) {

						FileEntity entity = FileEntityMapper.map(file);
						mongoTemplate.save(entity);
						
						scanDirectory(file,entity.getId());
					} else {
						return ResponseEntity.ok("path has already been processed");
					}
				} else {
					return ResponseEntity.ok("path is not exist or not a directory");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("ok");
	}
	
	@ResponseBody
	@RequestMapping(value = "/scan", method = RequestMethod.POST)
	public ResponseEntity<?> scanDir(HttpServletRequest request) throws Exception {

		try {
			String dir = request.getParameter("dir");
			String diskName = request.getParameter("diskName");
			log.info("{}, {}", dir, diskName);
			
		} catch (Exception e) {
			log.error("扫描目录出错", e);
			return ResponseEntity.badRequest().body("error");
		}
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@RequestMapping(value = "/dir/update", method = RequestMethod.GET)
	public ResponseEntity<?> checkUpdate(HttpServletRequest request) throws Exception {

		try {
			String id = request.getParameter("id");
			if(StringUtils.isNotBlank(id)) { 
				fileScanService.scanAndUpdateById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("ok");
	}
	

	@ResponseBody
	@RequestMapping(value = "/dir/struct", method = RequestMethod.GET)
	public ResponseEntity<?> getStruct(HttpServletRequest request) throws Exception {

		try {
			String pid = request.getParameter("pid");
			if(StringUtils.isNotBlank(pid)) {
				Query query = new Query();
				query.addCriteria(Criteria.where("pid").is(pid));
				List<FileEntity> dbEntities = mongoTemplate.find(query, FileEntity.class);
				if(!CollectionUtils.isEmpty(dbEntities)) {
					List<String> files = new ArrayList<>();
					dbEntities.forEach(e -> {files.add(e.getPath());});
					return ResponseEntity.ok(files);
				} else {
					return ResponseEntity.ok("no data found");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("ok");
	}
	
	protected void scanDirectory(File file, String pid) throws IOException {
		for(File subFile : file.listFiles()) {
			if(subFile.exists()) {
				FileEntity subEntity = FileEntityMapper.map(subFile);
				subEntity.setPid(pid);
				mongoTemplate.save(subEntity);
				if(subFile.isDirectory()) {
					scanDirectory(subFile, subEntity.getId());
				}
			}
			
		}
	}
}
