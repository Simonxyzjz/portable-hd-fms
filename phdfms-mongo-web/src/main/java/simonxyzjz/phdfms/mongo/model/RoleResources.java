package simonxyzjz.phdfms.mongo.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleResources implements Serializable {
	private static final long serialVersionUID = -8559867942708057891L;
	private Integer roleid;
	private String resourcesid;
}