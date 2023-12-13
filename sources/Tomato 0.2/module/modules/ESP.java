/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.events.Event3D;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.modules.Killaura;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.utils.RenderUtils;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class ESP
extends Module {
    private static boolean players = true;
    private static boolean monsters;
    private static boolean animals;
    private boolean outline;
    private int state;
    private float r = 0.33f;
    private float g = 0.34f;
    private float b = 0.33f;
    private static float[] rainbowArray;

    public ESP() {
        super("ESP", 0, Category.RENDER, 142024688, true, "ESP baby", new String[]{"pse", "sep"});
    }

    @EventTarget
    private void onPreUpdate(EventPreMotionUpdates event) {
        rainbowArray = this.getRainbow();
    }

    @EventTarget(value=4)
    private void onRender3D(Event3D event) {
        if (this.outline) {
            return;
        }
        for (Object o : Wrapper.mc.theWorld.loadedEntityList) {
            if (!(o instanceof EntityLivingBase) || o == Wrapper.mc.thePlayer) continue;
            EntityLivingBase entity = (EntityLivingBase)o;
            int color = -1862314667;
            int thingyt = 1174405120;
            if (entity.hurtTime != 0) {
                color = -6750208;
                thingyt = 1184432128;
            }
            if (!ESP.checkValidity(entity)) continue;
            RenderUtils.drawEsp(entity, Event3D.getPartialTicks(), color, thingyt);
        }
    }

    public static void renderOne() {
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(3.0f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glLineWidth(3.0f);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void renderFour() {
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    public static void renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }

    public static void renderOne(Entity ent) {
        if (!ESP.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(0.2f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        ESP.colorLines(ent);
        GL11.glLineWidth(1.5f);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    private static void colorLines(Entity ent) {
        int color = 13762557;
        if (Killaura.attackList.contains(ent)) {
            GL11.glColor4f(rainbowArray[0], rainbowArray[1], rainbowArray[2], 1.0f);
        } else {
            Cheese.getInstance();
            if (FriendManager.isFriend(ent.getName())) {
                color = -484140362;
            }
            ESP.color(color, 1.0f);
        }
    }

    private static void color(int color, float alpha) {
        float red = (float)(color >> 16 & 255) / 255.0f;
        float green = (float)(color >> 8 & 255) / 255.0f;
        float blue = (float)(color & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void renderTwo(Entity ent) {
        if (!ESP.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public static void renderThree(Entity ent) {
        if (!ESP.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void renderFour(Entity ent) {
        if (!ESP.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    public static void renderFive(Entity ent) {
        if (!ESP.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }

    private static boolean checkValidity(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            return players;
        }
        if (!(monsters && entity instanceof EntityMob || animals && entity instanceof EntityAnimal || animals && entity instanceof EntityBat)) {
            return false;
        }
        return true;
    }

    private float[] getRainbow() {
        if (this.state == 0) {
            this.r = (float)((double)this.r + 0.02);
            this.b = (float)((double)this.b - 0.02);
            if ((double)this.r >= 0.85) {
                ++this.state;
            }
        } else if (this.state == 1) {
            this.g = (float)((double)this.g + 0.02);
            this.r = (float)((double)this.r - 0.02);
            if ((double)this.g >= 0.85) {
                ++this.state;
            }
        } else {
            this.b = (float)((double)this.b + 0.02);
            this.g = (float)((double)this.g - 0.02);
            if ((double)this.b >= 0.85) {
                this.state = 0;
            }
        }
        return new float[]{this.r, this.g, this.b};
    }
}

