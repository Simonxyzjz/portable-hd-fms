package simonxyzjz.phdfms.mongo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TempFileEntity {
	private String path;
	private String name;
	private String parent;
	private long length;
	private long lastModified;
	private boolean directory;
}
