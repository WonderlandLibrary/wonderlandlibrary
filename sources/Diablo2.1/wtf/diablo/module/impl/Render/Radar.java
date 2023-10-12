package wtf.diablo.module.impl.Render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import wtf.diablo.commands.impl.FriendCommand;
import wtf.diablo.events.impl.OverlayEvent;
import wtf.diablo.gui.guiElement.GuiElement;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.utils.render.RenderUtil;

public class Radar extends Module {

    public BooleanSetting players = new BooleanSetting("Players", true);
    public BooleanSetting animals = new BooleanSetting("Animals", false);
    public BooleanSetting mobs = new BooleanSetting("Mobs", false);
    public BooleanSetting invisible = new BooleanSetting("Invisibles", false);

    public Radar() {
        super("Radar", "Minimap type beat", Category.RENDER, ServerType.All);
    }

    public GuiElement element = new GuiElement("Radar",this,50,50,50,50);

    @Subscribe
    public void onOverlay(OverlayEvent e) {
        element.renderStart();

        element.setWidth(125);
        element.setHeight(125);

        double width = element.getWidth();
        double height = element.getHeight();

        Gui.drawRect(-1,-1,width + 1,height + 1,0xFF212121);

        GlStateManager.translate(width / 2f, height /2f,0);
        for(Entity entity : mc.theWorld.loadedEntityList){
            if(entity instanceof EntityLivingBase){
                int color = 0;
                if(entity.isInvisible() && !invisible.getValue())
                    continue;
                if(mobs.getValue())
                    if(entity instanceof EntityMob){
                        color = 0xff4d1410;
                    }

                if(animals.getValue())
                    if(entity instanceof EntityAnimal){
                        color = 0xff38ffeb;
                    }

                if(players.getValue())
                    if(entity instanceof EntityPlayer){
                        color = 0xffff3838;
                        if(FriendCommand.isFriend((EntityLivingBase) entity))
                            color = 0xff30db30;
                    }

                double x = mc.thePlayer.posX - entity.posX;
                double y = mc.thePlayer.posZ - entity.posZ;
                Gui.drawRect(x - 1, y - 1,x + 1, y + 1, color);
            }
        }
        GlStateManager.rotate(mc.thePlayer.rotationYaw,0,0,1);
        GlStateManager.color(1,1,1);
        RenderUtil.drawImage(-3,-3,6,6,new ResourceLocation("diablo/icons/triangle.png"));
        GlStateManager.rotate(-mc.thePlayer.rotationYaw,0,0,1);
        GlStateManager.translate(-(width / 2f), -(height /2f),0);


        element.renderEnd();
    }
}
