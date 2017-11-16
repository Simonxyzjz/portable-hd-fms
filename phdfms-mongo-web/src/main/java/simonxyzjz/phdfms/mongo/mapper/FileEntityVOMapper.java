package simonxyzjz.phdfms.mongo.mapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import simonxyzjz.phdfms.mongo.domain.FileEntity;
import simonxyzjz.phdfms.mongo.vo.FileEntityVO;

public class FileEntityVOMapper {
	
	public static FileEntityVO map(FileEntity entity) throws IOException {
		FileEntityVO vo = new FileEntityVO();
		BeanUtils.copyProperties(entity, vo);
		
		vo.setLastModifiedDate(entity.getLastModified()==null?null:new Date(entity.getLastModified()));
		vo.setSize(toByteUnit(entity.getLength()));
		vo.setCreatedDate(entity.getCreatedAt()==null?null:new Date(entity.getCreatedAt()));
		vo.setUpdatedDate(entity.getUpdatedAt()==null?null:new Date(entity.getUpdatedAt()));
		return vo;
	}
	
	public static String toByteUnit(Long value) {
		if(value == null || value < 0) {
			return null;
		}
		
		if(value >= 1024 && value < 1024*1024) {
			return new BigDecimal(value/1024D).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"KB";
		}
		else if(value >= 1024*1024 && value < 1024*1024*1024) {
			return new BigDecimal(value/(1024D*1024D)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"MB";
		}
		else if(value >= 1024*1024*1024) {
			return new BigDecimal(value/(1024D*1024D*1024D)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"GB";
		}
		else {
			return value+"B";
		}
	}
}
