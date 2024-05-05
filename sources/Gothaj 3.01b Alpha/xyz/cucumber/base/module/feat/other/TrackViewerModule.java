package xyz.cucumber.base.module.feat.other;

import java.io.IOException;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

@ModuleInfo(category = Category.VISUALS, description = "Displays music on screen", name = "Track Viewer", priority = ArrayPriority.LOW)
public class TrackViewerModule extends Mod{
	
	/*private NumberSettings positionX = new NumberSettings("Position X", 30, 0, 2000, 1);
	private NumberSettings positionY = new NumberSettings("Position Y", 50, 0, 2000, 1);
	
	private PositionUtils position = new PositionUtils(0,0,100, 20,1);
	
	private Track track;
	private  ResourceLocation coverImage;
	
	@EventListener
	public void onRenderGui(EventRenderGui e) {

        final DynamicTexture[] dynamicTexture = new DynamicTexture[1];
        if(track == null || Client.INSTANCE.getSpotify().getApi().getTrack() == null) {
        	track = Client.INSTANCE.getSpotify().getApi().getTrack();
        	return;
        }
        if(track != Client.INSTANCE.getSpotify().getApi().getTrack()) {
        	track = Client.INSTANCE.getSpotify().getApi().getTrack();
        	 Minecraft.getMinecraft().addScheduledTask(() -> {
                 try {
     				dynamicTexture[0] = new DynamicTexture(Client.INSTANCE.getSpotify().getApi().getOpenAPI().requestImage(Client.INSTANCE.getSpotify().getLastTrack()));
     			} catch (IOException e1) {
     				e1.printStackTrace();
     			}
                coverImage = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("cover.jpg", dynamicTexture[0]);
             });;
        }
        if(coverImage != null) {
        	position.setX(positionX.getValue());
        	position.setY(positionY.getValue());
        	double w = (Fonts.getFont("rb-r").getWidth(track.getName()) > Fonts.getFont("rb-m-13").getWidth(track.getArtist()) ?Fonts.getFont("rb-r").getWidth(track.getName()) : Fonts.getFont("rb-m-13").getWidth(track.getArtist()))+20+7.5;
        	position.setWidth(w);
        	position.setHeight(25);
        	RenderUtils.drawRect(position.getX(),position.getY(), position.getX2(), position.getY2(), 0x45000000);
        	RenderUtils.drawImage(position.getX()+2.5, position.getY()+2.5, 20,20, coverImage, -1);
        	
        	Fonts.getFont("rb-r").drawString(track.getName(), position.getX()+5+20,position.getY()+2.5+3, -1);
        	Fonts.getFont("rb-m-13").drawString(track.getArtist(), position.getX()+5+20,position.getY()+2.5+8+3, 0xffaaaaaa);
        	
        	RenderUtils.drawRect(position.getX(),position.getY2(), position.getX()+ position.getWidth()/track.getLength()*Client.INSTANCE.getSpotify().getApi().getPosition(), position.getY2()-1, 0xffffffff);
        	
        }
    }*/
	
}
