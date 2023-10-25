package org.dreamcore.client.feature.impl.visual;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import optifine.CustomColors;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.misc.StreamerMode;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class NameTags extends Feature {

    public BooleanSetting armor;
    public BooleanSetting backGround;
    public NumberSetting opacity;
    public NumberSetting size;
    public ListSetting hpMode = new ListSetting("Health Mode", "HP", () -> true, "HP", "Percentage");

    public NameTags() {
        super("NameTags", "Показывает игроков, ник, броню и их здоровье сквозь стены", Type.Visuals);
        size = new NumberSetting("NameTags Size", 0.5f, 0.2f, 2, 0.01f, () -> true);
        backGround = new BooleanSetting("NameTags Background", true, () -> true);
        opacity = new NumberSetting("Background Opacity", 120, 0, 255, 10, () -> backGround.getBoolValue());
        armor = new BooleanSetting("Show Armor", true, () -> true);
        addSettings(hpMode, size, backGround, opacity, armor);
    }

    @EventTarget
    public void onRender3d(EventRender3D event) {
        for (EntityPlayer entity : mc.world.playerEntities) {
            if (entity != null) {
                double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks()) - mc.getRenderManager().renderPosX;
                double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()) - mc.getRenderManager().renderPosY;
                double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks()) - mc.getRenderManager().renderPosZ;

                String tag;
                if (dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) {
                    tag = "Protected";
                } else if (dreamcore.instance.friendManager.isFriend(entity.getName())) {
                    tag = ChatFormatting.GREEN + "[F] " + ChatFormatting.RESET + entity.getDisplayName().getUnformattedText();
                } else {
                    tag = entity.getDisplayName().getUnformattedText();
                }

                y += (entity.isSneaking() ? 0.5F : 0.7F);
                float distance = Math.min(Math.max(1.2f * (mc.player.getDistanceToEntity(entity) * 0.15f), 1.25f), 6) * 0.015f;
                int health = (int) entity.getHealth();
                if (health <= entity.getMaxHealth() * 0.25F) {
                    tag = tag + "\u00a74";
                } else if (health <= entity.getMaxHealth() * 0.5F) {
                    tag = tag + "\u00a76";
                } else if (health <= entity.getMaxHealth() * 0.75F) {
                    tag = tag + "\u00a7e";
                } else if (health <= entity.getMaxHealth()) {
                    tag = tag + "\u00a72";
                }

                String hp;
                if (hpMode.currentMode.equals("Percentage")) {
                    hp = MathematicHelper.round(entity.getHealth() / entity.getMaxHealth() * 100F, 1) + "% ";
                } else {
                    hp = MathematicHelper.round(entity.getHealth(), 1) + " ";
                }

                tag = tag + " " + hp;

                float scale = distance;
                scale = scale * size.getNumberValue();
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x, (float) y + 1.4F, (float) z);
                GL11.glNormal3f(1, 1, 1);
                float fixed = mc.gameSettings.thirdPersonView == 2 ? -1 : 1;
                GL11.glRotatef(-mc.getRenderManager().playerViewY, 0, 1, 0);
                GL11.glRotatef(mc.getRenderManager().playerViewX, fixed, 0, 0);
                GL11.glScalef(-scale, -scale, scale);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int width = mc.fontRendererObj.getStringWidth(tag) / 2;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                if (backGround.getBoolValue()) {
                    RectHelper.drawRect(-width - 2, -(mc.fontRendererObj.FONT_HEIGHT + 1), width + 2, 2, PaletteHelper.getColor(0, (int) opacity.getNumberValue()));
                }

                mc.fontRendererObj.drawStringWithShadow(tag, MathematicHelper.getMiddle(-width - 2, width + 2) - width, -(mc.fontRendererObj.FONT_HEIGHT - 1), Color.WHITE.getRGB());
                if (armor.getBoolValue()) {
                    renderArmor(entity, 0, -(mc.fontRendererObj.FONT_HEIGHT + 1) - 20);
                }

                float yPotion = (float) (y - 45);
                for (PotionEffect effectPotion : entity.getActivePotionEffects()) {
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    Potion effect = Potion.getPotionById(CustomColors.getPotionId(effectPotion.getEffectName()));
                    if (effect != null) {

                        ChatFormatting getPotionColor = null;
                        if ((effectPotion.getDuration() < 200)) {
                            getPotionColor = ChatFormatting.RED;
                        } else if (effectPotion.getDuration() < 400) {
                            getPotionColor = ChatFormatting.GOLD;
                        } else if (effectPotion.getDuration() > 400) {
                            getPotionColor = ChatFormatting.GRAY;
                        }

                        String durationString = Potion.getDurationString(effectPotion);

                        String level = I18n.format(effect.getName());
                        if (effectPotion.getAmplifier() == 1) {
                            level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.2") + " (" + getPotionColor + durationString + ")";
                        } else if (effectPotion.getAmplifier() == 2) {
                            level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.3") + " (" + getPotionColor + durationString + ")";
                        } else if (effectPotion.getAmplifier() == 3) {
                            level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.4") + " (" + getPotionColor + durationString + ")";
                        }

                        mc.fontRendererObj.drawStringWithShadow(level, MathematicHelper.getMiddle(-width - 2, width + 2) - width, yPotion, effectPotion.getPotion().getLiquidColor());
                    }
                    yPotion -= 10;
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                }

                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

    public void renderArmor(EntityPlayer player, int x, int y) {
        InventoryPlayer items = player.inventory;
        ItemStack offhand = player.getHeldItemOffhand();
        ItemStack inHand = player.getHeldItemMainhand();
        ItemStack boots = items.armorItemInSlot(0);
        ItemStack leggings = items.armorItemInSlot(1);
        ItemStack body = items.armorItemInSlot(2);
        ItemStack helm = items.armorItemInSlot(3);
        ItemStack[] stuff;
        stuff = new ItemStack[]{offhand, inHand, helm, body, leggings, boots};
        ArrayList<ItemStack> stacks = new ArrayList<>();
        ItemStack[] array;
        int length = (array = stuff).length;

        for (int j = 0; j < length; j++) {
            ItemStack i = array[j];
            if ((i != null)) {
                i.getItem();
                stacks.add(i);
            }
        }
        int width = 18 * stacks.size() / 2;
        x -= width;
        GlStateManager.disableDepth();
        for (ItemStack stack : stacks) {
            renderItem(player, stack, x, y);
            x += 18;
        }
        GlStateManager.enableDepth();
    }

    public void renderItem(EntityPlayer e, ItemStack stack, int x, int y) {
        if (stack != null) {
            RenderItem renderItem = mc.getRenderItem();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x - 3, y + 10, 0);
            GlStateManager.popMatrix();

            renderItem.zLevel = -100;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            renderItem.renderItemIntoGUI(stack, x, y);
            renderItem.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y + 2, null);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            renderItem.zLevel = 0;

            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            renderEnchant(stack, x + 2, y - 18);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.disableStandardItemLighting();

            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }
    }

    public void renderEnchant(ItemStack item, float x, int y) {
        int encY = y + 5;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
        for (Enchantment enchantment : enchantments.keySet()) {
            int level = EnchantmentHelper.getEnchantmentLevel(enchantment, item);
            mc.fontRendererObj.drawStringWithShadow((String.valueOf(enchantment.getName().substring(12).charAt(0)).toUpperCase() + level), x, encY, 16777215);
            encY -= 12;
        }
    }
}