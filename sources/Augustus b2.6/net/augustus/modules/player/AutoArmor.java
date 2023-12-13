// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.minecraft.inventory.Slot;
import java.io.IOException;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.augustus.utils.MoveUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import java.util.Iterator;
import net.minecraft.item.ItemArmor;
import net.augustus.utils.RandomUtil;
import net.augustus.events.EventEarlyTick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.utils.BlockUtil;
import org.lwjgl.input.Mouse;
import net.augustus.events.EventClick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class AutoArmor extends Module
{
    private final TimeHelper timeHelper;
    private final TimeHelper timeHelper2;
    private final TimeHelper timeHelper3;
    private final TimeHelper timeHelper4;
    public StringValue mode;
    public BooleanValue noMove;
    public BooleanValue interactionCheck;
    public DoubleValue startDelay;
    public DoubleValue delay;
    public BooleanValue hotbar;
    public BooleanValue gommeQSG;
    public DoubleValue hotbarStartDelay;
    public DoubleValue hotbarDelay;
    private ArrayList<ItemStack> chestPlates;
    private ArrayList<ItemStack> helmets;
    private ArrayList<ItemStack> boots;
    private ArrayList<ItemStack> trousers;
    private boolean blockInv;
    private int oldSlotID;
    private boolean b1;
    private boolean invManager;
    
    public AutoArmor() {
        super("AutoArmor", new Color(23, 123, 208, 255), Categorys.PLAYER);
        this.timeHelper = new TimeHelper();
        this.timeHelper2 = new TimeHelper();
        this.timeHelper3 = new TimeHelper();
        this.timeHelper4 = new TimeHelper();
        this.mode = new StringValue(1, "Mode", this, "OpenInv", new String[] { "OpenInv", "SpoofInv", "Basic" });
        this.noMove = new BooleanValue(8, "NoMove", this, false);
        this.interactionCheck = new BooleanValue(9, "InteractCheck", this, false);
        this.startDelay = new DoubleValue(2, "StartDelay", this, 200.0, 0.0, 1000.0, 0);
        this.delay = new DoubleValue(3, "Delay", this, 90.0, 0.0, 400.0, 0);
        this.hotbar = new BooleanValue(4, "Hotbar", this, false);
        this.gommeQSG = new BooleanValue(5, "GommeQSG", this, false);
        this.hotbarStartDelay = new DoubleValue(6, "HStartDelay", this, 200.0, 0.0, 1000.0, 0);
        this.hotbarDelay = new DoubleValue(7, "HotbarDelay", this, 100.0, 0.0, 400.0, 0);
        this.b1 = true;
        this.invManager = true;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (AutoArmor.mc.thePlayer != null) {
            AutoArmor.mc.thePlayer.setServerInv(AutoArmor.mc.currentScreen);
        }
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        this.blockInv = false;
        if (this.mode.getSelected().equals("SpoofInv") && this.interactionCheck.getBoolean() && (Mouse.isButtonDown(1) || Mouse.isButtonDown(0) || (AutoArmor.mm.killAura.isToggled() && AutoArmor.mm.killAura.target != null) || BlockUtil.isScaffoldToggled())) {
            this.closeInv();
            this.blockInv = true;
            this.timeHelper2.reset();
        }
        if (this.mode.getSelected().equalsIgnoreCase("OpenInv")) {
            this.newAutoArmorHotbar();
        }
    }
    
    @EventTarget
    public void onEventTick(final EventEarlyTick eventEarlyTick) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        if (!AutoArmor.mm.inventoryCleaner.isToggled()) {
            this.newAutoArmor(true);
        }
    }
    
    public void newAutoArmorHotbar() {
        this.chestPlates = new ArrayList<ItemStack>();
        this.helmets = new ArrayList<ItemStack>();
        this.trousers = new ArrayList<ItemStack>();
        this.boots = new ArrayList<ItemStack>();
        final long random2 = RandomUtil.nextLong(-35L, 35L);
        final long random3 = RandomUtil.nextLong(-35L, 35L);
        final long delay2 = (long)(this.hotbarDelay.getValue() + random2);
        final long delay3 = (long)(this.hotbarStartDelay.getValue() + random3);
        ItemStack helm = null;
        ItemStack boot = null;
        ItemStack chestPlate = null;
        ItemStack trouser = null;
        ItemStack newHelm = null;
        ItemStack newBoot = null;
        ItemStack newChestPlate = null;
        ItemStack newTrouser = null;
        if (this.hotbar.getBoolean() && AutoArmor.mc.currentScreen == null) {
            final boolean qsg = this.gommeQSG.getBoolean();
            for (int i = 36; i < AutoArmor.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack itemStack = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                    if (((ItemArmor)itemStack.getItem()).armorType == 0) {
                        itemStack.setSlotID(i);
                        this.helmets.add(itemStack);
                    }
                    else if (((ItemArmor)itemStack.getItem()).armorType == 1) {
                        itemStack.setSlotID(i);
                        this.chestPlates.add(itemStack);
                    }
                    else if (((ItemArmor)itemStack.getItem()).armorType == 2) {
                        itemStack.setSlotID(i);
                        this.trousers.add(itemStack);
                    }
                    else if (((ItemArmor)itemStack.getItem()).armorType == 3) {
                        itemStack.setSlotID(i);
                        this.boots.add(itemStack);
                    }
                }
            }
            for (int i = 0; i < AutoArmor.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack itemStack = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                    if (i == 5) {
                        itemStack.setSlotID(i);
                        helm = itemStack;
                    }
                    if (i == 6) {
                        itemStack.setSlotID(i);
                        chestPlate = itemStack;
                    }
                    if (i == 7) {
                        itemStack.setSlotID(i);
                        trouser = itemStack;
                    }
                    if (i == 8) {
                        itemStack.setSlotID(i);
                        boot = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator = this.helmets.iterator();
            while (iterator.hasNext()) {
                final ItemStack itemStack = iterator.next();
                if (helm == null) {
                    if (newHelm != null) {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(newHelm)) {
                            continue;
                        }
                        newHelm = itemStack;
                    }
                    else {
                        newHelm = itemStack;
                    }
                }
                else {
                    if (!(helm.getItem() instanceof ItemArmor) || !qsg) {
                        continue;
                    }
                    if (newHelm != null) {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(newHelm)) {
                            continue;
                        }
                        newHelm = itemStack;
                    }
                    else {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(helm)) {
                            continue;
                        }
                        newHelm = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator2 = this.chestPlates.iterator();
            while (iterator2.hasNext()) {
                final ItemStack itemStack = iterator2.next();
                if (chestPlate == null) {
                    if (newChestPlate != null) {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(newChestPlate)) {
                            continue;
                        }
                        newChestPlate = itemStack;
                    }
                    else {
                        newChestPlate = itemStack;
                    }
                }
                else {
                    if (!(chestPlate.getItem() instanceof ItemArmor) || !qsg) {
                        continue;
                    }
                    if (newChestPlate != null) {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(newChestPlate)) {
                            continue;
                        }
                        newChestPlate = itemStack;
                    }
                    else {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(chestPlate)) {
                            continue;
                        }
                        newChestPlate = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator3 = this.trousers.iterator();
            while (iterator3.hasNext()) {
                final ItemStack itemStack = iterator3.next();
                if (trouser == null) {
                    if (newTrouser != null) {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(newTrouser)) {
                            continue;
                        }
                        newTrouser = itemStack;
                    }
                    else {
                        newTrouser = itemStack;
                    }
                }
                else {
                    if (!(trouser.getItem() instanceof ItemArmor) || !qsg) {
                        continue;
                    }
                    if (newTrouser != null) {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(newTrouser)) {
                            continue;
                        }
                        newTrouser = itemStack;
                    }
                    else {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(trouser)) {
                            continue;
                        }
                        newTrouser = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator4 = this.boots.iterator();
            while (iterator4.hasNext()) {
                final ItemStack itemStack = iterator4.next();
                if (boot == null) {
                    if (newBoot != null) {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(newBoot)) {
                            continue;
                        }
                        newBoot = itemStack;
                    }
                    else {
                        newBoot = itemStack;
                    }
                }
                else {
                    if (!(boot.getItem() instanceof ItemArmor) || !qsg) {
                        continue;
                    }
                    if (newBoot != null) {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(newBoot)) {
                            continue;
                        }
                        newBoot = itemStack;
                    }
                    else {
                        if (this.getDamageReduceAmount(itemStack) <= this.getDamageReduceAmount(boot)) {
                            continue;
                        }
                        newBoot = itemStack;
                    }
                }
            }
            if (this.timeHelper3.reached(delay3) && this.timeHelper2.reached(delay2)) {
                if (newBoot == null && newTrouser == null && newChestPlate == null && newHelm == null) {
                    this.timeHelper3.reset();
                    if (this.b1) {
                        AutoArmor.mc.thePlayer.inventory.currentItem = this.oldSlotID;
                    }
                    this.oldSlotID = AutoArmor.mc.thePlayer.inventory.currentItem;
                    this.b1 = false;
                }
                else {
                    if (newChestPlate != null) {
                        if (chestPlate == null) {
                            this.rightClick(newChestPlate.getSlotID());
                            this.b1 = true;
                        }
                        else if (qsg) {
                            this.rightClick(newChestPlate.getSlotID());
                            this.b1 = true;
                        }
                        this.timeHelper2.reset();
                        return;
                    }
                    if (newTrouser != null) {
                        if (trouser == null) {
                            this.rightClick(newTrouser.getSlotID());
                            this.b1 = true;
                        }
                        else if (qsg) {
                            this.rightClick(newTrouser.getSlotID());
                            this.b1 = true;
                        }
                        this.timeHelper2.reset();
                        return;
                    }
                    if (newHelm != null) {
                        if (helm == null) {
                            this.rightClick(newHelm.getSlotID());
                            this.b1 = true;
                        }
                        else if (qsg) {
                            this.rightClick(newHelm.getSlotID());
                            this.b1 = true;
                        }
                        this.timeHelper2.reset();
                        return;
                    }
                    if (newBoot != null) {
                        if (boot == null) {
                            this.rightClick(newBoot.getSlotID());
                            this.b1 = true;
                        }
                        else if (qsg) {
                            this.rightClick(newBoot.getSlotID());
                            this.b1 = true;
                        }
                        this.timeHelper2.reset();
                    }
                }
            }
        }
    }
    
    public void newAutoArmor(final boolean startDelay) {
        this.chestPlates = new ArrayList<ItemStack>();
        this.helmets = new ArrayList<ItemStack>();
        this.trousers = new ArrayList<ItemStack>();
        this.boots = new ArrayList<ItemStack>();
        final long random = RandomUtil.nextLong(-25L, 25L);
        final long random2 = RandomUtil.nextLong(-25L, 25L);
        final long random3 = RandomUtil.nextLong(-25L, 25L);
        final long random4 = RandomUtil.nextLong(-25L, 25L);
        final long delay = (long)(this.delay.getValue() + random);
        final long delay2 = (long)(this.hotbarDelay.getValue() + random2);
        long delay3 = 0L;
        final long delay4 = (long)(this.hotbarStartDelay.getValue() + random3);
        if (startDelay) {
            delay3 = (long)(this.startDelay.getValue() + random4);
        }
        ItemStack helm = null;
        ItemStack boot = null;
        ItemStack chestPlate = null;
        ItemStack trouser = null;
        ItemStack newHelm = null;
        ItemStack newBoot = null;
        ItemStack newChestPlate = null;
        ItemStack newTrouser = null;
        final boolean autoArmor = AutoArmor.mc.currentScreen instanceof GuiInventory || (!this.mode.getSelected().equalsIgnoreCase("OpenInv") && (AutoArmor.mc.currentScreen == null || AutoArmor.mc.currentScreen instanceof GuiInventory) && (!this.noMove.getBoolean() || (this.noMove.getBoolean() && !MoveUtil.isMoving())));
        if (autoArmor && !this.blockInv) {
            for (int i = 0; i < AutoArmor.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack itemStack = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                    if (((ItemArmor)itemStack.getItem()).armorType == 0) {
                        itemStack.setSlotID(i);
                        this.helmets.add(itemStack);
                    }
                    else if (((ItemArmor)itemStack.getItem()).armorType == 1) {
                        itemStack.setSlotID(i);
                        this.chestPlates.add(itemStack);
                    }
                    else if (((ItemArmor)itemStack.getItem()).armorType == 2) {
                        itemStack.setSlotID(i);
                        this.trousers.add(itemStack);
                    }
                    else if (((ItemArmor)itemStack.getItem()).armorType == 3) {
                        itemStack.setSlotID(i);
                        this.boots.add(itemStack);
                    }
                    if (itemStack.getSlotID() == 5) {
                        itemStack.setSlotID(i);
                        helm = itemStack;
                    }
                    if (itemStack.getSlotID() == 6) {
                        itemStack.setSlotID(i);
                        chestPlate = itemStack;
                    }
                    if (itemStack.getSlotID() == 7) {
                        itemStack.setSlotID(i);
                        trouser = itemStack;
                    }
                    if (itemStack.getSlotID() == 8) {
                        itemStack.setSlotID(i);
                        boot = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator = this.helmets.iterator();
            while (iterator.hasNext()) {
                final ItemStack itemStack = iterator.next();
                if (helm == null) {
                    if (newHelm != null) {
                        if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(newHelm)) {
                            newHelm = itemStack;
                        }
                        else {
                            if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(newHelm) || itemStack.getItemDamage() >= newHelm.getItemDamage()) {
                                continue;
                            }
                            newHelm = itemStack;
                        }
                    }
                    else {
                        newHelm = itemStack;
                    }
                }
                else {
                    if (!(helm.getItem() instanceof ItemArmor)) {
                        continue;
                    }
                    if (newHelm != null) {
                        if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(newHelm)) {
                            newHelm = itemStack;
                        }
                        else {
                            if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(newHelm) || itemStack.getItemDamage() >= newHelm.getItemDamage()) {
                                continue;
                            }
                            newHelm = itemStack;
                        }
                    }
                    else if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(helm)) {
                        newHelm = itemStack;
                    }
                    else {
                        if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(helm) || itemStack.getItemDamage() >= helm.getItemDamage()) {
                            continue;
                        }
                        newHelm = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator2 = this.chestPlates.iterator();
            while (iterator2.hasNext()) {
                final ItemStack itemStack = iterator2.next();
                if (chestPlate == null) {
                    if (newChestPlate != null) {
                        if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(newChestPlate)) {
                            newChestPlate = itemStack;
                        }
                        else {
                            if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(newChestPlate) || itemStack.getItemDamage() >= newChestPlate.getItemDamage()) {
                                continue;
                            }
                            newChestPlate = itemStack;
                        }
                    }
                    else {
                        newChestPlate = itemStack;
                    }
                }
                else {
                    if (!(chestPlate.getItem() instanceof ItemArmor)) {
                        continue;
                    }
                    if (newChestPlate != null) {
                        if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(newChestPlate)) {
                            newChestPlate = itemStack;
                        }
                        else {
                            if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(newChestPlate) || itemStack.getItemDamage() >= newChestPlate.getItemDamage()) {
                                continue;
                            }
                            newChestPlate = itemStack;
                        }
                    }
                    else if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(chestPlate)) {
                        newChestPlate = itemStack;
                    }
                    else {
                        if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(chestPlate) || itemStack.getItemDamage() >= chestPlate.getItemDamage()) {
                            continue;
                        }
                        newChestPlate = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator3 = this.trousers.iterator();
            while (iterator3.hasNext()) {
                final ItemStack itemStack = iterator3.next();
                if (trouser == null) {
                    if (newTrouser != null) {
                        if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(newTrouser)) {
                            newTrouser = itemStack;
                        }
                        else {
                            if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(newTrouser) || itemStack.getItemDamage() >= newTrouser.getItemDamage()) {
                                continue;
                            }
                            newTrouser = itemStack;
                        }
                    }
                    else {
                        newTrouser = itemStack;
                    }
                }
                else {
                    if (!(trouser.getItem() instanceof ItemArmor)) {
                        continue;
                    }
                    if (newTrouser != null) {
                        if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(newTrouser)) {
                            newTrouser = itemStack;
                        }
                        else {
                            if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(newTrouser) || itemStack.getItemDamage() >= newTrouser.getItemDamage()) {
                                continue;
                            }
                            newTrouser = itemStack;
                        }
                    }
                    else if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(trouser)) {
                        newTrouser = itemStack;
                    }
                    else {
                        if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(trouser) || itemStack.getItemDamage() >= trouser.getItemDamage()) {
                            continue;
                        }
                        newTrouser = itemStack;
                    }
                }
            }
            final Iterator<ItemStack> iterator4 = this.boots.iterator();
            while (iterator4.hasNext()) {
                final ItemStack itemStack = iterator4.next();
                if (boot == null) {
                    if (newBoot != null) {
                        if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(newBoot)) {
                            newBoot = itemStack;
                        }
                        else {
                            if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(newBoot) || itemStack.getItemDamage() <= newBoot.getItemDamage()) {
                                continue;
                            }
                            newBoot = itemStack;
                        }
                    }
                    else {
                        newBoot = itemStack;
                    }
                }
                else {
                    if (!(boot.getItem() instanceof ItemArmor)) {
                        continue;
                    }
                    if (newBoot != null) {
                        if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(newBoot)) {
                            newBoot = itemStack;
                        }
                        else {
                            if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(newBoot) || itemStack.getItemDamage() >= newBoot.getItemDamage()) {
                                continue;
                            }
                            newBoot = itemStack;
                        }
                    }
                    else if (this.getDamageReduceAmount(itemStack) > this.getDamageReduceAmount(boot)) {
                        newBoot = itemStack;
                    }
                    else {
                        if (this.getDamageReduceAmount(itemStack) != this.getDamageReduceAmount(boot) || itemStack.getItemDamage() >= boot.getItemDamage()) {
                            continue;
                        }
                        newBoot = itemStack;
                    }
                }
            }
            if (this.timeHelper4.reached(delay3) && this.timeHelper.reached(delay)) {
                if (newBoot == null && newTrouser == null && newChestPlate == null && newHelm == null) {
                    this.invManager = true;
                }
                if (newChestPlate != null) {
                    if (chestPlate == null) {
                        this.shiftClick(newChestPlate.getSlotID());
                    }
                    else {
                        this.replaceArmor(6);
                    }
                    this.timeHelper.reset();
                    this.invManager = false;
                    if (this.delay.getValue() != 0.0) {
                        return;
                    }
                }
                if (newTrouser != null) {
                    if (trouser == null) {
                        this.shiftClick(newTrouser.getSlotID());
                    }
                    else {
                        this.replaceArmor(7);
                    }
                    this.timeHelper.reset();
                    this.invManager = false;
                    if (this.delay.getValue() != 0.0) {
                        return;
                    }
                }
                if (newHelm != null) {
                    if (helm == null) {
                        this.shiftClick(newHelm.getSlotID());
                    }
                    else {
                        this.replaceArmor(5);
                    }
                    this.timeHelper.reset();
                    this.invManager = false;
                    if (this.delay.getValue() != 0.0) {
                        return;
                    }
                }
                if (newBoot != null) {
                    if (boot == null) {
                        this.shiftClick(newBoot.getSlotID());
                    }
                    else {
                        this.replaceArmor(8);
                    }
                    this.timeHelper.reset();
                    this.invManager = false;
                    if (this.delay.getValue() != 0.0) {
                        return;
                    }
                }
            }
        }
        else {
            this.timeHelper4.reset();
        }
        this.closeInv();
    }
    
    private void closeInv() {
        if (AutoArmor.mc.thePlayer.getServerInv() != null && this.mode.getSelected().equals("SpoofInv") && AutoArmor.mc.currentScreen == null) {
            AutoArmor.mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(AutoArmor.mc.thePlayer.inventoryContainer.windowId));
            AutoArmor.mc.thePlayer.setServerInv(null);
            System.out.println("CloseInv");
        }
    }
    
    private void openInv() {
        if (this.mode.getSelected().equals("SpoofInv") && AutoArmor.mc.thePlayer.getServerInv() == null && AutoArmor.mc.currentScreen == null) {
            AutoArmor.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            AutoArmor.mc.thePlayer.setServerInv(new GuiInventory(AutoArmor.mc.thePlayer));
            System.out.println("OpenInv");
        }
    }
    
    private double getDamageReduceAmount(final ItemStack itemStack) {
        double damageReduceAmount = 0.0;
        if (itemStack.getItem() instanceof ItemArmor) {
            damageReduceAmount += ((ItemArmor)itemStack.getItem()).damageReduceAmount + (6 + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack)) / 3.0f;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack) / 11.0;
            damageReduceAmount += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, itemStack) / 11.0;
            if (((ItemArmor)itemStack.getItem()).armorType == 0 && ((ItemArmor)itemStack.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.GOLD) {
                damageReduceAmount -= 0.01;
            }
        }
        return damageReduceAmount;
    }
    
    private void shiftClick(final int slotID) {
        this.openInv();
        if (this.mode.getSelected().equalsIgnoreCase("OpenInv")) {
            final Slot slot1 = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(slotID);
            try {
                final GuiInventory guiInventory = (GuiInventory)AutoArmor.mc.currentScreen;
                guiInventory.forceShift = true;
                guiInventory.mouseClicked(slot1.xDisplayPosition + 2 + guiInventory.guiLeft, slot1.yDisplayPosition + 2 + guiInventory.guiTop, 0);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, slotID, 0, 1, AutoArmor.mc.thePlayer);
        }
        this.timeHelper.reset();
    }
    
    private void replaceArmor(final int slotID) {
        this.openInv();
        AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, slotID, 1, 4, AutoArmor.mc.thePlayer);
        this.timeHelper.reset();
    }
    
    private void rightClick(final int slotID) {
        this.openInv();
        AutoArmor.mc.thePlayer.inventory.currentItem = slotID - 36;
        AutoArmor.mc.rightClickMouse();
        this.timeHelper.reset();
    }
    
    public boolean isInvManager() {
        return this.invManager;
    }
    
    public void setInvManager(final boolean invManager) {
        this.invManager = invManager;
    }
}
