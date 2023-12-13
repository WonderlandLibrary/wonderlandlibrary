package me.aidanmees.trivia.hook;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.ClickEvent;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiEditSignHook
extends GuiEditSign {
    public GuiEditSignHook(TileEntitySign p_i1097_1_) {
        super(p_i1097_1_);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done", new Object[0]));
        this.buttonList.add(this.doneBtn);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 24, "Force OP"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120 + 24 + 24, "Freeze"));
        this.tileSign.setEditable(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.tileSign.markDirty();
                this.mc.displayGuiScreen(null);
            }
            if (button.id == 1) {
            	
                this.tileSign.signText[0].getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/op " + this.mc.thePlayer.getName()));
                this.tileSign.signText[1] = new ChatComponentText("");
                this.tileSign.signText[2] = new ChatComponentText("");
                this.tileSign.markDirty();
                this.mc.displayGuiScreen(null);
            }
            if (button.id == 2) {
                int i = 0;
                while (i < this.tileSign.signText.length) {
                    this.tileSign.signText[i] = new ChatComponentText(StringUtils.repeat((String)"#", (int)32764));
                    ++i;
                }
                this.tileSign.markDirty();
                this.mc.displayGuiScreen(null);
            }
        }
    }
}