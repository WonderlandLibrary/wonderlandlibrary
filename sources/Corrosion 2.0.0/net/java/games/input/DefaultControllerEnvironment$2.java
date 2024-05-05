package net.java.games.input;

import java.security.PrivilegedAction;

final class DefaultControllerEnvironment$2 implements PrivilegedAction {
    // $FF: synthetic field
    private final String val$property;

    DefaultControllerEnvironment$2(String var1) {
        this.val$property = var1;
    }

    public Object run() {
        return System.getProperty(this.val$property);
    }
}
