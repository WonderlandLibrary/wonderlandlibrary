package me.rhys.client.module.player;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.breaker.TimeHelper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.ArrayUtils;

public class Stealer extends Module {
  public static boolean isChest;
  
  @Name("Tools")
  private boolean tools = true;
  
  @Name("Bow")
  private boolean bow = true;
  
  @Name("Trash")
  private boolean trash = false;
  
  @Name("Silent")
  private boolean silent = false;
  
  @Name("Blink")
  private boolean blink = false;
  
  @Name("Delay")
  @Clamp(min = 0.0D, max = 500.0D)
  private float delay = 10.0F;
  
  @Name("FirstItemDelay")
  @Clamp(min = 0.0D, max = 500.0D)
  private float firstItemDelay = 10.0F;
  
  private boolean onlychest = false;
  
  private final int[] itemHelmet;
  
  private final int[] itemChestplate;
  
  private final int[] itemLeggings;
  
  private final int[] itemBoots;
  
  public static TimeHelper time = new TimeHelper();
  
  public static TimeHelper openGuiHelper = new TimeHelper();
  
  int nextDelay = 0;
  
  CopyOnWriteArrayList<Packet<?>> packets;
  
  boolean isReleasing;
  
  public Stealer(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.packets = new CopyOnWriteArrayList<>();
    this.isReleasing = false;
    this.itemHelmet = new int[] { 298, 302, 306, 310, 314 };
    this.itemChestplate = new int[] { 299, 303, 307, 311, 315 };
    this.itemLeggings = new int[] { 300, 304, 308, 312, 316 };
    this.itemBoots = new int[] { 301, 305, 309, 313, 317 };
  }
  
  public boolean isStealing() {
    return !time.isDelayComplete(200.0D);
  }
  
