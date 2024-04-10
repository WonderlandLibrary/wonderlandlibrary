package info.sigmaclient.sigma.modules.movement.flys.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.FlyModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.network.play.client.CPlayerPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class OldNCPFly extends FlyModule {
    public OldNCPFly(Fly fly) {
        super("Hypixel", "Fly for Hypixel", fly);
    }
    float timer = 0, speed = 0;
    int ticks = 0;
    boolean jump = false, zoom = false;

    @Override
    public void onEnable() {
        timer = parent.oldNCPTimer.getValue().floatValue();
        ticks = 150;
        zoom = false;
        jump = false;
        speed = parent.oldNCPSpeed.getValue().floatValue();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.getMotion().y = 0;
        mc.player.getMotion().x = 0;
        mc.player.getMotion().z = 0;
        super.onDisable();
    }

    public static void 걾躚㹔햖ᔎ() {
        final double 髾핇竁婯䄟䬹 = mc.player.getPosX();
        final double 鶊姮韤蛊圭Ⱋ = mc.player.getPosY();
        final double 刃竬곻ꪕ쇼捉 = mc.player.getPosZ();
        for (int n = 9, i = 0; i < n; ++i) {
            final double n2 = 0.0;
            mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(髾핇竁婯䄟䬹 + n2, 鶊姮韤蛊圭Ⱋ + 0.41 + 䄟硙佉釒躚(), 刃竬곻ꪕ쇼捉 + n2, false));
//            if (㕠쥦㐈牰藸()) {
//                堧鏟ᔎ㕠釒.㦖홵Ꮺ韤堍.쬷ศ蕃䡸鶲걾().ꦱ㦖罡泹婯霥(new CPlayerPacket.PositionPacket(髾핇竁婯䄟䬹 + n2, 鶊姮韤蛊圭Ⱋ + 0.05 + 䄟硙佉釒躚(), 刃竬곻ꪕ쇼捉 + n2, false));
//            }
            mc.getConnection().sendPacket(new CPlayerPacket.PositionPacket(髾핇竁婯䄟䬹 + n2, 鶊姮韤蛊圭Ⱋ, 刃竬곻ꪕ쇼捉 + n2, false));
        }
        mc.getConnection().sendPacket(new CPlayerPacket(true));
    }

    public static double 䄟硙佉釒躚() {
        return Math.random() * 1.0E-8;
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        if(jump) {
            ticks--;
            if (ticks <= 50 && ticks > 0) {
                timer = 1 + (parent.oldNCPTimer.getValue().floatValue() - 1) * 0.4f;
            } else if (ticks <= 0) {
                timer = 1;
            }
            mc.player.getMotion().y = 0;
            event.y -= mc.player.ticksExisted % 2 == 0 ? 1E-10 : 0;
        }
        if(mc.player.onGround){
            mc.player.jump();
            jump = true;
            if(parent.oldNCPMode.is("Fast")) {
                걾躚㹔햖ᔎ();
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        mc.timer.setTimerSpeed(timer);
        if(!jump) return;
        if(parent.oldNCPMode.is("Basic")) {
            MovementUtils.strafing(event, MovementUtils.getBaseMoveSpeed());
        }else if(parent.oldNCPMode.is("Fast")) {
            if(mc.player.hurtTime > 0) zoom = true;
            if(!zoom) {
                MovementUtils.strafing(event, MovementUtils.getBaseMoveSpeed());
            }else{
                speed *= 0.95f;
                if(mc.player.hurtTime > 0) speed = 2;
                speed = Math.max(speed, 1f);
                MovementUtils.strafing(event, MovementUtils.getBaseMoveSpeed() * speed);
            }
        }
        super.onMoveEvent(event);
    }
}
