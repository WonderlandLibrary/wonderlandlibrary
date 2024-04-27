/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
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
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.value.TextValue;
import org.jetbrains.annotations.NotNull;

@ElementInfo(name="Image")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0016J\u000e\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u000fJ\u0010\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Image;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "()V", "height", "", "image", "Lnet/ccbluex/liquidbounce/value/TextValue;", "resourceLocation", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "width", "createElement", "", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "setImage", "Ljava/io/File;", "", "Companion", "LiKingSense"})
public final class Image
extends Element {
    private final TextValue image = new TextValue(this, "Image", ""){
        final /* synthetic */ Image this$0;

        public void fromJson(@NotNull JsonElement element) {
            Intrinsics.checkParameterIsNotNull((Object)element, (String)"element");
            super.fromJson(element);
            CharSequence charSequence = (CharSequence)this.get();
            boolean bl = false;
            if (charSequence.length() == 0) {
                return;
            }
            Image.access$setImage(this.this$0, (String)this.get());
        }

        protected void onChanged(@NotNull String oldValue, @NotNull String newValue) {
            Intrinsics.checkParameterIsNotNull((Object)oldValue, (String)"oldValue");
            Intrinsics.checkParameterIsNotNull((Object)newValue, (String)"newValue");
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
    private final IResourceLocation resourceLocation = MinecraftInstance.classProvider.createResourceLocation(RandomUtils.INSTANCE.randomNumber(128));
    private int width = 64;
    private int height = 64;
    public static final Companion Companion = new Companion(null);

    /*
     * Exception decompiling
     */
    @Override
    @NotNull
    public Border drawElement() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl11 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
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
            Intrinsics.checkExpressionValueIsNotNull((Object)bufferedImage2, (String)"bufferedImage");
            this.width = bufferedImage2.getWidth();
            this.height = bufferedImage.getHeight();
            MinecraftInstance.mc.getTextureManager().loadTexture(this.resourceLocation, MinecraftInstance.classProvider.createDynamicTexture(bufferedImage));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @NotNull
    public final Image setImage(@NotNull File image2) {
        Intrinsics.checkParameterIsNotNull((Object)image2, (String)"image");
        try {
            String string = Base64.getEncoder().encodeToString(Files.readAllBytes(image2.toPath()));
            Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"Base64.getEncoder().enco\u2026AllBytes(image.toPath()))");
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

    public static final /* synthetic */ Image access$setImage(Image $this, String image2) {
        return $this.setImage(image2);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Image$Companion;", "", "()V", "default", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Image;", "LiKingSense"})
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

