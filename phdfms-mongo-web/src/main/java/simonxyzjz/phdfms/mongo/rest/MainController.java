package simonxyzjz.phdfms.mongo.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import simonxyzjz.phdfms.mongo.domain.FileEntity;
import simonxyzjz.phdfms.mongo.mapper.FileEntityMapper;
import simonxyzjz.phdfms.mongo.service.FileScanService;


@Controller
@RequestMapping("/api/v1")
public class MainController {

	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private FileScanService fileScanService;
	

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
