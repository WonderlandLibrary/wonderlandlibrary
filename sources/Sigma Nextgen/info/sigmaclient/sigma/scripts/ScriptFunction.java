package info.sigmaclient.sigma.scripts;

import info.sigmaclient.sigma.utils.player.MovementUtils;

import static info.sigmaclient.sigma.modules.Module.mc;

public class ScriptFunction
{
    public void strafe(double speed){
        MovementUtils.strafing(speed);
    }
    public void setMotionX(double v){
        mc.player.getMotion().x = v;
    }
    public void setMotionY(double v){
        mc.player.getMotion().y = v;
    }
    public void setMotionZ(double v){
        mc.player.getMotion().z = v;
    }
    public void setMotion(double x, double y, double z){
        mc.player.getMotion().x = x;
        mc.player.getMotion().y = y;
        mc.player.getMotion().z = z;
    }
    public void jump(){
        mc.player.jump();
    }
}
