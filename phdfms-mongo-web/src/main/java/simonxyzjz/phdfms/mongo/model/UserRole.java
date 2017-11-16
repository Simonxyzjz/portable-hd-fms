package simonxyzjz.phdfms.mongo.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRole implements Serializable{
    private static final long serialVersionUID = -916411139749530670L;
    private Integer userid;
    private String roleid;
}