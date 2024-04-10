package info.sigmaclient.sigma.modules.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.EventManager;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.PremiumModule;
import info.sigmaclient.sigma.modules.combat.Killaura;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.network.play.server.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.CopyOnWriteArrayList;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class FakeLag extends PremiumModule {
    public NumberValue Lrange = new NumberValue("LeaveRange", 6, 0, 10, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue minRange = new NumberValue("MinRange", 0, 0, 6, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue range = new NumberValue("StartRange", 3, 0, 6, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue duration = new NumberValue("MaxDuration", 0.1, 0, 3, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue targetsCount = new NumberValue("MaxTarget", 1, 0, 6, NumberValue.NUMBER_TYPE.INT);

    BooleanValue fakelag = new BooleanValue("Fakelag", true);
    BooleanValue velocity = new BooleanValue("Velocity Clear", true);
    BooleanValue lagClear = new BooleanValue("Lag Clear", true);
    BooleanValue noHurt = new BooleanValue("When your no hurt", true);
    BooleanValue esp = new BooleanValue("ESP", true);


    BooleanValue tickbase = new BooleanValue("Tickbase", false);
    public BooleanValue betterAnimation = new BooleanValue("BetterAnimation", false);
    public NumberValue delay = new NumberValue("Delay", 0.5, 0, 2, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue outRange = new NumberValue("TimerStartRange", 4, 0, 6, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue stopRange = new NumberValue("TimerStopRange", 3, 0, 6, NumberValue.NUMBER_TYPE.FLOAT);
    public NumberValue tick = new NumberValue("Tick", 3, 0, 20, NumberValue.NUMBER_TYPE.FLOAT);

    public FakeLag() {
        super("FakeLag", Category.Misc, "Fake position. (killaura only & better)");
     registerValue(Lrange);
     registerValue(minRange);
     registerValue(range);
     registerValue(duration);
     registerValue(targetsCount);
     registerValue(fakelag);
     registerValue(velocity);
     registerValue(lagClear);
     registerValue(noHurt);
     registerValue(esp);
     registerValue(tickbase);
     registerValue(betterAnimation);
     registerValue(delay);
     registerValue(outRange);
     registerValue(stopRange);
     registerValue(tick);
    }
    public int durationTicks = 0, waitTicks = 0, delayTicks = 0;
    CopyOnWriteArrayList<IPacket<?>> serverPackets = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Entity> storeEntities = new CopyOnWriteArrayList<>();
    Entity targets = null;
    public boolean freezing = false, ignoreUpdate = false;
    public static boolean publicFreeze = false;
    public boolean noEvent = false ,clear = false;
    @Override
    public void onWorldEvent(WorldEvent event) {
        serverPackets.clear();
        storeEntities.clear();
        clear();
        super.onWorldEvent(event);
    }
    public void safely_clear() {
        clear = true;
    }
    public void clear() {
        publicFreeze = false;
        freezing = false;
        ignoreUpdate = false;
        noEvent = true;
        if (mc.getConnection() != null)
            serverPackets.forEach((e) -> {
                    PacketEvent packetEvent = new PacketEvent(e).setRev();
                    EventManager.call(packetEvent);
                try {
                    if (!packetEvent.cancelable)
                        ((IPacket<INetHandler>) e).processPacket(mc.getConnection());
                } catch (Exception ignored) {

                }
            });
        noEvent = false;
        storeEntities.forEach((entity) -> {
            if (entity == null || !entity.isAlive()) return;
            double x = entity.getServerPos().x;
            double y = entity.getServerPos().y;
            double z = entity.getServerPos().z;
            entity.setPosition(x, y, z);
        });
        targets = null;
        storeEntities.clear();
        serverPackets.clear();
        durationTicks = 0;
    }
    @Override
    public void onEnable() {
        publicFreeze = false;
        clear();
        noEvent = false;
        super.onEnable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) {
        if(ignoreUpdate) return;
        if(mc.world == null || mc.player == null) return;
        publicFreeze = false;
        if(waitTicks == 0){
            waitTicks--;
            ignoreUpdate = true;
            for(int i = 0;i<tick.getValue().intValue();i++) {
                mc.world.tickEntities();
            }
            ignoreUpdate = false;
        }
        if(waitTicks > 0){
            waitTicks --;
            publicFreeze = true;
        }else{
        }
        if(delayTicks > 0){
            delayTicks --;
        }
            int countInRange = 0;
            for(Entity entity : mc.world.getLoadedEntityList()){
                if(!(entity instanceof LivingEntity)) continue;
                if(Killaura.isTargetEnable((LivingEntity) entity)){
                    AxisAlignedBB afterBB = entity.getBoundingBox();
                    double afterRange = RotationUtils.nearestRotation(afterBB);
                    if(Killaura.raytrace.isEnable()) {
                        if (afterRange <= Killaura.range.getValue().floatValue())
                            countInRange++;
                    }else{
                        if (afterRange <= Killaura.range.getValue().floatValue())
                            countInRange++;
                    }
                }
            }
            if(countInRange > targetsCount.getValue().intValue()){
                clear();
                return;
            }
            if (freezing) {
                int calcDuration = (int) (duration.getValue().floatValue() * 10);
                durationTicks++;
                if (durationTicks > calcDuration || Killaura.attackTarget == null) {
                    clear();
                    return;
                }
            }
            if (!storeEntities.isEmpty()) {
                for (Entity entity : storeEntities) {
                    double x = entity.getServerPos().x;
                    double y = entity.getServerPos().y;
                    double z = entity.getServerPos().z;
                    AxisAlignedBB entityBB = new AxisAlignedBB(x - 0.4F, y, z - 0.4F, x + 0.4F, y + 1.7F, z + 0.4F);
                    double range = RotationUtils.nearestRotation(entityBB);
                    if (range <= minRange.getValue().floatValue() || range >= Lrange.getValue().floatValue()) {
                        clear();
                        break;
                    }
                }
            }
            if (Killaura.attackTarget != null) {
                if (targets != null && Killaura.attackTarget != targets) {
                    clear();
                }
                AxisAlignedBB afterBB = Killaura.attackTarget.getBoundingBox();
                double afterRange = RotationUtils.nearestRotation(afterBB);

                if (fakelag.isEnable() && (!noHurt.isEnable() || mc.player.hurtTime == 0) && (afterRange >= minRange.getValue().floatValue() && afterRange <= range.getValue().floatValue())) {
                    if (!freezing) {
                        durationTicks = 0;
                        freezing = true;
                    }
                    targets = Killaura.attackTarget;
                    return;
                }else{
                    targets = Killaura.attackTarget;
                    durationTicks = 0;
                }
                if(tickbase.isEnable()){
                    if(afterRange >= outRange.getValue().floatValue() && afterRange < stopRange.getValue().floatValue()){
                        if(delayTicks > 0){
                        }else{
                            waitTicks = tick.getValue().intValue();
                            delayTicks = (int) (delay.getValue().floatValue() * 10);
                        }
                    }
                }
            } else {
                clear();
                return;
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if (mc.world == null || mc.player == null) return;
        if (event.isSend()) {
            if(freezing && event.packet instanceof CUseEntityPacket){
                if(((CUseEntityPacket) event.packet).getEntityFromWorld(mc.world) != targets){
                    clear();
                }
            }
            return;
        }
        if(noEvent) return;
//        if(event.packet instanceof SConfirmTransactionPacket){
//            if(clear){
//                clear = false;
//                clear();
//            }
//        }
        if (event.packet instanceof SEntityPacket) {
            SEntityPacket packet = (SEntityPacket)event.packet;
            Entity entity = packet.getEntity(mc.world);
            if (entity != null)
            {
                if (!entity.canPassengerSteer())
                {
                    Vector3d vector3d = packet.func_244300_a(entity.getServerPos());
                    entity.lastServerPos = entity.getServerPos();
                    entity.func_242277_a(vector3d);
                    if (freezing) {
                        if (!storeEntities.contains(entity))
                            storeEntities.add(entity);
                        event.cancelable = true;
                        return;
                    }
                    if (packet.func_229745_h_())
                    {
                        if (!event.cancelable) {
                            float f = packet.isRotating() ? (float) (packet.getYaw() * 360) / 256.0F : entity.rotationYaw;
                            float f1 = packet.isRotating() ? (float) (packet.getPitch() * 360) / 256.0F : entity.rotationPitch;
                            entity.setPositionAndRotationDirect(vector3d.getX(), vector3d.getY(), vector3d.getZ(), f, f1, 3, false);
                        }
                    }
                    else if (packet.isRotating())
                    {
                        if (!event.cancelable) {
                            float f2 = (float) (packet.getYaw() * 360) / 256.0F;
                            float f3 = (float) (packet.getPitch() * 360) / 256.0F;
                            entity.setPositionAndRotationDirect(entity.getPosX(), entity.getPosY(), entity.getPosZ(), f2, f3, 3, false);
                        }
                    }

                    entity.setOnGround(packet.getOnGround());
                }
                event.cancelable = true;
            }
        } else {
            if ((((velocity.isEnable() && event.packet instanceof SEntityVelocityPacket) || (event.packet instanceof SPlayerPositionLookPacket && lagClear.isEnable())))) {
                clear();
                return;
            }
            if (freezing && !event.cancelable && !(event.packet instanceof SUpdateHealthPacket || event.packet instanceof SEntityStatusPacket || event.packet instanceof SDestroyEntitiesPacket)) {
                serverPackets.add(event.packet);
                event.cancelable = true;
            }
        }
        super.onPacketEvent(event);
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if(esp.isEnable()){
            if(targets != null && freezing) {
                RenderSystem.pushMatrix();
                RenderUtils.renderPos p = RenderUtils.getRenderPos();
                RenderSystem.translated(
                        targets.getServerPos().x - p.renderPosX,
                        targets.getServerPos().y - p.renderPosY,
                        targets.getServerPos().z - p.renderPosZ
                );
                EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
                entityrenderermanager.setRenderShadow(false);
                IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
//                RenderSystem.runAsFancy(() ->
//                {
                    GL11.glColor4f(1,1,1,0.5f);
                    entityrenderermanager.renderEntityStatic(targets, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, new MatrixStack(), irendertypebuffer$impl, 15728880);
//                });
                irendertypebuffer$impl.finish();
                entityrenderermanager.setRenderShadow(true);
                RenderSystem.popMatrix();
//                IRenderTypeBuffer.Impl irendertypebuffer$impl = Tessellator.getInstance().getBuffer().getRenderTypeBuffer();
//                mc.getRenderManager().renderEntityStatic(targets, targets.getPosX() - p.renderPosX, targets.getPosY() - p.renderPosY, targets.getPosZ() - p.renderPosZ, targets.rotationYaw, event.renderTime, new MatrixStack(), irendertypebuffer$impl, mc.getRenderManager().getPackedLight(targets, event.renderTime));
//                Tessellator.getInstance().draw();
//                RenderUtils.drawEntityServerESP2(targets, 1, 1, 1, 1, 1, 1);
            }
        }
        super.onRender3DEvent(event);
    }

    @Override
    public void onDisable() {
        publicFreeze = false;
        clear();
        super.onDisable();
    }

}
