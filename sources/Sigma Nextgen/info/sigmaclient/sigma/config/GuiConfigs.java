package info.sigmaclient.sigma.config;

import com.google.gson.annotations.SerializedName;
import info.sigmaclient.sigma.config.antisniper.DropGUI_;

import java.util.ArrayList;

public class GuiConfigs {
    @SerializedName("guis")
    ArrayList<DropGUI_> guis;
    @SerializedName("musicplayer")
    MusicPlayer musicplayer;
    @SerializedName("guiBlur")
    boolean guiBlur;
    @SerializedName("inGameGuiBlur")
    boolean inGameGuiBlur;
    public GuiConfigs(){
        guis = new ArrayList<>();
        musicplayer = new MusicPlayer();
    }
}
