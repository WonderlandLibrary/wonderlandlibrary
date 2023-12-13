// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.util.ColourHolder;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemArmor;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import java.awt.Color;
import com.elementars.eclient.Xulu;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.elementars.eclient.module.combat.PopCounter;
import net.minecraft.util.math.MathHelper;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.enemy.Enemies;
import com.elementars.eclient.friend.Friends;
import com.elementars.eclient.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.friend.Nicknames;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraft.client.renderer.culling.Frustum;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.client.renderer.culling.ICamera;
import dev.xulu.settings.Value;
import com.elementars.eclient.util.RenderUtils;
import com.elementars.eclient.module.Module;

public class Nametags extends Module
{
    private final RenderUtils renderUtils;
    private final Value<Boolean> outline;
    private final Value<Boolean> Orainbow;
    private final Value<Integer> Ored;
    private final Value<Integer> Ogreen;
    private final Value<Integer> Oblue;
    private final Value<Integer> Oalpha;
    private final Value<Float> Owidth;
    private final Value<Boolean> reversed;
    private final Value<Boolean> reversedHand;
    private final Value<Boolean> cf;
    private final Value<Boolean> max;
    private final Value<Boolean> maxText;
    private final Value<Boolean> health;
    private final Value<Boolean> gameMode;
    private final Value<Boolean> ping;
    private final Value<Boolean> pingColor;
    private final Value<Boolean> armor;
    private final Value<Boolean> durability;
    private final Value<Boolean> item;
    private final Value<Boolean> invisibles;
    private final Value<Boolean> pops;
    private final Value<Float> scale;
    private final Value<Float> height;
    private final Value<String> friendMode;
    private final Value<Boolean> friends;
    private final Value<Boolean> enemies;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final Value<Integer> Ered;
    private final Value<Integer> Egreen;
    private final Value<Integer> Eblue;
    public static Nametags INSTANCE;
    private ICamera camera;
    boolean shownItem;
    
