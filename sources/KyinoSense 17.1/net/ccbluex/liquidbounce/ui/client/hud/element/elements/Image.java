/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import com.google.gson.JsonElement;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import javax.imageio.ImageIO;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@ElementInfo(name="Image")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0016J\u000e\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u000fJ\u0010\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Image;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "()V", "height", "", "image", "Lnet/ccbluex/liquidbounce/value/TextValue;", "resourceLocation", "Lnet/minecraft/util/ResourceLocation;", "width", "createElement", "", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "setImage", "Ljava/io/File;", "", "Companion", "KyinoClient"})
public final class Image
extends Element {
    private final TextValue image = new TextValue(this, "Image", ""){
        final /* synthetic */ Image this$0;

        public void fromJson(@NotNull JsonElement element) {
            Intrinsics.checkParameterIsNotNull(element, "element");
            super.fromJson(element);
            CharSequence charSequence = (CharSequence)this.get();
            boolean bl = false;
            if (charSequence.length() == 0) {
                return;
            }
            Image.access$setImage(this.this$0, (String)this.get());
        }

        protected void onChanged(@NotNull String oldValue, @NotNull String newValue) {
            Intrinsics.checkParameterIsNotNull(oldValue, "oldValue");
            Intrinsics.checkParameterIsNotNull(newValue, "newValue");
            CharSequence charSequence = (CharSequence)this.get();
            boolean bl = false;
            if (charSequence.length() == 0) {
                return;
            }
            Image.access$setImage(this.this$0, (String)this.get());
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2);
        }
    };
    private final ResourceLocation resourceLocation = new ResourceLocation(RandomUtils.randomNumber(128));
    private int width = 64;
    private int height = 64;
    public static final Companion Companion = new Companion(null);

    @Override
    @NotNull
    public Border drawElement() {
        RenderUtils.drawImage(this.resourceLocation, 0, 0, this.width / 2, this.height / 2);
        return new Border(0.0f, 0.0f, (float)this.width / 2.0f, (float)this.height / 2.0f);
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

    private final Image setImage(String image2) {
        try {
            this.image.changeValue(image2);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(image2));
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            byteArrayInputStream.close();
            BufferedImage bufferedImage2 = bufferedImage;
            Intrinsics.checkExpressionValueIsNotNull(bufferedImage2, "bufferedImage");
            this.width = bufferedImage2.getWidth();
            this.height = bufferedImage.getHeight();
            Minecraft minecraft = Image.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_110434_K().func_110579_a(this.resourceLocation, (ITextureObject)new DynamicTexture(bufferedImage));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @NotNull
    public final Image setImage(@NotNull File image2) {
        Intrinsics.checkParameterIsNotNull(image2, "image");
        try {
            String string = Base64.getEncoder().encodeToString(Files.readAllBytes(image2.toPath()));
            Intrinsics.checkExpressionValueIsNotNull(string, "Base64.getEncoder().enco\u2026AllBytes(image.toPath()))");
            this.setImage(string);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Image() {
        super(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ Image access$setImage(Image $this, String image2) {
        return $this.setImage(image2);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Image$Companion;", "", "()V", "default", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Image;", "KyinoClient"})
    public static final class Companion {
        @NotNull
        public final Image default() {
            Image image2 = new Image();
            image2.setX(0.0);
            image2.setY(0.0);
            return image2;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

