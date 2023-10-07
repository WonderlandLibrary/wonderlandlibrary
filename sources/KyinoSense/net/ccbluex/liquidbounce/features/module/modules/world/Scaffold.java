/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  net.minecraft.potion.Potion
 *  net.minecraft.stats.StatList
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.PlaceRotation;
import net.ccbluex.liquidbounce.utils.RenderUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.utils.render.ESPUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Scaffold", category=ModuleCategory.PLAYER, description="Test", keyBind=45)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u00bc\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0007\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010}\u001a\u00020~H\u0002J\u0011\u0010\u007f\u001a\u00020~2\u0007\u0010\u0080\u0001\u001a\u00020\"H\u0002J\u0013\u0010\u0081\u0001\u001a\u00020~2\b\u0010\u0082\u0001\u001a\u00030\u0083\u0001H\u0002J\t\u0010\u0084\u0001\u001a\u00020~H\u0016J\t\u0010\u0085\u0001\u001a\u00020~H\u0016J\u0013\u0010\u0086\u0001\u001a\u00020~2\b\u0010\u0082\u0001\u001a\u00030\u0087\u0001H\u0007J\u0013\u0010\u0088\u0001\u001a\u00020~2\b\u0010\u0082\u0001\u001a\u00030\u0083\u0001H\u0007J\u0013\u0010\u0089\u0001\u001a\u00020~2\b\u0010\u0082\u0001\u001a\u00030\u008a\u0001H\u0007J\u0013\u0010\u008b\u0001\u001a\u00020~2\b\u0010\u0082\u0001\u001a\u00030\u008c\u0001H\u0007J\u0015\u0010\u008d\u0001\u001a\u00020~2\n\u0010\u0082\u0001\u001a\u0005\u0018\u00010\u008e\u0001H\u0007J\u0013\u0010\u008f\u0001\u001a\u00020~2\b\u0010\u0082\u0001\u001a\u00030\u0090\u0001H\u0017J\u0013\u0010\u0091\u0001\u001a\u00020~2\b\u0010\u0082\u0001\u001a\u00030\u0092\u0001H\u0007J\u0015\u0010\u0093\u0001\u001a\u00020~2\n\u0010\u0082\u0001\u001a\u0005\u0018\u00010\u0094\u0001H\u0007J\u0012\u0010\u0095\u0001\u001a\u00020~2\u0007\u0010\u0096\u0001\u001a\u00020\"H\u0002J%\u0010\u0097\u0001\u001a\u00020~2\b\u0010\u0098\u0001\u001a\u00030\u0099\u00012\u0007\u0010\u009a\u0001\u001a\u00020\u000b2\u0007\u0010\u009b\u0001\u001a\u00020\u000bH\u0002J%\u0010\u009c\u0001\u001a\u00020\"2\b\u0010\u009d\u0001\u001a\u00030\u009e\u00012\u0007\u0010\u009f\u0001\u001a\u00020\"2\u0007\u0010\u0096\u0001\u001a\u00020\"H\u0002J\t\u0010\u00a0\u0001\u001a\u00020\"H\u0002J\u0007\u0010\u00a1\u0001\u001a\u00020\"R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0013\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010&\u001a\u00020\"8F\u00a2\u0006\u0006\u001a\u0004\b&\u0010'R\u000e\u0010(\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010.\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u000e\u00101\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00104\u001a\u0004\u0018\u000105X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00106\u001a\u0004\u0018\u000105X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010<\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010\u0015R\u000e\u0010>\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010E\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020GX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010H\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010\u0015R\u0011\u0010J\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010\u0015R\u000e\u0010L\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010M\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u00100R\u000e\u0010O\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010T\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bU\u0010VR\u000e\u0010W\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010X\u001a\u0004\u0018\u000105X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010Y\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Z\u001a\u00020GX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010[\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\\\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010]\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010^\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010_\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010`\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010a\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010b\u001a\u0004\u0018\u00010cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010d\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010f\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010g\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010h\u001a\u00020iX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010j\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010k\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010l\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010m\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010o\u001a\u0004\u0018\u00010cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010p\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010q\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010r\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010s\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010t\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bu\u0010VR\u000e\u0010v\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010w\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010x\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010y\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010z\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010{\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010|\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u00a2\u0001"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Scaffold;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airSafeValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "autoBlockMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "autoDisableSpeedValue", "autoJumpValue", "autof5", "blocksAmount", "", "getBlocksAmount", "()I", "blocksToEagleValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "constantMotionJumpGroundValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "constantMotionValue", "counterDisplayValue", "getCounterDisplayValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "customMoveSpeedValue", "customPitchValue", "customSpeedValue", "customYawValue", "delay", "", "delayTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "downValue", "eagleEdgeDistanceValue", "eagleSilentValue", "eagleSneaking", "", "eagleValue", "expandLengthValue", "faceBlock", "isTowerOnly", "()Z", "jumpDelayValue", "jumpGround", "", "jumpMotionValue", "keepLengthValue", "keepRotOnJumpValue", "keepRotationValue", "getKeepRotationValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "lastMS", "lastSlot", "launchY", "lockRotation", "Lnet/ccbluex/liquidbounce/utils/Rotation;", "lookupRotation", "markValue", "maxDelayValue", "maxTurnSpeed", "minDelayValue", "minTurnSpeed", "modeValue", "getModeValue", "noHitCheckValue", "noSpeedPotValue", "offGroundTicks", "omniDirectionalExpand", "placeConditionValue", "placeModeValue", "placeableDelay", "placedBlocksWithoutEagle", "progress", "", "rotationLookupValue", "getRotationLookupValue", "rotationModeValue", "getRotationModeValue", "rotationStrafeValue", "rotationsValue", "getRotationsValue", "safeWalkValue", "sameYValue", "shouldGoDown", "slot", "smartSpeedValue", "speedModifierValue", "getSpeedModifierValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "speenPitchValue", "speenRotation", "speenSpeedValue", "spinYaw", "sprintModeValue", "stableFakeJumpValue", "stableMotionValue", "stableStopDelayValue", "stableStopValue", "staticPitchValue", "swingValue", "targetPlace", "Lnet/ccbluex/liquidbounce/utils/block/PlaceInfo;", "teleportDelayValue", "teleportGroundValue", "teleportHeightValue", "teleportNoMotionValue", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "timerValue", "towerCenterValue", "towerDelayTimer", "towerEnabled", "towerModeValue", "towerPlace", "towerPlaceModeValue", "towerTimerValue", "verusJumped", "verusState", "xzMultiplier", "getXzMultiplier", "zitterDelay", "zitterDirection", "zitterModeValue", "zitterSpeed", "zitterStrength", "zitterTimer", "zitterValue", "fakeJump", "", "findBlock", "expand", "move", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onDisable", "onEnable", "onJump", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotion", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender2D", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onStrafe", "Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "place", "towerActive", "renderItemStack", "stack", "Lnet/minecraft/item/ItemStack;", "x", "y", "search", "blockPosition", "Lnet/minecraft/util/BlockPos;", "checks", "shouldPlace", "towerActivation", "KyinoClient"})
public final class Scaffold
extends Module {
    private final BoolValue towerEnabled = new BoolValue("EnableTower", true);
    private final BoolValue towerCenterValue = new BoolValue("Tower-Center", false, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final ListValue towerModeValue = new ListValue("TowerMode", new String[]{"Jump", "Motion", "StableMotion", "ConstantMotion", "MotionTP", "BlocksmcTest3", "BlocksmcTest3.1", "Packet", "Teleport", "AAC3.3.9", "AAC3.6.4", "Verus"}, "ConstantMotion", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final ListValue towerPlaceModeValue = new ListValue("Tower-PlaceTiming", new String[]{"Pre", "Post", "Legit"}, "Legit");
    private final FloatValue towerTimerValue = new FloatValue("TowerTimer", 1.0f, 0.1f, 10.0f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "Jump", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final IntegerValue jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "Jump", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue stableMotionValue = new FloatValue("StableMotion", 0.41982f, 0.1f, 1.0f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "StableMotion", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue stableFakeJumpValue = new BoolValue("StableFakeJump", false, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "StableMotion", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue stableStopValue = new BoolValue("StableStop", false, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "StableMotion", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final IntegerValue stableStopDelayValue = new IntegerValue("StableStopDelay", 1500, 0, 5000, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "StableMotion", true) && (Boolean)Scaffold.access$getStableStopValue$p(this.this$0).get() != false;
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "ConstantMotion", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "ConstantMotion", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "Teleport", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final IntegerValue teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "Teleport", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue teleportGroundValue = new BoolValue("TeleportGround", true, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "Teleport", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue teleportNoMotionValue = new BoolValue("TeleportNoMotion", false, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getTowerEnabled$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getTowerModeValue$p(this.this$0).get(), "Teleport", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "Rewinside", "Expand"}, "Normal");
    @JvmField
    @NotNull
    public final ListValue sprintModeValue = new ListValue("SprintMode", new String[]{"Same", "Silent", "Ground", "Air", "Off"}, "Same");
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post", "Legit"}, "Legit");
    @NotNull
    private final ListValue counterDisplayValue = new ListValue("Counter", new String[]{"Off", "Simple", "Dark", "Exhibition", "Advanced", "Sigma", "Novoline"}, "Simple");
    private final ListValue autoBlockMode = new ListValue("AutoBlock", new String[]{"LiteSpoof", "Spoof", "Switch", "Off"}, "LiteSpoof");
    private final ListValue placeConditionValue = new ListValue("Place-Condition", new String[]{"Air", "FallDown", "NegativeMotion", "Always"}, "Always");
    private final BoolValue placeableDelay = new BoolValue("PlaceableDelay", false);
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 50, 0, 1000, "ms"){
        final /* synthetic */ Scaffold this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)Scaffold.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 50, 0, 1000, "ms"){
        final /* synthetic */ Scaffold this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)Scaffold.access$getMaxDelayValue$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f);
    @NotNull
    private final FloatValue speedModifierValue = new FloatValue("SpeedModifier", 1.0f, 0.0f, 2.0f, "x");
    @NotNull
    private final FloatValue xzMultiplier = new FloatValue("XZ-Multiplier", 1.0f, 0.0f, 4.0f, "x");
    private final BoolValue customSpeedValue = new BoolValue("CustomSpeed", false);
    private final FloatValue customMoveSpeedValue = new FloatValue("CustomMoveSpeed", 0.2f, 0.0f, 5.0f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getCustomSpeedValue$p(this.this$0).get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue markValue = new BoolValue("Mark", false);
    private final BoolValue swingValue = new BoolValue("Swing", false);
    private final BoolValue downValue = new BoolValue("Down", true);
    private final BoolValue sameYValue = new BoolValue("KeepY", false);
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private final BoolValue smartSpeedValue = new BoolValue("SpeedKeepY", true);
    private final BoolValue safeWalkValue = new BoolValue("SafeWalk", false);
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getSafeWalkValue$p(this.this$0).get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue autoDisableSpeedValue = new BoolValue("AutoDisable-Speed", false);
    private final BoolValue noSpeedPotValue = new BoolValue("NoSpeedPot", false);
    @NotNull
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    @NotNull
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", true, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)this.this$0.getRotationsValue().get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue rotationStrafeValue = new BoolValue("RotationStrafe", false);
    private final BoolValue noHitCheckValue = new BoolValue("NoHitCheck", false, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)this.this$0.getRotationsValue().get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 120.0f, 0.0f, 180.0f, "\u00b0", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)this.this$0.getRotationsValue().get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    }){
        final /* synthetic */ Scaffold this$0;

        protected void onChanged(float oldValue, float newValue) {
            float i = ((Number)Scaffold.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
            if (i > newValue) {
                this.set(Float.valueOf(i));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5, $super_call_param$6);
        }
    };
    private final FloatValue minTurnSpeed = new FloatValue(this, "MinTurnSpeed", 80.0f, 0.0f, 180.0f, "\u00b0", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)this.this$0.getRotationsValue().get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    }){
        final /* synthetic */ Scaffold this$0;

        protected void onChanged(float oldValue, float newValue) {
            float i = ((Number)Scaffold.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
            if (i < newValue) {
                this.set(Float.valueOf(i));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5, $super_call_param$6);
        }
    };
    @NotNull
    private final ListValue rotationModeValue = new ListValue("RotationMode", new String[]{"Normal", "AAC", "Watchdog", "Static", "Static2", "Static3", "Spin", "Custom"}, "Normal");
    @NotNull
    private final ListValue rotationLookupValue = new ListValue("RotationLookup", new String[]{"Normal", "AAC", "Same"}, "Normal");
    private final FloatValue staticPitchValue = new FloatValue("Static-Pitch", 86.0f, 80.0f, 90.0f, "\u00b0", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            String string = (String)this.this$0.getRotationModeValue().get();
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
            return StringsKt.startsWith$default(string3, "static", false, 2, null);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue customYawValue = new FloatValue("Custom-Yaw", 135.0f, -180.0f, 180.0f, "\u00b0", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)this.this$0.getRotationModeValue().get(), "custom", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue customPitchValue = new FloatValue("Custom-Pitch", 86.0f, -90.0f, 90.0f, "\u00b0", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)this.this$0.getRotationModeValue().get(), "custom", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue speenSpeedValue = new FloatValue("Spin-Speed", 5.0f, -90.0f, 90.0f, "\u00b0", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)this.this$0.getRotationModeValue().get(), "spin", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue speenPitchValue = new FloatValue("Spin-Pitch", 90.0f, -90.0f, 90.0f, "\u00b0", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)this.this$0.getRotationModeValue().get(), "spin", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue keepRotOnJumpValue = new BoolValue("KeepRotOnJump", true, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return !StringsKt.equals((String)this.this$0.getRotationModeValue().get(), "normal", true) && !StringsKt.equals((String)this.this$0.getRotationModeValue().get(), "aac", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final IntegerValue keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)this.this$0.getRotationsValue().get() != false && (Boolean)this.this$0.getKeepRotationValue().get() == false;
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue eagleValue = new BoolValue("Eagle", false);
    private final BoolValue eagleSilentValue = new BoolValue("EagleSilent", false, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getEagleValue$p(this.this$0).get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final IntegerValue blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getEagleValue$p(this.this$0).get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue eagleEdgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2f, 0.0f, 0.5f, "m", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return (Boolean)Scaffold.access$getEagleValue$p(this.this$0).get();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue omniDirectionalExpand = new BoolValue("OmniDirectionalExpand", true, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)this.this$0.getModeValue().get(), "expand", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final IntegerValue expandLengthValue = new IntegerValue("ExpandLength", 3, 1, 6, " blocks", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)this.this$0.getModeValue().get(), "expand", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue zitterValue = new BoolValue("Zitter", false, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return !this.this$0.isTowerOnly();
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final ListValue zitterModeValue = new ListValue("ZitterMode", new String[]{"Teleport", "Smooth"}, "Smooth", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return !this.this$0.isTowerOnly() && (Boolean)Scaffold.access$getZitterValue$p(this.this$0).get() != false;
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue zitterSpeed = new FloatValue("ZitterSpeed", 0.13f, 0.1f, 0.3f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return !this.this$0.isTowerOnly() && (Boolean)Scaffold.access$getZitterValue$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getZitterModeValue$p(this.this$0).get(), "teleport", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final FloatValue zitterStrength = new FloatValue("ZitterStrength", 0.072f, 0.05f, 0.2f, new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return !this.this$0.isTowerOnly() && (Boolean)Scaffold.access$getZitterValue$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getZitterModeValue$p(this.this$0).get(), "teleport", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final IntegerValue zitterDelay = new IntegerValue("ZitterDelay", 100, 0, 500, "ms", new Function0<Boolean>(this){
        final /* synthetic */ Scaffold this$0;

        public final boolean invoke() {
            return !this.this$0.isTowerOnly() && (Boolean)Scaffold.access$getZitterValue$p(this.this$0).get() != false && StringsKt.equals((String)Scaffold.access$getZitterModeValue$p(this.this$0).get(), "smooth", true);
        }
        {
            this.this$0 = scaffold;
            super(0);
        }
    });
    private final BoolValue autof5 = new BoolValue("ThirdPerson-View", false);
    private final MSTimer delayTimer = new MSTimer();
    private final MSTimer towerDelayTimer = new MSTimer();
    private final MSTimer zitterTimer = new MSTimer();
    private final TickTimer timer = new TickTimer();
    private PlaceInfo targetPlace;
    private PlaceInfo towerPlace;
    private int launchY;
    private boolean faceBlock;
    private Rotation lockRotation;
    private Rotation lookupRotation;
    private Rotation speenRotation;
    private int slot;
    private int lastSlot;
    private boolean zitterDirection;
    private long delay;
    private int placedBlocksWithoutEagle;
    private boolean eagleSneaking;
    private boolean shouldGoDown;
    private float progress;
    private float spinYaw;
    private long lastMS;
    private double jumpGround;
    private int verusState;
    private boolean verusJumped;
    private int offGroundTicks;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @NotNull
    public final ListValue getCounterDisplayValue() {
        return this.counterDisplayValue;
    }

    @NotNull
    public final FloatValue getSpeedModifierValue() {
        return this.speedModifierValue;
    }

    @NotNull
    public final FloatValue getXzMultiplier() {
        return this.xzMultiplier;
    }

    @NotNull
    public final BoolValue getRotationsValue() {
        return this.rotationsValue;
    }

    @NotNull
    public final BoolValue getKeepRotationValue() {
        return this.keepRotationValue;
    }

    @NotNull
    public final ListValue getRotationModeValue() {
        return this.rotationModeValue;
    }

    @NotNull
    public final ListValue getRotationLookupValue() {
        return this.rotationLookupValue;
    }

    public final boolean isTowerOnly() {
        return (Boolean)this.towerEnabled.get();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean towerActivation() {
        if ((Boolean)this.towerEnabled.get() == false) return false;
        KeyBinding keyBinding = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
        Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
        if (!keyBinding.func_151470_d()) return false;
        if ((Boolean)this.towerCenterValue.get() == false) return true;
        if ((Boolean)this.towerCenterValue.get() == false) return false;
        if (MovementUtils.isMoving()) return false;
        return true;
    }

    @Override
    public void onEnable() {
        if (Scaffold.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        this.progress = 0.0f;
        this.spinYaw = 0.0f;
        this.launchY = (int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
        this.lastSlot = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c;
        this.slot = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c;
        this.faceBlock = false;
        this.lastMS = System.currentTimeMillis();
        if (((Boolean)this.autof5.get()).booleanValue()) {
            Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74320_O = 1;
        }
    }

    private final void fakeJump() {
        Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70160_al = true;
        Scaffold.access$getMc$p$s1046033730().field_71439_g.func_71029_a(StatList.field_75953_u);
    }

    private final void move(MotionEvent event) {
        String string = (String)this.towerModeValue.get();
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
        switch (string3) {
            case "jump": {
                if (!Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E || !this.timer.hasTimePassed(((Number)this.jumpDelayValue.get()).intValue())) break;
                this.fakeJump();
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.jumpMotionValue.get()).floatValue();
                this.timer.reset();
                break;
            }
            case "blocksmctest": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u % 1.0 <= 0.00153598) {
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Math.floor(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.41998;
                }
                if (!(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u % 1.0 < 0.1) || this.offGroundTicks == 0) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Math.floor(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                break;
            }
            case "motion": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.41999998688698;
                    break;
                }
                if (!(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x < 0.1)) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -0.3;
                break;
            }
            case "motiontp": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.41999998688698;
                    break;
                }
                if (!(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x < 0.23)) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, (double)((int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                break;
            }
            case "blocksmctest3": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.42;
                }
                if (!(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x < 0.23)) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, (double)((int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E = true;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.42;
                break;
            }
            case "blocksmctest3.1": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.42;
                }
                if (!(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x < 0.23)) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E = true;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.42;
                break;
            }
            case "packet": {
                if (!Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E || !this.timer.hasTimePassed(2)) break;
                this.fakeJump();
                Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.41999998688698, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                Minecraft minecraft2 = Scaffold.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.76, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 1.08, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                this.timer.reset();
                break;
            }
            case "teleport": {
                if (((Boolean)this.teleportNoMotionValue.get()).booleanValue()) {
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                }
                if (!Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E && ((Boolean)this.teleportGroundValue.get()).booleanValue() || !this.timer.hasTimePassed(((Number)this.teleportDelayValue.get()).intValue())) break;
                this.fakeJump();
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70634_a(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u + ((Number)this.teleportHeightValue.get()).doubleValue(), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                this.timer.reset();
                break;
            }
            case "stablemotion": {
                if (((Boolean)this.stableFakeJumpValue.get()).booleanValue()) {
                    this.fakeJump();
                }
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.stableMotionValue.get()).floatValue();
                if (!((Boolean)this.stableStopValue.get()).booleanValue() || !this.towerDelayTimer.hasTimePassed(((Number)this.stableStopDelayValue.get()).intValue())) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -0.28;
                this.towerDelayTimer.reset();
                break;
            }
            case "constantmotion": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.fakeJump();
                    this.jumpGround = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.constantMotionValue.get()).floatValue();
                }
                if (!(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u > this.jumpGround + ((Number)this.constantMotionJumpGroundValue.get()).doubleValue())) break;
                this.fakeJump();
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, (double)((int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.constantMotionValue.get()).floatValue();
                this.jumpGround = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                break;
            }
            case "aac3.3.9": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.fakeJump();
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.4001;
                }
                Scaffold.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
                if (!(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x < 0.0)) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x -= 9.45E-6;
                Scaffold.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.6f;
                break;
            }
            case "aac3.6.4": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 4 == 1) {
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.4195464;
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t - 0.035, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                    break;
                }
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 4 != 0) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -0.5;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t + 0.035, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                break;
            }
            case "verus": {
                WorldClient worldClient = Scaffold.access$getMc$p$s1046033730().field_71441_e;
                Entity entity = (Entity)Scaffold.access$getMc$p$s1046033730().field_71439_g;
                EntityPlayerSP entityPlayerSP = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (!worldClient.func_72945_a(entity, entityPlayerSP.func_174813_aQ().func_72317_d(0.0, -0.01, 0.0)).isEmpty() && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70124_G) {
                    this.verusState = 0;
                    this.verusJumped = true;
                }
                if (this.verusJumped) {
                    MovementUtils.strafe();
                    switch (this.verusState) {
                        case 0: {
                            this.fakeJump();
                            Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.42f;
                            Scaffold scaffold = this;
                            ++scaffold.verusState;
                            int cfr_ignored_0 = scaffold.verusState;
                            break;
                        }
                        case 1: {
                            Scaffold scaffold = this;
                            ++scaffold.verusState;
                            int cfr_ignored_1 = scaffold.verusState;
                            break;
                        }
                        case 2: {
                            Scaffold scaffold = this;
                            ++scaffold.verusState;
                            int cfr_ignored_2 = scaffold.verusState;
                            break;
                        }
                        case 3: {
                            event.setOnGround(true);
                            Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                            Scaffold scaffold = this;
                            ++scaffold.verusState;
                            int cfr_ignored_3 = scaffold.verusState;
                            break;
                        }
                        case 4: {
                            Scaffold scaffold = this;
                            ++scaffold.verusState;
                            int cfr_ignored_4 = scaffold.verusState;
                        }
                    }
                    this.verusJumped = false;
                }
                this.verusJumped = true;
                break;
            }
            case "blocksmc": {
                if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u % 1.0 <= 0.00153598) {
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Math.floor(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.41998;
                    break;
                }
                if (!(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u % 1.0 < 0.1) || this.offGroundTicks == 0) break;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Math.floor(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        block54: {
            block57: {
                block56: {
                    block55: {
                        block53: {
                            block51: {
                                block52: {
                                    ItemStack itemStack;
                                    block50: {
                                        int blockSlot;
                                        block49: {
                                            if (((Boolean)this.autoDisableSpeedValue.get()).booleanValue()) {
                                                Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                                                if (module == null) {
                                                    Intrinsics.throwNpe();
                                                }
                                                if (module.getState()) {
                                                    Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                                                    if (module2 == null) {
                                                        Intrinsics.throwNpe();
                                                    }
                                                    module2.setState(false);
                                                    new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                                                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Speed was disabled!", Notification.Type.WARNING));
                                                }
                                            }
                                            blockSlot = -1;
                                            EntityPlayerSP entityPlayerSP = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                                            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                                            itemStack = entityPlayerSP.func_70694_bm();
                                            EntityPlayerSP entityPlayerSP2 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                                            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                                            if (entityPlayerSP2.func_70694_bm() == null) break block49;
                                            EntityPlayerSP entityPlayerSP3 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                                            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                                            ItemStack itemStack2 = entityPlayerSP3.func_70694_bm();
                                            Intrinsics.checkExpressionValueIsNotNull(itemStack2, "mc.thePlayer.heldItem");
                                            if (itemStack2.func_77973_b() instanceof ItemBlock) break block50;
                                        }
                                        if (StringsKt.equals((String)this.autoBlockMode.get(), "Off", true)) {
                                            return;
                                        }
                                        blockSlot = InventoryUtils.findAutoBlockBlock();
                                        if (blockSlot == -1) {
                                            return;
                                        }
                                        if (StringsKt.equals((String)this.autoBlockMode.get(), "Switch", true)) {
                                            Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c = blockSlot - 36;
                                            Scaffold.access$getMc$p$s1046033730().field_71442_b.func_78765_e();
                                        }
                                        if (StringsKt.equals((String)this.autoBlockMode.get(), "LiteSpoof", true)) {
                                            Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
                                            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                                            minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(blockSlot - 36));
                                            Slot slot = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a(blockSlot);
                                            Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(blockSlot)");
                                            itemStack = slot.func_75211_c();
                                        }
                                    }
                                    if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                                        this.offGroundTicks = 0;
                                    } else {
                                        int n = this.offGroundTicks;
                                        this.offGroundTicks = n + 1;
                                    }
                                    if (itemStack == null || itemStack.func_77973_b() == null || !(itemStack.func_77973_b() instanceof ItemBlock)) break block51;
                                    Item item = itemStack.func_77973_b();
                                    if (item == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
                                    }
                                    Block block = ((ItemBlock)item).func_179223_d();
                                    if (InventoryUtils.BLOCK_BLACKLIST.contains(block)) break block52;
                                    Block block2 = block;
                                    Intrinsics.checkExpressionValueIsNotNull(block2, "block");
                                    if (block2.func_149686_d() && itemStack.field_77994_a > 0) break block51;
                                }
                                return;
                            }
                            if ((!((Boolean)this.rotationsValue.get()).booleanValue() || ((Boolean)this.noHitCheckValue.get()).booleanValue() || this.faceBlock) && Intrinsics.areEqual((String)this.placeModeValue.get(), "Legit") && !this.towerActivation() || Intrinsics.areEqual((String)this.towerPlaceModeValue.get(), "Legit") && this.towerActivation()) {
                                this.place(false);
                            }
                            if (this.towerActivation()) {
                                this.shouldGoDown = false;
                                Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74311_E.field_74513_e = false;
                                return;
                            }
                            Scaffold.access$getMc$p$s1046033730().field_71428_T.field_74278_d = ((Number)this.timerValue.get()).floatValue();
                            boolean bl = this.shouldGoDown = (Boolean)this.downValue.get() != false && GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74311_E) && this.getBlocksAmount() > 1;
                            if (this.shouldGoDown) {
                                Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74311_E.field_74513_e = false;
                            }
                            if (((Boolean)this.customSpeedValue.get()).booleanValue()) {
                                MovementUtils.strafe(((Number)this.customMoveSpeedValue.get()).floatValue());
                            }
                            if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                                String mode = (String)this.modeValue.get();
                                if (StringsKt.equals(mode, "Rewinside", true)) {
                                    MovementUtils.strafe(0.2f);
                                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                                }
                                if (((Boolean)this.zitterValue.get()).booleanValue() && StringsKt.equals((String)this.zitterModeValue.get(), "smooth", true)) {
                                    if (!GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z)) {
                                        Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z.field_74513_e = false;
                                    }
                                    if (!GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x)) {
                                        Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x.field_74513_e = false;
                                    }
                                    if (this.zitterTimer.hasTimePassed(((Number)this.zitterDelay.get()).intValue())) {
                                        this.zitterDirection = !this.zitterDirection;
                                        this.zitterTimer.reset();
                                    }
                                    if (this.zitterDirection) {
                                        Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z.field_74513_e = true;
                                        Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x.field_74513_e = false;
                                    } else {
                                        Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z.field_74513_e = false;
                                        Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x.field_74513_e = true;
                                    }
                                }
                                if (((Boolean)this.eagleValue.get()).booleanValue() && !this.shouldGoDown) {
                                    int n;
                                    double dif = 0.5;
                                    if (((Number)this.eagleEdgeDistanceValue.get()).floatValue() > 0.0f) {
                                        n = 0;
                                        int n2 = 3;
                                        while (n <= n2) {
                                            void i;
                                            BlockPos blockPos = new BlockPos(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t + (double)(i == false ? -1 : (i == true ? 1 : 0)), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u - (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u == (double)((int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u) + 0.5 ? 0.0 : 1.0), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v + (double)(i == 2 ? -1 : (i == 3 ? 1 : 0)));
                                            PlaceInfo placeInfo = PlaceInfo.Companion.get(blockPos);
                                            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                                double calcDif = i > true ? Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v - (double)blockPos.func_177952_p() : Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t - (double)blockPos.func_177958_n();
                                                if ((calcDif -= 0.5) < 0.0) {
                                                    calcDif *= -1.0;
                                                }
                                                if ((calcDif -= 0.5) < dif) {
                                                    dif = calcDif;
                                                }
                                            }
                                            ++i;
                                        }
                                    }
                                    if (this.placedBlocksWithoutEagle >= ((Number)this.blocksToEagleValue.get()).intValue()) {
                                        boolean shouldEagle;
                                        IBlockState iBlockState = Scaffold.access$getMc$p$s1046033730().field_71441_e.func_180495_p(new BlockPos(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.0, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v));
                                        Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockStat\u2026sZ)\n                    )");
                                        boolean bl2 = shouldEagle = iBlockState.func_177230_c() == Blocks.field_150350_a || dif < ((Number)this.eagleEdgeDistanceValue.get()).doubleValue();
                                        if (((Boolean)this.eagleSilentValue.get()).booleanValue()) {
                                            if (this.eagleSneaking != shouldEagle) {
                                                Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
                                                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                                                minecraft.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Scaffold.access$getMc$p$s1046033730().field_71439_g, shouldEagle ? C0BPacketEntityAction.Action.START_SNEAKING : C0BPacketEntityAction.Action.STOP_SNEAKING));
                                            }
                                            this.eagleSneaking = shouldEagle;
                                        } else {
                                            Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74311_E.field_74513_e = shouldEagle;
                                        }
                                        this.placedBlocksWithoutEagle = 0;
                                    } else {
                                        n = this.placedBlocksWithoutEagle;
                                        this.placedBlocksWithoutEagle = n + 1;
                                    }
                                }
                                if (((Boolean)this.zitterValue.get()).booleanValue() && StringsKt.equals((String)this.zitterModeValue.get(), "teleport", true)) {
                                    MovementUtils.strafe(((Number)this.zitterSpeed.get()).floatValue());
                                    double yaw = Math.toRadians((double)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z + (this.zitterDirection ? 90.0 : -90.0));
                                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70159_w -= Math.sin(yaw) * ((Number)this.zitterStrength.get()).doubleValue();
                                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70179_y += Math.cos(yaw) * ((Number)this.zitterStrength.get()).doubleValue();
                                    boolean bl3 = this.zitterDirection = !this.zitterDirection;
                                }
                            }
                            if (StringsKt.equals((String)this.sprintModeValue.get(), "off", true) || StringsKt.equals((String)this.sprintModeValue.get(), "ground", true) && !Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E || StringsKt.equals((String)this.sprintModeValue.get(), "air", true) && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                                EntityPlayerSP entityPlayerSP = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                                entityPlayerSP.func_70031_b(false);
                            }
                            if (!this.shouldGoDown) break block53;
                            this.launchY = (int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1;
                            break block54;
                        }
                        if (((Boolean)this.sameYValue.get()).booleanValue()) break block54;
                        if (((Boolean)this.autoJumpValue.get()).booleanValue()) break block55;
                        if (!((Boolean)this.smartSpeedValue.get()).booleanValue()) break block56;
                        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                        if (module == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!module.getState()) break block56;
                    }
                    if (!GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A) && !(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u < (double)this.launchY)) break block57;
                }
                this.launchY = (int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
            }
            if (((Boolean)this.autoJumpValue.get()).booleanValue()) {
                Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (!module.getState() && MovementUtils.isMoving() && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
                }
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (Scaffold.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (StringsKt.equals((String)this.sprintModeValue.get(), "silent", true) && packet instanceof C0BPacketEntityAction && (((C0BPacketEntityAction)packet).func_180764_b() == C0BPacketEntityAction.Action.STOP_SPRINTING || ((C0BPacketEntityAction)packet).func_180764_b() == C0BPacketEntityAction.Action.START_SPRINTING)) {
            event.cancelEvent();
        }
        if (packet instanceof C09PacketHeldItemChange) {
            this.slot = ((C09PacketHeldItemChange)packet).func_149614_c();
        }
    }

    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.lookupRotation != null && ((Boolean)this.rotationStrafeValue.get()).booleanValue()) {
            float f;
            float f2 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z;
            Rotation rotation = this.lookupRotation;
            if (rotation == null) {
                Intrinsics.throwNpe();
            }
            int dif = (int)((MathHelper.func_76142_g((float)(f2 - rotation.getYaw() - 23.5f - (float)135)) + (float)180) / (float)45);
            Rotation rotation2 = this.lookupRotation;
            if (rotation2 == null) {
                Intrinsics.throwNpe();
            }
            float yaw = rotation2.getYaw();
            float strafe = event.getStrafe();
            float forward = event.getForward();
            float friction = event.getFriction();
            float calcForward = 0.0f;
            float calcStrafe = 0.0f;
            switch (dif) {
                case 0: {
                    calcForward = forward;
                    calcStrafe = strafe;
                    break;
                }
                case 1: {
                    calcForward += forward;
                    calcStrafe -= forward;
                    calcForward += strafe;
                    calcStrafe += strafe;
                    break;
                }
                case 2: {
                    calcForward = strafe;
                    calcStrafe = -forward;
                    break;
                }
                case 3: {
                    calcForward -= forward;
                    calcStrafe -= forward;
                    calcForward += strafe;
                    calcStrafe -= strafe;
                    break;
                }
                case 4: {
                    calcForward = -forward;
                    calcStrafe = -strafe;
                    break;
                }
                case 5: {
                    calcForward -= forward;
                    calcStrafe += forward;
                    calcForward -= strafe;
                    calcStrafe -= strafe;
                    break;
                }
                case 6: {
                    calcForward = -strafe;
                    calcStrafe = forward;
                    break;
                }
                case 7: {
                    calcForward += forward;
                    calcStrafe += forward;
                    calcForward -= strafe;
                    calcStrafe += strafe;
                }
            }
            if (calcForward > 1.0f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1.0f || calcForward > -0.9f && calcForward < -0.3f) {
                calcForward *= 0.5f;
            }
            if (calcStrafe > 1.0f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1.0f || calcStrafe > -0.9f && calcStrafe < -0.3f) {
                calcStrafe *= 0.5f;
            }
            if ((f = calcStrafe * calcStrafe + calcForward * calcForward) >= 1.0E-4f) {
                if ((f = MathHelper.func_76129_c((float)f)) < 1.0f) {
                    f = 1.0f;
                }
                f = friction / f;
                float yawSin = MathHelper.func_76126_a((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                float yawCos = MathHelper.func_76134_b((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70159_w += (double)((calcStrafe *= f) * yawCos - (calcForward *= f) * yawSin);
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70179_y += (double)(calcForward * yawCos + calcStrafe * yawSin);
            }
            event.cancelEvent();
        }
    }

    private final boolean shouldPlace() {
        boolean placeWhenAir = StringsKt.equals((String)this.placeConditionValue.get(), "air", true);
        boolean placeWhenFall = StringsKt.equals((String)this.placeConditionValue.get(), "falldown", true);
        boolean placeWhenNegativeMotion = StringsKt.equals((String)this.placeConditionValue.get(), "negativemotion", true);
        boolean alwaysPlace = StringsKt.equals((String)this.placeConditionValue.get(), "always", true);
        return this.towerActivation() || alwaysPlace || placeWhenAir && !Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E || placeWhenFall && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 0.0f || placeWhenNegativeMotion && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70181_x < 0.0;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        block58: {
            block53: {
                block57: {
                    block54: {
                        block56: {
                            block55: {
                                Intrinsics.checkParameterIsNotNull(event, "event");
                                if (this.towerActivation() && event.getEventState() != EventState.POST && ((Boolean)this.towerCenterValue.get()).booleanValue()) {
                                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                                }
                                if (((Boolean)this.noSpeedPotValue.get()).booleanValue() && Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76424_c) && !this.towerActivation() && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= (double)0.8f;
                                    Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= (double)0.8f;
                                }
                                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= (double)((Number)this.xzMultiplier.get()).floatValue();
                                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= (double)((Number)this.xzMultiplier.get()).floatValue();
                                if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
                                    if (StringsKt.equals((String)this.rotationModeValue.get(), "spin", true)) {
                                        this.spinYaw += ((Number)this.speenSpeedValue.get()).floatValue();
                                        this.spinYaw = MathHelper.func_76142_g((float)this.spinYaw);
                                        this.speenRotation = new Rotation(this.spinYaw, ((Number)this.speenPitchValue.get()).floatValue());
                                        RotationUtils.setTargetRotation(this.speenRotation);
                                    } else if (this.lockRotation != null) {
                                        RotationUtils.setTargetRotation(RotationUtils.limitAngleChange(RotationUtils.serverRotation, this.lockRotation, RandomUtils.nextFloat(((Number)this.minTurnSpeed.get()).floatValue(), ((Number)this.maxTurnSpeed.get()).floatValue())));
                                    }
                                } else if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue()) {
                                    RotationUtils.setTargetRotation(new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)180, 84.0f));
                                    v0 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                    Intrinsics.checkExpressionValueIsNotNull(v0, "mc.gameSettings.keyBindForward");
                                    if (!v0.func_151470_d()) {
                                        v1 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                        Intrinsics.checkExpressionValueIsNotNull(v1, "mc.gameSettings.keyBindRight");
                                        if (!v1.func_151470_d()) {
                                            v2 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                            Intrinsics.checkExpressionValueIsNotNull(v2, "mc.gameSettings.keyBindLeft");
                                            if (!v2.func_151470_d()) {
                                                v3 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                Intrinsics.checkExpressionValueIsNotNull(v3, "mc.gameSettings.keyBindBack");
                                                if (v3.func_151470_d()) {
                                                    RotationUtils.setTargetRotation(new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z, 84.0f));
                                                }
                                            }
                                        }
                                    }
                                    v4 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                    Intrinsics.checkExpressionValueIsNotNull(v4, "mc.gameSettings.keyBindForward");
                                    if (v4.func_151470_d()) {
                                        v5 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                        Intrinsics.checkExpressionValueIsNotNull(v5, "mc.gameSettings.keyBindRight");
                                        if (v5.func_151470_d()) {
                                            v6 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                            Intrinsics.checkExpressionValueIsNotNull(v6, "mc.gameSettings.keyBindLeft");
                                            if (!v6.func_151470_d()) {
                                                v7 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                Intrinsics.checkExpressionValueIsNotNull(v7, "mc.gameSettings.keyBindBack");
                                                if (!v7.func_151470_d()) {
                                                    RotationUtils.setTargetRotation(new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)135, 84.0f));
                                                }
                                            }
                                        }
                                    }
                                    v8 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                    Intrinsics.checkExpressionValueIsNotNull(v8, "mc.gameSettings.keyBindForward");
                                    if (!v8.func_151470_d()) {
                                        v9 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                        Intrinsics.checkExpressionValueIsNotNull(v9, "mc.gameSettings.keyBindRight");
                                        if (v9.func_151470_d()) {
                                            v10 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                            Intrinsics.checkExpressionValueIsNotNull(v10, "mc.gameSettings.keyBindLeft");
                                            if (!v10.func_151470_d()) {
                                                v11 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                Intrinsics.checkExpressionValueIsNotNull(v11, "mc.gameSettings.keyBindBack");
                                                if (!v11.func_151470_d()) {
                                                    RotationUtils.setTargetRotation(new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)90, 84.0f));
                                                }
                                            }
                                        }
                                    }
                                    v12 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                    Intrinsics.checkExpressionValueIsNotNull(v12, "mc.gameSettings.keyBindForward");
                                    if (!v12.func_151470_d()) {
                                        v13 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                        Intrinsics.checkExpressionValueIsNotNull(v13, "mc.gameSettings.keyBindRight");
                                        if (v13.func_151470_d()) {
                                            v14 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                            Intrinsics.checkExpressionValueIsNotNull(v14, "mc.gameSettings.keyBindLeft");
                                            if (!v14.func_151470_d()) {
                                                v15 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                Intrinsics.checkExpressionValueIsNotNull(v15, "mc.gameSettings.keyBindBack");
                                                if (v15.func_151470_d()) {
                                                    RotationUtils.setTargetRotation(new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)45, 84.0f));
                                                }
                                            }
                                        }
                                    }
                                    v16 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                    Intrinsics.checkExpressionValueIsNotNull(v16, "mc.gameSettings.keyBindForward");
                                    if (v16.func_151470_d()) {
                                        v17 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                        Intrinsics.checkExpressionValueIsNotNull(v17, "mc.gameSettings.keyBindRight");
                                        if (!v17.func_151470_d()) {
                                            v18 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                            Intrinsics.checkExpressionValueIsNotNull(v18, "mc.gameSettings.keyBindLeft");
                                            if (v18.func_151470_d()) {
                                                v19 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                Intrinsics.checkExpressionValueIsNotNull(v19, "mc.gameSettings.keyBindBack");
                                                if (!v19.func_151470_d()) {
                                                    RotationUtils.setTargetRotation(new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)225, 84.0f));
                                                }
                                            }
                                        }
                                    }
                                    v20 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                    Intrinsics.checkExpressionValueIsNotNull(v20, "mc.gameSettings.keyBindForward");
                                    if (!v20.func_151470_d()) {
                                        v21 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                        Intrinsics.checkExpressionValueIsNotNull(v21, "mc.gameSettings.keyBindRight");
                                        if (!v21.func_151470_d()) {
                                            v22 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                            Intrinsics.checkExpressionValueIsNotNull(v22, "mc.gameSettings.keyBindLeft");
                                            if (v22.func_151470_d()) {
                                                v23 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                Intrinsics.checkExpressionValueIsNotNull(v23, "mc.gameSettings.keyBindBack");
                                                if (!v23.func_151470_d()) {
                                                    RotationUtils.setTargetRotation(new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)270, 84.0f));
                                                }
                                            }
                                        }
                                    }
                                    v24 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                    Intrinsics.checkExpressionValueIsNotNull(v24, "mc.gameSettings.keyBindForward");
                                    if (!v24.func_151470_d()) {
                                        v25 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                        Intrinsics.checkExpressionValueIsNotNull(v25, "mc.gameSettings.keyBindRight");
                                        if (!v25.func_151470_d()) {
                                            v26 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                            Intrinsics.checkExpressionValueIsNotNull(v26, "mc.gameSettings.keyBindLeft");
                                            if (v26.func_151470_d()) {
                                                v27 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                Intrinsics.checkExpressionValueIsNotNull(v27, "mc.gameSettings.keyBindBack");
                                                if (v27.func_151470_d()) {
                                                    RotationUtils.setTargetRotation(new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)315, 84.0f));
                                                }
                                            }
                                        }
                                    }
                                }
                                mode = (String)this.modeValue.get();
                                eventState = event.getEventState();
                                var4_4 = 0;
                                var5_5 = 7;
                                while (var4_4 <= var5_5) {
                                    if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[i] != null && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[i].field_77994_a <= 0) {
                                        Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[i] = null;
                                    }
                                    ++i;
                                }
                                if ((!((Boolean)this.rotationsValue.get()).booleanValue() || ((Boolean)this.noHitCheckValue.get()).booleanValue() || this.faceBlock) && StringsKt.equals((String)this.placeModeValue.get(), eventState.getStateName(), true) && !this.towerActivation() || StringsKt.equals((String)this.towerPlaceModeValue.get(), eventState.getStateName(), true) && this.towerActivation()) {
                                    this.place(false);
                                }
                                if (eventState != EventState.PRE) break block53;
                                if (!this.shouldPlace()) break block54;
                                if (StringsKt.equals((String)this.autoBlockMode.get(), "Off", true)) break block55;
                                v28 = InventoryUtils.findAutoBlockBlock() == -1;
                                break block56;
                            }
                            v29 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                            Intrinsics.checkExpressionValueIsNotNull(v29, "mc.thePlayer");
                            if (v29.func_70694_bm() == null) ** GOTO lbl-1000
                            v30 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                            Intrinsics.checkExpressionValueIsNotNull(v30, "mc.thePlayer");
                            v31 = v30.func_70694_bm();
                            Intrinsics.checkExpressionValueIsNotNull(v31, "mc.thePlayer.heldItem");
                            if (!(v31.func_77973_b() instanceof ItemBlock)) lbl-1000:
                            // 2 sources

                            {
                                v28 = true;
                            } else {
                                v28 = false;
                            }
                        }
                        if (!v28) break block57;
                    }
                    return;
                }
                this.findBlock(StringsKt.equals(mode, "expand", true) != false && this.towerActivation() == false);
            }
            if (this.targetPlace == null && ((Boolean)this.placeableDelay.get()).booleanValue()) {
                this.delayTimer.reset();
            }
            if (!this.towerActivation()) {
                this.verusState = 0;
                this.towerPlace = null;
                return;
            }
            Scaffold.access$getMc$p$s1046033730().field_71428_T.field_74278_d = ((Number)this.towerTimerValue.get()).floatValue();
            if (eventState != EventState.POST) break block58;
            this.towerPlace = null;
            this.timer.update();
            v32 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(v32, "mc.thePlayer");
            if (v32.func_70694_bm() == null) ** GOTO lbl-1000
            v33 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(v33, "mc.thePlayer");
            v34 = v33.func_70694_bm();
            Intrinsics.checkExpressionValueIsNotNull(v34, "mc.thePlayer.heldItem");
            if (v34.func_77973_b() instanceof ItemBlock) {
                v35 = true;
            } else lbl-1000:
            // 2 sources

            {
                v35 = isHeldItemBlock = false;
            }
            if (InventoryUtils.findAutoBlockBlock() != -1 || isHeldItemBlock) {
                this.launchY = (int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                if (StringsKt.equals((String)this.towerModeValue.get(), "verus", true) || BlockUtils.getBlock(new BlockPos(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u + (double)2, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v)) instanceof BlockAir) {
                    this.move(event);
                }
                blockPos = new BlockPos(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.0, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                v36 = Scaffold.access$getMc$p$s1046033730().field_71441_e.func_180495_p(blockPos);
                Intrinsics.checkExpressionValueIsNotNull(v36, "mc.theWorld.getBlockState(blockPos)");
                if (v36.func_177230_c() instanceof BlockAir && this.search(blockPos, true, true) && ((Boolean)this.rotationsValue.get()).booleanValue() && (vecRotation = RotationUtils.faceBlock(blockPos)) != null) {
                    RotationUtils.setTargetRotation(RotationUtils.limitAngleChange(RotationUtils.serverRotation, vecRotation.getRotation(), RandomUtils.nextFloat(((Number)this.minTurnSpeed.get()).floatValue(), ((Number)this.maxTurnSpeed.get()).floatValue())));
                    v37 = this.towerPlace;
                    if (v37 == null) {
                        Intrinsics.throwNpe();
                    }
                    v37.setVec3(vecRotation.getVec());
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    private final void findBlock(boolean expand) {
        block21: {
            block22: {
                block18: {
                    block19: {
                        block20: {
                            block17: {
                                if (!this.shouldGoDown) break block17;
                                v0 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u == (double)((int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u) + 0.5 ? new BlockPos(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 0.6, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v) : new BlockPos(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 0.6, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v).func_177977_b();
                                break block18;
                            }
                            if (this.towerActivation()) ** GOTO lbl-1000
                            if (((Boolean)this.sameYValue.get()).booleanValue()) break block19;
                            if (((Boolean)this.autoJumpValue.get()).booleanValue()) break block20;
                            if (!((Boolean)this.smartSpeedValue.get()).booleanValue()) ** GOTO lbl-1000
                            v1 = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                            if (v1 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!v1.getState()) ** GOTO lbl-1000
                        }
                        if (GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A)) ** GOTO lbl-1000
                    }
                    if ((double)this.launchY <= Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u) {
                        v0 = new BlockPos(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, (double)(this.launchY - 1), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                    } else lbl-1000:
                    // 5 sources

                    {
                        v0 = blockPosition = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u == (double)((int)Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u) + 0.5 ? new BlockPos((Entity)Scaffold.access$getMc$p$s1046033730().field_71439_g) : new BlockPos(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v).func_177977_b();
                    }
                }
                if (expand) break block21;
                if (!BlockUtils.isReplaceable(blockPosition)) break block22;
                v2 = blockPosition;
                Intrinsics.checkExpressionValueIsNotNull(v2, "blockPosition");
                if (!this.search(v2, this.shouldGoDown == false, false)) break block21;
            }
            return;
        }
        if (expand) {
            yaw = Math.toRadians(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z);
            if (((Boolean)this.omniDirectionalExpand.get()).booleanValue()) {
                v3 = (int)Math.round(-Math.sin(yaw));
            } else {
                v4 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(v4, "mc.thePlayer");
                v5 = v4.func_174811_aO();
                Intrinsics.checkExpressionValueIsNotNull(v5, "mc.thePlayer.horizontalFacing");
                v6 = v5.func_176730_m();
                Intrinsics.checkExpressionValueIsNotNull(v6, "mc.thePlayer.horizontalFacing.directionVec");
                v3 = x = v6.func_177958_n();
            }
            if (((Boolean)this.omniDirectionalExpand.get()).booleanValue()) {
                v7 = (int)Math.round(Math.cos(yaw));
            } else {
                v8 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(v8, "mc.thePlayer");
                v9 = v8.func_174811_aO();
                Intrinsics.checkExpressionValueIsNotNull(v9, "mc.thePlayer.horizontalFacing");
                v10 = v9.func_176730_m();
                Intrinsics.checkExpressionValueIsNotNull(v10, "mc.thePlayer.horizontalFacing.directionVec");
                v7 = v10.func_177952_p();
            }
            z = v7;
            var7_9 = 0;
            var8_10 = ((Number)this.expandLengthValue.get()).intValue();
            while (var7_9 < var8_10) {
                v11 = blockPosition.func_177982_a(x * i, 0, z * i);
                Intrinsics.checkExpressionValueIsNotNull(v11, "blockPosition.add(x * i, 0, z * i)");
                if (this.search(v11, false, false)) {
                    return;
                }
                ++i;
            }
        } else {
            yaw = -1;
            var4_11 = 1;
            while (yaw <= var4_11) {
                x = -1;
                var6_8 = 1;
                while (x <= var6_8) {
                    v12 = blockPosition.func_177982_a((int)x, 0, (int)z);
                    Intrinsics.checkExpressionValueIsNotNull(v12, "blockPosition.add(x, 0, z)");
                    if (this.search(v12, this.shouldGoDown == false, false)) {
                        return;
                    }
                    ++z;
                }
                ++x;
            }
        }
    }

    private final void place(boolean towerActive) {
        ItemStack itemStack;
        int blockSlot;
        block35: {
            block36: {
                block34: {
                    block33: {
                        block29: {
                            block30: {
                                block31: {
                                    block32: {
                                        if ((towerActive ? this.towerPlace : this.targetPlace) == null) {
                                            if (((Boolean)this.placeableDelay.get()).booleanValue()) {
                                                this.delayTimer.reset();
                                            }
                                            return;
                                        }
                                        if (this.towerActivation()) break block29;
                                        if (!this.delayTimer.hasTimePassed(this.delay)) break block30;
                                        if (((Boolean)this.sameYValue.get()).booleanValue()) break block31;
                                        if (((Boolean)this.autoJumpValue.get()).booleanValue()) break block32;
                                        if (!((Boolean)this.smartSpeedValue.get()).booleanValue()) break block29;
                                        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                                        if (module == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        if (!module.getState()) break block29;
                                    }
                                    if (GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A)) break block29;
                                }
                                PlaceInfo placeInfo = towerActive ? this.towerPlace : this.targetPlace;
                                if (placeInfo == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (this.launchY - 1 == (int)placeInfo.getVec3().field_72448_b) break block29;
                            }
                            return;
                        }
                        blockSlot = -1;
                        EntityPlayerSP entityPlayerSP = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                        itemStack = entityPlayerSP.func_70694_bm();
                        EntityPlayerSP entityPlayerSP2 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                        if (entityPlayerSP2.func_70694_bm() == null) break block33;
                        EntityPlayerSP entityPlayerSP3 = Scaffold.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                        ItemStack itemStack2 = entityPlayerSP3.func_70694_bm();
                        Intrinsics.checkExpressionValueIsNotNull(itemStack2, "mc.thePlayer.heldItem");
                        if (itemStack2.func_77973_b() instanceof ItemBlock) break block34;
                    }
                    if (StringsKt.equals((String)this.autoBlockMode.get(), "Off", true)) {
                        return;
                    }
                    blockSlot = InventoryUtils.findAutoBlockBlock();
                    if (blockSlot == -1) {
                        return;
                    }
                    if (StringsKt.equals((String)this.autoBlockMode.get(), "Switch", true)) {
                        Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c = blockSlot - 36;
                        Scaffold.access$getMc$p$s1046033730().field_71442_b.func_78765_e();
                    }
                    if (StringsKt.equals((String)this.autoBlockMode.get(), "Spoof", true)) {
                        Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(blockSlot - 36));
                        Slot slot = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a(blockSlot);
                        Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(blockSlot)");
                        itemStack = slot.func_75211_c();
                    }
                    if (StringsKt.equals((String)this.autoBlockMode.get(), "LiteSpoof", true)) {
                        Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(blockSlot - 36));
                        Slot slot = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a(blockSlot);
                        Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(blockSlot)");
                        itemStack = slot.func_75211_c();
                    }
                }
                if (itemStack == null || itemStack.func_77973_b() == null || !(itemStack.func_77973_b() instanceof ItemBlock)) break block35;
                Item item = itemStack.func_77973_b();
                if (item == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
                }
                Block block = ((ItemBlock)item).func_179223_d();
                if (InventoryUtils.BLOCK_BLACKLIST.contains(block)) break block36;
                Block block2 = block;
                Intrinsics.checkExpressionValueIsNotNull(block2, "block");
                if (block2.func_149686_d() && itemStack.field_77994_a > 0) break block35;
            }
            return;
        }
        PlayerControllerMP playerControllerMP = Scaffold.access$getMc$p$s1046033730().field_71442_b;
        EntityPlayerSP entityPlayerSP = Scaffold.access$getMc$p$s1046033730().field_71439_g;
        WorldClient worldClient = Scaffold.access$getMc$p$s1046033730().field_71441_e;
        PlaceInfo placeInfo = towerActive ? this.towerPlace : this.targetPlace;
        if (placeInfo == null) {
            Intrinsics.throwNpe();
        }
        BlockPos blockPos = placeInfo.getBlockPos();
        PlaceInfo placeInfo2 = towerActive ? this.towerPlace : this.targetPlace;
        if (placeInfo2 == null) {
            Intrinsics.throwNpe();
        }
        EnumFacing enumFacing = placeInfo2.getEnumFacing();
        PlaceInfo placeInfo3 = towerActive ? this.towerPlace : this.targetPlace;
        if (placeInfo3 == null) {
            Intrinsics.throwNpe();
        }
        if (playerControllerMP.func_178890_a(entityPlayerSP, worldClient, itemStack, blockPos, enumFacing, placeInfo3.getVec3())) {
            this.delayTimer.reset();
            long l = this.delay = (Boolean)this.placeableDelay.get() == false ? 0L : TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
            if (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                float modifier = ((Number)this.speedModifierValue.get()).floatValue();
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= (double)modifier;
                Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= (double)modifier;
            }
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                Scaffold.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
            } else {
                PacketUtils.sendPacketNoEvent((Packet)new C0APacketAnimation());
            }
        }
        if (towerActive) {
            this.towerPlace = null;
        } else {
            this.targetPlace = null;
        }
        if (blockSlot >= 0 && StringsKt.equals((String)this.autoBlockMode.get(), "Spoof", true)) {
            Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
        }
    }

    @Override
    public void onDisable() {
        if (Scaffold.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        if (!GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74311_E)) {
            Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74311_E.field_74513_e = false;
            if (this.eagleSneaking) {
                Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Scaffold.access$getMc$p$s1046033730().field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        if (StringsKt.equals((String)this.sprintModeValue.get(), "silent", true)) {
            EntityPlayerSP entityPlayerSP = Scaffold.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_70051_ag()) {
                PacketUtils.sendPacketNoEvent((Packet)new C0BPacketEntityAction((Entity)Scaffold.access$getMc$p$s1046033730().field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                PacketUtils.sendPacketNoEvent((Packet)new C0BPacketEntityAction((Entity)Scaffold.access$getMc$p$s1046033730().field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
            }
        }
        if (!GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z)) {
            Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x)) {
            Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x.field_74513_e = false;
        }
        this.lockRotation = null;
        this.lookupRotation = null;
        Scaffold.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
        this.shouldGoDown = false;
        this.faceBlock = false;
        if (this.lastSlot != Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c && StringsKt.equals((String)this.autoBlockMode.get(), "switch", true)) {
            Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c = this.lastSlot;
            Scaffold.access$getMc$p$s1046033730().field_71442_b.func_78765_e();
        }
        if (this.slot != Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c && (StringsKt.equals((String)this.autoBlockMode.get(), "spoof", true) || StringsKt.equals((String)this.autoBlockMode.get(), "litespoof", true)) || this.slot != Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c) {
            Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
        }
        if (((Boolean)this.autof5.get()).booleanValue()) {
            Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74320_O = 0;
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!((Boolean)this.safeWalkValue.get()).booleanValue() || this.shouldGoDown) {
            return;
        }
        if (((Boolean)this.airSafeValue.get()).booleanValue() || Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            event.setSafeWalk(true);
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.towerActivation()) {
            event.cancelEvent();
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender2D(@Nullable Render2DEvent event) {
        block12: {
            this.progress = (float)(System.currentTimeMillis() - this.lastMS) / 100.0f;
            if (this.progress >= (float)true) {
                this.progress = 1.0f;
            }
            counterMode = (String)this.counterDisplayValue.get();
            scaledResolution = new ScaledResolution(Scaffold.access$getMc$p$s1046033730());
            info = String.valueOf(this.getBlocksAmount()) + " Blocks";
            infoWidth = Fonts.font40.func_78256_a(info);
            infoWidth2 = Fonts.minecraftFont.func_78256_a(String.valueOf(this.getBlocksAmount()) + "");
            if (StringsKt.equals(counterMode, "exhibition", true)) {
                Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2 - 1), (float)(scaledResolution.func_78328_b() / 2 - 36), -16777216, false);
                Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2 + 1), (float)(scaledResolution.func_78328_b() / 2 - 36), -16777216, false);
                Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 35), -16777216, false);
                Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 37), -16777216, false);
                Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 36), -16711936, false);
            }
            if (!StringsKt.equals(counterMode, "advanced", true)) break block12;
            if (this.slot < 0 || this.slot >= 9 || Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.slot] == null) ** GOTO lbl-1000
            v0 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.slot];
            Intrinsics.checkExpressionValueIsNotNull(v0, "mc.thePlayer.inventory.mainInventory[slot]");
            if (v0.func_77973_b() == null) ** GOTO lbl-1000
            v1 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.slot];
            Intrinsics.checkExpressionValueIsNotNull(v1, "mc.thePlayer.inventory.mainInventory[slot]");
            if (v1.func_77973_b() instanceof ItemBlock) {
                v2 = true;
            } else lbl-1000:
            // 3 sources

            {
                v2 = canRenderStack = false;
            }
            if (canRenderStack) {
                RenderUtils.drawRect(scaledResolution.func_78326_a() / 2 - infoWidth / 2 - 4, scaledResolution.func_78328_b() / 2 - 26, scaledResolution.func_78326_a() / 2 + infoWidth / 2 + 4, scaledResolution.func_78328_b() / 2 - 5, -1610612736);
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)(scaledResolution.func_78326_a() / 2 - 8), (float)(scaledResolution.func_78328_b() / 2 - 25), (float)(scaledResolution.func_78326_a() / 2 - 8));
                v3 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.slot];
                Intrinsics.checkExpressionValueIsNotNull(v3, "mc.thePlayer.inventory.mainInventory[slot]");
                this.renderItemStack(v3, 0, 0);
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179117_G();
            Fonts.font40.drawCenteredString(info, scaledResolution.func_78326_a() / 2, scaledResolution.func_78328_b() / 2 - 36, -1);
        }
        if (StringsKt.equals(counterMode, "sigma", true)) {
            GlStateManager.func_179109_b((float)0.0f, (float)(-14.0f - this.progress * 4.0f), (float)0.0f);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glColor4f((float)0.15f, (float)0.15f, (float)0.15f, (float)this.progress);
            GL11.glBegin((int)6);
            GL11.glEnd();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            GlStateManager.func_179117_G();
            Fonts.font37.drawCenteredString(info, (float)(scaledResolution.func_78326_a() / 2) + 0.1f, scaledResolution.func_78328_b() - 70, new Color(1.0f, 1.0f, 1.0f, this.progress).getRGB(), true);
            GlStateManager.func_179109_b((float)0.0f, (float)(14.0f + this.progress * 4.0f), (float)0.0f);
        }
        if (StringsKt.equals(counterMode, "novoline", true)) {
            if (this.slot >= 0 && this.slot < 9 && Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.slot] != null) {
                v4 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.slot];
                Intrinsics.checkExpressionValueIsNotNull(v4, "mc.thePlayer.inventory.mainInventory[slot]");
                if (v4.func_77973_b() != null) {
                    v5 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.slot];
                    Intrinsics.checkExpressionValueIsNotNull(v5, "mc.thePlayer.inventory.mainInventory[slot]");
                    if (v5.func_77973_b() instanceof ItemBlock) {
                        GlStateManager.func_179094_E();
                        GlStateManager.func_179109_b((float)(scaledResolution.func_78326_a() / 2 - 22), (float)(scaledResolution.func_78328_b() / 2 + 16), (float)(scaledResolution.func_78326_a() / 2 - 22));
                        v6 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.slot];
                        Intrinsics.checkExpressionValueIsNotNull(v6, "mc.thePlayer.inventory.mainInventory[slot]");
                        this.renderItemStack(v6, 0, 0);
                        GlStateManager.func_179121_F();
                    }
                }
            }
            GlStateManager.func_179117_G();
            Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + " Blocks", (float)(scaledResolution.func_78326_a() / 2), (float)(scaledResolution.func_78328_b() / 2 + 20), -1, true);
        }
        if (StringsKt.equals(counterMode, "simple", true)) {
            Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + " Blocks", (float)scaledResolution.func_78326_a() / 1.95f, (float)(scaledResolution.func_78328_b() / 2 + 20), -1, true);
        }
        if (StringsKt.equals(counterMode, "dark", true)) {
            Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2 - 1), (float)(scaledResolution.func_78328_b() / 2 - 36), -16777216, false);
            Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2 + 1), (float)(scaledResolution.func_78328_b() / 2 - 36), -16777216, false);
            Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 35), -16777216, false);
            Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 37), -16777216, false);
            Fonts.minecraftFont.func_175065_a(String.valueOf(this.getBlocksAmount()) + "", (float)(scaledResolution.func_78326_a() / 2 - infoWidth2 / 2), (float)(scaledResolution.func_78328_b() / 2 - 36), -65281, false);
        }
    }

    private final void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179091_B();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderHelper.func_74520_c();
        Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_175599_af().func_180450_b(stack, x, y);
        Minecraft minecraft2 = Scaffold.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
        minecraft2.func_175599_af().func_175030_a(Scaffold.access$getMc$p$s1046033730().field_71466_p, stack, x, y);
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public void onRender3D(@NotNull Render3DEvent event) {
        int n;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!((Boolean)this.markValue.get()).booleanValue()) {
            return;
        }
        int n2 = 0;
        int n3 = n = StringsKt.equals((String)this.modeValue.get(), "Expand", true) && !this.towerActivation() ? ((Number)this.expandLengthValue.get()).intValue() + 1 : 2;
        while (n2 < n) {
            void i;
            double x = 0.0;
            double size = 0.0;
            double y = 0.0;
            double boundindY = 0.0;
            double z = 0.0;
            Object convertedPoints = null;
            double xd = 0.0;
            double d = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70142_S + (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t - Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70142_S) * (double)event.getPartialTicks();
            Minecraft minecraft = Scaffold.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            double posX = d - minecraft.func_175598_ae().field_78725_b;
            double d2 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70137_T + (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70163_u - Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70137_T) * (double)event.getPartialTicks();
            Minecraft minecraft2 = Scaffold.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            double posY = d2 - minecraft2.func_175598_ae().field_78726_c;
            double d3 = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70136_U + (Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v - Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70136_U) * (double)event.getPartialTicks();
            Minecraft minecraft3 = Scaffold.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
            double posZ = d3 - minecraft3.func_175598_ae().field_78723_d;
            ESPUtils.cylinder((Entity)Scaffold.access$getMc$p$s1046033730().field_71439_g, posX, posY, posZ, 1.0, 64);
            ESPUtils.shadow((Entity)Scaffold.access$getMc$p$s1046033730().field_71439_g, posX, posY, posZ, 1.0, 64);
            ++i;
        }
    }

    /*
     * WARNING - void declaration
     */
    private final boolean search(BlockPos blockPosition, boolean checks, boolean towerActive) {
        this.faceBlock = false;
        if (!BlockUtils.isReplaceable(blockPosition)) {
            return false;
        }
        boolean staticYawMode = StringsKt.equals((String)this.rotationLookupValue.get(), "AAC", true) || StringsKt.equals((String)this.rotationLookupValue.get(), "same", true) && (StringsKt.equals((String)this.rotationModeValue.get(), "AAC", true) || StringsKt.contains$default((CharSequence)this.rotationModeValue.get(), "Static", false, 2, null) && !StringsKt.equals((String)this.rotationModeValue.get(), "static3", true));
        double d = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
        EntityPlayerSP entityPlayerSP = Scaffold.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        Vec3 eyesPos = new Vec3(d, entityPlayerSP.func_174813_aQ().field_72338_b + (double)Scaffold.access$getMc$p$s1046033730().field_71439_g.func_70047_e(), Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
        PlaceRotation placeRotation = null;
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = blockPosition.func_177972_a(side);
            if (!BlockUtils.canBeClicked(neighbor)) continue;
            Vec3 dirVec = new Vec3(side.func_176730_m());
            for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
                for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                    double zSearch = 0.1;
                    while (zSearch < 0.9) {
                        int n;
                        Vec3 posVec = new Vec3((Vec3i)blockPosition).func_72441_c(xSearch, ySearch, zSearch);
                        double distanceSqPosVec = eyesPos.func_72436_e(posVec);
                        Vec3 hitVec = posVec.func_178787_e(new Vec3(dirVec.field_72450_a * 0.5, dirVec.field_72448_b * 0.5, dirVec.field_72449_c * 0.5));
                        if (checks && (eyesPos.func_72436_e(hitVec) > 18.0 || distanceSqPosVec > eyesPos.func_72436_e(posVec.func_178787_e(dirVec)) || Scaffold.access$getMc$p$s1046033730().field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) != null)) {
                            zSearch += 0.1;
                            continue;
                        }
                        int n2 = 0;
                        int n3 = n = staticYawMode ? 2 : 1;
                        while (n2 < n) {
                            Rotation rotation;
                            void i;
                            block70: {
                                block71: {
                                    block68: {
                                        block69: {
                                            block66: {
                                                block67: {
                                                    block64: {
                                                        block65: {
                                                            block62: {
                                                                block63: {
                                                                    double diffX = staticYawMode && i == false ? 0.0 : hitVec.field_72450_a - eyesPos.field_72450_a;
                                                                    double diffY = hitVec.field_72448_b - eyesPos.field_72448_b;
                                                                    double diffZ = staticYawMode && i == true ? 0.0 : hitVec.field_72449_c - eyesPos.field_72449_c;
                                                                    double diffXZ = MathHelper.func_76133_a((double)(diffX * diffX + diffZ * diffZ));
                                                                    this.lookupRotation = rotation = new Rotation(MathHelper.func_76142_g((float)((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f)), MathHelper.func_76142_g((float)(-((float)Math.toDegrees(Math.atan2(diffY, diffXZ))))));
                                                                    if (!StringsKt.equals((String)this.rotationModeValue.get(), "static", true)) break block62;
                                                                    if (((Boolean)this.keepRotOnJumpValue.get()).booleanValue()) break block63;
                                                                    KeyBinding keyBinding = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
                                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
                                                                    if (keyBinding.func_151470_d()) break block62;
                                                                }
                                                                rotation = new Rotation(MovementUtils.getScaffoldRotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z, Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70702_br), ((Number)this.staticPitchValue.get()).floatValue());
                                                            }
                                                            if (!StringsKt.equals((String)this.rotationModeValue.get(), "watchdog", true)) break block64;
                                                            if (((Boolean)this.keepRotOnJumpValue.get()).booleanValue()) break block65;
                                                            KeyBinding keyBinding = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
                                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
                                                            if (keyBinding.func_151470_d()) break block64;
                                                        }
                                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)180, 84.0f);
                                                        KeyBinding keyBinding = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                                        Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindForward");
                                                        if (!keyBinding.func_151470_d()) {
                                                            KeyBinding keyBinding2 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding2, "mc.gameSettings.keyBindRight");
                                                            if (!keyBinding2.func_151470_d()) {
                                                                KeyBinding keyBinding3 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                                                Intrinsics.checkExpressionValueIsNotNull(keyBinding3, "mc.gameSettings.keyBindLeft");
                                                                if (!keyBinding3.func_151470_d()) {
                                                                    KeyBinding keyBinding4 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding4, "mc.gameSettings.keyBindBack");
                                                                    if (keyBinding4.func_151470_d()) {
                                                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z, 84.0f);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        KeyBinding keyBinding5 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                                        Intrinsics.checkExpressionValueIsNotNull(keyBinding5, "mc.gameSettings.keyBindForward");
                                                        if (keyBinding5.func_151470_d()) {
                                                            KeyBinding keyBinding6 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding6, "mc.gameSettings.keyBindRight");
                                                            if (keyBinding6.func_151470_d()) {
                                                                KeyBinding keyBinding7 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                                                Intrinsics.checkExpressionValueIsNotNull(keyBinding7, "mc.gameSettings.keyBindLeft");
                                                                if (!keyBinding7.func_151470_d()) {
                                                                    KeyBinding keyBinding8 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding8, "mc.gameSettings.keyBindBack");
                                                                    if (!keyBinding8.func_151470_d()) {
                                                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)135, 84.0f);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        KeyBinding keyBinding9 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                                        Intrinsics.checkExpressionValueIsNotNull(keyBinding9, "mc.gameSettings.keyBindForward");
                                                        if (!keyBinding9.func_151470_d()) {
                                                            KeyBinding keyBinding10 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding10, "mc.gameSettings.keyBindRight");
                                                            if (keyBinding10.func_151470_d()) {
                                                                KeyBinding keyBinding11 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                                                Intrinsics.checkExpressionValueIsNotNull(keyBinding11, "mc.gameSettings.keyBindLeft");
                                                                if (!keyBinding11.func_151470_d()) {
                                                                    KeyBinding keyBinding12 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding12, "mc.gameSettings.keyBindBack");
                                                                    if (!keyBinding12.func_151470_d()) {
                                                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)90, 84.0f);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        KeyBinding keyBinding13 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                                        Intrinsics.checkExpressionValueIsNotNull(keyBinding13, "mc.gameSettings.keyBindForward");
                                                        if (!keyBinding13.func_151470_d()) {
                                                            KeyBinding keyBinding14 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding14, "mc.gameSettings.keyBindRight");
                                                            if (keyBinding14.func_151470_d()) {
                                                                KeyBinding keyBinding15 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                                                Intrinsics.checkExpressionValueIsNotNull(keyBinding15, "mc.gameSettings.keyBindLeft");
                                                                if (!keyBinding15.func_151470_d()) {
                                                                    KeyBinding keyBinding16 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding16, "mc.gameSettings.keyBindBack");
                                                                    if (keyBinding16.func_151470_d()) {
                                                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)45, 84.0f);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        KeyBinding keyBinding17 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                                        Intrinsics.checkExpressionValueIsNotNull(keyBinding17, "mc.gameSettings.keyBindForward");
                                                        if (keyBinding17.func_151470_d()) {
                                                            KeyBinding keyBinding18 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding18, "mc.gameSettings.keyBindRight");
                                                            if (!keyBinding18.func_151470_d()) {
                                                                KeyBinding keyBinding19 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                                                Intrinsics.checkExpressionValueIsNotNull(keyBinding19, "mc.gameSettings.keyBindLeft");
                                                                if (keyBinding19.func_151470_d()) {
                                                                    KeyBinding keyBinding20 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding20, "mc.gameSettings.keyBindBack");
                                                                    if (!keyBinding20.func_151470_d()) {
                                                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)225, 84.0f);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        KeyBinding keyBinding21 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                                        Intrinsics.checkExpressionValueIsNotNull(keyBinding21, "mc.gameSettings.keyBindForward");
                                                        if (!keyBinding21.func_151470_d()) {
                                                            KeyBinding keyBinding22 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding22, "mc.gameSettings.keyBindRight");
                                                            if (!keyBinding22.func_151470_d()) {
                                                                KeyBinding keyBinding23 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                                                Intrinsics.checkExpressionValueIsNotNull(keyBinding23, "mc.gameSettings.keyBindLeft");
                                                                if (keyBinding23.func_151470_d()) {
                                                                    KeyBinding keyBinding24 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding24, "mc.gameSettings.keyBindBack");
                                                                    if (!keyBinding24.func_151470_d()) {
                                                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)270, 84.0f);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        KeyBinding keyBinding25 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
                                                        Intrinsics.checkExpressionValueIsNotNull(keyBinding25, "mc.gameSettings.keyBindForward");
                                                        if (!keyBinding25.func_151470_d()) {
                                                            KeyBinding keyBinding26 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
                                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding26, "mc.gameSettings.keyBindRight");
                                                            if (!keyBinding26.func_151470_d()) {
                                                                KeyBinding keyBinding27 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
                                                                Intrinsics.checkExpressionValueIsNotNull(keyBinding27, "mc.gameSettings.keyBindLeft");
                                                                if (keyBinding27.func_151470_d()) {
                                                                    KeyBinding keyBinding28 = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74368_y;
                                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding28, "mc.gameSettings.keyBindBack");
                                                                    if (keyBinding28.func_151470_d()) {
                                                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z - (float)315, 84.0f);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (!StringsKt.equals((String)this.rotationModeValue.get(), "static2", true) && !StringsKt.equals((String)this.rotationModeValue.get(), "static3", true)) break block66;
                                                    if (((Boolean)this.keepRotOnJumpValue.get()).booleanValue()) break block67;
                                                    KeyBinding keyBinding = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
                                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
                                                    if (keyBinding.func_151470_d()) break block66;
                                                }
                                                rotation = new Rotation(rotation.getYaw(), ((Number)this.staticPitchValue.get()).floatValue());
                                            }
                                            if (!StringsKt.equals((String)this.rotationModeValue.get(), "custom", true)) break block68;
                                            if (((Boolean)this.keepRotOnJumpValue.get()).booleanValue()) break block69;
                                            KeyBinding keyBinding = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
                                            Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
                                            if (keyBinding.func_151470_d()) break block68;
                                        }
                                        rotation = new Rotation(Scaffold.access$getMc$p$s1046033730().field_71439_g.field_70177_z + ((Number)this.customYawValue.get()).floatValue(), ((Number)this.customPitchValue.get()).floatValue());
                                    }
                                    if (!StringsKt.equals((String)this.rotationModeValue.get(), "spin", true) || this.speenRotation == null) break block70;
                                    if (((Boolean)this.keepRotOnJumpValue.get()).booleanValue()) break block71;
                                    KeyBinding keyBinding = Scaffold.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
                                    Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
                                    if (keyBinding.func_151470_d()) break block70;
                                }
                                Rotation rotation2 = this.speenRotation;
                                if (rotation2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.utils.Rotation");
                                }
                                rotation = rotation2;
                            }
                            Vec3 rotationVector = RotationUtils.getVectorForRotation(StringsKt.equals((String)this.rotationLookupValue.get(), "same", true) ? rotation : this.lookupRotation);
                            Vec3 vector = eyesPos.func_72441_c(rotationVector.field_72450_a * (double)4, rotationVector.field_72448_b * (double)4, rotationVector.field_72449_c * (double)4);
                            MovingObjectPosition obj = Scaffold.access$getMc$p$s1046033730().field_71441_e.func_147447_a(eyesPos, vector, false, false, true);
                            if (obj.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
                                MovingObjectPosition movingObjectPosition = obj;
                                Intrinsics.checkExpressionValueIsNotNull(movingObjectPosition, "obj");
                                if (Intrinsics.areEqual(movingObjectPosition.func_178782_a(), neighbor) && (placeRotation == null || RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation()))) {
                                    BlockPos blockPos = neighbor;
                                    Intrinsics.checkExpressionValueIsNotNull(blockPos, "neighbor");
                                    EnumFacing enumFacing = side.func_176734_d();
                                    Intrinsics.checkExpressionValueIsNotNull(enumFacing, "side.opposite");
                                    Vec3 vec3 = hitVec;
                                    Intrinsics.checkExpressionValueIsNotNull(vec3, "hitVec");
                                    placeRotation = new PlaceRotation(new PlaceInfo(blockPos, enumFacing, vec3), rotation);
                                }
                            }
                            ++i;
                        }
                        zSearch += 0.1;
                    }
                }
            }
        }
        if (placeRotation == null) {
            return false;
        }
        if (((Boolean)this.rotationsValue.get()).booleanValue()) {
            if (((Number)this.minTurnSpeed.get()).floatValue() < (float)180) {
                Rotation rotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, placeRotation.getRotation(), RandomUtils.nextFloat(((Number)this.minTurnSpeed.get()).floatValue(), ((Number)this.maxTurnSpeed.get()).floatValue()));
                Intrinsics.checkExpressionValueIsNotNull(rotation, "RotationUtils.limitAngle\u2026.get())\n                )");
                Rotation limitedRotation = rotation;
                if ((int)((float)10 * MathHelper.func_76142_g((float)limitedRotation.getYaw())) == (int)((float)10 * MathHelper.func_76142_g((float)placeRotation.getRotation().getYaw())) && (int)((float)10 * MathHelper.func_76142_g((float)limitedRotation.getPitch())) == (int)((float)10 * MathHelper.func_76142_g((float)placeRotation.getRotation().getPitch()))) {
                    RotationUtils.setTargetRotation(placeRotation.getRotation(), ((Number)this.keepLengthValue.get()).intValue());
                    this.lockRotation = placeRotation.getRotation();
                    this.faceBlock = true;
                } else {
                    RotationUtils.setTargetRotation(limitedRotation, ((Number)this.keepLengthValue.get()).intValue());
                    this.lockRotation = limitedRotation;
                    this.faceBlock = false;
                }
            } else {
                RotationUtils.setTargetRotation(placeRotation.getRotation(), ((Number)this.keepLengthValue.get()).intValue());
                this.lockRotation = placeRotation.getRotation();
                this.faceBlock = true;
            }
            if (StringsKt.equals((String)this.rotationLookupValue.get(), "same", true)) {
                this.lookupRotation = this.lockRotation;
            }
        }
        if (towerActive) {
            this.towerPlace = placeRotation.getPlaceInfo();
        } else {
            this.targetPlace = placeRotation.getPlaceInfo();
        }
        return true;
    }

    /*
     * WARNING - void declaration
     */
    private final int getBlocksAmount() {
        int amount = 0;
        int n = 36;
        int n2 = 44;
        while (n <= n2) {
            void i;
            Slot slot = Scaffold.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a((int)i);
            Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(i)");
            ItemStack itemStack = slot.func_75211_c();
            if (itemStack != null && itemStack.func_77973_b() instanceof ItemBlock) {
                Item item = itemStack.func_77973_b();
                if (item == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
                }
                Block block = ((ItemBlock)item).func_179223_d();
                if (!InventoryUtils.BLOCK_BLACKLIST.contains(block)) {
                    Block block2 = block;
                    Intrinsics.checkExpressionValueIsNotNull(block2, "block");
                    if (block2.func_149686_d()) {
                        amount += itemStack.field_77994_a;
                    }
                }
            }
            ++i;
        }
        return amount;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ BoolValue access$getTowerEnabled$p(Scaffold $this) {
        return $this.towerEnabled;
    }

    public static final /* synthetic */ ListValue access$getTowerModeValue$p(Scaffold $this) {
        return $this.towerModeValue;
    }

    public static final /* synthetic */ BoolValue access$getStableStopValue$p(Scaffold $this) {
        return $this.stableStopValue;
    }

    public static final /* synthetic */ IntegerValue access$getMinDelayValue$p(Scaffold $this) {
        return $this.minDelayValue;
    }

    public static final /* synthetic */ IntegerValue access$getMaxDelayValue$p(Scaffold $this) {
        return $this.maxDelayValue;
    }

    public static final /* synthetic */ BoolValue access$getCustomSpeedValue$p(Scaffold $this) {
        return $this.customSpeedValue;
    }

    public static final /* synthetic */ BoolValue access$getSafeWalkValue$p(Scaffold $this) {
        return $this.safeWalkValue;
    }

    public static final /* synthetic */ FloatValue access$getMinTurnSpeed$p(Scaffold $this) {
        return $this.minTurnSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMaxTurnSpeed$p(Scaffold $this) {
        return $this.maxTurnSpeed;
    }

    public static final /* synthetic */ BoolValue access$getEagleValue$p(Scaffold $this) {
        return $this.eagleValue;
    }

    public static final /* synthetic */ BoolValue access$getZitterValue$p(Scaffold $this) {
        return $this.zitterValue;
    }

    public static final /* synthetic */ ListValue access$getZitterModeValue$p(Scaffold $this) {
        return $this.zitterModeValue;
    }
}

