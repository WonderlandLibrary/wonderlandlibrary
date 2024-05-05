package net.java.games.input;

final class ShutdownHook extends Thread {
    // $FF: synthetic field
    private final DirectInputEnvironmentPlugin this$0;

    private ShutdownHook(DirectInputEnvironmentPlugin var1) {
        this.this$0 = var1;
    }

    public final void run() {
        for(int i = 0; i < DirectInputEnvironmentPlugin.access$200(this.this$0).size(); ++i) {
            IDirectInputDevice device = (IDirectInputDevice)DirectInputEnvironmentPlugin.access$200(this.this$0).get(i);
            device.release();
        }

    }

    // $FF: synthetic method
    ShutdownHook(DirectInputEnvironmentPlugin x0, Object x1) {
        this(x0);
    }
}
