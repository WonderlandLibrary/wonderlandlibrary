package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.TerrainScreenEvent;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={DownloadingTerrainScreen.class})
public class MixinDownloadingTerrainScreen extends Screen {
    public MixinDownloadingTerrainScreen(Text text) {
        super(text);
    }

    @Inject(at={@At(value="HEAD")}, method={"tick"})
    void tick(CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (new TerrainScreenEvent().invoke()) {
            close();
        }
    }
}
