package me.AquaVit.liquidSense.modules.misc;

import me.AquaVit.liquidSense.modules.render.ItemRotate;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;


@ModuleInfo(name = "Animations", description = "Animations", category = ModuleCategory.MISC)
public class Animations extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Rotate360","Push","PushOther","Screw","Windmill","SmoothFloat","Sigma","SigmaOther","Remix","Swang","Swong","Swank","MeMe","Slide","Swing","Jello","Swaing","IDK"}, "Push");
    public static final ListValue GodMode = new ListValue("ThirdView",new String[]{"1.7","1.8","Dev"},"1.7");
    private final IntegerValue ScrewSpeedValue = new IntegerValue("ScrewSpeed", 10, 1, 20);
    public static final FloatValue SpeedRotate = new FloatValue("SpeedRotate", 1f, 0f, 10f);
    public static final FloatValue Scale = new FloatValue("Scale", 1f, 0f, 3.5f);
    public static final FloatValue itemPosX = new FloatValue("ItemPosX", 0.94f, -1f, 1f);
    public static final FloatValue itemPosY = new FloatValue("ItemPosY", -0.05f, -1f, 1f);
    public static final FloatValue itemPosZ = new FloatValue("ItemPosZ", -1.2f, -2f, 1f);
    public static final IntegerValue ArmSlow = new IntegerValue("ArmSlow", 1, 0, 5);
    private static int f3 = 0;
    private static float shabi = 0;

    public ListValue getModeValue() {
        return modeValue;
    }

    public IntegerValue getSB() {
        return ArmSlow;
    }

    public IntegerValue getValue() {
        return ScrewSpeedValue;
    }

    public static void ItemRenderRotation() {

        MSTimer rotateTimer = new MSTimer();

        GlStateManager.rotate(shabi, 1.0F, 0.0F, 2.0F);
        if (rotateTimer.hasTimePassed(1)) {
            ++shabi;
            shabi = shabi + Animations.SpeedRotate.get();
            rotateTimer.reset();
        }
        if (shabi > 360.0F) {
            shabi = 0.0F;
        }
    }

    public static void renderblock(float f, float f1) {
        GlStateManager.translate(Animations.itemPosX.get(), Animations.itemPosY.get(), Animations.itemPosZ.get());
        final Animations animations = (Animations) LiquidBounce.moduleManager.getModule(Animations.class);
        Aura killAura = (Aura) LiquidBounce.moduleManager.getModule(Aura.class);
        Entity entity = killAura.getTarget();
        GlStateManager.translate(0.8F, -0.52F, -1.0F);
        GlStateManager.rotate((float) f3, 0.0F, -0.9F, 0.9F);
        if (f3 < 99999999) {
            if (entity != null && entity.hurtResistantTime != 0) {
                f3 += 5;
            }

            f3 += animations.getValue().get();
        } else {
            f3 = 0;
        }

        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.translate(-0.2F, 0.0F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 100.0F);
        GlStateManager.rotate(-80.0F, 10.0F, 0.0F, 0.0F);
        GlStateManager.rotate(80.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(Animations.Scale.get(), Animations.Scale.get(), Animations.Scale.get());
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}