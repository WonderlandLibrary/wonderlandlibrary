package me.dev.legacy.util;

public abstract class AbstractFriend implements INameable, IFriendable {

    private String name;
    private String alias;

    public AbstractFriend(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.name = name;
    }
}
