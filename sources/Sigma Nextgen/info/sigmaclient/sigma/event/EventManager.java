package info.sigmaclient.sigma.event;

import info.sigmaclient.sigma.commands.impl.GPSCommand;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.modules.world.Timer;
import info.sigmaclient.sigma.sigma5.SelfDestructManager;
import info.sigmaclient.sigma.sigma5.utils.BlurUtils;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.event.player.*;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.event.render.RenderShaderEvent;
import info.sigmaclient.sigma.gui.othergui.anticrack.AntiCrack;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.gui.hide.ClickGUI;
import info.sigmaclient.sigma.modules.movement.BlockFly;
import info.sigmaclient.sigma.modules.movement.TargetStrafe;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import info.sigmaclient.sigma.gui.clickgui.JelloClickGui;
import info.sigmaclient.sigma.gui.clickgui.musicplayer.MusicWaveRender;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import org.lwjgl.opengl.GL11;

import static info.sigmaclient.sigma.modules.Module.mc;
import static info.sigmaclient.sigma.modules.combat.Killaura.unBlock;
import static info.sigmaclient.sigma.modules.render.ESP.shadowESP;
import static info.sigmaclient.sigma.modules.world.Timer.violation;

public class EventManager {
    public static boolean nextLegitUnblock = false;
    private static boolean init = false;
    public static void init(){
        init = true;
    }
    public static void call(Event event) {
        if(!init || SelfDestructManager.destruct) return;
        if(event instanceof WindowUpdateEvent){
            SigmaNG.SigmaNG.antiAgent.doOneCheck();
            if(!SigmaNG.SigmaNG.verify.verify){
                if(!(Minecraft.getInstance().currentScreen instanceof AntiCrack)){
                    Minecraft.getInstance().displayGuiScreen(new AntiCrack());
                }
            }
        }
        if(Minecraft.getInstance().player == null || Minecraft.getInstance().world == null) return;

        if(event instanceof RenderEvent) {
        }
        if(event instanceof UpdateEvent){
            if(((UpdateEvent) event).isPre()){
                if(RotationUtils.NEXT_SLOT != -1){
                    Minecraft.getInstance().getConnection().sendPacketNOEvent(new CHeldItemChangePacket(RotationUtils.NEXT_SLOT));
                    RotationUtils.NEXT_SLOT = -1;
                }
            }
        }

        for (Module module : SigmaNG.getSigmaNG().moduleManager.modules) {
            if(event instanceof KeyEvent){
                if(module.key != -1 && ((KeyEvent) event).key == module.key)
                    module.toggle();
            }
            if(module.enabled) {
                EventRunner.doEvent(module, event);
            }
        }
        if(event instanceof ClickEvent && nextLegitUnblock){
            unBlock();
            nextLegitUnblock = false;
        }
        if(event instanceof RenderEvent){
            GPSCommand.render();
            ((BlockFly)SigmaNG.SigmaNG.moduleManager.getModule(BlockFly.class)).renderBlockCounter();
            if(SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA) {
                SigmaNG.getSigmaNG().notificationManager.onRender();
            }
            MusicWaveRender.SELF.drawTexture();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            if(!(mc.currentScreen instanceof JelloClickGui) && mc.player != null && mc.world != null){
                if(JelloClickGui.leave.getValue() < 1.68 && ClickGUI.isEnableFirst){
                    GL11.glPushMatrix();
                    ClickGUI.clickGui.drawScreen(0,0, mc.timer.renderPartialTicks);
                    GL11.glPopMatrix();
                }
            }
        }
        if(event instanceof RenderShaderEvent){
            MusicWaveRender.SELF.drawWave();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        if(event instanceof Render3DEvent){
            //if(!shaders)
            //shadowESP.renderEvent2();
            GlStateManager.disableLighting();
            GlStateManager.enableTexture2D();
        }
        if(event instanceof WindowUpdateEvent){
            MusicWaveRender.SELF.onTick();
            if(!(mc.currentScreen instanceof JelloClickGui) && mc.player != null && mc.world != null) {
                JelloClickGui.leave.interpolate(1.7f, 3);
            }
        }
        if(event instanceof UpdateEvent){
            if(((UpdateEvent) event).isPre()) {
                ((BlockFly)SigmaNG.SigmaNG.moduleManager.getModule(BlockFly.class)).tickForAnim();
                SigmaNG.getSigmaNG().notificationManager.onTick();
            }else{
                if(!((UpdateEvent) event).send){
                    Timer.violation -= 1;
//                    ChatUtils.sendMessageWithPrefix("1 " + violation);
                    Timer.violation = Math.max(Timer.violation, 0);
//                    return;
                }
            }
        }
        if(event instanceof MoveEvent){
            MovementUtils.clacMotion((MoveEvent) event);
            TargetStrafe.onMove((MoveEvent) event);
        }
    }
}
