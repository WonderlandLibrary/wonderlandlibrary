package com.canon.majik.impl.ui.clickgui.item.items;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import com.canon.majik.impl.ui.clickgui.item.Item;
import org.lwjgl.input.Keyboard;

public class ItemKeyBind extends Item<Module> {
    private boolean pendingKey;

    public ItemKeyBind(Module module, int x, int y, int width, int height) {
        super(module, x, y, width, height);
    }

    @Override
    public int drawScreen(int mouseX, int mouseY, float partialTicks, int offset) {
        this.offset = offset;
        float y = this.y + offset;

        RenderUtils.rect(x, y, width, height, 0x80000000);
        RenderUtils.rect(x,y,width - 129,height, ClickGui.instance.color.getValue().getRGB());
        if(ClickGui.instance.cfont.getValue()){
            Initializer.CFont.drawStringWithShadow(pendingKey ? "Press Key..." : "Bind [" + Keyboard.getKeyName(getObject().getKey()) + "]",
                    x + 5,
                    y + height / 2f - Initializer.CFont.getHeight() / 2f,
                    -1);
        }else {
            mc.fontRenderer.drawStringWithShadow(pendingKey ? "Press Key..." : "Bind [" + Keyboard.getKeyName(getObject().getKey()) + "]",
                    x + 5,
                    y + height / 2f - mc.fontRenderer.FONT_HEIGHT / 2f,
                    -1);
        }
        return height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (bounding(mouseX, mouseY)) {
            if (mouseButton == 0)
                pendingKey = !pendingKey;
        } else
            pendingKey = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (pendingKey) {
            if (keyCode == Keyboard.KEY_DELETE)
                getObject().setKey(Keyboard.KEY_NONE);
            else
                getObject().setKey(keyCode);
            pendingKey = false;
        }
    }
}
