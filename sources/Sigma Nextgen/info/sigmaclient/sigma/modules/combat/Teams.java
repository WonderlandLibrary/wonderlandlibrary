package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.entity.player.PlayerEntity;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Teams extends Module {
    public static ModeValue type = new ModeValue("Type", "Name", new String[]{
            "Name",
            "System",
            "Name2"
    });
    public Teams() {
        super("Teams", Category.Combat, "Stop hitting your teammates.");
     registerValue(type);
    }
    public static boolean isTeam(PlayerEntity PlayerEntity){
        if(!SigmaNG.getSigmaNG().moduleManager.getModule(Teams.class).enabled) return false;
        String n = PlayerEntity.getDisplayName().getString();
        String n2 = mc.player.getDisplayName().getString();
        if(n.length() > 2 && n2.length() > 2)
            if(type.is("Name"))
                return n.substring(0, 2).equals(n2.substring(0, 2));
        if(n.length() > 4 && n2.length() > 4)
            if(type.is("Name2"))
                return n.substring(0, 4).equals(n2.substring(0, 4));
        if(type.is("System"))
            return PlayerEntity.isOnSameTeam(mc.player);
        return false;
    }
}
