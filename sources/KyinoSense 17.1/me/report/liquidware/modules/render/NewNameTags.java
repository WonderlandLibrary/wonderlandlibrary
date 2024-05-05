/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.util.IChatComponent
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package me.report.liquidware.modules.render;

import java.awt.Color;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiBot;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.GLUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="2DNameTags", description=":/", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J \u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002J\u001e\u0010\u0011\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fJ0\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0018H\u0002J\b\u0010\u001b\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2={"Lme/report/liquidware/modules/render/NewNameTags;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "armorValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "scaleValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "onRender3D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "renderEnchantText", "stack", "Lnet/minecraft/item/ItemStack;", "x", "", "y", "renderItemStack", "renderNameTag", "entity", "Lnet/minecraft/entity/EntityLivingBase;", "tag", "", "pX", "", "pY", "pZ", "whatTheFuckOpenGLThisFixesItemGlint", "KyinoClient"})
public final class NewNameTags
extends Module {
    private final BoolValue armorValue = new BoolValue("Armor", true);
    private final FloatValue scaleValue = new FloatValue("Scale", 1.0f, 0.5f, 2.0f);

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Iterator iterator2 = NewNameTags.access$getMc$p$s1046033730().field_71441_e.field_73010_i.iterator();
        while (iterator2.hasNext()) {
            EntityPlayer o;
            EntityPlayer entityPlayer = o = (EntityPlayer)iterator2.next();
            if (entityPlayer == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
            }
            EntityLivingBase e = (EntityLivingBase)entityPlayer;
            if (!e.func_70089_S() || e == NewNameTags.access$getMc$p$s1046033730().field_71439_g || !EntityUtils.isSelected((Entity)e, false)) continue;
            Minecraft minecraft = NewNameTags.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            RenderManager renderManager = minecraft.func_175598_ae();
            double pX = e.field_70142_S + (e.field_70165_t - e.field_70142_S) * (double)NewNameTags.access$getMc$p$s1046033730().field_71428_T.field_74281_c - renderManager.field_78725_b;
            double pY = e.field_70137_T + (e.field_70163_u - e.field_70137_T) * (double)NewNameTags.access$getMc$p$s1046033730().field_71428_T.field_74281_c - renderManager.field_78726_c;
            double pZ = e.field_70136_U + (e.field_70161_v - e.field_70136_U) * (double)NewNameTags.access$getMc$p$s1046033730().field_71428_T.field_74281_c - renderManager.field_78723_d;
            String string = e.func_70005_c_();
            Intrinsics.checkExpressionValueIsNotNull(string, "e.name");
            this.renderNameTag(e, string, pX, pY, pZ);
        }
    }

    private final void renderNameTag(EntityLivingBase entity, String tag2, double pX, double pY, double pZ) {
        String tag3 = tag2;
        double pY2 = pY;
        ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
        FontRenderer fontRenderer = NewNameTags.access$getMc$p$s1046033730().field_71466_p;
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer, "mc.fontRendererObj");
        FontRenderer fr = fontRenderer;
        float size = NewNameTags.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)entity) / 2.5f;
        if (size < 4.0f) {
            size = 4.0f;
        }
        pY2 += entity.func_70093_af() ? 0.45 : 0.6;
        float scale = size * ((Number)this.scaleValue.get()).floatValue();
        scale /= 200.0f;
        IChatComponent iChatComponent = entity.func_145748_c_();
        Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
        String string = iChatComponent.func_150254_d();
        Intrinsics.checkExpressionValueIsNotNull(string, "entity.displayName.formattedText");
        tag3 = string;
        String bot = "";
        bot = AntiBot.isBot(entity) ? "\u00a77[Bot] " : "";
        int HEALTH = (int)entity.func_110143_aJ();
        String COLOR1 = null;
        COLOR1 = (double)HEALTH > 20.0 ? "\u00a79" : ((double)HEALTH >= 11.0 ? "\u00a7a" : ((double)HEALTH >= 4.0 ? "\u00a7e" : "\u00a74"));
        String hp = " [" + COLOR1 + HEALTH + " \u00a7c\u2764\u00a7f]";
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)pX), (float)((float)pY2 + 1.4f), (float)((float)pZ));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        Minecraft minecraft = NewNameTags.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        RenderManager renderManager = minecraft.func_175598_ae();
        GL11.glRotatef((float)(-renderManager.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)renderManager.field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)(-scale), (float)(-scale), (float)scale);
        GLUtils.setGLCap(2896, false);
        GLUtils.setGLCap(2929, false);
        int width = sr.func_78328_b() / 2;
        int Height = sr.func_78328_b() / 2;
        GLUtils.setGLCap(3042, true);
        GL11.glBlendFunc((int)770, (int)771);
        IChatComponent iChatComponent2 = entity.func_145748_c_();
        Intrinsics.checkExpressionValueIsNotNull(iChatComponent2, "entity.displayName");
        String lol = iChatComponent2.func_150254_d();
        String USERNAME = bot + lol + hp;
        int STRING_WIDTH = fr.func_78256_a(USERNAME) / 2;
        Gui.func_73734_a((int)(-STRING_WIDTH - 1), (int)-14, (int)(STRING_WIDTH + 1), (int)-4, (int)Integer.MIN_VALUE);
        fr.func_175063_a(USERNAME, (float)(-STRING_WIDTH), (float)(fr.field_78288_b - 22), 0xFFFFFF);
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        GL11.glScaled((double)1.0, (double)1.0, (double)1.0);
        int COLOR = new Color(200, 75, 75).getRGB();
        if (entity.func_110143_aJ() > (float)20) {
            COLOR = -65292;
        }
        GL11.glPushMatrix();
        GL11.glScaled((double)1.5, (double)1.5, (double)1.5);
        if (((Boolean)this.armorValue.get()).booleanValue()) {
            int xOffset = 0;
            EntityLivingBase entityLivingBase = entity;
            if (entityLivingBase == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.player.EntityPlayer");
            }
            for (ItemStack armourStack : ((EntityPlayer)entityLivingBase).field_71071_by.field_70460_b) {
                if (armourStack == null) continue;
                xOffset -= 10;
            }
            ItemStack renderStack = null;
            if (((EntityPlayer)entity).func_70694_bm() != null) {
                xOffset -= 8;
                ItemStack itemStack = ((EntityPlayer)entity).func_70694_bm().func_77946_l();
                Intrinsics.checkExpressionValueIsNotNull(itemStack, "entity.heldItem.copy()");
                renderStack = itemStack;
                if (renderStack.func_77962_s() && (renderStack.func_77973_b() instanceof ItemTool || renderStack.func_77973_b() instanceof ItemArmor)) {
                    renderStack.field_77994_a = 1;
                }
                this.renderItemStack(renderStack, xOffset, -34);
                xOffset += 20;
            }
            for (ItemStack armourStack : ((EntityPlayer)entity).field_71071_by.field_70460_b) {
                if (armourStack == null) continue;
                ItemStack renderStack1 = armourStack.func_77946_l();
                if (renderStack1.func_77962_s()) {
                    ItemStack itemStack = renderStack1;
                    Intrinsics.checkExpressionValueIsNotNull(itemStack, "renderStack1");
                    if (itemStack.func_77973_b() instanceof ItemTool || renderStack1.func_77973_b() instanceof ItemArmor) {
                        renderStack1.field_77994_a = 1;
                    }
                }
                ItemStack itemStack = renderStack1;
                Intrinsics.checkExpressionValueIsNotNull(itemStack, "renderStack1");
                this.renderItemStack(itemStack, xOffset, -33);
                xOffset += 20;
            }
        }
        GL11.glPopMatrix();
        GLUtils.revertAllCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public final void renderItemStack(@NotNull ItemStack stack, int x, int y) {
        Intrinsics.checkParameterIsNotNull(stack, "stack");
        GL11.glPushMatrix();
        GL11.glDepthMask((boolean)true);
        GlStateManager.func_179086_m((int)256);
        RenderHelper.func_74519_b();
        Minecraft.func_71410_x().func_175599_af().field_77023_b = -150.0f;
        this.whatTheFuckOpenGLThisFixesItemGlint();
        Minecraft.func_71410_x().func_175599_af().func_180450_b(stack, x, y);
        Minecraft.func_71410_x().func_175599_af().func_175030_a(Minecraft.func_71410_x().field_71466_p, stack, x, y);
        Minecraft.func_71410_x().func_175599_af().field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        this.renderEnchantText(stack, x, y);
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179140_f();
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.func_179097_i();
        GlStateManager.func_179126_j();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glPopMatrix();
    }

    private final void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.func_179140_f();
        GlStateManager.func_179097_i();
        GlStateManager.func_179084_k();
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
        GlStateManager.func_179140_f();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179118_c();
        GlStateManager.func_179084_k();
        GlStateManager.func_179147_l();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
    }

    private final void renderEnchantText(ItemStack stack, int x, int y) {
        int unbreakingLevel2 = 0;
        int enchantmentY = y - 8;
        if (stack.func_77986_q() != null && stack.func_77986_q().func_74745_c() >= 6) {
            NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("god", (float)x * (float)2, (float)enchantmentY, 0xFF0000);
            return;
        }
        if (stack.func_77973_b() instanceof ItemArmor) {
            int protectionLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180310_c.field_77352_x, (ItemStack)stack);
            int projectileProtectionLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180308_g.field_77352_x, (ItemStack)stack);
            int blastProtectionLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77327_f.field_77352_x, (ItemStack)stack);
            int fireProtectionLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77329_d.field_77352_x, (ItemStack)stack);
            int thornsLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_92091_k.field_77352_x, (ItemStack)stack);
            int unbreakingLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (protectionLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("pr" + protectionLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (projectileProtectionLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("pp" + projectileProtectionLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (blastProtectionLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("bp" + blastProtectionLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (fireProtectionLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("fp" + fireProtectionLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (thornsLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 't' + thornsLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (unbreakingLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 'u' + unbreakingLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
        }
        if (stack.func_77973_b() instanceof ItemBow) {
            int powerLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77345_t.field_77352_x, (ItemStack)stack);
            int punchLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77344_u.field_77352_x, (ItemStack)stack);
            int flameLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77343_v.field_77352_x, (ItemStack)stack);
            unbreakingLevel2 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (powerLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("po" + powerLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (punchLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("pu" + punchLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (flameLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 'f' + flameLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 'u' + unbreakingLevel2, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
        }
        if (stack.func_77973_b() instanceof ItemSword) {
            int sharpnessLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180314_l.field_77352_x, (ItemStack)stack);
            int knockbackLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180313_o.field_77352_x, (ItemStack)stack);
            int fireAspectLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77334_n.field_77352_x, (ItemStack)stack);
            unbreakingLevel2 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (sharpnessLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("sh" + sharpnessLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (knockbackLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("kn" + knockbackLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 'f' + fireAspectLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("ub" + unbreakingLevel2, (float)x * 1.0f, (float)enchantmentY, 65535);
            }
        }
        if (stack.func_77973_b() instanceof ItemTool) {
            int unbreakingLevel22 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            int efficiencyLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77349_p.field_77352_x, (ItemStack)stack);
            int fortuneLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77346_s.field_77352_x, (ItemStack)stack);
            int silkTouch = EnchantmentHelper.func_77506_a((int)Enchantment.field_77348_q.field_77352_x, (ItemStack)stack);
            if (efficiencyLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("eff" + efficiencyLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (fortuneLevel > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("fo" + fortuneLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (silkTouch > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("st" + silkTouch, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (unbreakingLevel22 > 0) {
                NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("ub" + unbreakingLevel22, (float)x * 1.0f, (float)enchantmentY, 65535);
            }
        }
        if (stack.func_77973_b() == Items.field_151153_ao && stack.func_77962_s()) {
            NewNameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("god", (float)x * (float)2, (float)enchantmentY, 52479);
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

