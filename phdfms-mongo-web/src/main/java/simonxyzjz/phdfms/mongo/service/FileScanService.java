package simonxyzjz.phdfms.mongo.service;

public interface FileScanService {
	
	void scanAndUpdateById(String id) throws Exception;
	
	void scanAndUpdateByPath(String path) throws Exception;
}
