// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.text.DecimalFormat;
import com.elementars.eclient.util.AnnouncerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.elementars.eclient.module.misc.Announcer;
import com.elementars.eclient.Xulu;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;
import com.elementars.eclient.module.Module;

public class AnnouncerRegistry
{
    static Module announcer;
    Minecraft minecraft;
    String heldItem;
    Item heldBlock;
    static int blocksWalkerCounter;
    static int dropItemDelay;
    static int itemPickUpDelay;
    static int blockPlacedDelay;
    static int blockBrokeDelay;
    static int chatDelay;
    static int commandDelay;
    static int pauseDelay;
    static int inventoryDelay;
    static int playerListDelay;
    static int perspectivesDelay;
    static int crouchedDelay;
    static int jumpDelay;
    static int attackDelay;
    static int eattingDelay;
    int lastWalkedAmount;
    int kek;
    BlockPos blockPos;
    String blockName;
    static long lastPositionUpdate;
    static double lastPositionX;
    static double lastPositionY;
    static double lastPositionZ;
    private static double speed;
    
    public AnnouncerRegistry() {
        this.minecraft = Minecraft.getMinecraft();
        this.heldItem = "";
        this.lastWalkedAmount = 0;
        this.kek = 0;
        this.blockName = "";
    }
    
    public static void initAnnouncer() {
        AnnouncerRegistry.announcer = Xulu.MODULE_MANAGER.getModuleByName("Announcer");
    }
    
    private void sendMessage(final String string) {
        if (Announcer.delayy == 0) {
            this.minecraft.player.sendChatMessage(string);
            Announcer.delayy = (Announcer.delay.getValue() & 0x14);
        }
    }
    
