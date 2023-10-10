package info.sigmaclient;

import info.sigmaclient.module.impl.movement.Bhop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.renderer.texture.TextureManager;
import org.apache.logging.log4j.LogManager;

public class MCHook extends Minecraft {

    public MCHook(GameConfiguration gc) {
        super(gc);
    }
}
