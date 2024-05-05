// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.combat;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import Lavish.modules.Module;

public class Antibot extends Module
{
    private static List<EntityPlayer> invalid;
    
    static {
        Antibot.invalid = new ArrayList<EntityPlayer>();
    }
    
    public Antibot() {
        super("Antibot", 0, true, Category.Combat, "Removes Bots");
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Hypixel");
        options.add("Mineplex");
        Client.instance.setmgr.rSetting(new Setting("Antibot Mode", this, "Hypixel", options));
    }
    
    @Override
    public void onUpdate() {
        if (Client.instance.setmgr.getSettingByName("Antibot Mode").getValString().equalsIgnoreCase("Hypixel")) {
            Minecraft var10000 = Antibot.mc;
            for (final Object entity : Antibot.mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    var10000 = Antibot.mc;
                    if (Antibot.mc.thePlayer.equals(entity) || !((Entity)entity).isInvisible() || ((Entity)entity).ticksExisted >= 105) {
                        continue;
                    }
                    var10000 = Antibot.mc;
                    Antibot.mc.theWorld.removeEntity((Entity)entity);
                }
            }
        }
        if (Client.instance.setmgr.getSettingByName("Antibot Mode").getValString().equalsIgnoreCase("Mineplex")) {
            for (final Object o : Antibot.mc.theWorld.loadedEntityList) {
                final Entity en = (Entity)o;
                if (en instanceof EntityPlayer && !(en instanceof EntityPlayerSP)) {
                    final int ticks = en.ticksExisted;
                    final double diffY = Math.abs(Antibot.mc.thePlayer.posY - en.posY);
                    final String name = en.getName();
                    final String customname = en.getCustomNameTag();
                    if (customname != "" || en.ticksExisted <= 25 || Antibot.invalid.contains(en)) {
                        continue;
                    }
                    Antibot.invalid.add((EntityPlayer)en);
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        Antibot.invalid.clear();
    }
    
    @Override
    public void onDisable() {
        Antibot.invalid.clear();
    }
    
    public static List<EntityPlayer> getInvalid() {
        return Antibot.invalid;
    }
}
