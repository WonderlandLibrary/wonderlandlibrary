package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.impl.combat.aura.Aura;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.utils.property.impl.StringsProperty;

public class BlockAnimation extends Cheat {
    public StringsProperty mode;

    public BlockAnimation() {
        super("Animations", "Different Sword block animations", CheatCategory.VISUAL);
        this.mode = new StringsProperty("Mode", "How this cheat will function.", null, false, true, new String[]{"Helium", "Matt", "Remix", "Shotbowxd", "1.7", "Sigma", "Slide", "Dortware", "Poke", "oHare", "Swang", "In", "Astro", "Exhibition", "Spin"}, new Boolean[]{true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false});
        this.registerProperties(this.mode);
    }

    @Collect
    public void playerUpdate(PlayerUpdateEvent event) {
        setMode(mode.getSelectedStrings().get(0));
    }
}