package net.minecraft.command.server;

import java.util.concurrent.Callable;

class CommandBlockLogic.1
implements Callable<String> {
    CommandBlockLogic.1() {
    }

    public String call() throws Exception {
        return CommandBlockLogic.this.getCommand();
    }
}
