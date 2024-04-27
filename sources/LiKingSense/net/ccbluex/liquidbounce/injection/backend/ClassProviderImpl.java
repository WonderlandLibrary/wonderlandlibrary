/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  io.netty.buffer.ByteBuf
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.NotImplementedError
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockBush
 *  net.minecraft.block.BlockCactus
 *  net.minecraft.block.BlockCarpet
 *  net.minecraft.block.BlockChest
 *  net.minecraft.block.BlockDynamicLiquid
 *  net.minecraft.block.BlockFence
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLadder
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockPane
 *  net.minecraft.block.BlockSand
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockSlime
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.block.BlockStaticLiquid
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
 *  net.minecraft.network.play.server.SPacketWindowItems
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.stats.StatBase
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
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.NotImplementedError;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
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
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStaticLiquid;
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
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u00cc\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\ba\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J8\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u000eH\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0018\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\"H\u0016J\u0018\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020(H\u0016J \u0010)\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0016J\u0018\u00100\u001a\u0002012\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u000205H\u0016J\u0010\u00106\u001a\u0002072\u0006\u0010 \u001a\u00020\u001dH\u0016J\b\u00108\u001a\u000209H\u0016J\u0010\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020=H\u0016J\u0012\u0010>\u001a\u00020?2\b\u0010@\u001a\u0004\u0018\u00010\"H\u0016J:\u0010>\u001a\u00020?2\u0006\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020\u001d2\b\u0010D\u001a\u0004\u0018\u00010\"2\u0006\u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020F2\u0006\u0010H\u001a\u00020FH\u0016J \u0010I\u001a\u00020\u001f2\u0006\u00104\u001a\u00020J2\u0006\u0010K\u001a\u00020B2\u0006\u0010L\u001a\u00020MH\u0016J \u0010N\u001a\u00020;2\u0006\u0010O\u001a\u00020F2\u0006\u0010P\u001a\u00020F2\u0006\u0010<\u001a\u00020=H\u0016J8\u0010Q\u001a\u00020R2\u0006\u0010S\u001a\u00020\u000e2\u0006\u0010T\u001a\u00020\u000e2\u0006\u0010U\u001a\u00020\u000e2\u0006\u0010O\u001a\u00020F2\u0006\u0010P\u001a\u00020F2\u0006\u0010<\u001a\u00020=H\u0016J(\u0010V\u001a\u00020;2\u0006\u0010S\u001a\u00020\u000e2\u0006\u0010T\u001a\u00020\u000e2\u0006\u0010U\u001a\u00020\u000e2\u0006\u0010<\u001a\u00020=H\u0016J\u0010\u0010W\u001a\u00020\u001f2\u0006\u0010X\u001a\u00020&H\u0016J\u0014\u0010Y\u001a\u0006\u0012\u0002\b\u00030Z2\u0006\u0010[\u001a\u00020\\H\u0016J\u0018\u0010]\u001a\u00020^2\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u00020_H\u0016J\u0018\u0010]\u001a\u00020^2\u0006\u0010`\u001a\u0002032\u0006\u0010a\u001a\u00020bH\u0016J\u0010\u0010c\u001a\u00020d2\u0006\u0010X\u001a\u00020&H\u0016J\u0018\u0010e\u001a\u00020f2\u0006\u0010g\u001a\u00020h2\u0006\u0010i\u001a\u00020&H\u0016J\u0010\u0010j\u001a\u00020k2\u0006\u0010l\u001a\u00020mH\u0016J0\u0010n\u001a\u0002032\u0006\u0010o\u001a\u00020p2\u0006\u0010q\u001a\u00020\u000e2\u0006\u0010r\u001a\u00020\u000e2\u0006\u0010s\u001a\u00020\u000e2\u0006\u0010t\u001a\u00020=H\u0016J\u0018\u0010u\u001a\u00020v2\u0006\u0010o\u001a\u00020w2\u0006\u0010x\u001a\u00020yH\u0016J8\u0010z\u001a\u00020{2\u0006\u0010|\u001a\u00020\u001d2\u0006\u0010S\u001a\u00020\u001d2\u0006\u0010T\u001a\u00020\u001d2\u0006\u0010}\u001a\u00020\u001d2\u0006\u0010~\u001a\u00020\u001d2\u0006\u0010X\u001a\u00020&H\u0016J(\u0010z\u001a\u00020{2\u0006\u0010|\u001a\u00020\u001d2\u0006\u0010S\u001a\u00020\u001d2\u0006\u0010T\u001a\u00020\u001d2\u0006\u0010X\u001a\u00020&H\u0016J'\u0010\u007f\u001a\u00030\u0080\u00012\b\u0010\u0081\u0001\u001a\u00030\u0080\u00012\b\u0010\u0082\u0001\u001a\u00030\u0083\u00012\b\u0010\u0084\u0001\u001a\u00030\u0085\u0001H\u0016J\u0014\u0010\u0086\u0001\u001a\u00030\u0080\u00012\b\u0010\u0087\u0001\u001a\u00030\u0080\u0001H\u0016J\u0014\u0010\u0088\u0001\u001a\u00030\u0080\u00012\b\u0010\u0087\u0001\u001a\u00030\u0080\u0001H\u0016J\u001e\u0010\u0089\u0001\u001a\u00030\u0080\u00012\b\u0010\u0087\u0001\u001a\u00030\u0080\u00012\b\u0010\u008a\u0001\u001a\u00030\u008b\u0001H\u0016J<\u0010\u008c\u0001\u001a\u00030\u008d\u00012\u0006\u0010|\u001a\u00020\u001d2\b\u0010\u008e\u0001\u001a\u00030\u008f\u00012\u0006\u0010S\u001a\u00020\u001d2\u0006\u0010T\u001a\u00020\u001d2\u0006\u0010}\u001a\u00020\u001d2\u0006\u0010~\u001a\u00020\u001dH\u0016J\u0014\u0010\u0090\u0001\u001a\u00030\u0080\u00012\b\u0010\u0087\u0001\u001a\u00030\u0080\u0001H\u0016J<\u0010\u0091\u0001\u001a\u00030\u008d\u00012\u0006\u0010|\u001a\u00020\u001d2\b\u0010\u008e\u0001\u001a\u00030\u008f\u00012\u0006\u0010S\u001a\u00020\u001d2\u0006\u0010T\u001a\u00020\u001d2\u0006\u0010}\u001a\u00020\u001d2\u0006\u0010~\u001a\u00020\u001dH\u0016J\u001c\u0010\u0092\u0001\u001a\u00020\u001f2\u0007\u0010\u0093\u0001\u001a\u00020&2\b\u0010\u0094\u0001\u001a\u00030\u0095\u0001H\u0016J\n\u0010\u0096\u0001\u001a\u00030\u0097\u0001H\u0016J\u0013\u0010\u0098\u0001\u001a\u00020\"2\b\u0010\u0099\u0001\u001a\u00030\u009a\u0001H\u0016J\u0013\u0010\u0098\u0001\u001a\u00020\"2\b\u0010\u009b\u0001\u001a\u00030\u0097\u0001H\u0016J%\u0010\u0098\u0001\u001a\u00020\"2\b\u0010\u009b\u0001\u001a\u00030\u0097\u00012\u0007\u0010\u009c\u0001\u001a\u00020\u001d2\u0007\u0010\u009d\u0001\u001a\u00020\u001dH\u0016J\n\u0010\u009e\u0001\u001a\u00030\u009f\u0001H\u0016J\u0012\u0010\u00a0\u0001\u001a\u00030\u00a1\u00012\u0006\u0010i\u001a\u00020\u000eH\u0016J\n\u0010\u00a2\u0001\u001a\u00030\u00a3\u0001H\u0016J\u0013\u0010\u00a4\u0001\u001a\u00030\u00a5\u00012\u0007\u0010\u00a6\u0001\u001a\u00020&H\u0016J\u0013\u0010\u00a7\u0001\u001a\u00020(2\b\u0010\u00a8\u0001\u001a\u00030\u00a9\u0001H\u0016J$\u0010\u00aa\u0001\u001a\u00030\u00ab\u00012\u0006\u0010|\u001a\u00020\u001d2\u0007\u0010\u00ac\u0001\u001a\u00020\u001d2\u0007\u0010\u00ad\u0001\u001a\u00020\u001dH\u0016J\u0013\u0010\u00ae\u0001\u001a\u00030\u00af\u00012\u0007\u0010\u00b0\u0001\u001a\u00020&H\u0016J\u0014\u0010\u00b1\u0001\u001a\u00030\u00b2\u00012\b\u0010\u00b3\u0001\u001a\u00030\u00b4\u0001H\u0016J\u0014\u0010\u00b5\u0001\u001a\u00030\u00b6\u00012\b\u0010\u0082\u0001\u001a\u00030\u0083\u0001H\u0016J.\u0010\u00b7\u0001\u001a\u00030\u00b8\u00012\u0007\u0010\u00b9\u0001\u001a\u00020&2\u0007\u0010\u00ba\u0001\u001a\u00020&2\u0007\u0010\u00bb\u0001\u001a\u00020&2\u0007\u0010\u00bc\u0001\u001a\u00020&H\u0016J5\u0010\u00bd\u0001\u001a\u00030\u00be\u00012\n\u0010\u00bf\u0001\u001a\u0005\u0018\u00010\u00c0\u00012\u0007\u0010\u00c1\u0001\u001a\u00020&2\n\u0010\u00c2\u0001\u001a\u0005\u0018\u00010\u00af\u00012\b\u0010\u00c3\u0001\u001a\u00030\u00c4\u0001H\u0016J\u0014\u0010\u00c5\u0001\u001a\u00030\u009a\u00012\b\u0010\u00c6\u0001\u001a\u00030\u00c7\u0001H\u0016J\u0014\u0010\u00c8\u0001\u001a\u00030\u00c9\u00012\b\u0010\u00c6\u0001\u001a\u00030\u00ca\u0001H\u0016J\u0013\u0010\u00cb\u0001\u001a\u00020M2\b\u0010\u00c6\u0001\u001a\u00030\u00cc\u0001H\u0016J\n\u0010\u00cd\u0001\u001a\u00030\u00ce\u0001H\u0016J\u0014\u0010\u00cf\u0001\u001a\u00030\u0097\u00012\b\u0010\u00c6\u0001\u001a\u00030\u00d0\u0001H\u0016J\u0014\u0010\u00d1\u0001\u001a\u00030\u00d2\u00012\b\u0010\u00c6\u0001\u001a\u00030\u00d3\u0001H\u0016J\u0014\u0010\u00d4\u0001\u001a\u00030\u00d5\u00012\b\u0010\u00c6\u0001\u001a\u00030\u00d6\u0001H\u0016J\u0014\u0010\u00d7\u0001\u001a\u00030\u00d8\u00012\b\u0010\u00c6\u0001\u001a\u00030\u00d9\u0001H\u0016J\u0014\u0010\u00da\u0001\u001a\u00030\u00b4\u00012\b\u0010\u00c6\u0001\u001a\u00030\u00db\u0001H\u0016J\u0015\u0010\u00dc\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00df\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e0\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e1\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e2\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e3\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e4\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e5\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e6\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e7\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e8\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00e9\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ea\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00eb\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ec\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ed\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ee\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ef\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f0\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f1\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f2\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f3\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f4\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f5\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f6\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f7\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f8\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00f9\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00fa\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00fb\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00fc\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00fd\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00fe\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ff\u0001\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0080\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0081\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0082\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0083\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0084\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0085\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0086\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0087\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0088\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0089\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u008a\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u008b\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u008c\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u008d\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u008e\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u008f\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0090\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0091\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0092\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0093\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0094\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0095\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0096\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0097\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0098\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u0099\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u009a\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u009b\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u009c\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u009d\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u009e\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u009f\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a0\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a1\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a2\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a3\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a4\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a5\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a6\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a7\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a8\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00a9\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00aa\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ab\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ac\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ad\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ae\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00af\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b0\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b1\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b2\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b3\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b4\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b5\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b6\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b7\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b8\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00b9\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00ba\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00bb\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00bc\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00bd\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u0015\u0010\u00be\u0002\u001a\u00020=2\n\u0010\u00dd\u0001\u001a\u0005\u0018\u00010\u00de\u0001H\u0016J\u001d\u0010\u00bf\u0002\u001a\u00030\u00c0\u00022\u0007\u0010\u00b9\u0001\u001a\u00020&2\b\u0010\u00c1\u0002\u001a\u00030\u00c2\u0002H\u0016J\u0014\u0010\u00c3\u0002\u001a\u00030\u008f\u00012\b\u0010\u00c4\u0002\u001a\u00030\u00c5\u0002H\u0016J\u0014\u0010\u00c6\u0002\u001a\u00030\u0080\u00012\b\u0010\u00c7\u0002\u001a\u00030\u00c8\u0002H\u0016JI\u0010\u00c9\u0002\u001a\u00030\u00c0\u00022\b\u0010\u00ca\u0002\u001a\u00030\u00cb\u00022\b\u0010\u0082\u0001\u001a\u00030\u0083\u00012\u0006\u0010}\u001a\u00020\u001d2\u0006\u0010~\u001a\u00020\u001d2\u0007\u0010\u00cc\u0002\u001a\u00020\u001d2\u0007\u0010\u00cd\u0002\u001a\u00020\u001d2\u0007\u0010\u00ce\u0002\u001a\u00020\u001dH\u0016R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u00cf\u0002"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ClassProviderImpl;", "Lnet/ccbluex/liquidbounce/api/IClassProvider;", "()V", "jsonToNBTInstance", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/IJsonToNBT;", "getJsonToNBTInstance", "()Lnet/ccbluex/liquidbounce/api/minecraft/nbt/IJsonToNBT;", "tessellatorInstance", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/ITessellator;", "getTessellatorInstance", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/ITessellator;", "createAxisAlignedBB", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "minX", "", "minY", "minZ", "maxX", "maxY", "maxZ", "createCPacketAnimation", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketAnimation;", "createCPacketClientStatus", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus;", "state", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus$WEnumState;", "createCPacketCloseWindow", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCloseWindow;", "windowId", "", "createCPacketCreativeInventoryAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "slot", "itemStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "createCPacketCustomPayload", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCustomPayload;", "channel", "", "payload", "Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;", "createCPacketEncryptionResponse", "secretKey", "Ljavax/crypto/SecretKey;", "publicKey", "Ljava/security/PublicKey;", "verifyToken", "", "createCPacketEntityAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction;", "player", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "wAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction$WAction;", "createCPacketHeldItemChange", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketHeldItemChange;", "createCPacketKeepAlive", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketKeepAlive;", "createCPacketPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "onGround", "", "createCPacketPlayerBlockPlacement", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerBlockPlacement;", "stack", "positionIn", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "placedBlockDirectionIn", "stackIn", "facingXIn", "", "facingYIn", "facingZIn", "createCPacketPlayerDigging", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "pos", "facing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "createCPacketPlayerLook", "yaw", "pitch", "createCPacketPlayerPosLook", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerPosLook;", "x", "y", "z", "createCPacketPlayerPosition", "createCPacketTabComplete", "text", "createCPacketTryUseItem", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "hand", "Lnet/ccbluex/liquidbounce/api/enums/WEnumHand;", "createCPacketUseEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "entity", "positionVector", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "createChatComponentText", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "createClickEvent", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "action", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent$WAction;", "value", "createDynamicTexture", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/IDynamicTexture;", "image", "Ljava/awt/image/BufferedImage;", "createEntityLightningBolt", "world", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "posX", "posY", "posZ", "effectOnly", "createEntityOtherPlayerMP", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityOtherPlayerMP;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "gameProfile", "Lcom/mojang/authlib/GameProfile;", "createGuiButton", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiButton;", "id", "width", "height", "createGuiConnecting", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "parent", "mc", "Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "serverData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "createGuiModList", "parentScreen", "createGuiMultiplayer", "createGuiOptions", "gameSettings", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "createGuiPasswordField", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiTextField;", "iFontRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "createGuiSelectWorld", "createGuiTextField", "createICPacketResourcePackStatus", "hash", "status", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus$WAction;", "createItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "createItemStack", "blockEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "item", "amount", "meta", "createNBTTagCompound", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "createNBTTagDouble", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagDouble;", "createNBTTagList", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagList;", "createNBTTagString", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagString;", "string", "createPacketBuffer", "buffer", "Lio/netty/buffer/ByteBuf;", "createPotionEffect", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotionEffect;", "time", "strength", "createResourceLocation", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "resourceName", "createSafeVertexBuffer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/vertex/IVertexBuffer;", "vertexFormat", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/vertex/IVertexFormat;", "createScaledResolution", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IScaledResolution;", "createSession", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "name", "uuid", "accessToken", "accountType", "createThreadDownloadImageData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/IThreadDownloadImageData;", "cacheFileIn", "Ljava/io/File;", "imageUrlIn", "textureResourceLocation", "imageBufferIn", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/WIImageBuffer;", "getBlockEnum", "type", "Lnet/ccbluex/liquidbounce/api/enums/BlockType;", "getEnchantmentEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/enchantments/IEnchantment;", "Lnet/ccbluex/liquidbounce/api/enums/EnchantmentType;", "getEnumFacing", "Lnet/ccbluex/liquidbounce/api/enums/EnumFacingType;", "getGlStateManager", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IGlStateManager;", "getItemEnum", "Lnet/ccbluex/liquidbounce/api/enums/ItemType;", "getMaterialEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/block/material/IMaterial;", "Lnet/ccbluex/liquidbounce/api/enums/MaterialType;", "getPotionEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/PotionType;", "getStatEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/stats/IStatBase;", "Lnet/ccbluex/liquidbounce/api/enums/StatType;", "getVertexFormatEnum", "Lnet/ccbluex/liquidbounce/api/enums/WDefaultVertexFormats;", "isBlockAir", "obj", "", "isBlockBedrock", "isBlockBush", "isBlockCactus", "isBlockCarpet", "isBlockFence", "isBlockLadder", "isBlockLiquid", "isBlockPane", "isBlockSlab", "isBlockSlime", "isBlockSnow", "isBlockStairs", "isBlockVine", "isCPacketAnimation", "isCPacketChatMessage", "isCPacketClientStatus", "isCPacketCloseWindow", "isCPacketConfirmTransaction", "isCPacketCustomPayload", "isCPacketEntityAction", "isCPacketHandshake", "isCPacketHeldItemChange", "isCPacketKeepAlive", "isCPacketPlayer", "isCPacketPlayerBlockPlacement", "isCPacketPlayerDigging", "isCPacketPlayerLook", "isCPacketPlayerPosLook", "isCPacketPlayerPosition", "isCPacketTryUseItem", "isCPacketUseEntity", "isClickGui", "isEntityAnimal", "isEntityArmorStand", "isEntityArrow", "isEntityBat", "isEntityBoat", "isEntityDragon", "isEntityFallingBlock", "isEntityGhast", "isEntityGolem", "isEntityItem", "isEntityLivingBase", "isEntityMinecart", "isEntityMinecartChest", "isEntityMob", "isEntityPlayer", "isEntityShulker", "isEntitySlime", "isEntitySquid", "isEntityTNTPrimed", "isEntityVillager", "isGuiChat", "isGuiChest", "isGuiContainer", "isGuiGameOver", "isGuiHudDesigner", "isGuiIngameMenu", "isGuiInventory", "isItemAir", "isItemAppleGold", "isItemArmor", "isItemAxe", "isItemBed", "isItemBlock", "isItemBoat", "isItemBow", "isItemBucket", "isItemBucketMilk", "isItemEgg", "isItemEnchantedBook", "isItemEnderPearl", "isItemFishingRod", "isItemFood", "isItemMinecart", "isItemPickaxe", "isItemPotion", "isItemSnowball", "isItemSword", "isItemTool", "isSPacketAnimation", "isSPacketChat", "isSPacketCloseWindow", "isSPacketEntity", "isSPacketEntityVelocity", "isSPacketExplosion", "isSPacketPlayerPosLook", "isSPacketResourcePackSend", "isSPacketTabComplete", "isSPacketWindowItems", "isTileEntityChest", "isTileEntityDispenser", "isTileEntityEnderChest", "isTileEntityFurnace", "isTileEntityHopper", "isTileEntityShulkerBox", "wrapCreativeTab", "", "wrappedCreativeTabs", "Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;", "wrapFontRenderer", "fontRenderer", "Lnet/ccbluex/liquidbounce/api/util/IWrappedFontRenderer;", "wrapGuiScreen", "clickGui", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;", "wrapGuiSlot", "wrappedGuiSlot", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiSlot;", "top", "bottom", "slotHeight", "LiKingSense"})
public final class ClassProviderImpl
implements IClassProvider {
    public static final ClassProviderImpl INSTANCE;

    @Override
    @NotNull
    public ITessellator getTessellatorInstance() {
        Tessellator tessellator = Tessellator.func_178181_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)tessellator, (String)"Tessellator.getInstance()");
        return new TessellatorImpl(tessellator);
    }

    @Override
    @NotNull
    public IJsonToNBT getJsonToNBTInstance() {
        return JsonToNBTImpl.INSTANCE;
    }

    @Override
    @NotNull
    public IResourceLocation createResourceLocation(@NotNull String resourceName) {
        Intrinsics.checkParameterIsNotNull((Object)resourceName, (String)"resourceName");
        return new ResourceLocationImpl(new ResourceLocation(resourceName));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public IThreadDownloadImageData createThreadDownloadImageData(@Nullable File cacheFileIn, @NotNull String imageUrlIn, @Nullable IResourceLocation textureResourceLocation, @NotNull WIImageBuffer imageBufferIn) {
        ResourceLocation resourceLocation;
        Intrinsics.checkParameterIsNotNull((Object)imageUrlIn, (String)"imageUrlIn");
        Intrinsics.checkParameterIsNotNull((Object)imageBufferIn, (String)"imageBufferIn");
        File file = cacheFileIn;
        String string = imageUrlIn;
        IResourceLocation iResourceLocation = textureResourceLocation;
        if (iResourceLocation != null) {
            void $this$unwrap$iv;
            IResourceLocation iResourceLocation2 = iResourceLocation;
            String string2 = string;
            File file2 = file;
            boolean $i$f$unwrap = false;
            void v3 = $this$unwrap$iv;
            if (v3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl");
            }
            ResourceLocation resourceLocation2 = ((ResourceLocationImpl)v3).getWrapped();
            file = file2;
            string = string2;
            resourceLocation = resourceLocation2;
        } else {
            resourceLocation = null;
        }
        IImageBuffer iImageBuffer = new IImageBuffer(imageBufferIn){
            final /* synthetic */ WIImageBuffer $imageBufferIn;

            @Nullable
            public BufferedImage func_78432_a(@Nullable BufferedImage image2) {
                return this.$imageBufferIn.parseUserSkin(image2);
            }

            public void func_152634_a() {
                this.$imageBufferIn.skinAvailable();
            }
            {
                this.$imageBufferIn = $captured_local_variable$0;
            }
        };
        ResourceLocation resourceLocation3 = resourceLocation;
        String string3 = string;
        File file3 = file;
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(file3, string3, resourceLocation3, iImageBuffer);
        return new ThreadDownloadImageDataImpl<ThreadDownloadImageData>(threadDownloadImageData);
    }

    @Override
    @NotNull
    public IPacketBuffer createPacketBuffer(@NotNull ByteBuf buffer) {
        Intrinsics.checkParameterIsNotNull((Object)buffer, (String)"buffer");
        return new PacketBufferImpl(new PacketBuffer(buffer));
    }

    @Override
    @NotNull
    public IIChatComponent createChatComponentText(@NotNull String text) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        return new IChatComponentImpl((ITextComponent)new TextComponentString(text));
    }

    @Override
    @NotNull
    public IClickEvent createClickEvent(@NotNull IClickEvent.WAction action, @NotNull String value) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)action), (String)"action");
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        IClickEvent.WAction $this$unwrap$iv = action;
        boolean $i$f$unwrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$11[$this$unwrap$iv.ordinal()]) {
            case 1: {
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        ClickEvent.Action action2 = ClickEvent.Action.OPEN_URL;
        String string = value;
        ClickEvent.Action action3 = action2;
        ClickEvent clickEvent = new ClickEvent(action3, string);
        return new ClickEventImpl(clickEvent);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public IGuiTextField createGuiTextField(int id, @NotNull IFontRenderer iFontRenderer, int x, int y, int width, int height) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)iFontRenderer, (String)"iFontRenderer");
        IFontRenderer iFontRenderer2 = iFontRenderer;
        int n = id;
        boolean $i$f$unwrap = false;
        FontRenderer fontRenderer = ((FontRendererImpl)$this$unwrap$iv).getWrapped();
        int n2 = height;
        int n3 = width;
        int n4 = y;
        int n5 = x;
        FontRenderer fontRenderer2 = fontRenderer;
        int n6 = n;
        GuiTextField guiTextField = new GuiTextField(n6, fontRenderer2, n5, n4, n3, n2);
        return new GuiTextFieldImpl(guiTextField);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public IGuiTextField createGuiPasswordField(int id, @NotNull IFontRenderer iFontRenderer, int x, int y, int width, int height) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)iFontRenderer, (String)"iFontRenderer");
        IFontRenderer iFontRenderer2 = iFontRenderer;
        int n = id;
        boolean $i$f$unwrap = false;
        FontRenderer fontRenderer = ((FontRendererImpl)$this$unwrap$iv).getWrapped();
        int n2 = height;
        int n3 = width;
        int n4 = y;
        int n5 = x;
        FontRenderer fontRenderer2 = fontRenderer;
        int n6 = n;
        GuiTextField guiTextField = new GuiPasswordField(n6, fontRenderer2, n5, n4, n3, n2);
        return new GuiTextFieldImpl(guiTextField);
    }

    @Override
    @NotNull
    public IGuiButton createGuiButton(int id, int x, int y, int width, int height, @NotNull String text) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        return new GuiButtonImpl(new GuiButton(id, x, y, width, height, text));
    }

    @Override
    @NotNull
    public IGuiButton createGuiButton(int id, int x, int y, @NotNull String text) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        return new GuiButtonImpl(new GuiButton(id, x, y, text));
    }

    @Override
    @NotNull
    public ISession createSession(@NotNull String name, @NotNull String uuid, @NotNull String accessToken, @NotNull String accountType) {
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        Intrinsics.checkParameterIsNotNull((Object)uuid, (String)"uuid");
        Intrinsics.checkParameterIsNotNull((Object)accessToken, (String)"accessToken");
        Intrinsics.checkParameterIsNotNull((Object)accountType, (String)"accountType");
        return new SessionImpl(new Session(name, uuid, accessToken, accountType));
    }

    @Override
    @NotNull
    public IDynamicTexture createDynamicTexture(@NotNull BufferedImage image2) {
        Intrinsics.checkParameterIsNotNull((Object)image2, (String)"image");
        return new DynamicTextureImpl<DynamicTexture>(new DynamicTexture(image2));
    }

    @Override
    @NotNull
    public IItem createItem() {
        return new ItemImpl<Item>(new Item());
    }

    @Override
    @NotNull
    public IItemStack createItemStack(@NotNull IItem item, int amount, int meta) {
        Intrinsics.checkParameterIsNotNull((Object)item, (String)"item");
        IItem $this$unwrap$iv = item;
        boolean $i$f$unwrap = false;
        Object t2 = ((ItemImpl)$this$unwrap$iv).getWrapped();
        int n = meta;
        int n2 = amount;
        Object t3 = t2;
        ItemStack itemStack = new ItemStack(t3, n2, n);
        return new ItemStackImpl(itemStack);
    }

    @Override
    @NotNull
    public IEntity createEntityLightningBolt(@NotNull IWorld world, double posX, double posY, double posZ, boolean effectOnly) {
        Intrinsics.checkParameterIsNotNull((Object)world, (String)"world");
        IWorld $this$unwrap$iv = world;
        boolean $i$f$unwrap = false;
        Object t2 = ((WorldImpl)$this$unwrap$iv).getWrapped();
        boolean bl = effectOnly;
        double d = posZ;
        double d2 = posY;
        double d3 = posX;
        Object t3 = t2;
        Entity $this$wrap$iv = (Entity)new EntityLightningBolt(t3, d3, d2, d, bl);
        boolean $i$f$wrap = false;
        return new EntityImpl<Entity>($this$wrap$iv);
    }

    @Override
    @NotNull
    public IItemStack createItemStack(@NotNull IItem item) {
        Object t2;
        Intrinsics.checkParameterIsNotNull((Object)item, (String)"item");
        IItem $this$unwrap$iv = item;
        boolean $i$f$unwrap = false;
        Object t3 = t2 = ((ItemImpl)$this$unwrap$iv).getWrapped();
        ItemStack itemStack = new ItemStack(t3);
        return new ItemStackImpl(itemStack);
    }

    @Override
    @NotNull
    public IItemStack createItemStack(@NotNull IBlock blockEnum) {
        Block block;
        Intrinsics.checkParameterIsNotNull((Object)blockEnum, (String)"blockEnum");
        IBlock $this$unwrap$iv = blockEnum;
        boolean $i$f$unwrap = false;
        Block block2 = block = ((BlockImpl)$this$unwrap$iv).getWrapped();
        ItemStack itemStack = new ItemStack(block2);
        return new ItemStackImpl(itemStack);
    }

    @Override
    @NotNull
    public IAxisAlignedBB createAxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new AxisAlignedBBImpl(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
    }

    @Override
    @NotNull
    public IScaledResolution createScaledResolution(@NotNull IMinecraft mc) {
        Minecraft minecraft;
        Intrinsics.checkParameterIsNotNull((Object)mc, (String)"mc");
        IMinecraft $this$unwrap$iv = mc;
        boolean $i$f$unwrap = false;
        Minecraft minecraft2 = minecraft = ((MinecraftImpl)$this$unwrap$iv).getWrapped();
        ScaledResolution scaledResolution = new ScaledResolution(minecraft2);
        return new ScaledResolutionImpl(scaledResolution);
    }

    @Override
    @NotNull
    public INBTTagCompound createNBTTagCompound() {
        return new NBTTagCompoundImpl(new NBTTagCompound());
    }

    @Override
    @NotNull
    public INBTTagList createNBTTagList() {
        return new NBTTagListImpl(new NBTTagList());
    }

    @Override
    @NotNull
    public INBTTagString createNBTTagString(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull((Object)string, (String)"string");
        return new NBTTagStringImpl<NBTTagString>(new NBTTagString(string));
    }

    @Override
    @NotNull
    public INBTTagDouble createNBTTagDouble(double value) {
        return new NBTTagDoubleImpl<NBTTagDouble>(new NBTTagDouble(value));
    }

    @Override
    @NotNull
    public IEntityOtherPlayerMP createEntityOtherPlayerMP(@NotNull IWorldClient world, @NotNull GameProfile gameProfile) {
        Intrinsics.checkParameterIsNotNull((Object)world, (String)"world");
        Intrinsics.checkParameterIsNotNull((Object)gameProfile, (String)"gameProfile");
        IWorldClient $this$unwrap$iv = world;
        boolean $i$f$unwrap = false;
        WorldClient worldClient = (WorldClient)((WorldClientImpl)$this$unwrap$iv).getWrapped();
        GameProfile gameProfile2 = gameProfile;
        World world2 = (World)worldClient;
        EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(world2, gameProfile2);
        return new EntityOtherPlayerMPImpl<EntityOtherPlayerMP>(entityOtherPlayerMP);
    }

    @Override
    @NotNull
    public IPotionEffect createPotionEffect(int id, int time, int strength) {
        return new PotionEffectImpl(new PotionEffect(Potion.func_188412_a((int)id), time, strength));
    }

    @Override
    @NotNull
    public IGuiScreen createGuiOptions(@NotNull IGuiScreen parentScreen, @NotNull IGameSettings gameSettings) {
        GameSettings gameSettings2;
        Intrinsics.checkParameterIsNotNull((Object)parentScreen, (String)"parentScreen");
        Intrinsics.checkParameterIsNotNull((Object)gameSettings, (String)"gameSettings");
        Object $this$unwrap$iv = parentScreen;
        boolean $i$f$unwrap = false;
        GuiScreen guiScreen = (GuiScreen)((GuiScreenImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = gameSettings;
        $i$f$unwrap = false;
        GameSettings gameSettings3 = gameSettings2 = ((GameSettingsImpl)$this$unwrap$iv).getWrapped();
        GuiScreen guiScreen2 = guiScreen;
        GuiScreen guiScreen3 = (GuiScreen)new GuiOptions(guiScreen2, gameSettings3);
        return new GuiScreenImpl<GuiScreen>(guiScreen3);
    }

    @Override
    @NotNull
    public IGuiScreen createGuiSelectWorld(@NotNull IGuiScreen parentScreen) {
        GuiScreen guiScreen;
        Intrinsics.checkParameterIsNotNull((Object)parentScreen, (String)"parentScreen");
        IGuiScreen $this$unwrap$iv = parentScreen;
        boolean $i$f$unwrap = false;
        GuiScreen guiScreen2 = guiScreen = (GuiScreen)((GuiScreenImpl)$this$unwrap$iv).getWrapped();
        GuiScreen guiScreen3 = (GuiScreen)new GuiWorldSelection(guiScreen2);
        return new GuiScreenImpl<GuiScreen>(guiScreen3);
    }

    @Override
    @NotNull
    public IGuiScreen createGuiMultiplayer(@NotNull IGuiScreen parentScreen) {
        GuiScreen guiScreen;
        Intrinsics.checkParameterIsNotNull((Object)parentScreen, (String)"parentScreen");
        IGuiScreen $this$unwrap$iv = parentScreen;
        boolean $i$f$unwrap = false;
        GuiScreen guiScreen2 = guiScreen = (GuiScreen)((GuiScreenImpl)$this$unwrap$iv).getWrapped();
        GuiScreen guiScreen3 = (GuiScreen)new GuiMultiplayer(guiScreen2);
        return new GuiScreenImpl<GuiScreen>(guiScreen3);
    }

    @Override
    @NotNull
    public IGuiScreen createGuiModList(@NotNull IGuiScreen parentScreen) {
        GuiScreen guiScreen;
        Intrinsics.checkParameterIsNotNull((Object)parentScreen, (String)"parentScreen");
        IGuiScreen $this$unwrap$iv = parentScreen;
        boolean $i$f$unwrap = false;
        GuiScreen guiScreen2 = guiScreen = (GuiScreen)((GuiScreenImpl)$this$unwrap$iv).getWrapped();
        GuiScreen guiScreen3 = (GuiScreen)new GuiModList(guiScreen2);
        return new GuiScreenImpl<GuiScreen>(guiScreen3);
    }

    @Override
    @NotNull
    public IGuiScreen createGuiConnecting(@NotNull IGuiScreen parent, @NotNull IMinecraft mc, @NotNull IServerData serverData) {
        ServerData serverData2;
        Intrinsics.checkParameterIsNotNull((Object)parent, (String)"parent");
        Intrinsics.checkParameterIsNotNull((Object)mc, (String)"mc");
        Intrinsics.checkParameterIsNotNull((Object)serverData, (String)"serverData");
        Object $this$unwrap$iv = parent;
        boolean $i$f$unwrap = false;
        GuiScreen guiScreen = (GuiScreen)((GuiScreenImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = mc;
        $i$f$unwrap = false;
        Minecraft minecraft = ((MinecraftImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = serverData;
        $i$f$unwrap = false;
        ServerData serverData3 = serverData2 = ((ServerDataImpl)$this$unwrap$iv).getWrapped();
        Minecraft minecraft2 = minecraft;
        GuiScreen guiScreen2 = guiScreen;
        GuiScreen guiScreen3 = (GuiScreen)new GuiConnecting(guiScreen2, minecraft2, serverData3);
        return new GuiScreenImpl<GuiScreen>(guiScreen3);
    }

    @Override
    @NotNull
    public ICPacketHeldItemChange createCPacketHeldItemChange(int slot) {
        return new CPacketHeldItemChangeImpl<CPacketHeldItemChange>(new CPacketHeldItemChange(slot));
    }

    @Override
    @NotNull
    public ICPacketPlayerBlockPlacement createCPacketPlayerBlockPlacement(@Nullable IItemStack stack) {
        Backend this_$iv = Backend.INSTANCE;
        boolean $i$f$BACKEND_UNSUPPORTED = false;
        throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
    }

    @Override
    @NotNull
    public ICPacketPlayerBlockPlacement createCPacketPlayerBlockPlacement(@NotNull WBlockPos positionIn, int placedBlockDirectionIn, @Nullable IItemStack stackIn, float facingXIn, float facingYIn, float facingZIn) {
        Intrinsics.checkParameterIsNotNull((Object)positionIn, (String)"positionIn");
        WBlockPos $this$unwrap$iv = positionIn;
        boolean $i$f$unwrap = false;
        BlockPos blockPos = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        float f = facingZIn;
        float f2 = facingYIn;
        float f3 = facingXIn;
        EnumHand enumHand = EnumHand.MAIN_HAND;
        EnumFacing enumFacing = EnumFacing.values()[placedBlockDirectionIn];
        BlockPos blockPos2 = blockPos;
        CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock = new CPacketPlayerTryUseItemOnBlock(blockPos2, enumFacing, enumHand, f3, f2, f);
        return new CPacketPlayerBlockPlacementImpl<CPacketPlayerTryUseItemOnBlock>(cPacketPlayerTryUseItemOnBlock);
    }

    @Override
    @NotNull
    public ICPacketPlayerPosLook createCPacketPlayerPosLook(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        return new CPacketPlayerPosLookImpl<CPacketPlayer.PositionRotation>(new CPacketPlayer.PositionRotation(x, y, z, yaw, pitch, onGround));
    }

    @Override
    @NotNull
    public ICPacketClientStatus createCPacketClientStatus(@NotNull ICPacketClientStatus.WEnumState state) {
        CPacketClientStatus.State state2;
        CPacketClientStatus.State state3;
        Intrinsics.checkParameterIsNotNull((Object)((Object)state), (String)"state");
        ICPacketClientStatus.WEnumState $this$unwrap$iv = state;
        boolean $i$f$unwrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$12[$this$unwrap$iv.ordinal()]) {
            case 1: {
                state3 = CPacketClientStatus.State.PERFORM_RESPAWN;
                break;
            }
            case 2: {
                state3 = CPacketClientStatus.State.REQUEST_STATS;
                break;
            }
            case 3: {
                Backend backend = Backend.INSTANCE;
                boolean $i$f$BACKEND_UNSUPPORTED = false;
                throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        CPacketClientStatus.State state4 = state2 = state3;
        CPacketClientStatus cPacketClientStatus = new CPacketClientStatus(state4);
        return new CPacketClientStatusImpl<CPacketClientStatus>(cPacketClientStatus);
    }

    @Override
    @NotNull
    public IPacket createCPacketPlayerDigging(@NotNull ICPacketPlayerDigging.WAction wAction, @NotNull WBlockPos pos, @NotNull IEnumFacing facing) {
        EnumFacing enumFacing;
        CPacketPlayerDigging.Action action;
        Intrinsics.checkParameterIsNotNull((Object)((Object)wAction), (String)"wAction");
        Intrinsics.checkParameterIsNotNull((Object)pos, (String)"pos");
        Intrinsics.checkParameterIsNotNull((Object)facing, (String)"facing");
        Object $this$unwrap$iv = wAction;
        boolean $i$f$unwrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[((Enum)$this$unwrap$iv).ordinal()]) {
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
        $this$unwrap$iv = pos;
        $i$f$unwrap = false;
        BlockPos blockPos = new BlockPos(((WVec3i)$this$unwrap$iv).getX(), ((WVec3i)$this$unwrap$iv).getY(), ((WVec3i)$this$unwrap$iv).getZ());
        $this$unwrap$iv = facing;
        $i$f$unwrap = false;
        EnumFacing enumFacing2 = enumFacing = ((EnumFacingImpl)$this$unwrap$iv).getWrapped();
        BlockPos blockPos2 = blockPos;
        CPacketPlayerDigging.Action action3 = action2;
        Packet packet = (Packet)new CPacketPlayerDigging(action3, blockPos2, enumFacing2);
        return new PacketImpl<Packet>(packet);
    }

    @Override
    @NotNull
    public PacketImpl<?> createCPacketTryUseItem(@NotNull WEnumHand hand) {
        EnumHand enumHand;
        EnumHand enumHand2;
        Intrinsics.checkParameterIsNotNull((Object)((Object)hand), (String)"hand");
        WEnumHand $this$unwrap$iv = hand;
        boolean $i$f$unwrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$16[$this$unwrap$iv.ordinal()]) {
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
        return new PacketImpl<Packet>(packet);
    }

    @Override
    @NotNull
    public ICPacketPlayer createCPacketPlayerPosition(double x, double y, double z, boolean onGround) {
        return new CPacketPlayerImpl<CPacketPlayer>((CPacketPlayer)new CPacketPlayer.Position(x, y, z, onGround));
    }

    @Override
    @NotNull
    public IPacket createICPacketResourcePackStatus(@NotNull String hash, @NotNull ICPacketResourcePackStatus.WAction status) {
        CPacketResourcePackStatus.Action action;
        CPacketResourcePackStatus.Action action2;
        Intrinsics.checkParameterIsNotNull((Object)hash, (String)"hash");
        Intrinsics.checkParameterIsNotNull((Object)((Object)status), (String)"status");
        ICPacketResourcePackStatus.WAction $this$unwrap$iv = status;
        boolean $i$f$unwrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$13[$this$unwrap$iv.ordinal()]) {
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
        return new PacketImpl<Packet>(packet);
    }

    @Override
    @NotNull
    public ICPacketPlayer createCPacketPlayerLook(float yaw, float pitch, boolean onGround) {
        return new CPacketPlayerImpl<CPacketPlayer>((CPacketPlayer)new CPacketPlayer.Rotation(yaw, pitch, onGround));
    }

    @Override
    @NotNull
    public ICPacketUseEntity createCPacketUseEntity(@NotNull IEntity player, @NotNull ICPacketUseEntity.WAction wAction) {
        ICPacketUseEntity iCPacketUseEntity;
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
        Intrinsics.checkParameterIsNotNull((Object)((Object)wAction), (String)"wAction");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$0[wAction.ordinal()]) {
            case 1: {
                IEntity $this$unwrap$iv = player;
                boolean $i$f$unwrap = false;
                Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
                EnumHand enumHand = EnumHand.MAIN_HAND;
                Object t3 = t2;
                CPacketUseEntity cPacketUseEntity = new CPacketUseEntity(t3, enumHand);
                iCPacketUseEntity = new CPacketUseEntityImpl<CPacketUseEntity>(cPacketUseEntity);
                break;
            }
            case 2: {
                Object t4;
                IEntity $this$unwrap$iv = player;
                boolean $i$f$unwrap = false;
                Object t5 = t4 = ((EntityImpl)$this$unwrap$iv).getWrapped();
                CPacketUseEntity cPacketUseEntity = new CPacketUseEntity(t5);
                iCPacketUseEntity = new CPacketUseEntityImpl<CPacketUseEntity>(cPacketUseEntity);
                break;
            }
            case 3: {
                Backend this_$iv = Backend.INSTANCE;
                boolean $i$f$BACKEND_UNSUPPORTED = false;
                throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return iCPacketUseEntity;
    }

    @Override
    @NotNull
    public ICPacketUseEntity createCPacketUseEntity(@NotNull IEntity entity, @NotNull WVec3 positionVector) {
        Vec3d vec3d;
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        Intrinsics.checkParameterIsNotNull((Object)positionVector, (String)"positionVector");
        Object $this$unwrap$iv = entity;
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = positionVector;
        EnumHand enumHand = EnumHand.MAIN_HAND;
        $i$f$unwrap = false;
        Vec3d vec3d2 = vec3d = new Vec3d(((WVec3)$this$unwrap$iv).getXCoord(), ((WVec3)$this$unwrap$iv).getYCoord(), ((WVec3)$this$unwrap$iv).getZCoord());
        EnumHand enumHand2 = enumHand;
        Object t3 = t2;
        CPacketUseEntity cPacketUseEntity = new CPacketUseEntity(t3, enumHand2, vec3d2);
        return new CPacketUseEntityImpl<CPacketUseEntity>(cPacketUseEntity);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public IPacket createCPacketCreativeInventoryAction(int slot, @NotNull IItemStack itemStack) {
        void $this$unwrap$iv;
        ItemStack itemStack2;
        Intrinsics.checkParameterIsNotNull((Object)itemStack, (String)"itemStack");
        IItemStack iItemStack = itemStack;
        int n = slot;
        boolean $i$f$unwrap = false;
        ItemStack itemStack3 = itemStack2 = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        int n2 = n;
        Packet packet = (Packet)new CPacketCreativeInventoryAction(n2, itemStack3);
        return new PacketImpl<Packet>(packet);
    }

    @Override
    @NotNull
    public ICPacketEntityAction createCPacketEntityAction(@NotNull IEntity player, @NotNull ICPacketEntityAction.WAction wAction) {
        CPacketEntityAction.Action action;
        CPacketEntityAction.Action action2;
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
        Intrinsics.checkParameterIsNotNull((Object)((Object)wAction), (String)"wAction");
        Object $this$unwrap$iv = player;
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = wAction;
        $i$f$unwrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[((Enum)$this$unwrap$iv).ordinal()]) {
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
        Object t3 = t2;
        CPacketEntityAction cPacketEntityAction = new CPacketEntityAction(t3, action3);
        return new CPacketEntityActionImpl<CPacketEntityAction>(cPacketEntityAction);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public ICPacketCustomPayload createCPacketCustomPayload(@NotNull String channel, @NotNull IPacketBuffer payload) {
        void $this$unwrap$iv;
        PacketBuffer packetBuffer;
        Intrinsics.checkParameterIsNotNull((Object)channel, (String)"channel");
        Intrinsics.checkParameterIsNotNull((Object)payload, (String)"payload");
        IPacketBuffer iPacketBuffer = payload;
        String string = channel;
        boolean $i$f$unwrap = false;
        PacketBuffer packetBuffer2 = packetBuffer = ((PacketBufferImpl)$this$unwrap$iv).getWrapped();
        String string2 = string;
        CPacketCustomPayload cPacketCustomPayload = new CPacketCustomPayload(string2, packetBuffer2);
        return new CPacketCustomPayloadImpl<CPacketCustomPayload>(cPacketCustomPayload);
    }

    @Override
    @NotNull
    public ICPacketCloseWindow createCPacketCloseWindow(int windowId) {
        return new CPacketCloseWindowImpl<CPacketCloseWindow>(new CPacketCloseWindow(windowId));
    }

    @Override
    @NotNull
    public ICPacketCloseWindow createCPacketCloseWindow() {
        return new CPacketCloseWindowImpl<CPacketCloseWindow>(new CPacketCloseWindow());
    }

    @Override
    @NotNull
    public ICPacketPlayer createCPacketPlayer(boolean onGround) {
        return new CPacketPlayerImpl<CPacketPlayer>(new CPacketPlayer(onGround));
    }

    @Override
    @NotNull
    public IPacket createCPacketTabComplete(@NotNull String text) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        return new PacketImpl<Packet>((Packet)new CPacketTabComplete(text, null, false));
    }

    @Override
    @NotNull
    public ICPacketAnimation createCPacketAnimation() {
        return new CPacketAnimationImpl<CPacketAnimation>(new CPacketAnimation(EnumHand.MAIN_HAND));
    }

    @Override
    @NotNull
    public ICPacketKeepAlive createCPacketKeepAlive() {
        return new CPacketKeepAliveImpl<CPacketKeepAlive>(new CPacketKeepAlive());
    }

    @Override
    public boolean isEntityAnimal(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityAnimal;
    }

    @Override
    public boolean isEntitySquid(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntitySquid;
    }

    @Override
    public boolean isEntityBat(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityBat;
    }

    @Override
    public boolean isEntityGolem(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityGolem;
    }

    @Override
    public boolean isEntityMob(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityMob;
    }

    @Override
    public boolean isEntityVillager(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityVillager;
    }

    @Override
    public boolean isEntitySlime(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntitySlime;
    }

    @Override
    public boolean isEntityGhast(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityGhast;
    }

    @Override
    public boolean isEntityDragon(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityDragon;
    }

    @Override
    public boolean isEntityLivingBase(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityLivingBase;
    }

    @Override
    public boolean isEntityPlayer(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityPlayer;
    }

    @Override
    public boolean isEntityArmorStand(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityArmorStand;
    }

    @Override
    public boolean isEntityTNTPrimed(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityTNTPrimed;
    }

    @Override
    public boolean isEntityBoat(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityBoat;
    }

    @Override
    public boolean isEntityMinecart(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityMinecart;
    }

    @Override
    public boolean isEntityItem(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityItem;
    }

    @Override
    public boolean isEntityArrow(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityArrow;
    }

    @Override
    public boolean isEntityFallingBlock(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityFallingBlock;
    }

    @Override
    public boolean isEntityMinecartChest(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityMinecartChest;
    }

    @Override
    public boolean isEntityShulker(@Nullable Object obj) {
        return obj instanceof EntityImpl && ((EntityImpl)obj).getWrapped() instanceof EntityShulker;
    }

    @Override
    public boolean isTileEntityChest(@Nullable Object obj) {
        return obj instanceof TileEntityImpl && ((TileEntityImpl)obj).getWrapped() instanceof TileEntityChest;
    }

    @Override
    public boolean isTileEntityEnderChest(@Nullable Object obj) {
        return obj instanceof TileEntityImpl && ((TileEntityImpl)obj).getWrapped() instanceof TileEntityEnderChest;
    }

    @Override
    public boolean isTileEntityFurnace(@Nullable Object obj) {
        return obj instanceof TileEntityImpl && ((TileEntityImpl)obj).getWrapped() instanceof TileEntityFurnace;
    }

    @Override
    public boolean isTileEntityDispenser(@Nullable Object obj) {
        return obj instanceof TileEntityImpl && ((TileEntityImpl)obj).getWrapped() instanceof TileEntityDispenser;
    }

    @Override
    public boolean isTileEntityHopper(@Nullable Object obj) {
        return obj instanceof TileEntityImpl && ((TileEntityImpl)obj).getWrapped() instanceof TileEntityHopper;
    }

    @Override
    public boolean isTileEntityShulkerBox(@Nullable Object obj) {
        return obj instanceof TileEntityImpl && ((TileEntityImpl)obj).getWrapped() instanceof TileEntityShulkerBox;
    }

    @Override
    public boolean isSPacketChat(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketChat;
    }

    @Override
    public boolean isSPacketEntity(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketEntity;
    }

    @Override
    public boolean isSPacketResourcePackSend(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketResourcePackSend;
    }

    @Override
    public boolean isSPacketPlayerPosLook(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketPlayerPosLook;
    }

    @Override
    public boolean isSPacketAnimation(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketAnimation;
    }

    @Override
    public boolean isSPacketEntityVelocity(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketEntityVelocity;
    }

    @Override
    public boolean isSPacketExplosion(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketEntityVelocity;
    }

    @Override
    public boolean isSPacketCloseWindow(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketCloseWindow;
    }

    @Override
    public boolean isSPacketTabComplete(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketTabComplete;
    }

    @Override
    public boolean isCPacketPlayer(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketPlayer;
    }

    @Override
    public boolean isCPacketPlayerBlockPlacement(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketPlayerTryUseItemOnBlock;
    }

    @Override
    public boolean isCPacketConfirmTransaction(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketConfirmTransaction;
    }

    @Override
    public boolean isCPacketUseEntity(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketUseEntity;
    }

    @Override
    public boolean isCPacketCloseWindow(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketCloseWindow;
    }

    @Override
    public boolean isCPacketChatMessage(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketChatMessage;
    }

    @Override
    public boolean isCPacketKeepAlive(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketKeepAlive;
    }

    @Override
    public boolean isCPacketPlayerPosition(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketPlayer.Position;
    }

    @Override
    public boolean isCPacketPlayerPosLook(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketPlayer.PositionRotation;
    }

    @Override
    public boolean isCPacketClientStatus(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketClientStatus;
    }

    @Override
    public boolean isCPacketAnimation(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketAnimation;
    }

    @Override
    public boolean isCPacketEntityAction(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketEntityAction;
    }

    @Override
    public boolean isSPacketWindowItems(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof SPacketWindowItems;
    }

    @Override
    public boolean isCPacketHeldItemChange(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketHeldItemChange;
    }

    @Override
    public boolean isCPacketPlayerLook(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketPlayer.Rotation;
    }

    @Override
    public boolean isCPacketCustomPayload(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketCustomPayload;
    }

    @Override
    public boolean isCPacketHandshake(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof C00Handshake;
    }

    @Override
    public boolean isCPacketPlayerDigging(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketPlayerDigging;
    }

    @Override
    public boolean isCPacketTryUseItem(@Nullable Object obj) {
        return obj instanceof PacketImpl && ((PacketImpl)obj).getWrapped() instanceof CPacketPlayerTryUseItem;
    }

    @Override
    public boolean isItemSword(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemSword;
    }

    @Override
    public boolean isItemTool(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemTool;
    }

    @Override
    public boolean isItemArmor(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemArmor;
    }

    @Override
    public boolean isItemPotion(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemPotion;
    }

    @Override
    public boolean isItemBlock(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemBlock;
    }

    @Override
    public boolean isItemBow(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemBow;
    }

    @Override
    public boolean isItemBucket(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemBucket;
    }

    @Override
    public boolean isItemFood(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemFood;
    }

    @Override
    public boolean isItemBucketMilk(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemBucketMilk;
    }

    @Override
    public boolean isItemPickaxe(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemPickaxe;
    }

    @Override
    public boolean isItemAxe(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemAxe;
    }

    @Override
    public boolean isItemBed(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemBed;
    }

    @Override
    public boolean isItemEnderPearl(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemEnderPearl;
    }

    @Override
    public boolean isItemEnchantedBook(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemEnchantedBook;
    }

    @Override
    public boolean isItemBoat(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemBoat;
    }

    @Override
    public boolean isItemMinecart(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemMinecart;
    }

    @Override
    public boolean isItemAppleGold(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemAppleGold;
    }

    @Override
    public boolean isItemSnowball(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemSnowball;
    }

    @Override
    public boolean isItemEgg(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemEgg;
    }

    @Override
    public boolean isItemFishingRod(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemFishingRod;
    }

    @Override
    public boolean isItemAir(@Nullable Object obj) {
        return obj instanceof ItemImpl && ((ItemImpl)obj).getWrapped() instanceof ItemAir;
    }

    @Override
    public boolean isBlockAir(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockAir;
    }

    @Override
    public boolean isBlockFence(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockFence;
    }

    @Override
    public boolean isBlockSnow(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockSnow;
    }

    @Override
    public boolean isBlockLadder(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockLadder;
    }

    @Override
    public boolean isBlockVine(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockVine;
    }

    @Override
    public boolean isBlockSlime(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockSlime;
    }

    @Override
    public boolean isBlockSlab(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockSlab;
    }

    @Override
    public boolean isBlockStairs(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockStairs;
    }

    @Override
    public boolean isBlockCarpet(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockCarpet;
    }

    @Override
    public boolean isBlockPane(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockPane;
    }

    @Override
    public boolean isBlockLiquid(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockLiquid;
    }

    @Override
    public boolean isBlockCactus(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockCactus;
    }

    @Override
    public boolean isBlockBedrock(@Nullable Object obj) {
        return obj instanceof BlockImpl && Intrinsics.areEqual((Object)((BlockImpl)obj).getWrapped(), (Object)Blocks.field_150357_h);
    }

    @Override
    public boolean isBlockBush(@Nullable Object obj) {
        return obj instanceof BlockImpl && ((BlockImpl)obj).getWrapped() instanceof BlockBush;
    }

    @Override
    public boolean isGuiInventory(@Nullable Object obj) {
        return obj instanceof GuiImpl && ((GuiImpl)obj).getWrapped() instanceof GuiInventory;
    }

    @Override
    public boolean isGuiContainer(@Nullable Object obj) {
        return obj instanceof GuiImpl && ((GuiImpl)obj).getWrapped() instanceof GuiContainer;
    }

    @Override
    public boolean isGuiGameOver(@Nullable Object obj) {
        return obj instanceof GuiImpl && ((GuiImpl)obj).getWrapped() instanceof GuiGameOver;
    }

    @Override
    public boolean isGuiChat(@Nullable Object obj) {
        return obj instanceof GuiImpl && ((GuiImpl)obj).getWrapped() instanceof GuiChat;
    }

    @Override
    public boolean isGuiIngameMenu(@Nullable Object obj) {
        return obj instanceof GuiImpl && ((GuiImpl)obj).getWrapped() instanceof GuiIngameMenu;
    }

    @Override
    public boolean isGuiChest(@Nullable Object obj) {
        return obj instanceof GuiImpl && ((GuiImpl)obj).getWrapped() instanceof GuiChest;
    }

    @Override
    public boolean isGuiHudDesigner(@Nullable Object obj) {
        return obj instanceof GuiScreenImpl && ((GuiScreenImpl)obj).getWrapped() instanceof GuiScreenWrapper && ((GuiScreenWrapper)((Object)((GuiScreenImpl)obj).getWrapped())).getWrapped() instanceof GuiHudDesigner;
    }

    @Override
    public boolean isClickGui(@Nullable Object obj) {
        return obj instanceof GuiScreenImpl && ((GuiScreenImpl)obj).getWrapped() instanceof GuiScreenWrapper && ((GuiScreenWrapper)((Object)((GuiScreenImpl)obj).getWrapped())).getWrapped() instanceof ClickGui;
    }

    @Override
    @NotNull
    public IPotion getPotionEnum(@NotNull PotionType type) {
        Potion potion;
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$1[type.ordinal()]) {
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
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        Intrinsics.checkExpressionValueIsNotNull((Object)potion, (String)"when (type) {\n          \u2026ects.JUMP_BOOST\n        }");
        return new PotionImpl(potion);
    }

    @Override
    @NotNull
    public IEnumFacing getEnumFacing(@NotNull EnumFacingType type) {
        EnumFacing enumFacing;
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$2[type.ordinal()]) {
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

    @Override
    @NotNull
    public IBlock getBlockEnum(@NotNull BlockType type) {
        IBlock iBlock;
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$3[type.ordinal()]) {
            case 1: {
                Block block = Blocks.field_150381_bn;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.ENCHANTING_TABLE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 2: {
                BlockChest blockChest = Blocks.field_150486_ae;
                Intrinsics.checkExpressionValueIsNotNull((Object)blockChest, (String)"Blocks.CHEST");
                Block $this$wrap$iv = (Block)blockChest;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 3: {
                Block block = Blocks.field_150477_bB;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.ENDER_CHEST");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 4: {
                Block block = Blocks.field_150447_bR;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.TRAPPED_CHEST");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 5: {
                Block block = Blocks.field_150467_bQ;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.ANVIL");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 6: {
                BlockSand blockSand = Blocks.field_150354_m;
                Intrinsics.checkExpressionValueIsNotNull((Object)blockSand, (String)"Blocks.SAND");
                Block $this$wrap$iv = (Block)blockSand;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 7: {
                Block block = Blocks.field_150321_G;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.WEB");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 8: {
                Block block = Blocks.field_150478_aa;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.TORCH");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 9: {
                Block block = Blocks.field_150462_ai;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.CRAFTING_TABLE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 10: {
                Block block = Blocks.field_150460_al;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.FURNACE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 11: {
                Block block = Blocks.field_150392_bi;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.WATERLILY");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 12: {
                Block block = Blocks.field_150367_z;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.DISPENSER");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 13: {
                Block block = Blocks.field_150456_au;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.STONE_PRESSURE_PLATE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 14: {
                Block block = Blocks.field_150452_aw;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.WOODEN_PRESSURE_PLATE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 15: {
                Block block = Blocks.field_150335_W;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.TNT");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 16: {
                Block block = Blocks.field_180393_cK;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.STANDING_BANNER");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 17: {
                Block block = Blocks.field_180394_cL;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.WALL_BANNER");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 18: {
                Block block = Blocks.field_150429_aA;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.REDSTONE_TORCH");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 19: {
                Block block = Blocks.field_150323_B;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.NOTEBLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 20: {
                Block block = Blocks.field_150409_cd;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.DROPPER");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 21: {
                Block block = Blocks.field_150431_aC;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.SNOW_LAYER");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 22: {
                Block block = Blocks.field_150350_a;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.AIR");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 23: {
                Block block = Blocks.field_150403_cj;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.PACKED_ICE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 24: {
                Block block = Blocks.field_150432_aD;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.ICE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 25: {
                BlockStaticLiquid blockStaticLiquid = Blocks.field_150355_j;
                Intrinsics.checkExpressionValueIsNotNull((Object)blockStaticLiquid, (String)"Blocks.WATER");
                Block $this$wrap$iv = (Block)blockStaticLiquid;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 26: {
                Block block = Blocks.field_180401_cv;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.BARRIER");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 27: {
                BlockDynamicLiquid blockDynamicLiquid = Blocks.field_150358_i;
                Intrinsics.checkExpressionValueIsNotNull((Object)blockDynamicLiquid, (String)"Blocks.FLOWING_WATER");
                Block $this$wrap$iv = (Block)blockDynamicLiquid;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 28: {
                Block block = Blocks.field_150365_q;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.COAL_ORE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 29: {
                Block block = Blocks.field_150366_p;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.IRON_ORE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 30: {
                Block block = Blocks.field_150352_o;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.GOLD_ORE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 31: {
                Block block = Blocks.field_150450_ax;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.REDSTONE_ORE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 32: {
                Block block = Blocks.field_150369_x;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.LAPIS_ORE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 33: {
                Block block = Blocks.field_150482_ag;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.DIAMOND_ORE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 34: {
                Block block = Blocks.field_150412_bA;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.EMERALD_ORE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 35: {
                Block block = Blocks.field_150449_bY;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.QUARTZ_ORE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 36: {
                Block block = Blocks.field_150435_aG;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.CLAY");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 37: {
                Block block = Blocks.field_150426_aN;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.GLOWSTONE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 38: {
                Block block = Blocks.field_150468_ap;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.LADDER");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 39: {
                Block block = Blocks.field_150402_ci;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.COAL_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 40: {
                Block block = Blocks.field_150339_S;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.IRON_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 41: {
                Block block = Blocks.field_150340_R;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.GOLD_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 42: {
                Block block = Blocks.field_150484_ah;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.DIAMOND_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 43: {
                Block block = Blocks.field_150475_bE;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.EMERALD_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 44: {
                Block block = Blocks.field_150451_bX;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.REDSTONE_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 45: {
                Block block = Blocks.field_150368_y;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.LAPIS_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 46: {
                BlockFire blockFire = Blocks.field_150480_ab;
                Intrinsics.checkExpressionValueIsNotNull((Object)blockFire, (String)"Blocks.FIRE");
                Block $this$wrap$iv = (Block)blockFire;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 47: {
                Block block = Blocks.field_150341_Y;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.MOSSY_COBBLESTONE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 48: {
                Block block = Blocks.field_150474_ac;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.MOB_SPAWNER");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 49: {
                Block block = Blocks.field_150378_br;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.END_PORTAL_FRAME");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 50: {
                Block block = Blocks.field_150342_X;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.BOOKSHELF");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 51: {
                Block block = Blocks.field_150483_bI;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.COMMAND_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 52: {
                BlockStaticLiquid blockStaticLiquid = Blocks.field_150353_l;
                Intrinsics.checkExpressionValueIsNotNull((Object)blockStaticLiquid, (String)"Blocks.LAVA");
                Block $this$wrap$iv = (Block)blockStaticLiquid;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 53: {
                BlockDynamicLiquid blockDynamicLiquid = Blocks.field_150356_k;
                Intrinsics.checkExpressionValueIsNotNull((Object)blockDynamicLiquid, (String)"Blocks.FLOWING_LAVA");
                Block $this$wrap$iv = (Block)blockDynamicLiquid;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 54: {
                Block block = Blocks.field_150470_am;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.LIT_FURNACE");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 55: {
                Block block = Blocks.field_150380_bt;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.DRAGON_EGG");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 56: {
                Block block = Blocks.field_150420_aW;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.BROWN_MUSHROOM_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 57: {
                Block block = Blocks.field_150419_aX;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.RED_MUSHROOM_BLOCK");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            case 58: {
                Block block = Blocks.field_150458_ak;
                Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"Blocks.FARMLAND");
                Block $this$wrap$iv = block;
                boolean $i$f$wrap = false;
                iBlock = new BlockImpl($this$wrap$iv);
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return iBlock;
    }

    @Override
    @NotNull
    public IMaterial getMaterialEnum(@NotNull MaterialType type) {
        Material material;
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$4[type.ordinal()]) {
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
        Intrinsics.checkExpressionValueIsNotNull((Object)material, (String)"when (type) {\n          \u2026al.LAVA\n                }");
        return new MaterialImpl(material);
    }

    @Override
    @NotNull
    public IStatBase getStatEnum(@NotNull StatType type) {
        StatBase statBase;
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$5[type.ordinal()]) {
            case 1: {
                statBase = StatList.field_75953_u;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        Intrinsics.checkExpressionValueIsNotNull((Object)statBase, (String)"when (type) {\n          \u2026st.JUMP\n                }");
        return new StatBaseImpl(statBase);
    }

    @Override
    @NotNull
    public IItem getItemEnum(@NotNull ItemType type) {
        Item item;
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$6[type.ordinal()]) {
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
        return new ItemImpl<Item>(item);
    }

    @Override
    @NotNull
    public IEnchantment getEnchantmentEnum(@NotNull EnchantmentType type) {
        Enchantment enchantment;
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$7[type.ordinal()]) {
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
        Intrinsics.checkExpressionValueIsNotNull((Object)enchantment, (String)"when (type) {\n          \u2026REAKING\n                }");
        return new EnchantmentImpl(enchantment);
    }

    @Override
    @NotNull
    public IVertexFormat getVertexFormatEnum(@NotNull WDefaultVertexFormats type) {
        VertexFormat vertexFormat;
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        switch (ClassProviderImpl$WhenMappings.$EnumSwitchMapping$8[type.ordinal()]) {
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
            case 4: {
                vertexFormat = DefaultVertexFormats.field_181709_i;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        Intrinsics.checkExpressionValueIsNotNull((Object)vertexFormat, (String)"when (type) {\n          \u2026X_COLOR\n                }");
        return new VertexFormatImpl(vertexFormat);
    }

    @Override
    @NotNull
    public IFontRenderer wrapFontRenderer(@NotNull IWrappedFontRenderer fontRenderer) {
        Intrinsics.checkParameterIsNotNull((Object)fontRenderer, (String)"fontRenderer");
        return new FontRendererImpl(new FontRendererWrapper(fontRenderer));
    }

    @Override
    @NotNull
    public IGuiScreen wrapGuiScreen(@NotNull WrappedGuiScreen clickGui) {
        Intrinsics.checkParameterIsNotNull((Object)clickGui, (String)"clickGui");
        GuiScreenImpl<GuiScreen> instance = new GuiScreenImpl<GuiScreen>(new GuiScreenWrapper(clickGui));
        clickGui.setRepresentedScreen(instance);
        return instance;
    }

    @Override
    @NotNull
    public IVertexBuffer createSafeVertexBuffer(@NotNull IVertexFormat vertexFormat) {
        VertexFormat vertexFormat2;
        Intrinsics.checkParameterIsNotNull((Object)vertexFormat, (String)"vertexFormat");
        IVertexFormat $this$unwrap$iv = vertexFormat;
        boolean $i$f$unwrap = false;
        VertexFormat vertexFormat3 = vertexFormat2 = ((VertexFormatImpl)$this$unwrap$iv).getWrapped();
        VertexBuffer $this$wrap$iv = new SafeVertexBuffer(vertexFormat3);
        boolean $i$f$wrap = false;
        return new VertexBufferImpl($this$wrap$iv);
    }

    @Override
    public void wrapCreativeTab(@NotNull String name, @NotNull WrappedCreativeTabs wrappedCreativeTabs) {
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        Intrinsics.checkParameterIsNotNull((Object)wrappedCreativeTabs, (String)"wrappedCreativeTabs");
        wrappedCreativeTabs.setRepresentedType(new CreativeTabsImpl(new CreativeTabsWrapper(wrappedCreativeTabs, name)));
    }

    @Override
    public void wrapGuiSlot(@NotNull WrappedGuiSlot wrappedGuiSlot, @NotNull IMinecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        Intrinsics.checkParameterIsNotNull((Object)wrappedGuiSlot, (String)"wrappedGuiSlot");
        Intrinsics.checkParameterIsNotNull((Object)mc, (String)"mc");
        new GuiSlotWrapper(wrappedGuiSlot, mc, width, height, top, bottom, slotHeight);
    }

    @Override
    @NotNull
    public IGlStateManager getGlStateManager() {
        return GlStateManagerImpl.INSTANCE;
    }

    @Override
    @NotNull
    public IPacket createCPacketEncryptionResponse(@NotNull SecretKey secretKey, @NotNull PublicKey publicKey, @NotNull byte[] verifyToken) {
        Intrinsics.checkParameterIsNotNull((Object)secretKey, (String)"secretKey");
        Intrinsics.checkParameterIsNotNull((Object)publicKey, (String)"publicKey");
        Intrinsics.checkParameterIsNotNull((Object)verifyToken, (String)"verifyToken");
        return new PacketImpl<Packet>((Packet)new CPacketEncryptionResponse(secretKey, publicKey, verifyToken));
    }

    private ClassProviderImpl() {
    }

    static {
        ClassProviderImpl classProviderImpl;
        INSTANCE = classProviderImpl = new ClassProviderImpl();
    }
}

