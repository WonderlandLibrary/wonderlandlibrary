package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.configurations.property.object.StringProperty;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static cc.novoline.gui.screen.setting.SettingType.*;

public final class BedBreaker extends AbstractModule {

    private double currentDamage;
    private boolean digging;
    private BlockPos currentPos;

    BlockPos whiteListed = new BlockPos(0, 0, 0);

    @Property("mode")
    private final StringProperty mode = PropertyFactory.createString("Simple").acceptableValues("Simple", "Advanced");
    @Property("radious")
    private final IntProperty radius = PropertyFactory.createInt(4).minimum(3).maximum(5);
    @Property("draw-radius")
    private final BooleanProperty drawRadius = PropertyFactory.createBoolean(false);

    public BedBreaker(@NonNull ModuleManager novoline) {
        super(novoline, "BedBreaker", EnumModuleType.PLAYER, "Breaks bed around you");
        Manager.put(new Setting("BB_MODE", "Mode", COMBOBOX, this, this.mode));
        Manager.put(new Setting("BB_RADIUS", "Break radius", SLIDER, this, this.radius, 1));
        Manager.put(new Setting("AV_BOOST", "Draw radius", CHECKBOX, this, this.drawRadius));
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {
        if (currentPos != null) {
            if (currentDamage == 0) {
                sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, currentPos, EnumFacing.UP));
            }

            Block block = mc.world.getBlockState(currentPos).getBlock();
            currentDamage += block.getPlayerRelativeBlockHardness(this.mc.player, this.mc.player.worldObj, currentPos);

            if (this.currentDamage >= 1.0F) {
                this.digging = false;
                sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.UP));
                mc.playerController.onPlayerDestroyBlock(currentPos, EnumFacing.UP);
                this.currentDamage = 0.0F;
                currentPos = null;
            }

            sendPacket(new C0APacketAnimation());
            this.mc.world.sendBlockBreakProgress(this.mc.player.getEntityID(), currentPos, (int) (this.currentDamage * 10.0F) - 1);
        }
    }

    @EventTarget
    public void onMotionUpdate(MotionUpdateEvent event) {
/*        if (getModule(OptionalClass.class).teleporting) {
            return;
        }*/
        
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (currentPos != null && (!getModule(KillAura.class).isEnabled() || getModule(KillAura.class).getTarget() == null)) {
                if (!bedAround()) {
                    currentPos = null;
                    currentDamage = 0;
                }
                float[] rotations = RotationUtil.getRotations(currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5);
                event.setYaw(rotations[0]);
                event.setPitch(rotations[1]);
                mc.player.updateTool(currentPos);
                mc.player.rotationYawHead = rotations[0];
                mc.player.renderArmPitch = rotations[1];
            } else {
                for (int x = -radius.get(); x < radius.get() + 1; x++) {
                    for (int z = -radius.get(); z < radius.get() + 1; z++) {
                        for (int y = -3; y < 5; y++) {
                            BlockPos pos = new BlockPos(mc.player.posX - x, mc.player.posY + y, mc.player.posZ - z);
                            Block block = mc.world.getBlockState(pos).getBlock();
                            if (!isWhitelisted(pos) && mc.world.getBlockState(pos).getBlock() ==
                                    Blocks.bed && mc.world.getBlockState(pos).getValue(BlockBed.PART) ==
                                    BlockBed.EnumPartType.HEAD) {

                                if (mode.get().equals("Advanced")) {
                                    float[] rotations = RotationUtil.getRotations(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

                                    Vec3 myPos = new Vec3(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
                                    Vec3 rotationsVector = getVectorForRotation(rotations[1], rotations[0]);
                                    Vec3 multiplied = myPos.addVector(rotationsVector.xCoord * radius.get(), rotationsVector.yCoord * radius.get(), rotationsVector.zCoord * radius.get());

                                    MovingObjectPosition movingObjectPosition = mc.world.rayTraceBlocks(myPos, multiplied, false, false, false);

                                    currentPos = movingObjectPosition.getBlockPos();
                                    currentDamage = 0;
                                    if (mc.world.getBlockState(currentPos).getBlock() == Blocks.bed) {
                                        break;
                                    }
                                } else {
                                    currentPos = pos;
                                    currentDamage = 0;
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    protected final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(Math.toRadians(-yaw) - (float) Math.PI);
        float f1 = MathHelper.sin(Math.toRadians(-yaw) - (float) Math.PI);
        float f2 = -MathHelper.cos(Math.toRadians(-pitch));
        float f3 = MathHelper.sin(Math.toRadians(-pitch));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    public Vec3 getPositionEyes(float partialTicks) {
        return new Vec3(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (drawRadius.get()) {
            RenderUtils.pre3D();
            GL11.glLineWidth(6);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (double d = 0; d < Math.PI * 2; d += Math.PI * 2 / 26) {
                float alpha = (new Color(255, 255, 255, 150).getRGB() >> 24 & 0xFF) / 255.0F;
                float red = (new Color(255, 255, 255, 150).getRGB() >> 16 & 0xFF) / 255.0F;
                float green = (new Color(255, 255, 255, 150).getRGB() >> 8 & 0xFF) / 255.0F;
                float blue = (new Color(255, 255, 255, 150).getRGB() & 0xFF) / 255.0F;
                GL11.glColor4f(red, green, blue, alpha);
                double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * event.getPartialTicks() + Math.sin(d) * radius.get() - mc.getRenderManager().renderPosX, // @off
                        y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY,
                        z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * event.getPartialTicks() + Math.cos(d) * radius.get() - mc.getRenderManager().renderPosZ; // @on
                GL11.glVertex3d(x, y, z);
            }
            GL11.glEnd();
            RenderUtils.post3D();
        }
        if (currentPos != null) {
            RenderUtils.drawSolidBlockESP(new BlockPos(currentPos), new Color(255, 255, 255, 50).getRGB());
        }
    }

    private boolean bedAround() {
        for (int x = -radius.get(); x < radius.get() + 1; x++) {
            for (int z = -radius.get(); z < radius.get() + 1; z++) {
                for (int y = -3; y < 5; y++) {
                    BlockPos pos = new BlockPos(mc.player.posX - x, mc.player.posY + y, mc.player.posZ - z);
                    Block block = mc.world.getBlockState(pos).getBlock();
                    if (!isWhitelisted(pos) && mc.world.getBlockState(pos).getBlock() == Blocks.bed && mc.world.getBlockState(pos).getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public BlockPos getWhiteListed() {
        return whiteListed;
    }

    public void setWhiteListed(BlockPos whiteListed) {
        this.whiteListed = whiteListed;
    }

    private boolean isWhitelisted(@NotNull BlockPos pos) {
        return pos.getX() == whiteListed.getX() && pos.getY() == whiteListed.getY() && pos.getZ() == whiteListed.getZ();
    }
}
