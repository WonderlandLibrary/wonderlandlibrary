package viaversion.viaversion.api.data;

public class StoredObject {
    private final UserConnection user;

    public StoredObject(UserConnection user) {
        this.user = user;
    }

    public UserConnection getUser() {
        return user;
    }
}
