// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import java.util.Iterator;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.augustus.utils.MoveUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.augustus.utils.RandomUtil;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.augustus.events.EventEarlyTick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.utils.BlockUtil;
import org.lwjgl.input.Mouse;
import net.augustus.events.EventClick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class InventoryCleaner extends Module
{
    private final TimeHelper timeHelper;
    private final TimeHelper timeHelper2;
    public StringValue mode;
    public BooleanValue noMove;
    public BooleanValue interactionCheck;
    public DoubleValue delay;
    public DoubleValue startDelay;
    public BooleanValue sort;
    private boolean blockInv;
    
    public InventoryCleaner() {
        super("InvCleaner", new Color(32, 94, 103), Categorys.PLAYER);
        this.timeHelper = new TimeHelper();
        this.timeHelper2 = new TimeHelper();
        this.mode = new StringValue(1, "Mode", this, "OpenInv", new String[] { "OpenInv", "SpoofInv", "Basic" });
        this.noMove = new BooleanValue(5, "NoMove", this, false);
        this.interactionCheck = new BooleanValue(6, "InteractCheck", this, false);
        this.delay = new DoubleValue(2, "Delay", this, 100.0, 0.0, 200.0, 0);
        this.startDelay = new DoubleValue(3, "StartDelay", this, 100.0, 0.0, 1000.0, 0);
        this.sort = new BooleanValue(4, "Sort", this, true);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (InventoryCleaner.mc.thePlayer != null) {
            InventoryCleaner.mc.thePlayer.setServerInv(InventoryCleaner.mc.currentScreen);
        }
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        this.blockInv = false;
        if (this.mode.getSelected().equals("SpoofInv") && this.interactionCheck.getBoolean() && (Mouse.isButtonDown(1) || Mouse.isButtonDown(0) || (InventoryCleaner.mm.killAura.isToggled() && InventoryCleaner.mm.killAura.target != null) || BlockUtil.isScaffoldToggled())) {
            this.closeInv();
            this.blockInv = true;
            this.timeHelper2.reset();
        }
    }
    
    @EventTarget
    public void onEventTick(final EventEarlyTick eventEarlyTick) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        final ArrayList<ItemStack> swords = new ArrayList<ItemStack>();
        final ArrayList<ItemStack> bows = new ArrayList<ItemStack>();
        final ArrayList<ItemStack> rods = new ArrayList<ItemStack>();
        final ArrayList<ItemStack> foods = new ArrayList<ItemStack>();
        final ArrayList<ItemStack> gapples = new ArrayList<ItemStack>();
        final ArrayList<ItemStack> potions = new ArrayList<ItemStack>();
        final ArrayList<ItemStack> axes = new ArrayList<ItemStack>();
        final ArrayList<ItemStack> pickAxes = new ArrayList<ItemStack>();
        final ArrayList<ItemStack> shovels = new ArrayList<ItemStack>();
        final ArrayList<Integer> allToKeep = new ArrayList<Integer>();
        final int swordID = 0;
        final int bowID = 1;
        final int rodID = 2;
        final int foodID = 7;
        final int gappleID = 8;
        ItemStack sword = null;
        ItemStack bow = null;
        ItemStack rod = null;
        ItemStack food = null;
        ItemStack gapple = null;
        ItemStack axe = null;
        final ItemStack pickAxe = null;
        final ItemStack shovel = null;
        ItemStack newSword = null;
        ItemStack newBow = null;
        ItemStack newRod = null;
        ItemStack newFood = null;
        ItemStack newGapple = null;
        ItemStack newAxe = null;
        ItemStack newPickAxe = null;
        ItemStack newShovel = null;
        final long delay = (long)(this.delay.getValue() + RandomUtil.nextLong(0L, 60L));
        final long startDelay = (long)(this.startDelay.getValue() + RandomUtil.nextLong(-35L, 35L));
        final boolean invCleaner = InventoryCleaner.mc.currentScreen instanceof GuiInventory || (!this.mode.getSelected().equalsIgnoreCase("OpenInv") && (InventoryCleaner.mc.currentScreen == null || InventoryCleaner.mc.currentScreen instanceof GuiInventory) && (!this.noMove.getBoolean() || (this.noMove.getBoolean() && !MoveUtil.isMoving())));
        if (invCleaner && !this.blockInv) {
            for (int i = 0; i < InventoryCleaner.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack itemStack = InventoryCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null) {
                    if (itemStack.getItem() instanceof ItemSword) {
                        itemStack.setSlotID(i);
                        swords.add(itemStack);
                        if (i == swordID + 36) {
                            itemStack.setSlotID(i);
                            sword = itemStack;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemAxe) {
                        itemStack.setSlotID(i);
                        axes.add(itemStack);
                        if (i == swordID + 36) {
                            itemStack.setSlotID(i);
                            axe = itemStack;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemPickaxe) {
                        itemStack.setSlotID(i);
                        pickAxes.add(itemStack);
                    }
                    else if (itemStack.getItem() instanceof ItemSpade) {
                        itemStack.setSlotID(i);
                        shovels.add(itemStack);
                    }
                    else if (itemStack.getItem() instanceof ItemBlock) {
                        if (this.itemsToDrop(itemStack)) {
                            itemStack.setSlotID(i);
                            allToKeep.add(i);
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemBow) {
                        itemStack.setSlotID(i);
                        bows.add(itemStack);
                        if (i == bowID + 36) {
                            itemStack.setSlotID(i);
                            bow = itemStack;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemFishingRod) {
                        itemStack.setSlotID(i);
                        rods.add(itemStack);
                        if (i == rodID + 36) {
                            itemStack.setSlotID(i);
                            rod = itemStack;
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemFood) {
                        if (itemStack.getItem() == Item.getByNameOrId("golden_apple")) {
                            itemStack.setSlotID(i);
                            gapples.add(itemStack);
                            allToKeep.add(i);
                            if (i == gappleID + 36) {
                                itemStack.setSlotID(i);
                                gapple = itemStack;
                            }
                        }
                        else if (this.itemsToDrop(itemStack)) {
                            itemStack.setSlotID(i);
                            foods.add(itemStack);
                            allToKeep.add(i);
                            if (i == foodID + 36) {
                                itemStack.setSlotID(i);
                                food = itemStack;
                            }
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemPotion) {
                        final ItemPotion itemPotion = (ItemPotion)itemStack.getItem();
                        if (!itemPotion.getEffects(itemStack).isEmpty()) {
                            final PotionEffect potionEffect = itemPotion.getEffects(itemStack).get(0);
                            final Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                            if (!potion.isBadEffect()) {
                                itemStack.setSlotID(i);
                                potions.add(itemStack);
                                allToKeep.add(i);
                            }
                        }
                    }
                    else if (itemStack.getItem() instanceof ItemTool) {
                        if (this.itemsToDrop(itemStack)) {
                            itemStack.setSlotID(i);
                            allToKeep.add(i);
                        }
                    }
                    else if (this.itemsToKeep(itemStack)) {
                        itemStack.setSlotID(i);
                        allToKeep.add(i);
                    }
                    else if (!(itemStack.getItem() instanceof ItemArmor)) {
                        if (this.itemsToDrop(itemStack)) {
                            itemStack.setSlotID(i);
                            allToKeep.add(i);
                        }
                    }
                }
            }
            final Iterator<ItemStack> iterator = swords.iterator();
            while (iterator.hasNext()) {
                final ItemStack itemStack = iterator.next();
                if (itemStack.getSlotID() != swordID + 36) {
                    if (sword == null) {
                        if (newSword != null) {
                            if (this.getDamageSword(itemStack) > this.getDamageSword(newSword)) {
                                newSword = itemStack;
                            }
                            else {
                                if (this.getDamageSword(itemStack) != this.getDamageSword(newSword) || itemStack.getItemDamage() >= newSword.getItemDamage()) {
                                    continue;
                                }
                                newSword = itemStack;
                            }
                        }
                        else {
                            newSword = itemStack;
                        }
                    }
                    else {
                        if (!(sword.getItem() instanceof ItemSword)) {
                            continue;
                        }
                        if (newSword != null) {
                            if (this.getDamageSword(itemStack) > this.getDamageSword(newSword)) {
                                newSword = itemStack;
                            }
                            else {
                                if (this.getDamageSword(itemStack) != this.getDamageSword(newSword) || itemStack.getItemDamage() >= newSword.getItemDamage()) {
                                    continue;
                                }
                                newSword = itemStack;
                            }
                        }
                        else if (this.getDamageSword(itemStack) > this.getDamageSword(sword)) {
                            newSword = itemStack;
                        }
                        else {
                            if (this.getDamageSword(itemStack) != this.getDamageSword(sword) || itemStack.getItemDamage() >= sword.getItemDamage()) {
                                continue;
                            }
                            newSword = itemStack;
                        }
                    }
                }
            }
            final Iterator<ItemStack> iterator2 = axes.iterator();
            while (iterator2.hasNext()) {
                final ItemStack itemStack = iterator2.next();
                if (axe == null) {
                    if (newAxe != null) {
                        if (this.getToolDamage(itemStack) > this.getToolDamage(newAxe)) {
                            newAxe = itemStack;
                        }
                        else {
                            if (this.getToolDamage(itemStack) != this.getToolDamage(newAxe) || itemStack.getItemDamage() >= newAxe.getItemDamage()) {
                                continue;
                            }
                            newAxe = itemStack;
                        }
                    }
                    else {
                        newAxe = itemStack;
                    }
                }
                else {
                    if (!(axe.getItem() instanceof ItemAxe)) {
                        continue;
                    }
                    if (newAxe != null) {
                        if (this.getToolDamage(itemStack) > this.getToolDamage(newAxe)) {
                            newAxe = itemStack;
                        }
                        else {
                            if (this.getToolDamage(itemStack) != this.getToolDamage(newAxe) || itemStack.getItemDamage() >= newAxe.getItemDamage()) {
                                continue;
                            }
                            newAxe = itemStack;
                        }
                    }
                    else if (this.getToolDamage(itemStack) > this.getToolDamage(axe)) {
                        newAxe = itemStack;
                    }
                    else {
                        if (this.getToolDamage(itemStack) != this.getToolDamage(axe) || itemStack.getItemDamage() >= axe.getItemDamage()) {
                            continue;
                        }
                        newAxe = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator3 = pickAxes.iterator();
            while (iterator3.hasNext()) {
                final ItemStack itemStack = iterator3.next();
                if (pickAxe == null) {
                    if (newPickAxe != null) {
                        if (this.getToolSpeed(itemStack) > this.getToolSpeed(newPickAxe)) {
                            newPickAxe = itemStack;
                        }
                        else {
                            if (this.getToolSpeed(itemStack) != this.getToolSpeed(newPickAxe) || itemStack.getItemDamage() >= newPickAxe.getItemDamage()) {
                                continue;
                            }
                            newPickAxe = itemStack;
                        }
                    }
                    else {
                        newPickAxe = itemStack;
                    }
                }
                else {
                    if (!(pickAxe.getItem() instanceof ItemPickaxe)) {
                        continue;
                    }
                    if (newPickAxe != null) {
                        if (this.getToolSpeed(itemStack) > this.getToolSpeed(newPickAxe)) {
                            newPickAxe = itemStack;
                        }
                        else {
                            if (this.getToolSpeed(itemStack) != this.getToolSpeed(newPickAxe) || itemStack.getItemDamage() >= newPickAxe.getItemDamage()) {
                                continue;
                            }
                            newPickAxe = itemStack;
                        }
                    }
                    else if (this.getToolSpeed(itemStack) > this.getToolSpeed(pickAxe)) {
                        newPickAxe = itemStack;
                    }
                    else {
                        if (this.getToolSpeed(itemStack) != this.getToolSpeed(pickAxe) || itemStack.getItemDamage() >= pickAxe.getItemDamage()) {
                            continue;
                        }
                        newPickAxe = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator4 = shovels.iterator();
            while (iterator4.hasNext()) {
                final ItemStack itemStack = iterator4.next();
                if (shovel == null) {
                    if (newShovel != null) {
                        if (this.getToolSpeed(itemStack) > this.getToolSpeed(newShovel)) {
                            newShovel = itemStack;
                        }
                        else {
                            if (this.getToolSpeed(itemStack) != this.getToolSpeed(newShovel) || itemStack.getItemDamage() >= newShovel.getItemDamage()) {
                                continue;
                            }
                            newShovel = itemStack;
                        }
                    }
                    else {
                        newShovel = itemStack;
                    }
                }
                else {
                    if (!(shovel.getItem() instanceof ItemSpade)) {
                        continue;
                    }
                    if (newShovel != null) {
                        if (this.getToolSpeed(itemStack) > this.getToolSpeed(newShovel)) {
                            newShovel = itemStack;
                        }
                        else {
                            if (this.getToolSpeed(itemStack) != this.getToolSpeed(newShovel) || itemStack.getItemDamage() >= newShovel.getItemDamage()) {
                                continue;
                            }
                            newShovel = itemStack;
                        }
                    }
                    else if (this.getToolSpeed(itemStack) > this.getToolSpeed(shovel)) {
                        newShovel = itemStack;
                    }
                    else {
                        if (this.getToolSpeed(itemStack) != this.getToolSpeed(shovel) || itemStack.getItemDamage() >= shovel.getItemDamage()) {
                            continue;
                        }
                        newShovel = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator5 = bows.iterator();
            while (iterator5.hasNext()) {
                final ItemStack itemStack = iterator5.next();
                if (itemStack.getSlotID() != bowID + 36) {}
                if (bow == null) {
                    if (newBow != null) {
                        if (itemStack.getItemDamage() >= newBow.getItemDamage()) {
                            continue;
                        }
                        newBow = itemStack;
                    }
                    else {
                        newBow = itemStack;
                    }
                }
                else {
                    if (!(bow.getItem() instanceof ItemBow)) {
                        continue;
                    }
                    if (newBow != null) {
                        if (itemStack.getItemDamage() >= newBow.getItemDamage()) {
                            continue;
                        }
                        newBow = itemStack;
                    }
                    else {
                        if (itemStack.getItemDamage() >= bow.getItemDamage()) {
                            continue;
                        }
                        newBow = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator6 = rods.iterator();
            while (iterator6.hasNext()) {
                final ItemStack itemStack = iterator6.next();
                if (itemStack.getSlotID() != rodID + 36) {
                    if (rod == null) {
                        if (newRod != null) {
                            if (itemStack.getItemDamage() >= newRod.getItemDamage()) {
                                continue;
                            }
                            newRod = itemStack;
                        }
                        else {
                            newRod = itemStack;
                        }
                    }
                    else {
                        if (!(rod.getItem() instanceof ItemFishingRod)) {
                            continue;
                        }
                        if (newRod != null) {
                            if (itemStack.getItemDamage() >= newRod.getItemDamage()) {
                                continue;
                            }
                            newRod = itemStack;
                        }
                        else {
                            if (itemStack.getItemDamage() >= rod.getItemDamage()) {
                                continue;
                            }
                            newRod = itemStack;
                        }
                    }
                }
            }
            final Iterator<ItemStack> iterator7 = foods.iterator();
            while (iterator7.hasNext()) {
                final ItemStack itemStack = iterator7.next();
                if (itemStack.getSlotID() != foodID + 36) {
                    if (food == null) {
                        if (newFood != null) {
                            if (itemStack.stackSize <= newFood.stackSize) {
                                continue;
                            }
                            newFood = itemStack;
                        }
                        else {
                            newFood = itemStack;
                        }
                    }
                    else {
                        if (!(food.getItem() instanceof ItemFood)) {
                            continue;
                        }
                        if (newFood != null) {
                            if (itemStack.stackSize <= newFood.stackSize) {
                                continue;
                            }
                            newFood = itemStack;
                        }
                        else {
                            if (itemStack.stackSize <= food.stackSize) {
                                continue;
                            }
                            newFood = itemStack;
                        }
                    }
                }
            }
            final Iterator<ItemStack> iterator8 = gapples.iterator();
            while (iterator8.hasNext()) {
                final ItemStack itemStack = iterator8.next();
                if (itemStack.getSlotID() != gappleID + 36) {
                    if (gapple == null) {
                        if (newGapple != null) {
                            if (itemStack.stackSize <= newGapple.stackSize) {
                                continue;
                            }
                            newGapple = itemStack;
                        }
                        else {
                            newGapple = itemStack;
                        }
                    }
                    else {
                        if (!(gapple.getItem() instanceof ItemFood)) {
                            continue;
                        }
                        if (newGapple != null) {
                            if (itemStack.stackSize <= newGapple.stackSize) {
                                continue;
                            }
                            newGapple = itemStack;
                        }
                        else {
                            if (itemStack.stackSize <= gapple.stackSize) {
                                continue;
                            }
                            newGapple = itemStack;
                        }
                    }
                }
            }
            if (newSword != null) {
                allToKeep.add(newSword.getSlotID());
            }
            if (sword != null) {
                allToKeep.add(sword.getSlotID());
            }
            if (newBow != null) {
                allToKeep.add(newBow.getSlotID());
            }
            if (bow != null) {
                allToKeep.add(bow.getSlotID());
            }
            if (newRod != null) {
                allToKeep.add(newRod.getSlotID());
            }
            if (rod != null) {
                allToKeep.add(rod.getSlotID());
            }
            if (newFood != null) {
                allToKeep.add(newFood.getSlotID());
            }
            if (food != null) {
                allToKeep.add(food.getSlotID());
            }
            if (newGapple != null) {
                allToKeep.add(newGapple.getSlotID());
            }
            if (gapple != null) {
                allToKeep.add(gapple.getSlotID());
            }
            if (axe != null) {
                allToKeep.add(axe.getSlotID());
            }
            if (newAxe != null) {
                allToKeep.add(newAxe.getSlotID());
            }
            if (pickAxe != null) {
                allToKeep.add(pickAxe.getSlotID());
            }
            if (newPickAxe != null) {
                allToKeep.add(newPickAxe.getSlotID());
            }
            if (shovel != null) {
                allToKeep.add(shovel.getSlotID());
            }
            if (newShovel != null) {
                allToKeep.add(newShovel.getSlotID());
            }
            if (this.timeHelper2.reached(startDelay)) {
                if (this.timeHelper.reached(delay) && this.sort.getBoolean()) {
                    if (newSword != null) {
                        this.switchItems(newSword, swordID);
                        this.timeHelper.reset();
                        if (this.delay.getValue() != 0.0) {
                            return;
                        }
                    }
                    if (sword == null && axe == null && newAxe != null) {
                        this.switchItems(newAxe, swordID);
                        this.timeHelper.reset();
                        if (this.delay.getValue() != 0.0) {
                            return;
                        }
                    }
                    if (newBow != null) {
                        this.switchItems(newBow, bowID);
                        this.timeHelper.reset();
                        if (this.delay.getValue() != 0.0) {
                            return;
                        }
                    }
                    if (newRod != null) {
                        this.switchItems(newRod, rodID);
                        this.timeHelper.reset();
                        if (this.delay.getValue() != 0.0) {
                            return;
                        }
                    }
                    if (newFood != null) {
                        this.switchItems(newFood, foodID);
                        this.timeHelper.reset();
                        if (this.delay.getValue() != 0.0) {
                            return;
                        }
                    }
                    if (newGapple != null) {
                        this.switchItems(newGapple, gappleID);
                        this.timeHelper.reset();
                        if (this.delay.getValue() != 0.0) {
                            return;
                        }
                    }
                }
                if (InventoryCleaner.mm.autoArmor.isToggled()) {
                    InventoryCleaner.mm.autoArmor.newAutoArmor(false);
                }
                if (this.timeHelper.reached(delay) && InventoryCleaner.mm.autoArmor.isInvManager()) {
                    for (int i = 9; i < InventoryCleaner.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                        final ItemStack itemStack = InventoryCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (itemStack != null) {
                            boolean notDrop = false;
                            for (int ii = 0; ii < allToKeep.size(); ++ii) {
                                final int slot = allToKeep.get(ii);
                                if (i == slot) {
                                    notDrop = true;
                                    break;
                                }
                            }
                            if (!notDrop) {
                                this.dropItems(i);
                                this.timeHelper.reset();
                                if (this.delay.getValue() != 0.0) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            this.timeHelper2.reset();
        }
        this.closeInv();
    }
    
    private void closeInv() {
        if (InventoryCleaner.mc.thePlayer.getServerInv() != null && this.mode.getSelected().equals("SpoofInv") && InventoryCleaner.mc.currentScreen == null) {
            InventoryCleaner.mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(InventoryCleaner.mc.thePlayer.inventoryContainer.windowId));
            InventoryCleaner.mc.thePlayer.setServerInv(null);
            System.out.println("CloseInv");
        }
    }
    
    private void openInv() {
        if (this.mode.getSelected().equals("SpoofInv") && InventoryCleaner.mc.thePlayer.getServerInv() == null && InventoryCleaner.mc.currentScreen == null) {
            InventoryCleaner.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            InventoryCleaner.mc.thePlayer.setServerInv(new GuiInventory(InventoryCleaner.mc.thePlayer));
            System.out.println("OpenInv");
        }
    }
    
    private boolean itemsToKeep(final ItemStack itemStack) {
        final Item i = itemStack.getItem();
        return i.equals(Item.getItemById(288)) || i.equals(Item.getItemById(289));
    }
    
    private boolean itemsToDrop(final ItemStack itemStack) {
        final Item i = itemStack.getItem();
        for (int c = 183; c < 192; ++c) {
            if (i.equals(Item.getItemById(c))) {
                return false;
            }
        }
        for (int c = 290; c < 295; ++c) {
            if (i.equals(Item.getItemById(c))) {
                return false;
            }
        }
        if (i.equals(Item.getItemById(39)) || i.equals(Item.getItemById(40))) {
            return InventoryCleaner.mm.autoSoup.isToggled();
        }
        return !i.equals(Item.getItemById(288)) && !i.equals(Item.getItemById(289)) && !i.equals(Item.getItemById(289)) && !i.equals(Item.getByNameOrId("tallgrass")) && !i.equals(Item.getByNameOrId("deadbush")) && !i.equals(Item.getByNameOrId("red_flower")) && !i.equals(Item.getItemById(53)) && !i.equals(Item.getItemById(65)) && !i.equals(Item.getItemById(66)) && !i.equals(Item.getItemById(67)) && !i.equals(Item.getItemById(70)) && !i.equals(Item.getItemById(72)) && !i.equals(Item.getItemById(77)) && !i.equals(Item.getItemById(85)) && !i.equals(Item.getItemById(81)) && !i.equals(Item.getItemById(96)) && !i.equals(Item.getItemById(101)) && !i.equals(Item.getItemById(102)) && !i.equals(Item.getItemById(106)) && !i.equals(Item.getItemById(107)) && !i.equals(Item.getItemById(108)) && !i.equals(Item.getItemById(109)) && !i.equals(Item.getItemById(111)) && !i.equals(Item.getItemById(113)) && !i.equals(Item.getItemById(114)) && !i.equals(Item.getItemById(128)) && !i.equals(Item.getItemById(131)) && !i.equals(Item.getItemById(134)) && !i.equals(Item.getItemById(135)) && !i.equals(Item.getItemById(136)) && !i.equals(Item.getItemById(143)) && !i.equals(Item.getItemById(136)) && !i.equals(Item.getItemById(147)) && !i.equals(Item.getItemById(148)) && !i.equals(Item.getItemById(151)) && !i.equals(Item.getItemById(154)) && !i.equals(Item.getItemById(156)) && !i.equals(Item.getItemById(157)) && !i.equals(Item.getItemById(163)) && !i.equals(Item.getItemById(164)) && !i.equals(Item.getItemById(167)) && !i.equals(Item.getItemById(180)) && !i.equals(Item.getItemById(287)) && !i.equals(Item.getItemById(287)) && !i.equals(Item.getItemById(318)) && !i.equals(Item.getItemById(321)) && !i.equals(Item.getItemById(337)) && !i.equals(Item.getItemById(338)) && !i.equals(Item.getItemById(348)) && !i.equals(Item.getItemById(352)) && !i.equals(Item.getItemById(353)) && !i.equals(Item.getItemById(354)) && !i.equals(Item.getItemById(356)) && !i.equals(Item.getItemById(361)) && !i.equals(Item.getItemById(362)) && !i.equals(Item.getItemById(367)) && !i.equals(Item.getItemById(370)) && !i.equals(Item.getItemById(371)) && !i.equals(Item.getItemById(372)) && !i.equals(Item.getItemById(373)) && !i.equals(Item.getItemById(334)) && !i.equals(Item.getByNameOrId("stone_slab")) && !i.equals(Item.getByNameOrId("snow_layer")) && !i.equals(Item.getByNameOrId("wooden_slab")) && !i.equals(Item.getByNameOrId("cobblestone_wall")) && !i.equals(Item.getByNameOrId("anvil")) && !i.equals(Item.getByNameOrId("stained_glass_pane")) && !i.equals(Item.getByNameOrId("carpet")) && !i.equals(Item.getByNameOrId("double_plant")) && !i.equals(Item.getByNameOrId("stone_slab2")) && !i.equals(Item.getByNameOrId("sapling"));
    }
    
    private void switchItems(final ItemStack itemStack, final int hotBarSlot) {
        this.openInv();
        InventoryCleaner.mc.playerController.windowClick(InventoryCleaner.mc.thePlayer.inventoryContainer.windowId, itemStack.getSlotID(), hotBarSlot, 2, InventoryCleaner.mc.thePlayer);
    }
    
    private void dropItems(final int slotID) {
        this.openInv();
        InventoryCleaner.mc.playerController.windowClick(InventoryCleaner.mc.thePlayer.inventoryContainer.windowId, slotID, 1, 4, InventoryCleaner.mc.thePlayer);
    }
    
    private double getDamageSword(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack.getItem() instanceof ItemSword) {
            damage += ((ItemSword)itemStack.getItem()).getAttackDamage() + EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
        }
        return damage;
    }
    
    private double getToolDamage(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack.getItem() instanceof ItemAxe) {
            damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.wood, MapColor.woodColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack) / 11.0;
        }
        else if (itemStack.getItem() instanceof ItemPickaxe) {
            damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.rock, MapColor.stoneColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack) / 11.0;
        }
        else if (itemStack.getItem() instanceof ItemSpade) {
            damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.sand, MapColor.sandColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack) / 11.0;
        }
        damage += itemStack.getItem().getMaxDamage() + EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) / 11.0;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, itemStack) / 11.0;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
        return damage;
    }
    
    private double getToolSpeed(final ItemStack itemStack) {
        double damage = 0.0;
        if (itemStack.getItem() instanceof ItemTool) {
            if (itemStack.getItem() instanceof ItemAxe) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.wood, MapColor.woodColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            else if (itemStack.getItem() instanceof ItemPickaxe) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.rock, MapColor.stoneColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            else if (itemStack.getItem() instanceof ItemSpade) {
                damage += itemStack.getItem().getStrVsBlock(itemStack, new Block(Material.sand, MapColor.sandColor)) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            }
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemStack) / 11.0;
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) / 33.0;
        }
        return damage;
    }
}
