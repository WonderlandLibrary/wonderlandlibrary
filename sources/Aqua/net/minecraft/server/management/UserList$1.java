package net.minecraft.server.management;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import net.minecraft.server.management.UserListEntry;

static final class UserList.1
implements ParameterizedType {
    UserList.1() {
    }

    public Type[] getActualTypeArguments() {
        return new Type[]{UserListEntry.class};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
