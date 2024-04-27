package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import me.AquaVit.liquidSense.utils.BlurBuffer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.script.api.global.Chat;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;



@ElementInfo(name = "KeyBinds")
public class KeyBinds extends Element {


    @Nullable
    @Override
    public Border drawElement() {
        int index = 0;
        for(Module module : LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == Keyboard.KEY_NONE)
                continue;
            index++;
        }
        BlurBuffer.blurArea((int) ((-4.5F + this.getRenderX()) * this.getScale()),
                (int) ((this.getRenderY() + Fonts.csgo40.FONT_HEIGHT - 2) * this.getScale()),
                (Fonts.csgo40.getStringWidth("F") + Fonts.font40.getStringWidth("Binds") + 67) * this.getScale(),
                (8 + index * 14) * this.getScale(),
                true);

        if (!this.getInfo().disableScale())
            GL11.glScalef(this.getScale(), this.getScale(), this.getScale());
        GL11.glTranslated(this.getRenderX(), this.getRenderY(), 0.0);

        int y = 1;
        for(Module module : LiquidBounce.moduleManager.getModules()) {
            if(module.getKeyBind() == Keyboard.KEY_NONE)
                continue;

            GlStateManager.resetColor();

            //RenderUtils.drawRect(0f, 10f + ypos , 150f, 10f + ypos + 12, new Color(35, 35, 35, 255).getRGB());
            if (module.getState()){
                Fonts.font40.drawString(module.getName(), -1.1F, y + 17, Color.WHITE.getRGB());
                Fonts.font40.drawString("on" , Fonts.csgo40.getStringWidth("F") + Fonts.font40.getStringWidth("Binds") + 46F , y + 17, Color.WHITE.getRGB());
            } else {
                Fonts.font40.drawString(module.getName(), -1.1F, y + 17, Color.WHITE.getRGB());
                Fonts.font40.drawString("off", Fonts.csgo40.getStringWidth("F") + Fonts.font40.getStringWidth("Binds") + 45 , y + 17, Color.WHITE.getRGB());

            }
            y += 14;
        }
        RenderUtils.drawBorderedRect(-5.5F, -5.5F, Fonts.csgo40.getStringWidth("K") + Fonts.font40.getStringWidth("Binds") + 60, Fonts.csgo40.FONT_HEIGHT + 0.5F, 3F, new Color(16, 25, 32, 200).getRGB(), new Color(16, 25, 32, 200).getRGB());
        Fonts.csgo40.drawString("K", -0.8F, -2F, new Color(0, 131, 193).getRGB(), false);
        Fonts.font40.drawString("Binds", Fonts.csgo40.getStringWidth("K") + 3, -1F, Color.WHITE.getRGB(), false);
        return new Border(20, 20, 120, 80);
    }
}
