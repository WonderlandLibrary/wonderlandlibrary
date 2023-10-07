/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C16PacketClientStatus$EnumState
 *  net.minecraft.network.play.server.S45PacketTitle
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.WorldSettings$GameType
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import me.report.liquidware.modules.movement.TargetStrafe;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.combat.Teams;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Aura", spacedName="Aura", description="Automatically attacks targets around you.", category=ModuleCategory.COMBAT, keyBind=19)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u00a4\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u0007\n\u0002\b\u0011\n\u0002\u0010!\n\u0002\b\u001d\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001:\u0002\u00a3\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0081\u0001\u001a\u00030\u0082\u00012\u0007\u0010\u0083\u0001\u001a\u00020%H\u0002J\u0013\u0010\u0084\u0001\u001a\u00020D2\b\u0010\u0083\u0001\u001a\u00030\u0085\u0001H\u0002J\u0016\u0010\u0086\u0001\u001a\u0005\u0018\u00010\u0087\u00012\b\u0010\u0083\u0001\u001a\u00030\u0085\u0001H\u0002J\u0012\u0010\u0088\u0001\u001a\u00020\u00142\u0007\u0010\u0083\u0001\u001a\u00020%H\u0002J\u0013\u0010\u0089\u0001\u001a\u00020\u00142\n\u0010\u0083\u0001\u001a\u0005\u0018\u00010\u0085\u0001J\n\u0010\u008a\u0001\u001a\u00030\u0082\u0001H\u0016J\n\u0010\u008b\u0001\u001a\u00030\u0082\u0001H\u0016J\u0014\u0010\u008c\u0001\u001a\u00030\u0082\u00012\b\u0010\u008d\u0001\u001a\u00030\u008e\u0001H\u0007J\u0014\u0010\u008f\u0001\u001a\u00030\u0082\u00012\b\u0010\u008d\u0001\u001a\u00030\u0090\u0001H\u0007J\u0014\u0010\u0091\u0001\u001a\u00030\u0082\u00012\b\u0010\u008d\u0001\u001a\u00030\u0092\u0001H\u0007J\u0014\u0010\u0093\u0001\u001a\u00030\u0082\u00012\b\u0010\u008d\u0001\u001a\u00030\u0094\u0001H\u0007J\u0014\u0010\u0095\u0001\u001a\u00030\u0082\u00012\b\u0010\u008d\u0001\u001a\u00030\u0096\u0001H\u0007J\u0014\u0010\u0097\u0001\u001a\u00030\u0082\u00012\b\u0010\u008d\u0001\u001a\u00030\u0098\u0001H\u0007J\n\u0010\u0099\u0001\u001a\u00030\u0082\u0001H\u0002J\u001d\u0010\u009a\u0001\u001a\u00030\u0082\u00012\b\u0010\u009b\u0001\u001a\u00030\u0085\u00012\u0007\u0010\u009c\u0001\u001a\u00020\u0014H\u0002J\n\u0010\u009d\u0001\u001a\u00030\u0082\u0001H\u0002J\b\u0010\u009e\u0001\u001a\u00030\u0082\u0001J\n\u0010\u009f\u0001\u001a\u00030\u0082\u0001H\u0002J\n\u0010\u00a0\u0001\u001a\u00030\u0082\u0001H\u0002J\u0013\u0010\u00a1\u0001\u001a\u00020\u00142\b\u0010\u0083\u0001\u001a\u00030\u0085\u0001H\u0002J\n\u0010\u00a2\u0001\u001a\u00030\u0082\u0001H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\u00020\u00148BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0016R\u0014\u0010\u001c\u001a\u00020\u00148BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0016R\u0014\u0010\u001e\u001a\u00020\u00148BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\u0016R\u000e\u0010 \u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010$\u001a\u0004\u0018\u00010%X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R\u000e\u0010*\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010.\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0016\"\u0004\b0\u0010\u0018R\u000e\u00101\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u00105\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u0016\"\u0004\b7\u0010\u0018R\u000e\u00108\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010>\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010C\u001a\u00020D8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\bE\u0010FR\u000e\u0010G\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010U\u001a\b\u0012\u0004\u0012\u00020\"0VX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010X\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Y\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Z\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010[\u001a\u00020-\u00a2\u0006\b\n\u0000\u001a\u0004\b\\\u0010]R\u000e\u0010^\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010_\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010`\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010a\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010b\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010c\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010d\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010f\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010g\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010h\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010i\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bj\u0010\u0016\"\u0004\bk\u0010\u0018R\u000e\u0010l\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010m\u001a\u00020DX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bn\u0010F\"\u0004\bo\u0010pR\u000e\u0010q\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010r\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010s\u001a\u0004\u0018\u00010t8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bu\u0010vR\u001c\u0010w\u001a\u0004\u0018\u00010%X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bx\u0010'\"\u0004\by\u0010)R\u0011\u0010z\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b{\u0010\u0011R\u000e\u0010|\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010}\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010~\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000f\n\u0000\u001a\u0004\b\u007f\u0010\u0016\"\u0005\b\u0080\u0001\u0010\u0018\u00a8\u0006\u00a4\u0001"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aacValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "abThruWallValue", "accuracyValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "afterTickPatchValue", "alpha", "attackDelay", "", "attackTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "autoBlockModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getAutoBlockModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "blockRate", "blockingStatus", "", "getBlockingStatus", "()Z", "setBlockingStatus", "(Z)V", "blue", "canBlock", "getCanBlock", "canSmartBlock", "getCanSmartBlock", "cancelRun", "getCancelRun", "circleValue", "clicks", "", "containerOpen", "currentTarget", "Lnet/minecraft/entity/EntityLivingBase;", "getCurrentTarget", "()Lnet/minecraft/entity/EntityLivingBase;", "setCurrentTarget", "(Lnet/minecraft/entity/EntityLivingBase;)V", "debugValue", "displayAutoBlockSettings", "failRateValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "fakeBlock", "getFakeBlock", "setFakeBlock", "fakeSharpValue", "fakeSwingValue", "fovValue", "green", "hitable", "getHitable", "setHitable", "hurtTimeValue", "interactAutoBlockValue", "keepSprintValue", "lastHitTick", "limitedMultiTargetsValue", "livingRaycastValue", "markEntity", "markTimer", "maxCPS", "maxPredictSize", "maxRand", "maxRange", "", "getMaxRange", "()F", "maxSpinSpeed", "maxTurnSpeed", "minCPS", "minPredictSize", "minRand", "minSpinSpeed", "minTurnSpeed", "noHitCheck", "noInventoryAttackValue", "noInventoryDelayValue", "noScaffValue", "noSendRot", "outborderValue", "predictValue", "prevTargetEntities", "", "priorityValue", "randomCenterNewValue", "randomCenterValue", "rangeSprintReducementValue", "rangeValue", "getRangeValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "raycastIgnoredValue", "raycastValue", "red", "rotationStrafeValue", "rotations", "silentRotationValue", "smartABFacingValue", "smartABItemValue", "smartABRangeValue", "smartABTolerationValue", "smartAutoBlockValue", "smartBlocking", "getSmartBlocking", "setSmartBlocking", "spinHurtTimeValue", "spinYaw", "getSpinYaw", "setSpinYaw", "(F)V", "swingValue", "switchDelayValue", "tag", "", "getTag", "()Ljava/lang/String;", "target", "getTarget", "setTarget", "targetModeValue", "getTargetModeValue", "throughWallsRangeValue", "verusAutoBlockValue", "verusBlocking", "getVerusBlocking", "setVerusBlocking", "attackEntity", "", "entity", "getRange", "Lnet/minecraft/entity/Entity;", "getTargetRotation", "Lnet/ccbluex/liquidbounce/utils/Rotation;", "isAlive", "isEnemy", "onDisable", "onEnable", "onEntityMove", "event", "Lnet/ccbluex/liquidbounce/event/EntityMovementEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onStrafe", "Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "runAttack", "startBlocking", "interactEntity", "interact", "stopBlocking", "update", "updateHitable", "updateKA", "updateRotations", "updateTarget", "CombatListener", "KyinoClient"})
public final class KillAura
extends Module {
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)KillAura.access$getMinCPS$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
            KillAura.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)KillAura.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)KillAura.access$getMaxCPS$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
            KillAura.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)KillAura.access$getMaxCPS$p(this.this$0).get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    @NotNull
    private final FloatValue rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
    private final FloatValue throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
    private final FloatValue rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
    private final ListValue rotations = new ListValue("RotationMode", new String[]{"Vanilla", "BackTrack", "Spin", "None"}, "BackTrack");
    private final IntegerValue spinHurtTimeValue = new IntegerValue("Spin-HitHurtTime", 10, 0, 10);
    private final FloatValue maxSpinSpeed = new FloatValue(this, "MaxSpinSpeed", 180.0f, 0.0f, 180.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMinSpinSpeed$p(this.this$0).get()).floatValue();
            if (v > newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final FloatValue minSpinSpeed = new FloatValue(this, "MinSpinSpeed", 180.0f, 0.0f, 180.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMaxSpinSpeed$p(this.this$0).get()).floatValue();
            if (v < newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue noSendRot = new BoolValue("NoSendRotation", true);
    private final BoolValue noHitCheck = new BoolValue("NoHitCheck", false);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime"}, "Distance");
    @NotNull
    private final ListValue targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
    private final IntegerValue switchDelayValue = new IntegerValue("SwitchDelay", 1000, 1, 2000);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue keepSprintValue = new BoolValue("KeepSprint", true);
    @NotNull
    private final ListValue autoBlockModeValue = new ListValue("AutoBlock", new String[]{"None", "Packet", "AfterTick", "NCP", "OldHypixel"}, "None");
    private final BoolValue displayAutoBlockSettings = new BoolValue("Open-AutoBlock-Settings", false);
    private final BoolValue interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
    private final BoolValue verusAutoBlockValue = new BoolValue("VerusAutoBlock", false);
    private final BoolValue abThruWallValue = new BoolValue("AutoBlockThroughWalls", false);
    private final BoolValue smartAutoBlockValue = new BoolValue("SmartAutoBlock", false);
    private final BoolValue smartABItemValue = new BoolValue("SmartAutoBlock-ItemCheck", true);
    private final BoolValue smartABFacingValue = new BoolValue("SmartAutoBlock-FacingCheck", true);
    private final FloatValue smartABRangeValue = new FloatValue("SmartAB-Range", 3.5f, 3.0f, 8.0f);
    private final FloatValue smartABTolerationValue = new FloatValue("SmartAB-Toleration", 0.0f, 0.0f, 2.0f);
    private final BoolValue afterTickPatchValue = new BoolValue("AfterTickPatch", true);
    private final IntegerValue blockRate = new IntegerValue("BlockRate", 100, 1, 100);
    private final BoolValue raycastValue = new BoolValue("RayCast", true);
    private final BoolValue raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
    private final BoolValue livingRaycastValue = new BoolValue("LivingRayCast", true);
    private final BoolValue aacValue = new BoolValue("AAC", false);
    private final FloatValue maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
            if (v > newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final FloatValue minTurnSpeed = new FloatValue(this, "MinTurnSpeed", 180.0f, 0.0f, 180.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
            if (v < newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue silentRotationValue = new BoolValue("SilentRotation", true);
    private final ListValue rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off");
    private final FloatValue fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
    private final BoolValue predictValue = new BoolValue("Predict", true);
    private final FloatValue maxPredictSize = new FloatValue(this, "MaxPredictSize", 1.0f, 0.1f, 5.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMinPredictSize$p(this.this$0).get()).floatValue();
            if (v > newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final FloatValue minPredictSize = new FloatValue(this, "MinPredictSize", 1.0f, 0.1f, 5.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMaxPredictSize$p(this.this$0).get()).floatValue();
            if (v < newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue randomCenterValue = new BoolValue("RandomCenter", false);
    private final BoolValue randomCenterNewValue = new BoolValue("NewCalc", true);
    private final FloatValue minRand = new FloatValue(this, "MinMultiply", 0.8f, 0.0f, 2.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMaxRand$p(this.this$0).get()).floatValue();
            if (v < newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final FloatValue maxRand = new FloatValue(this, "MaxMultiply", 0.8f, 0.0f, 2.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMinRand$p(this.this$0).get()).floatValue();
            if (v > newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue outborderValue = new BoolValue("Outborder", false);
    private final FloatValue failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
    private final BoolValue fakeSwingValue = new BoolValue("FakeSwing", true);
    private final BoolValue noInventoryAttackValue = new BoolValue("NoInvAttack", false);
    private final IntegerValue noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
    private final IntegerValue limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50);
    private final BoolValue noScaffValue = new BoolValue("NoScaffold", true);
    private final BoolValue debugValue = new BoolValue("Debug", false);
    private final BoolValue circleValue = new BoolValue("Circle", true);
    private final IntegerValue accuracyValue = new IntegerValue("Accuracy", 59, 0, 59);
    private final BoolValue fakeSharpValue = new BoolValue("FakeSharp", true);
    private final IntegerValue red = new IntegerValue("Red", 0, 0, 255);
    private final IntegerValue green = new IntegerValue("Green", 0, 0, 255);
    private final IntegerValue blue = new IntegerValue("Blue", 0, 0, 255);
    private final IntegerValue alpha = new IntegerValue("Alpha", 0, 0, 255);
    @Nullable
    private EntityLivingBase target;
    @Nullable
    private EntityLivingBase currentTarget;
    private boolean hitable;
    private final List<Integer> prevTargetEntities;
    private EntityLivingBase markEntity;
    private final MSTimer markTimer;
    private final MSTimer attackTimer;
    private long attackDelay;
    private int clicks;
    private int lastHitTick;
    private long containerOpen;
    private boolean blockingStatus;
    private boolean verusBlocking;
    private boolean fakeBlock;
    private boolean smartBlocking;
    private float spinYaw;

    @NotNull
    public final FloatValue getRangeValue() {
        return this.rangeValue;
    }

    @NotNull
    public final ListValue getTargetModeValue() {
        return this.targetModeValue;
    }

    @NotNull
    public final ListValue getAutoBlockModeValue() {
        return this.autoBlockModeValue;
    }

    @Nullable
    public final EntityLivingBase getTarget() {
        return this.target;
    }

    public final void setTarget(@Nullable EntityLivingBase entityLivingBase) {
        this.target = entityLivingBase;
    }

    @Nullable
    public final EntityLivingBase getCurrentTarget() {
        return this.currentTarget;
    }

    public final void setCurrentTarget(@Nullable EntityLivingBase entityLivingBase) {
        this.currentTarget = entityLivingBase;
    }

    public final boolean getHitable() {
        return this.hitable;
    }

    public final void setHitable(boolean bl) {
        this.hitable = bl;
    }

    public final boolean getBlockingStatus() {
        return this.blockingStatus;
    }

    public final void setBlockingStatus(boolean bl) {
        this.blockingStatus = bl;
    }

    public final boolean getVerusBlocking() {
        return this.verusBlocking;
    }

    public final void setVerusBlocking(boolean bl) {
        this.verusBlocking = bl;
    }

    public final boolean getFakeBlock() {
        return this.fakeBlock;
    }

    public final void setFakeBlock(boolean bl) {
        this.fakeBlock = bl;
    }

    public final boolean getSmartBlocking() {
        return this.smartBlocking;
    }

    public final void setSmartBlocking(boolean bl) {
        this.smartBlocking = bl;
    }

    private final boolean getCanSmartBlock() {
        return (Boolean)this.smartAutoBlockValue.get() == false || this.smartBlocking;
    }

    public final float getSpinYaw() {
        return this.spinYaw;
    }

    public final void setSpinYaw(float f) {
        this.spinYaw = f;
    }

    @Override
    public void onEnable() {
        if (KillAura.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        if (KillAura.access$getMc$p$s1046033730().field_71441_e == null) {
            return;
        }
        this.updateTarget();
        this.verusBlocking = false;
        this.smartBlocking = false;
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.currentTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.clicks = 0;
        this.stopBlocking();
        if (this.verusBlocking && !this.blockingStatus) {
            EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (!entityPlayerSP.func_70632_aY()) {
                this.verusBlocking = false;
                if (((Boolean)this.verusAutoBlockValue.get()).booleanValue()) {
                    PacketUtils.sendPacketNoEvent((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
                }
            }
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getEventState() == EventState.POST) {
            if (this.target == null) {
                return;
            }
            if (this.currentTarget == null) {
                return;
            }
            this.updateHitable();
            if (StringsKt.equals((String)this.autoBlockModeValue.get(), "AfterTick", true) && this.getCanBlock()) {
                EntityLivingBase entityLivingBase = this.currentTarget;
                if (entityLivingBase == null) {
                    Intrinsics.throwNpe();
                }
                this.startBlocking((Entity)entityLivingBase, this.hitable);
            }
        }
        if (StringsKt.equals((String)this.rotationStrafeValue.get(), "Off", true)) {
            this.update();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(TargetStrafe.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type me.report.liquidware.modules.movement.TargetStrafe");
        }
        TargetStrafe targetStrafe = (TargetStrafe)module;
        if (StringsKt.equals((String)this.rotationStrafeValue.get(), "Off", true) && !targetStrafe.getState()) {
            return;
        }
        this.update();
        if (this.currentTarget == null || RotationUtils.targetRotation == null) return;
        if (targetStrafe.canStrafe()) {
            Rotation rotation = RotationUtils.targetRotation;
            if (rotation == null) {
                return;
            }
            Rotation rotation2 = rotation;
            float yaw = rotation2.component1();
            float strafe = event.getStrafe();
            float forward = event.getForward();
            float friction = event.getFriction();
            float f = strafe * strafe + forward * forward;
            if (f >= 1.0E-4f) {
                if ((f = MathHelper.func_76129_c((float)f)) < 1.0f) {
                    f = 1.0f;
                }
                f = friction / f;
                float yawSin = MathHelper.func_76126_a((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                float yawCos = MathHelper.func_76134_b((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                KillAura.access$getMc$p$s1046033730().field_71439_g.field_70159_w += (double)((strafe *= f) * yawCos - (forward *= f) * yawSin);
                KillAura.access$getMc$p$s1046033730().field_71439_g.field_70179_y += (double)(forward * yawCos + strafe * yawSin);
            }
            event.cancelEvent();
            return;
        } else {
            String string = (String)this.rotationStrafeValue.get();
            boolean strafe = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            string = string3;
            switch (string.hashCode()) {
                case -902327211: {
                    if (!string.equals("silent")) return;
                    break;
                }
                case -891986231: {
                    if (!string.equals("strict")) return;
                    Rotation rotation = RotationUtils.targetRotation;
                    if (rotation == null) {
                        return;
                    }
                    Rotation forward = rotation;
                    float yaw = forward.component1();
                    float strafe2 = event.getStrafe();
                    float forward2 = event.getForward();
                    float friction = event.getFriction();
                    float f = strafe2 * strafe2 + forward2 * forward2;
                    if (f >= 1.0E-4f) {
                        if ((f = MathHelper.func_76129_c((float)f)) < 1.0f) {
                            f = 1.0f;
                        }
                        f = friction / f;
                        float yawSin = MathHelper.func_76126_a((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                        float yawCos = MathHelper.func_76134_b((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                        KillAura.access$getMc$p$s1046033730().field_71439_g.field_70159_w += (double)((strafe2 *= f) * yawCos - (forward2 *= f) * yawSin);
                        KillAura.access$getMc$p$s1046033730().field_71439_g.field_70179_y += (double)(forward2 * yawCos + strafe2 * yawSin);
                    }
                    event.cancelEvent();
                    return;
                }
            }
            this.update();
            RotationUtils.targetRotation.applyStrafeToPlayer(event);
            event.cancelEvent();
        }
    }

    public final void update() {
        if (this.getCancelRun() || ((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (KillAura.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            return;
        }
        this.updateTarget();
        if (this.target == null) {
            this.stopBlocking();
            return;
        }
        this.currentTarget = this.target;
        if (!StringsKt.equals((String)this.targetModeValue.get(), "Switch", true) && this.isEnemy((Entity)this.currentTarget)) {
            this.target = this.currentTarget;
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (this.verusBlocking && (packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)packet).func_180762_c() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM || packet instanceof C08PacketPlayerBlockPlacement) && ((Boolean)this.verusAutoBlockValue.get()).booleanValue()) {
            event.cancelEvent();
        }
        if (packet instanceof C09PacketHeldItemChange) {
            this.verusBlocking = false;
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.updateKA();
        this.smartBlocking = false;
        if (((Boolean)this.smartAutoBlockValue.get()).booleanValue() && this.target != null) {
            EntityLivingBase entityLivingBase = this.target;
            if (entityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            EntityLivingBase smTarget = entityLivingBase;
            if (!((Boolean)this.smartABItemValue.get()).booleanValue() || smTarget.func_70694_bm() != null && smTarget.func_70694_bm().func_77973_b() != null && (smTarget.func_70694_bm().func_77973_b() instanceof ItemSword || smTarget.func_70694_bm().func_77973_b() instanceof ItemAxe)) {
                EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, (Entity)smTarget) < ((Number)this.smartABRangeValue.get()).doubleValue()) {
                    if (((Boolean)this.smartABFacingValue.get()).booleanValue()) {
                        if (smTarget.func_174822_a((double)((double)((Number)this.smartABRangeValue.get()).floatValue()), (float)1.0f).field_72313_a == MovingObjectPosition.MovingObjectType.MISS) {
                            Vec3 eyesVec = smTarget.func_174824_e(1.0f);
                            Vec3 lookVec = smTarget.func_70676_i(1.0f);
                            Vec3 pointingVec = eyesVec.func_72441_c(lookVec.field_72450_a * ((Number)this.smartABRangeValue.get()).doubleValue(), lookVec.field_72448_b * ((Number)this.smartABRangeValue.get()).doubleValue(), lookVec.field_72449_c * ((Number)this.smartABRangeValue.get()).doubleValue());
                            float border = KillAura.access$getMc$p$s1046033730().field_71439_g.func_70111_Y() + ((Number)this.smartABTolerationValue.get()).floatValue();
                            EntityPlayerSP entityPlayerSP2 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                            AxisAlignedBB bb = entityPlayerSP2.func_174813_aQ().func_72314_b((double)border, (double)border, (double)border);
                            this.smartBlocking = bb.func_72327_a(eyesVec, pointingVec) != null || bb.func_72326_a(smTarget.func_174813_aQ());
                        }
                    } else {
                        this.smartBlocking = true;
                    }
                }
            }
        }
        if (this.blockingStatus || KillAura.access$getMc$p$s1046033730().field_71439_g.func_70632_aY()) {
            this.verusBlocking = true;
        } else if (this.verusBlocking) {
            this.verusBlocking = false;
            if (((Boolean)this.verusAutoBlockValue.get()).booleanValue()) {
                PacketUtils.sendPacketNoEvent((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            }
        }
    }

    private final void updateKA() {
        if (this.getCancelRun()) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (KillAura.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (KillAura.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target != null && this.currentTarget != null) {
            while (this.clicks > 0) {
                this.runAttack();
                int n = this.clicks;
                this.clicks = n + -1;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        int n;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.circleValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)(KillAura.access$getMc$p$s1046033730().field_71439_g.field_70142_S + (KillAura.access$getMc$p$s1046033730().field_71439_g.field_70165_t - KillAura.access$getMc$p$s1046033730().field_71439_g.field_70142_S) * (double)KillAura.access$getMc$p$s1046033730().field_71428_T.field_74281_c - KillAura.access$getMc$p$s1046033730().func_175598_ae().field_78725_b), (double)(KillAura.access$getMc$p$s1046033730().field_71439_g.field_70137_T + (KillAura.access$getMc$p$s1046033730().field_71439_g.field_70163_u - KillAura.access$getMc$p$s1046033730().field_71439_g.field_70137_T) * (double)KillAura.access$getMc$p$s1046033730().field_71428_T.field_74281_c - KillAura.access$getMc$p$s1046033730().func_175598_ae().field_78726_c), (double)(KillAura.access$getMc$p$s1046033730().field_71439_g.field_70136_U + (KillAura.access$getMc$p$s1046033730().field_71439_g.field_70161_v - KillAura.access$getMc$p$s1046033730().field_71439_g.field_70136_U) * (double)KillAura.access$getMc$p$s1046033730().field_71428_T.field_74281_c - KillAura.access$getMc$p$s1046033730().func_175598_ae().field_78723_d));
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)((float)((Number)this.red.get()).intValue() / 255.0f), (float)((float)((Number)this.green.get()).intValue() / 255.0f), (float)((float)((Number)this.blue.get()).intValue() / 255.0f), (float)((float)((Number)this.alpha.get()).intValue() / 255.0f));
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glBegin((int)3);
            int n2 = 0;
            IntProgression intProgression = RangesKt.step(new IntRange(n2, 360), 60 - ((Number)this.accuracyValue.get()).intValue());
            n = intProgression.getFirst();
            int n3 = intProgression.getLast();
            int n4 = intProgression.getStep();
            int n5 = n;
            int n6 = n3;
            if (n4 >= 0 ? n5 <= n6 : n5 >= n6) {
                while (true) {
                    void i;
                    GL11.glVertex2f((float)((float)Math.cos((double)i * Math.PI / 180.0) * ((Number)this.rangeValue.get()).floatValue()), (float)((float)Math.sin((double)i * Math.PI / 180.0) * ((Number)this.rangeValue.get()).floatValue()));
                    if (i == n3) break;
                    n = i + n4;
                }
            }
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        if (this.getCancelRun()) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (KillAura.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (KillAura.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target == null) {
            return;
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            EntityLivingBase entityLivingBase = this.currentTarget;
            if (entityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            if (entityLivingBase.field_70737_aN <= ((Number)this.hurtTimeValue.get()).intValue()) {
                n = this.clicks;
                this.clicks = n + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
    }

    @EventTarget
    public final void onEntityMove(@NotNull EntityMovementEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Entity movedEntity = event.getMovedEntity();
        if (this.target == null || Intrinsics.areEqual(movedEntity, this.currentTarget) ^ true) {
            return;
        }
        this.updateHitable();
    }

    /*
     * Unable to fully structure code
     */
    private final void runAttack() {
        if (this.target == null) {
            return;
        }
        if (this.currentTarget == null) {
            return;
        }
        failRate = ((Number)this.failRateValue.get()).floatValue();
        swing = (Boolean)this.swingValue.get();
        multi = StringsKt.equals((String)this.targetModeValue.get(), "Multi", true);
        v0 = openInventory = (Boolean)this.aacValue.get() != false && KillAura.access$getMc$p$s1046033730().field_71462_r instanceof GuiInventory != false;
        if (!(failRate > (float)false)) ** GOTO lbl-1000
        v1 = new Random();
        if ((float)v1.nextInt(100) <= failRate) {
            v2 = true;
        } else lbl-1000:
        // 2 sources

        {
            v2 = failHit = false;
        }
        if (openInventory) {
            v3 = KillAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(v3, "mc");
            v3.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
        }
        if (!this.hitable || failHit) {
            if (swing && (((Boolean)this.fakeSwingValue.get()).booleanValue() || failHit)) {
                KillAura.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
            }
        } else {
            if (!multi) {
                v4 = this.currentTarget;
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                this.attackEntity(v4);
            } else {
                targets = 0;
                for (Entity entity : KillAura.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
                    v5 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(v5, "mc.thePlayer");
                    v6 = (Entity)v5;
                    v7 = entity;
                    Intrinsics.checkExpressionValueIsNotNull(v7, "entity");
                    distance = PlayerExtensionKt.getDistanceToEntityBox(v6, v7);
                    if (!(entity instanceof EntityLivingBase) || !this.isEnemy(entity) || !(distance <= (double)this.getRange(entity))) continue;
                    this.attackEntity((EntityLivingBase)entity);
                    if (((Number)this.limitedMultiTargetsValue.get()).intValue() == 0 || ((Number)this.limitedMultiTargetsValue.get()).intValue() > ++targets) continue;
                    break;
                }
            }
            if (((Boolean)this.aacValue.get()).booleanValue()) {
                v8 = this.target;
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                v9 = v8.func_145782_y();
            } else {
                v10 = this.currentTarget;
                if (v10 == null) {
                    Intrinsics.throwNpe();
                }
                v9 = v10.func_145782_y();
            }
            this.prevTargetEntities.add(v9);
            if (Intrinsics.areEqual(this.target, this.currentTarget)) {
                this.target = null;
            }
        }
        if (StringsKt.equals((String)this.targetModeValue.get(), "Switch", true) && this.attackTimer.hasTimePassed(((Number)this.switchDelayValue.get()).intValue()) && ((Number)this.switchDelayValue.get()).intValue() != 0) {
            if (((Boolean)this.aacValue.get()).booleanValue()) {
                v11 = this.target;
                if (v11 == null) {
                    Intrinsics.throwNpe();
                }
                v12 = v11.func_145782_y();
            } else {
                v13 = this.currentTarget;
                if (v13 == null) {
                    Intrinsics.throwNpe();
                }
                v12 = v13.func_145782_y();
            }
            this.prevTargetEntities.add(v12);
            this.attackTimer.reset();
        }
        if (openInventory) {
            v14 = KillAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(v14, "mc");
            v14.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        }
    }

    private final void updateTarget() {
        Object searchTarget = null;
        int hurtTime = ((Number)this.hurtTimeValue.get()).intValue();
        float fov = ((Number)this.fovValue.get()).floatValue();
        boolean switchMode = StringsKt.equals((String)this.targetModeValue.get(), "Switch", true);
        boolean bl = false;
        List targets = new ArrayList();
        for (Entity entity : KillAura.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase) || !this.isEnemy(entity) || switchMode && this.prevTargetEntities.contains(((EntityLivingBase)entity).func_145782_y())) continue;
            EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            double distance = PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity);
            double entityFov = RotationUtils.getRotationDifference(entity);
            if (!(distance <= (double)this.getMaxRange()) || fov != 180.0f && !(entityFov <= (double)fov) || ((EntityLivingBase)entity).field_70737_aN > hurtTime) continue;
            targets.add(entity);
        }
        String entity = (String)this.priorityValue.get();
        boolean bl2 = false;
        String string = entity;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string2 = string.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).toLowerCase()");
        switch (string2) {
            case "distance": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl3 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl2 = false;
                        EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                        Comparable comparable = Double.valueOf(PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, (Entity)it));
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        EntityPlayerSP entityPlayerSP2 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                        Double d = PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP2, (Entity)it);
                        return ComparisonsKt.compareValues(comparable2, (Comparable)d);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
                break;
            }
            case "health": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl4 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Float.valueOf(it.func_110143_aJ());
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Float f = Float.valueOf(it.func_110143_aJ());
                        return ComparisonsKt.compareValues(comparable2, (Comparable)f);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
                break;
            }
            case "direction": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl5 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Double.valueOf(RotationUtils.getRotationDifference((Entity)it));
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Double d = RotationUtils.getRotationDifference((Entity)it);
                        return ComparisonsKt.compareValues(comparable2, (Comparable)d);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
                break;
            }
            case "livingtime": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl6 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl2 = false;
                        it = (EntityLivingBase)b;
                        Comparable comparable = Integer.valueOf(-it.field_70173_aa);
                        bl2 = false;
                        Integer n = -it.field_70173_aa;
                        return ComparisonsKt.compareValues(comparable, (Comparable)n);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
            }
        }
        boolean found = false;
        for (EntityLivingBase entity2 : targets) {
            if (!this.updateRotations((Entity)entity2)) continue;
            this.target = entity2;
            found = true;
            break;
        }
        if (found) {
            if (StringsKt.equals((String)this.rotations.get(), "spin", true)) {
                this.spinYaw += RandomUtils.nextFloat(((Number)this.minSpinSpeed.get()).floatValue(), ((Number)this.maxSpinSpeed.get()).floatValue());
                this.spinYaw = MathHelper.func_76142_g((float)this.spinYaw);
                Rotation rot = new Rotation(this.spinYaw, 90.0f);
                RotationUtils.setTargetRotation(rot, 0);
            }
            return;
        }
        this.target = null;
        Collection collection = this.prevTargetEntities;
        boolean bl7 = false;
        if (!collection.isEmpty()) {
            this.prevTargetEntities.clear();
            this.updateTarget();
        }
    }

    public final boolean isEnemy(@Nullable Entity entity) {
        if (entity instanceof EntityLivingBase && (EntityUtils.targetDead || this.isAlive((EntityLivingBase)entity)) && Intrinsics.areEqual(entity, KillAura.access$getMc$p$s1046033730().field_71439_g) ^ true) {
            if (!EntityUtils.targetInvisible && entity.func_82150_aj()) {
                return false;
            }
            if (EntityUtils.targetPlayer && entity instanceof EntityPlayer) {
                if (((EntityPlayer)entity).func_175149_v() || AntiBot.isBot((EntityLivingBase)entity)) {
                    return false;
                }
                if (EntityUtils.isFriend(entity)) {
                    Module module = LiquidBounce.INSTANCE.getModuleManager().get(NoFriends.class);
                    if (module == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!module.getState()) {
                        return false;
                    }
                }
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(Teams.class);
                if (module == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.Teams");
                }
                Teams teams = (Teams)module;
                return !teams.getState() || !teams.isInYourTeam((EntityLivingBase)entity);
            }
            return EntityUtils.targetMobs && EntityUtils.isMob(entity) || EntityUtils.targetAnimals && EntityUtils.isAnimal(entity);
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    private final void attackEntity(EntityLivingBase entity) {
        EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_70632_aY() || this.blockingStatus) {
            this.stopBlocking();
        }
        LiquidBounce.INSTANCE.getEventManager().callEvent(new AttackEvent((Entity)entity));
        this.markEntity = entity;
        if (StringsKt.equals((String)this.rotations.get(), "spin", true) && !((Boolean)this.noSendRot.get()).booleanValue()) {
            Rotation rotation = this.getTargetRotation((Entity)entity);
            if (rotation == null) {
                return;
            }
            Rotation targetedRotation = rotation;
            Minecraft minecraft = KillAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(targetedRotation.getYaw(), targetedRotation.getPitch(), KillAura.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
            if (((Boolean)this.debugValue.get()).booleanValue()) {
                ClientUtils.displayChatMessage("[KillAura] Silent rotation change.");
            }
        }
        if (((Boolean)this.swingValue.get()).booleanValue()) {
            KillAura.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
        }
        Minecraft minecraft = KillAura.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
        if (((Boolean)this.keepSprintValue.get()).booleanValue()) {
            if (KillAura.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 0.0f && !KillAura.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                EntityPlayerSP entityPlayerSP2 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                if (!entityPlayerSP2.func_70617_f_()) {
                    EntityPlayerSP entityPlayerSP3 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                    if (!entityPlayerSP3.func_70090_H() && !KillAura.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76440_q)) {
                        EntityPlayerSP entityPlayerSP4 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
                        if (!entityPlayerSP4.func_70115_ae()) {
                            KillAura.access$getMc$p$s1046033730().field_71439_g.func_71009_b((Entity)entity);
                        }
                    }
                }
            }
            EntityPlayerSP entityPlayerSP5 = KillAura.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc.thePlayer");
            if (EnchantmentHelper.func_152377_a((ItemStack)entityPlayerSP5.func_70694_bm(), (EnumCreatureAttribute)entity.func_70668_bt()) > 0.0f) {
                KillAura.access$getMc$p$s1046033730().field_71439_g.func_71047_c((Entity)entity);
            }
        } else if (KillAura.access$getMc$p$s1046033730().field_71442_b.field_78779_k != WorldSettings.GameType.SPECTATOR) {
            KillAura.access$getMc$p$s1046033730().field_71439_g.func_71059_n((Entity)entity);
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Criticals.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.Criticals");
        }
        Criticals criticals = (Criticals)module;
        int n = 0;
        int n2 = 2;
        while (n <= n2) {
            void i;
            block25: {
                block24: {
                    block23: {
                        if (!(KillAura.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 0.0f) || KillAura.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break block23;
                        EntityPlayerSP entityPlayerSP6 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP6, "mc.thePlayer");
                        if (entityPlayerSP6.func_70617_f_()) break block23;
                        EntityPlayerSP entityPlayerSP7 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP7, "mc.thePlayer");
                        if (!entityPlayerSP7.func_70090_H() && !KillAura.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76440_q) && KillAura.access$getMc$p$s1046033730().field_71439_g.field_70154_o == null) break block24;
                    }
                    if (!criticals.getState() || !criticals.getMsTimer().hasTimePassed(((Number)criticals.getDelayValue().get()).intValue())) break block25;
                    EntityPlayerSP entityPlayerSP8 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP8, "mc.thePlayer");
                    if (entityPlayerSP8.func_70090_H()) break block25;
                    EntityPlayerSP entityPlayerSP9 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP9, "mc.thePlayer");
                    if (entityPlayerSP9.func_180799_ab() || KillAura.access$getMc$p$s1046033730().field_71439_g.field_70134_J) break block25;
                }
                KillAura.access$getMc$p$s1046033730().field_71439_g.func_71009_b((Entity)this.target);
            }
            EntityPlayerSP entityPlayerSP10 = KillAura.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP10, "mc.thePlayer");
            ItemStack itemStack = entityPlayerSP10.func_70694_bm();
            EntityLivingBase entityLivingBase = this.target;
            if (entityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            if (EnchantmentHelper.func_152377_a((ItemStack)itemStack, (EnumCreatureAttribute)entityLivingBase.func_70668_bt()) > 0.0f || ((Boolean)this.fakeSharpValue.get()).booleanValue()) {
                KillAura.access$getMc$p$s1046033730().field_71439_g.func_71047_c((Entity)this.target);
            }
            ++i;
        }
        if (!((Boolean)this.afterTickPatchValue.get()).booleanValue() || !StringsKt.equals((String)this.autoBlockModeValue.get(), "AfterTick", true)) {
            EntityPlayerSP entityPlayerSP11 = KillAura.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP11, "mc.thePlayer");
            if (entityPlayerSP11.func_70632_aY() || this.getCanBlock()) {
                this.startBlocking((Entity)entity, (Boolean)this.interactAutoBlockValue.get());
            }
        }
    }

    private final boolean updateRotations(Entity entity) {
        if (StringsKt.equals((String)this.rotations.get(), "none", true)) {
            return true;
        }
        Rotation rotation = this.getTargetRotation(entity);
        if (rotation == null) {
            return false;
        }
        Rotation defRotation = rotation;
        if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
            RotationUtils.setTargetRotation(defRotation, (Boolean)this.aacValue.get() != false && !StringsKt.equals((String)this.rotations.get(), "Spin", true) ? 15 : 0);
        } else {
            EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            defRotation.toPlayer((EntityPlayer)entityPlayerSP);
        }
        return true;
    }

    private final Rotation getTargetRotation(Entity entity) {
        AxisAlignedBB boundingBox = entity.func_174813_aQ();
        if (StringsKt.equals((String)this.rotations.get(), "Vanilla", true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return RotationUtils.serverRotation;
            }
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.func_72317_d((entity.field_70165_t - entity.field_70169_q) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70163_u - entity.field_70167_r) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70161_v - entity.field_70166_s) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            boolean bl = (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2);
            boolean bl2 = (Boolean)this.randomCenterValue.get();
            boolean bl3 = (Boolean)this.predictValue.get();
            EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, bl, bl2, bl3, PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue());
            if (vecRotation == null) {
                return null;
            }
            VecRotation vecRotation2 = vecRotation;
            Rotation rotation = vecRotation2.component2();
            Rotation rotation2 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull(rotation2, "RotationUtils.limitAngle\u2026rnSpeed.get()).toFloat())");
            Rotation limitedRotation = rotation2;
            return limitedRotation;
        }
        if (StringsKt.equals((String)this.rotations.get(), "Spin", true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return RotationUtils.serverRotation;
            }
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.func_72317_d((entity.field_70165_t - entity.field_70169_q) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70163_u - entity.field_70167_r) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70161_v - entity.field_70166_s) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, false, false, false, PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue());
            if (vecRotation == null) {
                return null;
            }
            VecRotation vecRotation3 = vecRotation;
            Rotation rotation = vecRotation3.component2();
            return rotation;
        }
        if (StringsKt.equals((String)this.rotations.get(), "BackTrack", true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.func_72317_d((entity.field_70165_t - entity.field_70169_q) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70163_u - entity.field_70167_r) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70161_v - entity.field_70166_s) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            Vec3 vec3 = RotationUtils.getCenter(entity.func_174813_aQ());
            boolean bl = (Boolean)this.predictValue.get();
            EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            Rotation rotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.OtherRotation(boundingBox, vec3, bl, PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange()), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull(rotation, "RotationUtils.limitAngle\u2026rnSpeed.get()).toFloat())");
            Rotation limitedRotation = rotation;
            return limitedRotation;
        }
        return RotationUtils.serverRotation;
    }

    private final void updateHitable() {
        if (StringsKt.equals((String)this.rotations.get(), "none", true)) {
            this.hitable = true;
            return;
        }
        if (StringsKt.equals((String)this.rotations.get(), "spin", true)) {
            EntityLivingBase entityLivingBase = this.target;
            if (entityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            this.hitable = entityLivingBase.field_70737_aN <= ((Number)this.spinHurtTimeValue.get()).intValue();
            return;
        }
        if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f || ((Boolean)this.noHitCheck.get()).booleanValue()) {
            this.hitable = true;
            return;
        }
        double d = this.getMaxRange();
        EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        Entity entity = (Entity)entityPlayerSP;
        EntityLivingBase entityLivingBase = this.target;
        if (entityLivingBase == null) {
            Intrinsics.throwNpe();
        }
        double d2 = PlayerExtensionKt.getDistanceToEntityBox(entity, (Entity)entityLivingBase);
        boolean bl = false;
        double reach = Math.min(d, d2) + 1.0;
        if (((Boolean)this.raycastValue.get()).booleanValue()) {
            Entity raycastedEntity2 = RaycastUtils.raycastEntity(reach, new RaycastUtils.IEntityFilter(this){
                final /* synthetic */ KillAura this$0;

                /*
                 * Enabled force condition propagation
                 * Lifted jumps to return sites
                 */
                public final boolean canRaycast(Entity it) {
                    if (((Boolean)KillAura.access$getLivingRaycastValue$p(this.this$0).get()).booleanValue()) {
                        if (!(it instanceof EntityLivingBase)) return false;
                        if (it instanceof EntityArmorStand) return false;
                    }
                    if (this.this$0.isEnemy(it)) return true;
                    if ((Boolean)KillAura.access$getRaycastIgnoredValue$p(this.this$0).get() != false) return true;
                    if ((Boolean)KillAura.access$getAacValue$p(this.this$0).get() == false) return false;
                    WorldClient worldClient = KillAura.access$getMc$p$s1046033730().field_71441_e;
                    Entity entity = it;
                    Intrinsics.checkExpressionValueIsNotNull(entity, "it");
                    List list = worldClient.func_72839_b(it, entity.func_174813_aQ());
                    Intrinsics.checkExpressionValueIsNotNull(list, "mc.theWorld.getEntitiesW\u2026it, it.entityBoundingBox)");
                    Collection collection = list;
                    boolean bl = false;
                    if (collection.isEmpty()) return false;
                    return true;
                }
                {
                    this.this$0 = killAura;
                }
            });
            if (((Boolean)this.raycastValue.get()).booleanValue() && raycastedEntity2 instanceof EntityLivingBase) {
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(NoFriends.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (module.getState() || !EntityUtils.isFriend(raycastedEntity2)) {
                    this.currentTarget = (EntityLivingBase)raycastedEntity2;
                }
            }
            this.hitable = ((Number)this.maxTurnSpeed.get()).floatValue() > 0.0f ? Intrinsics.areEqual(this.currentTarget, raycastedEntity2) : true;
        } else {
            this.hitable = RotationUtils.isFaced((Entity)this.currentTarget, reach);
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void startBlocking(Entity interactEntity, boolean interact) {
        if (!this.getCanSmartBlock() || StringsKt.equals((String)this.autoBlockModeValue.get(), "none", true) || ((Number)this.blockRate.get()).intValue() <= 0 || new Random().nextInt(100) > ((Number)this.blockRate.get()).intValue()) {
            return;
        }
        if (!((Boolean)this.abThruWallValue.get()).booleanValue() && interactEntity instanceof EntityLivingBase) {
            EntityLivingBase entityLB = (EntityLivingBase)interactEntity;
            EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!entityLB.func_70685_l((Entity)entityPlayerSP)) {
                this.fakeBlock = true;
                return;
            }
        }
        if (StringsKt.equals((String)this.autoBlockModeValue.get(), "ncp", true)) {
            Minecraft minecraft = KillAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
            this.blockingStatus = true;
            return;
        }
        if (StringsKt.equals((String)this.autoBlockModeValue.get(), "oldhypixel", true)) {
            Minecraft minecraft = KillAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, KillAura.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g(), 0.0f, 0.0f, 0.0f));
            this.blockingStatus = true;
            return;
        }
        if (interact) {
            void yaw;
            Minecraft minecraft = KillAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            Entity entity = minecraft.func_175606_aa();
            Vec3 positionEye = entity != null ? entity.func_174824_e(1.0f) : null;
            double expandSize = interactEntity.func_70111_Y();
            AxisAlignedBB boundingBox = interactEntity.func_174813_aQ().func_72314_b(expandSize, expandSize, expandSize);
            Rotation rotation = RotationUtils.targetRotation;
            if (rotation == null) {
                EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
                if (entityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                float f = entityPlayerSP.field_70177_z;
                EntityPlayerSP entityPlayerSP2 = KillAura.access$getMc$p$s1046033730().field_71439_g;
                if (entityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                rotation = new Rotation(f, entityPlayerSP2.field_70125_A);
            }
            Rotation rotation2 = rotation;
            float f = rotation2.component1();
            float pitch = rotation2.component2();
            void var10_10 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl = false;
            float yawCos = (float)Math.cos((double)var10_10);
            void var11_12 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl2 = false;
            float yawSin = (float)Math.sin((double)var11_12);
            float f2 = -pitch * ((float)Math.PI / 180);
            boolean bl3 = false;
            float pitchCos = -((float)Math.cos(f2));
            float f3 = -pitch * ((float)Math.PI / 180);
            boolean bl4 = false;
            float pitchSin = (float)Math.sin(f3);
            double d = this.getMaxRange();
            EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d2 = PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, interactEntity);
            boolean bl5 = false;
            double range = Math.min(d, d2) + 1.0;
            Vec3 vec3 = positionEye;
            if (vec3 == null) {
                Intrinsics.throwNpe();
            }
            Vec3 lookAt = vec3.func_72441_c((double)(yawSin * pitchCos) * range, (double)pitchSin * range, (double)(yawCos * pitchCos) * range);
            MovingObjectPosition movingObjectPosition = boundingBox.func_72327_a(positionEye, lookAt);
            if (movingObjectPosition == null) {
                return;
            }
            MovingObjectPosition movingObject = movingObjectPosition;
            Vec3 hitVec = movingObject.field_72307_f;
            Minecraft minecraft2 = KillAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            minecraft2.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(interactEntity, new Vec3(hitVec.field_72450_a - interactEntity.field_70165_t, hitVec.field_72448_b - interactEntity.field_70163_u, hitVec.field_72449_c - interactEntity.field_70161_v)));
            Minecraft minecraft3 = KillAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
            minecraft3.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(interactEntity, C02PacketUseEntity.Action.INTERACT));
        }
        Minecraft minecraft = KillAura.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(KillAura.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g()));
        this.blockingStatus = true;
    }

    private final void stopBlocking() {
        this.fakeBlock = false;
        if (this.blockingStatus) {
            if (StringsKt.equals((String)this.autoBlockModeValue.get(), "oldhypixel", true)) {
                Minecraft minecraft = KillAura.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(1.0, 1.0, 1.0), EnumFacing.DOWN));
            } else {
                Minecraft minecraft = KillAura.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            }
            this.blockingStatus = false;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCancelRun() {
        EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_175149_v()) return true;
        EntityPlayerSP entityPlayerSP2 = KillAura.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        if (!this.isAlive((EntityLivingBase)entityPlayerSP2)) return true;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Blink.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module.getState()) return true;
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class);
        if (module2 == null) {
            Intrinsics.throwNpe();
        }
        if (module2.getState()) return true;
        if ((Boolean)this.noScaffValue.get() == false) return false;
        Module module3 = LiquidBounce.INSTANCE.getModuleManager().get(Scaffold.class);
        if (module3 == null) {
            Intrinsics.throwNpe();
        }
        if (!module3.getState()) return false;
        return true;
    }

    private final boolean isAlive(EntityLivingBase entity) {
        return entity.func_70089_S() && entity.func_110143_aJ() > 0.0f || (Boolean)this.aacValue.get() != false && entity.field_70737_aN > 5;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCanBlock() {
        EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_70694_bm() == null) return false;
        EntityPlayerSP entityPlayerSP2 = KillAura.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        ItemStack itemStack = entityPlayerSP2.func_70694_bm();
        Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.heldItem");
        if (!(itemStack.func_77973_b() instanceof ItemSword)) return false;
        return true;
    }

    private final float getMaxRange() {
        float f = ((Number)this.rangeValue.get()).floatValue();
        float f2 = ((Number)this.throughWallsRangeValue.get()).floatValue();
        boolean bl = false;
        return Math.max(f, f2);
    }

    private final float getRange(Entity entity) {
        EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        float f = PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity) >= ((Number)this.throughWallsRangeValue.get()).doubleValue() ? ((Number)this.rangeValue.get()).floatValue() : ((Number)this.throughWallsRangeValue.get()).floatValue();
        EntityPlayerSP entityPlayerSP2 = KillAura.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        return f - (entityPlayerSP2.func_70051_ag() ? ((Number)this.rangeSprintReducementValue.get()).floatValue() : 0.0f);
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.targetModeValue.get();
    }

    public KillAura() {
        List list;
        KillAura killAura = this;
        boolean bl = false;
        killAura.prevTargetEntities = list = (List)new ArrayList();
        this.markTimer = new MSTimer();
        this.attackTimer = new MSTimer();
        this.containerOpen = -1L;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ BoolValue access$getLivingRaycastValue$p(KillAura $this) {
        return $this.livingRaycastValue;
    }

    public static final /* synthetic */ BoolValue access$getRaycastIgnoredValue$p(KillAura $this) {
        return $this.raycastIgnoredValue;
    }

    public static final /* synthetic */ BoolValue access$getAacValue$p(KillAura $this) {
        return $this.aacValue;
    }

    public static final /* synthetic */ IntegerValue access$getMinCPS$p(KillAura $this) {
        return $this.minCPS;
    }

    public static final /* synthetic */ long access$getAttackDelay$p(KillAura $this) {
        return $this.attackDelay;
    }

    public static final /* synthetic */ void access$setAttackDelay$p(KillAura $this, long l) {
        $this.attackDelay = l;
    }

    public static final /* synthetic */ IntegerValue access$getMaxCPS$p(KillAura $this) {
        return $this.maxCPS;
    }

    public static final /* synthetic */ FloatValue access$getMinSpinSpeed$p(KillAura $this) {
        return $this.minSpinSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMaxSpinSpeed$p(KillAura $this) {
        return $this.maxSpinSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMinTurnSpeed$p(KillAura $this) {
        return $this.minTurnSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMaxTurnSpeed$p(KillAura $this) {
        return $this.maxTurnSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMinPredictSize$p(KillAura $this) {
        return $this.minPredictSize;
    }

    public static final /* synthetic */ FloatValue access$getMaxPredictSize$p(KillAura $this) {
        return $this.maxPredictSize;
    }

    public static final /* synthetic */ FloatValue access$getMaxRand$p(KillAura $this) {
        return $this.maxRand;
    }

    public static final /* synthetic */ FloatValue access$getMinRand$p(KillAura $this) {
        return $this.minRand;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0003J\u0010\u0010!\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\"H\u0003J\u0010\u0010#\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020$H\u0003R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR\u001a\u0010\u0018\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0006\"\u0004\b\u001a\u0010\b\u00a8\u0006%"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura$CombatListener;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "killCounts", "", "getKillCounts", "()I", "setKillCounts", "(I)V", "startTime", "", "getStartTime", "()J", "setStartTime", "(J)V", "syncEntity", "Lnet/minecraft/entity/EntityLivingBase;", "getSyncEntity", "()Lnet/minecraft/entity/EntityLivingBase;", "setSyncEntity", "(Lnet/minecraft/entity/EntityLivingBase;)V", "totalPlayed", "getTotalPlayed", "setTotalPlayed", "win", "getWin", "setWin", "handleEvents", "", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
    public static final class CombatListener
    implements Listenable {
        @Nullable
        private static EntityLivingBase syncEntity;
        private static int killCounts;
        private static int totalPlayed;
        private static int win;
        private static long startTime;
        public static final CombatListener INSTANCE;

        @Nullable
        public final EntityLivingBase getSyncEntity() {
            return syncEntity;
        }

        public final void setSyncEntity(@Nullable EntityLivingBase entityLivingBase) {
            syncEntity = entityLivingBase;
        }

        public final int getKillCounts() {
            return killCounts;
        }

        public final void setKillCounts(int n) {
            killCounts = n;
        }

        public final int getTotalPlayed() {
            return totalPlayed;
        }

        public final void setTotalPlayed(int n) {
            totalPlayed = n;
        }

        public final int getWin() {
            return win;
        }

        public final void setWin(int n) {
            win = n;
        }

        public final long getStartTime() {
            return startTime;
        }

        public final void setStartTime(long l) {
            startTime = l;
        }

        @EventTarget
        private final void onAttack(AttackEvent event) {
            syncEntity = (EntityLivingBase)event.getTargetEntity();
        }

        @EventTarget
        private final void onUpdate(UpdateEvent event) {
            if (syncEntity != null) {
                EntityLivingBase entityLivingBase = syncEntity;
                if (entityLivingBase == null) {
                    Intrinsics.throwNpe();
                }
                if (entityLivingBase.field_70128_L) {
                    ++killCounts;
                    syncEntity = null;
                }
            }
        }

        @EventTarget(ignoreCondition=true)
        private final void onPacket(PacketEvent event) {
            Packet<?> packet = event.getPacket();
            if (event.getPacket() instanceof C00Handshake) {
                startTime = System.currentTimeMillis();
            }
            if (packet instanceof S45PacketTitle) {
                int n;
                String title;
                IChatComponent iChatComponent = ((S45PacketTitle)packet).func_179805_b();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "packet.message");
                String string = title = iChatComponent.func_150254_d();
                Intrinsics.checkExpressionValueIsNotNull(string, "title");
                if (StringsKt.contains$default((CharSequence)string, "Winner", false, 2, null)) {
                    n = win;
                    win = n + 1;
                }
                if (StringsKt.contains$default((CharSequence)title, "BedWar", false, 2, null)) {
                    n = totalPlayed;
                    totalPlayed = n + 1;
                }
                if (StringsKt.contains$default((CharSequence)title, "SkyWar", false, 2, null)) {
                    n = totalPlayed;
                    totalPlayed = n + 1;
                }
            }
        }

        @Override
        public boolean handleEvents() {
            return true;
        }

        private CombatListener() {
        }

        static {
            CombatListener combatListener;
            INSTANCE = combatListener = new CombatListener();
            startTime = System.currentTimeMillis();
            LiquidBounce.INSTANCE.getEventManager().registerListener(combatListener);
        }
    }
}

