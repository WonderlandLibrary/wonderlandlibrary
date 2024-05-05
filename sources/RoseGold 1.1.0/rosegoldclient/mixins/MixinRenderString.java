package rosegoldclient.mixins;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import rosegoldclient.Main;

@Mixin(FontRenderer.class)
public abstract class MixinRenderString {


    @Shadow
    public abstract int getCharWidth(char character);

    /**
     * @author FontRenderer
     * @reason because
     */
    @Overwrite
    public int getStringWidth(String text) {
        if (text == null) return 0;
        text = doNameThing(text);
        int i = 0;
        boolean flag = false;

        for (int j = 0; j < text.length(); ++j) {
            char c0 = text.charAt(j);
            int k = getCharWidth(c0);

            if (k < 0 && j < text.length() - 1) {
                ++j;
                c0 = text.charAt(j);

                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag = false;
                    }
                } else {
                    flag = true;
                }

                k = 0;
            }

            i += k;

            if (flag && k > 0) {
                ++i;
            }
        }
        return i;
    }

    @ModifyVariable(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value = "FIELD"))
    private String replace(String text) {
        if (!Main.configFile.anon) return text;
        return doNameThing(text);
    }

    private String doNameThing(String text) {
        if(text.contains("Randomize Text") || text.contains("Randomize text") || text.contains("§bPowered by: §aRoseGoldClient")) return text;
        if(Main.configFile.anon) {
            if (Main.configFile.randomizeAnon) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    int index = Main.alphaNumeric.indexOf(String.valueOf(text.charAt(i)));
                    if (index != -1) {
                        if (i > 0 && text.charAt(i - 1) == '§') {
                            sb.append(text.charAt(i));
                        } else {
                            sb.append(Main.shuffle.get(index));
                        }
                    } else {
                        sb.append(text.charAt(i));
                    }
                }
                text = sb.toString();
            } else {
                text = "";
            }
        }
        return text;
    }
}
