package koks.api.manager.cl;

import de.liquiddev.ircclient.client.IrcPlayer;
import de.liquiddev.ircclient.client.IrcRank;
import god.buddy.aot.BCompiler;
import koks.Koks;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class CLManager {

    final User user;

    public CLManager(String name) {
        user = new User(name);
    }

    public String getRolePrefix() {
        return getRolePrefix(user.getName());
    }

    public String getRolePrefix(String name) {
        final Role role = user.getRole(name);
        return role.getColor() + "§l" + Koks.name + " " + role.getName() + " §7| " + role.getColor() + "§l";
    }

    public String getPrefix() {
        return getRolePrefix() + user.getName() + "§r";
    }

    public User getUser() {
        return user;
    }
}
