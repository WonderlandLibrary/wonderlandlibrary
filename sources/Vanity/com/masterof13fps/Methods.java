package com.masterof13fps;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleManager;
import com.masterof13fps.features.modules.impl.combat.Aura;
import com.masterof13fps.manager.altmanager.AltManager;
import com.masterof13fps.manager.eventmanager.EventManager;
import com.masterof13fps.manager.fontmanager.FontManager;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.settingsmanager.SettingsManager;
import com.masterof13fps.utils.entity.PlayerUtil;
import com.masterof13fps.utils.render.Colors;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.*;
import net.optifine.Reflector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface Methods extends Wrapper {
    Minecraft mc = Minecraft.getInstance();

    default String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date(System.currentTimeMillis());
        return formatter.format(time);
    }

    default float updateRotation(float curRot, float destination, float speed) {
        float f = MathHelper.wrapAngleTo180_float(destination - curRot);

        if (f > speed) {
            f = speed;
        }

        if (f < -speed) {
            f = -speed;
        }

        return curRot + f;
    }

    default Colors getColors() {
        return new Colors();
    }

    default EntityPlayerSP getPlayer() {
        return mc.thePlayer;
    }

    default double getBaseMoveSpeed() {
        return 0.27;
    }

    default PlayerControllerMP getPlayerController() {
        return mc.playerController;
    }

    default WorldClient getWorld() {
        return mc.theWorld;
    }

    default SettingsManager getSettingsManager() {
        return Client.main().setMgr();
    }

    default ModuleManager getModuleManager() {
        return Client.main().modMgr();
    }

    default FontManager getFontManager() {
        return Client.main().fontMgr();
    }

    default Setting getSettingByName(String setting, Module mod) {
        return getSettingsManager().settingByName(setting, mod);
    }

    default EventManager getEventManager() {
        return new EventManager();
    }

    default double getClientVersion() {
        return Client.main().getClientVersion();
    }

    default String getClientName() {
        return Client.main().getClientName();
    }

    default void sendPacket(Packet<? extends INetHandler> packet) {
        try {
            getPlayer().sendQueue.addToSendQueue(packet);
        } catch (NullPointerException ignored) {
        }
    }

    default void reloadClient() {
        Client.main().modMgr().loadModules();
        Client.main().modMgr().loadBinds();
        AltManager.loadAlts();
        setGuiScreen(mc.currentScreen);
    }

    default void setGuiScreen(GuiScreen screen) {
        mc.displayGuiScreen(screen);
    }

    default void sendChatMessage(String message) {
        sendPacket(new C01PacketChatMessage(message));
    }

    default NetHandlerPlayClient getNetHandler() {
        return mc.getNetHandler();
    }

    default String getClientPrefix() {
        return Client.main().getClientPrefix();
    }

    default GameSettings getGameSettings() {
        return mc.gameSettings;
    }

    default double getX() {
        return getPlayer().posX;
    }

    default void setX(double x) {
        getPlayer().posX = x;
    }

    default double getY() {
        return getPlayer().posY;
    }

    default void setY(double y) {
        getPlayer().posY = y;
    }

    default double getZ() {
        return getPlayer().posZ;
    }

    default void setZ(double z) {
        getPlayer().posZ = z;
    }

    default void setMotionX(double x) {
        getPlayer().motionX = x;
    }

    default void setMotionY(double y) {
        getPlayer().motionY = y;
    }

    default void setSpeed(double speed) {
        PlayerUtil.setSpeed(speed);
    }

    default void setMotionZ(double z) {
        getPlayer().motionZ = z;
    }

    default boolean isMoving() {
        return getPlayer().moveForward != 0 || getPlayer().moveStrafing != 0;
    }

    default void setTimerSpeed(float timerSpeed) {
        mc.timer.timerSpeed = timerSpeed;
    }

    default Gson gson() {
        return new Gson();
    }

    default JsonParser jsonParser() {
        return new JsonParser();
    }

    default String getClientChangelog() {
        return "https://pastebin.com/raw/3NiZ3SMC";
    }

    default String getClientCredits() {
        return "https://pastebin.com/raw/u66J4vGm";
    }

    default Entity getCurrentTarget() {
        return Aura.currentTarget;
    }

    default IBlockState getBlock(final BlockPos pos) {
        return mc.theWorld.getBlockState(pos);
    }

    default String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd. MM yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    static Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3(f2 * f3, f4, f * f3);
    }

    default MovingObjectPosition rayTrace(final Entity view, final double blockReachDistance, final float partialTick, final float yaw, final float pitch) {
        final Vec3 vec3 = view.getPositionEyes(1.0f);
        final Vec3 vec4 = getVectorForRotation(pitch, yaw);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return view.worldObj.rayTraceBlocks(vec3, vec5, false, false, true);
    }

    default MovingObjectPosition getMouseOver(final Entity entity, final float yaw, final float pitch, final double range) {
        if (entity != null && mc.theWorld != null) {
            Entity pointedEntity = null;
            mc.pointedEntity = null;
            MovingObjectPosition objectMouseOver = rayTrace(entity, range, 1.0f, yaw, pitch);
            double d1 = range;
            final Vec3 vec3 = entity.getPositionEyes(1.0f);
            final boolean flag = false;
            final boolean flag2 = true;
            if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            final Vec3 vec4 = getVectorForRotation(pitch, yaw);
            final Vec3 vec5 = vec3.addVector(vec4.xCoord * range, vec4.yCoord * range, vec4.zCoord * range);
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List list = getWorld().getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * range, vec4.yCoord * range, vec4.zCoord * range).expand(f, f, f), Predicates.and((Predicate<? super Entity>) EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = (Entity) list.get(i);
                final float f2 = entity2.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f2, f2, f2);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0) {
                        pointedEntity = entity2;
                        vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                        d2 = 0.0;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d3 < d2 || d2 == 0.0) {
                        boolean flag3 = false;
                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag3 = Reflector.callBoolean(entity2, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }
                        if (entity2 == entity.ridingEntity && !flag3) {
                            if (d2 == 0.0) {
                                pointedEntity = entity2;
                                vec6 = movingobjectposition.hitVec;
                            }
                        } else {
                            pointedEntity = entity2;
                            vec6 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }
            if (pointedEntity != null && flag && vec3.distanceTo(vec6) > range) {
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
            }
            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
            }
            return objectMouseOver;
        }
        return null;
    }

    default float[] faceBlock(final BlockPos pos, final boolean scaffoldFix, final float currentYaw, final float currentPitch, final float speed) {
        final double x = pos.getX() + (scaffoldFix ? 0.5 : 0.0) - mc.thePlayer.posX;
        final double y = pos.getY() - (scaffoldFix ? 1.75 : 0.0) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double z = pos.getZ() + (scaffoldFix ? 0.5 : 0.0) - mc.thePlayer.posZ;
        final double calculate = MathHelper.sqrt_double(x * x + z * z);
        final float calcYaw = (float) (MathHelper.func_181159_b(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float calcPitch = (float) (-(MathHelper.func_181159_b(y, calculate) * 180.0 / 3.141592653589793));
        final float finalPitch = (calcPitch >= 90.0f) ? 90.0f : calcPitch;
        float yaw = updateRotation(currentYaw, calcYaw, speed);
        float pitch = updateRotation(currentPitch, finalPitch, speed);
        final float sense = mc.gameSettings.mouseSensitivity * 0.8f + 0.2f;
        final float fix = (float) (Math.pow(sense, 3.0) * 1.5);
        yaw -= yaw % fix;
        pitch -= pitch % fix;
        return new float[]{yaw, pitch};
    }

}
