package info.sigmaclient.sigma;

import info.sigmaclient.sigma.config.ConfigManager;
import info.sigmaclient.sigma.sigma5.SelfDestructManager;
import lombok.Getter;
import top.fl0wowp4rty.phantomshield.annotations.Native;

import java.util.Date;

public class Sigma {
    @Getter
    public static SigmaNG INSTANCE;
    public static String title = "Sigma 5.0";
    public static String getWindowTitle()
    {
        if(SelfDestructManager.destruct){
            return "Minecraft";
        }
        return title;
    }
    public void initClient(){
        DiscordRPC.load();
        title = "Jello for Sigma 5.0" + (((new Date().getMonth()==0 && new Date().getDate() == 1)) ? " New Year" : "");
    }
}
