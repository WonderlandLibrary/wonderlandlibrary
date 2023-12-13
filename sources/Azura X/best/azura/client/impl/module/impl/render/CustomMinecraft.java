package best.azura.client.impl.module.impl.render;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.BooleanValue;

@ModuleInfo(name = "Custom Minecraft", description = "Customize your minecraft feeling!", category = Category.RENDER)
public class CustomMinecraft extends Module {

    public final BooleanValue toolTip = new BooleanValue("Item tool tip", "Enable the Client font for the Item tooltip", false);
    public final BooleanValue chatMessageLength = new BooleanValue("Chat background length", "Change the background length to your message length", false);
    public final BooleanValue chatNoBackground = new BooleanValue("No chat background", "Disable or enable the chat background", false);
    public final BooleanValue fancyChat = new BooleanValue("Fancy Chat", "Chat fade-in", false);
    public final BooleanValue noEXPBar = new BooleanValue("No XP Bar", "Cancel exp bar rendering", false);


}
