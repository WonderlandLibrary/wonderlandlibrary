package koks.manager.cl;

/**
 * @author kroko
 * @created on 04.10.2020 : 21:12
 */
public class User {

    private final String username;
    private final Role role;

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
}
