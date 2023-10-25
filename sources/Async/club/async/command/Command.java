package club.async.command;

import club.async.util.ChatUtil;
import net.minecraft.client.Minecraft;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Command {

    private final Info annotation = getClass().getAnnotation(Info.class);
    private final String name = annotation.name();
    private final String description = annotation.description();
    private final String usage = annotation.usage();
    private final String[] aliases = annotation.aliases();
    protected Minecraft mc = Minecraft.getMinecraft();

    public void execute(String[] args) {}

    public void usage() {
        ChatUtil.addChatMessage(getUsage());
    }

    public String name() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
        String description();
        String usage();
        String[] aliases();
    }

}