  @EventTarget
  public void onPacketSend(PacketEvent e) {
    if (this.blink && e.getPacket() instanceof net.minecraft.network.play.client.C03PacketPlayer)
      if (this.mc.thePlayer.openContainer instanceof ContainerChest && isChest) {
        this.packets.add(e.getPacket());
        e.setCancelled(true);
      } else if (!this.packets.isEmpty() && !this.isReleasing) {
        this.isReleasing = true;
        for (Packet<?> packet : this.packets)
          this.mc.getNetHandler().getNetworkManager().sendPacket(packet); 
        this.packets.clear();
        this.isReleasing = false;
      }  
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    if (!GuiChest.firstItem.isDelayComplete(this.firstItemDelay))
      return; 
    if (!isChest && this.onlychest)
      return; 
    if (this.mc.thePlayer.openContainer != null && 
      this.mc.thePlayer.openContainer instanceof ContainerChest) {
      ContainerChest c = (ContainerChest)this.mc.thePlayer.openContainer;
      if (isChestEmpty(c) && openGuiHelper.isDelayComplete(800.0D) && time.isDelayComplete(400.0D))
        this.mc.thePlayer.closeScreen(); 
      for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); i++) {
        this.nextDelay = (int)this.delay;
        if (c.getLowerChestInventory().getStackInSlot(i) != null && time.isDelayComplete(this.nextDelay) && (itemIsUseful(c, i) || this.trash) && (new Random()).nextInt(100) <= 80) {
          this.mc.playerController.windowClick(c.windowId, i, 0, 1, (EntityPlayer)this.mc.thePlayer);
          this;
          time.reset();
        } 
      } 
    } 
  }
  
  private boolean isChestEmpty(ContainerChest c) {
    for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); i++) {
      if (c.getLowerChestInventory().getStackInSlot(i) != null && (
        itemIsUseful(c, i) || this.trash))
        return false; 
    } 
    return true;
  }
  
  public boolean isPotionNegative(ItemStack itemStack) {
    ItemPotion potion = (ItemPotion)itemStack.getItem();
    List<PotionEffect> potionEffectList = potion.getEffects(itemStack);
    return potionEffectList.stream().map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
      .anyMatch(Potion::isBadEffect);
  }
  
  private boolean itemIsUseful(ContainerChest c, int i) {
    ItemStack itemStack = c.getLowerChestInventory().getStackInSlot(i);
    Item item = itemStack.getItem();
    if ((item instanceof net.minecraft.item.ItemAxe || item instanceof net.minecraft.item.ItemPickaxe) && this.tools)
      return true; 
    if (item instanceof net.minecraft.item.ItemFood)
      return true; 
    if ((item instanceof net.minecraft.item.ItemBow || item == Items.arrow) && this.bow)
      return true; 
    if (item instanceof ItemPotion && !isPotionNegative(itemStack))
      return true; 
    if (item instanceof net.minecraft.item.ItemSword && isBestSword(c, itemStack))
      return true; 
    if (item instanceof ItemArmor && isBestArmor(c, itemStack))
      return true; 
    if (item instanceof net.minecraft.item.ItemBlock)
      return true; 
    return item instanceof net.minecraft.item.ItemEnderPearl;
  }
  
  private float getSwordDamage(ItemStack itemStack) {
    float damage = 0.0F;
    Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
    if (attributeModifier.isPresent())
      damage = (float)((AttributeModifier)attributeModifier.get()).getAmount(); 
    return damage + EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
  }
  
  private boolean isBestSword(ContainerChest c, ItemStack item) {
    float itemdamage1 = getSwordDamage(item);
    float itemdamage2 = 0.0F;
    int i;
    for (i = 0; i < 45; i++) {
      if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        float tempdamage = getSwordDamage(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack());
        if (tempdamage >= itemdamage2)
          itemdamage2 = tempdamage; 
      } 
    } 
    for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); i++) {
      if (c.getLowerChestInventory().getStackInSlot(i) != null) {
        float tempdamage = getSwordDamage(c.getLowerChestInventory().getStackInSlot(i));
        if (tempdamage >= itemdamage2)
          itemdamage2 = tempdamage; 
      } 
    } 
    return (itemdamage1 == itemdamage2);
  }
  
  private boolean isBestArmor(ContainerChest c, ItemStack item) {
    float itempro1 = ((ItemArmor)item.getItem()).damageReduceAmount;
    float itempro2 = 0.0F;
    if (isContain(this.itemHelmet, Item.getIdFromItem(item.getItem()))) {
      int i;
      for (i = 0; i < 45; i++) {
        if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemHelmet, 
            Item.getIdFromItem(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
          float temppro = ((ItemArmor)this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
          if (temppro > itempro2)
            itempro2 = temppro; 
        } 
      } 
      for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); i++) {
        if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemHelmet, 
            Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
          float temppro = ((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
          if (temppro > itempro2)
            itempro2 = temppro; 
        } 
      } 
    } 
    if (isContain(this.itemChestplate, Item.getIdFromItem(item.getItem()))) {
      int i;
      for (i = 0; i < 45; i++) {
        if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemChestplate, 
            Item.getIdFromItem(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
          float temppro = ((ItemArmor)this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
          if (temppro > itempro2)
            itempro2 = temppro; 
        } 
      } 
      for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); i++) {
        if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemChestplate, 
            Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
          float temppro = ((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
          if (temppro > itempro2)
            itempro2 = temppro; 
        } 
      } 
    } 
    if (isContain(this.itemLeggings, Item.getIdFromItem(item.getItem()))) {
      int i;
      for (i = 0; i < 45; i++) {
        if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemLeggings, 
            Item.getIdFromItem(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
          float temppro = ((ItemArmor)this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
          if (temppro > itempro2)
            itempro2 = temppro; 
        } 
      } 
      for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); i++) {
        if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemLeggings, 
            Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
          float temppro = ((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
          if (temppro > itempro2)
            itempro2 = temppro; 
        } 
      } 
    } 
    if (isContain(this.itemBoots, Item.getIdFromItem(item.getItem()))) {
      int i;
      for (i = 0; i < 45; i++) {
        if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemBoots, 
            Item.getIdFromItem(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
          float temppro = ((ItemArmor)this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
          if (temppro > itempro2)
            itempro2 = temppro; 
        } 
      } 
      for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); i++) {
        if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemBoots, 
            Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
          float temppro = ((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
          if (temppro > itempro2)
            itempro2 = temppro; 
        } 
      } 
    } 
    return (itempro1 == itempro2);
  }
  
  public static boolean isContain(int[] arr, int targetValue) {
    return ArrayUtils.contains(arr, targetValue);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\Stealer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */