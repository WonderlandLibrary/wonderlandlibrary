package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;

class ResourcePackListEntry.1
implements GuiYesNoCallback {
    ResourcePackListEntry.1() {
    }

    public void confirmClicked(boolean result, int id) {
        List list2 = ResourcePackListEntry.this.resourcePacksGUI.getListContaining(ResourcePackListEntry.this);
        ResourcePackListEntry.this.mc.displayGuiScreen((GuiScreen)ResourcePackListEntry.this.resourcePacksGUI);
        if (result) {
            list2.remove((Object)ResourcePackListEntry.this);
            ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(0, (Object)ResourcePackListEntry.this);
        }
    }
}
