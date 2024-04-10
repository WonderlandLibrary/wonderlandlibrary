package info.sigmaclient.sigma.utils;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.RenderModule;
import info.sigmaclient.sigma.modules.gui.*;
import info.sigmaclient.sigma.sigma5.SelfDestructManager;

public class ChatChangeXY {
    public static ChatChangeXY in = new ChatChangeXY();
    public RenderModule hoverModule = null;
    public double x = 100;
    public double y = 20;
    double lastClickX, lastClickY, startX, startY;
    boolean dragging = false;
    public void release(){
        dragging = false;
    }
    public void init(){
        dragging = false;
    }
    public void render(double mouseX, double mouseY){
        if(hoverModule == null) {
            dragging = false;
            return;
        }
        if (dragging) {
            x = lastClickX + mouseX - startX;
            y = lastClickY + mouseY - startY;
            hoverModule.setX((float) x);
            hoverModule.setY((float) y);
        }
    }
    public void click(double mouseX, double mouseY){
        dragging = false;
        if(SelfDestructManager.destruct) return;
        if(((TargetHUD)SigmaNG.getSigmaNG().moduleManager.getModule(TargetHUD.class)).isHover(mouseX, mouseY) && !dragging){
            hoverModule = ((TargetHUD)SigmaNG.getSigmaNG().moduleManager.getModule(TargetHUD.class));
            dragging = true;
        }
//        if(((TimerIndicator)SigmaNG.getSigmaNG().moduleManager.getModule(TimerIndicator.class)).isHover(mouseX, mouseY) && !dragging){
//            hoverModule = ((TimerIndicator)SigmaNG.getSigmaNG().moduleManager.getModule(TimerIndicator.class));
//            dragging = true;
//        }
//        if(((StaffActives)SigmaNG.getSigmaNG().moduleManager.getModule(StaffActives.class)).isHover(mouseX, mouseY) && !dragging){
//            hoverModule = ((StaffActives)SigmaNG.getSigmaNG().moduleManager.getModule(StaffActives.class));
//            dragging = true;
//        }
//        if(((KeyBinds)SigmaNG.getSigmaNG().moduleManager.getModule(KeyBinds.class)).isHover(mouseX, mouseY) && !dragging){
//            hoverModule = ((KeyBinds)SigmaNG.getSigmaNG().moduleManager.getModule(KeyBinds.class));
//            dragging = true;
//        }
//        if(((ActiveMods)SigmaNG.getSigmaNG().moduleManager.getModule(ActiveMods.class)).isHover(mouseX, mouseY) && !dragging){
//            hoverModule = ((ActiveMods)SigmaNG.getSigmaNG().moduleManager.getModule(ActiveMods.class));
//            dragging = true;
//        }
//        if(((PotionHUD)SigmaNG.getSigmaNG().moduleManager.getModule(PotionHUD.class)).isHover(mouseX, mouseY) && !dragging){
//            hoverModule = ((PotionHUD)SigmaNG.getSigmaNG().moduleManager.getModule(PotionHUD.class));
//            dragging = true;
//        }
        if(dragging){
            startX = mouseX;
            startY = mouseY;
            lastClickX = hoverModule.getX();
            lastClickY = hoverModule.getY();
        }
    }
}
