/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  net.minecraft.network.play.client.C0CPacketInput
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C0FPacketConfirmTransaction
 *  net.minecraft.network.play.client.C13PacketPlayerAbilities
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C17PacketCustomPayload
 *  net.minecraft.network.play.client.C18PacketSpectate
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S07PacketRespawn
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.network.play.server.S3EPacketTeams
 *  net.minecraft.network.play.server.S42PacketCombatEvent
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.fun;

import io.netty.buffer.Unpooled;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.BlinkUtils;
import net.ccbluex.liquidbounce.utils.ClassUtils;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.MovementUtilsFix;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.PosLookInstance;
import net.ccbluex.liquidbounce.utils.RenderUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.TimeHelper;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.ccbluex.liquidbounce.utils.timerNightX.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Disabler", category=ModuleCategory.FUN, description="Some Disabler / Report1337 ")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u00ee\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\n\u0010\u00b6\u0001\u001a\u00030\u00b7\u0001H\u0002J\u001d\u0010\u00b8\u0001\u001a\u00030\u00b7\u00012\b\u0010\u00b9\u0001\u001a\u00030\u00ba\u00012\t\b\u0002\u0010\u00bb\u0001\u001a\u00020\u0004J\u0011\u0010\u00bc\u0001\u001a\u00030\u00b7\u00012\u0007\u0010\u00bd\u0001\u001a\u00020\u0004J\f\u0010\u00be\u0001\u001a\u0005\u0018\u00010\u00bf\u0001H\u0002J\u0011\u0010\u00c0\u0001\u001a\u00020\u00042\b\u0010\u00c1\u0001\u001a\u00030\u00c2\u0001J\u0007\u0010\u00c3\u0001\u001a\u00020\u0004J\n\u0010\u00c4\u0001\u001a\u00030\u00b7\u0001H\u0016J\n\u0010\u00c5\u0001\u001a\u00030\u00b7\u0001H\u0016J\u0014\u0010\u00c6\u0001\u001a\u00030\u00b7\u00012\b\u0010\u00c7\u0001\u001a\u00030\u00c8\u0001H\u0007J\u0014\u0010\u00c9\u0001\u001a\u00030\u00b7\u00012\b\u0010\u00c7\u0001\u001a\u00030\u00ca\u0001H\u0007J\u0014\u0010\u00cb\u0001\u001a\u00030\u00b7\u00012\b\u0010\u00c7\u0001\u001a\u00030\u00cc\u0001H\u0007J\u0014\u0010\u00cd\u0001\u001a\u00030\u00b7\u00012\b\u0010\u00c7\u0001\u001a\u00030\u00ce\u0001H\u0007J\u0014\u0010\u00cf\u0001\u001a\u00030\u00b7\u00012\b\u0010\u00c7\u0001\u001a\u00030\u00d0\u0001H\u0007J\u0007\u0010\u00d1\u0001\u001a\u00020\u0004J\n\u0010\u00d2\u0001\u001a\u00030\u00b7\u0001H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u000207X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010C\u001a\u0012\u0012\u0004\u0012\u00020\b0Dj\b\u0012\u0004\u0012\u00020\b`EX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020JX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0018\u0010M\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030O0NX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010P\u001a\u0004\u0018\u00010QX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010X\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Y\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Z\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010[\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\\\u001a\u000207X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010]\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010^\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010_\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010`\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010a\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010c\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010d\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010e\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010g\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020h0O0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R6\u0010i\u001a*\u0012\u0004\u0012\u00020k\u0012\n\u0012\b\u0012\u0004\u0012\u00020h0O0jj\u0014\u0012\u0004\u0012\u00020k\u0012\n\u0012\b\u0012\u0004\u0012\u00020h0O`lX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010m\u001a\b\u0012\u0004\u0012\u00020n0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010o\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010p\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020h0O0qX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010r\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010s\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010t\u001a\b\u0012\u0004\u0012\u00020u0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010v\u001a\u00020wX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010x\u001a\u000207X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010y\u001a\u000207X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010z\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010{\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010|\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020h0O0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010}\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010~\u001a\u00020JX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u007f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0080\u0001\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0081\u0001\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0082\u0001\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0083\u0001\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001f\u0010\u0084\u0001\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u0012\n\u0000\u001a\u0006\b\u0085\u0001\u0010\u0086\u0001\"\u0006\b\u0087\u0001\u0010\u0088\u0001R\u000f\u0010\u0089\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008a\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008b\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008c\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008d\u0001\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008e\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u008f\u0001\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0090\u0001\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0091\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0092\u0001\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0093\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0094\u0001\u001a\u00030\u0095\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0096\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0097\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0098\u0001\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u0099\u0001\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009a\u0001\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009b\u0001\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001f\u0010\u009c\u0001\u001a\u0012\u0012\u0004\u0012\u00020n0Dj\b\u0012\u0004\u0012\u00020n`EX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009d\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009e\u0001\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u009f\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a0\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a1\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a2\u0001\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a3\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a4\u0001\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a5\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a6\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a7\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a8\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00a9\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00aa\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ab\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ac\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ad\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00ae\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00af\u0001\u001a\u000207X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b0\u0001\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000f\u0010\u00b1\u0001\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u00b2\u0001\u001a\u00030\u00b3\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u00b4\u0001\u001a\u00030\u00b3\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u00b5\u0001\u001a\u00030\u00b3\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u00d3\u0001"}, d2={"Lme/report/liquidware/modules/fun/Disabler;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "alrSendY", "", "alrSprint", "anotherQueue", "Ljava/util/LinkedList;", "Lnet/minecraft/network/play/client/C00PacketKeepAlive;", "autoAlert", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "avoidPhaseBpValue", "basic", "benHittingLean", "benTimer", "Lnet/ccbluex/liquidbounce/utils/timerNightX/MSTimer;", "blockdrop", "blocksmcAntiFlyCheck", "blocksmcBufferSizeValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "blocksmcFakeInput", "blocksmcFlagDelayValue", "blocksmcFlagValue", "blocksmcLobbyValue", "blocksmcSlientFlagApplyValue", "blocksmcValidPos", "boat", "buzzsemi", "c00Disabler", "c03Counter", "", "c0BDisabler", "c0fs", "canBlink", "canModify", "checkValid", "coldpvp", "compDecValue", "counter", "cubecraft", "currentBuffer", "currentDec", "currentDelay", "currentTrans", "debugValue", "decDelayMaxValue", "decDelayMinValue", "decTimer", "delayValue", "disableLogger", "disturbAmountValue", "dynamicValue", "expectedSetback", "flag", "flagMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "flagSilent", "flagTick", "funcraftstaff", "gwencombat", "heirteirac", "hycraft", "inCage", "input", "intave", "inventoryspoof", "jump", "keepAlives", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "lagTimer", "lastTick", "lastUid", "lastYaw", "", "latestblocksmc", "latestverus", "linkedQueue", "Ljava/util/Queue;", "Lnet/minecraft/network/Packet;", "lockRotation", "Lnet/ccbluex/liquidbounce/utils/Rotation;", "matrix", "matrixHotbarChange", "matrixMoveFix", "matrixMoveOnly", "matrixNoCheck", "matrixNoMovePacket", "maxpsf", "minBuffValue", "minpsf", "modifyC00Value", "modifyModeValue", "msTimer", "mushmc", "negativeValue", "noC03", "noC03s", "noC0BValue", "noMoveKeepRot", "oldmatrix", "oldwatchdog1", "oldwatchdog2", "packetBuffer", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "packetBus", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "packetQueue", "Lnet/minecraft/network/play/client/C0FPacketConfirmTransaction;", "packetinvade", "packets", "Ljava/util/concurrent/LinkedBlockingQueue;", "payload", "pingspoof", "playerQueue", "Lnet/minecraft/network/play/client/C03PacketPlayer;", "posLookInstance", "Lnet/ccbluex/liquidbounce/utils/PosLookInstance;", "psfSendMode", "psfStartSendMode", "psfWorldDelay", "pulseTimer", "queueBus", "rotModify", "rotatingSpeed", "rotationChanger", "runReset", "s08count", "sendDelay", "shouldActive", "shouldModifyRotation", "getShouldModifyRotation", "()Z", "setShouldModifyRotation", "(Z)V", "silentaccept", "spartancombat", "spectate", "staffValue", "statDecValue", "strafeDisabler", "strafePackets", "testDelay", "testFeature", "ticking", "tifality90", "timer", "Lnet/ccbluex/liquidbounce/utils/TimeHelper;", "timerA", "timerB", "timerCancelDelay", "timerCancelTimer", "timerShouldCancel", "transCount", "transactions", "tubnet", "uid", "universocraft", "vanilladesync", "verusAntiFlyCheck", "verusBufferSizeValue", "verusFakeInput", "verusFlagDelayValue", "verusFlagValue", "verusLobbyValue", "verusSlientFlagApplyValue", "verusValidPos", "veruscombat", "veruscombat2", "verussemi", "vulcanautoblock", "vulcancombat", "vulcanstrafe", "waitingDisplayMode", "watchDogAntiBan", "wdTimer", "x", "", "y", "z", "blink", "", "debug", "s", "", "force", "flush", "check", "getNearBoat", "Lnet/minecraft/entity/Entity;", "isInventory", "action", "", "isMoving", "onDisable", "onEnable", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender2D", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "shouldRun", "updateLagTime", "KyinoClient"})
public final class Disabler
extends Module {
    private final BoolValue basic = new BoolValue("Basic", false);
    private final BoolValue vanilladesync = new BoolValue("Vanilla-Desync", false);
    private final BoolValue silentaccept = new BoolValue("Silent-Accept", false);
    private final BoolValue packetinvade = new BoolValue("Packet-Invade", false);
    private final BoolValue coldpvp = new BoolValue("ColdPvP-KitPvP-Full", false);
    private final BoolValue funcraftstaff = new BoolValue("Funcraft-Staff", false);
    private final BoolValue blockdrop = new BoolValue("BlockDrop", false);
    private final BoolValue hycraft = new BoolValue("Hycraft", false);
    private final BoolValue inventoryspoof = new BoolValue("Inventory-Spoof", false);
    private final BoolValue tubnet = new BoolValue("Tubnet", false);
    private final BoolValue heirteirac = new BoolValue("Heirteir-AC", false);
    private final BoolValue intave = new BoolValue("Intave", false);
    private final BoolValue jump = new BoolValue("Jump", false);
    private final BoolValue boat = new BoolValue("Boat", false);
    private final BoolValue c0fs = new BoolValue("C0Fs", false);
    private final BoolValue latestverus = new BoolValue("Latest-Verus", false);
    private final BoolValue latestblocksmc = new BoolValue("Blocksmc-Combat", false);
    private final BoolValue verussemi = new BoolValue("Verus-Semi", false);
    private final BoolValue veruscombat = new BoolValue("Verus-Combat", false);
    private final BoolValue veruscombat2 = new BoolValue("Verus-Combat2", false);
    private final BoolValue gwencombat = new BoolValue("Gwen-Combat", false);
    private final BoolValue spartancombat = new BoolValue("Spartan-Combat", false);
    private final BoolValue pingspoof = new BoolValue("Ping-Spoof", false);
    private final BoolValue flag = new BoolValue("Flag", false);
    private final BoolValue input = new BoolValue("Input", false);
    private final BoolValue payload = new BoolValue("Payload", false);
    private final BoolValue spectate = new BoolValue("Spectate", false);
    private final BoolValue cubecraft = new BoolValue("Cubecraft", false);
    private final BoolValue mushmc = new BoolValue("MushMC", false);
    private final BoolValue universocraft = new BoolValue("Universocraft", false);
    private final BoolValue buzzsemi = new BoolValue("Buzz-Semi", false);
    private final BoolValue vulcancombat = new BoolValue("Vulcan-Combat", false);
    private final BoolValue vulcanstrafe = new BoolValue("Vulcan-Strafe", false);
    private final BoolValue vulcanautoblock = new BoolValue("Vulcan-AutoBlock", false);
    private final BoolValue matrix = new BoolValue("Matrix", false);
    private final BoolValue oldmatrix = new BoolValue("Old-Matrix", false);
    private final BoolValue oldwatchdog1 = new BoolValue("Old-Watchdog1", false);
    private final BoolValue oldwatchdog2 = new BoolValue("Old-Watchdog2", false);
    private final Queue<Packet<?>> linkedQueue = new LinkedBlockingQueue();
    private final TimeHelper timer = new TimeHelper();
    private final IntegerValue minpsf = new IntegerValue(this, "PingSpoof-MinDelay", 200, 0, 10000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPingspoof$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    }){
        final /* synthetic */ Disabler this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)Disabler.access$getMaxpsf$p(this.this$0).get()).intValue();
            if (v < newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5, $super_call_param$6);
        }
    };
    private final IntegerValue maxpsf = new IntegerValue(this, "PingSpoof-MaxDelay", 250, 0, 10000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPingspoof$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    }){
        final /* synthetic */ Disabler this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)Disabler.access$getMinpsf$p(this.this$0).get()).intValue();
            if (v > newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5, $super_call_param$6);
        }
    };
    private final ListValue psfStartSendMode = new ListValue("PingSpoof-StartSendMode", new String[]{"All", "First"}, "All", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPingspoof$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final ListValue psfSendMode = new ListValue("PingSpoof-SendMode", new String[]{"All", "First"}, "All", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPingspoof$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue psfWorldDelay = new IntegerValue("PingSpoof-WorldDelay", 15000, 0, 30000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPingspoof$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final ListValue flagMode = new ListValue("Flag-Mode", new String[]{"Edit", "Packet"}, "Edit", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getFlag$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue flagTick = new IntegerValue("Flag-TickDelay", 25, 1, 200, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getFlag$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue flagSilent = new BoolValue("Flag-SilentMode", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getFlag$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue matrixNoCheck = new BoolValue("Matrix-NoModuleCheck", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getMatrix$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue matrixMoveFix = new BoolValue("Matrix-MoveFix", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getMatrix$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue matrixMoveOnly = new BoolValue("Matrix-MoveOnly", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getMatrix$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue matrixNoMovePacket = new BoolValue("Matrix-NoMovePacket", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getMatrix$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue matrixHotbarChange = new BoolValue("Matrix-HotbarChange", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getMatrix$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue compDecValue = new BoolValue("VulcanDecrease", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcancombat$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue statDecValue = new IntegerValue("VulcanDecreaseDelay", 1500, 500, 2500, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcancombat$p(this.this$0).get() != false && (Boolean)Disabler.access$getCompDecValue$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue dynamicValue = new BoolValue("VulcanDynamicDelay", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcancombat$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue decDelayMinValue = new IntegerValue("VulcanMinDelay", 4500, 2000, 8000, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcancombat$p(this.this$0).get() != false && (Boolean)Disabler.access$getDynamicValue$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue decDelayMaxValue = new IntegerValue("VulcanMaxDelay", 5500, 2000, 8000, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcancombat$p(this.this$0).get() != false && (Boolean)Disabler.access$getDynamicValue$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue minBuffValue = new IntegerValue("VulcanMinBuff", 5, 0, 12, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcancombat$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue noC0BValue = new BoolValue("NoC0BPacket", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcancombat$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue avoidPhaseBpValue = new BoolValue("NoBadPacket", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcanstrafe$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue delayValue = new IntegerValue("PacketDelay", 6, 3, 10, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getVulcanstrafe$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue modifyC00Value = new BoolValue("Modify-C00", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPacketinvade$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final ListValue modifyModeValue = new ListValue("Modify-Mode", new String[]{"Disturb", "InvalidDisturb", "Invalid", "Drift"}, "Drift", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPacketinvade$p(this.this$0).get() != false && (Boolean)Disabler.access$getModifyC00Value$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue disturbAmountValue = new IntegerValue("Modify-Amount", 128, 1, 256, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPacketinvade$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue negativeValue = new BoolValue("Negative", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getPacketinvade$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue verusLobbyValue = new BoolValue("LobbyCheck", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestverus$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue verusFlagValue = new BoolValue("Verus-Flag", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestverus$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue verusSlientFlagApplyValue = new BoolValue("Verus-SilentFlagApply", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestverus$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue verusBufferSizeValue = new IntegerValue("Verus-QueueActiveSize", 300, 0, 1000, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestverus$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue verusFlagDelayValue = new IntegerValue("Verus-FlagDelay", 40, 40, 120, " tick", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestverus$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue verusAntiFlyCheck = new BoolValue("Verus-AntiFlight", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestverus$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue verusFakeInput = new BoolValue("Verus-FakeInput", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestverus$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue verusValidPos = new BoolValue("Verus-ValidPosition", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestverus$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue blocksmcLobbyValue = new BoolValue("LobbyCheck", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestblocksmc$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue blocksmcFlagValue = new BoolValue("Blocksmc-Flag", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestblocksmc$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue blocksmcSlientFlagApplyValue = new BoolValue("Blocksmc-SilentFlagApply", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestblocksmc$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue blocksmcBufferSizeValue = new IntegerValue("Blocksmc-QueueActiveSize", 300, 0, 1000, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestblocksmc$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue blocksmcFlagDelayValue = new IntegerValue("Blocksmc-FlagDelay", 40, 40, 120, " tick", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestblocksmc$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue blocksmcAntiFlyCheck = new BoolValue("Blocksmc-AntiFlight", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestblocksmc$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue blocksmcFakeInput = new BoolValue("Blocksmc-FakeInput", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestblocksmc$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue blocksmcValidPos = new BoolValue("Blocksmc-ValidPosition", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getLatestblocksmc$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final ListValue waitingDisplayMode = new ListValue("Waiting-Display", new String[]{"Top", "Middle", "Notification", "Chat", "None"}, "Top", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue autoAlert = new BoolValue("BanAlert", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue rotModify = new BoolValue("RotationModifier", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue tifality90 = new BoolValue("Tifality", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get() != false && (Boolean)Disabler.access$getRotModify$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue noMoveKeepRot = new BoolValue("NoMoveKeepRot", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get() != false && (Boolean)Disabler.access$getRotModify$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue noC03s = new BoolValue("NoC03s", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue testFeature = new BoolValue("PingSpoof", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue testDelay = new IntegerValue("Delay", 400, 0, 1000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get() != false && (Boolean)Disabler.access$getTestFeature$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue checkValid = new BoolValue("InvValidate", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog1$p(this.this$0).get() != false && (Boolean)Disabler.access$getTestFeature$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue rotationChanger = new BoolValue("RotationDisabler", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue c00Disabler = new BoolValue("KeepAliveDisabler", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue c0BDisabler = new BoolValue("C0BDisabler", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue watchDogAntiBan = new BoolValue("LessFlag", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue noC03 = new BoolValue("NoC03Packet", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue strafeDisabler = new BoolValue("StrafeDisabler", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final IntegerValue strafePackets = new IntegerValue("StrafeDisablerPacketAmount", 70, 60, 120, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue timerA = new BoolValue("Timer1", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue timerB = new BoolValue("Timer2", false, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private final BoolValue staffValue = new BoolValue("AntiStaff", true, new Function0<Boolean>(this){
        final /* synthetic */ Disabler this$0;

        public final boolean invoke() {
            return (Boolean)Disabler.access$getOldwatchdog2$p(this.this$0).get();
        }
        {
            this.this$0 = disabler;
            super(0);
        }
    });
    private double x;
    private double y;
    private double z;
    private final LinkedBlockingQueue<Packet<INetHandlerPlayServer>> packets = new LinkedBlockingQueue();
    private final MSTimer timerCancelDelay = new MSTimer();
    private final MSTimer timerCancelTimer = new MSTimer();
    private boolean timerShouldCancel = true;
    private boolean inCage = true;
    private boolean canBlink = true;
    private Rotation lockRotation;
    private int uid;
    private final BoolValue debugValue = new BoolValue("Debug", false);
    private float rotatingSpeed;
    private final ArrayList<C00PacketKeepAlive> keepAlives;
    private final ArrayList<C0FPacketConfirmTransaction> transactions;
    private final LinkedList<C0FPacketConfirmTransaction> packetQueue;
    private final LinkedList<C00PacketKeepAlive> anotherQueue;
    private final LinkedList<C03PacketPlayer> playerQueue;
    private final HashMap<Long, Packet<INetHandlerPlayServer>> packetBus;
    private final LinkedList<Packet<INetHandlerPlayServer>> queueBus;
    private final LinkedList<Packet<INetHandlerPlayServer>> packetBuffer;
    private final PosLookInstance posLookInstance;
    private final MSTimer msTimer;
    private final MSTimer wdTimer;
    private final MSTimer benTimer;
    private final MSTimer pulseTimer;
    private boolean disableLogger;
    private boolean alrSendY;
    private boolean alrSprint;
    private int c03Counter;
    private boolean expectedSetback;
    private int sendDelay;
    private boolean shouldActive;
    private boolean benHittingLean;
    private int transCount;
    private int counter;
    private boolean canModify;
    private boolean shouldModifyRotation;
    private int lastTick;
    private int s08count;
    private int ticking;
    private float lastYaw;
    private int lastUid;
    private int currentTrans;
    private int currentDelay;
    private int currentBuffer;
    private int currentDec;
    private final MSTimer lagTimer;
    private final MSTimer decTimer;
    private boolean runReset;

    public final boolean getShouldModifyRotation() {
        return this.shouldModifyRotation;
    }

    public final void setShouldModifyRotation(boolean bl) {
        this.shouldModifyRotation = bl;
    }

    public final boolean isMoving() {
        return Disabler.access$getMc$p$s1046033730().field_71439_g != null && (Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78900_b != 0.0f || Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78902_a != 0.0f || Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78899_d || Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78901_c);
    }

    public final void debug(@NotNull String s, boolean force) {
        Intrinsics.checkParameterIsNotNull(s, "s");
        if (((Boolean)this.debugValue.get()).booleanValue() || force) {
            ClientUtils.displayChatMessage("\u00a7c\u00a7l>> " + "\u00a7f" + s);
        }
    }

    public static /* synthetic */ void debug$default(Disabler disabler, String string, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        disabler.debug(string, bl);
    }

    public final boolean shouldRun() {
        return Disabler.access$getMc$p$s1046033730().field_71439_g != null && Disabler.access$getMc$p$s1046033730().field_71439_g.field_71071_by != null && ((Boolean)this.verusLobbyValue.get() == false || (Boolean)this.blocksmcLobbyValue.get() == false || !Disabler.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_146028_b(Items.field_151111_aL)) && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > 5;
    }

    public final boolean isInventory(short action) {
        return action > 0 && action < 100;
    }

    @Override
    public void onEnable() {
        if (((Boolean)this.vulcanstrafe.get()).booleanValue()) {
            this.c03Counter = -15;
        }
        if (((Boolean)this.oldwatchdog2.get()).booleanValue()) {
            this.counter = 0;
            this.inCage = true;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
            this.timerCancelDelay.reset();
            this.timerCancelTimer.reset();
        }
        if (((Boolean)this.boat.get()).booleanValue()) {
            Disabler.debug$default(this, "Place 2 boats next to each other and right click to use it!", false, 2, null);
        }
        if (((Boolean)this.intave.get()).booleanValue()) {
            this.msTimer.reset();
            this.linkedQueue.clear();
        }
        this.packetBuffer.clear();
        this.keepAlives.clear();
        this.transactions.clear();
        this.packetQueue.clear();
        this.anotherQueue.clear();
        this.playerQueue.clear();
        this.packetBus.clear();
        this.queueBus.clear();
        this.s08count = 0;
        this.pulseTimer.reset();
        this.msTimer.reset();
        this.wdTimer.reset();
        this.benTimer.reset();
        this.canModify = false;
        this.expectedSetback = false;
        this.shouldActive = false;
        this.alrSendY = false;
        this.alrSprint = false;
        this.transCount = 0;
        this.lastTick = 0;
        this.ticking = 0;
        this.lastUid = 0;
        this.posLookInstance.reset();
        this.shouldModifyRotation = false;
        this.benHittingLean = false;
        this.rotatingSpeed = 0.0f;
    }

    @Override
    public void onDisable() {
        C00PacketKeepAlive it;
        Iterable $this$forEach$iv = this.keepAlives;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            it = (C00PacketKeepAlive)element$iv;
            boolean bl = false;
            PacketUtils.sendPacketNoEvent((Packet)it);
        }
        $this$forEach$iv = this.transactions;
        $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            it = (C0FPacketConfirmTransaction)element$iv;
            boolean bl = false;
            PacketUtils.sendPacketNoEvent((Packet)it);
        }
        this.packetBuffer.clear();
        this.keepAlives.clear();
        this.transactions.clear();
        this.packetQueue.clear();
        this.anotherQueue.clear();
        this.packetBus.clear();
        this.lockRotation = null;
        if (((Boolean)this.inventoryspoof.get()).booleanValue()) {
            PacketUtils.sendPacketNoEvent((Packet)new C0DPacketCloseWindow());
        }
        if (((Boolean)this.vulcancombat.get()).booleanValue()) {
            this.updateLagTime();
            BlinkUtils.releasePacket$default(BlinkUtils.INSTANCE, "C0FPacketConfirmTransaction", false, 0, 0, 14, null);
            BlinkUtils.setBlinkState$default(BlinkUtils.INSTANCE, false, false, false, false, false, false, false, false, false, false, false, 2031, null);
        }
        if (((Boolean)this.oldwatchdog1.get()).booleanValue()) {
            $this$forEach$iv = this.anotherQueue;
            $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                it = (C00PacketKeepAlive)element$iv;
                boolean bl = false;
                PacketUtils.sendPacketNoEvent((Packet)it);
            }
            $this$forEach$iv = this.packetQueue;
            $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                it = (C0FPacketConfirmTransaction)element$iv;
                boolean bl = false;
                PacketUtils.sendPacketNoEvent((Packet)it);
            }
        }
        if (((Boolean)this.tubnet.get()).booleanValue()) {
            if (Disabler.access$getMc$p$s1046033730().field_71439_g == null) {
                return;
            }
            this.blink();
        }
        if (((Boolean)this.pingspoof.get()).booleanValue()) {
            Iterator iterator2 = this.queueBus.iterator();
            while (iterator2.hasNext()) {
                Packet p;
                Packet packet = p = (Packet)iterator2.next();
                Intrinsics.checkExpressionValueIsNotNull(packet, "p");
                PacketUtils.sendPacketNoEvent(packet);
            }
        }
        this.queueBus.clear();
        this.msTimer.reset();
        Disabler.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
        MovementUtils.strafe(0.0f);
        Disabler.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
        this.shouldModifyRotation = false;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.vulcanstrafe.get()).booleanValue()) {
            this.c03Counter = -15;
        }
        if (((Boolean)this.oldwatchdog2.get()).booleanValue()) {
            this.counter = 0;
            this.inCage = true;
        }
        if (((Boolean)this.vulcancombat.get()).booleanValue()) {
            BlinkUtils.clearPacket$default(BlinkUtils.INSTANCE, "C0FPacketConfirmTransaction", false, 0, 6, null);
            this.currentTrans = 0;
            this.updateLagTime();
            this.runReset = (Boolean)this.noC0BValue.get();
        }
        this.packetBuffer.clear();
        this.transactions.clear();
        this.keepAlives.clear();
        this.packetQueue.clear();
        this.anotherQueue.clear();
        this.playerQueue.clear();
        this.packetBus.clear();
        this.queueBus.clear();
        this.canModify = false;
        this.s08count = 0;
        this.msTimer.reset();
        this.wdTimer.reset();
        this.benTimer.reset();
        this.expectedSetback = false;
        this.shouldActive = false;
        this.alrSendY = false;
        this.alrSprint = false;
        this.benHittingLean = false;
        this.transCount = 0;
        this.counter = 0;
        this.lastTick = 0;
        this.ticking = 0;
        this.lastUid = 0;
        this.posLookInstance.reset();
        this.rotatingSpeed = 0.0f;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.shouldActive) return;
        ScaledResolution sc = new ScaledResolution(Disabler.access$getMc$p$s1046033730());
        float strength = RangesKt.coerceIn((float)this.msTimer.hasTimeLeft(((Number)this.psfWorldDelay.get()).intValue()) / (float)((Number)this.psfWorldDelay.get()).intValue(), 0.0f, 1.0f);
        if (((Boolean)this.pingspoof.get()).booleanValue()) {
            Stencil.write(true);
            RenderUtils.drawRoundedRectDisabler((float)sc.func_78326_a() / 2.0f - 50.0f, 35.0f, (float)sc.func_78326_a() / 2.0f + 50.0f, 55.0f, 10.0f, new Color(0, 0, 0, 140).getRGB());
            Stencil.erase(true);
            RenderUtils.drawRect((float)sc.func_78326_a() / 2.0f - 50.0f, 35.0f, (float)sc.func_78326_a() / 2.0f - 50.0f + 100.0f * strength, 55.0f, new Color(0, 111, 255, 70).getRGB());
            Stencil.dispose();
            GameFontRenderer gameFontRenderer = Fonts.font40;
            if (gameFontRenderer != null) {
                gameFontRenderer.drawCenteredString((int)((float)this.msTimer.hasTimeLeft(((Number)this.psfWorldDelay.get()).intValue()) / 1000.0f) + "s left...", (float)sc.func_78326_a() / 2.0f, 41.0f, -1);
            }
        }
        if (!((Boolean)this.oldwatchdog1.get()).booleanValue() || !((Boolean)this.testFeature.get()).booleanValue() || ServerUtils.isHypixelLobby()) return;
        Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        if (minecraft.func_71356_B()) return;
        String string = (String)this.waitingDisplayMode.get();
        Locale locale = Locale.getDefault();
        Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.getDefault()");
        Locale locale2 = locale;
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase(locale2);
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase(locale)");
        string = string3;
        switch (string.hashCode()) {
            case -1074341483: {
                if (!string.equals("middle")) return;
                break;
            }
            case 115029: {
                if (!string.equals("top")) return;
                Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f, 61.5f, new Color(0, 0, 0).getRGB(), false);
                Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f, 62.5f, new Color(0, 0, 0).getRGB(), false);
                Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f - 0.5f, 62.0f, new Color(0, 0, 0).getRGB(), false);
                Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f + 0.5f, 62.0f, new Color(0, 0, 0).getRGB(), false);
                Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f, 62.0f, new Color(220, 220, 60).getRGB(), false);
                return;
            }
        }
        Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f, (float)sc.func_78328_b() / 2.0f + 14.5f, new Color(0, 0, 0).getRGB(), false);
        Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f, (float)sc.func_78328_b() / 2.0f + 15.5f, new Color(0, 0, 0).getRGB(), false);
        Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f - 0.5f, (float)sc.func_78328_b() / 2.0f + 15.0f, new Color(0, 0, 0).getRGB(), false);
        Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f + 0.5f, (float)sc.func_78328_b() / 2.0f + 15.0f, new Color(0, 0, 0).getRGB(), false);
        Fonts.minecraftFont.func_175065_a("Disabling Watchdog...", (float)sc.func_78326_a() / 2.0f - (float)Fonts.minecraftFont.func_78256_a("Disabling watchdog...") / 2.0f, (float)sc.func_78328_b() / 2.0f + 15.0f, new Color(220, 220, 60).getRGB(), false);
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        block201: {
            block202: {
                block199: {
                    block200: {
                        Intrinsics.checkParameterIsNotNull(event, "event");
                        packet = event.getPacket();
                        p = event.getPacket();
                        if (((Boolean)this.boat.get()).booleanValue()) {
                            if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70154_o != null) {
                                this.canModify = true;
                            }
                            if (this.canModify && packet instanceof C03PacketPlayer) {
                                ((C03PacketPlayer)packet).field_149474_g = true;
                            }
                        }
                        if (((Boolean)this.c0fs.get()).booleanValue() && packet instanceof C0FPacketConfirmTransaction) {
                            event.cancelEvent();
                        }
                        if (((Boolean)this.universocraft.get()).booleanValue() && packet instanceof C0FPacketConfirmTransaction) {
                            if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                                this.uid = ((C0FPacketConfirmTransaction)packet).func_149533_d();
                                Disabler.debug$default(this, "funny 3", false, 2, null);
                            } else {
                                ((C0FPacketConfirmTransaction)packet).field_149534_b = (short)(this.uid - 2);
                                Disabler.debug$default(this, "funny 2", false, 2, null);
                            }
                        }
                        if (((Boolean)this.coldpvp.get()).booleanValue()) {
                            if (packet instanceof C00PacketKeepAlive) {
                                event.cancelEvent();
                                Disabler.debug$default(this, "funny 1", false, 2, null);
                            }
                            if (packet instanceof C0BPacketEntityAction && (((C0BPacketEntityAction)packet).func_180764_b() == C0BPacketEntityAction.Action.STOP_SPRINTING || ((C0BPacketEntityAction)packet).func_180764_b() == C0BPacketEntityAction.Action.START_SPRINTING)) {
                                event.cancelEvent();
                                Disabler.debug$default(this, "funny 2", false, 2, null);
                            }
                            if (packet instanceof C03PacketPlayer) {
                                PacketUtils.sendPacketNoEvent((Packet)new C0CPacketInput(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70702_br, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70701_bs, Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78901_c, Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78899_d));
                                x = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                                y = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                                z = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
                                yaw = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70177_z;
                                pitch = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70125_A;
                                ground = ((C03PacketPlayer)packet).field_149474_g;
                                if (((C03PacketPlayer)packet).func_149466_j()) {
                                    PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw, pitch, ground));
                                }
                            }
                        }
                        if (((Boolean)this.packetinvade.get()).booleanValue() && packet instanceof C00PacketKeepAlive && ((Boolean)this.modifyC00Value.get()).booleanValue()) {
                            x = (String)this.modifyModeValue.get();
                            tmp = -1;
                            switch (x.hashCode()) {
                                case -958421537: {
                                    if (!x.equals("Disturb")) break;
                                    tmp = 1;
                                    break;
                                }
                                case 66299785: {
                                    if (!x.equals("Drift")) break;
                                    tmp = 2;
                                    break;
                                }
                                case 351775784: {
                                    if (!x.equals("InvalidDisturb")) break;
                                    tmp = 3;
                                    break;
                                }
                                case -670529065: {
                                    if (!x.equals("Invalid")) break;
                                    tmp = 4;
                                    break;
                                }
                            }
                            switch (tmp) {
                                case 1: {
                                    amounts = RandomUtils.nextInt(1, ((Number)this.disturbAmountValue.get()).intValue() * 1024);
                                    if (((Boolean)this.negativeValue.get()).booleanValue()) {
                                        ((C00PacketKeepAlive)packet).field_149461_a -= amounts;
                                        break;
                                    }
                                    ((C00PacketKeepAlive)packet).field_149461_a += amounts;
                                    break;
                                }
                                case 3: {
                                    amounts = RandomUtils.nextInt(1024, 0x7FFFFFFF);
                                    if (((Boolean)this.negativeValue.get()).booleanValue()) {
                                        ((C00PacketKeepAlive)packet).field_149461_a -= amounts;
                                        break;
                                    }
                                    ((C00PacketKeepAlive)packet).field_149461_a += amounts;
                                    break;
                                }
                                case 4: {
                                    ((C00PacketKeepAlive)packet).field_149461_a = 0;
                                    break;
                                }
                                case 2: {
                                    if (((Boolean)this.negativeValue.get()).booleanValue()) {
                                        ((C00PacketKeepAlive)packet).field_149461_a -= ((Number)this.disturbAmountValue.get()).intValue();
                                        break;
                                    }
                                    ((C00PacketKeepAlive)packet).field_149461_a += ((Number)this.disturbAmountValue.get()).intValue();
                                }
                            }
                            Disabler.debug$default(this, "Modified C00:" + ((C00PacketKeepAlive)packet).field_149461_a, false, 2, null);
                        }
                        if (((Boolean)this.spectate.get()).booleanValue()) {
                            if (packet instanceof C03PacketPlayer) {
                                ((C03PacketPlayer)packet).field_149474_g = false;
                                v0 = Disabler.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(v0, "mc");
                                v1 = v0.func_147114_u();
                                v2 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(v2, "mc.thePlayer");
                                v1.func_147297_a((Packet)new C18PacketSpectate(v2.func_110124_au()));
                                v3 = Disabler.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(v3, "mc");
                                v3.func_147114_u().func_147297_a((Packet)new C13PacketPlayerAbilities(Disabler.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ));
                                Disabler.debug$default(this, "Packet C18 + C13", false, 2, null);
                            }
                            if (packet instanceof C13PacketPlayerAbilities) {
                                Disabler.debug$default(this, "Packet C13", false, 2, null);
                                ((C13PacketPlayerAbilities)packet).func_149483_b(true);
                                ((C13PacketPlayerAbilities)packet).func_149490_a(true);
                                ((C13PacketPlayerAbilities)packet).func_149491_c(true);
                                ((C13PacketPlayerAbilities)packet).func_149493_d(false);
                            }
                        }
                        if (((Boolean)this.vulcancombat.get()).booleanValue()) {
                            if (packet instanceof C0BPacketEntityAction && ((Boolean)this.noC0BValue.get()).booleanValue()) {
                                event.cancelEvent();
                                Disabler.debug$default(this, "C0B-EntityAction CANCELLED", false, 2, null);
                            }
                            if (packet instanceof C0FPacketConfirmTransaction) {
                                BlinkUtils.setBlinkState$default(BlinkUtils.INSTANCE, false, false, false, false, false, false, false, false, false, false, false, 2031, null);
                                transUID = ((C0FPacketConfirmTransaction)packet).field_149534_b;
                                if (transUID >= -25767 && transUID <= -24769) {
                                    BlinkUtils.setBlinkState$default(BlinkUtils.INSTANCE, false, false, false, false, true, false, false, false, false, false, false, 2031, null);
                                    Disabler.debug$default(this, "C0F-PingTickCounter IN " + BlinkUtils.INSTANCE.bufferSize("C0FPacketConfirmTransaction"), false, 2, null);
                                } else if (transUID == -30000) {
                                    BlinkUtils.setBlinkState$default(BlinkUtils.INSTANCE, false, false, false, false, true, false, false, false, false, false, false, 2031, null);
                                    Disabler.debug$default(this, "C0F-OnSpawn IN " + BlinkUtils.INSTANCE.bufferSize("C0FPacketConfirmTransaction"), false, 2, null);
                                }
                            }
                        }
                        if (!((Boolean)this.oldwatchdog2.get()).booleanValue()) break block199;
                        this.canBlink = true;
                        if ((float)Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > 200.0f) {
                            this.inCage = false;
                        }
                        if (((Boolean)this.timerA.get()).booleanValue() && !this.inCage && (packet instanceof C02PacketUseEntity || packet instanceof C03PacketPlayer || packet instanceof C07PacketPlayerDigging || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > ((Number)this.strafePackets.get()).intValue()) && this.timerShouldCancel) {
                            if (!this.timerCancelTimer.hasTimePassed(350L)) {
                                this.packets.add(packet);
                                event.cancelEvent();
                                this.canBlink = false;
                            } else {
                                Disabler.debug$default(this, "Timer 1 release packets", false, 2, null);
                                Disabler.debug$default(this, "Size " + String.valueOf(this.packets.size()), false, 2, null);
                                this.timerShouldCancel = false;
                                while (!this.packets.isEmpty()) {
                                    v4 = this.packets.take();
                                    Intrinsics.checkExpressionValueIsNotNull(v4, "packets.take()");
                                    PacketUtils.sendPacketNoEvent(v4);
                                }
                            }
                            killAura = (KillAura)LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
                            if (killAura != null) {
                                v5 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(v5, "mc.thePlayer");
                                if ((v5.func_71039_bw() || ((String)killAura.getAutoBlockModeValue().get()).equals("None")) && killAura.getTarget() != null) {
                                    v6 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                                    Intrinsics.checkExpressionValueIsNotNull(v6, "mc.thePlayer");
                                    if (v6.func_70694_bm() != null) {
                                        v7 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                                        Intrinsics.checkExpressionValueIsNotNull(v7, "mc.thePlayer");
                                        v8 = v7.func_70694_bm();
                                        Intrinsics.checkExpressionValueIsNotNull(v8, "mc.thePlayer.heldItem");
                                        if (v8.func_77973_b() instanceof ItemSword) {
                                            Disabler.debug$default(this, "Timer 1 release packets", false, 2, null);
                                            Disabler.debug$default(this, "Size " + String.valueOf(this.packets.size()), false, 2, null);
                                            this.timerShouldCancel = false;
                                            while (!this.packets.isEmpty()) {
                                                v9 = this.packets.take();
                                                Intrinsics.checkExpressionValueIsNotNull(v9, "packets.take()");
                                                PacketUtils.sendPacketNoEvent(v9);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (((Boolean)this.timerB.get()).booleanValue() && !this.inCage && (packet instanceof C02PacketUseEntity || packet instanceof C03PacketPlayer || packet instanceof C07PacketPlayerDigging || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > ((Number)this.strafePackets.get()).intValue()) && this.timerShouldCancel) {
                            if (!this.timerCancelTimer.hasTimePassed(250L)) {
                                this.packets.add(packet);
                                event.cancelEvent();
                                this.canBlink = false;
                            } else {
                                Disabler.debug$default(this, "Timer 2 release packets", false, 2, null);
                                Disabler.debug$default(this, "Size " + String.valueOf(this.packets.size()), false, 2, null);
                                this.timerShouldCancel = false;
                                while (!this.packets.isEmpty()) {
                                    v10 = this.packets.take();
                                    Intrinsics.checkExpressionValueIsNotNull(v10, "packets.take()");
                                    PacketUtils.sendPacketNoEvent(v10);
                                }
                            }
                            killAura = (KillAura)LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
                            if (killAura != null) {
                                v11 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(v11, "mc.thePlayer");
                                if ((v11.func_71039_bw() || ((String)killAura.getAutoBlockModeValue().get()).equals("None")) && killAura.getTarget() != null) {
                                    v12 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                                    Intrinsics.checkExpressionValueIsNotNull(v12, "mc.thePlayer");
                                    if (v12.func_70694_bm() != null) {
                                        v13 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                                        Intrinsics.checkExpressionValueIsNotNull(v13, "mc.thePlayer");
                                        v14 = v13.func_70694_bm();
                                        Intrinsics.checkExpressionValueIsNotNull(v14, "mc.thePlayer.heldItem");
                                        if (v14.func_77973_b() instanceof ItemSword) {
                                            Disabler.debug$default(this, "Timer 2 release packets", false, 2, null);
                                            Disabler.debug$default(this, "Size " + String.valueOf(this.packets.size()), false, 2, null);
                                            this.timerShouldCancel = false;
                                            while (!this.packets.isEmpty()) {
                                                v15 = this.packets.take();
                                                Intrinsics.checkExpressionValueIsNotNull(v15, "packets.take()");
                                                PacketUtils.sendPacketNoEvent(v15);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C05PacketPlayerLook) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && ((Boolean)this.noC03.get()).booleanValue()) {
                            event.cancelEvent();
                            this.canBlink = false;
                        }
                        if (((Boolean)this.strafeDisabler.get()).booleanValue() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa < ((Number)this.strafePackets.get()).intValue() && packet instanceof C03PacketPlayer && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 15 != 0) {
                            event.cancelEvent();
                            this.canBlink = false;
                        }
                        if (!((Boolean)this.watchDogAntiBan.get()).booleanValue() && (!((Boolean)this.strafeDisabler.get()).booleanValue() || Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa >= ((Number)this.strafePackets.get()).intValue())) break block199;
                        if (!(event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) || !Disabler.access$getMc$p$s1046033730().field_71439_g.field_70122_E || !(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70143_R > (float)10)) break block200;
                        if (this.counter > 0 && ((C03PacketPlayer.C06PacketPlayerPosLook)event.getPacket()).field_149479_a == this.x && ((C03PacketPlayer.C06PacketPlayerPosLook)event.getPacket()).field_149477_b == this.y && ((C03PacketPlayer.C06PacketPlayerPosLook)event.getPacket()).field_149478_c == this.z) {
                            v16 = Disabler.access$getMc$p$s1046033730();
                            Intrinsics.checkExpressionValueIsNotNull(v16, "mc");
                            v17 = v16.func_147114_u();
                            Intrinsics.checkExpressionValueIsNotNull(v17, "mc.netHandler");
                            v17.func_147298_b().func_179290_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer.C06PacketPlayerPosLook)event.getPacket()).field_149479_a, ((C03PacketPlayer.C06PacketPlayerPosLook)event.getPacket()).field_149477_b, ((C03PacketPlayer.C06PacketPlayerPosLook)event.getPacket()).field_149478_c, ((C03PacketPlayer.C06PacketPlayerPosLook)event.getPacket()).field_149474_g));
                            Disabler.debug$default(this, "Packet C04", false, 2, null);
                            event.cancelEvent();
                        }
                        ++this.counter;
                        if (!(event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook)) ** GOTO lbl-1000
                        v18 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(v18, "mc.thePlayer");
                        if (v18.func_70115_ae()) {
                            v19 = Disabler.access$getMc$p$s1046033730();
                            Intrinsics.checkExpressionValueIsNotNull(v19, "mc");
                            v19.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Disabler.access$getMc$p$s1046033730().field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                            Disabler.debug$default(this, "Packet C0B", false, 2, null);
                        } else if (event.getPacket() instanceof C0CPacketInput) {
                            v20 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                            Intrinsics.checkExpressionValueIsNotNull(v20, "mc.thePlayer");
                            if (v20.func_70115_ae()) {
                                v21 = Disabler.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(v21, "mc");
                                v22 = v21.func_147114_u();
                                Intrinsics.checkExpressionValueIsNotNull(v22, "mc.netHandler");
                                v22.func_147298_b().func_179290_a(event.getPacket());
                                v23 = Disabler.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(v23, "mc");
                                v23.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Disabler.access$getMc$p$s1046033730().field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
                                Disabler.debug$default(this, "Packet C0B", false, 2, null);
                                event.cancelEvent();
                            }
                        }
                    }
                    if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                        s08 = event.getPacket();
                        this.x = ((S08PacketPlayerPosLook)s08).field_148940_a;
                        this.y = ((S08PacketPlayerPosLook)s08).field_148938_b;
                        this.z = ((S08PacketPlayerPosLook)s08).field_148939_c;
                        Disabler.debug$default(this, "Receive S08", false, 2, null);
                    }
                    if (event.getPacket() instanceof S07PacketRespawn) {
                        this.counter = 0;
                    }
                }
                if (((Boolean)this.inventoryspoof.get()).booleanValue() && packet instanceof C0DPacketCloseWindow) {
                    event.cancelEvent();
                    Disabler.debug$default(this, "spoof inventory", false, 2, null);
                }
                if (((Boolean)this.mushmc.get()).booleanValue()) {
                    if (packet instanceof C16PacketClientStatus || packet instanceof C0APacketAnimation || packet instanceof S42PacketCombatEvent || packet instanceof S3EPacketTeams) {
                        event.cancelEvent();
                        Disabler.debug$default(this, "funny packet", false, 2, null);
                    }
                    Disabler.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75098_d = true;
                }
                if (((Boolean)this.cubecraft.get()).booleanValue()) {
                    if (packet instanceof C03PacketPlayer && Disabler.access$getMc$p$s1046033730().field_71462_r == null) {
                        PacketUtils.sendPacketNoEvent((Packet)new C08PacketPlayerBlockPlacement(new BlockPos((Entity)Disabler.access$getMc$p$s1046033730().field_71439_g).func_177979_c(5), EnumFacing.UP.func_176745_a(), null, 0.0f, 1.0f, 0.0f));
                        Disabler.debug$default(this, "disable", false, 2, null);
                    } else if (packet instanceof C00PacketKeepAlive) {
                        event.cancelEvent();
                        Disabler.debug$default(this, "fix", false, 2, null);
                    }
                }
                if (((Boolean)this.input.get()).booleanValue()) {
                    PacketUtils.sendPacketNoEvent((Packet)new C0CPacketInput(0.98f, 0.0f, true, true));
                    Disabler.debug$default(this, "input", false, 2, null);
                }
                if (((Boolean)this.vulcanautoblock.get()).booleanValue() && packet instanceof C17PacketCustomPayload) {
                    event.cancelEvent();
                    Disabler.debug$default(this, "fix autoblock packets", false, 2, null);
                }
                if (((Boolean)this.vulcanstrafe.get()).booleanValue() && packet instanceof C03PacketPlayer) {
                    s08 = this.c03Counter;
                    this.c03Counter = s08 + 1;
                    if (((C03PacketPlayer)packet).func_149466_j()) {
                        if (this.c03Counter >= ((Number)this.delayValue.get()).intValue()) {
                            Disabler.debug$default(this, "packet 1", false, 2, null);
                            PacketUtils.sendPacketNoEvent((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, (Boolean)this.avoidPhaseBpValue.get() != false ? BlockPos.field_177992_a : new BlockPos(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v), EnumFacing.DOWN));
                            this.c03Counter = 0;
                        } else if (this.c03Counter == ((Number)this.delayValue.get()).intValue() - 2) {
                            Disabler.debug$default(this, "packet 2", false, 2, null);
                            PacketUtils.sendPacketNoEvent((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, BlockPos.field_177992_a, EnumFacing.DOWN));
                        }
                    }
                }
                if (((Boolean)this.spartancombat.get()).booleanValue()) {
                    if (packet instanceof C00PacketKeepAlive && (this.keepAlives.size() <= 0 || Intrinsics.areEqual(packet, this.keepAlives.get(this.keepAlives.size() - 1)) ^ true)) {
                        Disabler.debug$default(this, "c00 added", false, 2, null);
                        this.keepAlives.add((C00PacketKeepAlive)packet);
                        event.cancelEvent();
                    }
                    if (packet instanceof C0FPacketConfirmTransaction && (this.transactions.size() <= 0 || Intrinsics.areEqual(packet, this.transactions.get(this.transactions.size() - 1)) ^ true)) {
                        Disabler.debug$default(this, "c0f added", false, 2, null);
                        this.transactions.add((C0FPacketConfirmTransaction)packet);
                        event.cancelEvent();
                    }
                }
                if (((Boolean)this.heirteirac.get()).booleanValue()) {
                    if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa < 120) {
                        return;
                    }
                    if (packet instanceof S08PacketPlayerPosLook) {
                        x = ((S08PacketPlayerPosLook)packet).field_148940_a - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                        y = ((S08PacketPlayerPosLook)packet).field_148938_b - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                        z = ((S08PacketPlayerPosLook)packet).field_148939_c - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
                        var12_49 = x * x + y * y + z * z;
                        var14_60 = false;
                        diff = Math.sqrt(var12_49);
                        if (diff < (double)12) {
                            event.cancelEvent();
                            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook)packet).field_148940_a, ((S08PacketPlayerPosLook)packet).field_148938_b, ((S08PacketPlayerPosLook)packet).field_148939_c, ((S08PacketPlayerPosLook)packet).func_148931_f(), ((S08PacketPlayerPosLook)packet).func_148930_g(), Disabler.access$getMc$p$s1046033730().field_71441_e.func_175623_d(new BlockPos(((S08PacketPlayerPosLook)packet).field_148940_a, ((S08PacketPlayerPosLook)packet).field_148938_b - 0.1, ((S08PacketPlayerPosLook)packet).field_148939_c)) == false));
                        }
                    }
                }
                if (((Boolean)this.intave.get()).booleanValue()) {
                    if (packet instanceof C00PacketKeepAlive) {
                        event.cancelEvent();
                        this.linkedQueue.add(event.getPacket());
                    }
                    if (packet instanceof C0FPacketConfirmTransaction) {
                        if (((C0FPacketConfirmTransaction)packet).func_149533_d() > 0 && ((C0FPacketConfirmTransaction)packet).func_149533_d() < 75) {
                            Disabler.debug$default(this, "funny intave moment", false, 2, null);
                            return;
                        }
                        this.linkedQueue.add(event.getPacket());
                        event.cancelEvent();
                    }
                    if (Disabler.access$getMc$p$s1046033730().field_71439_g != null && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa < 8) {
                        this.linkedQueue.clear();
                        this.msTimer.reset();
                    }
                }
                if (((Boolean)this.funcraftstaff.get()).booleanValue() && packet instanceof C03PacketPlayer) {
                    v24 = Disabler.access$getMc$p$s1046033730().field_71439_g.field_71174_a;
                    Intrinsics.checkExpressionValueIsNotNull(v24, "mc.thePlayer.sendQueue");
                    v24.func_147298_b().func_179290_a((Packet)new C00PacketKeepAlive(-2));
                    Disabler.debug$default(this, "LOL", false, 2, null);
                }
                if (((Boolean)this.basic.get()).booleanValue()) {
                    if (packet instanceof C00PacketKeepAlive) {
                        Disabler.debug$default(this, "cancel c00", false, 2, null);
                        event.cancelEvent();
                    }
                    if (packet instanceof C0FPacketConfirmTransaction) {
                        Disabler.debug$default(this, "cancel c0f", false, 2, null);
                        event.cancelEvent();
                    }
                }
                if (((Boolean)this.oldmatrix.get()).booleanValue() && packet instanceof C03PacketPlayer && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 15 == 0) {
                    try {
                        b = new ByteArrayOutputStream();
                        _out = new DataOutputStream(b);
                        v25 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(v25, "mc.thePlayer");
                        v26 = v25.func_146103_bH();
                        Intrinsics.checkExpressionValueIsNotNull(v26, "mc.thePlayer.gameProfile");
                        _out.writeUTF(v26.getName());
                        buf = new PacketBuffer(Unpooled.buffer());
                        buf.writeBytes(b.toByteArray());
                        v27 = Disabler.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(v27, "mc");
                        v27.func_147114_u().func_147297_a((Packet)new C17PacketCustomPayload("matrix:geyser", buf));
                        Disabler.debug$default(this, "Sent Matrix spoof packet.", false, 2, null);
                    }
                    catch (IOException e) {
                        Disabler.debug$default(this, "Error occurred.", false, 2, null);
                    }
                }
                if (((Boolean)this.tubnet.get()).booleanValue() && packet instanceof C03PacketPlayer && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 15 == 0) {
                    try {
                        b = new ByteArrayOutputStream();
                        _out = new DataOutputStream(b);
                        v28 = Disabler.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(v28, "mc.thePlayer");
                        v29 = v28.func_146103_bH();
                        Intrinsics.checkExpressionValueIsNotNull(v29, "mc.thePlayer.gameProfile");
                        _out.writeUTF(v29.getName());
                        buf = new PacketBuffer(Unpooled.buffer());
                        buf.writeBytes(b.toByteArray());
                        v30 = Disabler.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(v30, "mc");
                        v30.func_147114_u().func_147297_a((Packet)new C17PacketCustomPayload("matrix:geyser", buf));
                        Disabler.debug$default(this, "packet 1", false, 2, null);
                    }
                    catch (IOException e) {
                        Disabler.debug$default(this, "packet fix", false, 2, null);
                    }
                    if (Disabler.access$getMc$p$s1046033730().field_71439_g == null || this.disableLogger) {
                        return;
                    }
                    if (packet instanceof C03PacketPlayer) {
                        event.cancelEvent();
                    }
                    if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
                        event.cancelEvent();
                        this.packets.add(packet);
                    }
                }
                if (((Boolean)this.blockdrop.get()).booleanValue()) {
                    if (packet instanceof C00PacketKeepAlive && (this.keepAlives.size() <= 0 || Intrinsics.areEqual(packet, this.keepAlives.get(this.keepAlives.size() - 1)) ^ true)) {
                        Disabler.debug$default(this, "c00 added", false, 2, null);
                        this.keepAlives.add((C00PacketKeepAlive)packet);
                        event.cancelEvent();
                    }
                    if (packet instanceof C0FPacketConfirmTransaction && (this.transactions.size() <= 0 || Intrinsics.areEqual(packet, this.transactions.get(this.transactions.size() - 1)) ^ true)) {
                        Disabler.debug$default(this, "c0f added", false, 2, null);
                        this.transactions.add((C0FPacketConfirmTransaction)packet);
                        event.cancelEvent();
                    }
                    if (packet instanceof C13PacketPlayerAbilities && ((Boolean)this.staffValue.get()).booleanValue()) {
                        event.cancelEvent();
                    }
                }
                if (((Boolean)this.jump.get()).booleanValue() && packet instanceof C03PacketPlayer) {
                    capabilities = new PlayerCapabilities();
                    capabilities.field_75102_a = false;
                    capabilities.field_75100_b = true;
                    capabilities.field_75101_c = false;
                    capabilities.field_75098_d = false;
                    v31 = Disabler.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(v31, "mc");
                    v31.func_147114_u().func_147297_a((Packet)new C13PacketPlayerAbilities(capabilities));
                    Disabler.debug$default(this, "jump added", false, 2, null);
                }
                if (((Boolean)this.gwencombat.get()).booleanValue() && packet instanceof C00PacketKeepAlive) {
                    event.cancelEvent();
                    PacketUtils.sendPacketNoEvent((Packet)new C00PacketKeepAlive(((C00PacketKeepAlive)packet).field_149461_a - RandomUtils.nextInt(1000, 0x7FFFFFFF)));
                    Disabler.debug$default(this, "cancel c00", false, 2, null);
                }
                if (((Boolean)this.latestverus.get()).booleanValue()) {
                    if (!this.shouldRun()) {
                        this.msTimer.reset();
                        this.packetQueue.clear();
                        return;
                    }
                    if (packet instanceof C0FPacketConfirmTransaction && !this.isInventory(((C0FPacketConfirmTransaction)packet).field_149534_b)) {
                        this.packetQueue.add((C0FPacketConfirmTransaction)packet);
                        event.cancelEvent();
                        if (this.packetQueue.size() > ((Number)this.verusBufferSizeValue.get()).intValue()) {
                            if (!this.shouldActive) {
                                this.shouldActive = true;
                                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Successfully Verus Disabled!", Notification.Type.SUCCESS));
                            }
                            v32 = this.packetQueue.poll();
                            Intrinsics.checkExpressionValueIsNotNull(v32, "packetQueue.poll()");
                            PacketUtils.sendPacketNoEvent((Packet)v32);
                        }
                        Disabler.debug$default(this, "c0f, " + this.packetQueue.size(), false, 2, null);
                    }
                    if (packet instanceof C0BPacketEntityAction) {
                        event.cancelEvent();
                        Disabler.debug$default(this, "ignored packet action", false, 2, null);
                    }
                    if (packet instanceof C03PacketPlayer) {
                        if (((Boolean)this.verusFlagValue.get()).booleanValue() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % ((Number)this.verusFlagDelayValue.get()).intValue() == 0) {
                            Disabler.debug$default(this, "modified c03", false, 2, null);
                            ((C03PacketPlayer)packet).field_149477_b -= 11.015625;
                            ((C03PacketPlayer)packet).field_149474_g = false;
                            ((C03PacketPlayer)packet).func_149469_a(false);
                        }
                        if (((Boolean)this.verusValidPos.get()).booleanValue() && packet instanceof C03PacketPlayer && ((C03PacketPlayer)packet).field_149477_b % 0.015625 == 0.0) {
                            ((C03PacketPlayer)packet).field_149474_g = true;
                            Disabler.debug$default(this, "true asf", false, 2, null);
                        }
                    }
                    if (packet instanceof S08PacketPlayerPosLook && ((Boolean)this.verusSlientFlagApplyValue.get()).booleanValue()) {
                        x = ((S08PacketPlayerPosLook)packet).field_148940_a - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                        y = ((S08PacketPlayerPosLook)packet).field_148938_b - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                        z = ((S08PacketPlayerPosLook)packet).field_148939_c - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
                        var12_50 = x * x + y * y + z * z;
                        var14_60 = false;
                        diff = Math.sqrt(var12_50);
                        if (diff <= (double)8) {
                            event.cancelEvent();
                            Disabler.debug$default(this, "flag silent accept", false, 2, null);
                            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook)packet).field_148940_a, ((S08PacketPlayerPosLook)packet).field_148938_b, ((S08PacketPlayerPosLook)packet).field_148939_c, ((S08PacketPlayerPosLook)packet).func_148931_f(), ((S08PacketPlayerPosLook)packet).func_148930_g(), false));
                        }
                    }
                }
                if (((Boolean)this.latestblocksmc.get()).booleanValue()) {
                    if (!this.shouldRun()) {
                        this.msTimer.reset();
                        this.packetQueue.clear();
                        return;
                    }
                    if (packet instanceof C0FPacketConfirmTransaction && !this.isInventory(((C0FPacketConfirmTransaction)packet).field_149534_b)) {
                        this.packetQueue.add((C0FPacketConfirmTransaction)packet);
                        event.cancelEvent();
                        if (this.packetQueue.size() > ((Number)this.blocksmcBufferSizeValue.get()).intValue()) {
                            if (!this.shouldActive) {
                                this.shouldActive = true;
                                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Successfully Blocksmc Combat Disabled!", Notification.Type.SUCCESS));
                            }
                            v33 = this.packetQueue.poll();
                            Intrinsics.checkExpressionValueIsNotNull(v33, "packetQueue.poll()");
                            PacketUtils.sendPacketNoEvent((Packet)v33);
                        }
                        Disabler.debug$default(this, "[Disabler] c0f, " + this.packetQueue.size(), false, 2, null);
                    }
                    if (packet instanceof C0BPacketEntityAction) {
                        event.cancelEvent();
                        Disabler.debug$default(this, "[Disabler] ignored packet action", false, 2, null);
                    }
                    if (packet instanceof C03PacketPlayer) {
                        if (((Boolean)this.blocksmcFlagValue.get()).booleanValue() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % ((Number)this.blocksmcFlagDelayValue.get()).intValue() == 0) {
                            Disabler.debug$default(this, "[Disabler] modified c03", false, 2, null);
                            ((C03PacketPlayer)packet).field_149477_b -= 11.015625;
                            ((C03PacketPlayer)packet).field_149474_g = false;
                            ((C03PacketPlayer)packet).func_149469_a(false);
                        }
                        if (((Boolean)this.blocksmcValidPos.get()).booleanValue() && packet instanceof C03PacketPlayer && ((C03PacketPlayer)packet).field_149477_b % 0.015625 == 0.0) {
                            ((C03PacketPlayer)packet).field_149474_g = true;
                            Disabler.debug$default(this, "[Disabler] true asf", false, 2, null);
                        }
                    }
                    if (packet instanceof S08PacketPlayerPosLook && ((Boolean)this.blocksmcSlientFlagApplyValue.get()).booleanValue()) {
                        x = ((S08PacketPlayerPosLook)packet).field_148940_a - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                        y = ((S08PacketPlayerPosLook)packet).field_148938_b - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                        z = ((S08PacketPlayerPosLook)packet).field_148939_c - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
                        var12_51 = x * x + y * y + z * z;
                        var14_60 = false;
                        diff = Math.sqrt(var12_51);
                        if (diff <= (double)8) {
                            event.cancelEvent();
                            Disabler.debug$default(this, "flag silent accept", false, 2, null);
                            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook)packet).field_148940_a, ((S08PacketPlayerPosLook)packet).field_148938_b, ((S08PacketPlayerPosLook)packet).field_148939_c, ((S08PacketPlayerPosLook)packet).func_148931_f(), ((S08PacketPlayerPosLook)packet).func_148930_g(), false));
                        }
                    }
                }
                if (((Boolean)this.silentaccept.get()).booleanValue()) {
                    if (packet instanceof C03PacketPlayer) {
                        y = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u / 0.015625;
                        z = false;
                        yPos = Math.rint(y) * 0.015625;
                        Disabler.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t, yPos, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                        if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 45 == 0) {
                            Disabler.debug$default(this, "flag", false, 2, null);
                            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 11.725, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                        }
                    }
                    if (packet instanceof S08PacketPlayerPosLook) {
                        if (Disabler.access$getMc$p$s1046033730().field_71439_g == null || Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa <= 0) {
                            return;
                        }
                        x = ((S08PacketPlayerPosLook)packet).func_148932_c() - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                        y = ((S08PacketPlayerPosLook)packet).func_148928_d() - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                        z = ((S08PacketPlayerPosLook)packet).func_148933_e() - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
                        var12_52 = x * x + y * y + z * z;
                        var14_60 = false;
                        diff = Math.sqrt(var12_52);
                        if (diff <= (double)8) {
                            event.cancelEvent();
                            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook)packet).func_148932_c(), ((S08PacketPlayerPosLook)packet).func_148928_d(), ((S08PacketPlayerPosLook)packet).func_148933_e(), ((S08PacketPlayerPosLook)packet).func_148931_f(), ((S08PacketPlayerPosLook)packet).func_148930_g(), true));
                            Disabler.debug$default(this, "silent s08 accept", false, 2, null);
                        }
                    }
                    if (packet instanceof C0FPacketConfirmTransaction && !this.isInventory(((C0FPacketConfirmTransaction)packet).field_149534_b)) {
                        x = 4;
                        var5_57 = false;
                        y = 0;
                        y = 0;
                        var7_61 = x;
                        while (y < var7_61) {
                            it = y++;
                            $i$a$-repeat-Disabler$onPacket$1 = false;
                            this.packetQueue.add((C0FPacketConfirmTransaction)packet);
                        }
                        event.cancelEvent();
                        Disabler.debug$default(this, "c0f dupe: 4x", false, 2, null);
                    }
                }
                if (((Boolean)this.buzzsemi.get()).booleanValue() && packet instanceof C0FPacketConfirmTransaction) {
                    x = 5;
                    var5_58 = false;
                    y = 0;
                    y = 0;
                    var7_61 = x;
                    while (y < var7_61) {
                        it = y++;
                        $i$a$-repeat-Disabler$onPacket$2 = false;
                        this.packetQueue.add((C0FPacketConfirmTransaction)packet);
                    }
                    event.cancelEvent();
                    Disabler.debug$default(this, "c0f dupe: 5x", false, 2, null);
                }
                if (((Boolean)this.veruscombat.get()).booleanValue()) {
                    if (packet instanceof C0FPacketConfirmTransaction) {
                        event.cancelEvent();
                        Disabler.debug$default(this, "cancel c0f", false, 2, null);
                    } else if (packet instanceof C0BPacketEntityAction) {
                        event.cancelEvent();
                        Disabler.debug$default(this, "cancel c0b", false, 2, null);
                    }
                }
                if (((Boolean)this.veruscombat2.get()).booleanValue() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > 20 && !Disabler.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75101_c && p instanceof C0FPacketConfirmTransaction) {
                    event.cancelEvent();
                }
                if (((Boolean)this.verussemi.get()).booleanValue()) {
                    if (!this.shouldRun()) {
                        this.queueBus.clear();
                        return;
                    }
                    if (packet instanceof C0BPacketEntityAction) {
                        event.cancelEvent();
                        Disabler.debug$default(this, "cancel action", false, 2, null);
                    }
                    if (packet instanceof S08PacketPlayerPosLook && Disabler.access$getMc$p$s1046033730().field_71439_g.func_70011_f(((S08PacketPlayerPosLook)packet).field_148940_a, ((S08PacketPlayerPosLook)packet).field_148938_b, ((S08PacketPlayerPosLook)packet).field_148939_c) < (double)8) {
                        PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook)packet).field_148940_a, ((S08PacketPlayerPosLook)packet).field_148938_b, ((S08PacketPlayerPosLook)packet).field_148939_c, ((S08PacketPlayerPosLook)packet).field_148936_d, ((S08PacketPlayerPosLook)packet).field_148937_e, false));
                        event.cancelEvent();
                        Disabler.debug$default(this, "silent flag", false, 2, null);
                    }
                    if (packet instanceof C00PacketKeepAlive || packet instanceof C0FPacketConfirmTransaction && !this.isInventory(((C0FPacketConfirmTransaction)packet).field_149534_b)) {
                        this.queueBus.add(packet);
                        event.cancelEvent();
                        Disabler.debug$default(this, "c0f or c00, " + this.queueBus.size(), false, 2, null);
                        if (this.queueBus.size() > 300) {
                            v34 = this.queueBus.poll();
                            Intrinsics.checkExpressionValueIsNotNull(v34, "queueBus.poll()");
                            PacketUtils.sendPacketNoEvent(v34);
                            Disabler.debug$default(this, "poll", false, 2, null);
                        }
                    }
                    if (packet instanceof C03PacketPlayer) {
                        if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 20 == 0) {
                            PacketUtils.sendPacketNoEvent((Packet)new C0CPacketInput(0.98f, 0.98f, false, false));
                            Disabler.debug$default(this, "c18 and c0c", false, 2, null);
                        }
                        if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 45 == 0) {
                            ((C03PacketPlayer)packet).field_149477_b = -0.015625;
                            ((C03PacketPlayer)packet).field_149474_g = false;
                            ((C03PacketPlayer)packet).func_149469_a(false);
                            Disabler.debug$default(this, "flag packet", false, 2, null);
                        }
                    }
                }
                if (((Boolean)this.flag.get()).booleanValue()) {
                    if (packet instanceof C03PacketPlayer && StringsKt.equals((String)this.flagMode.get(), "edit", true) && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > 0 && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % ((Number)this.flagTick.get()).intValue() == 0) {
                        ((C03PacketPlayer)packet).func_149469_a(false);
                        ((C03PacketPlayer)packet).field_149474_g = false;
                        ((C03PacketPlayer)packet).field_149477_b = -0.08;
                        Disabler.debug$default(this, "flagged", false, 2, null);
                    }
                    if (packet instanceof S08PacketPlayerPosLook && ((Boolean)this.flagSilent.get()).booleanValue()) {
                        if (Disabler.access$getMc$p$s1046033730().field_71439_g == null || Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa <= 0) {
                            return;
                        }
                        x = ((S08PacketPlayerPosLook)packet).func_148932_c() - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                        y = ((S08PacketPlayerPosLook)packet).func_148928_d() - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                        z = ((S08PacketPlayerPosLook)packet).func_148933_e() - Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
                        var12_53 = x * x + y * y + z * z;
                        var14_60 = false;
                        diff = Math.sqrt(var12_53);
                        if (diff <= (double)8) {
                            event.cancelEvent();
                            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook)packet).func_148932_c(), ((S08PacketPlayerPosLook)packet).func_148928_d(), ((S08PacketPlayerPosLook)packet).func_148933_e(), ((S08PacketPlayerPosLook)packet).func_148931_f(), ((S08PacketPlayerPosLook)packet).func_148930_g(), true));
                            Disabler.debug$default(this, "silent s08 accept", false, 2, null);
                        }
                    }
                }
                if (((Boolean)this.pingspoof.get()).booleanValue()) {
                    if (packet instanceof C0FPacketConfirmTransaction && !this.isInventory(((C0FPacketConfirmTransaction)packet).field_149534_b)) {
                        this.queueBus.add(packet);
                        event.cancelEvent();
                        Disabler.debug$default(this, "c0f added, action id " + ((C0FPacketConfirmTransaction)packet).field_149534_b + ", target id " + ((C0FPacketConfirmTransaction)packet).field_149536_a, false, 2, null);
                    }
                    if (packet instanceof C00PacketKeepAlive) {
                        this.queueBus.add(packet);
                        event.cancelEvent();
                        Disabler.debug$default(this, "c00 added, key " + ((C00PacketKeepAlive)packet).field_149461_a, false, 2, null);
                    }
                }
                if (!((Boolean)this.matrix.get()).booleanValue()) break block201;
                if (((Boolean)this.matrixNoCheck.get()).booleanValue()) break block202;
                v35 = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                if (v35 == null) {
                    Intrinsics.throwNpe();
                }
                if (v35.getState()) break block202;
                v36 = LiquidBounce.INSTANCE.getModuleManager().getModule(Fly.class);
                if (v36 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v36.getState()) break block201;
            }
            if (packet instanceof C03PacketPlayer) {
                if (((Boolean)this.matrixNoMovePacket.get()).booleanValue() && !((C03PacketPlayer)packet).func_149466_j()) {
                    event.cancelEvent();
                    Disabler.debug$default(this, "no move, cancelled", false, 2, null);
                    return;
                }
                if (((Boolean)this.matrixMoveFix.get()).booleanValue()) {
                    ((C03PacketPlayer)packet).field_149474_g = true;
                    if (!((C03PacketPlayer)packet).field_149481_i) {
                        ((C03PacketPlayer)packet).field_149481_i = true;
                        ((C03PacketPlayer)packet).field_149476_e = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70177_z;
                        ((C03PacketPlayer)packet).field_149473_f = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70125_A;
                    }
                }
            }
        }
        if (((Boolean)this.oldwatchdog1.get()).booleanValue()) {
            v37 = Disabler.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(v37, "mc");
            if (v37.func_71356_B()) {
                return;
            }
            if (((Boolean)this.autoAlert.get()).booleanValue() && packet instanceof S02PacketChat) {
                v38 = ((S02PacketChat)packet).func_148915_c();
                Intrinsics.checkExpressionValueIsNotNull(v38, "packet.chatComponent");
                v39 = v38.func_150260_c();
                Intrinsics.checkExpressionValueIsNotNull(v39, "packet.chatComponent.unformattedText");
                if (StringsKt.contains((CharSequence)v39, "Cages opened!", true)) {
                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Speed is bannable until this notification disappears.", Notification.Type.WARNING, 20000L, 0, 0, 24, null));
                }
            }
            if (((Boolean)this.testFeature.get()).booleanValue() && !ServerUtils.isHypixelLobby()) {
                if (!(!(packet instanceof C0FPacketConfirmTransaction) || ((Boolean)this.checkValid.get()).booleanValue() && this.isInventory(((C0FPacketConfirmTransaction)packet).field_149534_b))) {
                    event.cancelEvent();
                    this.packetQueue.add((C0FPacketConfirmTransaction)packet);
                    Disabler.debug$default(this, "c0f, " + ((C0FPacketConfirmTransaction)packet).field_149534_b + " ID, " + ((C0FPacketConfirmTransaction)packet).field_149536_a + " wID", false, 2, null);
                    if (!this.shouldActive) {
                        this.shouldActive = true;
                        Disabler.debug$default(this, "activated", false, 2, null);
                        var4_23 = (String)this.waitingDisplayMode.get();
                        v40 = Locale.getDefault();
                        Intrinsics.checkExpressionValueIsNotNull(v40, "Locale.getDefault()");
                        var5_59 = v40;
                        var6_34 = false;
                        v41 = var4_23;
                        if (v41 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        v42 = v41.toLowerCase(var5_59);
                        Intrinsics.checkExpressionValueIsNotNull(v42, "(this as java.lang.String).toLowerCase(locale)");
                        var4_23 = v42;
                        tmp = -1;
                        switch (var4_23.hashCode()) {
                            case 595233003: {
                                if (!var4_23.equals("notification")) break;
                                tmp = 1;
                                break;
                            }
                            case 3052376: {
                                if (!var4_23.equals("chat")) break;
                                tmp = 2;
                                break;
                            }
                        }
                        switch (tmp) {
                            case 1: {
                                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Activated Disabler.", Notification.Type.SUCCESS, 2000L, 0, 0, 24, null));
                                break;
                            }
                            case 2: {
                                this.debug("Activated Disabler.", true);
                            }
                        }
                    }
                }
                if (packet instanceof C00PacketKeepAlive) {
                    event.cancelEvent();
                    this.anotherQueue.add((C00PacketKeepAlive)packet);
                    this.wdTimer.reset();
                    Disabler.debug$default(this, "c00, " + ((C00PacketKeepAlive)packet).field_149461_a, false, 2, null);
                }
                if ((packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation) && !this.shouldActive) {
                    event.cancelEvent();
                }
                if (packet instanceof S08PacketPlayerPosLook && !this.shouldActive) {
                    if (this.alrSendY) {
                        event.cancelEvent();
                        Disabler.debug$default(this, "no s08", false, 2, null);
                    } else {
                        this.alrSendY = true;
                        Disabler.debug$default(this, "first s08, ignore", false, 2, null);
                    }
                }
            }
            if (((Boolean)this.noC03s.get()).booleanValue() && packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(packet instanceof C03PacketPlayer.C05PacketPlayerLook) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                event.cancelEvent();
            }
        }
        if (((Boolean)this.hycraft.get()).booleanValue() && packet instanceof S3EPacketTeams) {
            Disabler.debug$default(this, "anticrash", false, 2, null);
            event.cancelEvent();
        }
        if (((Boolean)this.vanilladesync.get()).booleanValue() && packet instanceof S08PacketPlayerPosLook) {
            v43 = Disabler.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(v43, "mc");
            if (!v43.func_147114_u().field_147309_h) {
                Disabler.debug$default(this, "not loaded terrain yet", false, 2, null);
                return;
            }
            event.cancelEvent();
            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((S08PacketPlayerPosLook)packet).field_148940_a, ((S08PacketPlayerPosLook)packet).field_148938_b, ((S08PacketPlayerPosLook)packet).field_148939_c, false));
            Disabler.access$getMc$p$s1046033730().field_71439_g.func_70107_b(((S08PacketPlayerPosLook)packet).field_148940_a, ((S08PacketPlayerPosLook)packet).field_148938_b, ((S08PacketPlayerPosLook)packet).field_148939_c);
            Disabler.debug$default(this, "silent setback", false, 2, null);
        }
    }

    private final void updateLagTime() {
        this.decTimer.reset();
        this.lagTimer.reset();
        this.currentDelay = (Boolean)this.dynamicValue.get() != false ? RandomUtils.nextInt(((Number)this.decDelayMinValue.get()).intValue(), ((Number)this.decDelayMaxValue.get()).intValue()) : 5000;
        this.currentDec = (Boolean)this.compDecValue.get() != false ? ((Number)this.statDecValue.get()).intValue() : -1;
        this.currentBuffer = ((Number)this.minBuffValue.get()).intValue();
    }

    private final void blink() {
        try {
            this.disableLogger = true;
            while (!this.packets.isEmpty()) {
                Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                NetHandlerPlayClient netHandlerPlayClient = minecraft.func_147114_u();
                Intrinsics.checkExpressionValueIsNotNull(netHandlerPlayClient, "mc.netHandler");
                netHandlerPlayClient.func_147298_b().func_179290_a(this.packets.take());
            }
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
    }

    private final Entity getNearBoat() {
        List entities = Disabler.access$getMc$p$s1046033730().field_71441_e.field_72996_f;
        for (Entity entity_ : entities) {
            if (!(entity_ instanceof EntityBoat) || !(Intrinsics.areEqual(entity_, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70154_o) ^ true)) continue;
            return entity_;
        }
        return null;
    }

    public final void flush(boolean check) {
        if (StringsKt.equals(check ? (String)this.psfSendMode.get() : (String)this.psfStartSendMode.get(), "all", true)) {
            while (this.queueBus.size() > 0) {
                Packet<INetHandlerPlayServer> packet = this.queueBus.poll();
                Intrinsics.checkExpressionValueIsNotNull(packet, "queueBus.poll()");
                PacketUtils.sendPacketNoEvent(packet);
            }
        } else {
            Packet<INetHandlerPlayServer> packet = this.queueBus.poll();
            Intrinsics.checkExpressionValueIsNotNull(packet, "queueBus.poll()");
            PacketUtils.sendPacketNoEvent(packet);
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget(priority=2)
    public final void onMotion(@NotNull MotionEvent event) {
        block34: {
            block35: {
                float cYaw;
                Intrinsics.checkParameterIsNotNull(event, "event");
                Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                Module speed = module;
                Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(Fly.class);
                if (module2 == null) {
                    Intrinsics.throwNpe();
                }
                Module fly = module2;
                if (event.getEventState() == EventState.PRE) {
                    this.shouldModifyRotation = false;
                }
                if (((Boolean)this.universocraft.get()).booleanValue()) {
                    Module module3 = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                    Boolean bl = module3 != null ? Boolean.valueOf(module3.getState()) : null;
                    if (bl == null) {
                        Intrinsics.throwNpe();
                    }
                    if (bl.booleanValue()) {
                        RotationUtils.setTargetRotation(new Rotation(0.0f, 30.0f));
                    }
                }
                if (((Boolean)this.payload.get()).booleanValue() && event.getEventState() != EventState.POST) {
                    PacketUtils.sendPacketNoEvent((Packet)new C17PacketCustomPayload("40413eb1", new PacketBuffer(Unpooled.wrappedBuffer((byte[])new byte[]{8, 52, 48, 52, 49, 51, 101, 98, 49}))));
                    Disabler.debug$default(this, "funny", false, 2, null);
                }
                if (((Boolean)this.oldwatchdog2.get()).booleanValue() && ((Boolean)this.rotationChanger.get()).booleanValue()) {
                    Module module4 = LiquidBounce.INSTANCE.getModuleManager().get(Scaffold.class);
                    if (module4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!module4.getState()) {
                        Module module5 = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
                        if (module5 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!module5.getState()) {
                            cYaw = MovementUtilsFix.INSTANCE.getMovingYaw();
                            RotationUtils.setTargetRotation(new Rotation(cYaw, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70125_A), 10);
                        }
                    }
                }
                if (((Boolean)this.oldwatchdog1.get()).booleanValue() && event.getEventState() == EventState.PRE) {
                    if ((speed.getState() || fly.getState()) && ((Boolean)this.rotModify.get()).booleanValue()) {
                        this.shouldModifyRotation = true;
                        if (MovementUtils.isMoving()) {
                            this.lastYaw = cYaw = MovementUtils.getPredictionYaw(event.getX(), event.getZ()) - 90.0f;
                            event.setYaw(cYaw);
                            if (((Boolean)this.tifality90.get()).booleanValue()) {
                                event.setPitch(90.0f);
                            }
                            RotationUtils.setTargetRotation(new Rotation(cYaw, (Boolean)this.tifality90.get() != false ? 90.0f : event.getPitch()));
                        } else if (((Boolean)this.noMoveKeepRot.get()).booleanValue()) {
                            event.setYaw(this.lastYaw);
                            if (((Boolean)this.tifality90.get()).booleanValue()) {
                                event.setPitch(90.0f);
                            }
                            RotationUtils.setTargetRotation(new Rotation(this.lastYaw, (Boolean)this.tifality90.get() != false ? 90.0f : event.getPitch()));
                        }
                    }
                    Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    if (minecraft.func_71356_B()) {
                        return;
                    }
                    if (((Boolean)this.testFeature.get()).booleanValue() && !ServerUtils.isHypixelLobby() && this.shouldActive && this.wdTimer.hasTimePassed(((Number)this.testDelay.get()).intValue())) {
                        while (!this.anotherQueue.isEmpty()) {
                            C00PacketKeepAlive c00PacketKeepAlive = this.anotherQueue.poll();
                            Intrinsics.checkExpressionValueIsNotNull(c00PacketKeepAlive, "anotherQueue.poll()");
                            PacketUtils.sendPacketNoEvent((Packet)c00PacketKeepAlive);
                            Disabler.debug$default(this, "c00, " + this.anotherQueue.size(), false, 2, null);
                        }
                        while (!this.packetQueue.isEmpty()) {
                            C0FPacketConfirmTransaction c0FPacketConfirmTransaction = this.packetQueue.poll();
                            Intrinsics.checkExpressionValueIsNotNull(c0FPacketConfirmTransaction, "packetQueue.poll()");
                            PacketUtils.sendPacketNoEvent((Packet)c0FPacketConfirmTransaction);
                            Disabler.debug$default(this, "c0f, " + this.packetQueue.size(), false, 2, null);
                        }
                    }
                }
                if (event.getEventState() != EventState.POST || ((Boolean)this.matrixMoveOnly.get()).booleanValue() && !this.isMoving() || !((Boolean)this.matrix.get()).booleanValue()) break block34;
                if (((Boolean)this.matrixNoCheck.get()).booleanValue()) break block35;
                Module module6 = LiquidBounce.INSTANCE.getModuleManager().getModule(Fly.class);
                if (module6 == null) {
                    Intrinsics.throwNpe();
                }
                if (module6.getState()) break block35;
                Module module7 = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                if (module7 == null) {
                    Intrinsics.throwNpe();
                }
                if (!module7.getState()) break block34;
            }
            boolean changed = false;
            if (((Boolean)this.matrixHotbarChange.get()).booleanValue()) {
                int n = 0;
                int n2 = 8;
                while (n <= n2) {
                    void i;
                    if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[i] == null && i != Disabler.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c) {
                        PacketUtils.sendPacketNoEvent((Packet)new C09PacketHeldItemChange((int)i));
                        changed = true;
                        Disabler.debug$default(this, "found empty slot " + (int)i + ", switching", false, 2, null);
                        break;
                    }
                    ++i;
                }
            }
            double d = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
            double d2 = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
            double d3 = Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
            Rotation rotation = RotationUtils.serverRotation;
            Float f = rotation != null ? Float.valueOf(rotation.getYaw()) : null;
            if (f == null) {
                Intrinsics.throwNpe();
            }
            float f2 = f.floatValue();
            Rotation rotation2 = RotationUtils.serverRotation;
            Float f3 = rotation2 != null ? Float.valueOf(rotation2.getPitch()) : null;
            if (f3 == null) {
                Intrinsics.throwNpe();
            }
            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(d, d2, d3, f2, f3.floatValue(), Disabler.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
            Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, null, 0.0f, 0.0f, 0.0f));
            Disabler.debug$default(this, "sent placement", false, 2, null);
            if (changed) {
                PacketUtils.sendPacketNoEvent((Packet)new C09PacketHeldItemChange(Disabler.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                Disabler.debug$default(this, "switched back", false, 2, null);
            }
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Module flyMod;
        Intrinsics.checkParameterIsNotNull(event, "event");
        Module flight = LiquidBounce.INSTANCE.getModuleManager().getModule(Fly.class);
        if (((Boolean)this.boat.get()).booleanValue() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70154_o != null) {
            Disabler.access$getMc$p$s1046033730().field_71439_g.field_70125_A = (float)90.0;
            Disabler.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
            Disabler.access$getMc$p$s1046033730().field_71442_b.func_78764_a((EntityPlayer)Disabler.access$getMc$p$s1046033730().field_71439_g, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70154_o);
            Disabler.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
            Disabler.access$getMc$p$s1046033730().field_71442_b.func_78764_a((EntityPlayer)Disabler.access$getMc$p$s1046033730().field_71439_g, this.getNearBoat());
            this.canModify = true;
            Disabler.debug$default(this, "Destroy Boat", false, 2, null);
        }
        if (((Boolean)this.vulcancombat.get()).booleanValue()) {
            if (this.runReset) {
                this.runReset = false;
                PacketUtils.sendPacketNoEvent((Packet)new C0BPacketEntityAction((Entity)Disabler.access$getMc$p$s1046033730().field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            if (this.lagTimer.hasTimePassed(this.currentDelay) && BlinkUtils.INSTANCE.bufferSize("C0FPacketConfirmTransaction") > this.currentBuffer) {
                this.updateLagTime();
                BlinkUtils.releasePacket$default(BlinkUtils.INSTANCE, "C0FPacketConfirmTransaction", false, 0, this.currentBuffer, 6, null);
                Disabler.debug$default(this, "C0F-PingTickCounter RELEASE", false, 2, null);
            }
            if (this.decTimer.hasTimePassed(this.currentDec) && this.currentDec > 0) {
                BlinkUtils.releasePacket$default(BlinkUtils.INSTANCE, "C0FPacketConfirmTransaction", false, 1, 0, 10, null);
                Disabler.debug$default(this, "C0F-PingTickCounter DECREASE", false, 2, null);
                this.decTimer.reset();
            }
        }
        if (((Boolean)this.oldwatchdog2.get()).booleanValue()) {
            if (((Boolean)this.timerA.get()).booleanValue() && this.timerCancelDelay.hasTimePassed(5000L)) {
                this.timerShouldCancel = true;
                this.timerCancelTimer.reset();
                this.timerCancelDelay.reset();
            }
            if (((Boolean)this.timerB.get()).booleanValue() && this.timerCancelDelay.hasTimePassed(2000L)) {
                this.timerShouldCancel = true;
                this.timerCancelTimer.reset();
                this.timerCancelDelay.reset();
            }
            if (((Boolean)this.c00Disabler.get()).booleanValue() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70122_E && ClassUtils.INSTANCE.isBlockUnder() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70143_R > (float)10) {
                Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C00PacketKeepAlive(RandomUtils.nextInt(0, 1000)));
                Disabler.debug$default(this, "Hypixel Disabler C00", false, 2, null);
            }
            if (((Boolean)this.c0BDisabler.get()).booleanValue() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 180 == 90) {
                if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70143_R > (float)10) {
                    Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C00PacketKeepAlive(RandomUtils.nextInt(0, 1000)));
                    Disabler.debug$default(this, "Hypixel Disabler C0B", false, 2, null);
                    Disabler.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.8f;
                } else if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70143_R < (float)10 && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u == (double)Disabler.access$getMc$p$s1046033730().field_71439_g.field_70143_R) {
                    Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(false));
                    if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                        Disabler.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.4f;
                    }
                    if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70143_R == 0.0f) {
                        Minecraft minecraft2 = Disabler.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                        minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    }
                    Disabler.debug$default(this, "Hypixel Disabler C03", false, 2, null);
                }
            }
        }
        if (((Boolean)this.intave.get()).booleanValue()) {
            if (Disabler.access$getMc$p$s1046033730().field_71439_g == null) {
                return;
            }
            if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa < 3) {
                this.linkedQueue.clear();
                this.msTimer.reset();
            }
            if (!this.linkedQueue.isEmpty()) {
                this.linkedQueue.poll();
            }
            this.msTimer.reset();
        }
        if (((Boolean)this.heirteirac.get()).booleanValue()) {
            if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa < 120) {
                return;
            }
            Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
            Minecraft minecraft3 = Disabler.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
            minecraft3.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70163_u + (double)490, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
        }
        if (((Boolean)this.spartancombat.get()).booleanValue() && this.msTimer.hasTimePassed(3000L) && this.keepAlives.size() > 0 && this.transactions.size() > 0) {
            C00PacketKeepAlive c00PacketKeepAlive = this.keepAlives.get(this.keepAlives.size() - 1);
            Intrinsics.checkExpressionValueIsNotNull(c00PacketKeepAlive, "keepAlives[keepAlives.size - 1]");
            PacketUtils.sendPacketNoEvent((Packet)c00PacketKeepAlive);
            C0FPacketConfirmTransaction c0FPacketConfirmTransaction = this.transactions.get(this.transactions.size() - 1);
            Intrinsics.checkExpressionValueIsNotNull(c0FPacketConfirmTransaction, "transactions[transactions.size - 1]");
            PacketUtils.sendPacketNoEvent((Packet)c0FPacketConfirmTransaction);
            Disabler.debug$default(this, "c00 no." + (this.keepAlives.size() - 1) + " sent.", false, 2, null);
            Disabler.debug$default(this, "c0f no." + (this.transactions.size() - 1) + " sent.", false, 2, null);
            this.keepAlives.clear();
            this.transactions.clear();
            this.msTimer.reset();
        }
        if (((Boolean)this.tubnet.get()).booleanValue() && this.pulseTimer.hasTimePassed(60)) {
            Module module = flight;
            if (module == null) {
                Intrinsics.throwNpe();
            }
            if (module.getState()) {
                this.blink();
                this.pulseTimer.reset();
                Disabler.debug$default(this, "funny packet", false, 2, null);
            }
        }
        if (((Boolean)this.blockdrop.get()).booleanValue() && this.msTimer.hasTimePassed(3000L) && this.keepAlives.size() > 0 && this.transactions.size() > 0) {
            C00PacketKeepAlive c00PacketKeepAlive = this.keepAlives.get(this.keepAlives.size() - 1);
            Intrinsics.checkExpressionValueIsNotNull(c00PacketKeepAlive, "keepAlives[keepAlives.size - 1]");
            PacketUtils.sendPacketNoEvent((Packet)c00PacketKeepAlive);
            C0FPacketConfirmTransaction c0FPacketConfirmTransaction = this.transactions.get(this.transactions.size() - 1);
            Intrinsics.checkExpressionValueIsNotNull(c0FPacketConfirmTransaction, "transactions[transactions.size - 1]");
            PacketUtils.sendPacketNoEvent((Packet)c0FPacketConfirmTransaction);
            Disabler.debug$default(this, "c00 no." + (this.keepAlives.size() - 1) + " sent.", false, 2, null);
            Disabler.debug$default(this, "c0f no." + (this.transactions.size() - 1) + " sent.", false, 2, null);
            this.keepAlives.clear();
            this.transactions.clear();
            this.msTimer.reset();
        }
        if (((Boolean)this.silentaccept.get()).booleanValue() && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 180 == 0) {
            while (this.packetQueue.size() > 22) {
                C0FPacketConfirmTransaction c0FPacketConfirmTransaction = this.packetQueue.poll();
                Intrinsics.checkExpressionValueIsNotNull(c0FPacketConfirmTransaction, "packetQueue.poll()");
                PacketUtils.sendPacketNoEvent((Packet)c0FPacketConfirmTransaction);
            }
            Disabler.debug$default(this, "pushed queue until size < 22.", false, 2, null);
        }
        if (((Boolean)this.latestverus.get()).booleanValue()) {
            if (((Boolean)this.verusAntiFlyCheck.get()).booleanValue() && !this.shouldActive) {
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if ((flyMod = module).getState()) {
                    flyMod.setState(false);
                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification("You can't fly before successful activation.", Notification.Type.WARNING));
                    Disabler.debug$default(this, "no fly allowed", false, 2, null);
                }
            }
            if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 15 == 0 && this.shouldRun() && ((Boolean)this.verusFakeInput.get()).booleanValue()) {
                Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C0CPacketInput(RangesKt.coerceAtMost(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70702_br, 0.98f), RangesKt.coerceAtMost(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70701_bs, 0.98f), Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78901_c, Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78899_d));
                Disabler.debug$default(this, "c0c", false, 2, null);
            }
        }
        if (((Boolean)this.latestblocksmc.get()).booleanValue()) {
            if (((Boolean)this.blocksmcAntiFlyCheck.get()).booleanValue() && !this.shouldActive) {
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if ((flyMod = module).getState()) {
                    flyMod.setState(false);
                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification("You can't fly before successful activation.", Notification.Type.WARNING));
                    Disabler.debug$default(this, "no fly allowed", false, 2, null);
                }
            }
            if (Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 15 == 0 && this.shouldRun() && ((Boolean)this.blocksmcFakeInput.get()).booleanValue()) {
                Minecraft minecraft = Disabler.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C0CPacketInput(RangesKt.coerceAtMost(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70702_br, 0.98f), RangesKt.coerceAtMost(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70701_bs, 0.98f), Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78901_c, Disabler.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78899_d));
                Disabler.debug$default(this, "c0c", false, 2, null);
            }
        }
        if (((Boolean)this.pingspoof.get()).booleanValue()) {
            if (this.msTimer.hasTimePassed(((Number)this.psfWorldDelay.get()).intValue()) && !this.shouldActive) {
                this.shouldActive = true;
                this.sendDelay = RandomUtils.nextInt(((Number)this.minpsf.get()).intValue(), ((Number)this.maxpsf.get()).intValue());
                if (this.queueBus.size() > 0) {
                    this.flush(false);
                }
                this.msTimer.reset();
                Disabler.debug$default(this, "activated. expected next delay: " + this.sendDelay + "ms", false, 2, null);
            }
            if (this.shouldActive && this.msTimer.hasTimePassed(this.sendDelay) && !this.queueBus.isEmpty()) {
                this.flush(true);
                this.sendDelay = RandomUtils.nextInt(((Number)this.minpsf.get()).intValue(), ((Number)this.maxpsf.get()).intValue());
                this.msTimer.reset();
                Disabler.debug$default(this, "expected next delay: " + this.sendDelay + "ms", false, 2, null);
            }
        }
        if (((Boolean)this.flag.get()).booleanValue() && StringsKt.equals((String)this.flagMode.get(), "packet", true) && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > 0 && Disabler.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % ((Number)this.flagTick.get()).intValue() == 0) {
            PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.access$getMc$p$s1046033730().field_71439_g.field_70165_t, -0.08, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70161_v, Disabler.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
            Disabler.debug$default(this, "flagged", false, 2, null);
        }
    }

    public Disabler() {
        Disabler disabler = this;
        boolean bl = false;
        Cloneable cloneable = new ArrayList();
        disabler.keepAlives = cloneable;
        disabler = this;
        bl = false;
        cloneable = new ArrayList();
        disabler.transactions = cloneable;
        this.packetQueue = new LinkedList();
        this.anotherQueue = new LinkedList();
        this.playerQueue = new LinkedList();
        disabler = this;
        bl = false;
        cloneable = new HashMap();
        disabler.packetBus = cloneable;
        this.queueBus = new LinkedList();
        this.packetBuffer = new LinkedList();
        this.posLookInstance = new PosLookInstance();
        this.msTimer = new MSTimer();
        this.wdTimer = new MSTimer();
        this.benTimer = new MSTimer();
        this.pulseTimer = new MSTimer();
        this.currentDelay = 5000;
        this.currentBuffer = 4;
        this.currentDec = -1;
        this.lagTimer = new MSTimer();
        this.decTimer = new MSTimer();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ IntegerValue access$getMaxpsf$p(Disabler $this) {
        return $this.maxpsf;
    }

    public static final /* synthetic */ BoolValue access$getPingspoof$p(Disabler $this) {
        return $this.pingspoof;
    }

    public static final /* synthetic */ IntegerValue access$getMinpsf$p(Disabler $this) {
        return $this.minpsf;
    }

    public static final /* synthetic */ BoolValue access$getFlag$p(Disabler $this) {
        return $this.flag;
    }

    public static final /* synthetic */ BoolValue access$getMatrix$p(Disabler $this) {
        return $this.matrix;
    }

    public static final /* synthetic */ BoolValue access$getVulcancombat$p(Disabler $this) {
        return $this.vulcancombat;
    }

    public static final /* synthetic */ BoolValue access$getCompDecValue$p(Disabler $this) {
        return $this.compDecValue;
    }

    public static final /* synthetic */ BoolValue access$getDynamicValue$p(Disabler $this) {
        return $this.dynamicValue;
    }

    public static final /* synthetic */ BoolValue access$getVulcanstrafe$p(Disabler $this) {
        return $this.vulcanstrafe;
    }

    public static final /* synthetic */ BoolValue access$getPacketinvade$p(Disabler $this) {
        return $this.packetinvade;
    }

    public static final /* synthetic */ BoolValue access$getModifyC00Value$p(Disabler $this) {
        return $this.modifyC00Value;
    }

    public static final /* synthetic */ BoolValue access$getLatestverus$p(Disabler $this) {
        return $this.latestverus;
    }

    public static final /* synthetic */ BoolValue access$getLatestblocksmc$p(Disabler $this) {
        return $this.latestblocksmc;
    }

    public static final /* synthetic */ BoolValue access$getOldwatchdog1$p(Disabler $this) {
        return $this.oldwatchdog1;
    }

    public static final /* synthetic */ BoolValue access$getRotModify$p(Disabler $this) {
        return $this.rotModify;
    }

    public static final /* synthetic */ BoolValue access$getTestFeature$p(Disabler $this) {
        return $this.testFeature;
    }

    public static final /* synthetic */ BoolValue access$getOldwatchdog2$p(Disabler $this) {
        return $this.oldwatchdog2;
    }
}

