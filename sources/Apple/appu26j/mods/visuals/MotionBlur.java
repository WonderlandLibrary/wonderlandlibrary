package appu26j.mods.visuals;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.gui.LoadingScreen;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import pw.cinque.motionblur.resource.MotionBlurResourceManager;

@ModInterface(name = "Motion Blur", description = "A motion blur mod!", category = Category.VISUALS)
public class MotionBlur extends Mod
{
    private static MotionBlur instance;
    private float prevSliderValue = 1;
    private boolean temp = false;
    
    public MotionBlur()
    {
        this.instance = this;
        this.addSetting(new Setting("Blur Amount", this, 1, 2, 10, 1));
    }
    
    @Override
    public void onEnable()
    {
        super.onEnable();
        
        if (!((SimpleReloadableResourceManager) this.mc.getResourceManager()).domainResourceManagers.containsKey("motionblur"))
        {
            ((SimpleReloadableResourceManager) this.mc.getResourceManager()).domainResourceManagers.put("motionblur", new MotionBlurResourceManager(mc.metadataSerializer_));
        }
        
        if (!LoadingScreen.getProgress().isEmpty())
        {
            this.temp = true;
        }
        
        else
        {
            this.mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        }
    }
    
    @Subscribe
    public void onTick(EventTick e)
    {
        if (this.prevSliderValue != blurAmount() || this.temp)
        {
            this.mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
            this.prevSliderValue = blurAmount();
            
            if (this.temp)
            {
                this.temp = false;
            }
        }
        
        if (!this.mc.entityRenderer.isShaderActive())
        {
            this.mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        }
    }
    
    @Override
    public void onDisable()
    {
        super.onDisable();
        this.mc.entityRenderer.loadEntityShader(null);
    }
    
    public static MotionBlur get()
    {
        return instance;
    }
    
    public float blurAmount()
    {
        return this.getSetting("Blur Amount").getSliderValue();
    }
}
