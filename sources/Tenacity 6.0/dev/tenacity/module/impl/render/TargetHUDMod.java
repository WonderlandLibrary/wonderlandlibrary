// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.module.impl.render.targethud.RiseTargetHUD;
import dev.tenacity.utils.objects.PlayerDox;
import dev.tenacity.module.api.TargetManager;
import dev.tenacity.utils.animations.Direction;
import net.minecraft.client.gui.GuiChat;
import dev.tenacity.module.impl.render.targethud.AutoDoxTargetHUD;
import dev.tenacity.event.impl.render.PreRenderEvent;
import java.util.Iterator;
import dev.tenacity.utils.render.ESPUtil;
import net.minecraft.entity.Entity;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.module.impl.render.targethud.TargetHUD;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.Tenacity;
import dev.tenacity.module.Category;
import org.lwjgl.util.vector.Vector4f;
import dev.tenacity.module.impl.combat.KillAura;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.objects.Dragging;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.utils.objects.GradientColorWheel;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class TargetHUDMod extends Module
{
    private final ModeSetting targetHud;
    private final BooleanSetting trackTarget;
    private final ModeSetting trackingMode;
    public static boolean renderLayers;
    private final GradientColorWheel colorWheel;
    private EntityLivingBase target;
    private final Dragging drag;
    private final Animation openAnimation;
    private KillAura killAura;
    private Vector4f targetVector;
    
    public TargetHUDMod() {
        super("TargetHUD", "Target HUD", Category.RENDER, "Displays info about the KillAura target");
        this.targetHud = new ModeSetting("Mode", "Tenacity", new String[] { "Tenacity", "Old Tenacity", "Rise", "Exhibition", "Auto-Dox", "Akrien", "Astolfo", "Adapt", "Adapt 2", "Exire", "Novoline", "Vape" });
        this.trackTarget = new BooleanSetting("Track Target", false);
        this.trackingMode = new ModeSetting("Tracking Mode", "Middle", new String[] { "Middle", "Top", "Left", "Right" });
        this.colorWheel = new GradientColorWheel();
        this.drag = Tenacity.INSTANCE.createDrag(this, "targetHud", 300.0f, 300.0f);
        this.openAnimation = new DecelerateAnimation(175, 0.5);
        this.trackingMode.addParent(this.trackTarget, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.targetHud, this.trackTarget, this.trackingMode, this.colorWheel.createModeSetting("Color Mode", "Dark"), this.colorWheel.getColorSetting());
        TargetHUD.init();
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (this.trackTarget.isEnabled() && this.target != null) {
            for (final Entity entity : TargetHUDMod.mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityLivingBase) {
                    final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                    if (!this.target.equals(entityLivingBase)) {
                        continue;
                    }
                    this.targetVector = ESPUtil.getEntityPositionsOn2D(entity);
                }
            }
        }
    }
    
    @Override
    public void onPreRenderEvent(final PreRenderEvent event) {
        final TargetHUD currentTargetHUD = TargetHUD.get(this.targetHud.getMode());
        this.drag.setWidth(currentTargetHUD.getWidth());
        this.drag.setHeight(currentTargetHUD.getHeight());
        if (this.killAura == null) {
            this.killAura = (KillAura)Tenacity.INSTANCE.getModuleCollection().get(KillAura.class);
        }
        final AutoDoxTargetHUD autoDoxTargetHud = TargetHUD.get(AutoDoxTargetHUD.class);
        if (!(TargetHUDMod.mc.currentScreen instanceof GuiChat)) {
            if (!this.killAura.isEnabled()) {
                this.openAnimation.setDirection(Direction.BACKWARDS);
                if (this.openAnimation.finished(Direction.BACKWARDS)) {
                    autoDoxTargetHud.doxMap.clear();
                }
            }
            if (this.target == null && TargetManager.target != null) {
                this.target = TargetManager.target;
                this.openAnimation.setDirection(Direction.FORWARDS);
                if (!autoDoxTargetHud.doxMap.containsKey(this.target)) {
                    autoDoxTargetHud.doxMap.put(this.target, new PlayerDox(this.target));
                }
            }
            else if (TargetManager.target == null || this.target != TargetManager.target) {
                this.openAnimation.setDirection(Direction.BACKWARDS);
            }
            if (this.openAnimation.finished(Direction.BACKWARDS)) {
                TargetHUD.get(RiseTargetHUD.class).particles.clear();
                this.target = null;
            }
        }
        else {
            this.openAnimation.setDirection(Direction.FORWARDS);
            this.target = TargetHUDMod.mc.thePlayer;
            if (!autoDoxTargetHud.doxMap.containsKey(this.target)) {
                autoDoxTargetHud.doxMap.put(this.target, autoDoxTargetHud.thePlayerDox);
            }
        }
        if (this.target != null) {
            this.colorWheel.setColorsForMode("Dark", ColorUtil.brighter(new Color(30, 30, 30), 0.65f));
            this.colorWheel.setColors();
            currentTargetHUD.setColorWheel(this.colorWheel);
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        this.setSuffix(this.targetHud.getMode());
        final boolean tracking = this.trackTarget.isEnabled() && this.targetVector != null && this.target != TargetHUDMod.mc.thePlayer;
        final TargetHUD currentTargetHUD = TargetHUD.get(this.targetHud.getMode());
        if (this.target != null) {
            float trackScale = 1.0f;
            float x = this.drag.getX();
            float y = this.drag.getY();
            if (tracking) {
                final float newWidth = (this.targetVector.getZ() - this.targetVector.getX()) * 1.4f;
                trackScale = Math.min(1.0f, newWidth / currentTargetHUD.getWidth());
                final Pair<Float, Float> coords = this.getTrackedCoords();
                x = coords.getFirst();
                y = coords.getSecond();
            }
            RenderUtil.scaleStart(x + this.drag.getWidth() / 2.0f, y + this.drag.getHeight() / 2.0f, (float)(0.5 + this.openAnimation.getOutput().floatValue()) * trackScale);
            final float alpha = Math.min(1.0f, this.openAnimation.getOutput().floatValue() * 2.0f);
            currentTargetHUD.render(x, y, alpha, this.target);
            RenderUtil.scaleEnd();
        }
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent e) {
        float x = this.drag.getX();
        float y = this.drag.getY();
        float trackScale = 1.0f;
        final TargetHUD currentTargetHUD = TargetHUD.get(this.targetHud.getMode());
        if (this.trackTarget.isEnabled() && this.targetVector != null && this.target != TargetHUDMod.mc.thePlayer) {
            final Pair<Float, Float> coords = this.getTrackedCoords();
            x = coords.getFirst();
            y = coords.getSecond();
            final float newWidth = (this.targetVector.getZ() - this.targetVector.getX()) * 1.4f;
            trackScale = Math.min(1.0f, newWidth / currentTargetHUD.getWidth());
        }
        if (this.target != null) {
            final boolean glow = e.getBloomOptions().getSetting("TargetHud").isEnabled();
            RenderUtil.scaleStart(x + this.drag.getWidth() / 2.0f, y + this.drag.getHeight() / 2.0f, (float)(0.5 + this.openAnimation.getOutput().floatValue()) * trackScale);
            final float alpha = Math.min(1.0f, this.openAnimation.getOutput().floatValue() * 2.0f);
            currentTargetHUD.renderEffects(x, y, alpha, glow);
            RenderUtil.scaleEnd();
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.target = null;
    }
    
    private Pair<Float, Float> getTrackedCoords() {
        final ScaledResolution sr = new ScaledResolution(TargetHUDMod.mc);
        final float width = this.drag.getWidth();
        final float height = this.drag.getHeight();
        final float x = this.targetVector.getX();
        final float y = this.targetVector.getY();
        final float entityWidth = this.targetVector.getZ() - this.targetVector.getX();
        final float entityHeight = this.targetVector.getW() - this.targetVector.getY();
        final float middleX = x + entityWidth / 2.0f - width / 2.0f;
        final float middleY = y + entityHeight / 2.0f - height / 2.0f;
        final String mode = this.trackingMode.getMode();
        switch (mode) {
            case "Middle": {
                return Pair.of(middleX, middleY);
            }
            case "Top": {
                return Pair.of(middleX, y - (height / 2.0f + height / 4.0f));
            }
            case "Left": {
                return Pair.of(x - (width / 2.0f + width / 4.0f), middleY);
            }
            default: {
                return Pair.of(x + entityWidth - width / 4.0f, middleY);
            }
        }
    }
    
    static {
        TargetHUDMod.renderLayers = true;
    }
}