    public Nametags() {
        super("NameTags", "Enhances nametags", 0, Category.RENDER, true);
        this.renderUtils = new RenderUtils();
        this.outline = this.register(new Value<Boolean>("Outline", this, true));
        this.Orainbow = this.register(new Value<Boolean>("Outline Rainbow", this, false));
        this.Ored = this.register(new Value<Integer>("Outline Red", this, 0, 0, 255));
        this.Ogreen = this.register(new Value<Integer>("Outline Green", this, 0, 0, 255));
        this.Oblue = this.register(new Value<Integer>("Outline Blue", this, 0, 0, 255));
        this.Oalpha = this.register(new Value<Integer>("Outline Alpha", this, 150, 0, 255));
        this.Owidth = this.register(new Value<Float>("Outline Width", this, 1.5f, 0.0f, 3.0f));
        this.reversed = this.register(new Value<Boolean>("Reversed", this, false));
        this.reversedHand = this.register(new Value<Boolean>("Reversed Hand", this, false));
        this.cf = this.register(new Value<Boolean>("Custom Font", this, false));
        this.max = this.register(new Value<Boolean>("Show Max Enchants", this, true));
        this.maxText = this.register(new Value<Boolean>("Show Max Text", this, true));
        this.health = this.register(new Value<Boolean>("Health", this, true));
        this.gameMode = this.register(new Value<Boolean>("GameMode", this, true));
        this.ping = this.register(new Value<Boolean>("Ping", this, true));
        this.pingColor = this.register(new Value<Boolean>("Ping Color", this, true));
        this.armor = this.register(new Value<Boolean>("Armor", this, true));
        this.durability = this.register(new Value<Boolean>("Durability", this, true));
        this.item = this.register(new Value<Boolean>("Item Name", this, true));
        this.invisibles = this.register(new Value<Boolean>("Invisibles", this, false));
        this.pops = this.register(new Value<Boolean>("Pop Count", this, true));
        this.scale = this.register(new Value<Float>("Scale", this, 0.05f, 0.01f, 0.09f));
        this.height = this.register(new Value<Float>("Height", this, 2.5f, 0.5f, 5.0f));
        this.friendMode = this.register(new Value<String>("Friend Mode", this, "Text", new ArrayList<String>(Arrays.asList("Text", "Box"))));
        this.friends = this.register(new Value<Boolean>("Friends", this, true));
        this.enemies = this.register(new Value<Boolean>("Enemies", this, true));
        this.red = this.register(new Value<Integer>("Friend Red", this, 0, 0, 255));
        this.green = this.register(new Value<Integer>("Friend Green", this, 130, 0, 255));
        this.blue = this.register(new Value<Integer>("Friend Blue", this, 130, 0, 255));
        this.Ered = this.register(new Value<Integer>("Enemy Red", this, 200, 0, 255));
        this.Egreen = this.register(new Value<Integer>("Enemy Green", this, 0, 0, 255));
        this.Eblue = this.register(new Value<Integer>("Enemy Blue", this, 0, 0, 255));
        this.camera = (ICamera)new Frustum();
        Nametags.INSTANCE = this;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (Nametags.mc.player == null) {
            return;
        }
        final double d3 = Nametags.mc.player.lastTickPosX + (Nametags.mc.player.posX - Nametags.mc.player.lastTickPosX) * event.getPartialTicks();
        final double d4 = Nametags.mc.player.lastTickPosY + (Nametags.mc.player.posY - Nametags.mc.player.lastTickPosY) * event.getPartialTicks();
        final double d5 = Nametags.mc.player.lastTickPosZ + (Nametags.mc.player.posZ - Nametags.mc.player.lastTickPosZ) * event.getPartialTicks();
        this.camera.setPosition(d3, d4, d5);
        final List<EntityPlayer> players = new ArrayList<EntityPlayer>(Nametags.mc.world.playerEntities);
        players.sort(Comparator.comparing(entityPlayer -> Nametags.mc.player.getDistance((Entity)entityPlayer)).reversed());
        for (final EntityPlayer p : players) {
            final NetworkPlayerInfo npi = Nametags.mc.player.connection.getPlayerInfo(p.getGameProfile().getId());
            if (!this.camera.isBoundingBoxInFrustum(p.getEntityBoundingBox())) {
                continue;
            }
            if (p == Nametags.mc.getRenderViewEntity() || !p.isEntityAlive()) {
                continue;
            }
            final double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * Nametags.mc.timer.renderPartialTicks - Nametags.mc.renderManager.renderPosX;
            final double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * Nametags.mc.timer.renderPartialTicks - Nametags.mc.renderManager.renderPosY;
            final double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * Nametags.mc.timer.renderPartialTicks - Nametags.mc.renderManager.renderPosZ;
            if (npi != null && this.getShortName(npi.getGameType().getName()).equalsIgnoreCase("SP") && !this.invisibles.getValue()) {
                continue;
            }
            if (p.getName().startsWith("Body #")) {
                continue;
            }
            this.renderNametag(p, pX, pY, pZ);
        }
    }
    
    public String getShortName(final String gameType) {
        if (gameType.equalsIgnoreCase("survival")) {
            return "S";
        }
        if (gameType.equalsIgnoreCase("creative")) {
            return "C";
        }
        if (gameType.equalsIgnoreCase("adventure")) {
            return "A";
        }
        if (gameType.equalsIgnoreCase("spectator")) {
            return "SP";
        }
        return "NONE";
    }
    
    public String getHealth(final float health) {
        if (health > 18.0f) {
            return "a";
        }
        if (health > 16.0f) {
            return "2";
        }
        if (health > 12.0f) {
            return "e";
        }
        if (health > 8.0f) {
            return "6";
        }
        if (health > 5.0f) {
            return "c";
        }
        return "4";
    }
    
    public String getPing(final float ping) {
        if (ping > 200.0f) {
            return "c";
        }
        if (ping > 100.0f) {
            return "e";
        }
        return "a";
    }
    
