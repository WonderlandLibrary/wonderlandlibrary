// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.event.events.EventModelPlayerRender;
import com.elementars.eclient.event.events.EventPostRenderLayers;
import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.event.Event;
import com.elementars.eclient.Xulu;
import java.awt.Color;
import com.elementars.eclient.friend.Friends;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.elementars.eclient.event.events.EventModelRender;
import com.elementars.eclient.util.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Chams extends Module
{
    public static Value<String> mode;
    public final Value<Boolean> hand;
    public final Value<Boolean> lines;
    public final Value<Float> width;
    public final Value<Boolean> friendColor;
    public final Value<Boolean> rainbow;
    public final Value<Integer> r;
    public final Value<Integer> g;
    public final Value<Integer> b;
    public final Value<Integer> a;
    private static Value<Boolean> players;
    private static Value<Boolean> animals;
    private static Value<Boolean> mobs;
    public static Value<Boolean> crystals;
    public final Value<Integer> Vr;
    public final Value<Integer> Vg;
    public final Value<Integer> Vb;
    public final Value<Integer> Wr;
    public final Value<Integer> Wg;
    public final Value<Integer> Wb;
    
    public Chams() {
        super("Chams", "See entities through walls", 0, Category.RENDER, true);
        Chams.mode = this.register(new Value<String>("Mode", this, "ESP", new String[] { "ESP", "Normal", "Walls" }));
        this.Vr = this.register(new Value<Integer>("Visible Red", this, 255, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("Walls"));
        this.Vg = this.register(new Value<Integer>("Visible Green", this, 0, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("Walls"));
        this.Vb = this.register(new Value<Integer>("Visible Blue", this, 0, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("Walls"));
        this.Wr = this.register(new Value<Integer>("Wall Red", this, 0, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("Walls"));
        this.Wg = this.register(new Value<Integer>("Wall Green", this, 255, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("Walls"));
        this.Wb = this.register(new Value<Integer>("Wall Blue", this, 0, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("Walls"));
        this.hand = this.register(new Value<Boolean>("Hand", this, true)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        this.lines = this.register(new Value<Boolean>("Lines", this, false)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        this.width = this.register(new Value<Float>("Width", this, 1.0f, 0.0f, 10.0f)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        this.friendColor = this.register(new Value<Boolean>("Friends", this, true)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        this.r = this.register(new Value<Integer>("Red", this, 0, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        this.g = this.register(new Value<Integer>("Green", this, 255, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        this.b = this.register(new Value<Integer>("Blue", this, 255, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        this.a = this.register(new Value<Integer>("Alpha", this, 63, 0, 255)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP"));
        Chams.players = this.register(new Value<Boolean>("Players", this, true));
        Chams.animals = this.register(new Value<Boolean>("Animals", this, false));
        Chams.mobs = this.register(new Value<Boolean>("Mobs", this, false));
        Chams.crystals = this.register(new Value<Boolean>("Crystals", this, true)).visibleWhen(value -> Chams.mode.getValue().equalsIgnoreCase("ESP") || Chams.mode.getValue().equalsIgnoreCase("Walls"));
    }
    
    public static boolean renderChams(final Entity entity) {
        return !Chams.mode.getValue().equalsIgnoreCase("ESP") && ((entity instanceof EntityPlayer) ? Chams.players.getValue() : (EntityUtil.isPassive(entity) ? Chams.animals.getValue() : ((boolean)Chams.mobs.getValue())));
    }
    
    @EventTarget
    public void renderPre(final EventModelRender event) {
        if (Chams.mode.getValue().equalsIgnoreCase("Walls")) {
            if (event.entity instanceof EntityOtherPlayerMP && !Chams.players.getValue()) {
                return;
            }
            if (EntityUtil.isPassive(event.entity) && !Chams.animals.getValue()) {
                return;
            }
            if (!EntityUtil.isPassive(event.entity) && !Chams.mobs.getValue()) {
                return;
            }
            GlStateManager.pushMatrix();
            GL11.glDisable(2929);
            GL11.glColor4f(this.Wr.getValue() / 255.0f, this.Wg.getValue() / 255.0f, this.Wb.getValue() / 255.0f, 1.0f);
            GL11.glDisable(3553);
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
            GL11.glEnable(2929);
            GL11.glColor4f(this.Vr.getValue() / 255.0f, this.Vg.getValue() / 255.0f, this.Vb.getValue() / 255.0f, 1.0f);
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
            GL11.glEnable(3553);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            event.setCancelled(true);
        }
        else if (Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            final Color c = (this.friendColor.getValue() && Friends.isFriend(event.entity.getName())) ? new Color(0.27f, 0.7f, 0.92f) : (this.rainbow.getValue() ? new Color(Xulu.rgb) : new Color(this.r.getValue(), this.g.getValue(), this.b.getValue()));
            if (event.getEventState() == Event.State.PRE && !(event.entity instanceof EntityOtherPlayerMP)) {
                if (EntityUtil.isPassive(event.entity) && Chams.animals.getValue()) {
                    GL11.glPushMatrix();
                    GL11.glEnable(32823);
                    GL11.glPolygonOffset(1.0f, -100000.0f);
                    GL11.glPushAttrib(1048575);
                    if (!this.lines.getValue()) {
                        GL11.glPolygonMode(1028, 6914);
                    }
                    else {
                        GL11.glPolygonMode(1028, 6913);
                    }
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glDisable(2929);
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, this.a.getValue() / 255.0f);
                    if (this.lines.getValue()) {
                        GL11.glLineWidth((float)this.width.getValue());
                    }
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    GL11.glPopAttrib();
                    GL11.glPolygonOffset(1.0f, 100000.0f);
                    GL11.glDisable(32823);
                    GL11.glPopMatrix();
                    event.setCancelled(true);
                }
                else if (!EntityUtil.isPassive(event.entity) && Chams.mobs.getValue()) {
                    GL11.glPushMatrix();
                    GL11.glEnable(32823);
                    GL11.glPolygonOffset(1.0f, -100000.0f);
                    GL11.glPushAttrib(1048575);
                    if (!this.lines.getValue()) {
                        GL11.glPolygonMode(1028, 6914);
                    }
                    else {
                        GL11.glPolygonMode(1028, 6913);
                    }
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glDisable(2929);
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, this.a.getValue() / 255.0f);
                    if (this.lines.getValue()) {
                        GL11.glLineWidth((float)this.width.getValue());
                    }
                    event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                    GL11.glPopAttrib();
                    GL11.glPolygonOffset(1.0f, 100000.0f);
                    GL11.glDisable(32823);
                    GL11.glPopMatrix();
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventTarget
    public void renderPost(final EventPostRenderLayers event) {
        if (Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            if (!event.renderer.bindEntityTexture((Entity)event.entity)) {
                return;
            }
            final Color c = (this.friendColor.getValue() && Friends.isFriend(event.entity.getName())) ? new Color(0.27f, 0.7f, 0.92f) : (this.rainbow.getValue() ? new Color(Xulu.rgb) : new Color(this.r.getValue(), this.g.getValue(), this.b.getValue()));
            if (event.getEventState() == Event.State.PRE && event.entity instanceof EntityOtherPlayerMP && Chams.players.getValue()) {
                GL11.glPushMatrix();
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -100000.0f);
                GL11.glPushAttrib(1048575);
                if (!this.lines.getValue()) {
                    GL11.glPolygonMode(1028, 6914);
                }
                else {
                    GL11.glPolygonMode(1028, 6913);
                }
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, this.a.getValue() / 255.0f / 2.0f);
                if (this.lines.getValue()) {
                    GL11.glLineWidth((float)this.width.getValue());
                }
                event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                GL11.glPopAttrib();
                GL11.glPolygonOffset(1.0f, 100000.0f);
                GL11.glDisable(32823);
                GL11.glPopMatrix();
            }
        }
    }
    
    @EventTarget
    public void onPlayerModel(final EventModelPlayerRender event) {
        if (Chams.mode.getValue().equalsIgnoreCase("ESP") && Chams.players.getValue()) {
            final Color c = (this.friendColor.getValue() && Friends.isFriend(event.entity.getName())) ? new Color(0.27f, 0.7f, 0.92f) : (this.rainbow.getValue() ? new Color(Xulu.rgb) : new Color(this.r.getValue(), this.g.getValue(), this.b.getValue()));
            switch (event.getEventState()) {
                case PRE: {
                    GL11.glPushMatrix();
                    GL11.glEnable(32823);
                    GL11.glPolygonOffset(1.0f, -1.0E7f);
                    GL11.glPushAttrib(1048575);
                    if (!this.lines.getValue()) {
                        GL11.glPolygonMode(1028, 6914);
                    }
                    else {
                        GL11.glPolygonMode(1028, 6913);
                    }
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glDisable(2929);
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, this.a.getValue() / 255.0f / 2.0f);
                    if (this.lines.getValue()) {
                        GL11.glLineWidth((float)this.width.getValue());
                        break;
                    }
                    break;
                }
                case POST: {
                    GL11.glPopAttrib();
                    GL11.glPolygonOffset(1.0f, 1.0E7f);
                    GL11.glDisable(32823);
                    GL11.glPopMatrix();
                    break;
                }
            }
        }
    }
}
