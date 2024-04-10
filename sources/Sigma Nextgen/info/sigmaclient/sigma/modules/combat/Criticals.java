package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.AttackEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.play.client.CPlayerPacket;

import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Criticals extends Module {
    public static ModeValue mode = new ModeValue("Mode", "Packet", new String[]{
            "Blocksmc",
            "OldNCP",
            "Packet",
            "Hovered",
            "Vulcan",
            "Test",
            "Motion",
            "ColdPVP"
    });
    public static NumberValue motion = new NumberValue("motion", 0.2, 0, 1, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !mode.is("Motion");
        }
    };
    public static NumberValue hurtTime = new NumberValue("HurtTime", 1, 1, 10, NumberValue.NUMBER_TYPE.INT);
    public Criticals() {
        super("Criticals", Category.Combat, "Critical on the ground.");
     registerValue(mode);
     registerValue(motion);
     registerValue(hurtTime);
    }
    public static Random random = new Random();
    public static int attacked = 0;

    public int stage = 0;
    double y = 0;

    @Override
    public void onAttackEvent(AttackEvent event) {
        if(!event.post){
            if(event.LivingEntity instanceof LivingEntity entity)
                Criticals.criticalsEntity(entity);
        }
        super.onAttackEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent e) {
        if (e.isPost()) {
            return;
        }
        suffix = mode.getValue();
        attacked++;
        if(mode.getValue().equals("Motion") && Killaura.attackTarget != null && mc.player.onGround){
            mc.player.getMotion().y = motion.getValue().floatValue();
        }
        if(mode.is("Blocksmc")) {
            if(attacked == 0){
                e.y += 0.012343;
            }
        }
        if (mode.getValue().equals("Hovered")) {
            double ypos = mc.player.getPosY();
            if(mc.player.onGround){
                e.onGround = false;
                if(stage == 0){
                    y = ypos + 1E-8;
                    e.onGround = true;
                }else if(stage == 1)
                    y-= 5E-9;
                else
                    y-= 4E-9;

                if(y <= mc.player.getPosY()){
                    stage = 0;
                    y = mc.player.getPosY();
                    e.onGround = true;
                }
                e.y = y;
                stage ++;
            }else
                stage = 0;
        }
        if (mode.getValue().equals("Test")) {
            if(attacked == 0)
                e.onGround = false;
        }
        super.onUpdateEvent(e);
    }
    static boolean attacking = false;
    public static void criticalsEntity(LivingEntity LivingEntity){
        if(!SigmaNG.SigmaNG.moduleManager.getModule(Criticals.class).enabled || LivingEntity.hurtTime > hurtTime.getValue().longValue()){
            return;
        }
        attacking = true;
        mc.player.onCriticalHit(LivingEntity);
        switch (mode.getValue()){
            case "Test":
                attacked = -1;
                break;
            case "Dev2":
                if(!mc.player.onGround) return;
                addYPos(0.41999998688697815, false);
                addYPos(0.0, false);
                return;
            case "Dev3":
                if(!mc.player.onGround) return;
                addYPos(0.1, false);
                addYPos(0.3, false);
                addYPos(0.2, false);
                addYPos(0.1, false);
                return;
            case "Motion":
                if(!mc.player.onGround) return;
                return;
            case "OldNCP":
                if(!mc.player.onGround) return;
                double[] object = new double[]{
                        0.0625D,
                        0
                };
                for (double object3 : object) {
                    addYPos(object3, false);
                }
                return;
            case "Packet":
                if(!mc.player.onGround) return;
                addYPos(0.08, false);
                addYPos(0.03, false);
                return;
            case "Blocksmc":
                if(!mc.player.onGround) return;
                if (attacked > 6) {
                    addYPos(0.10749678457684, false);
                    addYPos(0.07348598467864, false);
                    attacked = -1;
                }
                return;
            case "ColdPVP":
                if(!mc.player.onGround) return;
                addYPos(0.41999998688697815, false);
                addYPos(0.35457, false);
                addYPos(0.14445, false);
                break;
            case "Vulcan":
                if(!mc.player.onGround) return;
                if (attacked > 8) {
                    addYPos(0.16, false);
                    addYPos(0.083, false);
                    addYPos(0.003, false);
                    attacked = 0;
                }
                break;
            case "Hovered":
                attacked = 1;
                break;
        }
    }
    public static void addYPos(double Y, boolean g){
        mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), mc.player.getPosY() + Y, mc.player.getPosZ(), g));
    }
}
