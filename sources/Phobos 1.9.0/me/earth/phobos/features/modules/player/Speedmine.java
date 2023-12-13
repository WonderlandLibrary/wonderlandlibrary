package me.earth.phobos.features.modules.player;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.BlockEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.*;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Speedmine
        extends Module {
    private static Speedmine INSTANCE = new Speedmine();
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(10.0f), Float.valueOf(0.0f), Float.valueOf(50.0f)));
    private final Timer timer = new Timer();
    public Setting<Boolean> tweaks = this.register(new Setting<Boolean>("Tweaks", true));
    public Setting<Mode> mode = this.register(new Setting<Object>("Mode", Mode.PACKET, v -> this.tweaks.getValue()));
    public Setting<Boolean> reset = this.register(new Setting<Boolean>("Reset", true));
    public Setting<Float> damage = this.register(new Setting<Object>("Damage", Float.valueOf(0.7f), Float.valueOf(0.0f), Float.valueOf(1.0f), v -> this.mode.getValue() == Mode.DAMAGE && this.tweaks.getValue() != false));
    public Setting<Boolean> noBreakAnim = this.register(new Setting<Boolean>("NoBreakAnim", false));
    public Setting<Boolean> noDelay = this.register(new Setting<Boolean>("NoDelay", false));
    public Setting<Boolean> noSwing = this.register(new Setting<Boolean>("NoSwing", false));
    public Setting<Boolean> noTrace = this.register(new Setting<Boolean>("NoTrace", false));
    public Setting<Boolean> noGapTrace = this.register(new Setting<Object>("NoGapTrace", Boolean.valueOf(false), v -> this.noTrace.getValue()));
    public Setting<Boolean> allow = this.register(new Setting<Boolean>("AllowMultiTask", false));
    public Setting<Boolean> pickaxe = this.register(new Setting<Object>("Pickaxe", Boolean.valueOf(true), v -> this.noTrace.getValue()));
    public Setting<Boolean> doubleBreak = this.register(new Setting<Boolean>("DoubleBreak", false));
    public Setting<Boolean> webSwitch = this.register(new Setting<Boolean>("WebSwitch", false));
    public Setting<Boolean> silentSwitch = this.register(new Setting<Boolean>("SilentSwitch", false));
    public Setting<Boolean> illegal = this.register(new Setting<Boolean>("IllegalMine", false));
    public Setting<Boolean> render = this.register(new Setting<Boolean>("Render", false));
    public Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(false), v -> this.render.getValue()));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.render.getValue() != false));
    public Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), v -> this.render.getValue()));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue() != false && this.render.getValue() != false));
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private boolean isMining = false;
    private BlockPos lastPos = null;
    private EnumFacing lastFacing = null;

    public Speedmine() {
        super("Speedmine", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static Speedmine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Speedmine();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (this.currentPos != null) {
            if (Speedmine.mc.player != null && Speedmine.mc.player.getDistanceSq(this.currentPos) > MathUtil.square(this.range.getValue().floatValue())) {
                this.currentPos = null;
                this.currentBlockState = null;
                return;
            }
            if (Speedmine.mc.player != null && this.silentSwitch.getValue().booleanValue() && this.timer.passedMs((int) (2000.0f * Phobos.serverManager.getTpsFactor())) && this.getPickSlot() != -1) {
                Speedmine.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.getPickSlot()));
            }
            if (!Speedmine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) || Speedmine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                this.currentPos = null;
                this.currentBlockState = null;
            } else if (this.webSwitch.getValue().booleanValue() && this.currentBlockState.getBlock() == Blocks.WEB && Speedmine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
            }
        }
    }

    @Override
    public void onUpdate() {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (this.noDelay.getValue().booleanValue()) {
            Speedmine.mc.playerController.blockHitDelay = 0;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && this.noBreakAnim.getValue().booleanValue()) {
            Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
        if (this.reset.getValue().booleanValue() && Speedmine.mc.gameSettings.keyBindUseItem.isKeyDown() && !this.allow.getValue().booleanValue()) {
            Speedmine.mc.playerController.isHittingBlock = false;
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.render.getValue().booleanValue() && this.currentPos != null) {
            Color color = new Color(this.timer.passedMs((int) (2000.0f * Phobos.serverManager.getTpsFactor())) ? 0 : 255, this.timer.passedMs((int) (2000.0f * Phobos.serverManager.getTpsFactor())) ? 255 : 0, 0, 255);
            RenderUtil.drawBoxESP(this.currentPos, color, false, color, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            CPacketPlayerDigging packet;
            if (this.noSwing.getValue().booleanValue() && event.getPacket() instanceof CPacketAnimation) {
                event.setCanceled(true);
            }
            if (this.noBreakAnim.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = event.getPacket()) != null && packet.getPosition() != null) {
                try {
                    for (Entity entity : Speedmine.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(packet.getPosition()))) {
                        if (!(entity instanceof EntityEnderCrystal)) continue;
                        this.showAnimation();
                        return;
                    }
                } catch (Exception exception) {
                    // empty catch block
                }
                if (packet.getAction().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.showAnimation(true, packet.getPosition(), packet.getFacing());
                }
                if (packet.getAction().equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.showAnimation();
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && Speedmine.mc.world.getBlockState(event.pos).getBlock() instanceof BlockEndPortalFrame) {
            Speedmine.mc.world.getBlockState(event.pos).getBlock().setHardness(50.0f);
        }
        if (event.getStage() == 3 && this.reset.getValue().booleanValue() && Speedmine.mc.playerController.curBlockDamageMP > 0.1f) {
            Speedmine.mc.playerController.isHittingBlock = true;
        }
        if (event.getStage() == 4 && this.tweaks.getValue().booleanValue()) {
            BlockPos above;
            if (BlockUtil.canBreak(event.pos)) {
                if (this.reset.getValue().booleanValue()) {
                    Speedmine.mc.playerController.isHittingBlock = false;
                }
                switch (this.mode.getValue()) {
                    case PACKET: {
                        if (this.currentPos == null) {
                            this.currentPos = event.pos;
                            this.currentBlockState = Speedmine.mc.world.getBlockState(this.currentPos);
                            this.timer.reset();
                        }
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        event.setCanceled(true);
                        break;
                    }
                    case DAMAGE: {
                        if (!(Speedmine.mc.playerController.curBlockDamageMP >= this.damage.getValue().floatValue()))
                            break;
                        Speedmine.mc.playerController.curBlockDamageMP = 1.0f;
                        break;
                    }
                    case INSTANT: {
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.playerController.onPlayerDestroyBlock(event.pos);
                        Speedmine.mc.world.setBlockToAir(event.pos);
                    }
                }
            }
            if (this.doubleBreak.getValue().booleanValue() && BlockUtil.canBreak(above = event.pos.add(0, 1, 0)) && Speedmine.mc.player.getDistance(above.getX(), above.getY(), above.getZ()) <= 5.0) {
                Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.playerController.onPlayerDestroyBlock(above);
                Speedmine.mc.world.setBlockToAir(above);
            }
        }
    }

    private int getPickSlot() {
        for (int i = 0; i < 9; ++i) {
            if (Speedmine.mc.player.inventory.getStackInSlot(i).getItem() != Items.DIAMOND_PICKAXE) continue;
            return i;
        }
        return -1;
    }

    private void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }

    public void showAnimation() {
        this.showAnimation(false, null, null);
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public enum Mode {
        PACKET,
        DAMAGE,
        INSTANT

    }
}

