// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.entity.EntityPlayerSP;
import ru.tuskevich.util.chat.ChatUtility;
import ru.tuskevich.modules.impl.HUD.Notifications;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import ru.tuskevich.util.movement.MovementUtils;
import ru.tuskevich.event.events.impl.EventUpdate;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemElytra;
import ru.tuskevich.util.world.InventoryUtility;
import net.minecraft.init.Items;
import ru.tuskevich.ui.dropui.setting.Setting;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Flight", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class Flight extends Module
{
    public static ModeSetting flightMode;
    public static SliderSetting speed;
    public static SliderSetting motionY;
    TimerUtility timerHelper;
    private boolean abletofly;
    int stage;
    static Minecraft mc;
    
    public Flight() {
        this.timerHelper = new TimerUtility();
        this.add(Flight.flightMode, Flight.speed, Flight.motionY);
    }
    
    @Override
    public void onEnable() {
        this.stage = 0;
        this.abletofly = false;
        if (Flight.flightMode.is("NexusElytra")) {
            if (InventoryUtility.getItemIndex(Items.ELYTRA) == -1) {
                return;
            }
            final Minecraft mc = Flight.mc;
            if (!(Minecraft.player.inventory.getItemStack().getItem() instanceof ItemElytra)) {
                final int elytra = InventoryUtility.getItemIndex(Items.ELYTRA);
                final PlayerControllerMP playerController = Flight.mc.playerController;
                final int windowId = 0;
                final int slotId = (elytra < 9) ? (elytra + 36) : elytra;
                final int mouseButton = 1;
                final ClickType pickup = ClickType.PICKUP;
                final Minecraft mc2 = Flight.mc;
                playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                final PlayerControllerMP playerController2 = Flight.mc.playerController;
                final int windowId2 = 0;
                final int slotId2 = 6;
                final int mouseButton2 = 1;
                final ClickType pickup2 = ClickType.PICKUP;
                final Minecraft mc3 = Flight.mc;
                playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                final PlayerControllerMP playerController3 = Flight.mc.playerController;
                final int windowId3 = 0;
                final int slotId3 = (elytra < 9) ? (elytra + 36) : elytra;
                final int mouseButton3 = 1;
                final ClickType pickup3 = ClickType.PICKUP;
                final Minecraft mc4 = Flight.mc;
                playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
            }
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.stage = 0;
        this.abletofly = false;
        final Minecraft mc = Flight.mc;
        Minecraft.player.capabilities.isFlying = false;
        final Minecraft mc2 = Flight.mc;
        Minecraft.player.capabilities.allowFlying = false;
        if (Flight.flightMode.is("NexusElytra")) {
            if (InventoryUtility.getItemIndex(Items.ELYTRA) == -1) {
                return;
            }
            final int armor = InventoryUtility.getSlotWithArmor();
            final PlayerControllerMP playerController = Flight.mc.playerController;
            final int windowId = 0;
            final int slotId = (armor < 9) ? (armor + 36) : armor;
            final int mouseButton = 1;
            final ClickType pickup = ClickType.PICKUP;
            final Minecraft mc3 = Flight.mc;
            playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
            final PlayerControllerMP playerController2 = Flight.mc.playerController;
            final int windowId2 = 0;
            final int slotId2 = 6;
            final int mouseButton2 = 1;
            final ClickType pickup2 = ClickType.PICKUP;
            final Minecraft mc4 = Flight.mc;
            playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
            final PlayerControllerMP playerController3 = Flight.mc.playerController;
            final int windowId3 = 0;
            final int slotId3 = (armor < 9) ? (armor + 36) : armor;
            final int mouseButton3 = 1;
            final ClickType pickup3 = ClickType.PICKUP;
            final Minecraft mc5 = Flight.mc;
            playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
        }
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (!this.state) {
            return;
        }
        final String options;
        final String mode = options = Flight.flightMode.getOptions();
        int n = -1;
        switch (options.hashCode()) {
            case 68891525: {
                if (options.equals("Glide")) {
                    n = 0;
                    break;
                }
                break;
            }
            case -1721492669: {
                if (options.equals("Vulcan")) {
                    n = 1;
                    break;
                }
                break;
            }
            case -2089914221: {
                if (options.equals("SunFirework")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 1897755483: {
                if (options.equals("Vanilla")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 1162292811: {
                if (options.equals("SunRiseNew")) {
                    n = 4;
                    break;
                }
                break;
            }
            case -2107833104: {
                if (options.equals("Matrix Elytra")) {
                    n = 5;
                    break;
                }
                break;
            }
            case -134524816: {
                if (options.equals("NexusElytra")) {
                    n = 6;
                    break;
                }
                break;
            }
        }
        Label_0569: {
            switch (n) {
                case 0: {
                    MovementUtils.setSpeed(Flight.speed.getFloatValue());
                    if (!Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                        final Minecraft mc = Flight.mc;
                        if (!Minecraft.player.collidedHorizontally) {
                            if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                final Minecraft mc2 = Flight.mc;
                                Minecraft.player.motionY = -0.30000001192092896;
                                break;
                            }
                            final Minecraft mc3 = Flight.mc;
                            final EntityPlayerSP player = Minecraft.player;
                            final Minecraft mc4 = Flight.mc;
                            player.motionY = ((Minecraft.player.ticksExisted % 2 != 0) ? -0.05 : 0.09);
                            break;
                        }
                    }
                    final Minecraft mc5 = Flight.mc;
                    Minecraft.player.motionY = 0.25;
                    break;
                }
                case 1: {
                    final Minecraft mc6 = Flight.mc;
                    if (Minecraft.player.ticksExisted % 2 == 0) {
                        Flight.mc.timer.timerSpeed = 3.5f;
                        final Minecraft mc7 = Flight.mc;
                        Minecraft.player.capabilities.isFlying = true;
                        break Label_0569;
                    }
                    final Minecraft mc8 = Flight.mc;
                    Minecraft.player.capabilities.isFlying = false;
                    Flight.mc.timer.timerSpeed = 1.0f;
                    break Label_0569;
                }
                case 2: {
                    if (this.getElytra() == -1 && this.getFirework() == -1) {
                        break Label_0569;
                    }
                    final Minecraft mc9 = Flight.mc;
                    if (!Minecraft.player.isElytraFlying()) {
                        break Label_0569;
                    }
                    final Minecraft mc10 = Flight.mc;
                    if (Minecraft.player.moveForward <= 0.0f) {
                        break Label_0569;
                    }
                    final Minecraft mc11 = Flight.mc;
                    if (Minecraft.player.ticksExisted % 45 == 0) {
                        final Minecraft mc12 = Flight.mc;
                        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.getFirework()));
                        final Minecraft mc13 = Flight.mc;
                        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        final Minecraft mc14 = Flight.mc;
                        final NetHandlerPlayClient connection = Minecraft.player.connection;
                        final Minecraft mc15 = Flight.mc;
                        connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
                    }
                    break Label_0569;
                }
                case 3: {
                    final Minecraft mc16 = Flight.mc;
                    Minecraft.player.motionY = 0.0;
                    if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                        final Minecraft mc17 = Flight.mc;
                        final EntityPlayerSP player2 = Minecraft.player;
                        ++player2.motionY;
                    }
                    if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        final Minecraft mc18 = Flight.mc;
                        final EntityPlayerSP player3 = Minecraft.player;
                        --player3.motionY;
                    }
                    MoveUtility.setMotion(Flight.speed.getFloatValue());
                    break;
                }
                case 4: {
                    if (++this.stage % 2 != 0) {
                        break;
                    }
                    final Minecraft mc19 = Flight.mc;
                    if (Minecraft.player.onGround) {
                        final Minecraft mc20 = Flight.mc;
                        Minecraft.player.jump();
                        break;
                    }
                    final int elytra = InventoryUtility.getItemIndex(Items.ELYTRA);
                    if (elytra != -2 && this.timerHelper.hasTimeElapsed(350L)) {
                        final PlayerControllerMP playerController = Flight.mc.playerController;
                        final int windowId = 0;
                        final int slotId = 6;
                        final int mouseButton = 1;
                        final ClickType pickup = ClickType.PICKUP;
                        final Minecraft mc21 = Flight.mc;
                        playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                        final PlayerControllerMP playerController2 = Flight.mc.playerController;
                        final int windowId2 = 0;
                        final int slotId2 = elytra;
                        final int mouseButton2 = 1;
                        final ClickType pickup2 = ClickType.PICKUP;
                        final Minecraft mc22 = Flight.mc;
                        playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                        final PlayerControllerMP playerController3 = Flight.mc.playerController;
                        final int windowId3 = 0;
                        final int slotId3 = 6;
                        final int mouseButton3 = 1;
                        final ClickType pickup3 = ClickType.PICKUP;
                        final Minecraft mc23 = Flight.mc;
                        playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
                    }
                    final Minecraft mc24 = Flight.mc;
                    if (Minecraft.player.isAirBorne) {
                        final NetHandlerPlayClient connection2 = Flight.mc.getConnection();
                        final Minecraft mc25 = Flight.mc;
                        connection2.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    final Minecraft mc26 = Flight.mc;
                    if (Minecraft.player.isElytraFlying()) {
                        this.abletofly = true;
                    }
                    Label_0952: {
                        if (this.abletofly) {
                            final Minecraft mc27 = Flight.mc;
                            Minecraft.player.motionY = 9.999999747378752E-6;
                            MoveUtility.setMotion(0.550000011920929);
                            if (!Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                                final Minecraft mc28 = Flight.mc;
                                if (!Minecraft.player.collidedHorizontally) {
                                    if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                        final Minecraft mc29 = Flight.mc;
                                        Minecraft.player.motionY = -0.15;
                                    }
                                    break Label_0952;
                                }
                            }
                            final Minecraft mc30 = Flight.mc;
                            Minecraft.player.motionY = 0.15;
                        }
                    }
                    if (elytra != -2 && this.timerHelper.hasTimeElapsed(350L)) {
                        final PlayerControllerMP playerController4 = Flight.mc.playerController;
                        final int windowId4 = 0;
                        final int slotId4 = 6;
                        final int mouseButton4 = 1;
                        final ClickType pickup4 = ClickType.PICKUP;
                        final Minecraft mc31 = Flight.mc;
                        playerController4.windowClick(windowId4, slotId4, mouseButton4, pickup4, Minecraft.player);
                        final PlayerControllerMP playerController5 = Flight.mc.playerController;
                        final int windowId5 = 0;
                        final int slotId5 = elytra;
                        final int mouseButton5 = 1;
                        final ClickType pickup5 = ClickType.PICKUP;
                        final Minecraft mc32 = Flight.mc;
                        playerController5.windowClick(windowId5, slotId5, mouseButton5, pickup5, Minecraft.player);
                        final PlayerControllerMP playerController6 = Flight.mc.playerController;
                        final int windowId6 = 0;
                        final int slotId6 = 6;
                        final int mouseButton6 = 1;
                        final ClickType pickup6 = ClickType.PICKUP;
                        final Minecraft mc33 = Flight.mc;
                        playerController6.windowClick(windowId6, slotId6, mouseButton6, pickup6, Minecraft.player);
                    }
                    break;
                }
                case 5: {
                    if (++this.stage % 2 != 0) {
                        break;
                    }
                    final Minecraft mc34 = Flight.mc;
                    if (Minecraft.player.onGround) {
                        final Minecraft mc35 = Flight.mc;
                        Minecraft.player.jump();
                        break;
                    }
                    final int elytra = InventoryUtility.getItemIndex(Items.ELYTRA);
                    if (elytra != -2) {
                        final PlayerControllerMP playerController7 = Flight.mc.playerController;
                        final int windowId7 = 0;
                        final int slotId7 = 6;
                        final int mouseButton7 = 1;
                        final ClickType pickup7 = ClickType.PICKUP;
                        final Minecraft mc36 = Flight.mc;
                        playerController7.windowClick(windowId7, slotId7, mouseButton7, pickup7, Minecraft.player);
                        final PlayerControllerMP playerController8 = Flight.mc.playerController;
                        final int windowId8 = 0;
                        final int slotId8 = elytra;
                        final int mouseButton8 = 1;
                        final ClickType pickup8 = ClickType.PICKUP;
                        final Minecraft mc37 = Flight.mc;
                        playerController8.windowClick(windowId8, slotId8, mouseButton8, pickup8, Minecraft.player);
                        final PlayerControllerMP playerController9 = Flight.mc.playerController;
                        final int windowId9 = 0;
                        final int slotId9 = 6;
                        final int mouseButton9 = 1;
                        final ClickType pickup9 = ClickType.PICKUP;
                        final Minecraft mc38 = Flight.mc;
                        playerController9.windowClick(windowId9, slotId9, mouseButton9, pickup9, Minecraft.player);
                    }
                    final Minecraft mc39 = Flight.mc;
                    if (Minecraft.player.isAirBorne) {
                        final NetHandlerPlayClient connection3 = Flight.mc.getConnection();
                        final Minecraft mc40 = Flight.mc;
                        connection3.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    final Minecraft mc41 = Flight.mc;
                    if (Minecraft.player.isElytraFlying()) {
                        this.abletofly = true;
                    }
                    Label_1331: {
                        if (this.abletofly) {
                            final Minecraft mc42 = Flight.mc;
                            Minecraft.player.motionY = 0.0010000000474974513;
                            MoveUtility.setMotion(0.699999988079071);
                            if (!Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                                final Minecraft mc43 = Flight.mc;
                                if (!Minecraft.player.collidedHorizontally) {
                                    if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                        final Minecraft mc44 = Flight.mc;
                                        Minecraft.player.motionY = -0.30000001192092896;
                                    }
                                    break Label_1331;
                                }
                            }
                            final Minecraft mc45 = Flight.mc;
                            Minecraft.player.motionY = 0.25;
                        }
                    }
                    if (elytra != -2) {
                        final PlayerControllerMP playerController10 = Flight.mc.playerController;
                        final int windowId10 = 0;
                        final int slotId10 = 6;
                        final int mouseButton10 = 1;
                        final ClickType pickup10 = ClickType.PICKUP;
                        final Minecraft mc46 = Flight.mc;
                        playerController10.windowClick(windowId10, slotId10, mouseButton10, pickup10, Minecraft.player);
                        final PlayerControllerMP playerController11 = Flight.mc.playerController;
                        final int windowId11 = 0;
                        final int slotId11 = elytra;
                        final int mouseButton11 = 1;
                        final ClickType pickup11 = ClickType.PICKUP;
                        final Minecraft mc47 = Flight.mc;
                        playerController11.windowClick(windowId11, slotId11, mouseButton11, pickup11, Minecraft.player);
                        final PlayerControllerMP playerController12 = Flight.mc.playerController;
                        final int windowId12 = 0;
                        final int slotId12 = 6;
                        final int mouseButton12 = 1;
                        final ClickType pickup12 = ClickType.PICKUP;
                        final Minecraft mc48 = Flight.mc;
                        playerController12.windowClick(windowId12, slotId12, mouseButton12, pickup12, Minecraft.player);
                    }
                    break;
                }
                case 6: {
                    if (InventoryUtility.getItemIndex(Items.ELYTRA) == -1) {
                        Notifications.notify("NexusElytra ->", "\u0427\u0442\u043e\u0431\u044b \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0444\u043b\u0430\u0439, \u0443 \u0432\u0430\u0441 \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u044d\u043b\u0438\u0442\u0440\u0430 \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435!", Notifications.Notify.NotifyType.ERROR, 2);
                        ChatUtility.addChatMessage("\u0427\u0442\u043e\u0431\u044b \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0444\u043b\u0430\u0439, \u0443 \u0432\u0430\u0441 \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u044d\u043b\u0438\u0442\u0440\u0430 \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435!");
                        this.toggle();
                        return;
                    }
                    final Minecraft mc49 = Flight.mc;
                    if (Minecraft.player.onGround) {
                        final Minecraft mc50 = Flight.mc;
                        Minecraft.player.jump();
                        this.timerHelper.reset();
                        break;
                    }
                    if (!this.timerHelper.hasTimeElapsed(350L)) {
                        break;
                    }
                    final Minecraft mc51 = Flight.mc;
                    if (Minecraft.player.ticksExisted % 2 == 0) {
                        final NetHandlerPlayClient connection4 = Flight.mc.getConnection();
                        final Minecraft mc52 = Flight.mc;
                        connection4.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    MoveUtility.setStrafe(MoveUtility.getSpeed() + Flight.speed.getFloatValue() / 3.0f);
                    final Minecraft mc53 = Flight.mc;
                    if (Minecraft.player.isElytraFlying()) {
                        this.abletofly = true;
                    }
                    if (MoveUtility.isInLiquid()) {
                        this.abletofly = false;
                    }
                    if (this.abletofly) {
                        final Minecraft mc54 = Flight.mc;
                        Minecraft.player.capabilities.isFlying = true;
                        final Minecraft mc55 = Flight.mc;
                        Minecraft.player.capabilities.allowFlying = true;
                    }
                    final Minecraft mc56 = Flight.mc;
                    if (!Minecraft.player.isSneaking() && Flight.mc.gameSettings.keyBindJump.pressed) {
                        final Minecraft mc57 = Flight.mc;
                        Minecraft.player.motionY = Flight.motionY.getFloatValue();
                        break;
                    }
                    if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        final Minecraft mc58 = Flight.mc;
                        Minecraft.player.motionY = -Flight.motionY.getFloatValue();
                        break;
                    }
                    final Minecraft mc59 = Flight.mc;
                    final EntityPlayerSP player4 = Minecraft.player;
                    final Minecraft mc60 = Flight.mc;
                    player4.motionY = ((Minecraft.player.ticksExisted % 2 != 0) ? -0.25 : 0.25);
                    break;
                }
            }
        }
    }
    
    public int getFirework() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = Flight.mc;
            Minecraft.player.inventory.getStackInSlot(i);
            final Minecraft mc2 = Flight.mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() == Items.FIREWORKS) {
                return i;
            }
        }
        return -1;
    }
    
    public int getElytra() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = Flight.mc;
            Minecraft.player.inventory.getStackInSlot(i);
            final Minecraft mc2 = Flight.mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
                return i;
            }
        }
        return -1;
    }
    
    private void disabler() {
        final Minecraft mc = Flight.mc;
        if (!(Minecraft.player.inventory.getItemStack().getItem() instanceof ItemElytra)) {
            final int elytra = InventoryUtility.getItemIndex(Items.ELYTRA);
            final PlayerControllerMP playerController = Flight.mc.playerController;
            final int windowId = 0;
            final int slotId = (elytra < 9) ? (elytra + 36) : elytra;
            final int mouseButton = 1;
            final ClickType pickup = ClickType.PICKUP;
            final Minecraft mc2 = Flight.mc;
            playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
            final PlayerControllerMP playerController2 = Flight.mc.playerController;
            final int windowId2 = 0;
            final int slotId2 = 6;
            final int mouseButton2 = 1;
            final ClickType pickup2 = ClickType.PICKUP;
            final Minecraft mc3 = Flight.mc;
            playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
            final PlayerControllerMP playerController3 = Flight.mc.playerController;
            final int windowId3 = 0;
            final int slotId3 = (elytra < 9) ? (elytra + 36) : elytra;
            final int mouseButton3 = 1;
            final ClickType pickup3 = ClickType.PICKUP;
            final Minecraft mc4 = Flight.mc;
            playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
            final NetHandlerPlayClient connection = Flight.mc.getConnection();
            final Minecraft mc5 = Flight.mc;
            connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }
    
    static {
        Flight.flightMode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "Glide", "Matrix Elytra", "NexusElytra", "SunRiseNew" });
        Flight.speed = new SliderSetting("Speed", 1.0f, 0.1f, 5.0f, 0.01f);
        Flight.motionY = new SliderSetting("Motion Y", 1.0f, 0.1f, 2.0f, 0.01f, () -> Flight.flightMode.is("NexusElytra"));
        Flight.mc = Minecraft.getMinecraft();
    }
}
