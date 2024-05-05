/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package com.wallhacks.losebypass.systems.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.PacketReceiveEvent;
import com.wallhacks.losebypass.event.events.Render3DEvent;
import com.wallhacks.losebypass.event.events.TickEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.utils.MathUtil;
import com.wallhacks.losebypass.utils.RenderUtil;
import com.wallhacks.losebypass.utils.Timer;
import java.awt.Color;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

@Module.Registration(name="SuperheroFX", description="Draws sexy hiteffects", category=Module.Category.RENDER)
public class SuperheroFX
extends Module {
    private static final String[] texts = new String[]{"OUCH", "ZAP", "BAM", "WOW", "POW", "SLAP", "EZ"};
    private static final Random rand = new Random();
    private final Timer timer = new Timer();
    private final DoubleSetting delay = this.doubleSetting("Delay", 1.0, 0.0, 10.0);
    private final DoubleSetting scale = this.doubleSetting("Scale", 1.5, 0.0, 5.0);
    private final IntSetting effects = this.intSetting("Effects", 1, 0, 5);
    private final BooleanSetting randomColor = this.booleanSetting("RandomColor", true);
    private final ColorSetting colourSetting = this.colorSetting("Color", new Color(50, 120, 230, 255));
    private final List<PopupText> popTexts = new CopyOnWriteArrayList<PopupText>();

    @SubscribeEvent
    public void onTick(TickEvent event) {
        this.popTexts.forEach(PopupText::update);
    }

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        this.popTexts.forEach(pop -> {
            RenderUtil.glBillboardDistanceScaled(((PopupText)pop).vec3.xCoord, ((PopupText)pop).vec3.yCoord, ((PopupText)pop).vec3.zCoord, SuperheroFX.mc.thePlayer, (Double)this.scale.getValue());
            GlStateManager.translate(-((double)LoseBypass.fontManager.getBadaboom().getStringWidth(pop.displayName) / 2.0), 0.0, 0.0);
            LoseBypass.fontManager.getBadaboom().drawText(pop.displayName, 0.0, 0.0, pop.color);
        });
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (SuperheroFX.mc.thePlayer == null) return;
        if (SuperheroFX.mc.theWorld == null) {
            return;
        }
        try {
            if (event.getPacket() instanceof S19PacketEntityStatus) {
                S19PacketEntityStatus packet = (S19PacketEntityStatus)event.getPacket();
                Entity e = packet.getEntity(SuperheroFX.mc.theWorld);
                if (packet.getOpCode() != 2) return;
                if (!(SuperheroFX.mc.thePlayer.getDistanceToEntity(e) < 20.0f & e != SuperheroFX.mc.thePlayer)) return;
                if (!this.timer.passedMs((long)((Double)this.delay.getValue() * 1000.0))) return;
                this.timer.reset();
                this.registerPopUpText(e);
                return;
            }
            if (!(event.getPacket() instanceof S13PacketDestroyEntities)) return;
            S13PacketDestroyEntities packet = (S13PacketDestroyEntities)event.getPacket();
            int[] array = packet.getEntityIDs();
            int i = 0;
            while (i < array.length - 1) {
                block9: {
                    int id;
                    block8: {
                        id = array[i];
                        try {
                            if (SuperheroFX.mc.theWorld.getEntityByID(id) != null) break block8;
                            break block9;
                        }
                        catch (ConcurrentModificationException exception) {
                            return;
                        }
                    }
                    Entity e = SuperheroFX.mc.theWorld.getEntityByID(id);
                    if (e != null && e.isDead && SuperheroFX.mc.thePlayer.getDistanceToEntity(e) < 20.0f & e != SuperheroFX.mc.thePlayer && e instanceof EntityPlayer) {
                        this.registerPopUpText(e);
                    }
                }
                ++i;
            }
            return;
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
    }

    private void registerPopUpText(Entity entity) {
        IntStream.rangeClosed(0, rand.nextInt((Integer)this.effects.getValue())).mapToObj(i -> MathUtil.getCenter(entity.getEntityBoundingBox()).addVector(rand.nextInt(2) - 1, (double)rand.nextInt(2) - 0.8, rand.nextInt(2) - 1)).map(pos -> new PopupText(texts[rand.nextInt(texts.length)], (Vec3)pos)).forEach(this.popTexts::add);
    }

    private double random() {
        return MathHelper.clamp_double(0.011 + rand.nextDouble() * 0.025, 0.011, 0.025);
    }

    class PopupText {
        public final int color;
        private final long start;
        private final double yIncrease;
        public final String displayName;
        private Vec3 vec3;

        public PopupText(String displayName, Vec3 vec3) {
            this.color = (Boolean)SuperheroFX.this.randomColor.getValue() == false ? SuperheroFX.this.colourSetting.getColor().getRGB() : Color.getHSBColor(rand.nextFloat(), 1.0f, 0.9f).getRGB();
            this.start = System.currentTimeMillis();
            this.yIncrease = SuperheroFX.this.random();
            this.displayName = ChatFormatting.ITALIC + displayName;
            this.vec3 = vec3;
        }

        public void update() {
            this.vec3 = this.vec3.addVector(0.0, this.yIncrease, 0.0);
            if (System.currentTimeMillis() - this.start <= 1000L) return;
            SuperheroFX.this.popTexts.remove(this);
        }
    }
}

