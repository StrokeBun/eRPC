package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
@Getter
@Setter
public class User implements Serializable {
    private static final  long serialVersionUID = 1L;
    private int id;
    private String username;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
