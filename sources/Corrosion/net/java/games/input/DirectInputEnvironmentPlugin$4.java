package net.java.games.input;

import java.security.PrivilegedAction;

class DirectInputEnvironmentPlugin$4 implements PrivilegedAction {
    // $FF: synthetic field
    private final DirectInputEnvironmentPlugin this$0;

    DirectInputEnvironmentPlugin$4(DirectInputEnvironmentPlugin var1) {
        this.this$0 = var1;
    }

    public final Object run() {
        Runtime.getRuntime().addShutdownHook(this.this$0.new ShutdownHook());
        return null;
    }
}
