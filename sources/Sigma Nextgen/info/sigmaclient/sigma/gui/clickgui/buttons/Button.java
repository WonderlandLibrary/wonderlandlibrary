package info.sigmaclient.sigma.gui.clickgui.buttons;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Button {
    public boolean isClicked = false;
    public int addY = 0;
    public void clicked(){
        isClicked = true;
    }
    public boolean isHidden(){
        return false;
    }
    public boolean isCancelClick(int x, int y, int mx, int my){
        return false;
    }
    public void markDraw(int x, int y, int mx, int my, float pticks, float alpha){

    }
    public void animTick(int x, int y, int mx, int my){

    }
    public void drawButton(int x, int y, int mx, int my, float pticks, float alpha){

    }
    public void release(int x, int y, int mx, int my){

    }

    public boolean clickButton(int x, int y, int mx, int my){
        return false;
    }

}
