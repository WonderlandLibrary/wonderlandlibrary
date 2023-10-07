package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.events.EventWorldChange;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@ModuleInfo(name = "Pit Bot", description = "Automatically follow and attack hypixel pit players", category = Category.MOVEMENT)
public class PitBot extends Module {

    @EventHandler
    public Listener<Event> eventListener = this::handle;

    private boolean spinning = false, attackingPlayer;
    private int ticksWalking = 0;
    private float lastYaw, fakeYaw, fakePitch;
    public static Vec3 targetPos;

    private void handle(final Event event) {
        if (!ServerUtil.isHypixel()) return;
        if (event instanceof EventWorldChange) setEnabled(false);
        if (event instanceof EventMotion) {
            if (((EventMotion) event).isPost()) {
                mc.thePlayer.prevRotationYaw = fakeYaw;
                mc.thePlayer.rotationYaw = fakeYaw;
                mc.thePlayer.prevRotationPitch = fakePitch;
                mc.thePlayer.rotationPitch = fakePitch;
            }
        }
        if (event instanceof EventRender2D) {
            float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            float f2 = (float) this.mc.mouseHelper.deltaX * f1;
            float f3 = (float) this.mc.mouseHelper.deltaY * f1;
            this.fakeYaw = (float)((double)this.fakeYaw + (double)f2 * 0.15D);
            this.fakePitch = (float)((double)this.fakePitch - (double)f3 * 0.15D);
            this.fakePitch = MathHelper.clamp_float(this.fakePitch, -90.0F, 90.0F);
        }
        if (event instanceof EventUpdate) {
            ArrayList<Entity> list = mc.theWorld.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer && e != mc.thePlayer &&
                            !e.isInvisibleToPlayer(mc.thePlayer) && !(e.posY > 90
                            && e.posX < 25 && e.posX > -25 && e.posZ < 25 && e.posZ > -25) && (e.posY <= mc.thePlayer.posY + 6 || e.posY >= mc.thePlayer.posY + 6))
                    .collect(Collectors.toCollection(ArrayList::new));
            list = list.stream().sorted(Comparator.comparingDouble(e ->
                    e.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toCollection(ArrayList::new));
            attackingPlayer = !list.isEmpty() && !(mc.thePlayer.posY > 90 && mc.thePlayer.posX < 25 && mc.thePlayer.posZ < 25 &&
                    mc.thePlayer.posX > -25 && mc.thePlayer.posZ > -25);
            if (attackingPlayer) {
                list.stream().findFirst().ifPresent(target -> targetPos = target.getPositionVector());
            } else targetPos = new Vec3(0, 100, 0);
            if (targetPos != null) {
                if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround && !Client.INSTANCE.getModuleManager().getModule(Step.class).isEnabled())
                    mc.thePlayer.jump();
                if (MathUtil.getDifference(lastYaw, mc.thePlayer.rotationYaw) > 5 && ticksWalking > 1) spinning = true;
                if (mc.thePlayer.getPositionVector().distanceTo(targetPos) < 0.1 || spinning) {
                    spinning = false;
                    ticksWalking = 0;
                    mc.gameSettings.keyBindForward.pressed = mc.gameSettings.keyBindSprint.pressed = false;
                    mc.gameSettings.keyBindLeft.pressed = mc.gameSettings.keyBindRight.pressed = false;
                    mc.thePlayer.setSpeed(0);
                    return;
                }
                lastYaw = mc.thePlayer.rotationYaw;
                ticksWalking++;
                mc.thePlayer.rotationYaw = RotationUtil.getNeededRotations(targetPos)[0];
                mc.thePlayer.rotationPitch = 0;
                mc.gameSettings.keyBindForward.pressed = true;
                if (mc.theWorld.isAnyLiquid(mc.thePlayer.getEntityBoundingBox().offset(0, 0.06, 0))) mc.thePlayer.motionY = 0.1;
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        fakePitch = mc.thePlayer.rotationPitch;
        fakeYaw = mc.thePlayer.rotationYaw;
        ticksWalking = 0;
        spinning = false;
        attackingPlayer = false;
    }
}