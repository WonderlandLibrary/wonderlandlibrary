package ru.salam4ik.bot.bot.entity;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import ru.salam4ik.bot.bot.network.BotPlayClient;

public class BotController {
    private int blockHitDelay;
    private final BotPlayClient connection;
    private GameType currentGameType;
    private int currentPlayerItem;

    public void pickItem(int n) {
        this.connection.sendPacket(new CPacketCustomPayload("MC|PickItem", new PacketBuffer(Unpooled.buffer()).writeVarInt(n)));
    }

    private void syncCurrentPlayItem() {
        int n = this.connection.getBot().inventory.currentItem;
        if (n != this.currentPlayerItem) {
            this.currentPlayerItem = n;
            this.connection.sendPacket(new CPacketHeldItemChange(this.currentPlayerItem));
        }
    }

    public boolean isSpectator() {
        return this.currentGameType == GameType.SPECTATOR;
    }

    public void updateController() {
        this.syncCurrentPlayItem();
        if (this.connection.getNetworkManager().isChannelOpen()) {
            this.connection.getNetworkManager().tick();
        }
    }

    public EnumActionResult interactWithEntity(EntityPlayer entityPlayer, Entity entity, RayTraceResult rayTraceResult, EnumHand enumHand) {
        this.syncCurrentPlayItem();
        Vec3d vec3d = new Vec3d(rayTraceResult.hitVec.x - entity.posX, rayTraceResult.hitVec.y - entity.posY, rayTraceResult.hitVec.z - entity.posZ);
        this.connection.sendPacket(new CPacketUseEntity(entity, enumHand, vec3d));
        return this.currentGameType == GameType.SPECTATOR ? EnumActionResult.PASS : entity.applyPlayerInteraction(entityPlayer, vec3d, enumHand);
    }

    public EnumActionResult interactWithEntity(EntityPlayer entityPlayer, Entity entity, EnumHand enumHand) {
        this.syncCurrentPlayItem();
        this.connection.sendPacket(new CPacketUseEntity(entity, enumHand));
        return this.currentGameType == GameType.SPECTATOR ? EnumActionResult.PASS : entityPlayer.interactOn(entity, enumHand);
    }

    public void setPlayerCapabilities(EntityPlayer entityPlayer) {
        this.currentGameType.configurePlayerCapabilities(entityPlayer.capabilities);
    }

    public void setGameType(GameType gameType) {
        this.currentGameType = gameType;
        this.currentGameType.configurePlayerCapabilities(this.connection.getBot().capabilities);
    }
    public BotController(BotPlayClient botPlayClient) {
        this.currentGameType = GameType.SURVIVAL;
        this.connection = botPlayClient;
    }

    public void flipPlayer(EntityPlayer entityPlayer) {
        entityPlayer.rotationYaw = -180.0f;
    }

    public boolean isSpectatorMode() {
        return this.currentGameType == GameType.SPECTATOR;
    }

    public void attackEntity(EntityPlayer playerIn, Entity targetEntity)
    {
        this.syncCurrentPlayItem();
        this.connection.sendPacket(new CPacketUseEntity(targetEntity));

        if (this.currentGameType != GameType.SPECTATOR)
        {
            playerIn.attackTargetEntityWithCurrentItem(targetEntity);
            playerIn.resetCooldown();
        }
    }

    public ItemStack windowClick(int n, int n2, int n3, ClickType clickType, EntityPlayer entityPlayer) {
        short s = entityPlayer.openContainer.getNextTransactionID(entityPlayer.inventory);
        ItemStack itemStack = entityPlayer.openContainer.slotClick(n2, n3, clickType, entityPlayer);
        this.connection.sendPacket(new CPacketClickWindow(n, n2, n3, clickType, itemStack, s));
        return itemStack;
    }




    public float getBlockReachDistance() {
        return this.currentGameType.isCreative() ? 5.0f : 4.5f;
    }

}