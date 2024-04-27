package me.AquaVit.liquidSense;

import me.AquaVit.liquidSense.modules.misc.*;
import me.AquaVit.liquidSense.modules.movement.*;
import me.AquaVit.liquidSense.modules.combat.*;
import me.AquaVit.liquidSense.modules.render.*;
import me.AquaVit.liquidSense.modules.world.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

public class LiquidSense {

    private LiquidBounce liquidBounce;
    private List<Object> liquidSenseModules;

    public LiquidSense(LiquidBounce liquidBounce){
        this.liquidBounce = liquidBounce;
    }

    public void onStarting(){
        Display.setTitle(LiquidBounce.CLIENT_NAME + " | " +  LiquidBounce.CLIENT_VERSION + " | " + LiquidBounce.MINECRAFT_VERSION  + " | " + "By AquaVit" + " | Loading...");
        ClientUtils.getLogger().info(LiquidBounce.CLIENT_NAME +  " | " + "By AquaVit" + " | "+ " 正在启动");
        loadModules();
    }

    public void onStarted(){
        Display.setTitle(LiquidBounce.CLIENT_NAME + " | " + LiquidBounce.CLIENT_VERSION + " | " + LiquidBounce.MINECRAFT_VERSION +" | "+ "By AquaVit");
        ClientUtils.getLogger().info(LiquidBounce.CLIENT_NAME + " | " + "By AquaVit" + " | " + " 加载完成");
    }

    private void loadModules(){
        this.liquidSenseModules = new ArrayList<>();
        liquidSenseModules.add(HYTBypass.class);
        liquidSenseModules.add(HYTLongJump.class);
        liquidSenseModules.add(Particle.class);
        liquidSenseModules.add(AutoJump.class);
        liquidSenseModules.add(EveryThingBlock.class);
        liquidSenseModules.add(SpeedMine.class);
        liquidSenseModules.add(Health.class);
        liquidSenseModules.add(AutoSword.class);
        liquidSenseModules.add(Ambience.class);
        liquidSenseModules.add(PlayerFace.class);
        liquidSenseModules.add(EnchantEffect.class);
        liquidSenseModules.add(HYTHighJump.class);
        liquidSenseModules.add(Animations.class);
        liquidSenseModules.add(ItemRotate.class);
        liquidSenseModules.add(LagBack.class);
        liquidSenseModules.add(MemoryFixer.class);
        liquidSenseModules.add(PointerESP.class);
        liquidSenseModules.add(Disabler.class);
        liquidSenseModules.add(LastestHypixelSpeed.class);
        liquidSenseModules.add(HYTRun.class);
        liquidSenseModules.add(Flight.class);
        liquidSenseModules.add(Skeltal.class);
        liquidSenseModules.add(LightningCheck.class);
        liquidSenseModules.add(LookTp.class);
        liquidSenseModules.add(Tp.class);
        liquidSenseModules.add(TargetStrafe.class);
        liquidSenseModules.add(Stair.class);
        liquidSenseModules.add(UHCXray.class);
        liquidSenseModules.add(AutoADL.class);
        liquidSenseModules.add(CameraView.class);
        liquidSenseModules.add(FakeBody.class);
        liquidSenseModules.add(KillESP.class);
        liquidSenseModules.add(PlayerList.class);
        liquidSenseModules.add(Jesus.class);
        liquidSenseModules.add(Cape.class);
        liquidSenseModules.add(HYTSpeed.class);
        liquidSenseModules.add(HYTFly.class);
        liquidSenseModules.add(HYTCriticals.class);
        liquidSenseModules.add(HYTNoFall.class);
        liquidSenseModules.add(AutoApple.class);
        liquidSenseModules.add(LastestHypixelFly.class);
        liquidSenseModules.add(Flight1.class);
        liquidSenseModules.add(NoHurt.class);
        liquidSenseModules.add(HeadLogo.class);
    }

    public List<Object> getLiquidSenseModules() {
        return liquidSenseModules;
    }


    class TitleRunnable implements Runnable{

        @Override
        public void run() {
            System.out.println("run");
                Display.setTitle(LiquidBounce.CLIENT_NAME + " | " + LiquidBounce.CLIENT_VERSION + " | " + LiquidBounce.MINECRAFT_VERSION +" | "+ "By AquaVit");
        }
    }
}
