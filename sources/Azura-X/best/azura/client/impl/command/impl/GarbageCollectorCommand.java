package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;
import best.azura.client.util.other.ChatUtil;

public class GarbageCollectorCommand extends ACommand {

    @Override
    public String getName() {
        return "garbagecollect";
    }

    @Override
    public String getDescription() {
        return "Runs java's garbage collector to fix memory issues.";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "gc" };
    }

    @Override
    public void handleCommand(String[] args) {
        final long beginTime = System.currentTimeMillis();
        ChatUtil.sendDebug("Beginning garbage collection...");
        System.gc();
        ChatUtil.sendDebug("Finished garbage collection after " + (System.currentTimeMillis() - beginTime) + " ms.");
    }
}