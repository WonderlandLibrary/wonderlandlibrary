// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.event.impl.game.WorldEvent;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.render.Render2DEvent;
import org.apache.commons.lang3.StringUtils;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.utils.player.ChatUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.utils.animations.impl.EaseInOutQuad;
import dev.tenacity.module.Category;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class AutoAuthenticate extends Module
{
    private final NumberSetting delay;
    private final StringSetting password;
    private final Animation animation;
    private final String[] PASSWORD_PLACEHOLDERS;
    private long runAt;
    private long startAt;
    private String runCommand;
    private HUDMod hudMod;
    
    public AutoAuthenticate() {
        super("AutoAuthenticate", "Auto Authenticate", Category.MISC, "Auto login/register on cracked servers");
        this.delay = new NumberSetting("Delay", 2000.0, 5000.0, 0.0, 100.0);
        this.password = new StringSetting("Password", "123123.");
        this.animation = new EaseInOutQuad(500, 1.0);
        this.PASSWORD_PLACEHOLDERS = new String[] { "password", "pass", "contrasena", "contrase\u00f1a" };
        this.addSettings(this.delay, this.password);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (this.runAt < System.currentTimeMillis() && this.runCommand != null) {
            this.animation.setDirection(Direction.BACKWARDS);
            ChatUtil.send(this.runCommand);
            this.reset();
        }
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
        final String msg = event.message.getUnformattedText();
        final String password = this.password.getString();
        final int passCount = this.count(msg);
        if (passCount > 0) {
            if (msg.contains("/register ")) {
                this.setRun("/register " + StringUtils.repeat(password + " ", passCount));
            }
            else if (msg.contains("/login ")) {
                this.setRun("/login " + StringUtils.repeat(password + " ", passCount));
            }
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if ((this.runAt > System.currentTimeMillis() && this.runCommand != null) || !this.animation.isDone()) {
            if (this.hudMod == null) {
                this.hudMod = Tenacity.INSTANCE.getModuleCollection().getModule(HUDMod.class);
            }
            final ScaledResolution sr = new ScaledResolution(AutoAuthenticate.mc);
            final float width = 120.0f;
            final float height = 5.0f;
            final float width2 = width / 2.0f;
            final float calc = (this.runAt == 0L) ? 1.0f : ((System.currentTimeMillis() - this.startAt) / (float)(this.runAt - this.startAt));
            final float scale = this.animation.getOutput().floatValue();
            final float left = sr.getScaledWidth() / 2.0f / scale - width2;
            float top = sr.getScaledHeight() / 2.0f + 30.0f;
            final float bottom = (sr.getScaledHeight() / 2.0f + 30.0f) / scale + height;
            float sw2 = sr.getScaledWidth() / 2.0f;
            top /= scale;
            sw2 /= scale;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            final Color color = HUDMod.getClientColors().getFirst();
            Gui.drawRect(left, top, sw2 + width2, bottom, color.darker().darker().getRGB());
            Gui.drawRect(left, top, sw2 - width2 + width * calc, bottom, color.getRGB());
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        this.reset();
    }
    
    @Override
    public void onEnable() {
        this.reset();
        super.onEnable();
    }
    
    private int count(String data) {
        int count = 0;
        data = data.toLowerCase();
        for (final String pass : this.PASSWORD_PLACEHOLDERS) {
            count += StringUtils.countMatches((CharSequence)data, (CharSequence)pass);
        }
        return count;
    }
    
    private void setRun(final String runCommand) {
        final long currentTimeMillis = System.currentTimeMillis();
        this.animation.setDirection(Direction.FORWARDS);
        this.startAt = currentTimeMillis;
        this.runAt = currentTimeMillis + this.delay.getValue().longValue();
        this.runCommand = runCommand.trim();
    }
    
    private void reset() {
        this.animation.setDirection(Direction.BACKWARDS);
        final long n = 0L;
        this.runAt = n;
        this.startAt = n;
        this.runCommand = null;
    }
}
