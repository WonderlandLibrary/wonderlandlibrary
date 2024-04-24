package Method.Client.command;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public abstract class Command {

    @Retention(RetentionPolicy.RUNTIME)
    @interface CommandFeatures {
        String Syntax() default "No Syntax provided!";

        String description() default "No description provided!";
    }

    protected static Minecraft mc = Minecraft.getMinecraft();
    public final String Syntax = getAnnotation().Syntax();

    public abstract void runCommand(String s, String[] args);

    public CommandFeatures getAnnotation() {
        if (this.getClass().isAnnotationPresent(CommandFeatures.class)) {
            return getClass().getAnnotation(CommandFeatures.class);
        }

        throw new IllegalStateException("Annotation 'AnnotationHelper' not found!");
    }

    public static void updateSlot(int slot, ItemStack stack) {
        Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketCreativeInventoryAction(slot, stack));
    }

}

