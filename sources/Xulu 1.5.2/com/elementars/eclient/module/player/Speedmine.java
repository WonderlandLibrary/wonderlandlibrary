// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import com.elementars.eclient.event.events.EventPlayerDamageBlock;
import com.elementars.eclient.event.events.EventClickBlock;
import com.elementars.eclient.event.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketAnimation;
import com.elementars.eclient.event.Event;
import com.elementars.eclient.event.events.EventSendPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import com.elementars.eclient.module.combat.MiddleClickPearl;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.init.Blocks;
import com.elementars.eclient.module.Category;
import net.minecraft.util.EnumFacing;
import com.elementars.eclient.util.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Speedmine extends Module
{
    public Value<Boolean> tweaks;
    public Value<Mode> mode;
    public Value<Boolean> reset;
    public Value<Float> damage;
    public Value<Boolean> noBreakAnim;
    public Value<Boolean> noDelay;
    public Value<Boolean> noSwing;
    public Value<Boolean> noTrace;
    public Value<Boolean> allow;
    public Value<Boolean> pickaxe;
    public Value<Boolean> doubleBreak;
    public Value<Boolean> webSwitch;
    public Value<Boolean> render;
    public Value<Boolean> box;
    public Value<Boolean> outline;
    private final Value<Integer> boxAlpha;
    private final Value<Float> lineWidth;
    private static Speedmine INSTANCE;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private final Timer timer;
    private boolean isMining;
    private BlockPos lastPos;
    private EnumFacing lastFacing;
    
    public Speedmine() {
        super("Speedmine", "Speeds up mining.", 0, Category.PLAYER, true);
        this.tweaks = this.register(new Value<Boolean>("Tweaks", this, true));
        this.mode = this.register(new Value<Mode>("Mode", this, Mode.PACKET, Mode.values())).visibleWhen(mode1 -> this.tweaks.getValue());
        this.reset = this.register(new Value<Boolean>("Reset", this, true));
        this.damage = this.register(new Value<Float>("Damage", this, 0.7f, 0.0f, 1.0f)).visibleWhen(aFloat -> this.mode.getValue() == Mode.DAMAGE && this.tweaks.getValue());
        this.noBreakAnim = this.register(new Value<Boolean>("NoBreakAnim", this, false));
        this.noDelay = this.register(new Value<Boolean>("NoDelay", this, false));
        this.noSwing = this.register(new Value<Boolean>("NoSwing", this, false));
        this.noTrace = this.register(new Value<Boolean>("NoTrace", this, false));
        this.allow = this.register(new Value<Boolean>("AllowMultiTask", this, false));
        this.pickaxe = this.register(new Value<Boolean>("Pickaxe", this, true)).visibleWhen(aBoolean -> this.noTrace.getValue());
        this.doubleBreak = this.register(new Value<Boolean>("DoubleBreak", this, false));
        this.webSwitch = this.register(new Value<Boolean>("WebSwitch", this, false));
        this.render = this.register(new Value<Boolean>("Render", this, false));
        this.box = this.register(new Value<Boolean>("Box", this, false)).visibleWhen(aBoolean -> this.render.getValue());
        this.outline = this.register(new Value<Boolean>("Outline", this, true)).visibleWhen(aBoolean -> this.render.getValue());
        this.boxAlpha = this.register(new Value<Integer>("BoxAlpha", this, 85, 0, 255)).visibleWhen(integer -> this.box.getValue() && this.render.getValue());
        this.lineWidth = this.register(new Value<Float>("LineWidth", this, 1.0f, 0.1f, 5.0f)).visibleWhen(aFloat -> this.outline.getValue() && this.render.getValue());
        this.timer = new Timer();
        this.isMining = false;
        this.lastPos = null;
        this.lastFacing = null;
        this.setInstance();
    }
    
    private void setInstance() {
        Speedmine.INSTANCE = this;
    }
    
    public static Speedmine getInstance() {
        if (Speedmine.INSTANCE == null) {
            Speedmine.INSTANCE = new Speedmine();
        }
        return Speedmine.INSTANCE;
    }
    
    @Override
    public void onUpdate() {
        if (Speedmine.mc.world == null || Speedmine.mc.player == null) {
            return;
        }
        if (this.currentPos != null) {
            if (!Speedmine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) || Speedmine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                this.currentPos = null;
                this.currentBlockState = null;
            }
            else if (this.webSwitch.getValue() && this.currentBlockState.getBlock() == Blocks.WEB && Speedmine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                MiddleClickPearl.switchToHotbarSlot(ItemSword.class, false);
            }
        }
        if (this.noDelay.getValue()) {
            Speedmine.mc.playerController.blockHitDelay = 0;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && this.noBreakAnim.getValue()) {
            Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
        if (this.reset.getValue() && Speedmine.mc.gameSettings.keyBindUseItem.isKeyDown() && !this.allow.getValue()) {
            Speedmine.mc.playerController.isHittingBlock = false;
        }
    }
    
    @EventTarget
    public void onPacketSend(final EventSendPacket event) {
        if (Speedmine.mc.world == null || Speedmine.mc.player == null) {
            return;
        }
        if (event.getEventState() == Event.State.PRE) {
            if (this.noSwing.getValue() && event.getPacket() instanceof CPacketAnimation) {
                event.setCancelled(true);
            }
            if (this.noBreakAnim.getValue() && event.getPacket() instanceof CPacketPlayerDigging) {
                final CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
                if (packet != null && packet.getPosition() != null) {
                    try {
                        for (final Entity entity : Speedmine.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(packet.getPosition()))) {
                            if (entity instanceof EntityEnderCrystal) {
                                this.showAnimation();
                                return;
                            }
                        }
                    }
                    catch (Exception ex) {}
                    if (packet.getAction().equals((Object)CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                        this.showAnimation(true, packet.getPosition(), packet.getFacing());
                    }
                    if (packet.getAction().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                        this.showAnimation();
                    }
                }
            }
        }
    }
    
    @EventTarget
    public void onClickBlock(final EventClickBlock event) {
        if (Speedmine.mc.player == null || Speedmine.mc.world == null) {
            return;
        }
        if (this.reset.getValue() && Speedmine.mc.playerController.curBlockDamageMP > 0.1f) {
            Speedmine.mc.playerController.isHittingBlock = true;
        }
    }
    
    @EventTarget
    public void onBlockEvent(final EventPlayerDamageBlock event) {
        if (Speedmine.mc.player == null || Speedmine.mc.world == null) {
            return;
        }
        if (this.tweaks.getValue()) {
            if (canBreak(event.getPos())) {
                if (this.reset.getValue()) {
                    Speedmine.mc.playerController.isHittingBlock = false;
                }
                switch (this.mode.getValue()) {
                    case PACKET: {
                        if (this.currentPos == null) {
                            this.currentPos = event.getPos();
                            this.currentBlockState = Speedmine.mc.world.getBlockState(this.currentPos);
                            this.timer.reset();
                        }
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                        Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                        event.setCancelled(true);
                        break;
                    }
                    case DAMAGE: {
                        if (Speedmine.mc.playerController.curBlockDamageMP >= this.damage.getValue()) {
                            Speedmine.mc.playerController.curBlockDamageMP = 1.0f;
                            break;
                        }
                        break;
                    }
                    case INSTANT: {
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                        Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                        Speedmine.mc.playerController.onPlayerDestroyBlock(event.getPos());
                        Speedmine.mc.world.setBlockToAir(event.getPos());
                        break;
                    }
                }
            }
            if (this.doubleBreak.getValue()) {
                final BlockPos above = event.getPos().add(0, 1, 0);
                if (canBreak(above) && Speedmine.mc.player.getDistance((double)above.getX(), (double)above.getY(), (double)above.getZ()) <= 5.0) {
                    Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.getFacing()));
                    Speedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.getFacing()));
                    Speedmine.mc.playerController.onPlayerDestroyBlock(above);
                    Speedmine.mc.world.setBlockToAir(above);
                }
            }
        }
    }
    
    private void showAnimation(final boolean isMining, final BlockPos lastPos, final EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }
    
    public void showAnimation() {
        this.showAnimation(false, null, null);
    }
    
    @Override
    public String getHudInfo() {
        return this.mode.getValue().name();
    }
    
    public static boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = Speedmine.mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, (World)Speedmine.mc.world, pos) != -1.0f;
    }
    
    static {
        Speedmine.INSTANCE = new Speedmine();
    }
    
    public enum Mode
    {
        PACKET, 
        DAMAGE, 
        INSTANT;
    }
}
