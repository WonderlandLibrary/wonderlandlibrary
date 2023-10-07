/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.event.EventSystem;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventNametagRender;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.event.impl.EventRenderEntity;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.misc.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class NEWW
extends Module {

	public static boolean active;
    private double distance;
    private double scale;
    private boolean armor = true;
    private Character formatChar = new Character('\247');
	public boolean formatting = true;
    public static Map<EntityLivingBase, double[]> entityPositions;

	
    public NEWW(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventRenderEntity.class, EventNametagRender.class})
    public void onEvent(Event event) {
    	this.distance = 10.0D;
        this.scale = 0.1D;
        this.armor = true;
        GlStateManager.pushMatrix();
        for (Object o : mc.theWorld.loadedEntityList)
        {
            Entity ent = (Entity)o;
            if (ent != mc.thePlayer)
            {
                if (((ent instanceof EntityPlayer)) && (!ent.isInvisible()))
                {
                    double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * EventRender3D.partialTicks - RenderManager.renderPosX;
                    double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * EventRender3D.partialTicks - RenderManager.renderPosY + ent.height + 0.5D;
                    double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * EventRender3D.partialTicks - RenderManager.renderPosZ;
                    String str = ent.getDisplayName().getFormattedText();
                    if (FriendManager.isFriend(ent.getName())) {
                        str = str.replace(ent.getName(), "\247b" + ent.getName());
                    }
                    String colorString = this.formatChar.toString();
                    double health = MathUtil.round(((EntityPlayer)ent).getHealth(), 2);
                    if (health >= 12.0D) {
                        colorString = String.valueOf(colorString) + "2";
                    } else if (health >= 4.0D) {
                        colorString = String.valueOf(colorString) + "6";
                    } else {
                        colorString = String.valueOf(colorString) + "4";
                    }
                    str = String.valueOf(str) + " " + colorString + Math.round(health / 2.0D);
                    float dist = mc.thePlayer.getDistanceToEntity(ent);
                    float scale = 0.02672F;
                    float factor = (float)(dist <= this.distance ? this.distance * this.scale : dist * this.scale);
                    scale *= factor;
                    GlStateManager.pushMatrix();
                    GlStateManager.disableDepth();
                    GlStateManager.translate(posX, posY, posZ);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-mc.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(mc.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-scale, -scale, scale);
                    GlStateManager.disableLighting();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                    GlStateManager.func_179090_x();
                    worldRenderer.startDrawingQuads();
                    float stringWidth = Client.f.getStringWidth(str) / 2;
                    GL11.glColor3f(0.0F, 0.0F, 0.0F);
                    GL11.glLineWidth(1.0E-6F);
                    GL11.glBegin(3);
                    GL11.glVertex2d(-stringWidth - 2, -0.8D);
                    GL11.glVertex2d(-stringWidth - 2, 8.8D);
                    GL11.glVertex2d(-stringWidth - 2, 8.8D);
                    GL11.glVertex2d(stringWidth + 2, 8.8D);
                    GL11.glVertex2d(stringWidth + 2, 8.8D);
                    GL11.glVertex2d(stringWidth + 2, -0.8D);
                    GL11.glVertex2d(stringWidth + 2, -0.8D);
                    GL11.glVertex2d(-stringWidth - 2, -0.8D);
                    GL11.glEnd();
                    worldRenderer.func_178974_a(0, 100);
                    worldRenderer.addVertex(-stringWidth - 2, -0.8D, 0.0D);
                    worldRenderer.addVertex(-stringWidth - 2, 8.8D, 0.0D);
                    worldRenderer.addVertex(stringWidth + 2, 8.8D, 0.0D);
                    worldRenderer.addVertex(stringWidth + 2, -0.8D, 0.0D);
                    tessellator.draw();
                    GlStateManager.func_179098_w();
                    mc.fontRendererObj.drawString(str, -Client.f.getStringWidth(str) / 2, 0, -1);
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    if ((this.armor) && ((ent instanceof EntityPlayer)))
                    {
                        List<ItemStack> itemsToRender = new ArrayList();
                        for (int i = 0; i < 5; i++)
                        {
                            ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(i);
                            if (stack != null) {
                                itemsToRender.add(stack);
                            }
                        }
                        int x = -(itemsToRender.size() * 8);
                        Iterator<ItemStack> iterator2 = itemsToRender.iterator();
                        while (iterator2.hasNext())
                        {
                            ItemStack stack = (ItemStack)iterator2.next();
                            GlStateManager.disableDepth();
                            RenderHelper.enableGUIStandardItemLighting();
                            mc.getRenderItem().zLevel = -100.0F;
                            mc.getRenderItem().func_175042_a(stack, x, -18);
                            mc.getRenderItem().func_175030_a(Minecraft.getMinecraft().fontRendererObj, stack, x, -18);
                            mc.getRenderItem().zLevel = 0.0F;
                            RenderHelper.disableStandardItemLighting();
                            GlStateManager.enableDepth();
                            String text = "";
                            if (stack != null)
                            {
                                int y = 0;
                                int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
                                int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                                int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
                                if (sLevel > 0)
                                {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("s" + sLevel, x, y);
                                    y -= 9;
                                }
                                if (fLevel > 0)
                                {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("f" + fLevel, x, y);
                                    y -= 9;
                                }
                                if (kLevel > 0)
                                {
                                    GL11.glDisable(2896);
                                    drawEnchantTag("k" + kLevel, x, y);
                                }
                                else if ((stack.getItem() instanceof ItemArmor))
                                {
                                    int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
                                    int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                                    int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                                    if (pLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("p" + pLevel, x, y);
                                        y -= 9;
                                    }
                                    if (tLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("t" + tLevel, x, y);
                                        y -= 9;
                                    }
                                    if (uLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("u" + uLevel, x, y);
                                    }
                                }
                                else if ((stack.getItem() instanceof ItemBow))
                                {
                                    int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                                    int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                                    int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
                                    if (powLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("p" + powLevel, x, y);
                                        y -= 9;
                                    }
                                    if (punLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("p" + punLevel, x, y);
                                        y -= 9;
                                    }
                                    if (fireLevel > 0)
                                    {
                                        GL11.glDisable(2896);
                                        drawEnchantTag("f" + fireLevel, x, y);
                                    }
                                }
                                else if (stack.getRarity() == EnumRarity.EPIC)
                                {
                                    drawEnchantTag("\247cGod", x, y);
                                } 
                                x += 16;
                            }
                        }
                    }
                    GlStateManager.popMatrix();
                }
                GlStateManager.disableBlend();
            }
        }
        GlStateManager.popMatrix();
        Event enr = EventSystem.getInstance(EventNametagRender.class);
        enr.setCancelled(true);
    }

    private static void drawEnchantTag(String text, int x, int y)
    {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x = (int)(x * 1.75D);
        GL11.glScalef(0.57F, 0.57F, 0.57F);
        y -= 4;
        mc.fontRendererObj.drawString(text, x, -36 - y, 64250);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}
