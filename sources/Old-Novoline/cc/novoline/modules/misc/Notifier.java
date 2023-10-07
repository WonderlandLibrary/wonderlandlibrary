package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.SpawnCheckEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Notifier extends AbstractModule {

    @Property("additional_y")
    private IntProperty add_y = PropertyFactory.createInt(10).minimum(10).maximum(90);

    /* constructors */
    public Notifier(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Notifier", EnumModuleType.MISC, "alert you if player in your chunks");
        Manager.put(new Setting("NF_ADD_Y", "Addition Y", SettingType.SLIDER, this, add_y, 5));
    }

    /* methods */
    public boolean isServerBot(Entity entity) {
        return entity.getDisplayName().getFormattedText().contains("\u00A78[NPC]");
    }

    /* events */
    @EventTarget
    public void onCheck(SpawnCheckEvent e) {
        if (e.getEntity() instanceof EntityPlayer && e.getEntity().getEntityID() != mc.player.getEntityID() && !isServerBot(e.getEntity())) {
            int x = (int) e.getEntity().posX, y = (int) e.getEntity().posY, z = (int) e.getEntity().posZ;
            String tp = mc.isSingleplayer() ? "/tp " : ".tp ", playerName = e.getEntity().getName().toLowerCase();

            ChatComponentText message = new ChatComponentText("\u00A7dNotifier\u00A7r\u00A77 \u00BB ");
            ChatComponentText textName = new ChatComponentText("\u00A73Name: \u00A7r");
            ChatComponentText name = new ChatComponentText(playerName);

            name = (ChatComponentText) name.setChatStyle(name.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(tp + playerName))));
            name = (ChatComponentText) name.setChatStyle(name.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, tp + playerName)));

            ChatComponentText textCoords = new ChatComponentText(" \u00A73Coords: \u00A7r");
            ChatComponentText coords = new ChatComponentText("\u00A7bX:\u00A7r " + x + " \u00A7bY:\u00A7r " + y + " \u00A7bZ:\u00A7r " + z);
            String coordsCommand = tp + x + " " + y + " " + z;

            coords = (ChatComponentText) coords.setChatStyle(coords.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(coordsCommand))));
            coords = (ChatComponentText) coords.setChatStyle(coords.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, coordsCommand)));

            message.appendSibling(textName).appendSibling(name).appendSibling(textCoords).appendSibling(coords);
            mc.ingameGUI.getChatGUI().printChatMessage(message);
        }
    }
}
