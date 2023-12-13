package me.nyan.flush.module.impl.misc;

import me.nyan.flush.Flush;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.notifications.Notification;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cape extends Module {
    private final BooleanSetting custom = new BooleanSetting("Custom", this, false);
    public String customPath;
    private DynamicTexture customImage;
    private ResourceLocation imageId;

    public Cape() {
        super("Cape", Category.MISC);
    }

    public boolean isCustom() {
        if (customPath != null && custom.getValue()) {
            if (customImage == null) {
                try {
                    BufferedImage img = ImageIO.read(new File(customPath));
                    if (img == null) {
                        flush.getNotificationManager().show(Notification.Type.ERROR, "ESP", "Failed to load ESP image: format unsupported", 3000);
                        customPath = null;
                        return false;
                    }
                    customImage = new DynamicTexture(img);
                    imageId = new ResourceLocation("capef/" + FilenameUtils.getName(customPath));
                } catch (IOException e) {
                    flush.getNotificationManager().show(Notification.Type.ERROR, "ESP", "Failed to load ESP image: " + e.getMessage(), 3000);
                    e.printStackTrace();
                    customPath = null;
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public DynamicTexture getCustomImage() {
        return customImage;
    }

    public String getCustomPath() {
        return customPath;
    }

    public void setCustomPath(String customPath) {
        this.customPath = customPath;
        customImage = null;
    }

    public ResourceLocation getImageId() {
        return imageId;
    }
}