    private String getName(final EntityPlayer player) {
        if (Nicknames.hasNickname(player.getName())) {
            return Nicknames.getNickname(player.getName());
        }
        return player.getName();
    }
    
    public void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        final int l4 = 0;
        this.shownItem = false;
        GlStateManager.pushMatrix();
        final FontRenderer var13 = Wrapper.getMinecraft().fontRenderer;
        final NetworkPlayerInfo npi = Nametags.mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
        final boolean isFriend = Friends.isFriend(player.getName()) && this.friends.getValue();
        final boolean isEnemy = Enemies.isEnemy(player.getName()) && this.enemies.getValue();
        String name = (((isFriend || isEnemy) && this.friendMode.getValue().equalsIgnoreCase("Text")) ? (Command.SECTIONSIGN() + (isFriend ? "b" : "c")) : (player.isSneaking() ? (Command.SECTIONSIGN() + "9") : (Command.SECTIONSIGN() + "r"))) + this.getName(player) + ((this.gameMode.getValue() && npi != null) ? (" [" + this.getShortName(npi.getGameType().getName()) + "]") : "") + ((this.ping.getValue() && npi != null) ? (" " + (this.pingColor.getValue() ? (Command.SECTIONSIGN() + this.getPing((float)npi.getResponseTime())) : "") + npi.getResponseTime() + "ms") : "") + (this.health.getValue() ? (" " + Command.SECTIONSIGN() + this.getHealth(player.getHealth() + player.getAbsorptionAmount()) + MathHelper.ceil(player.getHealth() + player.getAbsorptionAmount())) : "") + ((PopCounter.INSTANCE.popMap.containsKey(player) && this.pops.getValue()) ? (" " + ChatFormatting.DARK_RED + "-" + PopCounter.INSTANCE.popMap.get(player)) : "");
        name = name.replace(".0", "");
        final float distance = Nametags.mc.player.getDistance((Entity)player);
        final float var14 = ((distance / 5.0f <= 2.0f) ? 2.0f : (distance / 5.0f * (this.scale.getValue() * 10.0f + 1.0f))) * 2.5f * (this.scale.getValue() / 10.0f);
        final float var15 = this.scale.getValue() * this.getNametagSize((EntityLivingBase)player);
        GL11.glTranslated((double)(float)x, (float)y + this.height.getValue() - (player.isSneaking() ? 0.4 : 0.0) + ((distance / 5.0f > 2.0f) ? (distance / 12.0f - 0.7) : 0.0), (double)(float)z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-Nametags.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(Nametags.mc.renderManager.playerViewX, (Nametags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var14, -var14, var14);
        this.renderUtils.disableGlCap(2896, 2929);
        this.renderUtils.enableGlCap(3042);
        GL11.glBlendFunc(770, 771);
        int width;
        if (this.cf.getValue()) {
            width = Xulu.cFontRenderer.getStringWidth(name) / 2 + 1;
        }
        else {
            width = var13.getStringWidth(name) / 2;
        }
        final int color = ((isFriend || isEnemy) && this.friendMode.getValue().equalsIgnoreCase("Box")) ? (isFriend ? new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB() : new Color(this.Ered.getValue(), this.Egreen.getValue(), this.Eblue.getValue()).getRGB()) : ColorUtils.Colors.BLACK;
        int outlineColor = new Color(this.Ored.getValue(), this.Ogreen.getValue(), this.Oblue.getValue(), this.Oalpha.getValue()).getRGB();
        if (this.Orainbow.getValue()) {
            outlineColor = ColorUtils.changeAlpha(Xulu.rgb, this.Oalpha.getValue());
        }
        Gui.drawRect(-width - 2, 10, width + 1, 20, ColorUtils.changeAlpha(color, 120));
        if (this.outline.getValue()) {
            XuluTessellator.drawOutlineLine(-width - 2, 10.0, width + 1, 20.0, this.Owidth.getValue(), outlineColor);
        }
        if (this.cf.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(name, -width, 10.0, -1);
        }
        else {
            Nametags.mc.fontRenderer.drawStringWithShadow(name, (float)(-width), 11.0f, -1);
        }
        if (this.armor.getValue()) {
            int xOffset = -6;
            int count = 0;
            for (final ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack != null) {
                    xOffset -= 8;
                    if (armourStack.getItem() == Items.AIR) {
                        continue;
                    }
                    ++count;
                }
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) {
                ++count;
            }
            final int cacheX = xOffset - 8;
            xOffset += 8 * (5 - count) - ((count == 0) ? 4 : 0);
            Label_1555: {
                Label_1534: {
                    if (this.reversedHand.getValue()) {
                        if (player.getHeldItemOffhand().getItem() == Items.AIR) {
                            break Label_1534;
                        }
                    }
                    else if (player.getHeldItemMainhand().getItem() == Items.AIR) {
                        break Label_1534;
                    }
                    xOffset -= 8;
                    if (this.reversedHand.getValue()) {
                        final ItemStack renderStack = player.getHeldItemOffhand().copy();
                        this.renderItem(player, renderStack, xOffset, -10, cacheX, false);
                    }
                    else {
                        final ItemStack renderStack = player.getHeldItemMainhand().copy();
                        this.renderItem(player, renderStack, xOffset, -10, cacheX, true);
                    }
                    xOffset += 16;
                    break Label_1555;
                }
                if (!this.reversedHand.getValue()) {
                    this.shownItem = true;
                }
            }
            if (this.reversed.getValue()) {
                for (int index = 0; index <= 3; ++index) {
                    final ItemStack armourStack2 = (ItemStack)player.inventory.armorInventory.get(index);
                    if (armourStack2 != null && armourStack2.getItem() != Items.AIR) {
                        final ItemStack renderStack2 = armourStack2.copy();
                        this.renderItem(player, renderStack2, xOffset, -10, cacheX, false);
                        xOffset += 16;
                    }
                }
            }
            else {
                for (int index = 3; index >= 0; --index) {
                    final ItemStack armourStack2 = (ItemStack)player.inventory.armorInventory.get(index);
                    if (armourStack2 != null && armourStack2.getItem() != Items.AIR) {
                        final ItemStack renderStack2 = armourStack2.copy();
                        this.renderItem(player, renderStack2, xOffset, -10, cacheX, false);
                        xOffset += 16;
                    }
                }
            }
            Label_1833: {
                if (this.reversedHand.getValue()) {
                    if (player.getHeldItemMainhand().getItem() == Items.AIR) {
                        break Label_1833;
                    }
                }
                else if (player.getHeldItemOffhand().getItem() == Items.AIR) {
                    break Label_1833;
                }
                xOffset += 0;
                if (this.reversedHand.getValue()) {
                    final ItemStack renderOffhand = player.getHeldItemMainhand().copy();
                    this.renderItem(player, renderOffhand, xOffset, -10, cacheX, true);
                }
                else {
                    final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
                    this.renderItem(player, renderOffhand, xOffset, -10, cacheX, false);
                }
                xOffset += 8;
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }
        else if (this.durability.getValue()) {
            int xOffset = -6;
            int count = 0;
            for (final ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack != null) {
                    xOffset -= 8;
                    if (armourStack.getItem() == Items.AIR) {
                        continue;
                    }
                    ++count;
                }
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) {
                ++count;
            }
            final int cacheX = xOffset - 8;
            xOffset += 8 * (5 - count) - ((count == 0) ? 4 : 0);
            if (this.reversed.getValue()) {
                for (int index = 0; index <= 3; ++index) {
                    final ItemStack armourStack2 = (ItemStack)player.inventory.armorInventory.get(index);
                    if (armourStack2 != null && armourStack2.getItem() != Items.AIR) {
                        final ItemStack renderStack2 = armourStack2.copy();
                        this.renderDurabilityText(player, renderStack2, xOffset, -10);
                        xOffset += 16;
                    }
                }
            }
            else {
                for (int index = 3; index >= 0; --index) {
                    final ItemStack armourStack2 = (ItemStack)player.inventory.armorInventory.get(index);
                    if (armourStack2 != null && armourStack2.getItem() != Items.AIR) {
                        final ItemStack renderStack2 = armourStack2.copy();
                        this.renderDurabilityText(player, renderStack2, xOffset, -10);
                        xOffset += 16;
                    }
                }
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }
        this.renderUtils.resetCaps();
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public float getNametagSize(final EntityLivingBase player) {
        final ScaledResolution scaledRes = new ScaledResolution(Nametags.mc);
        final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
        return (float)twoDscale + Nametags.mc.player.getDistance((Entity)player) / 7.0f;
    }
    
    public void drawBorderRect(final float left, final float top, final float right, final float bottom, final int bcolor, final int icolor, final float f) {
        drawGuiRect(left + f, top + f, right - f, bottom - f, icolor);
        drawGuiRect(left, top, left + f, bottom, bcolor);
        drawGuiRect(left + f, top, right, top + f, bcolor);
        drawGuiRect(left + f, bottom - f, right, bottom, bcolor);
        drawGuiRect(right - f, top + f, right, bottom - f, bcolor);
    }
    
    public static void drawGuiRect(final double x1, final double y1, final double x2, final double y2, final int color) {
        final float red = (color >> 24 & 0xFF) / 255.0f;
        final float green = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float alpha = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(green, blue, alpha, red);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void fakeGuiRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        GlStateManager.pushMatrix();
        enableGL2D();
        fakeGuiRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        fakeGuiRect(x + width, y, x1 - width, y + width, borderColor);
        fakeGuiRect(x, y, x + width, y1, borderColor);
        fakeGuiRect(x1 - width, y, x1, y1, borderColor);
        fakeGuiRect(x + width, y1 - width, x1 - width, y1, borderColor);
        disableGL2D();
        GlStateManager.popMatrix();
    }
    
    public void renderItem(final EntityPlayer player, final ItemStack stack, final int x, final int y, final int nameX, final boolean showHeldItemText) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -100.0f;
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        Nametags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y / 2 - 12);
        if (this.durability.getValue()) {
            Nametags.mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRenderer, stack, x, y / 2 - 12);
        }
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        this.renderEnchantText(player, stack, x, y - 18);
        if (!this.shownItem && this.item.getValue() && showHeldItemText) {
            if (this.cf.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(stack.getDisplayName().equalsIgnoreCase("Air") ? "" : stack.getDisplayName(), nameX * 2 + 95 - Xulu.cFontRenderer.getStringWidth(stack.getDisplayName()) / 2, y - 37, -1);
            }
            else {
                Nametags.mc.fontRenderer.drawStringWithShadow(stack.getDisplayName().equalsIgnoreCase("Air") ? "" : stack.getDisplayName(), (float)(nameX * 2 + 95 - Nametags.mc.fontRenderer.getStringWidth(stack.getDisplayName()) / 2), (float)(y - 37), -1);
            }
            this.shownItem = true;
        }
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    public boolean isMaxEnchants(final ItemStack stack) {
        final NBTTagList enchants = stack.getEnchantmentTagList();
        final List<String> enchantments = new ArrayList<String>();
        int count = 0;
        if (enchants == null) {
            return false;
        }
        for (int index = 0; index < enchants.tagCount(); ++index) {
            final short id = enchants.getCompoundTagAt(index).getShort("id");
            final short level = enchants.getCompoundTagAt(index).getShort("lvl");
            final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc != null) {
                enchantments.add(enc.getTranslatedName((int)level));
            }
        }
        if (stack.getItem() == Items.DIAMOND_HELMET) {
            final int maxnum = 5;
            for (final String s : enchantments) {
                if (s.equalsIgnoreCase("Protection IV")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Respiration III")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Aqua Affinity")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Unbreaking III")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Mending")) {
                    ++count;
                }
            }
            return count >= maxnum;
        }
        if (stack.getItem() == Items.DIAMOND_CHESTPLATE) {
            final int maxnum = 3;
            for (final String s : enchantments) {
                if (s.equalsIgnoreCase("Protection IV")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Unbreaking III")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Mending")) {
                    ++count;
                }
            }
            return count >= maxnum;
        }
        if (stack.getItem() == Items.DIAMOND_LEGGINGS) {
            final int maxnum = 3;
            for (final String s : enchantments) {
                if (s.equalsIgnoreCase("Blast Protection IV")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Unbreaking III")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Mending")) {
                    ++count;
                }
            }
            return count >= maxnum;
        }
        if (stack.getItem() == Items.DIAMOND_BOOTS) {
            final int maxnum = 5;
            for (final String s : enchantments) {
                if (s.equalsIgnoreCase("Protection IV")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Feather Falling IV")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Depth Strider III")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Unbreaking III")) {
                    ++count;
                }
                if (s.equalsIgnoreCase("Mending")) {
                    ++count;
                }
            }
            return count >= maxnum;
        }
        return false;
    }
    
    private void renderDurabilityText(final EntityPlayer player, final ItemStack stack, final int x, final int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
            final float green = (stack.getMaxDamage() - (float)stack.getItemDamage()) / stack.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            if (this.cf.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(dmg + "%", x * 2 + 4, y - 10, ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            else {
                Nametags.mc.fontRenderer.drawStringWithShadow(dmg + "%", (float)(x * 2 + 4), (float)(y - 10), ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    public void renderEnchantText(final EntityPlayer player, final ItemStack stack, final int x, final int y) {
        int encY = y;
        int yCount = y;
        if ((stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) && this.durability.getValue()) {
            final float green = (stack.getMaxDamage() - (float)stack.getItemDamage()) / stack.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            if (this.cf.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(dmg + "%", x * 2 + 4, y - 10, ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            else {
                Nametags.mc.fontRenderer.drawStringWithShadow(dmg + "%", (float)(x * 2 + 4), (float)(y - 10), ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
        }
        if (this.max.getValue() && this.isMaxEnchants(stack)) {
            GL11.glPushMatrix();
            GL11.glScalef(1.0f, 1.0f, 0.0f);
            if (this.maxText.getValue()) {
                if (this.cf.getValue()) {
                    Xulu.cFontRenderer.drawStringWithShadow("Max", x * 2 + 7, yCount + 24, ColorUtils.Colors.RED);
                }
                else {
                    Nametags.mc.fontRenderer.drawStringWithShadow("Max", (float)(x * 2 + 7), (float)(yCount + 24), ColorUtils.Colors.RED);
                }
            }
            GL11.glScalef(1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
            return;
        }
        final NBTTagList enchants = stack.getEnchantmentTagList();
        if (enchants != null) {
            for (int index = 0; index < enchants.tagCount(); ++index) {
                final short id = enchants.getCompoundTagAt(index).getShort("id");
                final short level = enchants.getCompoundTagAt(index).getShort("lvl");
                final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                if (enc != null) {
                    if (!enc.isCurse()) {
                        String encName = (level == 1) ? enc.getTranslatedName((int)level).substring(0, 3).toLowerCase() : (enc.getTranslatedName((int)level).substring(0, 2).toLowerCase() + level);
                        encName = encName.substring(0, 1).toUpperCase() + encName.substring(1);
                        GL11.glPushMatrix();
                        GL11.glScalef(1.0f, 1.0f, 0.0f);
                        if (this.cf.getValue()) {
                            Xulu.cFontRenderer.drawStringWithShadow(encName, x * 2 + 3, yCount, -1);
                        }
                        else {
                            Nametags.mc.fontRenderer.drawStringWithShadow(encName, (float)(x * 2 + 3), (float)yCount, -1);
                        }
                        GL11.glScalef(1.0f, 1.0f, 1.0f);
                        GL11.glPopMatrix();
                        encY += 8;
                        yCount += 8;
                    }
                }
            }
        }
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
}
