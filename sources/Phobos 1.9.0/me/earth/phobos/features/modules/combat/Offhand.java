package me.earth.phobos.features.modules.combat;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.ProcessRightClickBlockEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.ServerModule;
import me.earth.phobos.features.setting.Bind;
import me.earth.phobos.features.setting.EnumConverter;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.mixin.mixins.accessors.IContainer;
import me.earth.phobos.mixin.mixins.accessors.ISPacketSetSlot;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Offhand
        extends Module {
    private static Offhand instance;
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final Timer timer = new Timer();
    private final Timer secondTimer = new Timer();
    private final Timer serverTimer = new Timer();
    public Setting<Type> type = this.register(new Setting<Type>("Mode", Type.NEW));
    public Setting<Boolean> cycle = this.register(new Setting<Object>("Cycle", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Bind> cycleKey = this.register(new Setting<Object>("Key", new Bind(-1), v -> this.cycle.getValue() != false && this.type.getValue() == Type.OLD));
    public Setting<Bind> offHandGapple = this.register(new Setting<Bind>("Gapple", new Bind(-1)));
    public Setting<Float> gappleHealth = this.register(new Setting<Float>("G-Health", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> gappleHoleHealth = this.register(new Setting<Float>("G-H-Health", Float.valueOf(3.5f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Bind> offHandCrystal = this.register(new Setting<Bind>("Crystal", new Bind(-1)));
    public Setting<Float> crystalHealth = this.register(new Setting<Float>("C-Health", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> crystalHoleHealth = this.register(new Setting<Float>("C-H-Health", Float.valueOf(3.5f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> cTargetDistance = this.register(new Setting<Float>("C-Distance", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(20.0f)));
    public Setting<Bind> obsidian = this.register(new Setting<Bind>("Obsidian", new Bind(-1)));
    public Setting<Float> obsidianHealth = this.register(new Setting<Float>("O-Health", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> obsidianHoleHealth = this.register(new Setting<Float>("O-H-Health", Float.valueOf(8.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Bind> webBind = this.register(new Setting<Bind>("Webs", new Bind(-1)));
    public Setting<Float> webHealth = this.register(new Setting<Float>("W-Health", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> webHoleHealth = this.register(new Setting<Float>("W-H-Health", Float.valueOf(8.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Boolean> holeCheck = this.register(new Setting<Boolean>("Hole-Check", true));
    public Setting<Boolean> crystalCheck = this.register(new Setting<Boolean>("Crystal-Check", false));
    public Setting<Boolean> gapSwap = this.register(new Setting<Boolean>("Gap-Swap", true));
    public Setting<Integer> updates = this.register(new Setting<Integer>("Updates", 1, 1, 2));
    public Setting<Boolean> cycleObby = this.register(new Setting<Object>("CycleObby", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> cycleWebs = this.register(new Setting<Object>("CycleWebs", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> crystalToTotem = this.register(new Setting<Object>("Crystal-Totem", Boolean.valueOf(true), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> absorption = this.register(new Setting<Object>("Absorption", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> autoGapple = this.register(new Setting<Object>("AutoGapple", Boolean.valueOf(false), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> onlyWTotem = this.register(new Setting<Object>("OnlyWTotem", Boolean.valueOf(true), v -> this.autoGapple.getValue() != false && this.type.getValue() == Type.OLD));
    public Setting<Boolean> unDrawTotem = this.register(new Setting<Object>("DrawTotems", Boolean.valueOf(true), v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> noOffhandGC = this.register(new Setting<Boolean>("NoOGC", false));
    public Setting<Boolean> retardOGC = this.register(new Setting<Boolean>("RetardOGC", false));
    public Setting<Boolean> returnToCrystal = this.register(new Setting<Boolean>("RecoverySwitch", false));
    public Setting<Integer> timeout = this.register(new Setting<Integer>("Timeout", 50, 0, 500));
    public Setting<Integer> timeout2 = this.register(new Setting<Integer>("Timeout2", 50, 0, 500));
    public Setting<Integer> actions = this.register(new Setting<Object>("Actions", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(4), v -> this.type.getValue() == Type.OLD));
    public Setting<NameMode> displayNameChange = this.register(new Setting<Object>("Name", NameMode.TOTEM, v -> this.type.getValue() == Type.OLD));
    public Setting<Boolean> guis = this.register(new Setting<Boolean>("Guis", false));
    public Setting<Integer> serverTimeOut = this.register(new Setting<Integer>("S-Timeout", 1000, 0, 5000));
    public Setting<Boolean> bedcheck = this.register(new Setting<Boolean>("BedCheck", false));
    public Mode mode = Mode.CRYSTALS;
    public Mode oldMode = Mode.CRYSTALS;
    public Mode2 currentMode = Mode2.TOTEMS;
    public int totems = 0;
    public int crystals = 0;
    public int gapples = 0;
    public int obby = 0;
    public int webs = 0;
    public int lastTotemSlot = -1;
    public int lastGappleSlot = -1;
    public int lastCrystalSlot = -1;
    public int lastObbySlot = -1;
    public int lastWebSlot = -1;
    public boolean holdingCrystal = false;
    public boolean holdingTotem = false;
    public boolean holdingGapple = false;
    public boolean holdingObby = false;
    public boolean holdingWeb = false;
    public boolean didSwitchThisTick = false;
    private int oldSlot = -1;
    private boolean swapToTotem = false;
    private boolean eatingApple = false;
    private boolean oldSwapToTotem = false;
    private boolean autoGappleSwitch = false;
    private boolean second = false;
    private boolean switchedForHealthReason = false;

    public Offhand() {
        super("Offhand", "Allows you to switch up your Offhand.", Module.Category.COMBAT, true, false, false);
        instance = this;
    }

    public static Offhand getInstance() {
        if (instance == null) {
            instance = new Offhand();
        }
        return instance;
    }

    public void onItemFinish(ItemStack stack, EntityLivingBase base) {
        if (this.noOffhandGC.getValue().booleanValue() && base.equals(Offhand.mc.player) && stack.getItem() == Offhand.mc.player.getHeldItemOffhand().getItem()) {
            this.secondTimer.reset();
            this.second = true;
        }
    }

    @Override
    public void onTick() {
        if (Offhand.nullCheck() || this.updates.getValue() == 1) {
            return;
        }
        this.doOffhand();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(ProcessRightClickBlockEvent event) {
        if (this.noOffhandGC.getValue().booleanValue() && event.hand == EnumHand.MAIN_HAND && event.stack.getItem() == Items.END_CRYSTAL && Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhand.mc.objectMouseOver != null && event.pos == Offhand.mc.objectMouseOver.getBlockPos()) {
            event.setCanceled(true);
            Offhand.mc.player.setActiveHand(EnumHand.OFF_HAND);
            Offhand.mc.playerController.processRightClick(Offhand.mc.player, Offhand.mc.world, EnumHand.OFF_HAND);
        }
    }

    @Override
    public void onUpdate() {
        if (this.noOffhandGC.getValue().booleanValue() && this.retardOGC.getValue().booleanValue()) {
            if (this.timer.passedMs(this.timeout.getValue().intValue())) {
                if (Offhand.mc.player != null && Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhand.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && Mouse.isButtonDown(1)) {
                    Offhand.mc.player.setActiveHand(EnumHand.OFF_HAND);
                    Offhand.mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
                }
            } else if (Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhand.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                Offhand.mc.gameSettings.keyBindUseItem.pressed = false;
            }
        }
        if (Offhand.nullCheck() || this.updates.getValue() == 2) {
            return;
        }
        this.doOffhand();
        if (this.secondTimer.passedMs(this.timeout2.getValue().intValue()) && this.second) {
            this.second = false;
            this.timer.reset();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (this.type.getValue() == Type.NEW) {
                if (this.offHandCrystal.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.CRYSTALS) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    } else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.CRYSTALS);
                }
                if (this.offHandGapple.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.GAPPLES) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    } else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.GAPPLES);
                }
                if (this.obsidian.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.OBSIDIAN) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    } else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.OBSIDIAN);
                }
                if (this.webBind.getValue().getKey() == Keyboard.getEventKey()) {
                    if (this.mode == Mode.WEBS) {
                        this.setSwapToTotem(!this.isSwapToTotem());
                    } else {
                        this.setSwapToTotem(false);
                    }
                    this.setMode(Mode.WEBS);
                }
            } else if (this.cycle.getValue().booleanValue()) {
                if (this.cycleKey.getValue().getKey() == Keyboard.getEventKey()) {
                    Mode2 newMode = (Mode2) EnumConverter.increaseEnum(this.currentMode);
                    if (newMode == Mode2.OBSIDIAN && !this.cycleObby.getValue().booleanValue() || newMode == Mode2.WEBS && !this.cycleWebs.getValue().booleanValue()) {
                        newMode = Mode2.TOTEMS;
                    }
                    this.setMode(newMode);
                }
            } else {
                if (this.offHandCrystal.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.CRYSTALS);
                }
                if (this.offHandGapple.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.GAPPLES);
                }
                if (this.obsidian.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.OBSIDIAN);
                }
                if (this.webBind.getValue().getKey() == Keyboard.getEventKey()) {
                    this.setMode(Mode2.WEBS);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (this.noOffhandGC.getValue().booleanValue() && !Offhand.fullNullCheck() && Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhand.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            CPacketPlayerTryUseItem packet;
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock packet2 = event.getPacket();
                if (packet2.getHand() == EnumHand.MAIN_HAND && !AutoCrystal.placedPos.contains(packet2.getPos())) {
                    if (this.timer.passedMs(this.timeout.getValue().intValue())) {
                        Offhand.mc.player.setActiveHand(EnumHand.OFF_HAND);
                        Offhand.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                    }
                    event.setCanceled(true);
                }
            } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet = event.getPacket()).getHand() == EnumHand.OFF_HAND && !this.timer.passedMs(this.timeout.getValue().intValue())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSetSlot packet;
        if (ServerModule.getInstance().isConnected() && event.getPacket() instanceof SPacketSetSlot && (packet = event.getPacket()).getSlot() == -1 && packet.getWindowId() != -1) {
            ((IContainer) Offhand.mc.player.openContainer).setTransactionID((short) packet.getWindowId());
            ((ISPacketSetSlot) packet).setWindowId(-1);
            this.serverTimer.reset();
            this.switchedForHealthReason = true;
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.type.getValue() == Type.NEW) {
            return String.valueOf(this.getStackSize());
        }
        switch (this.displayNameChange.getValue()) {
            case MODE: {
                return EnumConverter.getProperName(this.currentMode);
            }
            case TOTEM: {
                if (this.currentMode == Mode2.TOTEMS) {
                    return this.totems + "";
                }
                return EnumConverter.getProperName(this.currentMode);
            }
        }
        switch (this.currentMode) {
            case TOTEMS: {
                return this.totems + "";
            }
            case GAPPLES: {
                return this.gapples + "";
            }
        }
        return this.crystals + "";
    }

    @Override
    public String getDisplayName() {
        if (this.type.getValue() == Type.NEW) {
            if (!this.shouldTotem()) {
                switch (this.mode) {
                    case GAPPLES: {
                        return "OffhandGapple";
                    }
                    case WEBS: {
                        return "OffhandWebs";
                    }
                    case OBSIDIAN: {
                        return "OffhandObby";
                    }
                }
                return "OffhandCrystal";
            }
            return "AutoTotem" + (!this.isSwapToTotem() ? "-" + this.getModeStr() : "");
        }
        switch (this.displayNameChange.getValue()) {
            case MODE: {
                return this.displayName.getValue();
            }
            case TOTEM: {
                if (this.currentMode == Mode2.TOTEMS) {
                    return "AutoTotem";
                }
                return this.displayName.getValue();
            }
        }
        switch (this.currentMode) {
            case TOTEMS: {
                return "AutoTotem";
            }
            case GAPPLES: {
                return "OffhandGapple";
            }
            case WEBS: {
                return "OffhandWebs";
            }
            case OBSIDIAN: {
                return "OffhandObby";
            }
        }
        return "OffhandCrystal";
    }

    public void doOffhand() {
        if (!this.serverTimer.passedMs(this.serverTimeOut.getValue().intValue())) {
            return;
        }
        if (this.type.getValue() == Type.NEW) {
            if (Offhand.mc.currentScreen instanceof GuiContainer && !this.guis.getValue().booleanValue() && !(Offhand.mc.currentScreen instanceof GuiInventory)) {
                return;
            }
            if (this.gapSwap.getValue().booleanValue()) {
                if ((this.getSlot(Mode.GAPPLES) != -1 || Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) && Offhand.mc.player.getHeldItemMainhand().getItem() != Items.GOLDEN_APPLE && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    this.setMode(Mode.GAPPLES);
                    this.eatingApple = true;
                    this.swapToTotem = false;
                } else if (this.eatingApple) {
                    this.setMode(this.oldMode);
                    this.swapToTotem = this.oldSwapToTotem;
                    this.eatingApple = false;
                } else {
                    this.oldMode = this.mode;
                    this.oldSwapToTotem = this.swapToTotem;
                }
            }
            if (!this.shouldTotem()) {
                if (Offhand.mc.player.getHeldItemOffhand() == ItemStack.EMPTY || !this.isItemInOffhand()) {
                    int slot;
                    int n = slot = this.getSlot(this.mode) < 9 ? this.getSlot(this.mode) + 36 : this.getSlot(this.mode);
                    if (this.getSlot(this.mode) != -1) {
                        if (this.oldSlot != -1) {
                            Offhand.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Offhand.mc.player);
                            Offhand.mc.playerController.windowClick(0, this.oldSlot, 0, ClickType.PICKUP, Offhand.mc.player);
                        }
                        this.oldSlot = slot;
                        Offhand.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Offhand.mc.player);
                        Offhand.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Offhand.mc.player);
                        Offhand.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Offhand.mc.player);
                    }
                }
            } else if (!(this.eatingApple || Offhand.mc.player.getHeldItemOffhand() != ItemStack.EMPTY && Offhand.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)) {
                int slot;
                int n = slot = this.getTotemSlot() < 9 ? this.getTotemSlot() + 36 : this.getTotemSlot();
                if (this.getTotemSlot() != -1) {
                    Offhand.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Offhand.mc.player);
                    Offhand.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Offhand.mc.player);
                    Offhand.mc.playerController.windowClick(0, this.oldSlot, 0, ClickType.PICKUP, Offhand.mc.player);
                    this.oldSlot = -1;
                }
            }
        } else {
            if (!this.unDrawTotem.getValue().booleanValue()) {
                this.manageDrawn();
            }
            this.didSwitchThisTick = false;
            this.holdingCrystal = Offhand.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
            this.holdingTotem = Offhand.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
            this.holdingGapple = Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE;
            this.holdingObby = InventoryUtil.isBlock(Offhand.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
            this.holdingWeb = InventoryUtil.isBlock(Offhand.mc.player.getHeldItemOffhand().getItem(), BlockWeb.class);
            this.totems = Offhand.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
            if (this.holdingTotem) {
                this.totems += Offhand.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
            }
            this.crystals = Offhand.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
            if (this.holdingCrystal) {
                this.crystals += Offhand.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
            }
            this.gapples = Offhand.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
            if (this.holdingGapple) {
                this.gapples += Offhand.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
            }
            if (this.currentMode == Mode2.WEBS || this.currentMode == Mode2.OBSIDIAN) {
                this.obby = Offhand.mc.player.inventory.mainInventory.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.getItem(), BlockObsidian.class)).mapToInt(ItemStack::getCount).sum();
                if (this.holdingObby) {
                    this.obby += Offhand.mc.player.inventory.offHandInventory.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.getItem(), BlockObsidian.class)).mapToInt(ItemStack::getCount).sum();
                }
                this.webs = Offhand.mc.player.inventory.mainInventory.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.getItem(), BlockWeb.class)).mapToInt(ItemStack::getCount).sum();
                if (this.holdingWeb) {
                    this.webs += Offhand.mc.player.inventory.offHandInventory.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.getItem(), BlockWeb.class)).mapToInt(ItemStack::getCount).sum();
                }
            }
            this.doSwitch();
        }
    }

    private void manageDrawn() {
        if (this.currentMode == Mode2.TOTEMS && this.drawn.getValue().booleanValue()) {
            this.drawn.setValue(false);
        }
        if (this.currentMode != Mode2.TOTEMS && !this.drawn.getValue().booleanValue()) {
            this.drawn.setValue(true);
        }
    }

    public void doSwitch() {
        if (this.autoGapple.getValue().booleanValue()) {
            if (Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                if (Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && (!this.onlyWTotem.getValue().booleanValue() || Offhand.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)) {
                    this.setMode(Mode.GAPPLES);
                    this.autoGappleSwitch = true;
                }
            } else if (this.autoGappleSwitch) {
                this.setMode(Mode2.TOTEMS);
                this.autoGappleSwitch = false;
            }
        }
        if (this.currentMode == Mode2.GAPPLES && ((!EntityUtil.isSafe(Offhand.mc.player) || this.bedPlaceable()) && EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) <= this.gappleHealth.getValue().floatValue() || EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) <= this.gappleHoleHealth.getValue().floatValue()) || this.currentMode == Mode2.CRYSTALS && ((!EntityUtil.isSafe(Offhand.mc.player) || this.bedPlaceable()) && EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) <= this.crystalHealth.getValue().floatValue() || EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) <= this.crystalHoleHealth.getValue().floatValue()) || this.currentMode == Mode2.OBSIDIAN && ((!EntityUtil.isSafe(Offhand.mc.player) || this.bedPlaceable()) && EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) <= this.obsidianHealth.getValue().floatValue() || EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) <= this.obsidianHoleHealth.getValue().floatValue()) || this.currentMode == Mode2.WEBS && ((!EntityUtil.isSafe(Offhand.mc.player) || this.bedPlaceable()) && EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) <= this.webHealth.getValue().floatValue() || EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) <= this.webHoleHealth.getValue().floatValue())) {
            if (this.returnToCrystal.getValue().booleanValue() && this.currentMode == Mode2.CRYSTALS) {
                this.switchedForHealthReason = true;
            }
            this.setMode(Mode2.TOTEMS);
        }
        if (this.switchedForHealthReason && (EntityUtil.isSafe(Offhand.mc.player) && !this.bedPlaceable() && EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) > this.crystalHoleHealth.getValue().floatValue() || EntityUtil.getHealth(Offhand.mc.player, this.absorption.getValue()) > this.crystalHealth.getValue().floatValue())) {
            this.setMode(Mode2.CRYSTALS);
            this.switchedForHealthReason = false;
        }
        if (Offhand.mc.currentScreen instanceof GuiContainer && !this.guis.getValue().booleanValue() && !(Offhand.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        Item currentOffhandItem = Offhand.mc.player.getHeldItemOffhand().getItem();
        switch (this.currentMode) {
            case TOTEMS: {
                if (this.totems <= 0 || this.holdingTotem) break;
                this.lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastTotemSlot);
                this.putItemInOffhand(this.lastTotemSlot, lastSlot);
                break;
            }
            case GAPPLES: {
                if (this.gapples <= 0 || this.holdingGapple) break;
                this.lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.GOLDEN_APPLE, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastGappleSlot);
                this.putItemInOffhand(this.lastGappleSlot, lastSlot);
                break;
            }
            case WEBS: {
                if (this.webs <= 0 || this.holdingWeb) break;
                this.lastWebSlot = InventoryUtil.findInventoryBlock(BlockWeb.class, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastWebSlot);
                this.putItemInOffhand(this.lastWebSlot, lastSlot);
                break;
            }
            case OBSIDIAN: {
                if (this.obby <= 0 || this.holdingObby) break;
                this.lastObbySlot = InventoryUtil.findInventoryBlock(BlockObsidian.class, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastObbySlot);
                this.putItemInOffhand(this.lastObbySlot, lastSlot);
                break;
            }
            default: {
                if (this.crystals <= 0 || this.holdingCrystal) break;
                this.lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL, false);
                int lastSlot = this.getLastSlot(currentOffhandItem, this.lastCrystalSlot);
                this.putItemInOffhand(this.lastCrystalSlot, lastSlot);
            }
        }
        for (int i = 0; i < this.actions.getValue(); ++i) {
            InventoryUtil.Task task = this.taskList.poll();
            if (task == null) continue;
            task.run();
            if (!task.isSwitching()) continue;
            this.didSwitchThisTick = true;
        }
    }

    private int getLastSlot(Item item, int slotIn) {
        if (item == Items.END_CRYSTAL) {
            return this.lastCrystalSlot;
        }
        if (item == Items.GOLDEN_APPLE) {
            return this.lastGappleSlot;
        }
        if (item == Items.TOTEM_OF_UNDYING) {
            return this.lastTotemSlot;
        }
        if (InventoryUtil.isBlock(item, BlockObsidian.class)) {
            return this.lastObbySlot;
        }
        if (InventoryUtil.isBlock(item, BlockWeb.class)) {
            return this.lastWebSlot;
        }
        if (item == Items.AIR) {
            return -1;
        }
        return slotIn;
    }

    private void putItemInOffhand(int slotIn, int slotOut) {
        if (slotIn != -1 && this.taskList.isEmpty()) {
            this.taskList.add(new InventoryUtil.Task(slotIn));
            this.taskList.add(new InventoryUtil.Task(45));
            this.taskList.add(new InventoryUtil.Task(slotOut));
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    private boolean noNearbyPlayers() {
        return this.mode == Mode.CRYSTALS && Offhand.mc.world.playerEntities.stream().noneMatch(e -> e != Offhand.mc.player && !Phobos.friendManager.isFriend(e) && Offhand.mc.player.getDistance(e) <= this.cTargetDistance.getValue().floatValue());
    }

    private boolean isItemInOffhand() {
        switch (this.mode) {
            case GAPPLES: {
                return Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE;
            }
            case CRYSTALS: {
                return Offhand.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
            }
            case OBSIDIAN: {
                return Offhand.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock && ((ItemBlock) Offhand.mc.player.getHeldItemOffhand().getItem()).block == Blocks.OBSIDIAN;
            }
            case WEBS: {
                return Offhand.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock && ((ItemBlock) Offhand.mc.player.getHeldItemOffhand().getItem()).block == Blocks.WEB;
            }
        }
        return false;
    }

    private boolean isHeldInMainHand() {
        switch (this.mode) {
            case GAPPLES: {
                return Offhand.mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE;
            }
            case CRYSTALS: {
                return Offhand.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL;
            }
            case OBSIDIAN: {
                return Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && ((ItemBlock) Offhand.mc.player.getHeldItemMainhand().getItem()).block == Blocks.OBSIDIAN;
            }
            case WEBS: {
                return Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && ((ItemBlock) Offhand.mc.player.getHeldItemMainhand().getItem()).block == Blocks.WEB;
            }
        }
        return false;
    }

    private boolean shouldTotem() {
        if (this.isHeldInMainHand() || this.isSwapToTotem()) {
            return true;
        }
        if (this.holeCheck.getValue().booleanValue() && EntityUtil.isInHole(Offhand.mc.player) && !this.bedPlaceable()) {
            return Offhand.mc.player.getHealth() + Offhand.mc.player.getAbsorptionAmount() <= this.getHoleHealth() || Offhand.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || Offhand.mc.player.fallDistance >= 3.0f || this.noNearbyPlayers() || this.crystalCheck.getValue() != false && this.isCrystalsAABBEmpty();
        }
        return Offhand.mc.player.getHealth() + Offhand.mc.player.getAbsorptionAmount() <= this.getHealth() || Offhand.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || Offhand.mc.player.fallDistance >= 3.0f || this.noNearbyPlayers() || this.crystalCheck.getValue() != false && this.isCrystalsAABBEmpty();
    }

    private boolean isNotEmpty(BlockPos pos) {
        return Offhand.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).stream().anyMatch(e -> e instanceof EntityEnderCrystal);
    }

    private float getHealth() {
        switch (this.mode) {
            case CRYSTALS: {
                return this.crystalHealth.getValue().floatValue();
            }
            case GAPPLES: {
                return this.gappleHealth.getValue().floatValue();
            }
            case OBSIDIAN: {
                return this.obsidianHealth.getValue().floatValue();
            }
        }
        return this.webHealth.getValue().floatValue();
    }

    private float getHoleHealth() {
        switch (this.mode) {
            case CRYSTALS: {
                return this.crystalHoleHealth.getValue().floatValue();
            }
            case GAPPLES: {
                return this.gappleHoleHealth.getValue().floatValue();
            }
            case OBSIDIAN: {
                return this.obsidianHoleHealth.getValue().floatValue();
            }
        }
        return this.webHoleHealth.getValue().floatValue();
    }

    private boolean isCrystalsAABBEmpty() {
        return this.isNotEmpty(Offhand.mc.player.getPosition().add(1, 0, 0)) || this.isNotEmpty(Offhand.mc.player.getPosition().add(-1, 0, 0)) || this.isNotEmpty(Offhand.mc.player.getPosition().add(0, 0, 1)) || this.isNotEmpty(Offhand.mc.player.getPosition().add(0, 0, -1)) || this.isNotEmpty(Offhand.mc.player.getPosition());
    }

    int getStackSize() {
        int size = 0;
        if (this.shouldTotem()) {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() != Items.TOTEM_OF_UNDYING) continue;
                size += Offhand.mc.player.inventory.getStackInSlot(i).getCount();
            }
        } else if (this.mode == Mode.OBSIDIAN) {
            for (int i = 45; i > 0; --i) {
                if (!(Offhand.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || ((ItemBlock) Offhand.mc.player.inventory.getStackInSlot(i).getItem()).block != Blocks.OBSIDIAN)
                    continue;
                size += Offhand.mc.player.inventory.getStackInSlot(i).getCount();
            }
        } else if (this.mode == Mode.WEBS) {
            for (int i = 45; i > 0; --i) {
                if (!(Offhand.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || ((ItemBlock) Offhand.mc.player.inventory.getStackInSlot(i).getItem()).block != Blocks.WEB)
                    continue;
                size += Offhand.mc.player.inventory.getStackInSlot(i).getCount();
            }
        } else {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() != (this.mode == Mode.CRYSTALS ? Items.END_CRYSTAL : Items.GOLDEN_APPLE))
                    continue;
                size += Offhand.mc.player.inventory.getStackInSlot(i).getCount();
            }
        }
        return size;
    }

    int getSlot(Mode m) {
        int slot = -1;
        if (m == Mode.OBSIDIAN) {
            for (int i = 45; i > 0; --i) {
                if (!(Offhand.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || ((ItemBlock) Offhand.mc.player.inventory.getStackInSlot(i).getItem()).block != Blocks.OBSIDIAN)
                    continue;
                slot = i;
                break;
            }
        } else if (m == Mode.WEBS) {
            for (int i = 45; i > 0; --i) {
                if (!(Offhand.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || ((ItemBlock) Offhand.mc.player.inventory.getStackInSlot(i).getItem()).block != Blocks.WEB)
                    continue;
                slot = i;
                break;
            }
        } else {
            for (int i = 45; i > 0; --i) {
                if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() != (m == Mode.CRYSTALS ? Items.END_CRYSTAL : Items.GOLDEN_APPLE))
                    continue;
                slot = i;
                break;
            }
        }
        return slot;
    }

    int getTotemSlot() {
        int totemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() != Items.TOTEM_OF_UNDYING) continue;
            totemSlot = i;
            break;
        }
        return totemSlot;
    }

    private String getModeStr() {
        switch (this.mode) {
            case GAPPLES: {
                return "G";
            }
            case WEBS: {
                return "W";
            }
            case OBSIDIAN: {
                return "O";
            }
        }
        return "C";
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setMode(Mode2 mode) {
        this.currentMode = this.currentMode == mode ? Mode2.TOTEMS : (this.cycle.getValue() == false && this.crystalToTotem.getValue() != false && (this.currentMode == Mode2.CRYSTALS || this.currentMode == Mode2.OBSIDIAN || this.currentMode == Mode2.WEBS) && mode == Mode2.GAPPLES ? Mode2.TOTEMS : mode);
    }

    public boolean isSwapToTotem() {
        return this.swapToTotem;
    }

    public void setSwapToTotem(boolean swapToTotem) {
        this.swapToTotem = swapToTotem;
    }

    private boolean bedPlaceable() {
        if (!this.bedcheck.getValue().booleanValue()) {
            return false;
        }
        if (Offhand.mc.world.getBlockState(Offhand.mc.player.getPosition()).getBlock() != Blocks.BED && Offhand.mc.world.getBlockState(Offhand.mc.player.getPosition()).getBlock() != Blocks.AIR) {
            return false;
        }
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing == EnumFacing.UP || facing == EnumFacing.DOWN || Offhand.mc.world.getBlockState(Offhand.mc.player.getPosition().offset(facing)).getBlock() != Blocks.BED && Offhand.mc.world.getBlockState(Offhand.mc.player.getPosition().offset(facing)).getBlock() != Blocks.AIR)
                continue;
            return true;
        }
        return false;
    }

    public enum NameMode {
        MODE,
        TOTEM,
        AMOUNT

    }

    public enum Mode2 {
        TOTEMS,
        GAPPLES,
        CRYSTALS,
        OBSIDIAN,
        WEBS

    }

    public enum Type {
        OLD,
        NEW

    }

    public enum Mode {
        CRYSTALS,
        GAPPLES,
        OBSIDIAN,
        WEBS

    }
}

