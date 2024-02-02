package CakeClient.ui;

import CakeClient.modules.Module;
import net.minecraft.client.gui.Gui;

import org.apache.http.conn.ClientConnectionRequest;

import CakeClient.Client;
import net.minecraft.client.Minecraft;

public class HUD
{
    public Boolean shown;
    public Integer activationKey;
    public Integer upKey;
    public Integer downKey;
    public Integer selectKey;
    public Integer leftConfigKey;
    public Integer rightConfigKey;
    public Minecraft mc;
    private Integer fontSizeY;
    private Integer boxSizeX;
    private Integer configBoxSizeX;
    private Integer configBoxPosY;
    private Integer leftBorder;
    private Integer selectorPos;
    
    public HUD(int activationKey, int upKey, int downKey, int selectKey, int leftConfigKey, int rightConfigKey) {
        this.shown = true;
        this.mc = Minecraft.getMinecraft();
        this.fontSizeY = 8;
        this.boxSizeX = 69;
        this.leftBorder = 5;
        this.selectorPos = 0;
        this.configBoxSizeX = 100;
        this.configBoxPosY = 0;
        this.activationKey = activationKey;
        this.upKey = upKey;
        this.downKey = downKey;
        this.selectKey = selectKey;
        this.leftConfigKey = leftConfigKey;
        this.rightConfigKey = rightConfigKey;
    }
    
    public void onDraw() {
        if (this.shown) { 
        	// static elements
        	mc.fontRendererObj.drawString("CakeClient",200 , 5, -1);
        	if (Client.modules[this.selectorPos].getConfigStatus() != "")
        	{
        		Gui.drawRect(this.leftBorder + this.boxSizeX, this.configBoxPosY, this.leftBorder + this.boxSizeX + this.configBoxSizeX, this.configBoxPosY+ this.fontSizeY,-1879048192);
        		this.mc.fontRendererObj.drawString(Client.modules[this.selectorPos].getConfigStatus(), this.leftBorder + this.boxSizeX, this.configBoxPosY, 43520);
        	}
        	
        	Integer y = 0;
            Integer i = 0;
            Module[] modules;
            for (int length = (modules = Client.modules).length, j = 0; j < length; ++j) {
                final Module m = modules[j];
                Gui.drawRect(this.leftBorder, y, this.boxSizeX, y + this.fontSizeY, -1879048192);
                if (this.selectorPos == i) {
                	Gui.drawRect(this.leftBorder, y, this.boxSizeX, y + this.fontSizeY, -1);
                }
                if (m.enabled) {
                    this.mc.fontRendererObj.drawString(m.name, this.leftBorder, y, 43520);
                }
                else {
                    this.mc.fontRendererObj.drawString(m.name, this.leftBorder, y, 11141120);
                }
                ++i;
                y += this.fontSizeY;
            }
        }
    }
    
    public void keyUpdate(final int key) {
        if (this.activationKey == key) {
            this.shown = !this.shown;
        }
        else if (key == this.upKey) {
            --this.selectorPos;
        }
        else if (key == this.downKey) {
            ++this.selectorPos;
        }
        else if (key == this.selectKey) {
            try {
                Client.modules[this.selectorPos].toggle();
            }
            catch (Exception e) {
                System.out.println("Module selector error: " + e);
                this.selectorPos = 0;
            }
        }
        else if (key == this.leftConfigKey)
        {
        	Client.modules[this.selectorPos].onLeftConfig();
        }
        else if (key == this.rightConfigKey)
        {
        	Client.modules[this.selectorPos].onRightConfig();
        }
    }
}