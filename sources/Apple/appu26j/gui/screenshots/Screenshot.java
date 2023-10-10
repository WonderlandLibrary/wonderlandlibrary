package appu26j.gui.screenshots;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class Screenshot
{
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    private BufferedImage bufferedImage;
    private ResourceLocation image;
    private String time;
    private File file;
    
    public Screenshot(File file)
    {
        this.file = file;
        
        try
        {
            this.bufferedImage = ImageIO.read(file);
            DynamicTexture dynamicTexture = new DynamicTexture(this.bufferedImage);
            this.image = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(file.getName(), dynamicTexture);
        }
        
        catch (Exception e)
        {
            ;
        }
        
        // Code which I can't even read myself below here
        String dateAndTime = this.simpleDateFormat.format(this.getFile().lastModified());
        String[] parts = dateAndTime.split(" ");
        dateAndTime = parts[0];
        String temp = parts[1];
        String[] partsOfTemp = temp.split(":");
        temp = partsOfTemp[1];
        String temp2 = partsOfTemp[0];
        int number = Integer.parseInt(temp2);
        boolean AM = true;
        
        if (number >= 12)
        {
            if (number != 12)
            {
                number -= 12;
            }
            
            AM = false;
        }
        
        this.time = dateAndTime + " " + number + ":" + temp2 + " " + (AM ? "AM" : "PM");
    }
    
    public File getFile()
    {
        return this.file;
    }
    
    public ResourceLocation getImage()
    {
        return this.image;
    }
    
    public BufferedImage getBufferedImage()
    {
        return this.bufferedImage;
    }
    
    public String getTime()
    {
        return this.time;
    }
}
