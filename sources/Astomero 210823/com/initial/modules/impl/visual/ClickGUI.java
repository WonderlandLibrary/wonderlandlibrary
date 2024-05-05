package com.initial.modules.impl.visual;

import com.initial.modules.*;
import com.initial.clickguis.*;
import net.minecraft.client.gui.*;

public class ClickGUI extends Module
{
    public ClickGUI() {
        super("ClickGUI", 54, Category.VISUAL);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.mc.displayGuiScreen(new AstolfoGUI());
        this.toggle();
    }
}
