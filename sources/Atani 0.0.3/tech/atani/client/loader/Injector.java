package tech.atani.client.loader;

import net.minecraft.client.Minecraft;

public final class Injector
        extends Minecraft {

    public void checkGLError(String message) {
        super.checkGLError(message);
        switch (message) {
            case "Post startup":
                Modification.INSTANCE.start();
                break;
        }
    }
}