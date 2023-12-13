// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.util.render.RenderUtils;
import java.awt.Color;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import java.text.DecimalFormat;
import vip.Resolute.events.impl.EventAttack;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class Reach extends Module
{
    public static NumberSetting reach;
    public BooleanSetting display;
    public BooleanSetting misplace;
    public NumberSetting x;
    public NumberSetting y;
    public static boolean enabled;
    String distance;
    float dragX;
    float dragY;
    
    public Reach() {
        super("Reach", 0, "Allows farther reach", Category.COMBAT);
        this.display = new BooleanSetting("Display", false);
        this.misplace = new BooleanSetting("Misplace", false);
        this.x = new NumberSetting("X", 1.0, 0.0, 100.0, 1.0);
        this.y = new NumberSetting("Y", 1.0, 0.0, 100.0, 1.0);
        this.distance = "0.00";
        this.dragX = 0.0f;
        this.dragY = 0.0f;
        this.addSettings(Reach.reach, this.x, this.y, this.display, this.misplace);
    }
    
    @Override
    public void onEnable() {
        Reach.enabled = true;
    }
    
    @Override
    public void onDisable() {
        Reach.enabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("" + Reach.reach.getValue());
        if (e instanceof EventAttack) {
            final EventAttack event = (EventAttack)e;
            if (event.getTarget() != null) {
                final DecimalFormat df = new DecimalFormat("0.00");
                this.distance = df.format(Reach.mc.thePlayer.getDistanceToEntity(event.getTarget()));
            }
        }
        if (e instanceof EventRender2D) {
            final EventRender2D event2 = (EventRender2D)e;
            final ScaledResolution sr = new ScaledResolution(Reach.mc);
            float realX = Double.valueOf(this.x.getValue()).floatValue() / 100.0f * sr.getScaledWidth();
            float realY = Double.valueOf(this.y.getValue()).floatValue() / 100.0f * sr.getScaledHeight();
            final int mouseX = Mouse.getX() * sr.getScaledWidth() / Reach.mc.displayWidth;
            final int mouseY = sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / Reach.mc.displayHeight - 1;
            final boolean isLeftKeyDown = Mouse.isButtonDown(0);
            if (this.display.isEnabled()) {
                if (mouseX >= realX && mouseX <= realX + 25.0f && mouseY >= realY && mouseY <= realY + 15.0f && isLeftKeyDown && !Mouse.isGrabbed()) {
                    if (this.dragX == 0.0f && this.dragY == 0.0f) {
                        this.dragX = mouseX - realX;
                        this.dragY = mouseY - realY;
                    }
                    else {
                        realX = mouseX - this.dragX;
                        realY = mouseY - this.dragY;
                    }
                    this.x.setValue(realX / sr.getScaledWidth() * 100.0f);
                    this.y.setValue(realY / sr.getScaledHeight() * 100.0f);
                }
                else if (this.dragX != 0.0f || this.dragY != 0.0f) {
                    this.dragX = 0.0f;
                    this.dragY = 0.0f;
                }
                RenderUtils.drawRect(realX, realY, realX + 25.0f, realY + 15.0f, new Color(0, 0, 0, 150).getRGB());
                Reach.mc.fontRendererObj.drawStringWithShadow(this.distance, realX + 1.0f, realY + 2.0f, -1);
            }
        }
        if (e instanceof EventUpdate && this.misplace.isEnabled()) {
            for (final Object object : Reach.mc.theWorld.loadedEntityList) {
                final Entity o = (Entity)object;
                if (o.getName() == Reach.mc.thePlayer.getName()) {
                    continue;
                }
                final double oldX = o.posX;
                final double oldY = o.posY;
                final double oldZ = o.posZ;
                if (Reach.mc.thePlayer.getDistanceToEntity(o) > Reach.reach.getValue() || Reach.mc.thePlayer.getDistanceToEntity(o) <= 2.0f) {
                    continue;
                }
                final double mx = Math.cos(Math.toRadians(Reach.mc.thePlayer.rotationYaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(Reach.mc.thePlayer.rotationYaw + 90.0f));
                o.setPosition(oldX - mx / Reach.mc.thePlayer.getDistanceToEntity(o) * 0.5, oldY, oldZ - mz / Reach.mc.thePlayer.getDistanceToEntity(o) * 0.5);
            }
        }
    }
    
    static {
        Reach.reach = new NumberSetting("Reach", 3.3, 3.0, 6.0, 0.05);
        Reach.enabled = false;
    }
}
