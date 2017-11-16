package simonxyzjz.phdfms.mongo.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable{
    private static final long serialVersionUID = -8736616045315083846L;
    
    private Integer id;

    private String username;

    private String password;

    private Integer enable;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                '}';
    }
}