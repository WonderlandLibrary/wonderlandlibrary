package summer.cheat.cheats.render;

import net.minecraft.client.Minecraft;
import summer.Summer;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.RenderUtils;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.render.EventRender2D;
import summer.cheat.guiutil.Setting;

import java.awt.*;

public class Keystrokes extends Cheats {
    public int color;

    public Setting keyX;
    public Setting keyY;
    public Setting mouseButtons;

    public Keystrokes() {
        super("Keystrokes", "", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(keyX = new Setting("Key X", this, 1, 1, 897, false));
        Summer.INSTANCE.settingsManager.Property(keyY = new Setting("Key Y", this, 1, -105, 340, false));
        Summer.INSTANCE.settingsManager.Property(mouseButtons = new Setting("Mouse Buttons", this, false));
    }

    @EventTarget
    public void onRender(EventRender2D e) {
        if (!mc.gameSettings.showDebugInfo) {
            int x = keyX.getValInt();
            int y = keyY.getValInt();
            color = Color.getHSBColor(HUD.hudHue.getValFloat(), 1.0F, 1.0F).getRGB();
            RenderUtils.drawRect(x + 20, y + 111, x + 41, y + 130, 0x90000000);
            RenderUtils.drawRect(x + 1, y + 130, x + 61, y + 150, 0x90000000);
            if (mc.gameSettings.keyBindForward.pressed) {
                RenderUtils.drawRect(x + 21, y + 112, x + 40, y + 130, color);
            }
            if (mc.gameSettings.keyBindBack.pressed) {
                RenderUtils.drawRect(x + 21, y + 131, x + 40, y + 149, color);
            }
            if (mc.gameSettings.keyBindLeft.pressed) {
                RenderUtils.drawRect(x + 2, y + 131, x + 20, y + 149, color);
            }
            if (mc.gameSettings.keyBindRight.pressed) {
                RenderUtils.drawRect(x + 41, y + 131, x + 60, y + 149, color);
            }
            if (mouseButtons.getValBoolean()) {
                RenderUtils.drawRect(x + 30, y + 170, x + 61, y + 150, 0x90000000);
                RenderUtils.drawRect(x + 1, y + 170, x + 30, y + 150, 0x90000000);
                if (mc.gameSettings.keyBindAttack.pressed) {
                    RenderUtils.drawRect(x + 2, y + 150, x + 30, y + 169, color);
                }
                if (mc.gameSettings.keyBindUseItem.pressed) {
                    RenderUtils.drawRect(x + 60, y + 150, x + 31, y + 169, color);
                }
                Minecraft.fontRendererObj.drawStringWithShadow("LMB", x + 7, y + 156, 0xffffffff);
                Minecraft.fontRendererObj.drawStringWithShadow("RMB", x + 37, y + 156, 0xffffffff);
            }
            Minecraft.fontRendererObj.drawStringWithShadow("W", x + 28, y + 117, 0xffffffff);
            Minecraft.fontRendererObj.drawStringWithShadow("A", x + 8, y + 136, 0xffffffff);
            Minecraft.fontRendererObj.drawStringWithShadow("S", x + 28, y + 136, 0xffffffff);
            Minecraft.fontRendererObj.drawStringWithShadow("D", x + 48, y + 136, 0xffffffff);
        }
    }
}
