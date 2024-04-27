/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.multiplayer.PlayerControllerMP
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
 *  net.minecraft.network.play.INetHandlerPlayServer
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
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldSettings$GameType
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.combat;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.AttackEvent;
import net.dev.important.event.EntityMovementEvent;
import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.StrafeEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.Criticals;
import net.dev.important.modules.module.modules.combat.NoFriends;
import net.dev.important.modules.module.modules.exploit.Disabler;
import net.dev.important.modules.module.modules.misc.AntiBot;
import net.dev.important.modules.module.modules.misc.Teams;
import net.dev.important.modules.module.modules.movement.TargetStrafe;
import net.dev.important.modules.module.modules.player.Blink;
import net.dev.important.modules.module.modules.render.FreeCam;
import net.dev.important.modules.module.modules.world.Scaffold;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.CombatListener;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.IPlayerControllerMP;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.RaycastUtils;
import net.dev.important.utils.Rotation;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.VecRotation;
import net.dev.important.utils.extensions.PlayerExtensionKt;
import net.dev.important.utils.misc.RandomUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.utils.timer.TimeUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
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
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import oh.yalan.NativeClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@NativeClass
@Info(name="KillAura", spacedName="Kill Aura", description="Automatically attacks targets around you.", category=Category.COMBAT, cnName="\u6740\u622e")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u00b8\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u0007\n\u0002\b\u0011\n\u0002\u0010!\n\u0002\b\u001d\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u0000 \u00ab\u00012\u00020\u0001:\u0002\u00ab\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0080\u0001\u001a\u00030\u0081\u00012\u0007\u0010\u0082\u0001\u001a\u00020#H\u0002J\n\u0010\u0083\u0001\u001a\u00030\u0081\u0001H\u0002J\u0013\u0010\u0084\u0001\u001a\u00020B2\b\u0010\u0082\u0001\u001a\u00030\u0085\u0001H\u0002J\u0016\u0010\u0086\u0001\u001a\u0005\u0018\u00010\u0087\u00012\b\u0010\u0082\u0001\u001a\u00030\u0085\u0001H\u0002J\u0012\u0010\u0088\u0001\u001a\u00020\u00122\u0007\u0010\u0082\u0001\u001a\u00020#H\u0002J\u0013\u0010\u0089\u0001\u001a\u00020\u00122\n\u0010\u0082\u0001\u001a\u0005\u0018\u00010\u0085\u0001J\n\u0010\u008a\u0001\u001a\u00030\u0081\u0001H\u0016J\n\u0010\u008b\u0001\u001a\u00030\u0081\u0001H\u0016J\u0014\u0010\u008c\u0001\u001a\u00030\u0081\u00012\b\u0010\u008d\u0001\u001a\u00030\u008e\u0001H\u0007J\u0014\u0010\u008f\u0001\u001a\u00030\u0081\u00012\b\u0010\u008d\u0001\u001a\u00030\u0090\u0001H\u0007J\u0014\u0010\u0091\u0001\u001a\u00030\u0081\u00012\b\u0010\u008d\u0001\u001a\u00030\u0092\u0001H\u0007J\u0014\u0010\u0093\u0001\u001a\u00030\u0081\u00012\b\u0010\u008d\u0001\u001a\u00030\u0094\u0001H\u0007J\u0014\u0010\u0095\u0001\u001a\u00030\u0081\u00012\b\u0010\u008d\u0001\u001a\u00030\u0096\u0001H\u0007J\u0014\u0010\u0097\u0001\u001a\u00030\u0081\u00012\b\u0010\u008d\u0001\u001a\u00030\u0098\u0001H\u0007J\n\u0010\u0099\u0001\u001a\u00030\u0081\u0001H\u0002J(\u0010\u009a\u0001\u001a\u00030\u0081\u00012\b\u0010\u009b\u0001\u001a\u00030\u009c\u00012\n\u0010\u009d\u0001\u001a\u0005\u0018\u00010\u009e\u00012\b\u0010\u009f\u0001\u001a\u00030\u00a0\u0001J\u001d\u0010\u00a1\u0001\u001a\u00030\u0081\u00012\b\u0010\u00a2\u0001\u001a\u00030\u0085\u00012\u0007\u0010\u00a3\u0001\u001a\u00020\u0012H\u0002J\n\u0010\u00a4\u0001\u001a\u00030\u0081\u0001H\u0002J\n\u0010\u00a5\u0001\u001a\u00030\u0081\u0001H\u0002J\b\u0010\u00a6\u0001\u001a\u00030\u0081\u0001J\n\u0010\u00a7\u0001\u001a\u00030\u0081\u0001H\u0002J\n\u0010\u00a8\u0001\u001a\u00030\u0081\u0001H\u0002J\u0013\u0010\u00a9\u0001\u001a\u00020\u00122\b\u0010\u0082\u0001\u001a\u00030\u0085\u0001H\u0002J\n\u0010\u00aa\u0001\u001a\u00030\u0081\u0001H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\u00128BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0014R\u0014\u0010\u001a\u001a\u00020\u00128BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0014R\u0014\u0010\u001c\u001a\u00020\u00128BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0014R\u000e\u0010\u001e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\"\u001a\u0004\u0018\u00010#X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u000e\u0010(\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010,\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u0014\"\u0004\b.\u0010\u0016R\u000e\u0010/\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u00103\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0014\"\u0004\b5\u0010\u0016R\u000e\u00106\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010<\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010A\u001a\u00020B8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\bC\u0010DR\u000e\u0010E\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010S\u001a\b\u0012\u0004\u0012\u00020 0TX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010X\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010Y\u001a\u00020+\u00a2\u0006\b\n\u0000\u001a\u0004\bZ\u0010[R\u000e\u0010\\\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010]\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010^\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010_\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010`\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010a\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010c\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010d\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010e\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010g\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bh\u0010\u0014\"\u0004\bi\u0010\u0016R\u000e\u0010j\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010k\u001a\u00020BX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bl\u0010D\"\u0004\bm\u0010nR\u000e\u0010o\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010p\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010q\u001a\u00020r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bs\u0010tR\u001c\u0010u\u001a\u0004\u0018\u00010#X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bv\u0010%\"\u0004\bw\u0010'R\u0011\u0010x\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\by\u0010zR\u000e\u0010{\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010|\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010}\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b~\u0010\u0014\"\u0004\b\u007f\u0010\u0016\u00a8\u0006\u00ac\u0001"}, d2={"Lnet/dev/important/modules/module/modules/combat/KillAura;", "Lnet/dev/important/modules/module/Module;", "()V", "aacValue", "Lnet/dev/important/value/BoolValue;", "abThruWallValue", "accuracyValue", "Lnet/dev/important/value/IntegerValue;", "afterTickPatchValue", "alpha", "attackDelay", "", "attackTimer", "Lnet/dev/important/utils/timer/MSTimer;", "autoBlockModeValue", "Lnet/dev/important/value/ListValue;", "blockRate", "blockingStatus", "", "getBlockingStatus", "()Z", "setBlockingStatus", "(Z)V", "blue", "canBlock", "getCanBlock", "canSmartBlock", "getCanSmartBlock", "cancelRun", "getCancelRun", "circleValue", "clicks", "", "containerOpen", "currentTarget", "Lnet/minecraft/entity/EntityLivingBase;", "getCurrentTarget", "()Lnet/minecraft/entity/EntityLivingBase;", "setCurrentTarget", "(Lnet/minecraft/entity/EntityLivingBase;)V", "debugValue", "displayAutoBlockSettings", "failRateValue", "Lnet/dev/important/value/FloatValue;", "fakeBlock", "getFakeBlock", "setFakeBlock", "fakeSharpValue", "fakeSwingValue", "fovValue", "green", "hitable", "getHitable", "setHitable", "hurtTimeValue", "interactAutoBlockValue", "keepSprintValue", "lastHitTick", "limitedMultiTargetsValue", "livingRaycastValue", "markEntity", "markTimer", "maxCPS", "maxPredictSize", "maxRand", "maxRange", "", "getMaxRange", "()F", "maxSpinSpeed", "maxTurnSpeed", "minCPS", "minPredictSize", "minRand", "minSpinSpeed", "minTurnSpeed", "noHitCheck", "noInventoryAttackValue", "noInventoryDelayValue", "noScaffValue", "noSendRot", "outborderValue", "predictValue", "prevTargetEntities", "", "priorityValue", "randomCenterNewValue", "randomCenterValue", "rangeSprintReducementValue", "rangeValue", "getRangeValue", "()Lnet/dev/important/value/FloatValue;", "raycastIgnoredValue", "raycastValue", "red", "rotationStrafeValue", "rotations", "silentRotationValue", "smartABFacingValue", "smartABItemValue", "smartABRangeValue", "smartABTolerationValue", "smartAutoBlockValue", "smartBlocking", "getSmartBlocking", "setSmartBlocking", "spinHurtTimeValue", "spinYaw", "getSpinYaw", "setSpinYaw", "(F)V", "swingValue", "switchDelayValue", "tag", "", "getTag", "()Ljava/lang/String;", "target", "getTarget", "setTarget", "targetModeValue", "getTargetModeValue", "()Lnet/dev/important/value/ListValue;", "throughWallsRangeValue", "verusAutoBlockValue", "verusBlocking", "getVerusBlocking", "setVerusBlocking", "attackEntity", "", "entity", "block", "getRange", "Lnet/minecraft/entity/Entity;", "getTargetRotation", "Lnet/dev/important/utils/Rotation;", "isAlive", "isEnemy", "onDisable", "onEnable", "onEntityMove", "event", "Lnet/dev/important/event/EntityMovementEvent;", "onMotion", "Lnet/dev/important/event/MotionEvent;", "onPacket", "Lnet/dev/important/event/PacketEvent;", "onRender3D", "Lnet/dev/important/event/Render3DEvent;", "onStrafe", "Lnet/dev/important/event/StrafeEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "runAttack", "sendUseItem", "playerIn", "Lnet/minecraft/entity/player/EntityPlayer;", "worldIn", "Lnet/minecraft/world/World;", "itemStackIn", "Lnet/minecraft/item/ItemStack;", "startBlocking", "interactEntity", "interact", "stopBlocking", "unblock", "update", "updateHitable", "updateKA", "updateRotations", "updateTarget", "Companion", "LiquidBounce"})
public final class KillAura
extends Module {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final IntegerValue maxCPS = new IntegerValue(this){
        final /* synthetic */ KillAura this$0;
        {
            this.this$0 = $receiver;
            super("MaxCPS", 8, 1, 20);
        }

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)KillAura.access$getMinCPS$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
            KillAura.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)KillAura.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
    };
    @NotNull
    private final IntegerValue minCPS = new IntegerValue(this){
        final /* synthetic */ KillAura this$0;
        {
            this.this$0 = $receiver;
            super("MinCPS", 5, 1, 20);
        }

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)KillAura.access$getMaxCPS$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
            KillAura.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)KillAura.access$getMaxCPS$p(this.this$0).get()).intValue()));
        }
    };
    @NotNull
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    @NotNull
    private final FloatValue rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f, "m");
    @NotNull
    private final FloatValue throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f, "m");
    @NotNull
    private final FloatValue rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f, "m");
    @NotNull
    private final ListValue rotations;
    @NotNull
    private final IntegerValue spinHurtTimeValue;
    @NotNull
    private final FloatValue maxSpinSpeed;
    @NotNull
    private final FloatValue minSpinSpeed;
    @NotNull
    private final BoolValue noSendRot;
    @NotNull
    private final BoolValue noHitCheck;
    @NotNull
    private final ListValue priorityValue;
    @NotNull
    private final ListValue targetModeValue;
    @NotNull
    private final IntegerValue switchDelayValue;
    @NotNull
    private final BoolValue swingValue;
    @NotNull
    private final BoolValue keepSprintValue;
    @NotNull
    private final ListValue autoBlockModeValue;
    @NotNull
    private final BoolValue displayAutoBlockSettings;
    @NotNull
    private final BoolValue interactAutoBlockValue;
    @NotNull
    private final BoolValue verusAutoBlockValue;
    @NotNull
    private final BoolValue abThruWallValue;
    @NotNull
    private final BoolValue smartAutoBlockValue;
    @NotNull
    private final BoolValue smartABItemValue;
    @NotNull
    private final BoolValue smartABFacingValue;
    @NotNull
    private final FloatValue smartABRangeValue;
    @NotNull
    private final FloatValue smartABTolerationValue;
    @NotNull
    private final BoolValue afterTickPatchValue;
    @NotNull
    private final IntegerValue blockRate;
    @NotNull
    private final BoolValue raycastValue;
    @NotNull
    private final BoolValue raycastIgnoredValue;
    @NotNull
    private final BoolValue livingRaycastValue;
    @NotNull
    private final BoolValue aacValue;
    @NotNull
    private final FloatValue maxTurnSpeed;
    @NotNull
    private final FloatValue minTurnSpeed;
    @NotNull
    private final BoolValue silentRotationValue;
    @NotNull
    private final ListValue rotationStrafeValue;
    @NotNull
    private final FloatValue fovValue;
    @NotNull
    private final BoolValue predictValue;
    @NotNull
    private final FloatValue maxPredictSize;
    @NotNull
    private final FloatValue minPredictSize;
    @NotNull
    private final BoolValue randomCenterValue;
    @NotNull
    private final BoolValue randomCenterNewValue;
    @NotNull
    private final FloatValue minRand;
    @NotNull
    private final FloatValue maxRand;
    @NotNull
    private final BoolValue outborderValue;
    @NotNull
    private final FloatValue failRateValue;
    @NotNull
    private final BoolValue fakeSwingValue;
    @NotNull
    private final BoolValue noInventoryAttackValue;
    @NotNull
    private final IntegerValue noInventoryDelayValue;
    @NotNull
    private final IntegerValue limitedMultiTargetsValue;
    @NotNull
    private final BoolValue noScaffValue;
    @NotNull
    private final BoolValue debugValue;
    @NotNull
    private final BoolValue circleValue;
    @NotNull
    private final IntegerValue accuracyValue;
    @NotNull
    private final BoolValue fakeSharpValue;
    @NotNull
    private final IntegerValue red;
    @NotNull
    private final IntegerValue green;
    @NotNull
    private final IntegerValue blue;
    @NotNull
    private final IntegerValue alpha;
    @Nullable
    private EntityLivingBase target;
    @Nullable
    private EntityLivingBase currentTarget;
    private boolean hitable;
    @NotNull
    private final List<Integer> prevTargetEntities;
    @Nullable
    private EntityLivingBase markEntity;
    @NotNull
    private final MSTimer markTimer;
    @NotNull
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
    private static int killCounts;

    public KillAura() {
        Object object = new String[]{"Vanilla", "BackTrack", "Spin", "None"};
        this.rotations = new ListValue("RotationMode", (String[])object, "BackTrack");
        this.spinHurtTimeValue = new IntegerValue("Spin-HitHurtTime", 10, 0, 10, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "spin", true);
            }
        });
        object = new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "spin", true);
            }
        };
        this.maxSpinSpeed = new FloatValue(this, object){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super("MaxSpinSpeed", 180.0f, 0.0f, 180.0f, "\u00b0", $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)KillAura.access$getMinSpinSpeed$p(this.this$0).get()).floatValue();
                if (v > newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        object = new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "spin", true);
            }
        };
        this.minSpinSpeed = new FloatValue(this, object){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super("MinSpinSpeed", 180.0f, 0.0f, 180.0f, "\u00b0", $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)KillAura.access$getMaxSpinSpeed$p(this.this$0).get()).floatValue();
                if (v < newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        this.noSendRot = new BoolValue("NoSendRotation", true, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "spin", true);
            }
        });
        this.noHitCheck = new BoolValue("NoHitCheck", false, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "none", true);
            }
        });
        object = new String[]{"Health", "Distance", "Direction", "LivingTime"};
        this.priorityValue = new ListValue("Priority", (String[])object, "Distance");
        object = new String[]{"Single", "Switch", "Multi"};
        this.targetModeValue = new ListValue("TargetMode", (String[])object, "Switch");
        this.switchDelayValue = new IntegerValue("SwitchDelay", 1000, 1, 2000, "ms", new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)this.this$0.getTargetModeValue().get(), "switch", true);
            }
        });
        this.swingValue = new BoolValue("Swing", true);
        this.keepSprintValue = new BoolValue("KeepSprint", true);
        object = new String[]{"None", "Packet", "AfterTick", "NCP", "OldHypixel", "HytPit", "Vulcan"};
        this.autoBlockModeValue = new ListValue("AutoBlock", (String[])object, "None");
        this.displayAutoBlockSettings = new BoolValue("Open-AutoBlock-Settings", false, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true);
            }
        });
        this.interactAutoBlockValue = new BoolValue("InteractAutoBlock", true, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.verusAutoBlockValue = new BoolValue("VerusAutoBlock", false, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.abThruWallValue = new BoolValue("AutoBlockThroughWalls", false, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.smartAutoBlockValue = new BoolValue("SmartAutoBlock", false, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.smartABItemValue = new BoolValue("SmartAutoBlock-ItemCheck", true, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getSmartAutoBlockValue$p(this.this$0).get() != false && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.smartABFacingValue = new BoolValue("SmartAutoBlock-FacingCheck", true, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getSmartAutoBlockValue$p(this.this$0).get() != false && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.smartABRangeValue = new FloatValue("SmartAB-Range", 3.5f, 3.0f, 8.0f, "m", new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getSmartAutoBlockValue$p(this.this$0).get() != false && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.smartABTolerationValue = new FloatValue("SmartAB-Toleration", 0.0f, 0.0f, 2.0f, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getSmartAutoBlockValue$p(this.this$0).get() != false && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.afterTickPatchValue = new BoolValue("AfterTickPatch", true, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "AfterTick", true) && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.blockRate = new IntegerValue("BlockRate", 100, 1, 100, "%", new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getAutoBlockModeValue$p(this.this$0).get(), "None", true) && (Boolean)KillAura.access$getDisplayAutoBlockSettings$p(this.this$0).get() != false;
            }
        });
        this.raycastValue = new BoolValue("RayCast", true);
        this.raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
        this.livingRaycastValue = new BoolValue("LivingRayCast", true);
        this.aacValue = new BoolValue("AAC", false);
        object = new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "none", true);
            }
        };
        this.maxTurnSpeed = new FloatValue(this, object){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super("MaxTurnSpeed", 180.0f, 0.0f, 180.0f, "\u00b0", $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)KillAura.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
                if (v > newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        object = new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "none", true);
            }
        };
        this.minTurnSpeed = new FloatValue(this, object){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super("MinTurnSpeed", 180.0f, 0.0f, 180.0f, "\u00b0", $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)KillAura.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
                if (v < newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        this.silentRotationValue = new BoolValue("SilentRotation", true, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "none", true);
            }
        });
        object = new String[]{"Off", "Strict", "Silent"};
        this.rotationStrafeValue = new ListValue("Strafe", (String[])object, "Off");
        this.fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
        this.predictValue = new BoolValue("Predict", true);
        object = new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)KillAura.access$getPredictValue$p(this.this$0).get();
            }
        };
        this.maxPredictSize = new FloatValue(this, object){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super("MaxPredictSize", 1.0f, 0.1f, 5.0f, $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)KillAura.access$getMinPredictSize$p(this.this$0).get()).floatValue();
                if (v > newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        object = new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)KillAura.access$getPredictValue$p(this.this$0).get();
            }
        };
        this.minPredictSize = new FloatValue(this, object){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super("MinPredictSize", 1.0f, 0.1f, 5.0f, $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)KillAura.access$getMaxPredictSize$p(this.this$0).get()).floatValue();
                if (v < newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        this.randomCenterValue = new BoolValue("RandomCenter", false, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "none", true);
            }
        });
        this.randomCenterNewValue = new BoolValue("NewCalc", true, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "none", true) && (Boolean)KillAura.access$getRandomCenterValue$p(this.this$0).get() != false;
            }
        });
        object = new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "none", true) && (Boolean)KillAura.access$getRandomCenterValue$p(this.this$0).get() != false;
            }
        };
        this.minRand = new FloatValue(this, object){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super("MinMultiply", 0.8f, 0.0f, 2.0f, "x", $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)KillAura.access$getMaxRand$p(this.this$0).get()).floatValue();
                if (v < newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        object = new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)KillAura.access$getRotations$p(this.this$0).get(), "none", true) && (Boolean)KillAura.access$getRandomCenterValue$p(this.this$0).get() != false;
            }
        };
        this.maxRand = new FloatValue(this, object){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super("MaxMultiply", 0.8f, 0.0f, 2.0f, "x", $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)KillAura.access$getMinRand$p(this.this$0).get()).floatValue();
                if (v > newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        this.outborderValue = new BoolValue("Outborder", false);
        this.failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
        this.fakeSwingValue = new BoolValue("FakeSwing", true);
        this.noInventoryAttackValue = new BoolValue("NoInvAttack", false);
        this.noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500, "ms", new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)KillAura.access$getNoInventoryAttackValue$p(this.this$0).get();
            }
        });
        this.limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)this.this$0.getTargetModeValue().get(), "multi", true);
            }
        });
        this.noScaffValue = new BoolValue("NoScaffold", true);
        this.debugValue = new BoolValue("Debug", false);
        this.circleValue = new BoolValue("Circle", true);
        this.accuracyValue = new IntegerValue("Accuracy", 59, 0, 59, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)KillAura.access$getCircleValue$p(this.this$0).get();
            }
        });
        this.fakeSharpValue = new BoolValue("FakeSharp", true);
        this.red = new IntegerValue("Red", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)KillAura.access$getCircleValue$p(this.this$0).get();
            }
        });
        this.green = new IntegerValue("Green", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)KillAura.access$getCircleValue$p(this.this$0).get();
            }
        });
        this.blue = new IntegerValue("Blue", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)KillAura.access$getCircleValue$p(this.this$0).get();
            }
        });
        this.alpha = new IntegerValue("Alpha", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ KillAura this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)KillAura.access$getCircleValue$p(this.this$0).get();
            }
        });
        this.prevTargetEntities = new ArrayList();
        this.markTimer = new MSTimer();
        this.attackTimer = new MSTimer();
        this.containerOpen = -1L;
    }

    @NotNull
    public final FloatValue getRangeValue() {
        return this.rangeValue;
    }

    @NotNull
    public final ListValue getTargetModeValue() {
        return this.targetModeValue;
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
        if (MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        if (MinecraftInstance.mc.field_71441_e == null) {
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
        if (this.verusBlocking && !this.blockingStatus && !MinecraftInstance.mc.field_71439_g.func_70632_aY()) {
            this.verusBlocking = false;
            if (((Boolean)this.verusAutoBlockValue.get()).booleanValue()) {
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN)));
            }
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
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
                Intrinsics.checkNotNull(entityLivingBase);
                this.startBlocking((Entity)entityLivingBase, this.hitable);
            }
        }
        if (StringsKt.equals((String)this.rotationStrafeValue.get(), "Off", true)) {
            this.update();
        }
    }

    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Module module2 = Client.INSTANCE.getModuleManager().getModule(TargetStrafe.class);
        Intrinsics.checkNotNull(module2);
        TargetStrafe targetStrafe = (TargetStrafe)module2;
        if (StringsKt.equals((String)this.rotationStrafeValue.get(), "Off", true) && !targetStrafe.getState()) {
            return;
        }
        this.update();
        if (this.currentTarget != null && RotationUtils.targetRotation != null) {
            if (targetStrafe.getCanStrafe()) {
                Rotation rotation = RotationUtils.targetRotation;
                if (rotation == null) {
                    return;
                }
                float yaw = rotation.component1();
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
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70159_w += (double)((strafe *= f) * yawCos - (forward *= f) * yawSin);
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70179_y += (double)(forward * yawCos + strafe * yawSin);
                }
                event.cancelEvent();
            } else {
                String strafe = ((String)this.rotationStrafeValue.get()).toLowerCase();
                Intrinsics.checkNotNullExpressionValue(strafe, "this as java.lang.String).toLowerCase()");
                String string = strafe;
                if (Intrinsics.areEqual(string, "strict")) {
                    Rotation rotation = RotationUtils.targetRotation;
                    if (rotation == null) {
                        return;
                    }
                    float yaw = rotation.component1();
                    float strafe2 = event.getStrafe();
                    float forward = event.getForward();
                    float friction = event.getFriction();
                    float f = strafe2 * strafe2 + forward * forward;
                    if (f >= 1.0E-4f) {
                        if ((f = MathHelper.func_76129_c((float)f)) < 1.0f) {
                            f = 1.0f;
                        }
                        f = friction / f;
                        float yawSin = MathHelper.func_76126_a((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                        float yawCos = MathHelper.func_76134_b((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                        entityPlayerSP.field_70159_w += (double)((strafe2 *= f) * yawCos - (forward *= f) * yawSin);
                        entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                        entityPlayerSP.field_70179_y += (double)(forward * yawCos + strafe2 * yawSin);
                    }
                    event.cancelEvent();
                } else if (Intrinsics.areEqual(string, "silent")) {
                    this.update();
                    RotationUtils.targetRotation.applyStrafeToPlayer(event);
                    event.cancelEvent();
                }
            }
        }
    }

    public final void update() {
        if (this.getCancelRun() || ((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.mc.field_71462_r instanceof GuiContainer || System.currentTimeMillis() - this.containerOpen < (long)((Number)this.noInventoryDelayValue.get()).intValue())) {
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
        Intrinsics.checkNotNullParameter(event, "event");
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
        Intrinsics.checkNotNullParameter(event, "event");
        this.updateKA();
        this.smartBlocking = false;
        if (((Boolean)this.smartAutoBlockValue.get()).booleanValue() && this.target != null) {
            EntityLivingBase entityLivingBase = this.target;
            Intrinsics.checkNotNull(entityLivingBase);
            EntityLivingBase smTarget = entityLivingBase;
            if (!((Boolean)this.smartABItemValue.get()).booleanValue() || smTarget.func_70694_bm() != null && smTarget.func_70694_bm().func_77973_b() != null && (smTarget.func_70694_bm().func_77973_b() instanceof ItemSword || smTarget.func_70694_bm().func_77973_b() instanceof ItemAxe)) {
                EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer");
                if (PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, (Entity)smTarget) < (double)((Number)this.smartABRangeValue.get()).floatValue()) {
                    if (((Boolean)this.smartABFacingValue.get()).booleanValue()) {
                        if (smTarget.func_174822_a((double)((double)((Number)this.smartABRangeValue.get()).floatValue()), (float)1.0f).field_72313_a == MovingObjectPosition.MovingObjectType.MISS) {
                            Vec3 eyesVec = smTarget.func_174824_e(1.0f);
                            Vec3 lookVec = smTarget.func_70676_i(1.0f);
                            Vec3 pointingVec = eyesVec.func_72441_c(lookVec.field_72450_a * ((Number)this.smartABRangeValue.get()).doubleValue(), lookVec.field_72448_b * ((Number)this.smartABRangeValue.get()).doubleValue(), lookVec.field_72449_c * ((Number)this.smartABRangeValue.get()).doubleValue());
                            float border = MinecraftInstance.mc.field_71439_g.func_70111_Y() + ((Number)this.smartABTolerationValue.get()).floatValue();
                            AxisAlignedBB bb = MinecraftInstance.mc.field_71439_g.func_174813_aQ().func_72314_b((double)border, (double)border, (double)border);
                            this.smartBlocking = bb.func_72327_a(eyesVec, pointingVec) != null || bb.func_72326_a(smTarget.func_174813_aQ());
                        }
                    } else {
                        this.smartBlocking = true;
                    }
                }
            }
        }
        if (CombatListener.INSTANCE.getSyncEntity() != null) {
            EntityLivingBase entityLivingBase = CombatListener.INSTANCE.getSyncEntity();
            Intrinsics.checkNotNull(entityLivingBase);
            if (entityLivingBase.field_70128_L) {
                ++killCounts;
                CombatListener.INSTANCE.setSyncEntity(null);
            }
        }
        if (this.blockingStatus || MinecraftInstance.mc.field_71439_g.func_70632_aY()) {
            this.verusBlocking = true;
        } else if (this.verusBlocking) {
            this.verusBlocking = false;
            if (((Boolean)this.verusAutoBlockValue.get()).booleanValue()) {
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN)));
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
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.mc.field_71462_r instanceof GuiContainer || System.currentTimeMillis() - this.containerOpen < (long)((Number)this.noInventoryDelayValue.get()).intValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.mc.field_71462_r instanceof GuiContainer) {
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

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        int n;
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.circleValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)(MinecraftInstance.mc.field_71439_g.field_70142_S + (MinecraftInstance.mc.field_71439_g.field_70165_t - MinecraftInstance.mc.field_71439_g.field_70142_S) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78725_b), (double)(MinecraftInstance.mc.field_71439_g.field_70137_T + (MinecraftInstance.mc.field_71439_g.field_70163_u - MinecraftInstance.mc.field_71439_g.field_70137_T) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78726_c), (double)(MinecraftInstance.mc.field_71439_g.field_70136_U + (MinecraftInstance.mc.field_71439_g.field_70161_v - MinecraftInstance.mc.field_71439_g.field_70136_U) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78723_d));
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)((float)((Number)this.red.get()).intValue() / 255.0f), (float)((float)((Number)this.green.get()).intValue() / 255.0f), (float)((float)((Number)this.blue.get()).intValue() / 255.0f), (float)((float)((Number)this.alpha.get()).intValue() / 255.0f));
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glBegin((int)3);
            n = 60 - ((Number)this.accuracyValue.get()).intValue();
            if (n <= 0) {
                throw new IllegalArgumentException("Step must be positive, was: " + n + '.');
            }
            int n2 = 0;
            int n3 = ProgressionUtilKt.getProgressionLastElement(0, 360, n);
            if (n2 <= n3) {
                int i;
                do {
                    i = n2;
                    n2 += n;
                    GL11.glVertex2f((float)((float)Math.cos((double)i * Math.PI / 180.0) * ((Number)this.rangeValue.get()).floatValue()), (float)((float)Math.sin((double)i * Math.PI / 180.0) * ((Number)this.rangeValue.get()).floatValue()));
                } while (i != n3);
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
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.mc.field_71462_r instanceof GuiContainer || System.currentTimeMillis() - this.containerOpen < (long)((Number)this.noInventoryDelayValue.get()).intValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.mc.field_71462_r instanceof GuiContainer) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target == null) {
            return;
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            EntityLivingBase entityLivingBase = this.currentTarget;
            Intrinsics.checkNotNull(entityLivingBase);
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
        Intrinsics.checkNotNullParameter(event, "event");
        Entity movedEntity = event.getMovedEntity();
        if (this.target == null || !Intrinsics.areEqual(movedEntity, this.currentTarget)) {
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
        v0 = openInventory = (Boolean)this.aacValue.get() != false && MinecraftInstance.mc.field_71462_r instanceof GuiInventory != false;
        if (!(failRate > 0.0f)) ** GOTO lbl-1000
        v1 = new Random();
        if ((float)v1.nextInt(100) <= failRate) {
            v2 = true;
        } else lbl-1000:
        // 2 sources

        {
            v2 = failHit = false;
        }
        if (openInventory) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
        }
        if (!this.hitable || failHit) {
            if (swing && (((Boolean)this.fakeSwingValue.get()).booleanValue() || failHit)) {
                MinecraftInstance.mc.field_71439_g.func_71038_i();
            }
        } else {
            if (!multi) {
                v3 = this.currentTarget;
                Intrinsics.checkNotNull(v3);
                this.attackEntity(v3);
            } else {
                targets = 0;
                for (Entity entity : MinecraftInstance.mc.field_71441_e.field_72996_f) {
                    var11_10 = MinecraftInstance.mc.field_71439_g;
                    Intrinsics.checkNotNullExpressionValue(var11_10, "mc.thePlayer");
                    v4 = (Entity)var11_10;
                    Intrinsics.checkNotNullExpressionValue(entity, "entity");
                    distance = PlayerExtensionKt.getDistanceToEntityBox(v4, entity);
                    if (!(entity instanceof EntityLivingBase) || !this.isEnemy(entity) || !(distance <= (double)this.getRange(entity))) continue;
                    this.attackEntity((EntityLivingBase)entity);
                    if (((Number)this.limitedMultiTargetsValue.get()).intValue() == 0 || ((Number)this.limitedMultiTargetsValue.get()).intValue() > ++targets) continue;
                }
            }
            if (((Boolean)this.aacValue.get()).booleanValue()) {
                v5 = this.target;
                Intrinsics.checkNotNull(v5);
                v6 = v5.func_145782_y();
            } else {
                v7 = this.currentTarget;
                Intrinsics.checkNotNull(v7);
                v6 = v7.func_145782_y();
            }
            this.prevTargetEntities.add(v6);
            if (Intrinsics.areEqual(this.target, this.currentTarget)) {
                this.target = null;
            }
        }
        if (StringsKt.equals((String)this.targetModeValue.get(), "Switch", true) && this.attackTimer.hasTimePassed(((Number)this.switchDelayValue.get()).intValue()) && ((Number)this.switchDelayValue.get()).intValue() != 0) {
            if (((Boolean)this.aacValue.get()).booleanValue()) {
                v8 = this.target;
                Intrinsics.checkNotNull(v8);
                v9 = v8.func_145782_y();
            } else {
                v10 = this.currentTarget;
                Intrinsics.checkNotNull(v10);
                v9 = v10.func_145782_y();
            }
            this.prevTargetEntities.add(v9);
            this.attackTimer.reset();
        }
        if (openInventory) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        }
    }

    private final void block() {
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer");
        EntityPlayer entityPlayer = (EntityPlayer)entityPlayerSP;
        World world = (World)MinecraftInstance.mc.field_71441_e;
        entityPlayerSP = MinecraftInstance.mc.field_71439_g.func_71045_bC();
        Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer.currentEquippedItem");
        this.sendUseItem(entityPlayer, world, (ItemStack)entityPlayerSP);
        MinecraftInstance.mc.field_71474_y.field_74313_G.field_74513_e = true;
        MinecraftInstance.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, MinecraftInstance.mc.field_71439_g.func_70694_bm(), 0.0f, 0.0f, 0.0f));
        this.blockingStatus = true;
    }

    private final void unblock() {
        if (this.blockingStatus) {
            MinecraftInstance.mc.field_71474_y.field_74313_G.field_74513_e = false;
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            this.blockingStatus = false;
        }
    }

    private final void updateTarget() {
        Object searchTarget = null;
        int hurtTime = ((Number)this.hurtTimeValue.get()).intValue();
        float fov = ((Number)this.fovValue.get()).floatValue();
        boolean switchMode = StringsKt.equals((String)this.targetModeValue.get(), "Switch", true);
        List targets = new ArrayList();
        for (Entity entity : MinecraftInstance.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase) || !this.isEnemy(entity) || switchMode && this.prevTargetEntities.contains(((EntityLivingBase)entity).func_145782_y())) continue;
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer");
            double distance = PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity);
            double entityFov = RotationUtils.getRotationDifference(entity);
            if (!(distance <= (double)this.getMaxRange()) || !(fov == 180.0f) && !(entityFov <= (double)fov) || ((EntityLivingBase)entity).field_70737_aN > hurtTime) continue;
            targets.add(entity);
        }
        String distance = ((String)this.priorityValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(distance, "this as java.lang.String).toLowerCase()");
        switch (distance) {
            case "distance": {
                Object $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl = false;
                        EntityPlayerSP entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer");
                        Comparable comparable = Double.valueOf(PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, (Entity)it));
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl = false;
                        entityPlayerSP = KillAura.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer");
                        return ComparisonsKt.compareValues(comparable2, PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, (Entity)it));
                    }
                });
                break;
            }
            case "health": {
                Object $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl = false;
                        Comparable comparable = Float.valueOf(it.func_110143_aJ());
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl = false;
                        return ComparisonsKt.compareValues(comparable2, (Comparable)Float.valueOf(it.func_110143_aJ()));
                    }
                });
                break;
            }
            case "direction": {
                Object $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl = false;
                        Comparable comparable = Double.valueOf(RotationUtils.getRotationDifference((Entity)it));
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl = false;
                        return ComparisonsKt.compareValues(comparable2, RotationUtils.getRotationDifference((Entity)it));
                    }
                });
                break;
            }
            case "livingtime": {
                Object $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl = false;
                        it = (EntityLivingBase)b;
                        Comparable comparable = Integer.valueOf(-it.field_70173_aa);
                        bl = false;
                        return ComparisonsKt.compareValues(comparable, -it.field_70173_aa);
                    }
                });
            }
        }
        boolean found = false;
        for (EntityLivingBase entity : targets) {
            if (!this.updateRotations((Entity)entity)) continue;
            this.target = entity;
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
        if (!((Collection)this.prevTargetEntities).isEmpty()) {
            this.prevTargetEntities.clear();
            this.updateTarget();
        }
    }

    public final boolean isEnemy(@Nullable Entity entity) {
        if (entity instanceof EntityLivingBase && (EntityUtils.targetDead || this.isAlive((EntityLivingBase)entity)) && !Intrinsics.areEqual(entity, MinecraftInstance.mc.field_71439_g)) {
            if (!EntityUtils.targetInvisible && ((EntityLivingBase)entity).func_82150_aj()) {
                return false;
            }
            if (EntityUtils.targetPlayer && entity instanceof EntityPlayer) {
                if (((EntityPlayer)entity).func_175149_v() || AntiBot.isBot((EntityLivingBase)entity)) {
                    return false;
                }
                if (EntityUtils.isFriend(entity)) {
                    Module module2 = Client.INSTANCE.getModuleManager().get(NoFriends.class);
                    Intrinsics.checkNotNull(module2);
                    if (!module2.getState()) {
                        return false;
                    }
                }
                Module module3 = Client.INSTANCE.getModuleManager().get(Teams.class);
                if (module3 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.misc.Teams");
                }
                Teams teams = (Teams)module3;
                return !teams.getState() || !teams.isInYourTeam((EntityLivingBase)entity);
            }
            return EntityUtils.targetMobs && EntityUtils.isMob(entity) || EntityUtils.targetAnimals && EntityUtils.isAnimal(entity);
        }
        return false;
    }

    private final void attackEntity(EntityLivingBase entity) {
        if (MinecraftInstance.mc.field_71439_g.func_70632_aY() || this.blockingStatus) {
            this.stopBlocking();
        }
        Client.INSTANCE.getEventManager().callEvent(new AttackEvent((Entity)entity));
        this.markEntity = entity;
        if (StringsKt.equals((String)this.rotations.get(), "spin", true) && !((Boolean)this.noSendRot.get()).booleanValue()) {
            Rotation rotation = this.getTargetRotation((Entity)entity);
            if (rotation == null) {
                return;
            }
            Rotation targetedRotation = rotation;
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(targetedRotation.getYaw(), targetedRotation.getPitch(), MinecraftInstance.mc.field_71439_g.field_70122_E));
            if (((Boolean)this.debugValue.get()).booleanValue()) {
                ClientUtils.displayChatMessage("[KillAura] Silent rotation change.");
            }
        }
        if (((Boolean)this.swingValue.get()).booleanValue()) {
            MinecraftInstance.mc.field_71439_g.func_71038_i();
        }
        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
        if (((Boolean)this.keepSprintValue.get()).booleanValue()) {
            if (!(!(MinecraftInstance.mc.field_71439_g.field_70143_R > 0.0f) || MinecraftInstance.mc.field_71439_g.field_70122_E || MinecraftInstance.mc.field_71439_g.func_70617_f_() || MinecraftInstance.mc.field_71439_g.func_70090_H() || MinecraftInstance.mc.field_71439_g.func_70644_a(Potion.field_76440_q) || MinecraftInstance.mc.field_71439_g.func_70115_ae())) {
                MinecraftInstance.mc.field_71439_g.func_71009_b((Entity)entity);
            }
            if (EnchantmentHelper.func_152377_a((ItemStack)MinecraftInstance.mc.field_71439_g.func_70694_bm(), (EnumCreatureAttribute)entity.func_70668_bt()) > 0.0f) {
                MinecraftInstance.mc.field_71439_g.func_71047_c((Entity)entity);
            }
        } else if (MinecraftInstance.mc.field_71442_b.field_78779_k != WorldSettings.GameType.SPECTATOR) {
            MinecraftInstance.mc.field_71439_g.func_71059_n((Entity)entity);
        }
        Module module2 = Client.INSTANCE.getModuleManager().get(Criticals.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.Criticals");
        }
        Criticals criticals = (Criticals)module2;
        int n = 0;
        while (n < 3) {
            int i = n++;
            if (MinecraftInstance.mc.field_71439_g.field_70143_R > 0.0f && !MinecraftInstance.mc.field_71439_g.field_70122_E && !MinecraftInstance.mc.field_71439_g.func_70617_f_() && !MinecraftInstance.mc.field_71439_g.func_70090_H() && !MinecraftInstance.mc.field_71439_g.func_70644_a(Potion.field_76440_q) && MinecraftInstance.mc.field_71439_g.field_70154_o == null || criticals.getState() && criticals.getMsTimer().hasTimePassed(((Number)criticals.getDelayValue().get()).intValue()) && !MinecraftInstance.mc.field_71439_g.func_70090_H() && !MinecraftInstance.mc.field_71439_g.func_180799_ab() && !MinecraftInstance.mc.field_71439_g.field_70134_J) {
                MinecraftInstance.mc.field_71439_g.func_71009_b((Entity)this.target);
            }
            ItemStack itemStack = MinecraftInstance.mc.field_71439_g.func_70694_bm();
            EntityLivingBase entityLivingBase = this.target;
            Intrinsics.checkNotNull(entityLivingBase);
            if (!(EnchantmentHelper.func_152377_a((ItemStack)itemStack, (EnumCreatureAttribute)entityLivingBase.func_70668_bt()) > 0.0f) && !((Boolean)this.fakeSharpValue.get()).booleanValue()) continue;
            MinecraftInstance.mc.field_71439_g.func_71047_c((Entity)this.target);
        }
        if (!(((Boolean)this.afterTickPatchValue.get()).booleanValue() && StringsKt.equals((String)this.autoBlockModeValue.get(), "AfterTick", true) || !MinecraftInstance.mc.field_71439_g.func_70632_aY() && !this.getCanBlock())) {
            this.startBlocking((Entity)entity, (Boolean)this.interactAutoBlockValue.get());
        }
    }

    private final boolean updateRotations(Entity entity) {
        if (StringsKt.equals((String)this.rotations.get(), "none", true)) {
            return true;
        }
        Module module2 = Client.INSTANCE.getModuleManager().getModule(Disabler.class);
        Intrinsics.checkNotNull(module2);
        Disabler disabler = (Disabler)module2;
        boolean modify = disabler.getCanModifyRotation();
        if (modify) {
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
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
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
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, bl, bl2, bl3, PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity) < (double)((Number)this.throughWallsRangeValue.get()).floatValue(), this.getMaxRange(), RandomUtils.nextFloat(((Number)this.minRand.get()).floatValue(), ((Number)this.maxRand.get()).floatValue()), (Boolean)this.randomCenterNewValue.get());
            if (vecRotation == null) {
                return null;
            }
            Rotation rotation = vecRotation.component2();
            return RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
        }
        if (StringsKt.equals((String)this.rotations.get(), "Spin", true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return RotationUtils.serverRotation;
            }
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.func_72317_d((entity.field_70165_t - entity.field_70169_q) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70163_u - entity.field_70167_r) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70161_v - entity.field_70166_s) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
            Object rotation = RotationUtils.searchCenter(boundingBox, false, false, false, PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity) < (double)((Number)this.throughWallsRangeValue.get()).floatValue(), this.getMaxRange());
            if (rotation == null) {
                return null;
            }
            rotation = ((VecRotation)rotation).component2();
            return rotation;
        }
        if (StringsKt.equals((String)this.rotations.get(), "BackTrack", true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.func_72317_d((entity.field_70165_t - entity.field_70169_q) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70163_u - entity.field_70167_r) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.field_70161_v - entity.field_70166_s) * (double)RandomUtils.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            Vec3 vec3 = RotationUtils.getCenter(entity.func_174813_aQ());
            boolean bl = (Boolean)this.predictValue.get();
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
            Rotation rotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.OtherRotation(boundingBox, vec3, bl, PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity) < (double)((Number)this.throughWallsRangeValue.get()).floatValue(), this.getMaxRange()), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkNotNullExpressionValue(rotation, "limitAngleChange(Rotatio\u2026rnSpeed.get()).toFloat())");
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
        Module module2 = Client.INSTANCE.getModuleManager().getModule(Disabler.class);
        Intrinsics.checkNotNull(module2);
        Disabler disabler = (Disabler)module2;
        if (StringsKt.equals((String)this.rotations.get(), "spin", true)) {
            EntityLivingBase entityLivingBase = this.target;
            Intrinsics.checkNotNull(entityLivingBase);
            this.hitable = entityLivingBase.field_70737_aN <= ((Number)this.spinHurtTimeValue.get()).intValue();
            return;
        }
        if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f || ((Boolean)this.noHitCheck.get()).booleanValue() || disabler.getCanModifyRotation()) {
            this.hitable = true;
            return;
        }
        double d = this.getMaxRange();
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer");
        Entity entity = (Entity)entityPlayerSP;
        EntityLivingBase entityLivingBase = this.target;
        Intrinsics.checkNotNull(entityLivingBase);
        double reach = Math.min(d, PlayerExtensionKt.getDistanceToEntityBox(entity, (Entity)entityLivingBase)) + 1.0;
        if (((Boolean)this.raycastValue.get()).booleanValue()) {
            Entity raycastedEntity = RaycastUtils.raycastEntity(reach, arg_0 -> KillAura.updateHitable$lambda-4(this, arg_0));
            if (((Boolean)this.raycastValue.get()).booleanValue() && raycastedEntity instanceof EntityLivingBase) {
                Module module3 = Client.INSTANCE.getModuleManager().get(NoFriends.class);
                Intrinsics.checkNotNull(module3);
                if (module3.getState() || !EntityUtils.isFriend(raycastedEntity)) {
                    this.currentTarget = (EntityLivingBase)raycastedEntity;
                }
            }
            this.hitable = ((Number)this.maxTurnSpeed.get()).floatValue() > 0.0f ? Intrinsics.areEqual(this.currentTarget, raycastedEntity) : true;
        } else {
            this.hitable = RotationUtils.isFaced((Entity)this.currentTarget, reach);
        }
    }

    private final void startBlocking(Entity interactEntity, boolean interact) {
        if (!this.getCanSmartBlock() || StringsKt.equals((String)this.autoBlockModeValue.get(), "none", true) || ((Number)this.blockRate.get()).intValue() <= 0 || new Random().nextInt(100) > ((Number)this.blockRate.get()).intValue()) {
            return;
        }
        if (!((Boolean)this.abThruWallValue.get()).booleanValue() && interactEntity instanceof EntityLivingBase) {
            EntityLivingBase entityLB = (EntityLivingBase)interactEntity;
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
            if (!entityLB.func_70685_l((Entity)entityPlayerSP)) {
                this.fakeBlock = true;
                return;
            }
        }
        if (StringsKt.equals((String)this.autoBlockModeValue.get(), "ncp", true)) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
            this.blockingStatus = true;
            return;
        }
        if (StringsKt.equals((String)this.autoBlockModeValue.get(), "hytpit", true)) {
            new Robot().mousePress(4096);
            this.blockingStatus = true;
            return;
        }
        if (StringsKt.equals((String)this.autoBlockModeValue.get(), "oldhypixel", true)) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g(), 0.0f, 0.0f, 0.0f));
            this.blockingStatus = true;
            return;
        }
        if (StringsKt.equals((String)this.autoBlockModeValue.get(), "vulcan", true)) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
            this.blockingStatus = true;
            return;
        }
        if (interact) {
            Entity entity = MinecraftInstance.mc.func_175606_aa();
            Vec3 positionEye = entity == null ? null : entity.func_174824_e(1.0f);
            double expandSize = interactEntity.func_70111_Y();
            AxisAlignedBB boundingBox = interactEntity.func_174813_aQ().func_72314_b(expandSize, expandSize, expandSize);
            Rotation rotation = RotationUtils.targetRotation;
            if (rotation == null) {
                EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP);
                float f = entityPlayerSP.field_70177_z;
                EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP2);
                rotation = new Rotation(f, entityPlayerSP2.field_70125_A);
            }
            Rotation rotation2 = rotation;
            float yaw = rotation2.component1();
            float pitch = rotation2.component2();
            float yawCos = (float)Math.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
            float yawSin = (float)Math.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
            float pitchCos = -((float)Math.cos(-pitch * ((float)Math.PI / 180)));
            float pitchSin = (float)Math.sin(-pitch * ((float)Math.PI / 180));
            double d = this.getMaxRange();
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
            double range = Math.min(d, PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, interactEntity)) + 1.0;
            Vec3 vec3 = positionEye;
            Intrinsics.checkNotNull(vec3);
            Vec3 lookAt = vec3.func_72441_c((double)(yawSin * pitchCos) * range, (double)pitchSin * range, (double)(yawCos * pitchCos) * range);
            MovingObjectPosition movingObjectPosition = boundingBox.func_72327_a(positionEye, lookAt);
            if (movingObjectPosition == null) {
                return;
            }
            MovingObjectPosition movingObject = movingObjectPosition;
            Vec3 hitVec = movingObject.field_72307_f;
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(interactEntity, new Vec3(hitVec.field_72450_a - interactEntity.field_70165_t, hitVec.field_72448_b - interactEntity.field_70163_u, hitVec.field_72449_c - interactEntity.field_70161_v)));
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(interactEntity, C02PacketUseEntity.Action.INTERACT));
        }
        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g()));
        this.blockingStatus = true;
    }

    private final void stopBlocking() {
        this.fakeBlock = false;
        if (this.blockingStatus) {
            if (StringsKt.equals((String)this.autoBlockModeValue.get(), "oldhypixel", true)) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(1.0, 1.0, 1.0), EnumFacing.DOWN));
            } else {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            }
            this.blockingStatus = false;
        }
    }

    public final void sendUseItem(@NotNull EntityPlayer playerIn, @Nullable World worldIn, @NotNull ItemStack itemStackIn) {
        Intrinsics.checkNotNullParameter(playerIn, "playerIn");
        Intrinsics.checkNotNullParameter(itemStackIn, "itemStackIn");
        if (MinecraftInstance.mc.field_71442_b.field_78779_k != WorldSettings.GameType.SPECTATOR) {
            PlayerControllerMP playerControllerMP = MinecraftInstance.mc.field_71442_b;
            if (playerControllerMP == null) {
                throw new NullPointerException("null cannot be cast to non-null type net.dev.important.utils.IPlayerControllerMP");
            }
            ((IPlayerControllerMP)playerControllerMP).invokeSyncCurrentItem();
            int i = itemStackIn.field_77994_a;
            ItemStack itemstack = itemStackIn.func_77957_a(worldIn, playerIn);
            if (!Intrinsics.areEqual(itemstack, itemStackIn) || itemstack.field_77994_a != i) {
                playerIn.field_71071_by.field_70462_a[playerIn.field_71071_by.field_70461_c] = itemstack;
                if (itemstack.field_77994_a == 0) {
                    playerIn.field_71071_by.field_70462_a[playerIn.field_71071_by.field_70461_c] = null;
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCancelRun() {
        if (MinecraftInstance.mc.field_71439_g.func_175149_v()) return true;
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer");
        if (!this.isAlive((EntityLivingBase)entityPlayerSP)) return true;
        Module module2 = Client.INSTANCE.getModuleManager().get(Blink.class);
        Intrinsics.checkNotNull(module2);
        if (module2.getState()) return true;
        Module module3 = Client.INSTANCE.getModuleManager().get(FreeCam.class);
        Intrinsics.checkNotNull(module3);
        if (module3.getState()) return true;
        if ((Boolean)this.noScaffValue.get() == false) return false;
        Module module4 = Client.INSTANCE.getModuleManager().get(Scaffold.class);
        Intrinsics.checkNotNull(module4);
        if (!module4.getState()) return false;
        return true;
    }

    private final boolean isAlive(EntityLivingBase entity) {
        return entity.func_70089_S() && entity.func_110143_aJ() > 0.0f || (Boolean)this.aacValue.get() != false && entity.field_70737_aN > 5;
    }

    private final boolean getCanBlock() {
        return MinecraftInstance.mc.field_71439_g.func_70694_bm() != null && MinecraftInstance.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemSword;
    }

    private final float getMaxRange() {
        return Math.max(((Number)this.rangeValue.get()).floatValue(), ((Number)this.throughWallsRangeValue.get()).floatValue());
    }

    private final float getRange(Entity entity) {
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNullExpressionValue(entityPlayerSP, "mc.thePlayer");
        return (PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, entity) >= (double)((Number)this.throughWallsRangeValue.get()).floatValue() ? ((Number)this.rangeValue.get()).floatValue() : ((Number)this.throughWallsRangeValue.get()).floatValue()) - (MinecraftInstance.mc.field_71439_g.func_70051_ag() ? ((Number)this.rangeSprintReducementValue.get()).floatValue() : 0.0f);
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.targetModeValue.get();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static final boolean updateHitable$lambda-4(KillAura this$0, Entity it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (((Boolean)this$0.livingRaycastValue.get()).booleanValue()) {
            if (!(it instanceof EntityLivingBase)) return false;
            if (it instanceof EntityArmorStand) return false;
        }
        if (this$0.isEnemy(it)) return true;
        if ((Boolean)this$0.raycastIgnoredValue.get() != false) return true;
        if ((Boolean)this$0.aacValue.get() == false) return false;
        List list = MinecraftInstance.mc.field_71441_e.func_72839_b(it, it.func_174813_aQ());
        Intrinsics.checkNotNullExpressionValue(list, "mc.theWorld.getEntitiesW\u2026it, it.entityBoundingBox)");
        if (((Collection)list).isEmpty()) return false;
        return true;
    }

    public static final int getKillCounts() {
        return Companion.getKillCounts();
    }

    public static final void setKillCounts(int n) {
        Companion.setKillCounts(n);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ IntegerValue access$getMinCPS$p(KillAura $this) {
        return $this.minCPS;
    }

    public static final /* synthetic */ void access$setAttackDelay$p(KillAura $this, long l) {
        $this.attackDelay = l;
    }

    public static final /* synthetic */ IntegerValue access$getMaxCPS$p(KillAura $this) {
        return $this.maxCPS;
    }

    public static final /* synthetic */ ListValue access$getRotations$p(KillAura $this) {
        return $this.rotations;
    }

    public static final /* synthetic */ FloatValue access$getMinSpinSpeed$p(KillAura $this) {
        return $this.minSpinSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMaxSpinSpeed$p(KillAura $this) {
        return $this.maxSpinSpeed;
    }

    public static final /* synthetic */ ListValue access$getAutoBlockModeValue$p(KillAura $this) {
        return $this.autoBlockModeValue;
    }

    public static final /* synthetic */ BoolValue access$getDisplayAutoBlockSettings$p(KillAura $this) {
        return $this.displayAutoBlockSettings;
    }

    public static final /* synthetic */ BoolValue access$getSmartAutoBlockValue$p(KillAura $this) {
        return $this.smartAutoBlockValue;
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

    public static final /* synthetic */ BoolValue access$getPredictValue$p(KillAura $this) {
        return $this.predictValue;
    }

    public static final /* synthetic */ FloatValue access$getMaxPredictSize$p(KillAura $this) {
        return $this.maxPredictSize;
    }

    public static final /* synthetic */ BoolValue access$getRandomCenterValue$p(KillAura $this) {
        return $this.randomCenterValue;
    }

    public static final /* synthetic */ FloatValue access$getMaxRand$p(KillAura $this) {
        return $this.maxRand;
    }

    public static final /* synthetic */ FloatValue access$getMinRand$p(KillAura $this) {
        return $this.minRand;
    }

    public static final /* synthetic */ BoolValue access$getNoInventoryAttackValue$p(KillAura $this) {
        return $this.noInventoryAttackValue;
    }

    public static final /* synthetic */ BoolValue access$getCircleValue$p(KillAura $this) {
        return $this.circleValue;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R$\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2={"Lnet/dev/important/modules/module/modules/combat/KillAura$Companion;", "", "()V", "killCounts", "", "getKillCounts$annotations", "getKillCounts", "()I", "setKillCounts", "(I)V", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        public final int getKillCounts() {
            return killCounts;
        }

        public final void setKillCounts(int n) {
            killCounts = n;
        }

        @JvmStatic
        public static /* synthetic */ void getKillCounts$annotations() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

