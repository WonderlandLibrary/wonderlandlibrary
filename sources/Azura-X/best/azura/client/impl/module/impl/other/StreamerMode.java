package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ClickValue;
import best.azura.client.impl.value.StringValue;
import best.azura.client.util.textures.JordanTextureUtil;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@ModuleInfo(name = "Streamer Mode", category = Category.OTHER, description = "Hides your name and more if you want to")
public class StreamerMode extends Module {
    public final BooleanValue hideUserName = new BooleanValue("Hide Username", "Hides your username everywhere.", true);
    public final BooleanValue disableSB = new BooleanValue("Disable scoreboard", "Stop rendering the mc scoreboard.", true);
    public final BooleanValue hideGameID = new BooleanValue("Hide Game ID (Hypixel)", "Hide game id in scoreboard", true);
    public final BooleanValue skinChanger = new BooleanValue("Skin Changer", "Change your (and possibly other's) skin/s", false);
    public final BooleanValue slimModel = new BooleanValue("Slim Model", "Make your skin slim", skinChanger::getObject, false);
    public final StringValue skinBase64 = new StringValue("Skin Data", "Data for your skin", val -> {
        if (val instanceof StringValue) {
            final StringValue stringValue = (StringValue) val;
            Client.INSTANCE.getGlTasks().add(() -> {
                try {
                    this.skinLocation = JordanTextureUtil.getResourceFromImage(ImageIO.read(new ByteArrayInputStream(
                            DatatypeConverter.parseBase64Binary(getStringAfter(stringValue.getObject())))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }, () -> false, "");
    @SuppressWarnings("unused")
    public final ClickValue changeSkinValue = new ClickValue("Change Skin", "Change skin", skinChanger::getObject, () -> new Thread(() -> {
        final FileDialog fileDialog = new FileDialog((Frame) null, "Select skin");
        fileDialog.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg"));
        fileDialog.setAlwaysOnTop(true);
        fileDialog.setLocationRelativeTo(null);
        fileDialog.requestFocus();
        fileDialog.setVisible(true);
        if (fileDialog.getFiles().length >= 1) {
            try {
                final File file = fileDialog.getFiles()[0];
                if (!(file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg"))) {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Warning", "File " + file.getName() + " cannot be loaded!", 3000, Type.WARNING));
                    return;
                }
                final BufferedImage image = ImageIO.read(file);
                this.skinBase64.setObject(JordanTextureUtil.getBase64FromImage(image, "PNG"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }).start());
    @SuppressWarnings("unused")
    public final ClickValue resetSkinValue = new ClickValue("Reset Skin", "Reset skin", skinChanger::getObject, () -> {
        skinBase64.setObject("");
        skinLocation = null;
    });

    private ResourceLocation skinLocation;

    private String getStringAfter(String input) {
        if (input.contains(",")) return input.split(",")[1];
        return input;
    }


    public ResourceLocation getSkinLocation() {
        return skinLocation;
    }
}
