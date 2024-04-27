/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  io.netty.buffer.ByteBuf
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.NotImplementedError
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockBush
 *  net.minecraft.block.BlockCactus
 *  net.minecraft.block.BlockCarpet
 *  net.minecraft.block.BlockFence
 *  net.minecraft.block.BlockLadder
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockPane
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockSlime
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.block.BlockVine
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.client.gui.GuiWorldSelection
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.multiplayer.GuiConnecting
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.IImageBuffer
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.boss.EntityDragon
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.item.EntityFallingBlock
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.entity.item.EntityMinecartChest
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.monster.EntityGolem
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntityShulker
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemAir
 *  net.minecraft.item.ItemAppleGold
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemBed
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemBoat
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemBucket
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemEgg
 *  net.minecraft.item.ItemEnchantedBook
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemFishingRod
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemMinecart
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemSnowball
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagDouble
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.nbt.NBTTagString
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.login.client.CPacketEncryptionResponse
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketClientStatus
 *  net.minecraft.network.play.client.CPacketClientStatus$State
 *  net.minecraft.network.play.client.CPacketCloseWindow
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.client.CPacketCreativeInventoryAction
 *  net.minecraft.network.play.client.CPacketCustomPayload
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketResourcePackStatus
 *  net.minecraft.network.play.client.CPacketResourcePackStatus$Action
 *  net.minecraft.network.play.client.CPacketTabComplete
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.server.SPacketAnimation
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.network.play.server.SPacketCloseWindow
 *  net.minecraft.network.play.server.SPacketEntity
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketResourcePackSend
 *  net.minecraft.network.play.server.SPacketTabComplete
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.network.play.server.SPacketWindowItems
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.stats.StatList
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityDispenser
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.tileentity.TileEntityFurnace
 *  net.minecraft.tileentity.TileEntityHopper
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Session
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.event.ClickEvent
 *  net.minecraft.util.text.event.ClickEvent$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.client.GuiModList
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import kotlin.NoWhenBranchMatchedException;
import kotlin.NotImplementedError;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnchantmentType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.enums.MaterialType;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityOtherPlayerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IThreadDownloadImageData;
import net.ccbluex.liquidbounce.api.minecraft.client.render.WIImageBuffer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IDynamicTexture;
import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IGlStateManager;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.vertex.IVertexBuffer;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IGameSettings;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.nbt.IJsonToNBT;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagDouble;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagList;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagString;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketAnimation;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketClientStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCloseWindow;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketKeepAlive;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerBlockPlacement;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerPosLook;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketResourcePackStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.stats.IStatBase;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.ISession;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.api.network.IPacketBuffer;
import net.ccbluex.liquidbounce.api.util.IWrappedFontRenderer;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.AxisAlignedBBImpl;
import net.ccbluex.liquidbounce.injection.backend.Backend;
import net.ccbluex.liquidbounce.injection.backend.BlockImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketAnimationImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketClientStatusImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketCloseWindowImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketCustomPayloadImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketEntityActionImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketHeldItemChangeImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketKeepAliveImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerBlockPlacementImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerPosLookImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketUseEntityImpl;
import net.ccbluex.liquidbounce.injection.backend.ClassProviderImpl$WhenMappings;
import net.ccbluex.liquidbounce.injection.backend.ClickEventImpl;
import net.ccbluex.liquidbounce.injection.backend.CreativeTabsImpl;
import net.ccbluex.liquidbounce.injection.backend.DynamicTextureImpl;
import net.ccbluex.liquidbounce.injection.backend.EnchantmentImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityOtherPlayerMPImpl;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImpl;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.ccbluex.liquidbounce.injection.backend.GameSettingsImpl;
import net.ccbluex.liquidbounce.injection.backend.GlStateManagerImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiButtonImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiScreenImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiTextFieldImpl;
import net.ccbluex.liquidbounce.injection.backend.IChatComponentImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.ccbluex.liquidbounce.injection.backend.JsonToNBTImpl;
import net.ccbluex.liquidbounce.injection.backend.MaterialImpl;
import net.ccbluex.liquidbounce.injection.backend.MinecraftImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTTagDoubleImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTTagListImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTTagStringImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketBufferImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.injection.backend.PotionEffectImpl;
import net.ccbluex.liquidbounce.injection.backend.PotionImpl;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl;
import net.ccbluex.liquidbounce.injection.backend.ScaledResolutionImpl;
import net.ccbluex.liquidbounce.injection.backend.ServerDataImpl;
import net.ccbluex.liquidbounce.injection.backend.SessionImpl;
import net.ccbluex.liquidbounce.injection.backend.StatBaseImpl;
import net.ccbluex.liquidbounce.injection.backend.TessellatorImpl;
import net.ccbluex.liquidbounce.injection.backend.ThreadDownloadImageDataImpl;
import net.ccbluex.liquidbounce.injection.backend.TileEntityImpl;
import net.ccbluex.liquidbounce.injection.backend.VertexBufferImpl;
import net.ccbluex.liquidbounce.injection.backend.VertexFormatImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldClientImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.ccbluex.liquidbounce.injection.backend.utils.CreativeTabsWrapper;
import net.ccbluex.liquidbounce.injection.backend.utils.FontRendererWrapper;
import net.ccbluex.liquidbounce.injection.backend.utils.GuiPasswordField;
import net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper;
import net.ccbluex.liquidbounce.injection.backend.utils.GuiSlotWrapper;
import net.ccbluex.liquidbounce.injection.backend.utils.SafeVertexBuffer;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.GuiModList;
import org.jetbrains.annotations.Nullable;

