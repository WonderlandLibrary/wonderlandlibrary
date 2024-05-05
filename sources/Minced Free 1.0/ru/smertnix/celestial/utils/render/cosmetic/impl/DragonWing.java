package ru.smertnix.celestial.utils.render.cosmetic.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import ru.smertnix.celestial.utils.render.cosmetic.Cosmetic;

import org.lwjgl.opengl.GL11;

public class DragonWing extends ModelBase {
    private static ModelRenderer mr1;
    private static ModelRenderer mr2;

    public DragonWing() {
        setTextureOffset("wing.bone", 0, 0);
        setTextureOffset("wing.skin", -10, 8);
        setTextureOffset("wingtip.bone", 0, 5);
        setTextureOffset("wingtip.skin", -10, 18);
        (mr1 = new ModelRenderer(this, "wing")).setTextureSize(30, 30);
        mr1.setRotationPoint(-2.0F, 0.0F, 0.0F);
        mr1.addBox("bone", -10.0F, -1.0F, -1.0F, 10, 2, 2);
        mr1.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
        (mr2 = new ModelRenderer(this, "wingtip")).setTextureSize(30, 30);
        mr2.setRotationPoint(-10.0F, 0.0F, 0.0F);
        mr2.addBox("bone", -10.0F, -0.5F, -0.5F, 10, 1, 1);
        mr2.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
        mr1.addChild(mr2);
    }



    private static float calRotateHeadNowX(float yaw1, float yaw2, float t, EntityPlayer player) {
        if (!player.equals(Minecraft.getMinecraft().player))
            if ((0.0F > yaw1 && 0.0F < yaw2) || (0.0F < yaw1 && 0.0F > yaw2))
                return yaw2;
        float f = (yaw1 + (yaw2 - yaw1) * t) % 360.0F;
        return f;
    }

    private static float calRotateBodyNowX(float yaw1, float yaw2, float t) {
        float f = (yaw1 + (yaw2 - yaw1) * t) % 360.0F;
        return f;
    }

    private static float calRotateHeadNowY(float yaw1, float yaw2, float t) {
        float f = (yaw1 + (yaw2 - yaw1) * t) % 180.0F;
        return f;
    }

    private static float interpolate(float yaw1, float yaw2, float percent) {
        float f = (yaw1 + (yaw2 - yaw1) * percent) % 360.0F;
        if (f < 0.0F)
            f += 360.0F;
        return f;
    }
}
