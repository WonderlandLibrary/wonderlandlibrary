package dev.rise.script;

import dev.rise.event.api.Event;
import dev.rise.script.block.impl.event.EventBlock;

import java.util.ArrayList;
import java.util.List;

public final class ScriptManager {

    private final List<Script> scripts = new ArrayList<>();

    public ScriptManager() {
        //this.scripts.add(new Script("Cock and ball torture", "balls", "Cocklasso"));
    }

    public void handleEvent(final Event event) {
        for (final Script script : scripts) {
            for (final EventBlock block : script.getBlocks()) {
                if (block.getClazz() == event.getClass()) {
                    block.run(event);
                }
            }
        }
    }
}
