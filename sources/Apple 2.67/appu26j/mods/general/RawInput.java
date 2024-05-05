package appu26j.mods.general;

import org.lwjgl.opengl.Display;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.utils.RawMouseHelper;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Mouse;
import net.minecraft.util.MouseHelper;

@ModInterface(name = "Raw Input", description = "Removes the delay of moving your mouse.", category = Category.GENERAL)
public class RawInput extends Mod
{
    private volatile boolean enabled = false;
    private int deltaX = 0, deltaY = 0;
    private Controller[] controllers;
    private Mouse mouse;
    
    @Override
    public void onEnable()
    {
        super.onEnable();
        
        if (this.mc.thePlayer != null)
        {
            this.enabled = true;
            this.mc.mouseHelper = new RawMouseHelper();
            this.controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            
            new Thread(() ->
            {
                while (this.enabled)
                {
                    for (int i = 0; i < this.controllers.length && this.mouse == null; i++)
                    {
                        if (this.controllers[i].getType() == Controller.Type.MOUSE)
                        {
                            this.controllers[i].poll();
                            
                            if (((Mouse)this.controllers[i]).getX().getPollData() != 0 || ((Mouse)this.controllers[i]).getY().getPollData() != 0)
                            {
                                this.mouse = (Mouse)this.controllers[i];
                            }
                        }
                    }
                    
                    if (this.mouse != null)
                    {
                        this.mouse.poll();
                        
                        if (this.mc.currentScreen == null)
                        {
                            this.deltaX += this.mouse.getX().getPollData();
                            this.deltaY += this.mouse.getY().getPollData();
                        }
                    }
                    
                    try
                    {
                        Thread.sleep(1);
                    }
                    
                    catch (Exception e)
                    {
                        ;
                    }
                }
            }).start();
        }
    }
    
    @Subscribe
    public void onTick(EventTick e)
    {
        if (!this.enabled)
        {
            this.enabled = true;
            this.mc.mouseHelper = new RawMouseHelper();
            this.controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            
            new Thread(() ->
            {
                while (this.enabled)
                {
                    for (int i = 0; i < this.controllers.length && this.mouse == null; i++)
                    {
                        if (this.controllers[i].getType() == Controller.Type.MOUSE)
                        {
                            this.controllers[i].poll();
                            
                            if (((Mouse)this.controllers[i]).getX().getPollData() != 0 || ((Mouse)this.controllers[i]).getY().getPollData() != 0)
                            {
                                this.mouse = (Mouse)this.controllers[i];
                            }
                        }
                    }
                    
                    if (this.mouse != null)
                    {
                        this.mouse.poll();
                        
                        if (this.mc.currentScreen == null)
                        {
                            this.deltaX += this.mouse.getX().getPollData();
                            this.deltaY += this.mouse.getY().getPollData();
                        }
                    }
                    
                    try
                    {
                        Thread.sleep(1);
                    }
                    
                    catch (Exception ex)
                    {
                        ;
                    }
                }
            }).start();
        }
    }
    
    @Override
    public void onDisable()
    {
        super.onDisable();
        this.enabled = false;
        this.mc.mouseHelper = new MouseHelper();
    }
    
    public int getDeltaX()
    {
        return this.deltaX;
    }
    
    public int getDeltaY()
    {
        return this.deltaY;
    }
    
    public void setDeltaX(int deltaX)
    {
        this.deltaX = deltaX;
    }
    
    public void setDeltaY(int deltaY)
    {
        this.deltaY = deltaY;
    }
}
