package net.java.games.input;

import java.security.PrivilegedAction;

final class DefaultControllerEnvironment$3 implements PrivilegedAction {
    // $FF: synthetic field
    private final String val$property;
    // $FF: synthetic field
    private final String val$default_value;

    DefaultControllerEnvironment$3(String var1, String var2) {
        this.val$property = var1;
        this.val$default_value = var2;
    }

    public Object run() {
        return System.getProperty(this.val$property, this.val$default_value);
    }
}
