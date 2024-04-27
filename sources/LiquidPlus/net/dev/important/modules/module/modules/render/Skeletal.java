/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.render;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateModelEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@Info(name="Skeletal", description="idk", category=Category.RENDER, cnName="\u9aa8\u9abc\u663e\u793a")
public class Skeletal
extends Module {
    private final Map playerRotationMap = new WeakHashMap();
    private final IntegerValue red = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue green = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue blue = new IntegerValue("Blue", 255, 0, 255);
    private final BoolValue smoothLines = new BoolValue("SmoothLines", true);

    @EventTarget
    public final void onModelUpdate(UpdateModelEvent event) {
        ModelPlayer model = event.getModel();
        this.playerRotationMap.put(event.getPlayer(), new float[][]{{model.field_78116_c.field_78795_f, model.field_78116_c.field_78796_g, model.field_78116_c.field_78808_h}, {model.field_178723_h.field_78795_f, model.field_178723_h.field_78796_g, model.field_178723_h.field_78808_h}, {model.field_178724_i.field_78795_f, model.field_178724_i.field_78796_g, model.field_178724_i.field_78808_h}, {model.field_178721_j.field_78795_f, model.field_178721_j.field_78796_g, model.field_178721_j.field_78808_h}, {model.field_178722_k.field_78795_f, model.field_178722_k.field_78796_g, model.field_178722_k.field_78808_h}});
    }

    @EventTarget
    public void onRender(Render3DEvent event) {
        this.setupRender(true);
        GL11.glEnable((int)2903);
        GL11.glDisable((int)2848);
        this.playerRotationMap.keySet().removeIf(var0 -> this.contain((EntityPlayer)var0));
        Map playerRotationMap = this.playerRotationMap;
        List worldPlayers = Skeletal.mc.field_71441_e.field_73010_i;
        Object[] players = playerRotationMap.keySet().toArray();
        int playersLength = players.length;
        for (int i = 0; i < playersLength; ++i) {
            EntityPlayer player = (EntityPlayer)players[i];
            float[][] entPos = (float[][])playerRotationMap.get(player);
            if (entPos == null || player.func_145782_y() == -1488 || !player.func_70089_S() || !RenderUtils.isInViewFrustrum((Entity)player) || player.field_70128_L || player == Skeletal.mc.field_71439_g || player.func_70608_bn() || player.func_82150_aj()) continue;
            GL11.glPushMatrix();
            float[][] modelRotations = (float[][])playerRotationMap.get(player);
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)((float)((Integer)this.red.get()).intValue() / 255.0f), (float)((float)((Integer)this.green.get()).intValue() / 255.0f), (float)((float)((Integer)this.blue.get()).intValue() / 255.0f), (float)1.0f);
            double x = Skeletal.interpolate(player.field_70165_t, player.field_70142_S, event.getPartialTicks()) - Skeletal.mc.func_175598_ae().field_78725_b;
            double y = Skeletal.interpolate(player.field_70163_u, player.field_70137_T, event.getPartialTicks()) - Skeletal.mc.func_175598_ae().field_78726_c;
            double z = Skeletal.interpolate(player.field_70161_v, player.field_70136_U, event.getPartialTicks()) - Skeletal.mc.func_175598_ae().field_78723_d;
            GL11.glTranslated((double)x, (double)y, (double)z);
            float bodyYawOffset = player.field_70760_ar + (player.field_70761_aq - player.field_70760_ar) * Skeletal.mc.field_71428_T.field_74281_c;
            GL11.glRotatef((float)(-bodyYawOffset), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)0.0, (double)(player.func_70093_af() ? -0.235 : 0.0));
            float legHeight = player.func_70093_af() ? 0.6f : 0.75f;
            float rad = 57.29578f;
            GL11.glPushMatrix();
            GL11.glTranslated((double)-0.125, (double)legHeight, (double)0.0);
            if (modelRotations[3][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[3][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][1] * 57.29578f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[3][2] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[3][2] * 57.29578f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-legHeight), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.125, (double)legHeight, (double)0.0);
            if (modelRotations[4][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[4][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][1] * 57.29578f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[4][2] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[4][2] * 57.29578f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-legHeight), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated((double)0.0, (double)0.0, (double)(player.func_70093_af() ? 0.25 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)(player.func_70093_af() ? -0.05 : 0.0), (double)(player.func_70093_af() ? -0.01725 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)-0.375, (double)((double)legHeight + 0.55), (double)0.0);
            if (modelRotations[1][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[1][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[1][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[1][1] * 57.29578f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[1][2] != 0.0f) {
                GL11.glRotatef((float)(-modelRotations[1][2] * 57.29578f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.375, (double)((double)legHeight + 0.55), (double)0.0);
            if (modelRotations[2][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[2][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (modelRotations[2][1] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[2][1] * 57.29578f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (modelRotations[2][2] != 0.0f) {
                GL11.glRotatef((float)(-modelRotations[2][2] * 57.29578f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(bodyYawOffset - player.field_70759_as), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)((double)legHeight + 0.55), (double)0.0);
            if (modelRotations[0][0] != 0.0f) {
                GL11.glRotatef((float)(modelRotations[0][0] * 57.29578f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.3, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(player.func_70093_af() ? 25.0f : 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)(player.func_70093_af() ? -0.16175 : 0.0), (double)(player.func_70093_af() ? -0.48025 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)legHeight, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.125, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.125, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)legHeight, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.55, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)((double)legHeight + 0.55), (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.375, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.375, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
        }
        this.setupRender(false);
    }

    private void setupRender(boolean start) {
        boolean smooth = (Boolean)this.smoothLines.get();
        if (start) {
            if (smooth) {
                RenderUtils.startSmooth();
            } else {
                GL11.glDisable((int)2848);
            }
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
        } else {
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            if (smooth) {
                RenderUtils.endSmooth();
            }
        }
        GL11.glDepthMask((!start ? 1 : 0) != 0);
    }

    private boolean contain(EntityPlayer var0) {
        return !Skeletal.mc.field_71441_e.field_73010_i.contains(var0);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }
}

