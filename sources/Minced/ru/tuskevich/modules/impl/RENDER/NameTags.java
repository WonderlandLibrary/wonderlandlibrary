// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import org.lwjgl.opengl.GL11;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.util.StringUtils;
import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemAir;
import java.util.function.Consumer;
import net.minecraft.item.ItemStack;
import java.awt.Color;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.color.ColorUtility;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.modules.impl.PLAYER.StreamerMode;
import ru.tuskevich.Minced;
import net.minecraft.entity.EntityLivingBase;
import javax.vecmath.Vector4d;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Arrays;
import javax.vecmath.Vector3d;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.render.RenderUtility;
import net.minecraft.entity.player.EntityPlayer;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.ui.dropui.setting.Setting;
import java.util.ArrayList;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import net.minecraft.entity.Entity;
import java.util.List;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "NameTags", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class NameTags extends Module
{
    public final List<Entity> collectedEntities;
    public BooleanSetting potion;
    
    public NameTags() {
        this.potion = new BooleanSetting("Show Potions", true);
        this.collectedEntities = new ArrayList<Entity>();
        this.add(this.potion);
    }
    
    @EventTarget
    public void onDisplay(final EventDisplay eventDisplay) {
        this.collectEntities();
        final List<Entity> collectedEntities = this.collectedEntities;
        for (final Entity entity : collectedEntities) {
            if (entity instanceof EntityPlayer && RenderUtility.isInViewFrustum(entity)) {
                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * NameTags.mc.getRenderPartialTicks();
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * NameTags.mc.getRenderPartialTicks();
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * NameTags.mc.getRenderPartialTicks();
                final double width = entity.width / 1.5;
                final double n = entity.height;
                double n2 = 0.0;
                Label_0193: {
                    Label_0184: {
                        if (!entity.isSneaking()) {
                            final Entity entity2 = entity;
                            final Minecraft mc = NameTags.mc;
                            if (entity2 == Minecraft.player) {
                                final Minecraft mc2 = NameTags.mc;
                                if (Minecraft.player.isSneaking()) {
                                    break Label_0184;
                                }
                            }
                            n2 = 0.2;
                            break Label_0193;
                        }
                    }
                    n2 = -0.3;
                }
                final double height = n + n2;
                final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                final List<Vector3d> vectors = Arrays.asList(new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
                NameTags.mc.entityRenderer.setupCameraTransform(NameTags.mc.getRenderPartialTicks(), 0);
                final ScaledResolution sr = eventDisplay.sr;
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = RenderUtility.vectorTo2D(ScaledResolution.getScaleFactor(), vector.x - NameTags.mc.getRenderManager().renderPosX, vector.y - NameTags.mc.getRenderManager().renderPosY, vector.z - NameTags.mc.getRenderManager().renderPosZ);
                    if (vector != null && vector.z > 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }
                if (position != null) {
                    NameTags.mc.entityRenderer.setupOverlayRendering();
                    final double posX = position.x;
                    final double posY = position.y;
                    final double endPosX = position.z;
                    final EntityLivingBase contextEntity = (EntityLivingBase)entity;
                    final float center = (float)(posX + (endPosX - posX) / 2.0);
                    float maxOffsetY = 0.0f;
                    final boolean isNameProtect = Minced.getInstance().manager.getModule(StreamerMode.class).state;
                    String name;
                    if (Minced.getInstance().friendManager.isFriend(entity.getName())) {
                        name = (isNameProtect ? (ChatFormatting.GREEN + "[\u0414\u0440\u0443\u0433] " + ChatFormatting.WHITE + "\u041c\u0430\u0441\u0441\u0438\u0432\u0430\u0442\u043e\u0440 \u043b\u0438\u0441\u0442\u0430".concat(ChatFormatting.BLUE + " (" + ChatFormatting.WHITE + (int)contextEntity.getHealth() + "HP" + ChatFormatting.BLUE + ")")) : entity.getDisplayName().getFormattedText().concat("" + ColorUtility.getHealthStr(contextEntity) + (int)contextEntity.getHealth()));
                        RenderUtility.drawRound(center - (name.isEmpty() ? 0 : Fonts.Nunito15.getStringWidth(name)) / 2 - 1.5f, (float)posY - 15.0f, (float)(width + width + Fonts.Nunito15.getStringWidth(name)), (float)(Fonts.Nunito15.getFontHeight() + 3), 0.0f, new Color(32, 246, 17, 185));
                    }
                    else if (isNameProtect && StreamerMode.youtuber.get()) {
                        name = (isNameProtect ? (ChatFormatting.GOLD + "[\u0425\u0415\u0420\u041e\u0411\u0420\u0418\u041d] " + ChatFormatting.RED + "\u0421\u042d\u041d\u042d\u0427\u041a\u0410".concat(ChatFormatting.BLUE + " (" + ChatFormatting.WHITE + (int)contextEntity.getHealth() + "HP" + ChatFormatting.BLUE + ")")) : entity.getDisplayName().getFormattedText().concat("" + ColorUtility.getHealthStr(contextEntity) + (int)contextEntity.getHealth()));
                        RenderUtility.drawRound(center - (name.isEmpty() ? 0 : Fonts.Nunito15.getStringWidth(name)) / 2 - 1.5f, (float)posY - 15.0f, (float)(width + width + Fonts.Nunito15.getStringWidth(name)), (float)(Fonts.Nunito15.getFontHeight() + 3), 0.0f, new Color(0, 128, 255, 185));
                    }
                    else {
                        name = ChatFormatting.GRAY + entity.getDisplayName().getFormattedText().concat(ChatFormatting.WHITE + " (" + ChatFormatting.RESET + ColorUtility.getHealthStr(contextEntity) + (int)contextEntity.getHealth() + "HP" + ChatFormatting.WHITE + ")");
                    }
                    RenderUtility.drawRound(center - (name.isEmpty() ? 0 : Fonts.Nunito15.getStringWidth(name)) / 2 - 1.5f, (float)posY - 15.0f, (float)(width + width + Fonts.Nunito15.getStringWidth(name)), (float)(Fonts.Nunito15.getFontHeight() + 3), 0.0f, new Color(21, 20, 22, 185));
                    Fonts.Nunito15.drawStringWithShadow(name, center - (name.isEmpty() ? 0 : Fonts.Nunito15.getStringWidth(name)) / 2, (float)posY - 13.0f, -1);
                    maxOffsetY += 15.0f;
                    final List<ItemStack> stacks = new ArrayList<ItemStack>();
                    stacks.add(contextEntity.getHeldItemMainhand());
                    stacks.add(contextEntity.getHeldItemOffhand());
                    entity.getArmorInventoryList().forEach(stacks::add);
                    stacks.removeIf(w -> w.getItem() instanceof ItemAir);
                    final int totalSize = stacks.size() * 10;
                    maxOffsetY += 19.0f;
                    int iterable = 0;
                    for (final ItemStack stack : stacks) {
                        if (stack != null) {
                            RenderHelper.enableGUIStandardItemLighting();
                            drawItemStack(stack, center + iterable * 20 - totalSize + 2.0f, posY - maxOffsetY + 1.0, null, false);
                            RenderHelper.disableStandardItemLighting();
                            ++iterable;
                            final ArrayList<String> lines = new ArrayList<String>();
                            getEnchantment(lines, stack);
                            int i = 0;
                            for (final String s : lines) {
                                GlStateManager.pushMatrix();
                                Fonts.Nunito14.drawCenteredStringWithShadow(s, center + iterable * 20 - totalSize - 10.0f, (float)posY - maxOffsetY - 6.0f - i * 7, -1);
                                ++i;
                                GlStateManager.popMatrix();
                            }
                        }
                    }
                    if (this.potion.get()) {
                        final List<String> potions = new ArrayList<String>();
                        for (final PotionEffect potionEffect : contextEntity.getActivePotionEffects()) {
                            String power = "";
                            ChatFormatting potionColor = null;
                            if (potionEffect.getDuration() < 200) {
                                potionColor = ChatFormatting.RED;
                            }
                            else if (potionEffect.getDuration() < 400) {
                                potionColor = ChatFormatting.GOLD;
                            }
                            else if (potionEffect.getDuration() > 400) {
                                potionColor = ChatFormatting.GREEN;
                            }
                            if (potionEffect.getDuration() != 0) {
                                if (potionEffect.getAmplifier() == 0) {
                                    power = "I";
                                }
                                else if (potionEffect.getAmplifier() == 1) {
                                    power = "II";
                                }
                                else if (potionEffect.getAmplifier() == 2) {
                                    power = "III";
                                }
                                else if (potionEffect.getAmplifier() == 3) {
                                    power = "IV";
                                }
                                else if (potionEffect.getAmplifier() == 4) {
                                    power = "V";
                                }
                                potions.add(I18n.format(potionEffect.getPotion().getName(), new Object[0]) + " " + power + TextFormatting.GRAY + " " + potionColor + getDuration(potionEffect));
                            }
                        }
                        float startY = (float)(posY + 3.0);
                        if (contextEntity.getTotalArmorValue() > 0 || (!contextEntity.getHeldItemMainhand().isEmpty() && !contextEntity.getHeldItemOffhand().isEmpty())) {
                            startY -= maxOffsetY + 60.0f;
                        }
                        else {
                            startY -= maxOffsetY + totalSize;
                        }
                        for (final String s2 : potions) {
                            GlStateManager.pushMatrix();
                            Fonts.Nunito14.drawCenteredStringWithShadow(s2, center + iterable * 20 - totalSize - 60.0f, startY + 5.0f, -1);
                            startY -= 10.0f;
                            GlStateManager.popMatrix();
                        }
                    }
                }
                NameTags.mc.entityRenderer.setupOverlayRendering();
                GlStateManager.enableDepth();
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
            }
        }
    }
    
    public static String getDuration(final PotionEffect potionEffect) {
        if (potionEffect.getIsPotionDurationMax()) {
            return "**:**";
        }
        return StringUtils.ticksToElapsedTime(potionEffect.getDuration());
    }
    
    private boolean isValid(final Entity entity) {
        final Minecraft mc = NameTags.mc;
        return (entity != Minecraft.player || NameTags.mc.gameSettings.thirdPersonView != 0) && !entity.isDead && entity instanceof EntityPlayer;
    }
    
    private void collectEntities() {
        this.collectedEntities.clear();
        final List<Entity> playerEntities = NameTags.mc.world.loadedEntityList;
        for (final Entity entity : playerEntities) {
            if (this.isValid(entity)) {
                this.collectedEntities.add(entity);
            }
        }
    }
    
    public static void getEnchantment(final ArrayList<String> list, final ItemStack stack) {
        final Item item = stack.getItem();
        final int protection = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);
        final int thorns = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, stack);
        final int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
        final int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
        final int feather = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, stack);
        final int depth = EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack);
        final int vanishing_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, stack);
        final int binding_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, stack);
        final int sweeping = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
        final int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
        final int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
        final int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
        final int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        final int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
        final int flame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);
        final int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
        final int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        final int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        final int silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
        final int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        final int fireprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack);
        final int blastprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);
        if (item instanceof ItemAxe) {
            if (sharpness > 0) {
                list.add("Shr" + sharpness);
            }
            if (efficiency > 0) {
                list.add("Eff" + efficiency);
            }
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
        }
        if (item instanceof ItemArmor) {
            if (vanishing_curse > 0) {
                list.add("Vanish ");
            }
            if (fireprot > 0) {
                list.add("Firp" + fireprot);
            }
            if (blastprot > 0) {
                list.add("Bla" + blastprot);
            }
            if (binding_curse > 0) {
                list.add("§cBindi ");
            }
            if (depth > 0) {
                list.add("Dep" + depth);
            }
            if (feather > 0) {
                list.add("Fea" + feather);
            }
            if (protection > 0) {
                list.add("Pro" + protection);
            }
            if (thorns > 0) {
                list.add("Thr" + thorns);
            }
            if (mending > 0) {
                list.add("Men ");
            }
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
        }
        if (item instanceof ItemBow) {
            if (vanishing_curse > 0) {
                list.add("Vanish ");
            }
            if (binding_curse > 0) {
                list.add("Binding ");
            }
            if (infinity > 0) {
                list.add("Inf" + infinity);
            }
            if (power > 0) {
                list.add("Pow" + power);
            }
            if (punch > 0) {
                list.add("Pun" + punch);
            }
            if (mending > 0) {
                list.add("Men ");
            }
            if (flame > 0) {
                list.add("Fla" + flame);
            }
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
        }
        if (item instanceof ItemSword) {
            if (vanishing_curse > 0) {
                list.add("Vanish ");
            }
            if (looting > 0) {
                list.add("Loot" + looting);
            }
            if (binding_curse > 0) {
                list.add("Bindi ");
            }
            if (sweeping > 0) {
                list.add("Swe" + sweeping);
            }
            if (sharpness > 0) {
                list.add("Shr" + sharpness);
            }
            if (knockback > 0) {
                list.add("Kno" + knockback);
            }
            if (fireAspect > 0) {
                list.add("Fir" + fireAspect);
            }
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
            if (mending > 0) {
                list.add("Men ");
            }
        }
        if (item instanceof ItemTool) {
            if (unbreaking > 0) {
                list.add("Unb" + unbreaking);
            }
            if (mending > 0) {
                list.add("Men ");
            }
            if (vanishing_curse > 0) {
                list.add("Vanish ");
            }
            if (binding_curse > 0) {
                list.add("Binding ");
            }
            if (efficiency > 0) {
                list.add("Eff" + efficiency);
            }
            if (silktouch > 0) {
                list.add("Sil" + silktouch);
            }
            if (fortune > 0) {
                list.add("For" + fortune);
            }
        }
    }
    
    public static void drawItemStack(final ItemStack stack, final double x, final double y, final String altText, final boolean withoutOverlay) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0.0);
        NameTags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        if (!withoutOverlay) {
            NameTags.mc.getRenderItem().renderItemOverlayIntoGUI(NameTags.mc.fontRenderer, stack, 0.0, 0.0, altText);
        }
        GL11.glPopMatrix();
    }
}
