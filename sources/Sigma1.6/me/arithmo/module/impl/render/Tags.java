package me.arithmo.module.impl.render;

import java.awt.Color;
import java.text.DecimalFormat;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventNametagRender;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.RenderingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class Tags
  extends Module
{
  public Tags(ModuleData data)
  {
    super(data);
    this.settings.put(this.ARMOR, new Setting(this.ARMOR, Boolean.valueOf(true), "Show armor."));
    this.settings.put(this.INVISIBLES, new Setting(this.INVISIBLES, Boolean.valueOf(false), "Show invisibles."));
  }
  
  private String INVISIBLES = "INVISIBLES";
  private String ARMOR = "ARMOR";
  
  @RegisterEvent(events={EventRender3D.class, EventNametagRender.class})
  public void onEvent(Event event)
  {
    EventRender3D er;
    if ((event instanceof EventRender3D))
    {
      er = (EventRender3D)event;
      for (Object o : mc.theWorld.playerEntities)
      {
        EntityPlayer player = (EntityPlayer)o;
        if ((((Boolean)((Setting)this.settings.get(this.INVISIBLES)).getValue()).booleanValue()) || ((!player.isInvisible()) && (!(player instanceof EntityPlayerSP))))
        {
          double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * er.renderPartialTicks - RenderManager.renderPosX;
          double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * er.renderPartialTicks - RenderManager.renderPosY;
          double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * er.renderPartialTicks - RenderManager.renderPosZ;
          renderNametag(player, x, y, z);
        }
      }
    }
    if ((event instanceof EventNametagRender)) {
      event.setCancelled(true);
    }
  }
  
  public void renderNametag(EntityPlayer player, double x, double y, double z)
  {
    double tempY = y + (player.isSneaking() ? 0.5D : 0.7D);
    double size = getSize(player) * -0.02D;
    GlStateManager.pushMatrix();
    GL11.glEnable(3042);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.translate((float)x, (float)tempY + 1.6F, (float)z);
    GL11.glNormal3f(0.0F, 2.0F, 0.0F);
    GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
    GlStateManager.rotate(RenderManager.playerViewX, var10001, 0.0F, 0.0F);
    GL11.glScaled(size, size, size);
    GlStateManager.disableLighting();
    int width = mc.fontRendererObj.getStringWidth(player.getName() + " " + getHealth(player)) / 2;
    GlStateManager.enableTextures();
    RenderingUtil.rectangle(-width - 2, -(mc.fontRendererObj.FONT_HEIGHT - 6), width + 2, mc.fontRendererObj.FONT_HEIGHT + 1, -1728052224);
    GlStateManager.enableTextures();
    int color = -1;
    String str = player.getName();
    if (FriendManager.isFriend(str))
    {
      color = 6083583;
      str = FriendManager.getAlias(str);
    }
    mc.fontRendererObj.drawStringWithShadow(str, -mc.fontRendererObj.getStringWidth(String.valueOf(player.getName()) + " " + getHealth(player)) / 2, 0.0F, color);
    float health = player.getHealth();
    float[] fractions = { 0.0F, 0.5F, 1.0F };
    Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
    float progress = health * 5.0F * 0.01F;
    Color customColor = ESP2D.blendColors(fractions, colors, progress).brighter();
    mc.fontRendererObj.drawStringWithShadow((int)health + "", (mc.fontRendererObj.getStringWidth(String.valueOf(player.getName()) + " " + getHealth(player)) - mc.fontRendererObj.getStringWidth(getHealth(player)) * 2) / 2, 0.0F, customColor.getRGB());
    
    GlStateManager.disableBlend();
    if (((Boolean)((Setting)this.settings.get(this.ARMOR)).getValue()).booleanValue()) {
      renderArmor(player);
    }
    GlStateManager.enableBlend();
    GL11.glColor3d(1.0D, 1.0D, 1.0D);
    GL11.glDisable(3042);
    GL11.glEnable(3553);
    GL11.glDisable(2848);
    GL11.glDisable(3042);
    GL11.glEnable(2929);
    GL11.glPopMatrix();
  }
  
  public void renderArmor(EntityPlayer player)
  {
    int xOffset = 0;
    for (ItemStack armourStack : player.inventory.armorInventory) {
      if (armourStack != null) {
        xOffset -= 8;
      }
    }
    if (player.getHeldItem() != null)
    {
      xOffset -= 8;
      ItemStack stock = player.getHeldItem().copy();
      if ((stock.hasEffect()) && (((stock.getItem() instanceof ItemTool)) || ((stock.getItem() instanceof ItemArmor)))) {
        stock.stackSize = 1;
      }
      renderItemStack(stock, xOffset, -20);
      xOffset += 16;
    }
    ItemStack[] renderStack = player.inventory.armorInventory;
    for (int index = 3; index >= 0; index--)
    {
      ItemStack armourStack = renderStack[index];
      if (armourStack != null)
      {
        ItemStack renderStack2 = armourStack;
        renderItemStack(renderStack2, xOffset, -20);
        xOffset += 16;
      }
    }
  }
  
  private int getHealthColorHEX(EntityPlayer e)
  {
    int health = Math.round(20.0F * (e.getHealth() / e.getMaxHealth()));
    int color = 0;
    switch (health)
    {
    case 18: 
    case 19: 
      color = 9108247;
      break;
    case 16: 
    case 17: 
      color = 10026904;
      break;
    case 14: 
    case 15: 
      color = 12844472;
      break;
    case 12: 
    case 13: 
      color = 16633879;
      break;
    case 10: 
    case 11: 
      color = 15313687;
      break;
    case 8: 
    case 9: 
      color = 16285719;
      break;
    case 6: 
    case 7: 
      color = 16286040;
      break;
    case 4: 
    case 5: 
      color = 15031100;
      break;
    case 2: 
    case 3: 
      color = 16711680;
      break;
    case -1: 
    case 0: 
    case 1: 
      color = 16190746;
      break;
    default: 
      color = -11746281;
    }
    return color;
  }
  
  private String getHealth(EntityPlayer e)
  {
    String hp = "";
    DecimalFormat numberFormat = new DecimalFormat("#.0");
    double abs = 2.0F * (e.getAbsorptionAmount() / 4.0F);
    double health = (10.0D + abs) * (e.getHealth() / e.getMaxHealth());
    health = Double.valueOf(numberFormat.format(health)).doubleValue();
    if (Math.floor(health) == health) {
      hp = String.valueOf((int)health);
    } else {
      hp = String.valueOf(health);
    }
    return hp;
  }
  
  private float getSize(EntityPlayer player)
  {
    Entity ent = mc.thePlayer;
    double dist = ent.getDistanceToEntity(player) / 5.0F;
    float size = dist <= 2.0D ? 1.3F : (float)dist;
    return size;
  }
  
  private void renderItemStack(ItemStack stack, int x, int y)
  {
    GlStateManager.pushMatrix();
    GlStateManager.disableAlpha();
    GlStateManager.clear(256);
    mc.getRenderItem().zLevel = -150.0F;
    mc.getRenderItem().func_180450_b(stack, x, y);
    mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
    mc.getRenderItem().zLevel = 0.0F;
    GlStateManager.disableBlend();
    GlStateManager.scale(0.5D, 0.5D, 0.5D);
    GlStateManager.disableDepth();
    GlStateManager.disableLighting();
    renderEnchantText(stack, x, y);
    GlStateManager.enableLighting();
    GlStateManager.enableDepth();
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
    GlStateManager.enableAlpha();
    GlStateManager.popMatrix();
  }
  
  private void renderEnchantText(ItemStack stack, int x, int y)
  {
    int enchantmentY = y - 24;
    if ((stack.getEnchantmentTagList() != null) && (stack.getEnchantmentTagList().tagCount() >= 6))
    {
      mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
      return;
    }
    if ((stack.getItem() instanceof ItemArmor))
    {
      int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
      int projectileProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180308_g.effectId, stack);
      int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
      int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
      int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
      int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (protectionLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("pr" + protectionLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (projectileProtectionLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("pp" + projectileProtectionLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (blastProtectionLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("bp" + blastProtectionLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (fireProtectionLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("fp" + fireProtectionLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (thornsLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("t" + thornsLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (unbreakingLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
    }
    if ((stack.getItem() instanceof ItemBow))
    {
      int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
      int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
      int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
      int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (powerLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("po" + powerLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (punchLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("pu" + punchLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (flameLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("f" + flameLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (unbreakingLevel2 > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel2, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
    }
    if ((stack.getItem() instanceof ItemSword))
    {
      int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
      int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
      int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
      int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (sharpnessLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("sh" + sharpnessLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (knockbackLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("kn" + knockbackLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (fireAspectLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("f" + fireAspectLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (unbreakingLevel2 > 0) {
        mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel2, x * 2, enchantmentY, -1052689);
      }
    }
    if ((stack.getItem() instanceof ItemTool))
    {
      int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
      int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
      int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
      if (efficiencyLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (fortuneLevel > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (silkTouch > 0)
      {
        mc.fontRendererObj.drawStringWithShadow("st" + silkTouch, x * 2, enchantmentY, -1052689);
        enchantmentY += 8;
      }
      if (unbreakingLevel3 > 0) {
        mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel3, x * 2, enchantmentY, -1052689);
      }
    }
    if ((stack.getItem() == Items.golden_apple) && (stack.hasEffect())) {
      mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, -1052689);
    }
  }
}
