package net.minecraft.command.server;

import java.util.concurrent.Callable;

class CommandBlockLogic.2
implements Callable<String> {
    CommandBlockLogic.2() {
    }

    public String call() throws Exception {
        return CommandBlockLogic.this.getName();
    }
}
