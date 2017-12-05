package simonxyzjz.phdfms.mongo.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileEntityVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String diskName;
	private String id;
	private String pid;
	private String name;
	private String path;
	private String lastModifiedDate;
	private boolean directory;
	private boolean writable;
	private boolean readable;
	private boolean executable;
	private boolean exists;
	private boolean hidden;
	private boolean absolute;
	private String size;
	private Long length;
	private String parent;
	private String md5;
	private String ext;
	private String createdDate;
	private String updatedDate;
	
}
