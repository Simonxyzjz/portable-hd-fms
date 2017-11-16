package simonxyzjz.phdfms.mongo.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Role implements Serializable {
	private static final long serialVersionUID = -6140090613812307452L;

	private Integer id;

	private String roledesc;

	private Integer selected;
}