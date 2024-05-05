package io.github.raze.modules.collection.misc;

import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.utilities.collection.visual.ChatUtil;

public class NameProtect extends AbstractModule {

    public NameProtect() {
        super("NameProtect", "Hides names in the chat for you.", ModuleCategory.MISC);
    }

    private boolean wasMessageSaidOnce;

    @Override
    public void onEnable() {
        if(!wasMessageSaidOnce) {
            ChatUtil.addChatMessage("Change the custom name with the .nameprotect command!", true);
            wasMessageSaidOnce = true;
        }
    }

}
