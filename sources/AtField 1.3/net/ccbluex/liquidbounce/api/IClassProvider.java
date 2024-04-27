/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  io.netty.buffer.ByteBuf
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.MinecraftVersion;
import net.ccbluex.liquidbounce.api.SupportsMinecraftVersions;
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
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.api.network.IPacketBuffer;
import net.ccbluex.liquidbounce.api.util.IWrappedFontRenderer;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u00c0\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\be\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J8\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\rH&J\b\u0010\u0013\u001a\u00020\u0014H&J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H&J\b\u0010\u0019\u001a\u00020\u001aH&J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH&J\u0018\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020!H&J\u0018\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'H&J \u0010(\u001a\u00020\u001e2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.H&J\u0018\u0010/\u001a\u0002002\u0006\u00101\u001a\u0002022\u0006\u00103\u001a\u000204H&J\u0010\u00105\u001a\u0002062\u0006\u0010\u001f\u001a\u00020\u001cH&J\b\u00107\u001a\u000208H&J\u0010\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020<H&J\u0012\u0010=\u001a\u00020>2\b\u0010?\u001a\u0004\u0018\u00010!H'J:\u0010=\u001a\u00020>2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u001c2\b\u0010C\u001a\u0004\u0018\u00010!2\u0006\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020E2\u0006\u0010G\u001a\u00020EH&J \u0010H\u001a\u00020\u001e2\u0006\u00103\u001a\u00020I2\u0006\u0010J\u001a\u00020A2\u0006\u0010K\u001a\u00020LH&J \u0010M\u001a\u00020:2\u0006\u0010N\u001a\u00020E2\u0006\u0010O\u001a\u00020E2\u0006\u0010;\u001a\u00020<H&J8\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u00020\r2\u0006\u0010S\u001a\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010N\u001a\u00020E2\u0006\u0010O\u001a\u00020E2\u0006\u0010;\u001a\u00020<H&J(\u0010U\u001a\u00020:2\u0006\u0010R\u001a\u00020\r2\u0006\u0010V\u001a\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010;\u001a\u00020<H&J\u0010\u0010W\u001a\u00020\u001e2\u0006\u0010X\u001a\u00020%H&J\u0014\u0010Y\u001a\u0006\u0012\u0002\b\u00030Z2\u0006\u0010?\u001a\u00020[H'J\u0018\u0010\\\u001a\u00020]2\u0006\u00101\u001a\u0002022\u0006\u00103\u001a\u00020^H&J\u0018\u0010\\\u001a\u00020]2\u0006\u0010_\u001a\u0002022\u0006\u0010`\u001a\u00020aH&J\u0010\u0010b\u001a\u00020c2\u0006\u0010X\u001a\u00020%H&J\u0018\u0010d\u001a\u00020e2\u0006\u0010f\u001a\u00020g2\u0006\u0010h\u001a\u00020%H&J\u0010\u0010i\u001a\u00020j2\u0006\u0010k\u001a\u00020lH&J0\u0010m\u001a\u0002022\u0006\u0010n\u001a\u00020o2\u0006\u0010p\u001a\u00020\r2\u0006\u0010q\u001a\u00020\r2\u0006\u0010r\u001a\u00020\r2\u0006\u0010s\u001a\u00020<H&J\u0018\u0010t\u001a\u00020u2\u0006\u0010n\u001a\u00020v2\u0006\u0010w\u001a\u00020xH&J8\u0010y\u001a\u00020z2\u0006\u0010{\u001a\u00020\u001c2\u0006\u0010R\u001a\u00020\u001c2\u0006\u0010S\u001a\u00020\u001c2\u0006\u0010|\u001a\u00020\u001c2\u0006\u0010}\u001a\u00020\u001c2\u0006\u0010X\u001a\u00020%H&J(\u0010y\u001a\u00020z2\u0006\u0010{\u001a\u00020\u001c2\u0006\u0010R\u001a\u00020\u001c2\u0006\u0010S\u001a\u00020\u001c2\u0006\u0010X\u001a\u00020%H&J%\u0010~\u001a\u00020\u007f2\u0007\u0010\u0080\u0001\u001a\u00020\u007f2\b\u0010\u0081\u0001\u001a\u00030\u0082\u00012\b\u0010\u0083\u0001\u001a\u00030\u0084\u0001H&J\u0012\u0010\u0085\u0001\u001a\u00020\u007f2\u0007\u0010\u0086\u0001\u001a\u00020\u007fH&J\u0012\u0010\u0087\u0001\u001a\u00020\u007f2\u0007\u0010\u0086\u0001\u001a\u00020\u007fH&J\u001c\u0010\u0088\u0001\u001a\u00020\u007f2\u0007\u0010\u0086\u0001\u001a\u00020\u007f2\b\u0010\u0089\u0001\u001a\u00030\u008a\u0001H&J<\u0010\u008b\u0001\u001a\u00030\u008c\u00012\u0006\u0010{\u001a\u00020\u001c2\b\u0010\u008d\u0001\u001a\u00030\u008e\u00012\u0006\u0010R\u001a\u00020\u001c2\u0006\u0010S\u001a\u00020\u001c2\u0006\u0010|\u001a\u00020\u001c2\u0006\u0010}\u001a\u00020\u001cH&J\u0012\u0010\u008f\u0001\u001a\u00020\u007f2\u0007\u0010\u0086\u0001\u001a\u00020\u007fH&J<\u0010\u0090\u0001\u001a\u00030\u008c\u00012\u0006\u0010{\u001a\u00020\u001c2\b\u0010\u008d\u0001\u001a\u00030\u008e\u00012\u0006\u0010R\u001a\u00020\u001c2\u0006\u0010S\u001a\u00020\u001c2\u0006\u0010|\u001a\u00020\u001c2\u0006\u0010}\u001a\u00020\u001cH&J\u001c\u0010\u0091\u0001\u001a\u00020\u001e2\u0007\u0010\u0092\u0001\u001a\u00020%2\b\u0010\u0093\u0001\u001a\u00030\u0094\u0001H&J\n\u0010\u0095\u0001\u001a\u00030\u0096\u0001H&J\u0013\u0010\u0097\u0001\u001a\u00020!2\b\u0010\u0098\u0001\u001a\u00030\u0099\u0001H&J\u0013\u0010\u0097\u0001\u001a\u00020!2\b\u0010\u009a\u0001\u001a\u00030\u0096\u0001H&J%\u0010\u0097\u0001\u001a\u00020!2\b\u0010\u009a\u0001\u001a\u00030\u0096\u00012\u0007\u0010\u009b\u0001\u001a\u00020\u001c2\u0007\u0010\u009c\u0001\u001a\u00020\u001cH&J\n\u0010\u009d\u0001\u001a\u00030\u009e\u0001H&J\u0012\u0010\u009f\u0001\u001a\u00030\u00a0\u00012\u0006\u0010h\u001a\u00020\rH&J\n\u0010\u00a1\u0001\u001a\u00030\u00a2\u0001H&J\u0013\u0010\u00a3\u0001\u001a\u00030\u00a4\u00012\u0007\u0010\u00a5\u0001\u001a\u00020%H&J\u0013\u0010\u00a6\u0001\u001a\u00020'2\b\u0010\u00a7\u0001\u001a\u00030\u00a8\u0001H&J$\u0010\u00a9\u0001\u001a\u00030\u00aa\u00012\u0006\u0010{\u001a\u00020\u001c2\u0007\u0010\u00ab\u0001\u001a\u00020\u001c2\u0007\u0010\u00ac\u0001\u001a\u00020\u001cH&J\u0013\u0010\u00ad\u0001\u001a\u00030\u00ae\u00012\u0007\u0010\u00af\u0001\u001a\u00020%H&J\u0014\u0010\u00b0\u0001\u001a\u00030\u00b1\u00012\b\u0010\u00b2\u0001\u001a\u00030\u00b3\u0001H&J\u0014\u0010\u00b4\u0001\u001a\u00030\u00b5\u00012\b\u0010\u0081\u0001\u001a\u00030\u0082\u0001H&J.\u0010\u00b6\u0001\u001a\u00030\u00b7\u00012\u0007\u0010\u00b8\u0001\u001a\u00020%2\u0007\u0010\u00b9\u0001\u001a\u00020%2\u0007\u0010\u00ba\u0001\u001a\u00020%2\u0007\u0010\u00bb\u0001\u001a\u00020%H&J5\u0010\u00bc\u0001\u001a\u00030\u00bd\u00012\n\u0010\u00be\u0001\u001a\u0005\u0018\u00010\u00bf\u00012\u0007\u0010\u00c0\u0001\u001a\u00020%2\n\u0010\u00c1\u0001\u001a\u0005\u0018\u00010\u00ae\u00012\b\u0010\u00c2\u0001\u001a\u00030\u00c3\u0001H&J\u0014\u0010\u00c4\u0001\u001a\u00030\u0099\u00012\b\u0010\u00c5\u0001\u001a\u00030\u00c6\u0001H&J\u0014\u0010\u00c7\u0001\u001a\u00030\u00c8\u00012\b\u0010\u00c5\u0001\u001a\u00030\u00c9\u0001H&J\u0013\u0010\u00ca\u0001\u001a\u00020L2\b\u0010\u00c5\u0001\u001a\u00030\u00cb\u0001H&J\n\u0010\u00cc\u0001\u001a\u00030\u00cd\u0001H&J\u0014\u0010\u00ce\u0001\u001a\u00030\u0096\u00012\b\u0010\u00c5\u0001\u001a\u00030\u00cf\u0001H&J\u0014\u0010\u00d0\u0001\u001a\u00030\u00d1\u00012\b\u0010\u00c5\u0001\u001a\u00030\u00d2\u0001H&J\u0014\u0010\u00d3\u0001\u001a\u00030\u00d4\u00012\b\u0010\u00c5\u0001\u001a\u00030\u00d5\u0001H&J\u0014\u0010\u00d6\u0001\u001a\u00030\u00d7\u00012\b\u0010\u00c5\u0001\u001a\u00030\u00d8\u0001H&J\u0014\u0010\u00d9\u0001\u001a\u00030\u00b3\u00012\b\u0010\u00c5\u0001\u001a\u00030\u00da\u0001H&J\u0014\u0010\u00db\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00dd\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00de\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00df\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e0\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e1\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e2\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e3\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e4\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e5\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e6\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e7\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e8\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00e9\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ea\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00eb\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ec\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ed\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ee\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ef\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f0\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f1\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f2\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f3\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f4\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f5\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f6\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f7\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f8\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00f9\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00fa\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00fb\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00fc\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00fd\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00fe\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ff\u0001\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0080\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0081\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0082\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0083\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0084\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0085\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0086\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0087\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0088\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0089\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u008a\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u008b\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u008c\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u008d\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u008e\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u008f\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0090\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0091\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0092\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0093\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0094\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0095\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0096\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0097\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0098\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0099\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u009a\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u009b\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u009c\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u009d\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u009e\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u009f\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a0\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a1\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a2\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a3\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a4\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a5\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a6\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a7\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a8\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00a9\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00aa\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ab\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ac\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ad\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ae\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00af\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b0\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b1\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b2\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b3\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b4\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b5\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b6\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b7\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b8\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00b9\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00ba\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00bb\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00bc\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00bd\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u00be\u0002\u001a\u00020<2\t\u0010\u00dc\u0001\u001a\u0004\u0018\u00010\u0001H&J\u001d\u0010\u00bf\u0002\u001a\u00030\u00c0\u00022\u0007\u0010\u00b8\u0001\u001a\u00020%2\b\u0010\u00c1\u0002\u001a\u00030\u00c2\u0002H&J\u0014\u0010\u00c3\u0002\u001a\u00030\u008e\u00012\b\u0010\u00c4\u0002\u001a\u00030\u00c5\u0002H&J\u0013\u0010\u00c6\u0002\u001a\u00020\u007f2\b\u0010\u00c7\u0002\u001a\u00030\u00c8\u0002H&JI\u0010\u00c9\u0002\u001a\u00030\u00c0\u00022\b\u0010\u00ca\u0002\u001a\u00030\u00cb\u00022\b\u0010\u0081\u0001\u001a\u00030\u0082\u00012\u0006\u0010|\u001a\u00020\u001c2\u0006\u0010}\u001a\u00020\u001c2\u0007\u0010\u00cc\u0002\u001a\u00020\u001c2\u0007\u0010\u00cd\u0002\u001a\u00020\u001c2\u0007\u0010\u00ce\u0002\u001a\u00020\u001cH&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\t\u00a8\u0006\u00cf\u0002"}, d2={"Lnet/ccbluex/liquidbounce/api/IClassProvider;", "", "jsonToNBTInstance", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/IJsonToNBT;", "getJsonToNBTInstance", "()Lnet/ccbluex/liquidbounce/api/minecraft/nbt/IJsonToNBT;", "tessellatorInstance", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/ITessellator;", "getTessellatorInstance", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/ITessellator;", "createAxisAlignedBB", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "minX", "", "minY", "minZ", "maxX", "maxY", "maxZ", "createCPacketAnimation", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketAnimation;", "createCPacketClientStatus", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus;", "state", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus$WEnumState;", "createCPacketCloseWindow", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCloseWindow;", "windowId", "", "createCPacketCreativeInventoryAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "slot", "itemStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "createCPacketCustomPayload", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCustomPayload;", "channel", "", "payload", "Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;", "createCPacketEncryptionResponse", "secretKey", "Ljavax/crypto/SecretKey;", "publicKey", "Ljava/security/PublicKey;", "VerifyToken", "", "createCPacketEntityAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction;", "player", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "wAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction$WAction;", "createCPacketHeldItemChange", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketHeldItemChange;", "createCPacketKeepAlive", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketKeepAlive;", "createCPacketPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "onGround", "", "createCPacketPlayerBlockPlacement", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerBlockPlacement;", "stack", "positionIn", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "placedBlockDirectionIn", "stackIn", "facingXIn", "", "facingYIn", "facingZIn", "createCPacketPlayerDigging", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "pos", "facing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "createCPacketPlayerLook", "yaw", "pitch", "createCPacketPlayerPosLook", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerPosLook;", "x", "y", "z", "createCPacketPlayerPosition", "negativeInfinity", "createCPacketTabComplete", "text", "createCPacketTryUseItem", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/enums/WEnumHand;", "createCPacketUseEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "entity", "positionVector", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "createChatComponentText", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "createClickEvent", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "action", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent$WAction;", "value", "createDynamicTexture", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/IDynamicTexture;", "image", "Ljava/awt/image/BufferedImage;", "createEntityLightningBolt", "world", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "posX", "posY", "posZ", "effectOnly", "createEntityOtherPlayerMP", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityOtherPlayerMP;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "GameProfile", "Lcom/mojang/authlib/GameProfile;", "createGuiButton", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiButton;", "id", "width", "height", "createGuiConnecting", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "parent", "mc", "Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "serverData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "createGuiModList", "parentScreen", "createGuiMultiplayer", "createGuiOptions", "gameSettings", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "createGuiPasswordField", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiTextField;", "iFontRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "createGuiSelectWorld", "createGuiTextField", "createICPacketResourcePackStatus", "hash", "status", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus$WAction;", "createItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "createItemStack", "blockEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "item", "amount", "meta", "createNBTTagCompound", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "createNBTTagDouble", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagDouble;", "createNBTTagList", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagList;", "createNBTTagString", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagString;", "string", "createPacketBuffer", "buffer", "Lio/netty/buffer/ByteBuf;", "createPotionEffect", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotionEffect;", "time", "strength", "createResourceLocation", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "resourceName", "createSafeVertexBuffer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/vertex/IVertexBuffer;", "vertexFormat", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/vertex/IVertexFormat;", "createScaledResolution", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IScaledResolution;", "createSession", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "name", "uuid", "accessToken", "accountType", "createThreadDownloadImageData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/IThreadDownloadImageData;", "cacheFileIn", "Ljava/io/File;", "imageUrlIn", "textureResourceLocation", "imageBufferIn", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/WIImageBuffer;", "getBlockEnum", "type", "Lnet/ccbluex/liquidbounce/api/enums/BlockType;", "getEnchantmentEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/enchantments/IEnchantment;", "Lnet/ccbluex/liquidbounce/api/enums/EnchantmentType;", "getEnumFacing", "Lnet/ccbluex/liquidbounce/api/enums/EnumFacingType;", "getGlStateManager", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IGlStateManager;", "getItemEnum", "Lnet/ccbluex/liquidbounce/api/enums/ItemType;", "getMaterialEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/block/material/IMaterial;", "Lnet/ccbluex/liquidbounce/api/enums/MaterialType;", "getPotionEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/PotionType;", "getStatEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/stats/IStatBase;", "Lnet/ccbluex/liquidbounce/api/enums/StatType;", "getVertexFormatEnum", "Lnet/ccbluex/liquidbounce/api/enums/WDefaultVertexFormats;", "isBlockAir", "obj", "isBlockBedrock", "isBlockBush", "isBlockCactus", "isBlockCarpet", "isBlockFence", "isBlockLadder", "isBlockLiquid", "isBlockPane", "isBlockSlab", "isBlockSlime", "isBlockSnow", "isBlockStairs", "isBlockVine", "isCPacketAnimation", "isCPacketChatMessage", "isCPacketClientStatus", "isCPacketCloseWindow", "isCPacketConfirmTransaction", "isCPacketCustomPayload", "isCPacketEntityAction", "isCPacketHandshake", "isCPacketHeldItemChange", "isCPacketKeepAlive", "isCPacketPlayer", "isCPacketPlayerBlockPlacement", "isCPacketPlayerDigging", "isCPacketPlayerLook", "isCPacketPlayerPosLook", "isCPacketPlayerPosition", "isCPacketTryUseItem", "isCPacketUseEntity", "isClickGui", "isEntityAnimal", "isEntityArmorStand", "isEntityArrow", "isEntityBat", "isEntityBoat", "isEntityDragon", "isEntityFallingBlock", "isEntityFireball", "isEntityGhast", "isEntityGolem", "isEntityItem", "isEntityLivingBase", "isEntityMinecart", "isEntityMinecartChest", "isEntityMob", "isEntityPlayer", "isEntityShulker", "isEntitySlime", "isEntitySquid", "isEntityTNTPrimed", "isEntityVillager", "isGuiChat", "isGuiChest", "isGuiContainer", "isGuiGameOver", "isGuiHudDesigner", "isGuiIngameMenu", "isGuiInventory", "isItemAir", "isItemAppleGold", "isItemArmor", "isItemAxe", "isItemBed", "isItemBlock", "isItemBoat", "isItemBow", "isItemBucket", "isItemBucketMilk", "isItemEgg", "isItemEnchantedBook", "isItemEnderPearl", "isItemFishingRod", "isItemFood", "isItemMinecart", "isItemPickaxe", "isItemPotion", "isItemSnowball", "isItemSword", "isItemTool", "isSPacketAnimation", "isSPacketChat", "isSPacketCloseWindow", "isSPacketEntity", "isSPacketEntityVelocity", "isSPacketExplosion", "isSPacketPlayerPosLook", "isSPacketResourcePackSend", "isSPacketTabComplete", "isSPacketTimeUpdate", "isSPacketWindowItems", "isTileEntityChest", "isTileEntityDispenser", "isTileEntityEnderChest", "isTileEntityFurnace", "isTileEntityHopper", "isTileEntityShulkerBox", "wrapCreativeTab", "", "wrappedCreativeTabs", "Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;", "wrapFontRenderer", "fontRenderer", "Lnet/ccbluex/liquidbounce/api/util/IWrappedFontRenderer;", "wrapGuiScreen", "clickGui", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;", "wrapGuiSlot", "wrappedGuiSlot", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiSlot;", "top", "bottom", "slotHeight", "AtField"})
public interface IClassProvider {
    public boolean isSPacketResourcePackSend(@Nullable Object var1);

    @NotNull
    public IGuiScreen createGuiMultiplayer(@NotNull IGuiScreen var1);

    @NotNull
    public ICPacketUseEntity createCPacketUseEntity(@NotNull IEntity var1, @NotNull WVec3 var2);

    @NotNull
    public IPacket createCPacketTabComplete(@NotNull String var1);

    public boolean isBlockLadder(@Nullable Object var1);

    public boolean isItemBoat(@Nullable Object var1);

    public boolean isEntityAnimal(@Nullable Object var1);

    public boolean isTileEntityFurnace(@Nullable Object var1);

    @NotNull
    public IGuiButton createGuiButton(int var1, int var2, int var3, int var4, int var5, @NotNull String var6);

    public boolean isCPacketConfirmTransaction(@Nullable Object var1);

    public boolean isCPacketHandshake(@Nullable Object var1);

    public boolean isCPacketUseEntity(@Nullable Object var1);

    public boolean isBlockSlime(@Nullable Object var1);

    @NotNull
    public IThreadDownloadImageData createThreadDownloadImageData(@Nullable File var1, @NotNull String var2, @Nullable IResourceLocation var3, @NotNull WIImageBuffer var4);

    @NotNull
    public ITessellator getTessellatorInstance();

    public boolean isEntityMinecartChest(@Nullable Object var1);

    public boolean isCPacketTryUseItem(@Nullable Object var1);

    @NotNull
    public IClickEvent createClickEvent(@NotNull IClickEvent.WAction var1, @NotNull String var2);

    @NotNull
    public IPotion getPotionEnum(@NotNull PotionType var1);

    public boolean isTileEntityShulkerBox(@Nullable Object var1);

    public boolean isEntitySquid(@Nullable Object var1);

    public boolean isBlockSnow(@Nullable Object var1);

    public boolean isItemPotion(@Nullable Object var1);

    public boolean isSPacketCloseWindow(@Nullable Object var1);

    @NotNull
    public IAxisAlignedBB createAxisAlignedBB(double var1, double var3, double var5, double var7, double var9, double var11);

    public boolean isItemMinecart(@Nullable Object var1);

    @NotNull
    public IVertexBuffer createSafeVertexBuffer(@NotNull IVertexFormat var1);

    @NotNull
    public IEnumFacing getEnumFacing(@NotNull EnumFacingType var1);

    public boolean isGuiInventory(@Nullable Object var1);

    public boolean isItemBed(@Nullable Object var1);

    @NotNull
    public IVertexFormat getVertexFormatEnum(@NotNull WDefaultVertexFormats var1);

    public boolean isEntityFallingBlock(@Nullable Object var1);

    public boolean isBlockSlab(@Nullable Object var1);

    @NotNull
    public IScaledResolution createScaledResolution(@NotNull IMinecraft var1);

    public boolean isCPacketPlayerLook(@Nullable Object var1);

    public boolean isItemPickaxe(@Nullable Object var1);

    @NotNull
    public IStatBase getStatEnum(@NotNull StatType var1);

    public boolean isItemAxe(@Nullable Object var1);

    @NotNull
    public IPacket createCPacketCreativeInventoryAction(int var1, @NotNull IItemStack var2);

    @NotNull
    public IGuiScreen createGuiOptions(@NotNull IGuiScreen var1, @NotNull IGameSettings var2);

    @NotNull
    public IGuiTextField createGuiTextField(int var1, @NotNull IFontRenderer var2, int var3, int var4, int var5, int var6);

    public boolean isEntityArrow(@Nullable Object var1);

    public boolean isEntityGhast(@Nullable Object var1);

    public boolean isClickGui(@Nullable Object var1);

    public void wrapGuiSlot(@NotNull WrappedGuiSlot var1, @NotNull IMinecraft var2, int var3, int var4, int var5, int var6, int var7);

    @NotNull
    public INBTTagDouble createNBTTagDouble(double var1);

    @NotNull
    public ICPacketCloseWindow createCPacketCloseWindow();

    public boolean isItemTool(@Nullable Object var1);

    public boolean isItemBlock(@Nullable Object var1);

    @NotNull
    public IItem createItem();

    public boolean isEntityGolem(@Nullable Object var1);

    public boolean isCPacketCustomPayload(@Nullable Object var1);

    @NotNull
    public IEntity createEntityLightningBolt(@NotNull IWorld var1, double var2, double var4, double var6, boolean var8);

    public boolean isCPacketClientStatus(@Nullable Object var1);

    public boolean isItemEnderPearl(@Nullable Object var1);

    public boolean isTileEntityHopper(@Nullable Object var1);

    public boolean isItemBow(@Nullable Object var1);

    public boolean isEntityBoat(@Nullable Object var1);

    @NotNull
    public IEnchantment getEnchantmentEnum(@NotNull EnchantmentType var1);

    public boolean isItemFishingRod(@Nullable Object var1);

    public boolean isSPacketPlayerPosLook(@Nullable Object var1);

    @SupportsMinecraftVersions(value={MinecraftVersion.MC_1_12})
    @NotNull
    public PacketImpl createCPacketTryUseItem(@NotNull WEnumHand var1);

    @NotNull
    public IItem getItemEnum(@NotNull ItemType var1);

    public boolean isCPacketEntityAction(@Nullable Object var1);

    public boolean isBlockVine(@Nullable Object var1);

    public boolean isEntityItem(@Nullable Object var1);

    public boolean isGuiGameOver(@Nullable Object var1);

    @NotNull
    public IPacket createCPacketPlayerDigging(@NotNull ICPacketPlayerDigging.WAction var1, @NotNull WBlockPos var2, @NotNull IEnumFacing var3);

    @NotNull
    public IPotionEffect createPotionEffect(int var1, int var2, int var3);

    public boolean isCPacketKeepAlive(@Nullable Object var1);

    @NotNull
    public ICPacketKeepAlive createCPacketKeepAlive();

    public boolean isSPacketWindowItems(@Nullable Object var1);

    @NotNull
    public IItemStack createItemStack(@NotNull IItem var1, int var2, int var3);

    public boolean isItemSnowball(@Nullable Object var1);

    public boolean isEntitySlime(@Nullable Object var1);

    public boolean isEntityArmorStand(@Nullable Object var1);

    public boolean isSPacketExplosion(@Nullable Object var1);

    @NotNull
    public IDynamicTexture createDynamicTexture(@NotNull BufferedImage var1);

    public boolean isCPacketPlayerBlockPlacement(@Nullable Object var1);

    @NotNull
    public IItemStack createItemStack(@NotNull IItem var1);

    @NotNull
    public ICPacketPlayer createCPacketPlayerPosition(double var1, double var3, double var5, boolean var7);

    @NotNull
    public IFontRenderer wrapFontRenderer(@NotNull IWrappedFontRenderer var1);

    @NotNull
    public ICPacketPlayer createCPacketPlayer(boolean var1);

    @NotNull
    public ICPacketCustomPayload createCPacketCustomPayload(@NotNull String var1, @NotNull IPacketBuffer var2);

    public void wrapCreativeTab(@NotNull String var1, @NotNull WrappedCreativeTabs var2);

    @NotNull
    public ICPacketCloseWindow createCPacketCloseWindow(int var1);

    public boolean isItemBucket(@Nullable Object var1);

    public boolean isSPacketEntity(@Nullable Object var1);

    public boolean isEntityShulker(@Nullable Object var1);

    @NotNull
    public INBTTagString createNBTTagString(@NotNull String var1);

    public boolean isCPacketAnimation(@Nullable Object var1);

    public boolean isCPacketChatMessage(@Nullable Object var1);

    @NotNull
    public ICPacketEntityAction createCPacketEntityAction(@NotNull IEntity var1, @NotNull ICPacketEntityAction.WAction var2);

    public boolean isCPacketPlayerPosition(@Nullable Object var1);

    @NotNull
    public IPacketBuffer createPacketBuffer(@NotNull ByteBuf var1);

    public boolean isBlockStairs(@Nullable Object var1);

    @NotNull
    public IJsonToNBT getJsonToNBTInstance();

    public boolean isEntityLivingBase(@Nullable Object var1);

    @NotNull
    public ICPacketPlayerPosLook createCPacketPlayerPosLook(double var1, double var3, double var5, float var7, float var8, boolean var9);

    public boolean isSPacketAnimation(@Nullable Object var1);

    public boolean isCPacketPlayerPosLook(@Nullable Object var1);

    @NotNull
    public IGlStateManager getGlStateManager();

    public boolean isTileEntityEnderChest(@Nullable Object var1);

    @NotNull
    public IGuiScreen createGuiSelectWorld(@NotNull IGuiScreen var1);

    @NotNull
    public IGuiTextField createGuiPasswordField(int var1, @NotNull IFontRenderer var2, int var3, int var4, int var5, int var6);

    @NotNull
    public ICPacketHeldItemChange createCPacketHeldItemChange(int var1);

    public boolean isBlockCactus(@Nullable Object var1);

    @NotNull
    public ICPacketPlayer createCPacketPlayerLook(float var1, float var2, boolean var3);

    public boolean isBlockFence(@Nullable Object var1);

    @NotNull
    public IMaterial getMaterialEnum(@NotNull MaterialType var1);

    public boolean isItemAppleGold(@Nullable Object var1);

    public boolean isGuiChest(@Nullable Object var1);

    public boolean isGuiChat(@Nullable Object var1);

    public boolean isItemAir(@Nullable Object var1);

    public boolean isCPacketCloseWindow(@Nullable Object var1);

    public boolean isSPacketTimeUpdate(@Nullable Object var1);

    public boolean isCPacketPlayerDigging(@Nullable Object var1);

    @NotNull
    public IGuiScreen createGuiConnecting(@NotNull IGuiScreen var1, @NotNull IMinecraft var2, @NotNull IServerData var3);

    @SupportsMinecraftVersions(value={MinecraftVersion.MC_1_12})
    @NotNull
    public ICPacketPlayerBlockPlacement createCPacketPlayerBlockPlacement(@Nullable IItemStack var1);

    @NotNull
    public IItemStack createItemStack(@NotNull IBlock var1);

    public boolean isItemEgg(@Nullable Object var1);

    public boolean isCPacketHeldItemChange(@Nullable Object var1);

    public boolean isBlockPane(@Nullable Object var1);

    @NotNull
    public ICPacketPlayerBlockPlacement createCPacketPlayerBlockPlacement(@NotNull WBlockPos var1, int var2, @Nullable IItemStack var3, float var4, float var5, float var6);

    public boolean isItemArmor(@Nullable Object var1);

    @NotNull
    public IPacket createCPacketEncryptionResponse(@NotNull SecretKey var1, @NotNull PublicKey var2, @NotNull byte[] var3);

    @NotNull
    public IIChatComponent createChatComponentText(@NotNull String var1);

    @NotNull
    public IResourceLocation createResourceLocation(@NotNull String var1);

    public boolean isEntityPlayer(@Nullable Object var1);

    public boolean isGuiContainer(@Nullable Object var1);

    public boolean isSPacketEntityVelocity(@Nullable Object var1);

    public boolean isEntityVillager(@Nullable Object var1);

    public boolean isBlockBedrock(@Nullable Object var1);

    public boolean isItemFood(@Nullable Object var1);

    public boolean isItemSword(@Nullable Object var1);

    public boolean isEntityMinecart(@Nullable Object var1);

    @NotNull
    public IPacket createICPacketResourcePackStatus(@NotNull String var1, @NotNull ICPacketResourcePackStatus.WAction var2);

    public boolean isItemBucketMilk(@Nullable Object var1);

    public boolean isEntityTNTPrimed(@Nullable Object var1);

    public boolean isEntityDragon(@Nullable Object var1);

    public boolean isBlockCarpet(@Nullable Object var1);

    public boolean isGuiHudDesigner(@Nullable Object var1);

    @NotNull
    public INBTTagCompound createNBTTagCompound();

    public boolean isBlockLiquid(@Nullable Object var1);

    @NotNull
    public IBlock getBlockEnum(@NotNull BlockType var1);

    @NotNull
    public IEntityOtherPlayerMP createEntityOtherPlayerMP(@NotNull IWorldClient var1, @NotNull GameProfile var2);

    @NotNull
    public IGuiButton createGuiButton(int var1, int var2, int var3, @NotNull String var4);

    public boolean isBlockAir(@Nullable Object var1);

    public boolean isItemEnchantedBook(@Nullable Object var1);

    @NotNull
    public ICPacketClientStatus createCPacketClientStatus(@NotNull ICPacketClientStatus.WEnumState var1);

    public boolean isEntityFireball(@Nullable Object var1);

    @NotNull
    public IGuiScreen createGuiModList(@NotNull IGuiScreen var1);

    public boolean isEntityBat(@Nullable Object var1);

    public boolean isSPacketChat(@Nullable Object var1);

    @NotNull
    public ISession createSession(@NotNull String var1, @NotNull String var2, @NotNull String var3, @NotNull String var4);

    @NotNull
    public ICPacketAnimation createCPacketAnimation();

    @NotNull
    public ICPacketUseEntity createCPacketUseEntity(@NotNull IEntity var1, @NotNull ICPacketUseEntity.WAction var2);

    public boolean isTileEntityChest(@Nullable Object var1);

    public boolean isBlockBush(@Nullable Object var1);

    public boolean isSPacketTabComplete(@Nullable Object var1);

    public boolean isGuiIngameMenu(@Nullable Object var1);

    @NotNull
    public IGuiScreen wrapGuiScreen(@NotNull WrappedGuiScreen var1);

    public boolean isEntityMob(@Nullable Object var1);

    @NotNull
    public INBTTagList createNBTTagList();

    public boolean isTileEntityDispenser(@Nullable Object var1);

    public boolean isCPacketPlayer(@Nullable Object var1);
}

