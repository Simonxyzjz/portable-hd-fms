package simonxyzjz.phdfms.mongo.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Resources implements Serializable {
	private static final long serialVersionUID = -6812242071705361506L;

	private Integer id;

	private String name;

	private String resurl;

	private Integer type;

	private Integer parentid;

	private Integer sort;

	private String checked;

	@Override
	public String toString() {
		return "Resources{" + "id=" + id + ", name='" + name + '\'' + ", resurl='" + resurl + '\'' + ", type=" + type
				+ ", parentid=" + parentid + ", sort=" + sort + '}';
	}
}