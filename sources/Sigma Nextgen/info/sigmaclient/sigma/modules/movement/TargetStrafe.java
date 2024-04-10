//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\1\Desktop\Minecraft-Deobfuscator3000-1.2.3\config"!

/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package info.sigmaclient.sigma.modules.movement;


import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.Killaura;
import info.sigmaclient.sigma.utils.Variable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.Comparator;

public final class TargetStrafe extends Module {
    public static BooleanValue onlySpace = new BooleanValue("Only Space", false);
    public static BooleanValue onlySpeed = new BooleanValue("Only Speed", true);
    public static ModeValue modeValue = new ModeValue("Type", "Smart", new String[]{"Smart", "Control"});
    private static final NumberValue radius = new NumberValue("Radius", 2.0, 0.1, 6.0, NumberValue.NUMBER_TYPE.FLOAT);
    private static int direction = -1;
    public TargetStrafe() {
        super("TargetStrafe", Category.Movement, "spin spin spin");
     registerValue(onlySpace);
     registerValue(onlySpeed);
     registerValue(modeValue);
     registerValue(radius);
    }


//    @Override
    public static void onMove(MoveEvent event) {
        if(!SigmaNG.getSigmaNG().moduleManager.getModule(TargetStrafe.class).enabled) return;
        sf:
        {
            if (!TargetStrafe.canStrafe()) {
                break sf;
            }
            if (TargetStrafe.onlySpeed.isEnable() && !SigmaNG.getSigmaNG().moduleManager.getModule(Speed.class).enabled) {
                break sf;
            }
            if (TargetStrafe.onlySpace.isEnable()) {
                if (!mc.gameSettings.keyBindJump.pressed) {
                    break sf;
                }
            }
            boolean smart = modeValue.is("Smart");
            if (smart && mc.player.collidedHorizontally) {
                direction *= -1;
            }
            if (smart) {
                Speed speed = ((Speed) SigmaNG.getSigmaNG().moduleManager.getModule(Speed.class));
                if (speed.enabled && speed.mode.is("Hypixel") && speed.hypixelMode.is("FakeStrafe")) {
                    if (!mc.player.onGround) break sf;
                }
                if (speed.enabled && speed.mode.is("Grim")) {
                    if(!Variable.is_pushing){
                        break sf;
                    }
                }
            }
//            final double n3 = n / (n2 * 3.141592653589793 * 2.0) * 360.0 * this.亟䈔釒婯捉;
//            final double n4 = Math.atan2(TargetStrafe.浣韤쬷햖㼜.player.刃竬곻ꪕ쇼捉() - 璧室䖼頉啖.刃竬곻ꪕ쇼捉(), TargetStrafe.浣韤쬷햖㼜.player.髾핇竁婯䄟䬹() - 璧室䖼頉啖.髾핇竁婯䄟䬹()) * 180.0 / 3.141592653589793 - 90.0;
//            final double n5 = (n4 + n3) * 3.141592653589793 / 180.0;
//            final double n6 = (Math.atan2(璧室䖼頉啖.藸펊ใ埙岋좯().嶗䈔Ⱋ玑Ꮀ牰 + Math.cos(n5) * n2 - TargetStrafe.浣韤쬷햖㼜.player.刃竬곻ꪕ쇼捉(), 璧室䖼頉啖.藸펊ใ埙岋좯().頉ꁈၝ嘖藸竁 - Math.sin(n5) * n2 - TargetStrafe.浣韤쬷햖㼜.player.髾핇竁婯䄟䬹()) * 180.0 / 3.141592653589793 - 90.0) * 3.141592653589793 / 180.0;
//            홵属䢶W쟗.玑㠠ಽ娍鶲(-Math.sin(n6) * n);
//            홵属䢶W쟗.䢿셴䬹觯岋(Math.cos(n6) * n);
//            final 䈔㕠㥇ใၝ핇 韤揩픓콗뫤뼢 = TargetStrafe.浣韤쬷햖㼜.player.韤揩픓콗뫤뼢(홵属䢶W쟗.罡㐖瀳ꈍᏔ());
//            if (韤揩픓콗뫤뼢.頉ꁈၝ嘖藸竁 != 홵属䢶W쟗.뵯㔢ၝ挐騜() || 韤揩픓콗뫤뼢.嶗䈔Ⱋ玑Ꮀ牰 != 홵属䢶W쟗.敤쇽侃褕늦()) {
//                this.亟䈔釒婯捉 *= -1;
//                final double n7 = (n4 + n3 * -1.0) * 3.141592653589793 / 180.0;
//                final double n8 = (Math.atan2(璧室䖼頉啖.藸펊ใ埙岋좯().嶗䈔Ⱋ玑Ꮀ牰 + Math.cos(n7) * n2 - TargetStrafe.浣韤쬷햖㼜.player.刃竬곻ꪕ쇼捉(), 璧室䖼頉啖.藸펊ใ埙岋좯().頉ꁈၝ嘖藸竁 - Math.sin(n7) * n2 - TargetStrafe.浣韤쬷햖㼜.player.髾핇竁婯䄟䬹()) * 180.0 / 3.141592653589793 - 90.0) * 3.141592653589793 / 180.0;
//                홵属䢶W쟗.玑㠠ಽ娍鶲(-Math.sin(n8) * n);
//                홵属䢶W쟗.䢿셴䬹觯岋(Math.cos(n8) * n);
//            }
            double radius = TargetStrafe.radius.getValue().floatValue();
            double speed = Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ());
            ArrayList<Point> nearestPoint = new ArrayList<>();
            for (int p = 0; p < 60; p ++) {
                double diffX = Killaura.attackTarget.getPosX() - mc.player.getPosX();
                double diffZ = Killaura.attackTarget.getPosZ() - mc.player.getPosZ();
                float yaw = MathHelper.wrapAngleTo180_float((float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f + p * 3 * -direction);
                double mx = Math.cos(Math.toRadians(yaw + 90.0f)) * radius;
                double mz = Math.sin(Math.toRadians(yaw + 90.0f)) * radius;
                Point n = new Point();
                n.vector3d = new Vector3d(Killaura.attackTarget.getPosX() + mx, 0, Killaura.attackTarget.getPosZ() + mz);
                n.point = p;
                nearestPoint.add(n);
            }
            nearestPoint.sort(Comparator.comparingDouble((p) -> mc.player.getDistanceSq(p.vector3d)));
//            Collections.reverse(nearestPoint);
            Point near = nearestPoint.get(0);
            int p = near.point;
            p += 7;
            if(p < 0)
                p = 60 - p;
            if(p > 59)
                p = (p - 59);
            near = nearestPoint.get(p);
            double diffX = near.vector3d.x - mc.player.getPosX();
            double diffZ = near.vector3d.z - mc.player.getPosZ();
            float yaw = MathHelper.wrapAngleTo180_float((float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f);
            double mx = Math.cos(Math.toRadians(yaw + 90.0f)) * speed;
            double mz = Math.sin(Math.toRadians(yaw + 90.0f)) * speed;

            event.setX(mx);
            event.setZ(mz);
        }
    }
    static class Point {
        public Vector3d vector3d;
        public int point;
    }
    @Override
    public void onEnable() {
    }

    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) {
            if (mc.gameSettings.keyBindLeft.isPressed()) {
                this.direction = 1;
            }
            if (mc.gameSettings.keyBindRight.isPressed()) {
                this.direction = -1;
            }
        }
    }

    public static float getRotations(double x, double y, double z) {
        final double x2 = mc.player.getPosX();
        final double z2 = mc.player.getPosZ();
        double diffX = x - x2;
        double diffZ = z - z2;
        return (float) (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90F;
    }
    public static boolean canStrafe() {
        return Killaura.attackTarget != null && SigmaNG.SigmaNG.moduleManager.getModule(Killaura.class).enabled;
    }
}

