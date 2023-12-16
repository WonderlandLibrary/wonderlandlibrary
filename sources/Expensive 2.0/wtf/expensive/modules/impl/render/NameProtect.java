package wtf.expensive.modules.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import wtf.expensive.events.Event;
import wtf.expensive.friend.Friend;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.TextSetting;
import wtf.expensive.util.ClientUtil;

@FunctionAnnotation(name = "NameProtect", type = Type.Render)
public class NameProtect extends Function {

    public TextSetting name = new TextSetting("Ник", "expensive");
    public BooleanOption friends = new BooleanOption("Друзья", false);


    public NameProtect() {
        addSettings(name, friends);
    }

    @Override
    public void onEvent(Event event) {

    }

    public String patch(String text) {
        String out = text;
        if (this.state) {
            out = text.replaceAll(Minecraft.getInstance().session.getUsername(), name.text);
        }
        return out;
    }

    public ITextComponent patchFriendTextComponent(ITextComponent text, String name) {
        ITextComponent out = text;
        if (this.friends.get() && this.state) {
            out = ClientUtil.replace(text, name, this.name.text);
        }
        return out;
    }
}
