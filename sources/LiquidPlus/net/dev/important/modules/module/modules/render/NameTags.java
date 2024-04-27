/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  javax.vecmath.Vector3d
 *  javax.vecmath.Vector4d
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityGolem
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.misc.AntiBot;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

@Info(name="NameTags", description="=/", category=Category.RENDER, cnName="\u540d\u5b57\u6807\u7b7e")
public class NameTags
extends Module {
    private final BoolValue player = new BoolValue("Player", true);
    private final BoolValue mob = new BoolValue("Mob", false);
    private final BoolValue animal = new BoolValue("Animal", false);
    private final BoolValue invisible = new BoolValue("Invisible", false);
    private final BoolValue health = new BoolValue("Health", true);
    private final BoolValue distance = new BoolValue("Distance", false);
    private final IntegerValue background = new IntegerValue("BackGround", 100, 0, 255);
    private final FontValue font = new FontValue("Font", Fonts.minecraftFont);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        NameTags.mc.field_71441_e.field_72996_f.forEach(entity -> {
            EntityLivingBase ent;
            if (entity instanceof EntityLivingBase && this.isValid(ent = (EntityLivingBase)entity) && RenderUtils.isInViewFrustrum((Entity)ent) && entity.func_110124_au() != NameTags.mc.field_71439_g.func_110124_au()) {
                double posX = RenderUtils.interpolate(entity.field_70165_t, entity.field_70142_S, event.getPartialTicks());
                double posY = RenderUtils.interpolate(entity.field_70163_u, entity.field_70137_T, event.getPartialTicks());
                double posZ = RenderUtils.interpolate(entity.field_70161_v, entity.field_70136_U, event.getPartialTicks());
                double width = (double)entity.field_70130_N / 1.5;
                double height = (double)entity.field_70131_O + (entity.func_70093_af() ? -0.3 : 0.2);
                AxisAlignedBB aabb = new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height + 0.05, posZ + width);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c), new Vector3d(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c), new Vector3d(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c), new Vector3d(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c), new Vector3d(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f), new Vector3d(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f), new Vector3d(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f), new Vector3d(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f));
                NameTags.mc.field_71460_t.func_78479_a(event.getPartialTicks(), 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = RenderUtils.project(vector.x - NameTags.mc.func_175598_ae().field_78730_l, vector.y - NameTags.mc.func_175598_ae().field_78731_m, vector.z - NameTags.mc.func_175598_ae().field_78728_n);
                    if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
                NameTags.mc.field_71460_t.func_78478_c();
                if (position != null) {
                    GL11.glPushMatrix();
                    float x = (float)position.x;
                    float x2 = (float)position.z;
                    float y = (float)position.y - 1.0f;
                    String healthText = (ent.func_110143_aJ() >= 16.0f ? ChatFormatting.GREEN : (ent.func_110143_aJ() >= 12.0f ? ChatFormatting.YELLOW : (ent.func_110143_aJ() >= 8.0f ? ChatFormatting.RED : ChatFormatting.DARK_RED))) + " " + (int)ent.func_110143_aJ();
                    String nameText = ((Boolean)this.distance.get() != false ? "(" + Math.round(NameTags.mc.field_71439_g.func_70011_f(ent.field_70165_t, ent.field_70163_u, ent.field_70161_v)) + "m) " : "") + ent.func_145748_c_().func_150260_c() + ((Boolean)this.health.get() != false ? healthText : "");
                    if ((Integer)this.background.get() > 0) {
                        RenderUtils.MoondrawRect((double)(x + (x2 - x) / 2.0f - (float)(!((FontRenderer)this.font.get()).equals(Fonts.minecraftFont) ? ((FontRenderer)this.font.get()).func_78256_a(nameText) : NameTags.mc.field_71466_p.func_78256_a(nameText)) / 2.0f) - 2.5, y - (float)(!((FontRenderer)this.font.get()).equals(Fonts.minecraftFont) ? ((FontRenderer)this.font.get()).field_78288_b + 5 : NameTags.mc.field_71466_p.field_78288_b + 4), !((FontRenderer)this.font.get()).equals(Fonts.minecraftFont) ? ((FontRenderer)this.font.get()).func_78256_a(nameText) + 5 : NameTags.mc.field_71466_p.func_78256_a(nameText) + 3, !((FontRenderer)this.font.get()).equals(Fonts.minecraftFont) ? ((FontRenderer)this.font.get()).field_78288_b + 2 : NameTags.mc.field_71466_p.field_78288_b + 2, new Color(0, 0, 0, (Integer)this.background.get()).getRGB());
                    }
                    ((FontRenderer)this.font.get()).func_175063_a(nameText, (float)((int)(x + (x2 - x) / 2.0f - (float)((FontRenderer)this.font.get()).func_78256_a(nameText) / 2.0f)), (float)((int)(y - (float)((FontRenderer)this.font.get()).field_78288_b - 2.0f)), this.getNameColor(ent));
                    GL11.glPopMatrix();
                }
            }
        });
    }

    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.isBot(entity) && NameTags.mc.field_71439_g != entity && entity.func_145782_y() != -1488 && this.isValidType(entity) && entity.func_70089_S() && (!entity.func_82150_aj() || (Boolean)this.invisible.get() != false);
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (Boolean)this.player.get() != false && entity instanceof EntityPlayer || (Boolean)this.mob.get() != false && (entity instanceof EntityMob || entity instanceof EntitySlime) || (Boolean)this.animal.get() != false && entity instanceof EntityAnimal || (Boolean)this.invisible.get() != false && (entity instanceof EntityVillager || entity instanceof EntityGolem);
    }

    private int getNameColor(EntityLivingBase entity) {
        if (EntityUtils.isFriend((Entity)entity)) {
            return new Color(100, 150, 255).getRGB();
        }
        if (entity.func_70005_c_().equals(NameTags.mc.field_71439_g.func_70005_c_())) {
            return new Color(100, 200, 100).getRGB();
        }
        return new Color(255, 255, 255).getRGB();
    }
}

