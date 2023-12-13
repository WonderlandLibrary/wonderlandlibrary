// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraft.util.text.ITextComponent;
import java.io.IOException;
import net.minecraft.util.text.TextComponentString;
import com.elementars.eclient.command.Command;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntitySign;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import com.elementars.eclient.event.events.GuiScreenEvent;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class ColorSign extends Module
{
    public ColorSign() {
        super("ColorSign", "Allows writing with colors on signs", 0, Category.MISC, true);
    }
    
    @EventTarget
    private void onGui(final GuiScreenEvent.Displayed event) {
        if (event.getScreen() instanceof GuiEditSign && this.isToggled()) {
            event.setScreen((GuiScreen)new KamiGuiEditSign(((GuiEditSign)event.getScreen()).tileSign));
        }
    }
    
    private class KamiGuiEditSign extends GuiEditSign
    {
        public KamiGuiEditSign(final TileEntitySign teSign) {
            super(teSign);
        }
        
        public void initGui() {
            super.initGui();
        }
        
        protected void actionPerformed(final GuiButton button) throws IOException {
            if (button.id == 0) {
                this.tileSign.signText[this.editLine] = (ITextComponent)new TextComponentString(this.tileSign.signText[this.editLine].getFormattedText().replaceAll("(" + Command.SECTIONSIGN() + ")(.)", "$1$1$2$2"));
            }
            super.actionPerformed(button);
        }
        
        protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            String s = ((TextComponentString)this.tileSign.signText[this.editLine]).getText();
            s = s.replace("&", Command.SECTIONSIGN() + "");
            this.tileSign.signText[this.editLine] = (ITextComponent)new TextComponentString(s);
        }
    }
}
