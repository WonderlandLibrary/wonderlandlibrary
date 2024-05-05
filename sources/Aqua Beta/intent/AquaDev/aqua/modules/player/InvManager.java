package intent.AquaDev.aqua.modules.player;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventClick;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.TimeUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class InvManager
extends Module {
    private static int[] bestArmorDamageReducement;
    TimeUtil time = new TimeUtil();
    TimeUtil timeUtil = new TimeUtil();
    private int[] bestArmorSlots;
    private float bestSwordDamage;
    private int bestSwordSlot;
    private final List<Integer> trash = new ArrayList();
    private boolean canFake;

    public InvManager() {
        super("InvManager", "InvManager", 0, Category.Player);
        Aqua.setmgr.register(new Setting("Delay", (Module)this, 50.0, 0.0, 2000.0, false));
        Aqua.setmgr.register(new Setting("PrevSwords", (Module)this, true));
        Aqua.setmgr.register(new Setting("OpenInv", (Module)this, false));
        Aqua.setmgr.register(new Setting("FakeInv", (Module)this, true));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventClick) {
            float DelayY;
            this.searchForItems();
            for (int i = 0; i < 4; ++i) {
                if (this.bestArmorSlots[i] == -1) continue;
                int bestSlot = this.bestArmorSlots[i];
                DelayY = (float)Aqua.setmgr.getSetting("InvManagerDelay").getCurrentNumber();
                ItemStack oldArmor = InvManager.mc.thePlayer.inventory.armorItemInSlot(i);
                if (Aqua.setmgr.getSetting("InvManagerOpenInv").isState() && InvManager.mc.currentScreen instanceof GuiInventory && !Aqua.setmgr.getSetting("InvManagerFakeInv").isState() && oldArmor != null && oldArmor.getItem() != null && this.time.hasReached((long)DelayY)) {
                    InvManager.mc.playerController.windowClick(InvManager.mc.thePlayer.inventoryContainer.windowId, 8 - i, 0, 1, (EntityPlayer)InvManager.mc.thePlayer);
                    this.time.reset();
                }
                if (Aqua.setmgr.getSetting("InvManagerFakeInv").isState() && this.canFakeInv()) {
                    InvManager.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)InvManager.mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                    if (oldArmor != null && oldArmor.getItem() != null && this.time.hasReached((long)DelayY)) {
                        InvManager.mc.playerController.windowClick(InvManager.mc.thePlayer.inventoryContainer.windowId, 8 - i, 0, 1, (EntityPlayer)InvManager.mc.thePlayer);
                        this.time.reset();
                    }
                }
                if (Aqua.setmgr.getSetting("InvManagerOpenInv").isState() && InvManager.mc.currentScreen instanceof GuiInventory && !Aqua.setmgr.getSetting("InvManagerFakeInv").isState() && this.time.hasReached((long)DelayY)) {
                    InvManager.mc.playerController.windowClick(InvManager.mc.thePlayer.inventoryContainer.windowId, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, 1, (EntityPlayer)InvManager.mc.thePlayer);
                    this.time.reset();
                }
                if (!Aqua.setmgr.getSetting("InvManagerFakeInv").isState() || !this.canFakeInv()) continue;
                InvManager.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)InvManager.mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                if (!this.time.hasReached((long)DelayY)) continue;
                InvManager.mc.playerController.windowClick(InvManager.mc.thePlayer.inventoryContainer.windowId, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, 1, (EntityPlayer)InvManager.mc.thePlayer);
                this.time.reset();
            }
            if (Aqua.setmgr.getSetting("InvManagerOpenInv").isState() && InvManager.mc.currentScreen instanceof GuiInventory && !Aqua.setmgr.getSetting("InvManagerFakeInv").isState() && this.bestSwordSlot != -1 && this.bestSwordDamage != -1.0f) {
                float DelayY2 = (float)Aqua.setmgr.getSetting("InvManagerDelay").getCurrentNumber();
                if (this.time.hasReached((long)DelayY2)) {
                    InvManager.mc.playerController.windowClick(InvManager.mc.thePlayer.inventoryContainer.windowId, this.bestSwordSlot < 9 ? this.bestSwordSlot + 36 : this.bestSwordSlot, 0, 2, (EntityPlayer)InvManager.mc.thePlayer);
                    this.time.reset();
                }
                InvManager.mc.playerController.syncCurrentPlayItem();
            }
            if (Aqua.setmgr.getSetting("InvManagerFakeInv").isState() && this.canFakeInv()) {
                float DelayY3;
                InvManager.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)InvManager.mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                if (this.bestSwordSlot != -1 && this.bestSwordDamage != -1.0f && this.time.hasReached((long)(DelayY3 = (float)Aqua.setmgr.getSetting("InvManagerDelay").getCurrentNumber()))) {
                    InvManager.mc.playerController.windowClick(InvManager.mc.thePlayer.inventoryContainer.windowId, this.bestSwordSlot < 9 ? this.bestSwordSlot + 36 : this.bestSwordSlot, 0, 2, (EntityPlayer)InvManager.mc.thePlayer);
                    this.time.reset();
                }
            }
            this.searchForTrash();
            Collections.shuffle(this.trash);
            if (Aqua.setmgr.getSetting("InvManagerOpenInv").isState() && InvManager.mc.currentScreen instanceof GuiInventory && !Aqua.setmgr.getSetting("InvManagerFakeInv").isState()) {
                for (Integer integer : this.trash) {
                    DelayY = (float)Aqua.setmgr.getSetting("InvManagerDelay").getCurrentNumber();
                    if (!this.timeUtil.hasReached((long)DelayY)) continue;
                    InvManager.mc.playerController.windowClick(InvManager.mc.thePlayer.inventoryContainer.windowId, integer < 9 ? integer + 36 : integer, 1, 4, (EntityPlayer)InvManager.mc.thePlayer);
                    this.timeUtil.reset();
                }
            }
            if (Aqua.setmgr.getSetting("InvManagerFakeInv").isState() && this.canFakeInv()) {
                InvManager.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)InvManager.mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                for (Integer integer : this.trash) {
                    DelayY = (float)Aqua.setmgr.getSetting("InvManagerDelay").getCurrentNumber();
                    if (!this.timeUtil.hasReached((long)DelayY)) continue;
                    InvManager.mc.playerController.windowClick(InvManager.mc.thePlayer.inventoryContainer.windowId, integer < 9 ? integer + 36 : integer, 1, 4, (EntityPlayer)InvManager.mc.thePlayer);
                    this.timeUtil.reset();
                }
            }
        }
    }

    public boolean canFakeInv() {
        return !InvManager.mc.thePlayer.isUsingItem() && !InvManager.mc.thePlayer.isEating() && InvManager.mc.currentScreen == null && !InvManager.mc.gameSettings.keyBindUseItem.isKeyDown() && !InvManager.mc.gameSettings.keyBindAttack.isKeyDown() && !InvManager.mc.gameSettings.keyBindJump.isKeyDown() && (double)InvManager.mc.thePlayer.swingProgress == 0.0;
    }

    private void searchForItems() {
        ItemArmor armor;
        ItemStack itemStack;
        int i;
        bestArmorDamageReducement = new int[4];
        this.bestArmorSlots = new int[4];
        this.bestSwordDamage = -1.0f;
        this.bestSwordSlot = -1;
        Arrays.fill((int[])bestArmorDamageReducement, (int)-1);
        Arrays.fill((int[])this.bestArmorSlots, (int)-1);
        for (i = 0; i < this.bestArmorSlots.length; ++i) {
            itemStack = InvManager.mc.thePlayer.inventory.armorItemInSlot(i);
            if (itemStack == null || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemArmor)) continue;
            armor = (ItemArmor)itemStack.getItem();
            InvManager.bestArmorDamageReducement[i] = armor.damageReduceAmount;
        }
        for (i = 0; i < 36; ++i) {
            ItemSword sword;
            itemStack = InvManager.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) continue;
            if (itemStack.getItem() instanceof ItemArmor) {
                armor = (ItemArmor)itemStack.getItem();
                int armorType = 3 - armor.armorType;
                if (bestArmorDamageReducement[armorType] < armor.damageReduceAmount) {
                    InvManager.bestArmorDamageReducement[armorType] = armor.damageReduceAmount;
                    this.bestArmorSlots[armorType] = i;
                }
            }
            if (itemStack.getItem() instanceof ItemSword && this.bestSwordDamage < (sword = (ItemSword)itemStack.getItem()).getDamageVsEntity() + (float)EnchantmentHelper.getEnchantmentLevel((int)Enchantment.sharpness.effectId, (ItemStack)itemStack)) {
                this.bestSwordDamage = sword.getDamageVsEntity() + (float)EnchantmentHelper.getEnchantmentLevel((int)Enchantment.sharpness.effectId, (ItemStack)itemStack);
                this.bestSwordSlot = i;
            }
            if (!(itemStack.getItem() instanceof ItemTool)) continue;
            sword = (ItemTool)itemStack.getItem();
            float damage = sword.getToolMaterial().getDamageVsEntity() + (float)EnchantmentHelper.getEnchantmentLevel((int)Enchantment.sharpness.effectId, (ItemStack)itemStack);
            try {
                if (Aqua.setmgr.getSetting("InvManagerPrefSwords").isState()) {
                    damage -= 1.0f;
                }
                if (!(this.bestSwordDamage < damage)) continue;
                this.bestSwordDamage = damage;
                this.bestSwordSlot = i;
                continue;
            }
            catch (NullPointerException nullPointerException) {
                // empty catch block
            }
        }
    }

    private void searchForTrash() {
        ItemArmor armor;
        ItemStack itemStack;
        int i;
        this.trash.clear();
        bestArmorDamageReducement = new int[4];
        this.bestArmorSlots = new int[4];
        this.bestSwordDamage = -1.0f;
        this.bestSwordSlot = -1;
        Arrays.fill((int[])bestArmorDamageReducement, (int)-1);
        Arrays.fill((int[])this.bestArmorSlots, (int)-1);
        List[] allItems = new List[4];
        ArrayList allSwords = new ArrayList();
        for (i = 0; i < this.bestArmorSlots.length; ++i) {
            itemStack = InvManager.mc.thePlayer.inventory.armorItemInSlot(i);
            allItems[i] = new ArrayList();
            if (itemStack == null || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemArmor)) continue;
            armor = (ItemArmor)itemStack.getItem();
            InvManager.bestArmorDamageReducement[i] = armor.damageReduceAmount;
            this.bestArmorSlots[i] = 8 + i;
        }
        for (i = 9; i < InvManager.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
            ItemSword sword;
            itemStack = InvManager.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null || itemStack.getItem() == null) continue;
            if (itemStack.getItem() instanceof ItemArmor) {
                armor = (ItemArmor)itemStack.getItem();
                int armorType = 3 - armor.armorType;
                allItems[armorType].add((Object)i);
                if (bestArmorDamageReducement[armorType] < armor.damageReduceAmount) {
                    InvManager.bestArmorDamageReducement[armorType] = armor.damageReduceAmount;
                    this.bestArmorSlots[armorType] = i;
                }
            }
            if (itemStack.getItem() instanceof ItemSword) {
                sword = (ItemSword)itemStack.getItem();
                allSwords.add((Object)i);
                if (this.bestSwordDamage < sword.getDamageVsEntity() + (float)EnchantmentHelper.getEnchantmentLevel((int)Enchantment.sharpness.effectId, (ItemStack)itemStack)) {
                    this.bestSwordDamage = sword.getDamageVsEntity() + (float)EnchantmentHelper.getEnchantmentLevel((int)Enchantment.sharpness.effectId, (ItemStack)itemStack);
                    this.bestSwordSlot = i;
                }
            }
            if (!(itemStack.getItem() instanceof ItemTool)) continue;
            sword = (ItemTool)itemStack.getItem();
            float damage = sword.getToolMaterial().getDamageVsEntity() + (float)EnchantmentHelper.getEnchantmentLevel((int)Enchantment.sharpness.effectId, (ItemStack)itemStack);
            try {
                if (Aqua.setmgr.getSetting("InvManagerPrefSwords").isState()) {
                    damage -= 1.0f;
                }
                if (!(this.bestSwordDamage < damage)) continue;
                this.bestSwordDamage = damage;
                this.bestSwordSlot = i;
                continue;
            }
            catch (NullPointerException nullPointerException) {
                // empty catch block
            }
        }
        i = 0;
        while (i < allItems.length) {
            List allItem = allItems[i];
            int finalI = i++;
            allItem.stream().filter(slot -> slot != this.bestArmorSlots[finalI]).forEach(arg_0 -> this.trash.add(arg_0));
        }
        allSwords.stream().filter(slot -> slot != this.bestSwordSlot).forEach(arg_0 -> this.trash.add(arg_0));
    }
}
