package vestige.module.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.RenderEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.misc.TimerUtil;
import vestige.util.player.FixedRotations;
import vestige.util.player.RotationsUtil;

import java.util.ArrayList;
import java.util.Comparator;

public class AimAssist extends Module {

    private Antibot antibotModule;
    private Teams teamsModule;

    private final ModeSetting filter = new ModeSetting("Filter", "Range", "Range", "Health");
    private final DoubleSetting range = new DoubleSetting("Range", 4.5, 3, 8, 0.1);

    private final IntegerSetting speed = new IntegerSetting("Speed", 10, 1, 40, 1);

    private final TimerUtil timer = new TimerUtil();

    private EntityPlayer target;

    private FixedRotations rotations;

    public AimAssist() {
        super("AimAssist", Category.COMBAT);
        this.addSettings(filter, range, speed);
    }

    @Override
    public void onEnable() {
        rotations = new FixedRotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
    }

    @Override
    public void onClientStarted() {
        antibotModule = Vestige.instance.getModuleManager().getModule(Antibot.class);
        teamsModule = Vestige.instance.getModuleManager().getModule(Teams.class);
    }

    @Listener
    public void onRender(RenderEvent event) {
        target = findTarget();

        if(target != null && Mouse.isButtonDown(0) && mc.currentScreen == null) {
            float rots[] = RotationsUtil.getRotationsToEntity(target, false);

            float yaw = rots[0];
            float currentYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);

            float diff = Math.abs(currentYaw - yaw);

            if (diff >= 4 && diff <= 356) {
                float aa;

                if (diff <= speed.getValue()) {
                    aa = diff * 0.9F;
                } else {
                    aa = (float) (speed.getValue() - Math.random() * 0.5F);
                }

                float finalSpeed = aa * Math.max(timer.getTimeElapsed(), 1) * 0.01F;

                if (diff <= 180) {
                    if (currentYaw > yaw) {
                        mc.thePlayer.rotationYaw -= finalSpeed;
                    } else {
                        mc.thePlayer.rotationYaw += finalSpeed;
                    }
                } else {
                    if (currentYaw > yaw) {
                        mc.thePlayer.rotationYaw += finalSpeed;
                    } else {
                        mc.thePlayer.rotationYaw -= finalSpeed;
                    }
                }
            }
        }

        rotations.updateRotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);

        mc.thePlayer.rotationYaw = rotations.getYaw();
        mc.thePlayer.rotationPitch = rotations.getPitch();

        timer.reset();
    }

    public EntityPlayer findTarget() {
        ArrayList<EntityPlayer> entities = new ArrayList<>();
        for(Entity entity : mc.theWorld.loadedEntityList) {
            if(entity instanceof EntityPlayer && entity != mc.thePlayer) {
                EntityPlayer player = (EntityPlayer) entity;

                if(canAttackEntity(player)) {
                    entities.add(player);
                }
            }
        }

        if(entities != null && entities.size() > 0) {
            switch (filter.getMode()) {
                case "Range":
                    entities.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));
                    break;
                case "Health":
                    entities.sort(Comparator.comparingDouble(entity -> entity.getHealth()));
                    break;
            }

            return entities.get(0);
        }

        return null;
    }

    private boolean canAttackEntity(EntityPlayer player) {
        if (!player.isDead) {
            if (mc.thePlayer.getDistanceToEntity(player) < range.getValue()) {
                if ((!player.isInvisible() && !player.isInvisibleToPlayer(mc.thePlayer))) {
                    if(!teamsModule.canAttack(player)) {
                        return false;
                    }

                    return antibotModule.canAttack(player, this);
                }
            }
        }

        return false;
    }

}
