package tech.drainwalk.client.module.modules.overlay;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.util.math.MathHelper;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.ColorOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.events.EventRender2D;

import java.awt.*;
import java.util.Objects;

public class TargetHUD extends Module {
    private boolean show;
    private double healthBarWidth;
    private String enemyNickname;
    private double enemyHP;
    private double enemyDistance;
    private EntityPlayer entityPlayer;
    private Entity entity;
    public static ColorOption color = new ColorOption("Color", -1);
    private final SelectOption mode = new SelectOption("Mode", 0,
            new SelectOptionValue("Type 1"),
            new SelectOptionValue("Type 2"),
            new SelectOptionValue("Type 3")
    );


    public TargetHUD() {
        super("TargetHUD", Category.OVERLAY);
        register(mode, color);
    }
    @EventTarget
    public void onRender(EventRender2D e) {
            ScaledResolution sr = new ScaledResolution(mc);
        double posX = 60;
        double posY = -250;
            final float scaledWidth = sr.getScaledWidth();

            final float x = (float) (scaledWidth / 2.0f - posX);
            final float y = (float) (scaledWidth / 2.0f + posY);
        final float health = Math.round(enemyHP);
        double hpPercentage = health / 20;

        hpPercentage = MathHelper.clamp(hpPercentage, 0, 1);
        final double hpWidth = 97.0 * hpPercentage;

        final String healthStr = String.valueOf(Math.round(enemyHP));
        if (mode.getValueByIndex(0)) {

            Gui.drawRect((int) (x + 120.5), (int) (y - 25.5), (int) (x + 270), (int) (y + 30.5f), new Color(25, 25, 25, 150).getRGB());
            Gui.drawRect((int) (x + 130.0f), (int) (y - 22.0f), (int) (x + 260.0f), (int) (y - 15.0f), new Color(255, 0, 0, 200).getRGB());
            Gui.drawRect((int) (x + 166.0f), (int) (y + 6.0f), (int) (x + 166.0f + this.healthBarWidth), (int) (y + 15.0f), new Color(255, 255, 255, 255).getRGB());

            Gui.drawRect((int) (x + 130.0f), (int) (y - 22.0f), (int) (x + 163.0f + hpWidth), (int) (y - 15.0f), new Color(0, 219, 255, 255).getRGB());

            mc.fontRenderer.drawStringWithShadow(healthStr, x + 128.0f + 46.0f - mc.fontRenderer.getStringWidth(healthStr) / 2.0f, y + 6.0f, -1);
            mc.fontRenderer.drawStringWithShadow("\u2764", x + 128.0f + 46.0f + mc.fontRenderer.getStringWidth(healthStr), y + 6f, new Color(255, 0, 0, 255).getRGB());
            mc.fontRenderer.drawStringWithShadow(entity.getName(), x + 167, y - 5.0f, -1);

        }
    }
}