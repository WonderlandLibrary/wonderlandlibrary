package de.tired.base.module.implementation.visual.esp;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render2DEvent;
import de.tired.base.event.events.Render3DEvent;
import de.tired.base.event.events.Render3DEvent2;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import lombok.SneakyThrows;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.reflections.Reflections;

import java.awt.*;
import java.util.*;
import java.util.List;

@ModuleAnnotation(name = "ESP", category = ModuleCategory.RENDER)
public class Esp extends Module {

    private final ModeSetting modeValue;

    private final List<EspExtension> espModes;

    public ColorPickerSetting real2DColor;

    public ColorPickerSetting dropShadowColor;

    public final BooleanSetting healthBar;

    //Glow Esp Settings
    private final BooleanSetting mobs = new BooleanSetting("Mobs", this, true);
    private final BooleanSetting animals = new BooleanSetting("Animals", this, true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisible", this, true);
    private final BooleanSetting players = new BooleanSetting("players", this, true);
    private final BooleanSetting villagers = new BooleanSetting("villagers", this, true);
    public final BooleanSetting isInView = new BooleanSetting("IsInView", this, true);
    public final BooleanSetting dropShadow;

    public final BooleanSetting colorShadow;

    public NumberSetting renderDistance = new NumberSetting("renderDistance", this, 100, 1, 10000, 1);

    public NumberSetting shadowRadius;
    public final BooleanSetting fadeColor = new BooleanSetting("fadeColor", this, true);

    public Esp() {

        espModes = new ArrayList<>();
        final Reflections reflections = new Reflections("de.tired.base.module.implementation.visual.esp.modes");

        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(EspModeAnnotation.class);

        ArrayList<String> modesArray = new ArrayList<>();
        for (Class<?> aClass : classes) {
            try {
                final EspExtension extension = (EspExtension) aClass.newInstance();
                espModes.add(extension);
                modesArray.add(extension.modeName);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String[] modes = new String[modesArray.size()];
        modesArray.toArray(modes);
        System.out.println(Arrays.toString(modes));
        modeValue = new ModeSetting("EspMode", this, modes);

        //Real2D Settings
        healthBar = new BooleanSetting("healthBar", this, true, () -> modeValue.getValue().equalsIgnoreCase("Real2D"));

        real2DColor = new ColorPickerSetting("Real2DColor", this, new Color(244, 0, 0, 255), () -> modeValue.getValue().equalsIgnoreCase("Real2D"));



        dropShadow = new BooleanSetting("dropShadow", this, true, () -> modeValue.getValue().equalsIgnoreCase("Real2D"));
        shadowRadius = new NumberSetting("shadowRadius", this, 4, 3, 30, 1, () -> modeValue.getValue().equalsIgnoreCase("Real2D") && dropShadow.getValue());
        colorShadow = new BooleanSetting("colorShadow", this, true, () -> dropShadow.getValue() && modeValue.getValue().equalsIgnoreCase("Real2D"));
        dropShadowColor = new ColorPickerSetting("dropShadowColor", this, new Color(244, 0, 0, 255), () -> modeValue.getValue().equalsIgnoreCase("Real2D") && dropShadow.getValue() && colorShadow.getValue());


    }

    @EventTarget
    public void onRender3D(Render3DEvent render3DEvent) {
        if (espModes == null || MC.thePlayer == null) return;
        Objects.requireNonNull(getCurrentMode()).onRender3D(render3DEvent);
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {

        if (espModes == null || MC.thePlayer == null) return;
        Objects.requireNonNull(getCurrentMode()).onRender2D(event);
    }

    @EventTarget
    public void onRender3D2(Render3DEvent2 event) {
        if (espModes == null || MC.thePlayer == null) return;
        Objects.requireNonNull(getCurrentMode()).onRender3D2(event);
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }

    @SneakyThrows
    private EspExtension getCurrentMode() {
        for (EspExtension espExtension : espModes) {
            if (espExtension.modeName.equalsIgnoreCase(modeValue.getValue())) {
                return espExtension;
            }
        }
        return null;
    }

    public boolean shouldRender(Entity entity) {
        if (entity instanceof EntityPlayer && !players.getValue()) return false;
        if (entity instanceof EntityAnimal && !animals.getValue()) return false;
        if (entity instanceof EntityMob && !mobs.getValue()) return false;
        if (entity instanceof EntityItem || entity instanceof EntityItemFrame) return false;
        if (entity instanceof EntityVillager && !villagers.getValue()) return false;
        if (entity.isInvisibleToPlayer(MC.thePlayer) && !invisibles.getValue()) return false;
        if (entity.getDistanceSqToEntity(MC.thePlayer) >= renderDistance.getValueInt()) return false;
        if (entity.getName().equalsIgnoreCase(MC.thePlayer.getName())) return false;
        return !entity.isDead;
    }


}
