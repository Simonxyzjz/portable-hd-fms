package simonxyzjz.phdfms.mongo.mapper;

import java.io.File;
import java.io.IOException;

import simonxyzjz.phdfms.mongo.domain.TempFileEntity;


public class TempFileEntityMapper {

	public static TempFileEntity map(File file) throws IOException {
		TempFileEntity entity = new TempFileEntity();
		entity.setDirectory(file.isDirectory());
		entity.setLastModified(file.lastModified());
		entity.setLength(file.length());
		entity.setName(file.getName());
		entity.setParent(file.getParent());
		entity.setPath(file.getPath());
		return entity;
	}
}
