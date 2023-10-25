package net.minecraft.client.gui;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList
extends GuiListExtended {
    private final GuiControls field_148191_k;
    private final Minecraft mc;
    private final GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth = 0;

    public GuiKeyBindingList(GuiControls controls, Minecraft mcIn) {
        super(mcIn, GuiControls.width, GuiControls.height, 63, GuiControls.height - 32, 20);
        this.field_148191_k = controls;
        this.mc = mcIn;
        KeyBinding[] akeybinding = (KeyBinding[])ArrayUtils.clone((Object[])mcIn.gameSettings.keyBindings);
        this.listEntries = new GuiListExtended.IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
        Arrays.sort((Object[])akeybinding);
        int i = 0;
        String s = null;
        for (KeyBinding keybinding : akeybinding) {
            int j;
            String s1 = keybinding.getKeyCategory();
            if (!s1.equals(s)) {
                s = s1;
                this.listEntries[i++] = new CategoryEntry(this, s1);
            }
            if ((j = mcIn.fontRendererObj.getStringWidth(I18n.format((String)keybinding.getKeyDescription(), (Object[])new Object[0]))) > this.maxListLabelWidth) {
                this.maxListLabelWidth = j;
            }
            this.listEntries[i++] = new KeyEntry(this, keybinding, null);
        }
    }

    protected int getSize() {
        return this.listEntries.length;
    }

    public GuiListExtended.IGuiListEntry getListEntry(int index) {
        return this.listEntries[index];
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    static /* synthetic */ Minecraft access$100(GuiKeyBindingList x0) {
        return x0.mc;
    }

    static /* synthetic */ GuiControls access$200(GuiKeyBindingList x0) {
        return x0.field_148191_k;
    }

    static /* synthetic */ int access$300(GuiKeyBindingList x0) {
        return x0.maxListLabelWidth;
    }
}
