package simonxyzjz.phdfms.mongo.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TempVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String path;
	private Integer filter;
	private String lastModified;
	private String createdAt;
	private Boolean directory;
	private String md5;
}
