package simonxyzjz.phdfms.mongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection="temp_test")
@Getter
@Setter
@NoArgsConstructor
public class FileEntity {
	
	@Id
	private String id;
	
	private String pid;
	
	private String name;
	
	private String path;
	
	private Long lastModified;
	
	private boolean directory;
	
	private boolean writable;
	
	private boolean readable;
	
	private boolean executable;
	
	private boolean exists;
	
	private boolean hidden;
	
	private boolean absolute;
	
	private Long length;
	
	private String parent;
	
	
	/**
	 * 文件指纹
	 */
	private String md5;
	
	/**
	 * 文件扩展名
	 */
	private String ext;
	
	/**
	 * 记录创建时间戳
	 */
	private Long createdAt;

	/**
	 * 记录更新时间戳
	 */
	private Long updatedAt;

}
