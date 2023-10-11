package io.github.raze.modules.collection.visual;

import io.github.raze.Raze;
import io.github.raze.events.collection.visual.EventRender3D;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.math.MathUtil;
import io.github.raze.utilities.collection.visual.ColorUtil;
import io.github.raze.utilities.collection.visual.esp.EntityESPUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMonster;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

public class ESP extends AbstractModule {

    private final ArraySetting mode, colorMode;
    private final NumberSetting rColor, gColor, bColor, alphaColor;
    private final BooleanSetting mobs,animals;

    public ESP() {
        super("Esp", "Renders a box on entities.", ModuleCategory.VISUAL);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Box",  "Box", "2D", "Other Box"),
                colorMode = new ArraySetting(this, "Color", "Custom",  "Custom", "Rainbow", "Astolfo"),

                rColor = new NumberSetting(this, "R", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom")),

                gColor = new NumberSetting(this, "G", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom")),

                bColor = new NumberSetting(this, "B", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom")),

                alphaColor = new NumberSetting(this, "Transparency", 0, 255, 120)
                        .setHidden(() -> !mode.compare("Box")),

                mobs = new BooleanSetting(this, "Monsters", false),
                animals = new BooleanSetting(this, "Animals", false)

        );
    }

    @Listen
    public void onRender(EventRender3D eventRender3D) {
        mc.theWorld.loadedEntityList.stream()
                .filter(entityObject -> entityObject instanceof EntityLivingBase)
                .map(entityObject -> (EntityLivingBase) entityObject)
                .filter(EntityLivingBase::isEntityAlive)
                .forEach(entity -> {
                    double x = MathUtil.interpolate(entity.posX, entity.lastTickPosX) - mc.getRenderManager().renderPosX;
                    double y = MathUtil.interpolate(entity.posY, entity.lastTickPosY) - mc.getRenderManager().renderPosY;
                    double z = MathUtil.interpolate(entity.posZ, entity.lastTickPosZ) - mc.getRenderManager().renderPosZ;

                    float r, g, b;
                    if(colorMode.compare("Custom")) {
                        r = (float) (rColor.get().floatValue() / 255.0);
                        g = (float) (gColor.get().floatValue() / 255.0);
                        b = (float) (bColor.get().floatValue() / 255.0);
                    } else if(colorMode.compare("Rainbow")) {
                        Color rainbowColor = ColorUtil.getRainbow(5.0f, 1.0f, 1.0f, 2000);
                        r = rainbowColor.getRed() / 255.0f;
                        g = rainbowColor.getGreen() / 255.0f;
                        b = rainbowColor.getBlue() / 255.0f;
                    } else {
                        int astolfoRGB = ColorUtil.AstolfoRGB(1000, 2000);
                        r = ((astolfoRGB >> 16) & 0xFF) / 255.0f;
                        g = ((astolfoRGB >> 8) & 0xFF) / 255.0f;
                        b = (astolfoRGB & 0xFF) / 255.0f;
                    }

                    if (((entity instanceof EntityPlayer && entity != mc.thePlayer) ||
                            //Monsters
                            (entity instanceof EntityMonster || entity instanceof EntityGhast || entity instanceof EntitySlime) && mobs.get() ||

                            //Animals
                            (entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntitySquid || entity instanceof EntityBat) && animals.get())

                            && mode.get().equals("Box")) {
                        EntityESPUtil.renderBoxESP(entity, x, y, z, r, g, b, (float) (alphaColor.get().floatValue() / 255.0));

                    } else if (((entity instanceof EntityPlayer && entity != mc.thePlayer) ||
                            //Monsters
                            (entity instanceof EntityMonster || entity instanceof EntityGhast || entity instanceof EntitySlime) && mobs.get() ||

                            //Animals
                            (entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntitySquid || entity instanceof EntityBat) && animals.get())

                            && mode.get().equals("2D")) {
                        EntityESPUtil.render2DESP(entity, x, y, z, r, g, b);

                    } else if (((entity instanceof EntityPlayer && entity != mc.thePlayer) ||
                            //Monsters
                            (entity instanceof EntityMonster || entity instanceof EntityGhast || entity instanceof EntitySlime) && mobs.get() ||

                            //Animals
                            (entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntitySquid || entity instanceof EntityBat) && animals.get())

                            && mode.get().equals("Other Box")) {
                        EntityESPUtil.renderOtherBoxESP(entity, x, y, z, r, g, b);
                    }
                });
    }
}