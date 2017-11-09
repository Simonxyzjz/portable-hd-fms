package simonxyzjz.phdfms.mongo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import simonxyzjz.phdfms.mongo.domain.FileEntity;
import simonxyzjz.phdfms.mongo.mapper.FileEntityMapper;
import simonxyzjz.phdfms.mongo.service.FileScanService;

@Slf4j
@Service
public class FileScanServiceImpl implements FileScanService {

	@Autowired
	public MongoTemplate mongoTemplate;

	public void scanAndUpdateById(String id) throws Exception {

		FileEntity main = mongoTemplate.findById(new ObjectId(id), FileEntity.class);
		if (main == null) {
			log.warn("no document found for id={}", id);
			return;
		}
		
		scanAndUpdate(main);

	}

	protected void scanAndUpdate(FileEntity entity) throws IOException {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("pid").is(entity.getId()));
		query.addCriteria(Criteria.where("exists").is(true));
		List<FileEntity> dbEntities = mongoTemplate.find(query, FileEntity.class);
		
		// 校验之前， 先递归检查本目录下有无增加文件
		File mainDir = FileUtils.getFile(entity.getPath());
		storeNewFilesToDb(mainDir, entity);
		
		// 递归检查是否有文件被修改或删除
		for (FileEntity dbe : dbEntities) {
			File file = FileUtils.getFile(dbe.getPath());

			if (file.exists()) {

				if (dbe.isDirectory()) {
					scanAndUpdate(dbe);
				}

				if (file.lastModified() != dbe.getLastModified()) {
					log.warn("file modified, update db[id={}]", dbe.getId());
					dbe.setLastModified(file.lastModified());
					dbe.setUpdatedAt(System.currentTimeMillis());
					dbe.setMd5(FileEntityMapper.getMd5(file));
					mongoTemplate.save(dbe);
				}

			} else {
				if(dbe.isDirectory()) {
					updateNotExistsForDir(dbe);
				}
				log.warn("file not exists, update db[id={}]", dbe.getId());
				dbe.setExists(false);
				dbe.setUpdatedAt(System.currentTimeMillis());
				mongoTemplate.save(dbe);
			}
		}
	}
	
	protected void storeNewFilesToDb(File dir, FileEntity dirEntity) throws IOException {

		for(File tmpFile : dir.listFiles()) {
			Query query = new Query();
			query.addCriteria(Criteria.where("pid").is(dirEntity.getId()));
			query.addCriteria(Criteria.where("name").is(tmpFile.getName()));
			query.addCriteria(Criteria.where("exists").is(true));
			FileEntity dbEntity = mongoTemplate.findOne(query, FileEntity.class);
			if(dbEntity == null) {
				log.info("new file found, save to db[path={}]", tmpFile.getPath());
				dbEntity = FileEntityMapper.map(tmpFile);
				dbEntity.setPid(dirEntity.getId());
				mongoTemplate.save(dbEntity);
				if(tmpFile.isDirectory()) {
					scanAndSaveNewFiles(tmpFile, dbEntity.getId());
				}
			}
		}
	}
	
	protected void scanAndSaveNewFiles(File file, String pid) throws IOException {
		for(File subFile : file.listFiles()) {
			if(subFile.exists()) {
				log.info("new file found, save to db[path={}]", subFile.getPath());
				FileEntity subEntity = FileEntityMapper.map(subFile);
				subEntity.setPid(pid);
				mongoTemplate.save(subEntity);
				if(subFile.isDirectory()) {
					scanAndSaveNewFiles(subFile, subEntity.getId());
				}
			}
			
		}
	}
	
	public void updateNotExistsForDir(FileEntity entity) {
		Query query = new Query();
		query.addCriteria(Criteria.where("pid").is(entity.getId()));
		query.addCriteria(Criteria.where("exists").is(true));
		List<FileEntity> dbEntities = mongoTemplate.find(query, FileEntity.class);
		for (FileEntity dbe : dbEntities) {
			if(dbe.isDirectory()) {
				updateNotExistsForDir(dbe);
			}
			log.warn("file not exists, update db[id={},path={}]", dbe.getId(), dbe.getPath());
			dbe.setExists(false);
			dbe.setUpdatedAt(System.currentTimeMillis());
			mongoTemplate.save(dbe);
		}
	}

	public void scanAndUpdateByPath(String path) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("path").is(path));
		List<FileEntity> main = mongoTemplate.find(query, FileEntity.class);
		if (main == null || main.size() > 1) {
			log.warn("no document found or too many documents for path={}", path);
			return;
		}
		
		scanAndUpdate(main.get(0));
	}
}
