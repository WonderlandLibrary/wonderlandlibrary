/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import com.google.gson.JsonElement;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import javax.imageio.ImageIO;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.TextValue;

@ElementInfo(name="Image")
public final class Image
extends Element {
    public static final Companion Companion = new Companion(null);
    private final TextValue image = new TextValue(this, "Image", ""){
        final Image this$0;

        public void fromJson(JsonElement jsonElement) {
            super.fromJson(jsonElement);
            CharSequence charSequence = (CharSequence)this.get();
            boolean bl = false;
            if (charSequence.length() == 0) {
                return;
            }
            Image.access$setImage(this.this$0, (String)this.get());
        }

        static {
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged((String)object, (String)object2);
        }

        protected void onChanged(String string, String string2) {
            CharSequence charSequence = (CharSequence)this.get();
            boolean bl = false;
            if (charSequence.length() == 0) {
                return;
            }
            Image.access$setImage(this.this$0, (String)this.get());
        }
        {
            this.this$0 = image2;
            super(string, string2);
        }
    };
    private int width = 64;
    private int height = 64;
    private final IResourceLocation resourceLocation = MinecraftInstance.classProvider.createResourceLocation(RandomUtils.INSTANCE.randomNumber(128));

    @Override
    public Border drawElement() {
        RenderUtils.drawImage(this.resourceLocation, 0, 0, this.width / 2, this.height / 2);
        return new Border(0.0f, 0.0f, (float)this.width / 2.0f, (float)this.height / 2.0f);
    }

    public final Image setImage(File file) {
        try {
            this.setImage(Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public static final Image access$setImage(Image image2, String string) {
        return image2.setImage(string);
    }

    private final Image setImage(String string) {
        try {
            this.image.changeValue(string);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(string));
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            byteArrayInputStream.close();
            this.width = bufferedImage.getWidth();
            this.height = bufferedImage.getHeight();
            MinecraftInstance.mc.getTextureManager().loadTexture(this.resourceLocation, MinecraftInstance.classProvider.createDynamicTexture(bufferedImage));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return this;
    }

    @Override
    public boolean createElement() {
        File file = MiscUtils.openFileChooser();
        if (file == null) {
            return false;
        }
        File file2 = file;
        if (!file2.exists()) {
            MiscUtils.showErrorPopup("Error", "The file does not exist.");
            return false;
        }
        if (file2.isDirectory()) {
            MiscUtils.showErrorPopup("Error", "The file is a directory.");
            return false;
        }
        this.setImage(file2);
        return true;
    }

    public Image() {
        super(0.0, 0.0, 0.0f, null, 15, null);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Image default() {
            Image image2 = new Image();
            image2.setX(0.0);
            image2.setY(0.0);
            return image2;
        }
    }
}