    @SubscribeEvent
    public void livingUpdate(final TickEvent.PlayerTickEvent event) {
        if (event.player instanceof EntityPlayerSP) {
            if (this.kek >= 1 && AnnouncerRegistry.announcer.isToggled() && this.minecraft.world.getBlockState(this.blockPos).getBlock() instanceof BlockAir && AnnouncerRegistry.blockBrokeDelay >= 300 && Announcer.blockBroke.getValue()) {
                this.sendMessage(AnnouncerUtil.getBlockBreak(this.blockName));
                AnnouncerRegistry.blockBrokeDelay = 0;
                this.kek = 0;
            }
            if (AnnouncerRegistry.announcer.isToggled()) {
                this.heldItem = this.minecraft.player.inventory.getCurrentItem().getDisplayName();
                this.heldBlock = this.minecraft.player.inventory.getCurrentItem().getItem();
                ++AnnouncerRegistry.blocksWalkerCounter;
                ++AnnouncerRegistry.dropItemDelay;
                ++AnnouncerRegistry.itemPickUpDelay;
                ++AnnouncerRegistry.blockPlacedDelay;
                ++AnnouncerRegistry.blockBrokeDelay;
                ++AnnouncerRegistry.chatDelay;
                ++AnnouncerRegistry.commandDelay;
                ++AnnouncerRegistry.pauseDelay;
                ++AnnouncerRegistry.inventoryDelay;
                ++AnnouncerRegistry.playerListDelay;
                ++AnnouncerRegistry.perspectivesDelay;
                ++AnnouncerRegistry.crouchedDelay;
                ++AnnouncerRegistry.jumpDelay;
                ++AnnouncerRegistry.attackDelay;
                ++AnnouncerRegistry.eattingDelay;
                if (AnnouncerRegistry.lastPositionUpdate + 30000L < System.currentTimeMillis() && Announcer.walk.getValue()) {
                    final double d0 = AnnouncerRegistry.lastPositionX - this.minecraft.player.lastTickPosX;
                    final double d2 = AnnouncerRegistry.lastPositionY - this.minecraft.player.lastTickPosY;
                    final double d3 = AnnouncerRegistry.lastPositionZ - this.minecraft.player.lastTickPosZ;
                    AnnouncerRegistry.speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
                    if (AnnouncerRegistry.speed > 0.0 && AnnouncerRegistry.speed <= 5000.0) {
                        this.sendMessage(AnnouncerUtil.getMove(new DecimalFormat("#").format(AnnouncerRegistry.speed)));
                        AnnouncerRegistry.lastPositionUpdate = System.currentTimeMillis();
                        AnnouncerRegistry.lastPositionX = this.minecraft.player.lastTickPosX;
                        AnnouncerRegistry.lastPositionY = this.minecraft.player.lastTickPosY;
                        AnnouncerRegistry.lastPositionZ = this.minecraft.player.lastTickPosZ;
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void itemCrafted(final PlayerEvent.ItemCraftedEvent event) {
        if (event.player instanceof EntityPlayerSP && AnnouncerRegistry.announcer.isToggled() && Announcer.craftedItem.getValue()) {
            this.sendMessage(AnnouncerUtil.getCraft(event.crafting.getCount() + " " + event.crafting.getDisplayName()));
        }
    }
    
    @SubscribeEvent
    public void itemPickedUp(final EntityItemPickupEvent event) {
        if (AnnouncerRegistry.announcer.isToggled() && AnnouncerRegistry.itemPickUpDelay >= 150 && Announcer.pickUpItem.getValue()) {
            final ItemStack entityStack = event.getItem().getItem();
            this.sendMessage(AnnouncerUtil.getPickedUp(entityStack.getDisplayName()));
            AnnouncerRegistry.itemPickUpDelay = 0;
        }
    }
    
    @SubscribeEvent
    public void itemSmelted(final PlayerEvent.ItemSmeltedEvent event) {
        if (event.player instanceof EntityPlayerSP && AnnouncerRegistry.announcer.isToggled() && Announcer.smeltedItem.getValue()) {
            this.sendMessage(AnnouncerUtil.getSmelted(event.smelting.getCount() + " " + event.smelting.getDisplayName()));
        }
    }
    
    @SubscribeEvent
    public void playerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (AnnouncerRegistry.announcer.isToggled() && Announcer.respawn.getValue()) {
            this.sendMessage(AnnouncerUtil.getRespawn());
        }
    }
    
    @SubscribeEvent
    public void playerBlockPlaced(final PlayerInteractEvent.RightClickBlock event) {
        if (AnnouncerRegistry.announcer.isToggled() && AnnouncerRegistry.blockPlacedDelay >= 150 && Announcer.blockPlaced.getValue() && event.getItemStack().getItem() instanceof ItemBlock) {
            this.sendMessage(AnnouncerUtil.getPlaced(event.getItemStack().getDisplayName()));
            AnnouncerRegistry.blockPlacedDelay = 0;
        }
    }
    
    @SubscribeEvent
    public void playerBlockBroke(final PlayerInteractEvent.LeftClickBlock event) {
        this.blockPos = event.getPos();
        this.blockName = this.minecraft.world.getBlockState(event.getPos()).getBlock().getLocalizedName();
        this.kek = 1;
    }
    
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (AnnouncerRegistry.announcer.isToggled()) {
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindDrop.getKeyCode() && !this.heldItem.equals("Air") && AnnouncerRegistry.dropItemDelay >= 150 && Announcer.itemDroped.getValue()) {
                this.sendMessage(AnnouncerUtil.getDropped(this.heldItem));
                AnnouncerRegistry.dropItemDelay = 0;
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindChat.getKeyCode() && AnnouncerRegistry.chatDelay >= 150 && Announcer.openChat.getValue()) {
                this.sendMessage(AnnouncerUtil.getChat());
                AnnouncerRegistry.chatDelay = 0;
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindPickBlock.getKeyCode() && Announcer.pickBlock.getValue()) {
                this.sendMessage(AnnouncerUtil.getPickBlock());
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindCommand.getKeyCode() && AnnouncerRegistry.commandDelay >= 150 && Announcer.command.getValue()) {
                this.sendMessage(AnnouncerUtil.getConsole());
                AnnouncerRegistry.commandDelay = 0;
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindFullscreen.getKeyCode() && Announcer.fullScreen.getValue()) {
                this.sendMessage(AnnouncerUtil.getFullScreen());
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == 1 && AnnouncerRegistry.pauseDelay >= 150 && Announcer.pauseGame.getValue()) {
                this.sendMessage(AnnouncerUtil.getPause());
                AnnouncerRegistry.pauseDelay = 0;
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindInventory.getKeyCode() && AnnouncerRegistry.inventoryDelay >= 150 && Announcer.openInv.getValue()) {
                this.sendMessage(AnnouncerUtil.getInventory());
                AnnouncerRegistry.inventoryDelay = 0;
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindPlayerList.getKeyCode() && AnnouncerRegistry.playerListDelay >= 150 && Announcer.playerList.getValue()) {
                this.sendMessage(AnnouncerUtil.getPlayerList());
                AnnouncerRegistry.playerListDelay = 0;
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindScreenshot.getKeyCode() && Announcer.screenShot.getValue()) {
                this.sendMessage(AnnouncerUtil.getScreenShot());
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindSwapHands.getKeyCode() && Announcer.swapHand.getValue()) {
                this.sendMessage(AnnouncerUtil.getSwappedHands());
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindSneak.getKeyCode() && AnnouncerRegistry.crouchedDelay >= 150 && Announcer.sneak.getValue()) {
                this.sendMessage(AnnouncerUtil.getCrouched());
                AnnouncerRegistry.crouchedDelay = 0;
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindTogglePerspective.getKeyCode() && AnnouncerRegistry.perspectivesDelay >= 150 && Announcer.Perspective.getValue()) {
                this.sendMessage(AnnouncerUtil.getPerspectives());
                AnnouncerRegistry.perspectivesDelay = 0;
            }
            if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == this.minecraft.gameSettings.keyBindJump.getKeyCode() && AnnouncerRegistry.jumpDelay >= 300 && Announcer.jump.getValue()) {
                this.sendMessage(AnnouncerUtil.getJumped());
                AnnouncerRegistry.jumpDelay = 0;
            }
            if (this.minecraft.player.isHandActive() && AnnouncerRegistry.eattingDelay >= 300 && Announcer.eatting.getValue() && (this.minecraft.player.getHeldItemMainhand().getItem() instanceof ItemFood || this.minecraft.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold)) {
                this.sendMessage(AnnouncerUtil.getAte(this.minecraft.player.getHeldItemMainhand().getDisplayName()));
                AnnouncerRegistry.eattingDelay = 0;
            }
        }
    }
    
    @SubscribeEvent
    public void onMouseClicked(final MouseEvent event) {
        if (AnnouncerRegistry.announcer.isToggled() && Mouse.getEventButtonState() && Mouse.getEventButton() == 0 && this.minecraft.objectMouseOver.entityHit != null && AnnouncerRegistry.attackDelay >= 300 && Announcer.attack.getValue()) {
            this.sendMessage(AnnouncerUtil.getAttacked(TextFormatting.getTextWithoutFormattingCodes(this.minecraft.objectMouseOver.entityHit.getName()), this.minecraft.player.getHeldItemMainhand().getDisplayName()));
            AnnouncerRegistry.attackDelay = 0;
        }
    }
    
    static {
        AnnouncerRegistry.dropItemDelay = 0;
        AnnouncerRegistry.itemPickUpDelay = 0;
        AnnouncerRegistry.blockPlacedDelay = 0;
        AnnouncerRegistry.blockBrokeDelay = 0;
        AnnouncerRegistry.chatDelay = 0;
        AnnouncerRegistry.commandDelay = 0;
        AnnouncerRegistry.pauseDelay = 0;
        AnnouncerRegistry.inventoryDelay = 0;
        AnnouncerRegistry.playerListDelay = 0;
        AnnouncerRegistry.perspectivesDelay = 0;
        AnnouncerRegistry.crouchedDelay = 0;
        AnnouncerRegistry.jumpDelay = 0;
        AnnouncerRegistry.attackDelay = 0;
        AnnouncerRegistry.eattingDelay = 0;
    }
}
