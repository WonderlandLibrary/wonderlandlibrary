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
 *  net.minecraft.util.Timer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiBot;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.GLUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
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
import net.minecraft.util.Timer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="NameTags", category=ModuleCategory.RENDER, description="Changes the scale of the nametags so you can always read them.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0018\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u001fH\u0007J \u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$H\u0002J\u001e\u0010&\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$J0\u0010'\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u001c2\u0006\u0010(\u001a\u00020\u001b2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020*2\u0006\u0010,\u001a\u00020*H\u0002J\b\u0010-\u001a\u00020\u0017H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/NameTags;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "armorValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "borderValue", "decimalFormat", "Ljava/text/DecimalFormat;", "distanceValue", "easingHP", "", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "healthValue", "jelloAlphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "jelloColorValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "pingValue", "scaleValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getColor", "", "entity", "Lnet/minecraft/entity/Entity;", "getPlayerName", "", "Lnet/minecraft/entity/EntityLivingBase;", "onRender3D", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "renderEnchantText", "stack", "Lnet/minecraft/item/ItemStack;", "x", "", "y", "renderItemStack", "renderNameTag", "tag", "pX", "", "pY", "pZ", "whatTheFuckOpenGLThisFixesItemGlint", "KyinoClient"})
public final class NameTags
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"2D", "Simple", "Ghost", "LiquidSense", "Jello"}, "LiquidSense");
    private final BoolValue healthValue = new BoolValue("Health", true);
    private final BoolValue distanceValue = new BoolValue("Distance", false);
    private final BoolValue armorValue = new BoolValue("Armor", true);
    private final BoolValue pingValue = new BoolValue("Ping", true);
    private final FontValue fontValue;
    private final BoolValue borderValue;
    private final BoolValue jelloColorValue;
    private final IntegerValue jelloAlphaValue;
    private final FloatValue scaleValue;
    private final DecimalFormat decimalFormat;
    private float easingHP;

    private final String getPlayerName(EntityLivingBase entity) {
        IChatComponent iChatComponent = entity.func_145748_c_();
        Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
        String name = iChatComponent.func_150254_d();
        String pre = "";
        if (LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(entity.func_70005_c_())) {
            pre = pre + "\u00a7b[Friend] ";
        }
        if (AntiBot.isBot(entity)) {
            pre = pre + "\u00a7e[BOT] ";
        }
        if (!AntiBot.isBot(entity)) {
            pre = LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(entity.func_70005_c_()) ? "\u00a7b[Friend] \u00a7c" : "\u00a7c";
        }
        return name + pre;
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Iterator iterator2 = NameTags.access$getMc$p$s1046033730().field_71441_e.field_73010_i.iterator();
        while (iterator2.hasNext()) {
            EntityPlayer o;
            EntityPlayer entityPlayer = o = (EntityPlayer)iterator2.next();
            if (entityPlayer == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
            }
            EntityLivingBase e = (EntityLivingBase)entityPlayer;
            if (!e.func_70089_S() || e == NameTags.access$getMc$p$s1046033730().field_71439_g || !EntityUtils.isSelected((Entity)e, false)) continue;
            Minecraft minecraft = NameTags.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            RenderManager renderManager = minecraft.func_175598_ae();
            double pX = e.field_70142_S + (e.field_70165_t - e.field_70142_S) * (double)NameTags.access$getMc$p$s1046033730().field_71428_T.field_74281_c - renderManager.field_78725_b;
            double pY = e.field_70137_T + (e.field_70163_u - e.field_70137_T) * (double)NameTags.access$getMc$p$s1046033730().field_71428_T.field_74281_c - renderManager.field_78726_c;
            double pZ = e.field_70136_U + (e.field_70161_v - e.field_70136_U) * (double)NameTags.access$getMc$p$s1046033730().field_71428_T.field_74281_c - renderManager.field_78723_d;
            String string = e.func_70005_c_();
            Intrinsics.checkExpressionValueIsNotNull(string, "e.name");
            this.renderNameTag(e, string, pX, pY, pZ);
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void renderNameTag(EntityLivingBase entity, String tag2, double pX, double pY, double pZ) {
        FontRenderer fontRenderer = (FontRenderer)this.fontValue.get();
        GL11.glPushMatrix();
        Minecraft minecraft = NameTags.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        RenderManager renderManager = minecraft.func_175598_ae();
        Timer timer = NameTags.access$getMc$p$s1046033730().field_71428_T;
        GL11.glTranslated((double)(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b), (double)(entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c + (double)entity.func_70047_e() + 0.55), (double)(entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d));
        Minecraft minecraft2 = NameTags.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
        GL11.glRotatef((float)(-minecraft2.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        Minecraft minecraft3 = NameTags.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
        GL11.glRotatef((float)minecraft3.func_175598_ae().field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
        float distance = NameTags.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)entity) / 4.0f;
        if (distance < 1.0f) {
            distance = 1.0f;
        }
        float scale = distance / 150.0f * ((Number)this.scaleValue.get()).floatValue();
        RenderUtils.disableGlCap(2896, 2929);
        RenderUtils.enableGlCap(3042);
        GL11.glBlendFunc((int)770, (int)771);
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "simple": {
                float healthPercent = RangesKt.coerceAtMost(entity.func_110143_aJ() / entity.func_110138_aP(), 1.0f);
                int width = RangesKt.coerceAtLeast(fontRenderer.func_78256_a(tag2), 30) / 2;
                float maxWidth = (float)(width * 2) + 12.0f;
                GL11.glScalef((float)(-scale * (float)2), (float)(-scale * (float)2), (float)(scale * (float)2));
                RenderUtils.drawRect((float)(-width) - 6.0f, (float)(-fontRenderer.field_78288_b) * 1.7f, (float)width + 6.0f, -2.0f, new Color(0, 0, 0, ((Number)this.jelloAlphaValue.get()).intValue()));
                RenderUtils.drawRect((float)(-width) - 6.0f, -2.0f, (float)(-width) - 6.0f + maxWidth * healthPercent, 0.0f, ColorUtils.healthColor(entity.func_110143_aJ(), entity.func_110138_aP(), ((Number)this.jelloAlphaValue.get()).intValue()));
                RenderUtils.drawRect((float)(-width) - 6.0f + maxWidth * healthPercent, -2.0f, (float)width + 6.0f, 0.0f, new Color(0, 0, 0, ((Number)this.jelloAlphaValue.get()).intValue()));
                int n = (int)((float)(-fontRenderer.func_78256_a(tag2)) * 0.5f);
                int n2 = (int)((float)(-fontRenderer.field_78288_b) * 1.4f);
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
                fontRenderer.func_78276_b(tag2, n, n2, color.getRGB());
                break;
            }
            case "2d": {
                String tag3 = tag2;
                double pY2 = pY;
                ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
                FontRenderer fontRenderer2 = NameTags.access$getMc$p$s1046033730().field_71466_p;
                Intrinsics.checkExpressionValueIsNotNull(fontRenderer2, "mc.fontRendererObj");
                FontRenderer fr = fontRenderer2;
                float size = NameTags.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)entity) / 2.5f;
                if (size < 4.0f) {
                    size = 4.0f;
                }
                pY2 += entity.func_70093_af() ? 0.45 : 0.6;
                float scale2 = size * ((Number)this.scaleValue.get()).floatValue();
                scale2 /= 200.0f;
                IChatComponent iChatComponent = entity.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
                String string4 = iChatComponent.func_150254_d();
                Intrinsics.checkExpressionValueIsNotNull(string4, "entity.displayName.formattedText");
                tag3 = string4;
                String bot = "";
                bot = AntiBot.isBot(entity) ? "\u00a77[Bot] " : "";
                int HEALTH = (int)entity.func_110143_aJ();
                String COLOR1 = null;
                COLOR1 = (double)HEALTH > 20.0 ? "\u00a79" : ((double)HEALTH >= 11.0 ? "\u00a7a" : ((double)HEALTH >= 4.0 ? "\u00a7e" : "\u00a74"));
                String hp = " [" + COLOR1 + HEALTH + " \u00a7c\u2764\u00a7f]";
                GL11.glPushMatrix();
                GL11.glTranslatef((float)((float)pX), (float)((float)pY2 + 1.4f), (float)((float)pZ));
                GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                Minecraft minecraft4 = NameTags.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft4, "mc");
                RenderManager renderManager2 = minecraft4.func_175598_ae();
                GL11.glRotatef((float)(-renderManager2.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)renderManager2.field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glScalef((float)(-scale2), (float)(-scale2), (float)scale2);
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
                break;
            }
            case "sigma2dbeta": {
                String tag4 = tag2;
                double pY3 = pY;
                ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
                FontRenderer fontRenderer3 = NameTags.access$getMc$p$s1046033730().field_71466_p;
                Intrinsics.checkExpressionValueIsNotNull(fontRenderer3, "mc.fontRendererObj");
                FontRenderer fr = fontRenderer3;
                float size = NameTags.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)entity) / 2.5f;
                if (size < 4.0f) {
                    size = 4.0f;
                }
                pY3 += entity.func_70093_af() ? 0.45 : 0.6;
                float scale3 = size * ((Number)this.scaleValue.get()).floatValue();
                scale3 /= 200.0f;
                IChatComponent iChatComponent = entity.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
                String string5 = iChatComponent.func_150254_d();
                Intrinsics.checkExpressionValueIsNotNull(string5, "entity.displayName.formattedText");
                tag4 = string5;
                String bot = "";
                bot = AntiBot.isBot(entity) ? "\u00a77[Bot] " : "";
                int HEALTH = (int)entity.func_110143_aJ();
                String COLOR1 = null;
                COLOR1 = (double)HEALTH > 20.0 ? "\u00a79" : ((double)HEALTH >= 11.0 ? "\u00a7a" : ((double)HEALTH >= 4.0 ? "\u00a7e" : "\u00a74"));
                String hp = " [" + COLOR1 + HEALTH + " \u00a7c\u2764\u00a7f]";
                GL11.glPushMatrix();
                GL11.glTranslatef((float)((float)pX), (float)((float)pY3 + 1.4f), (float)((float)pZ));
                GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                Minecraft minecraft5 = NameTags.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft5, "mc");
                RenderManager renderManager3 = minecraft5.func_175598_ae();
                GL11.glRotatef((float)(-renderManager3.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)renderManager3.field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glScalef((float)(-scale3), (float)(-scale3), (float)scale3);
                GLUtils.setGLCap(2896, false);
                GLUtils.setGLCap(2929, false);
                int width2 = sr.func_78328_b() / 2;
                int Height = sr.func_78328_b() / 2;
                GLUtils.setGLCap(3042, true);
                GL11.glBlendFunc((int)770, (int)771);
                IChatComponent iChatComponent3 = entity.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent3, "entity.displayName");
                String lol = iChatComponent3.func_150254_d();
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
                break;
            }
            case "liquidsense": {
                String botText;
                int HEALTH = (int)entity.func_110143_aJ();
                String COLOR1 = null;
                COLOR1 = (double)HEALTH > 20.0 ? "\u00a79" : ((double)HEALTH >= 13.0 ? "\u00a7a" : ((double)HEALTH >= 4.0 ? "\u00a7e" : "\u00a74"));
                EntityLivingBase entityLiving = entity;
                this.getColor((Entity)entityLiving);
                Unit color = Unit.INSTANCE;
                boolean bot = AntiBot.isBot(entity);
                String string6 = botText = bot ? " \u00a77[\u00a76\u00a7lBot\u00a77]" : "";
                String nameColor = bot ? "\u00a73" : (entity.func_82150_aj() ? "\u00a76" : (entity.func_70093_af() ? "\u00a74" : "\u00a77"));
                String healthText2 = (Boolean)this.healthValue.get() != false ? ' ' + COLOR1 + this.decimalFormat.format(Float.valueOf(entity.func_110143_aJ() / entity.func_110138_aP() * (float)100)) + '%' : "";
                String hp = " [" + COLOR1 + HEALTH + " \u00a7c\u2764\u00a7f]";
                String distanceText2 = (Boolean)this.distanceValue.get() != false ? "\u00a77 [\u00a7a" + MathKt.roundToInt(NameTags.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)entity)) + "\u00a77]" : "";
                String text = distanceText2 + nameColor + tag2 + healthText2 + botText;
                GL11.glScalef((float)(-scale), (float)(-scale), (float)scale);
                int width = fontRenderer.func_78256_a(text) / 2;
                RenderUtils.drawRect((float)(-width) - 2.0f, -2.0f, (float)width + 4.0f, (float)fontRenderer.field_78288_b + 2.0f, Integer.MIN_VALUE);
                fontRenderer.func_175065_a(text, 1.0f + (float)(-width), Intrinsics.areEqual(fontRenderer, Fonts.minecraftFont) ? 1.0f : 1.5f, 0xFFFFFF, true);
                if (!((Boolean)this.armorValue.get()).booleanValue() || !(entity instanceof EntityPlayer)) break;
                int width2 = 0;
                int n = 4;
                while (width2 <= n) {
                    void index;
                    if (entity.func_71124_b((int)index) != null) {
                        Intrinsics.checkExpressionValueIsNotNull(NameTags.access$getMc$p$s1046033730(), "mc");
                        NameTags.access$getMc$p$s1046033730().func_175599_af().field_77023_b = -147.0f;
                        Minecraft minecraft6 = NameTags.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft6, "mc");
                        minecraft6.func_175599_af().func_180450_b(entity.func_71124_b((int)index), -50 + index * 20, -22);
                    }
                    ++index;
                }
                GlStateManager.func_179141_d();
                GlStateManager.func_179084_k();
                GlStateManager.func_179098_w();
                break;
            }
            case "jello": {
                Color hpBarColor = new Color(255, 255, 255, ((Number)this.jelloAlphaValue.get()).intValue());
                IChatComponent iChatComponent = entity.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
                String name = iChatComponent.func_150260_c();
                if (((Boolean)this.jelloColorValue.get()).booleanValue()) {
                    String string7 = name;
                    Intrinsics.checkExpressionValueIsNotNull(string7, "name");
                    if (StringsKt.startsWith$default(string7, "\u00a7", false, 2, null)) {
                        String entityLiving = name;
                        int color = 1;
                        int bot = 2;
                        boolean botText = false;
                        String string8 = entityLiving.substring(color, bot);
                        Intrinsics.checkExpressionValueIsNotNull(string8, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        hpBarColor = ColorUtils.colorCode(string8, ((Number)this.jelloAlphaValue.get()).intValue());
                    }
                }
                Color bgColor = new Color(50, 50, 50, ((Number)this.jelloAlphaValue.get()).intValue());
                int width = fontRenderer.func_78256_a(tag2) / 2;
                float maxWidth = (float)width + 4.0f - ((float)(-width) - 4.0f);
                float healthPercent = entity.func_110143_aJ() / entity.func_110138_aP();
                GL11.glScalef((float)(-scale * (float)2), (float)(-scale * (float)2), (float)(scale * (float)2));
                RenderUtils.drawRect((float)(-width) - 4.0f, (float)(-fontRenderer.field_78288_b) * 3.0f, (float)width + 4.0f, -3.0f, bgColor);
                if (healthPercent > 1.0f) {
                    healthPercent = 1.0f;
                }
                RenderUtils.drawRect((float)(-width) - 4.0f, -3.0f, (float)(-width) - 4.0f + maxWidth * healthPercent, 1.0f, hpBarColor);
                RenderUtils.drawRect((float)(-width) - 4.0f + maxWidth * healthPercent, -3.0f, (float)width + 4.0f, 1.0f, bgColor);
                int n = -width;
                int n3 = -fontRenderer.field_78288_b * 2 - 4;
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
                fontRenderer.func_78276_b(tag2, n, n3, color.getRGB());
                GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
                String string9 = "Health: " + (int)entity.func_110143_aJ();
                int n4 = -width * 2;
                int n5 = -fontRenderer.field_78288_b * 2;
                Color color2 = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull(color2, "Color.WHITE");
                fontRenderer.func_78276_b(string9, n4, n5, color2.getRGB());
                break;
            }
            case "ghost": {
                boolean bot = AntiBot.isBot(entity);
                String nameColor = bot ? "\u00a73" : (entity.func_82150_aj() ? "\u00a76" : (entity.func_70093_af() ? "\u00a74" : "\u00a77"));
                String distanceText = (Boolean)this.distanceValue.get() != false ? "\u00a77 [\u00a7a" + MathKt.roundToInt(NameTags.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)entity)) + "\u00a77]" : "";
                String healthText = (Boolean)this.healthValue.get() != false ? "\u00a77 [\u00a7f" + (int)entity.func_110143_aJ() + "\u00a7c\u2764\u00a77]" : "";
                String botText = bot ? " \u00a77[\u00a76\u00a7lBot\u00a77]" : "";
                String text = distanceText + nameColor + tag2 + healthText + botText;
                GL11.glScalef((float)(-scale), (float)(-scale), (float)scale);
                int width = fontRenderer.func_78256_a(text) / 2;
                if (((Boolean)this.borderValue.get()).booleanValue()) {
                    RenderUtils.drawBorderedRect((float)(-width) - 2.0f, -2.0f, (float)width + 4.0f, (float)fontRenderer.field_78288_b + 2.0f, 2.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
                } else {
                    RenderUtils.drawRect((float)(-width) - 2.0f, -2.0f, (float)width + 4.0f, (float)fontRenderer.field_78288_b + 2.0f, Integer.MIN_VALUE);
                }
                fontRenderer.func_175065_a(text, 1.0f + (float)(-width), Intrinsics.areEqual(fontRenderer, Fonts.minecraftFont) ? 1.0f : 1.5f, 0xFFFFFF, true);
                if (!((Boolean)this.armorValue.get()).booleanValue() || !(entity instanceof EntityPlayer)) break;
                int healthText2 = 0;
                int hp = 4;
                while (healthText2 <= hp) {
                    void index;
                    if (entity.func_71124_b((int)index) != null) {
                        Intrinsics.checkExpressionValueIsNotNull(NameTags.access$getMc$p$s1046033730(), "mc");
                        NameTags.access$getMc$p$s1046033730().func_175599_af().field_77023_b = -147.0f;
                        Minecraft minecraft7 = NameTags.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft7, "mc");
                        minecraft7.func_175599_af().func_180450_b(entity.func_71124_b((int)index), -50 + index * 20, -22);
                    }
                    ++index;
                }
                GlStateManager.func_179141_d();
                GlStateManager.func_179084_k();
                GlStateManager.func_179098_w();
                break;
            }
            case "liquidbounce": {
                String distanceText;
                boolean bot = AntiBot.isBot(entity);
                String nameColor = bot ? "\u00a73" : (entity.func_82150_aj() ? "\u00a76" : (entity.func_70093_af() ? "\u00a74" : "\u00a77"));
                int ping = entity instanceof EntityPlayer ? EntityUtils.getPing((EntityPlayer)entity) : 0;
                String string10 = distanceText = (Boolean)this.distanceValue.get() != false ? "\u00a77" + MathKt.roundToInt(NameTags.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)entity)) + "m " : "";
                String pingText = (Boolean)this.pingValue.get() != false && entity instanceof EntityPlayer ? (ping > 200 ? "\u00a7c" : (ping > 100 ? "\u00a7e" : "\u00a7a")) + ping + "ms \u00a77" : "";
                String healthText = (Boolean)this.healthValue.get() != false ? "\u00a77\u00a7c " + (int)entity.func_110143_aJ() + " HP" : "";
                String botText = bot ? " \u00a77[\u00a76\u00a7lBot\u00a77]" : "";
                String text = distanceText + pingText + nameColor + tag2 + healthText + botText;
                GL11.glScalef((float)(-scale), (float)(-scale), (float)scale);
                int width = fontRenderer.func_78256_a(text) / 2;
                if (((Boolean)this.borderValue.get()).booleanValue()) {
                    RenderUtils.drawBorderedRect((float)(-width) - 2.0f, -2.0f, (float)width + 4.0f, (float)fontRenderer.field_78288_b + 2.0f, 2.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
                } else {
                    RenderUtils.drawRect((float)(-width) - 2.0f, -2.0f, (float)width + 4.0f, (float)fontRenderer.field_78288_b + 2.0f, Integer.MIN_VALUE);
                }
                fontRenderer.func_175065_a(text, 1.0f + (float)(-width), Intrinsics.areEqual(fontRenderer, Fonts.minecraftFont) ? 1.0f : 1.5f, 0xFFFFFF, true);
                if (!((Boolean)this.armorValue.get()).booleanValue() || !(entity instanceof EntityPlayer)) break;
                int distanceText2 = 0;
                int n = 4;
                while (distanceText2 <= n) {
                    void index;
                    if (entity.func_71124_b((int)index) != null) {
                        Intrinsics.checkExpressionValueIsNotNull(NameTags.access$getMc$p$s1046033730(), "mc");
                        NameTags.access$getMc$p$s1046033730().func_175599_af().field_77023_b = -147.0f;
                        Minecraft minecraft8 = NameTags.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft8, "mc");
                        minecraft8.func_175599_af().func_180450_b(entity.func_71124_b((int)index), -50 + index * 20, -22);
                    }
                    ++index;
                }
                GlStateManager.func_179141_d();
                GlStateManager.func_179084_k();
                GlStateManager.func_179098_w();
            }
        }
        RenderUtils.resetCaps();
        GlStateManager.func_179117_G();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public final void getColor(@Nullable Entity entity) {
        if (entity instanceof EntityLivingBase) {
            Entity entity2 = entity;
        }
    }

    private final void renderEnchantText(ItemStack stack, int x, int y) {
        int unbreakingLevel2 = 0;
        int enchantmentY = y - 8;
        if (stack.func_77986_q() != null && stack.func_77986_q().func_74745_c() >= 6) {
            NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("god", (float)x * (float)2, (float)enchantmentY, 0xFF0000);
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
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("pr" + protectionLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (projectileProtectionLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("pp" + projectileProtectionLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (blastProtectionLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("bp" + blastProtectionLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (fireProtectionLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("fp" + fireProtectionLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (thornsLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 't' + thornsLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
            if (unbreakingLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 'u' + unbreakingLevel, (float)x * 1.0f, (float)enchantmentY, 52479);
                enchantmentY += 8;
            }
        }
        if (stack.func_77973_b() instanceof ItemBow) {
            int powerLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77345_t.field_77352_x, (ItemStack)stack);
            int punchLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77344_u.field_77352_x, (ItemStack)stack);
            int flameLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77343_v.field_77352_x, (ItemStack)stack);
            unbreakingLevel2 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (powerLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("po" + powerLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (punchLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("pu" + punchLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (flameLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 'f' + flameLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 'u' + unbreakingLevel2, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
        }
        if (stack.func_77973_b() instanceof ItemSword) {
            int sharpnessLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180314_l.field_77352_x, (ItemStack)stack);
            int knockbackLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180313_o.field_77352_x, (ItemStack)stack);
            int fireAspectLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77334_n.field_77352_x, (ItemStack)stack);
            unbreakingLevel2 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (sharpnessLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("sh" + sharpnessLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (knockbackLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("kn" + knockbackLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("" + 'f' + fireAspectLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("ub" + unbreakingLevel2, (float)x * 1.0f, (float)enchantmentY, 65535);
            }
        }
        if (stack.func_77973_b() instanceof ItemTool) {
            int unbreakingLevel22 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            int efficiencyLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77349_p.field_77352_x, (ItemStack)stack);
            int fortuneLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77346_s.field_77352_x, (ItemStack)stack);
            int silkTouch = EnchantmentHelper.func_77506_a((int)Enchantment.field_77348_q.field_77352_x, (ItemStack)stack);
            if (efficiencyLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("eff" + efficiencyLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (fortuneLevel > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("fo" + fortuneLevel, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (silkTouch > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("st" + silkTouch, (float)x * 1.0f, (float)enchantmentY, 65535);
                enchantmentY += 8;
            }
            if (unbreakingLevel22 > 0) {
                NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("ub" + unbreakingLevel22, (float)x * 1.0f, (float)enchantmentY, 65535);
            }
        }
        if (stack.func_77973_b() == Items.field_151153_ao && stack.func_77962_s()) {
            NameTags.access$getMc$p$s1046033730().field_71466_p.func_175063_a("god", (float)x * (float)2, (float)enchantmentY, 52479);
        }
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

    public NameTags() {
        FontRenderer fontRenderer = Fonts.minecraftFont;
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer, "Fonts.minecraftFont");
        this.fontValue = new FontValue("Font", fontRenderer);
        this.borderValue = new BoolValue("Border", false);
        this.jelloColorValue = new BoolValue("Jello-HPColor", false);
        this.jelloAlphaValue = new IntegerValue("Jello-Alpha", 170, 0, 255);
        this.scaleValue = new FloatValue("Scale", 1.0f, 1.0f, 4.0f);
        this.decimalFormat = new DecimalFormat("0");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

