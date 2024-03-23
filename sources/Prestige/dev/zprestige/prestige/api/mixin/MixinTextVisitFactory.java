package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.NameEvent;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={TextVisitFactory.class})
public class MixinTextVisitFactory {
    @ModifyArg(method={"visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"}, at=@At(value="INVOKE", target="Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal=0), index=0)
    private static String visitFormatted(String string) {
        if (string != null) {
            if (!Prestige.Companion.getSelfDestructed()) {
                NameEvent event = new NameEvent("", string);
                if (event.invoke()) {
                    return string.replace(event.getFakeName(), event.getName());
                }
            }
            return string;
        }
        return null;
    }
}