public final class ClassProviderImpl
implements IClassProvider {
    public static final ClassProviderImpl INSTANCE;

    @Override
    public boolean isBlockLadder(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockLadder;
    }

    @Override
    public ICPacketCustomPayload createCPacketCustomPayload(String string, IPacketBuffer iPacketBuffer) {
        PacketBuffer packetBuffer;
        IPacketBuffer iPacketBuffer2 = iPacketBuffer;
        String string2 = string;
        boolean bl = false;
        PacketBuffer packetBuffer2 = packetBuffer = ((PacketBufferImpl)iPacketBuffer2).getWrapped();
        String string3 = string2;
        CPacketCustomPayload cPacketCustomPayload = new CPacketCustomPayload(string3, packetBuffer2);
        return new CPacketCustomPayloadImpl(cPacketCustomPayload);
    }

    @Override
    public boolean isItemAppleGold(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemAppleGold;
    }

    @Override
    public ITessellator getTessellatorInstance() {
        return new TessellatorImpl(Tessellator.func_178181_a());
    }

    @Override
    public IGuiScreen createGuiConnecting(IGuiScreen iGuiScreen, IMinecraft iMinecraft, IServerData iServerData) {
        ServerData serverData;
        Object object = iGuiScreen;
        boolean bl = false;
        GuiScreen guiScreen = (GuiScreen)((GuiScreenImpl)object).getWrapped();
        object = iMinecraft;
        bl = false;
        Minecraft minecraft = ((MinecraftImpl)object).getWrapped();
        object = iServerData;
        bl = false;
        ServerData serverData2 = serverData = ((ServerDataImpl)object).getWrapped();
        Minecraft minecraft2 = minecraft;
        GuiScreen guiScreen2 = guiScreen;
        GuiScreen guiScreen3 = (GuiScreen)new GuiConnecting(guiScreen2, minecraft2, serverData2);
        return new GuiScreenImpl(guiScreen3);
    }

    @Override
    public IGuiTextField createGuiTextField(int n, IFontRenderer iFontRenderer, int n2, int n3, int n4, int n5) {
        IFontRenderer iFontRenderer2 = iFontRenderer;
        int n6 = n;
        boolean bl = false;
        FontRenderer fontRenderer = ((FontRendererImpl)iFontRenderer2).getWrapped();
        int n7 = n5;
        int n8 = n4;
        int n9 = n3;
        int n10 = n2;
        FontRenderer fontRenderer2 = fontRenderer;
        int n11 = n6;
        GuiTextField guiTextField = new GuiTextField(n11, fontRenderer2, n10, n9, n8, n7);
        return new GuiTextFieldImpl(guiTextField);
    }

    @Override
    public void wrapGuiSlot(WrappedGuiSlot wrappedGuiSlot, IMinecraft iMinecraft, int n, int n2, int n3, int n4, int n5) {
        new GuiSlotWrapper(wrappedGuiSlot, iMinecraft, n, n2, n3, n4, n5);
    }

    @Override
    public IThreadDownloadImageData createThreadDownloadImageData(@Nullable File file, String string, @Nullable IResourceLocation iResourceLocation, WIImageBuffer wIImageBuffer) {
        ResourceLocation resourceLocation;
        File file2 = file;
        String string2 = string;
        IResourceLocation iResourceLocation2 = iResourceLocation;
        if (iResourceLocation2 != null) {
            IResourceLocation iResourceLocation3 = iResourceLocation2;
            String string3 = string2;
            File file3 = file2;
            boolean bl = false;
            ResourceLocation resourceLocation2 = ((ResourceLocationImpl)iResourceLocation3).getWrapped();
            file2 = file3;
            string2 = string3;
            resourceLocation = resourceLocation2;
        } else {
            resourceLocation = null;
        }
        IImageBuffer iImageBuffer = new IImageBuffer(wIImageBuffer){
            final WIImageBuffer $imageBufferIn;

            public void func_152634_a() {
                this.$imageBufferIn.skinAvailable();
            }

            public BufferedImage func_78432_a(@Nullable BufferedImage bufferedImage) {
                return this.$imageBufferIn.parseUserSkin(bufferedImage);
            }

            static {
            }
            {
                this.$imageBufferIn = wIImageBuffer;
            }
        };
        ResourceLocation resourceLocation3 = resourceLocation;
        String string4 = string2;
        File file4 = file2;
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(file4, string4, resourceLocation3, iImageBuffer);
        return new ThreadDownloadImageDataImpl(threadDownloadImageData);
    }

    @Override
    public boolean isItemEnderPearl(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemEnderPearl;
    }

    @Override
    public boolean isCPacketPlayerPosLook(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketPlayer.PositionRotation;
    }

    @Override
    public IPotion getPotionEnum(PotionType potionType) {
        Potion potion;
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$1[potionType.ordinal()]) {
            case 1: {
                potion = MobEffects.field_76432_h;
                break;
            }
            case 2: {
                potion = MobEffects.field_76428_l;
                break;
            }
            case 3: {
                potion = MobEffects.field_76440_q;
                break;
            }
            case 4: {
                potion = MobEffects.field_76424_c;
                break;
            }
            case 5: {
                potion = MobEffects.field_76438_s;
                break;
            }
            case 6: {
                potion = MobEffects.field_76419_f;
                break;
            }
            case 7: {
                potion = MobEffects.field_76431_k;
                break;
            }
            case 8: {
                potion = MobEffects.field_76437_t;
                break;
            }
            case 9: {
                potion = MobEffects.field_76421_d;
                break;
            }
            case 10: {
                potion = MobEffects.field_76433_i;
                break;
            }
            case 11: {
                potion = MobEffects.field_82731_v;
                break;
            }
            case 12: {
                potion = MobEffects.field_76436_u;
                break;
            }
            case 13: {
                potion = MobEffects.field_76439_r;
                break;
            }
            case 14: {
                potion = MobEffects.field_76430_j;
                break;
            }
            case 15: {
                boolean bl = false;
                throw (Throwable)new NotImplementedError(null, 1, null);
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        Potion potion2 = potion;
        return new PotionImpl(potion2);
    }

    @Override
    public boolean isBlockSlime(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockSlime;
    }

    @Override
    public boolean isItemBed(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemBed;
    }

    @Override
    public boolean isGuiChest(@Nullable Object object) {
        return object instanceof GuiImpl && ((GuiImpl)object).getWrapped() instanceof GuiChest;
    }

    @Override
    public boolean isCPacketPlayerDigging(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketPlayerDigging;
    }

    @Override
    public boolean isCPacketPlayer(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketPlayer;
    }

    @Override
    public boolean isGuiHudDesigner(@Nullable Object object) {
        return object instanceof GuiScreenImpl && ((GuiScreenImpl)object).getWrapped() instanceof GuiScreenWrapper && ((GuiScreenWrapper)((GuiScreenImpl)object).getWrapped()).getWrapped() instanceof GuiHudDesigner;
    }

    @Override
    public boolean isEntityShulker(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityShulker;
    }

    @Override
    public boolean isEntityTNTPrimed(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityTNTPrimed;
    }

    @Override
    public IGuiScreen createGuiSelectWorld(IGuiScreen iGuiScreen) {
        GuiScreen guiScreen;
        IGuiScreen iGuiScreen2 = iGuiScreen;
        boolean bl = false;
        GuiScreen guiScreen2 = guiScreen = (GuiScreen)((GuiScreenImpl)iGuiScreen2).getWrapped();
        GuiScreen guiScreen3 = (GuiScreen)new GuiWorldSelection(guiScreen2);
        return new GuiScreenImpl(guiScreen3);
    }

    @Override
    public IScaledResolution createScaledResolution(IMinecraft iMinecraft) {
        Minecraft minecraft;
        IMinecraft iMinecraft2 = iMinecraft;
        boolean bl = false;
        Minecraft minecraft2 = minecraft = ((MinecraftImpl)iMinecraft2).getWrapped();
        ScaledResolution scaledResolution = new ScaledResolution(minecraft2);
        return new ScaledResolutionImpl(scaledResolution);
    }

    @Override
    public IEntityOtherPlayerMP createEntityOtherPlayerMP(IWorldClient iWorldClient, GameProfile gameProfile) {
        IWorldClient iWorldClient2 = iWorldClient;
        boolean bl = false;
        WorldClient worldClient = (WorldClient)((WorldClientImpl)iWorldClient2).getWrapped();
        GameProfile gameProfile2 = gameProfile;
        World world = (World)worldClient;
        EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(world, gameProfile2);
        return new EntityOtherPlayerMPImpl(entityOtherPlayerMP);
    }

    @Override
    public INBTTagCompound createNBTTagCompound() {
        return new NBTTagCompoundImpl(new NBTTagCompound());
    }

    @Override
    public boolean isEntityLivingBase(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityLivingBase;
    }

    @Override
    public boolean isGuiGameOver(@Nullable Object object) {
        return object instanceof GuiImpl && ((GuiImpl)object).getWrapped() instanceof GuiGameOver;
    }

    @Override
    public ICPacketUseEntity createCPacketUseEntity(IEntity iEntity, ICPacketUseEntity.WAction wAction) {
        ICPacketUseEntity iCPacketUseEntity;
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$0[wAction.ordinal()]) {
            case 1: {
                IEntity iEntity2 = iEntity;
                boolean bl = false;
                Entity entity = ((EntityImpl)iEntity2).getWrapped();
                EnumHand enumHand = EnumHand.MAIN_HAND;
                Entity entity2 = entity;
                CPacketUseEntity cPacketUseEntity = new CPacketUseEntity(entity2, enumHand);
                iCPacketUseEntity = new CPacketUseEntityImpl(cPacketUseEntity);
                break;
            }
            case 2: {
                Entity entity;
                IEntity iEntity3 = iEntity;
                boolean bl = false;
                Entity entity3 = entity = ((EntityImpl)iEntity3).getWrapped();
                CPacketUseEntity cPacketUseEntity = new CPacketUseEntity(entity3);
                iCPacketUseEntity = new CPacketUseEntityImpl(cPacketUseEntity);
                break;
            }
            case 3: {
                Backend backend = Backend.INSTANCE;
                boolean bl = false;
                throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return iCPacketUseEntity;
    }

    @Override
    public boolean isItemTool(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemTool;
    }

    @Override
    public boolean isGuiInventory(@Nullable Object object) {
        return object instanceof GuiImpl && ((GuiImpl)object).getWrapped() instanceof GuiInventory;
    }

    @Override
    public boolean isTileEntityFurnace(@Nullable Object object) {
        return object instanceof TileEntityImpl && ((TileEntityImpl)object).getWrapped() instanceof TileEntityFurnace;
    }

    static {
        ClassProviderImpl classProviderImpl;
        INSTANCE = classProviderImpl = new ClassProviderImpl();
    }

    @Override
    public boolean isSPacketExplosion(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketEntityVelocity;
    }

    @Override
    public boolean isCPacketAnimation(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketAnimation;
    }

    @Override
    public IEnchantment getEnchantmentEnum(EnchantmentType enchantmentType) {
        Enchantment enchantment;
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$7[enchantmentType.ordinal()]) {
            case 1: {
                enchantment = Enchantments.field_185302_k;
                break;
            }
            case 2: {
                enchantment = Enchantments.field_185309_u;
                break;
            }
            case 3: {
                enchantment = Enchantments.field_180310_c;
                break;
            }
            case 4: {
                enchantment = Enchantments.field_180309_e;
                break;
            }
            case 5: {
                enchantment = Enchantments.field_180308_g;
                break;
            }
            case 6: {
                enchantment = Enchantments.field_92091_k;
                break;
            }
            case 7: {
                enchantment = Enchantments.field_77329_d;
                break;
            }
            case 8: {
                enchantment = Enchantments.field_185298_f;
                break;
            }
            case 9: {
                enchantment = Enchantments.field_185299_g;
                break;
            }
            case 10: {
                enchantment = Enchantments.field_185297_d;
                break;
            }
            case 11: {
                enchantment = Enchantments.field_185307_s;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return new EnchantmentImpl(enchantment);
    }

    @Override
    public boolean isEntityBoat(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityBoat;
    }

    @Override
    public IFontRenderer wrapFontRenderer(IWrappedFontRenderer iWrappedFontRenderer) {
        return new FontRendererImpl(new FontRendererWrapper(iWrappedFontRenderer));
    }

    @Override
    public boolean isCPacketCloseWindow(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketCloseWindow;
    }

    @Override
    public boolean isBlockSlab(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockSlab;
    }

    @Override
    public boolean isEntityVillager(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityVillager;
    }

    @Override
    public boolean isTileEntityEnderChest(@Nullable Object object) {
        return object instanceof TileEntityImpl && ((TileEntityImpl)object).getWrapped() instanceof TileEntityEnderChest;
    }

    @Override
    public IIChatComponent createChatComponentText(String string) {
        return new IChatComponentImpl((ITextComponent)new TextComponentString(string));
    }

    @Override
    public boolean isItemPickaxe(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemPickaxe;
    }

    @Override
    public boolean isBlockCarpet(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockCarpet;
    }

    @Override
    public boolean isClickGui(@Nullable Object object) {
        return object instanceof GuiScreenImpl && ((GuiScreenImpl)object).getWrapped() instanceof GuiScreenWrapper && ((GuiScreenWrapper)((GuiScreenImpl)object).getWrapped()).getWrapped() instanceof ClickGui;
    }

    private ClassProviderImpl() {
    }

    @Override
    public IItemStack createItemStack(IBlock iBlock) {
        Block block;
        IBlock iBlock2 = iBlock;
        boolean bl = false;
        Block block2 = block = ((BlockImpl)iBlock2).getWrapped();
        ItemStack itemStack = new ItemStack(block2);
        return new ItemStackImpl(itemStack);
    }

    @Override
    public boolean isItemArmor(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemArmor;
    }

    @Override
    public IPacket createCPacketTabComplete(String string) {
        return new PacketImpl((Packet)new CPacketTabComplete(string, null, false));
    }

    @Override
    public IClickEvent createClickEvent(IClickEvent.WAction wAction, String string) {
        IClickEvent.WAction wAction2 = wAction;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$11[wAction2.ordinal()]) {
            case 1: {
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        ClickEvent.Action action = ClickEvent.Action.OPEN_URL;
        String string2 = string;
        ClickEvent.Action action2 = action;
        ClickEvent clickEvent = new ClickEvent(action2, string2);
        return new ClickEventImpl(clickEvent);
    }

    @Override
    public ICPacketPlayerBlockPlacement createCPacketPlayerBlockPlacement(@Nullable IItemStack iItemStack) {
        Backend backend = Backend.INSTANCE;
        boolean bl = false;
        throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
    }

    @Override
    public boolean isTileEntityChest(@Nullable Object object) {
        return object instanceof TileEntityImpl && ((TileEntityImpl)object).getWrapped() instanceof TileEntityChest;
    }

    @Override
    public boolean isEntityArmorStand(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityArmorStand;
    }

    @Override
    public IStatBase getStatEnum(StatType statType) {
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$5[statType.ordinal()]) {
            case 1: {
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return new StatBaseImpl(StatList.field_75953_u);
    }

    @Override
    public IBlock getBlockEnum(BlockType blockType) {
        IBlock iBlock;
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$3[blockType.ordinal()]) {
            case 1: {
                Block block = Blocks.field_150381_bn;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 2: {
                Block block = (Block)Blocks.field_150486_ae;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 3: {
                Block block = Blocks.field_150477_bB;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 4: {
                Block block = Blocks.field_150447_bR;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 5: {
                Block block = Blocks.field_150467_bQ;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 6: {
                Block block = (Block)Blocks.field_150354_m;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 7: {
                Block block = Blocks.field_150321_G;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 8: {
                Block block = Blocks.field_150478_aa;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 9: {
                Block block = Blocks.field_150462_ai;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 10: {
                Block block = Blocks.field_150460_al;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 11: {
                Block block = Blocks.field_150392_bi;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 12: {
                Block block = Blocks.field_150367_z;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 13: {
                Block block = Blocks.field_150456_au;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 14: {
                Block block = Blocks.field_150452_aw;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 15: {
                Block block = Blocks.field_150335_W;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 16: {
                Block block = Blocks.field_180393_cK;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 17: {
                Block block = Blocks.field_180394_cL;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 18: {
                Block block = Blocks.field_150429_aA;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 19: {
                Block block = Blocks.field_150323_B;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 20: {
                Block block = Blocks.field_150409_cd;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 21: {
                Block block = Blocks.field_150431_aC;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 22: {
                Block block = Blocks.field_150350_a;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 23: {
                Block block = Blocks.field_150403_cj;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 24: {
                Block block = Blocks.field_150432_aD;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 25: {
                Block block = (Block)Blocks.field_150355_j;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 26: {
                Block block = Blocks.field_180401_cv;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 27: {
                Block block = (Block)Blocks.field_150358_i;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 28: {
                Block block = Blocks.field_150365_q;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 29: {
                Block block = Blocks.field_150366_p;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 30: {
                Block block = Blocks.field_150352_o;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 31: {
                Block block = Blocks.field_150450_ax;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 32: {
                Block block = Blocks.field_150369_x;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 33: {
                Block block = Blocks.field_150482_ag;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 34: {
                Block block = Blocks.field_150412_bA;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 35: {
                Block block = Blocks.field_150449_bY;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 36: {
                Block block = Blocks.field_150435_aG;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 37: {
                Block block = Blocks.field_150426_aN;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 38: {
                Block block = Blocks.field_150468_ap;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 39: {
                Block block = Blocks.field_150402_ci;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 40: {
                Block block = Blocks.field_150339_S;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 41: {
                Block block = Blocks.field_150340_R;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 42: {
                Block block = Blocks.field_150484_ah;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 43: {
                Block block = Blocks.field_150475_bE;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 44: {
                Block block = Blocks.field_150451_bX;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 45: {
                Block block = Blocks.field_150368_y;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 46: {
                Block block = (Block)Blocks.field_150480_ab;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 47: {
                Block block = Blocks.field_150341_Y;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 48: {
                Block block = Blocks.field_150474_ac;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 49: {
                Block block = Blocks.field_150378_br;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 50: {
                Block block = Blocks.field_150342_X;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 51: {
                Block block = Blocks.field_150483_bI;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 52: {
                Block block = (Block)Blocks.field_150353_l;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 53: {
                Block block = (Block)Blocks.field_150356_k;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 54: {
                Block block = Blocks.field_150470_am;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 55: {
                Block block = Blocks.field_150380_bt;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 56: {
                Block block = Blocks.field_150420_aW;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 57: {
                Block block = Blocks.field_150419_aX;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            case 58: {
                Block block = Blocks.field_150458_ak;
                boolean bl = false;
                iBlock = new BlockImpl(block);
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return iBlock;
    }

    @Override
    public boolean isEntityAnimal(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityAnimal;
    }

    @Override
    public boolean isTileEntityHopper(@Nullable Object object) {
        return object instanceof TileEntityImpl && ((TileEntityImpl)object).getWrapped() instanceof TileEntityHopper;
    }

    @Override
    public boolean isSPacketChat(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketChat;
    }

    @Override
    public boolean isCPacketPlayerPosition(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketPlayer.Position;
    }

    @Override
    public boolean isItemBucketMilk(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemBucketMilk;
    }

    @Override
    public IGuiButton createGuiButton(int n, int n2, int n3, String string) {
        return new GuiButtonImpl(new GuiButton(n, n2, n3, string));
    }

    @Override
    public boolean isItemPotion(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemPotion;
    }

    @Override
    public boolean isItemBow(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemBow;
    }

    @Override
    public boolean isItemAir(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemAir;
    }

    @Override
    public IPacket createCPacketCreativeInventoryAction(int n, IItemStack iItemStack) {
        ItemStack itemStack;
        IItemStack iItemStack2 = iItemStack;
        int n2 = n;
        boolean bl = false;
        ItemStack itemStack2 = itemStack = ((ItemStackImpl)iItemStack2).getWrapped();
        int n3 = n2;
        Packet packet = (Packet)new CPacketCreativeInventoryAction(n3, itemStack2);
        return new PacketImpl(packet);
    }

    @Override
    public IMaterial getMaterialEnum(MaterialType materialType) {
        Material material;
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$4[materialType.ordinal()]) {
            case 1: {
                material = Material.field_151579_a;
                break;
            }
            case 2: {
                material = Material.field_151586_h;
                break;
            }
            case 3: {
                material = Material.field_151587_i;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return new MaterialImpl(material);
    }

    @Override
    public ICPacketUseEntity createCPacketUseEntity(IEntity iEntity, WVec3 wVec3) {
        Vec3d vec3d;
        Object object = iEntity;
        boolean bl = false;
        Entity entity = ((EntityImpl)object).getWrapped();
        object = wVec3;
        EnumHand enumHand = EnumHand.MAIN_HAND;
        bl = false;
        Vec3d vec3d2 = vec3d = new Vec3d(((WVec3)object).getXCoord(), ((WVec3)object).getYCoord(), ((WVec3)object).getZCoord());
        EnumHand enumHand2 = enumHand;
        Entity entity2 = entity;
        CPacketUseEntity cPacketUseEntity = new CPacketUseEntity(entity2, enumHand2, vec3d2);
        return new CPacketUseEntityImpl(cPacketUseEntity);
    }

    @Override
    public ICPacketHeldItemChange createCPacketHeldItemChange(int n) {
        return new CPacketHeldItemChangeImpl(new CPacketHeldItemChange(n));
    }

    @Override
    public boolean isItemFood(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemFood;
    }

    @Override
    public IGuiButton createGuiButton(int n, int n2, int n3, int n4, int n5, String string) {
        return new GuiButtonImpl(new GuiButton(n, n2, n3, n4, n5, string));
    }

    @Override
    public boolean isEntitySquid(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntitySquid;
    }

    @Override
    public boolean isCPacketCustomPayload(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketCustomPayload;
    }

    @Override
    public ICPacketPlayerBlockPlacement createCPacketPlayerBlockPlacement(WBlockPos wBlockPos, int n, @Nullable IItemStack iItemStack, float f, float f2, float f3) {
        WBlockPos wBlockPos2 = wBlockPos;
        boolean bl = false;
        BlockPos blockPos = new BlockPos(wBlockPos2.getX(), wBlockPos2.getY(), wBlockPos2.getZ());
        float f4 = f3;
        float f5 = f2;
        float f6 = f;
        EnumHand enumHand = EnumHand.MAIN_HAND;
        EnumFacing enumFacing = EnumFacing.values()[n];
        BlockPos blockPos2 = blockPos;
        CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock = new CPacketPlayerTryUseItemOnBlock(blockPos2, enumFacing, enumHand, f6, f5, f4);
        return new CPacketPlayerBlockPlacementImpl(cPacketPlayerTryUseItemOnBlock);
    }

    @Override
    public boolean isSPacketWindowItems(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketWindowItems;
    }

    @Override
    public ICPacketCloseWindow createCPacketCloseWindow(int n) {
        return new CPacketCloseWindowImpl(new CPacketCloseWindow(n));
    }

    @Override
    public ICPacketAnimation createCPacketAnimation() {
        return new CPacketAnimationImpl(new CPacketAnimation(EnumHand.MAIN_HAND));
    }

    @Override
    public INBTTagList createNBTTagList() {
        return new NBTTagListImpl(new NBTTagList());
    }

    @Override
    public boolean isSPacketEntityVelocity(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketEntityVelocity;
    }

    @Override
    public boolean isSPacketResourcePackSend(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketResourcePackSend;
    }

    @Override
    public boolean isTileEntityShulkerBox(@Nullable Object object) {
        return object instanceof TileEntityImpl && ((TileEntityImpl)object).getWrapped() instanceof TileEntityShulkerBox;
    }

    @Override
    public boolean isSPacketAnimation(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketAnimation;
    }

    @Override
    public PacketImpl createCPacketTryUseItem(WEnumHand wEnumHand) {
        EnumHand enumHand;
        EnumHand enumHand2;
        WEnumHand wEnumHand2 = wEnumHand;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$16[wEnumHand2.ordinal()]) {
            case 1: {
                enumHand2 = EnumHand.MAIN_HAND;
                break;
            }
            case 2: {
                enumHand2 = EnumHand.OFF_HAND;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        EnumHand enumHand3 = enumHand = enumHand2;
        Packet packet = (Packet)new CPacketPlayerTryUseItem(enumHand3);
        return new PacketImpl(packet);
    }

    @Override
    public boolean isBlockLiquid(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockLiquid;
    }

    @Override
    public IPacket createCPacketEncryptionResponse(SecretKey secretKey, PublicKey publicKey, byte[] byArray) {
        return new PacketImpl((Packet)new CPacketEncryptionResponse(secretKey, publicKey, byArray));
    }

    @Override
    public boolean isCPacketUseEntity(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketUseEntity;
    }

    @Override
    public boolean isItemMinecart(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemMinecart;
    }

    @Override
    public IItem getItemEnum(ItemType itemType) {
        Item item;
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$6[itemType.ordinal()]) {
            case 1: {
                item = Items.field_151009_A;
                break;
            }
            case 2: {
                item = Items.field_151054_z;
                break;
            }
            case 3: {
                item = Items.field_151033_d;
                break;
            }
            case 4: {
                item = Items.field_151129_at;
                break;
            }
            case 5: {
                item = Items.field_151099_bA;
                break;
            }
            case 6: {
                item = Items.field_151131_as;
                break;
            }
            case 7: {
                item = Items.field_151095_cc;
                break;
            }
            case 8: {
                item = (Item)Items.field_151068_bn;
                break;
            }
            case 9: {
                item = Items.field_151144_bL;
                break;
            }
            case 10: {
                item = (Item)Items.field_179565_cj;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return new ItemImpl(item);
    }

    @Override
    public IPacket createCPacketPlayerDigging(ICPacketPlayerDigging.WAction wAction, WBlockPos wBlockPos, IEnumFacing iEnumFacing) {
        EnumFacing enumFacing;
        CPacketPlayerDigging.Action action;
        Object object = wAction;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[((Enum)object).ordinal()]) {
            case 1: {
                action = CPacketPlayerDigging.Action.START_DESTROY_BLOCK;
                break;
            }
            case 2: {
                action = CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK;
                break;
            }
            case 3: {
                action = CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK;
                break;
            }
            case 4: {
                action = CPacketPlayerDigging.Action.DROP_ALL_ITEMS;
                break;
            }
            case 5: {
                action = CPacketPlayerDigging.Action.DROP_ITEM;
                break;
            }
            case 6: {
                action = CPacketPlayerDigging.Action.RELEASE_USE_ITEM;
                break;
            }
            case 7: {
                action = CPacketPlayerDigging.Action.SWAP_HELD_ITEMS;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        CPacketPlayerDigging.Action action2 = action;
        object = wBlockPos;
        bl = false;
        BlockPos blockPos = new BlockPos(((WVec3i)object).getX(), ((WVec3i)object).getY(), ((WVec3i)object).getZ());
        object = iEnumFacing;
        bl = false;
        EnumFacing enumFacing2 = enumFacing = ((EnumFacingImpl)object).getWrapped();
        BlockPos blockPos2 = blockPos;
        CPacketPlayerDigging.Action action3 = action2;
        Packet packet = (Packet)new CPacketPlayerDigging(action3, blockPos2, enumFacing2);
        return new PacketImpl(packet);
    }

    @Override
    public boolean isCPacketChatMessage(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketChatMessage;
    }

    @Override
    public boolean isItemAxe(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemAxe;
    }

    @Override
    public boolean isItemEnchantedBook(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemEnchantedBook;
    }

    @Override
    public boolean isSPacketEntity(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketEntity;
    }

    @Override
    public ICPacketCloseWindow createCPacketCloseWindow() {
        return new CPacketCloseWindowImpl(new CPacketCloseWindow());
    }

    @Override
    public INBTTagDouble createNBTTagDouble(double d) {
        return new NBTTagDoubleImpl(new NBTTagDouble(d));
    }

    @Override
    public ICPacketPlayer createCPacketPlayer(boolean bl) {
        return new CPacketPlayerImpl(new CPacketPlayer(bl));
    }

    @Override
    public boolean isCPacketClientStatus(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketClientStatus;
    }

    @Override
    public IPacket createICPacketResourcePackStatus(String string, ICPacketResourcePackStatus.WAction wAction) {
        CPacketResourcePackStatus.Action action;
        CPacketResourcePackStatus.Action action2;
        ICPacketResourcePackStatus.WAction wAction2 = wAction;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$13[wAction2.ordinal()]) {
            case 1: {
                action2 = CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED;
                break;
            }
            case 2: {
                action2 = CPacketResourcePackStatus.Action.DECLINED;
                break;
            }
            case 3: {
                action2 = CPacketResourcePackStatus.Action.FAILED_DOWNLOAD;
                break;
            }
            case 4: {
                action2 = CPacketResourcePackStatus.Action.ACCEPTED;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        CPacketResourcePackStatus.Action action3 = action = action2;
        Packet packet = (Packet)new CPacketResourcePackStatus(action3);
        return new PacketImpl(packet);
    }

    @Override
    public boolean isSPacketTabComplete(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketTabComplete;
    }

    @Override
    public boolean isGuiContainer(@Nullable Object object) {
        return object instanceof GuiImpl && ((GuiImpl)object).getWrapped() instanceof GuiContainer;
    }

    @Override
    public boolean isBlockVine(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockVine;
    }

    @Override
    public boolean isEntitySlime(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntitySlime;
    }

    @Override
    public boolean isCPacketKeepAlive(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketKeepAlive;
    }

    @Override
    public IPacketBuffer createPacketBuffer(ByteBuf byteBuf) {
        return new PacketBufferImpl(new PacketBuffer(byteBuf));
    }

    @Override
    public IGuiScreen createGuiOptions(IGuiScreen iGuiScreen, IGameSettings iGameSettings) {
        GameSettings gameSettings;
        Object object = iGuiScreen;
        boolean bl = false;
        GuiScreen guiScreen = (GuiScreen)((GuiScreenImpl)object).getWrapped();
        object = iGameSettings;
        bl = false;
        GameSettings gameSettings2 = gameSettings = ((GameSettingsImpl)object).getWrapped();
        GuiScreen guiScreen2 = guiScreen;
        GuiScreen guiScreen3 = (GuiScreen)new GuiOptions(guiScreen2, gameSettings2);
        return new GuiScreenImpl(guiScreen3);
    }

    @Override
    public boolean isItemBucket(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemBucket;
    }

    @Override
    public ICPacketKeepAlive createCPacketKeepAlive() {
        return new CPacketKeepAliveImpl(new CPacketKeepAlive());
    }

    @Override
    public boolean isBlockSnow(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockSnow;
    }

    @Override
    public boolean isSPacketCloseWindow(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketCloseWindow;
    }

    @Override
    public IItem createItem() {
        return new ItemImpl(new Item());
    }

    @Override
    public IEntity createEntityLightningBolt(IWorld iWorld, double d, double d2, double d3, boolean bl) {
        IWorld iWorld2 = iWorld;
        boolean bl2 = false;
        World world = ((WorldImpl)iWorld2).getWrapped();
        boolean bl3 = bl;
        double d4 = d3;
        double d5 = d2;
        double d6 = d;
        World world2 = world;
        iWorld2 = (Entity)new EntityLightningBolt(world2, d6, d5, d4, bl3);
        bl2 = false;
        return new EntityImpl((Entity)iWorld2);
    }

    @Override
    public boolean isCPacketConfirmTransaction(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketConfirmTransaction;
    }

    @Override
    public IVertexBuffer createSafeVertexBuffer(IVertexFormat iVertexFormat) {
        VertexFormat vertexFormat;
        IVertexFormat iVertexFormat2 = iVertexFormat;
        boolean bl = false;
        VertexFormat vertexFormat2 = vertexFormat = ((VertexFormatImpl)iVertexFormat2).getWrapped();
        iVertexFormat2 = new SafeVertexBuffer(vertexFormat2);
        bl = false;
        return new VertexBufferImpl((VertexBuffer)iVertexFormat2);
    }

    @Override
    public boolean isEntityDragon(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityDragon;
    }

    @Override
    public boolean isEntityGhast(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityGhast;
    }

    @Override
    public boolean isGuiChat(@Nullable Object object) {
        return object instanceof GuiImpl && ((GuiImpl)object).getWrapped() instanceof GuiChat;
    }

    @Override
    public boolean isItemFishingRod(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemFishingRod;
    }

    @Override
    public boolean isBlockPane(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockPane;
    }

    @Override
    public boolean isBlockCactus(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockCactus;
    }

    @Override
    public ICPacketClientStatus createCPacketClientStatus(ICPacketClientStatus.WEnumState wEnumState) {
        CPacketClientStatus.State state;
        CPacketClientStatus.State state2;
        ICPacketClientStatus.WEnumState wEnumState2 = wEnumState;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$12[wEnumState2.ordinal()]) {
            case 1: {
                state2 = CPacketClientStatus.State.PERFORM_RESPAWN;
                break;
            }
            case 2: {
                state2 = CPacketClientStatus.State.REQUEST_STATS;
                break;
            }
            case 3: {
                Backend backend = Backend.INSTANCE;
                boolean bl2 = false;
                throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        CPacketClientStatus.State state3 = state = state2;
        CPacketClientStatus cPacketClientStatus = new CPacketClientStatus(state3);
        return new CPacketClientStatusImpl(cPacketClientStatus);
    }

    @Override
    public IJsonToNBT getJsonToNBTInstance() {
        return JsonToNBTImpl.INSTANCE;
    }

    @Override
    public boolean isGuiIngameMenu(@Nullable Object object) {
        return object instanceof GuiImpl && ((GuiImpl)object).getWrapped() instanceof GuiIngameMenu;
    }

    @Override
    public IDynamicTexture createDynamicTexture(BufferedImage bufferedImage) {
        return new DynamicTextureImpl(new DynamicTexture(bufferedImage));
    }

    @Override
    public IGuiTextField createGuiPasswordField(int n, IFontRenderer iFontRenderer, int n2, int n3, int n4, int n5) {
        IFontRenderer iFontRenderer2 = iFontRenderer;
        int n6 = n;
        boolean bl = false;
        FontRenderer fontRenderer = ((FontRendererImpl)iFontRenderer2).getWrapped();
        int n7 = n5;
        int n8 = n4;
        int n9 = n3;
        int n10 = n2;
        FontRenderer fontRenderer2 = fontRenderer;
        int n11 = n6;
        GuiTextField guiTextField = new GuiPasswordField(n11, fontRenderer2, n10, n9, n8, n7);
        return new GuiTextFieldImpl(guiTextField);
    }

    @Override
    public boolean isItemSword(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemSword;
    }

    @Override
    public IVertexFormat getVertexFormatEnum(WDefaultVertexFormats wDefaultVertexFormats) {
        VertexFormat vertexFormat;
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$8[wDefaultVertexFormats.ordinal()]) {
            case 1: {
                vertexFormat = DefaultVertexFormats.field_181705_e;
                break;
            }
            case 2: {
                vertexFormat = DefaultVertexFormats.field_181707_g;
                break;
            }
            case 3: {
                vertexFormat = DefaultVertexFormats.field_181706_f;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return new VertexFormatImpl(vertexFormat);
    }

    @Override
    public IGuiScreen createGuiMultiplayer(IGuiScreen iGuiScreen) {
        GuiScreen guiScreen;
        IGuiScreen iGuiScreen2 = iGuiScreen;
        boolean bl = false;
        GuiScreen guiScreen2 = guiScreen = (GuiScreen)((GuiScreenImpl)iGuiScreen2).getWrapped();
        GuiScreen guiScreen3 = (GuiScreen)new GuiMultiplayer(guiScreen2);
        return new GuiScreenImpl(guiScreen3);
    }

    @Override
    public boolean isBlockFence(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockFence;
    }

    @Override
    public boolean isEntityMob(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityMob;
    }

    @Override
    public boolean isEntityMinecart(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityMinecart;
    }

    @Override
    public boolean isItemBoat(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemBoat;
    }

    @Override
    public boolean isEntityGolem(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityGolem;
    }

    @Override
    public boolean isEntityItem(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityItem;
    }

    @Override
    public boolean isEntityFireball(@Nullable Object object) {
        String string = "Not yet implemented";
        boolean bl = false;
        throw (Throwable)new NotImplementedError("An operation is not implemented: " + string);
    }

    @Override
    public boolean isBlockBush(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockBush;
    }

    @Override
    public boolean isEntityArrow(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityArrow;
    }

    @Override
    public boolean isCPacketHandshake(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof C00Handshake;
    }

    @Override
    public IResourceLocation createResourceLocation(String string) {
        return new ResourceLocationImpl(new ResourceLocation(string));
    }

    @Override
    public ISession createSession(String string, String string2, String string3, String string4) {
        return new SessionImpl(new Session(string, string2, string3, string4));
    }

    @Override
    public boolean isCPacketEntityAction(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketEntityAction;
    }

    @Override
    public boolean isTileEntityDispenser(@Nullable Object object) {
        return object instanceof TileEntityImpl && ((TileEntityImpl)object).getWrapped() instanceof TileEntityDispenser;
    }

    @Override
    public boolean isCPacketTryUseItem(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketPlayerTryUseItem;
    }

    @Override
    public boolean isItemBlock(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemBlock;
    }

    @Override
    public boolean isItemEgg(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemEgg;
    }

    @Override
    public void wrapCreativeTab(String string, WrappedCreativeTabs wrappedCreativeTabs) {
        wrappedCreativeTabs.setRepresentedType(new CreativeTabsImpl(new CreativeTabsWrapper(wrappedCreativeTabs, string)));
    }

    @Override
    public IGuiScreen createGuiModList(IGuiScreen iGuiScreen) {
        GuiScreen guiScreen;
        IGuiScreen iGuiScreen2 = iGuiScreen;
        boolean bl = false;
        GuiScreen guiScreen2 = guiScreen = (GuiScreen)((GuiScreenImpl)iGuiScreen2).getWrapped();
        GuiScreen guiScreen3 = (GuiScreen)new GuiModList(guiScreen2);
        return new GuiScreenImpl(guiScreen3);
    }

    @Override
    public IItemStack createItemStack(IItem iItem) {
        Item item;
        IItem iItem2 = iItem;
        boolean bl = false;
        Item item2 = item = ((ItemImpl)iItem2).getWrapped();
        ItemStack itemStack = new ItemStack(item2);
        return new ItemStackImpl(itemStack);
    }

    @Override
    public ICPacketPlayer createCPacketPlayerLook(float f, float f2, boolean bl) {
        return new CPacketPlayerImpl((CPacketPlayer)new CPacketPlayer.Rotation(f, f2, bl));
    }

    @Override
    public boolean isSPacketTimeUpdate(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketTimeUpdate;
    }

    @Override
    public boolean isCPacketHeldItemChange(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketHeldItemChange;
    }

    @Override
    public boolean isBlockBedrock(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped().equals(Blocks.field_150357_h);
    }

    @Override
    public boolean isSPacketPlayerPosLook(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof SPacketPlayerPosLook;
    }

    @Override
    public boolean isEntityPlayer(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityPlayer;
    }

    @Override
    public ICPacketPlayer createCPacketPlayerPosition(double d, double d2, double d3, boolean bl) {
        return new CPacketPlayerImpl((CPacketPlayer)new CPacketPlayer.Position(d, d2, d3, bl));
    }

    @Override
    public IAxisAlignedBB createAxisAlignedBB(double d, double d2, double d3, double d4, double d5, double d6) {
        return new AxisAlignedBBImpl(new AxisAlignedBB(d, d2, d3, d4, d5, d6));
    }

    @Override
    public IGuiScreen wrapGuiScreen(WrappedGuiScreen wrappedGuiScreen) {
        GuiScreenImpl guiScreenImpl = new GuiScreenImpl(new GuiScreenWrapper(wrappedGuiScreen));
        wrappedGuiScreen.setRepresentedScreen(guiScreenImpl);
        return guiScreenImpl;
    }

    @Override
    public boolean isCPacketPlayerLook(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketPlayer.Rotation;
    }

    @Override
    public boolean isBlockStairs(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockStairs;
    }

    @Override
    public ICPacketEntityAction createCPacketEntityAction(IEntity iEntity, ICPacketEntityAction.WAction wAction) {
        CPacketEntityAction.Action action;
        CPacketEntityAction.Action action2;
        Object object = iEntity;
        boolean bl = false;
        Entity entity = ((EntityImpl)object).getWrapped();
        object = wAction;
        bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[((Enum)object).ordinal()]) {
            case 1: {
                action2 = CPacketEntityAction.Action.START_SNEAKING;
                break;
            }
            case 2: {
                action2 = CPacketEntityAction.Action.STOP_SNEAKING;
                break;
            }
            case 3: {
                action2 = CPacketEntityAction.Action.STOP_SLEEPING;
                break;
            }
            case 4: {
                action2 = CPacketEntityAction.Action.START_SPRINTING;
                break;
            }
            case 5: {
                action2 = CPacketEntityAction.Action.STOP_SPRINTING;
                break;
            }
            case 6: {
                action2 = CPacketEntityAction.Action.OPEN_INVENTORY;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        CPacketEntityAction.Action action3 = action = action2;
        Entity entity2 = entity;
        CPacketEntityAction cPacketEntityAction = new CPacketEntityAction(entity2, action3);
        return new CPacketEntityActionImpl(cPacketEntityAction);
    }

    @Override
    public boolean isEntityFallingBlock(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityFallingBlock;
    }

    @Override
    public boolean isEntityMinecartChest(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityMinecartChest;
    }

    @Override
    public IItemStack createItemStack(IItem iItem, int n, int n2) {
        IItem iItem2 = iItem;
        boolean bl = false;
        Item item = ((ItemImpl)iItem2).getWrapped();
        int n3 = n2;
        int n4 = n;
        Item item2 = item;
        ItemStack itemStack = new ItemStack(item2, n4, n3);
        return new ItemStackImpl(itemStack);
    }

    @Override
    public boolean isEntityBat(@Nullable Object object) {
        return object instanceof EntityImpl && ((EntityImpl)object).getWrapped() instanceof EntityBat;
    }

    @Override
    public ICPacketPlayerPosLook createCPacketPlayerPosLook(double d, double d2, double d3, float f, float f2, boolean bl) {
        return new CPacketPlayerPosLookImpl(new CPacketPlayer.PositionRotation(d, d2, d3, f, f2, bl));
    }

    @Override
    public INBTTagString createNBTTagString(String string) {
        return new NBTTagStringImpl(new NBTTagString(string));
    }

    @Override
    public boolean isBlockAir(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).getWrapped() instanceof BlockAir;
    }

    @Override
    public IPotionEffect createPotionEffect(int n, int n2, int n3) {
        return new PotionEffectImpl(new PotionEffect(Potion.func_188412_a((int)n), n2, n3));
    }

    @Override
    public boolean isItemSnowball(@Nullable Object object) {
        return object instanceof ItemImpl && ((ItemImpl)object).getWrapped() instanceof ItemSnowball;
    }

    @Override
    public boolean isCPacketPlayerBlockPlacement(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).getWrapped() instanceof CPacketPlayerTryUseItemOnBlock;
    }

    @Override
    public IGlStateManager getGlStateManager() {
        return GlStateManagerImpl.INSTANCE;
    }

    @Override
    public IEnumFacing getEnumFacing(EnumFacingType enumFacingType) {
        EnumFacing enumFacing;
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$2[enumFacingType.ordinal()]) {
            case 1: {
                enumFacing = EnumFacing.DOWN;
                break;
            }
            case 2: {
                enumFacing = EnumFacing.UP;
                break;
            }
            case 3: {
                enumFacing = EnumFacing.NORTH;
                break;
            }
            case 4: {
                enumFacing = EnumFacing.SOUTH;
                break;
            }
            case 5: {
                enumFacing = EnumFacing.WEST;
                break;
            }
            case 6: {
                enumFacing = EnumFacing.EAST;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return new EnumFacingImpl(enumFacing);
    }
}

