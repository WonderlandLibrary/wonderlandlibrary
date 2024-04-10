package info.sigmaclient.sigma.gui.keybindmanager;

import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.util.InputMappings;

import java.awt.*;

public class KeyBindButton {
    public float x , y , height = 20, strOffset = 0;
    public float anims = 0 , maxAnim = 2.5F;

    int key;
    public String keyName;
    public String name;
    String prefix = "key.keyboard.";
    public boolean press = false;
    boolean isCoin;
    public KeyBindButton(float x , float y , String key , boolean ol){
        height = 31.5F;
        this.x = x;
        this.isCoin = ol;
        this.y = y;
        this.keyName = prefix + key;
        this.name = key;
        this.key = InputMappings.getInputByName("key.keyboard."+key).getKeyCode();
    }
    public KeyBindButton(float x , float y , String key , boolean ol , int o){
        height = 31.5F;
        this.x = x;
        this.isCoin = ol;
        this.y = y;
        this.keyName = prefix + "unknown";
        if(key.equalsIgnoreCase("WINDOWS")){
            key = "O";
        }
        if(key.equalsIgnoreCase("SPACE")){
            key = "           ";
        }
        if(key.equalsIgnoreCase("RCtrl")){
            key = "Ctrl";
        }
        if(key.equalsIgnoreCase("RWINDOWS")){
            key = "O";
        }
        if(key.equalsIgnoreCase("LIST")){
            key = "-";
        }
        if(key.equalsIgnoreCase("RSHIFT")){
            key = "RShift";
        }
        this.name = key;
        this.key = -2;
    }
    public void draw(){
        RenderUtils.drawRoundedRect(x, y + anims, x + height, y+ 31.5F + 2.5F,4,new Color(222,222,222).getRGB());
        RenderUtils.drawRoundedRect(x,y + anims,x + height,y+ 31.5F + anims,4,new Color(240,240,240).getRGB());
        JelloFontUtil.jelloFont20.drawString(this.name.substring(0,1).toUpperCase() + (this.name.length() > 1 ? this.name.substring(1) : ""),
                x + 12.5F + strOffset,y + 12.5F + anims,isCoin ? new Color(58, 58, 58).getRGB() : new Color(140, 140, 140).getRGB());
    }
    public boolean isHover(double x , double y){
        return ClickUtils.isClickable(this.x,this.y,this.x + height,this.y + 31.5F,x,y);
    }

    public void anim() {
        if (this.press) {
            if (anims < maxAnim) {
                anims += (maxAnim - anims) * 0.5f;
            } else {
                anims = maxAnim;
            }
        } else {
            if (anims > 0){
                anims -= (anims) * 0.5f;
            } else {
                anims = 0;
            }
        }
    }
}
