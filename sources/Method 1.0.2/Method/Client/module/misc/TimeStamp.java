package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static Method.Client.Main.setmgr;

public class TimeStamp extends Module {
    /////////////////////
    public TimeStamp() {
        super("TimeStamp", Keyboard.KEY_NONE, Category.MISC, "TimeStamp");
    }

    Setting Formats = setmgr.add(new Setting("formats", this, "H24:mm", "H24:mm", "H12:mm", "H12:mm a", "H24:mm:ss", "H12:mm:ss", "H12:mm:ss a"));
    Setting Deco = setmgr.add(new Setting("Deco", this, "bracket", "bracket", "square", "curley", "none"));
    Setting Location = setmgr.add(new Setting("Location", this, "Start", "Start", "End"));
    Setting Colortype;
    Setting Textcolor;
    Setting BracketColor;

    @Override
    public void setup() {
        ArrayList<String> Color = new ArrayList<>();
        Color.add("GREEN");
        Color.add("BLACK");
        Color.add("DARK_BLUE");
        Color.add("DARK_GREEN");
        Color.add("DARK_AQUA");
        Color.add("DARK_RED");
        Color.add("DARK_PURPLE");
        Color.add("GOLD");
        Color.add("GRAY");
        Color.add("DARK_GRAY");
        Color.add("BLUE");
        Color.add("AQUA");
        Color.add("RED");
        Color.add("LIGHT_PURPLE");
        Color.add("YELLOW");
        Color.add("WHITE");
        setmgr.add(Colortype = new Setting("Time", this, "GREEN", Color));
        setmgr.add(Textcolor = new Setting("Text", this, "WHITE", Color));
        setmgr.add(BracketColor = new Setting("{C}", this, "WHITE", Color));
    }

    /////////////////////


    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof SPacketChat) {
            final SPacketChat packet2 = (SPacketChat) packet;
            String input = packet2.getChatComponent().getUnformattedText();
            if (Location.getValString().equalsIgnoreCase("Start")) {
                packet2.chatComponent = new TextComponentString(TmStamp() + " " + TextFormatting.getValueByName(Textcolor.getValString().toLowerCase()) + input + TextFormatting.RESET);
            }
            if (Location.getValString().equalsIgnoreCase("End")) {
                packet2.chatComponent = new TextComponentString(TextFormatting.getValueByName(Textcolor.getValString().toLowerCase()) + input + " " + TextFormatting.RESET + TmStamp() + TextFormatting.RESET);

            }
        }
        return true;
    }

    private String TmStamp() {
        String decoLeft = "";
        String decoRight = "";
        if (Deco.getValString().equalsIgnoreCase("bracket")) {
            decoLeft = "(";
            decoRight = ")";
        }
        if (Deco.getValString().equalsIgnoreCase("square")) {
            decoLeft = "[";
            decoRight = "]";
        }
        if (Deco.getValString().equalsIgnoreCase("curley")) {
            decoLeft = "{";
            decoRight = "}";
        }

        final String dateFormat = Formats.getValString().replace("H24", "k").replace("H12", "h");
        final String date = new SimpleDateFormat(dateFormat).format(new Date());

        final TextComponentString time = new TextComponentString(TextFormatting.getValueByName(BracketColor.getValString().toLowerCase()) + decoLeft + TextFormatting.RESET + TextFormatting.getValueByName(Colortype.getValString().toLowerCase()) + date + TextFormatting.RESET + TextFormatting.getValueByName(BracketColor.getValString().toLowerCase()) + decoRight + TextFormatting.RESET);
        return (time.getText());
    }
}
