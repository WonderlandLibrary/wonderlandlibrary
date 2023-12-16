package wtf.expensive.managment;

import wtf.expensive.command.CommandManager;
import wtf.expensive.command.macro.MacroManager;
import wtf.expensive.config.ConfigManager;
import wtf.expensive.config.LastAccountConfig;
import wtf.expensive.friend.FriendManager;
import wtf.expensive.modules.FunctionManager;
import wtf.expensive.notification.NotificationManager;
import wtf.expensive.proxy.ProxyConnection;
import wtf.expensive.scripts.ScriptManager;
import wtf.expensive.ui.alt.AltConfig;
import wtf.expensive.ui.alt.AltManager;
import wtf.expensive.ui.beta.ClickGui;
import wtf.expensive.ui.clickgui.Window;
import wtf.expensive.ui.midnight.StyleManager;
import wtf.expensive.util.UserProfile;

public class Managment {

    public static FunctionManager FUNCTION_MANAGER;
    public static CommandManager COMMAND_MANAGER;
    public static FriendManager FRIEND_MANAGER;
    public static MacroManager MACRO_MANAGER;
    public static LastAccountConfig LAST_ACCOUNT_CONFIG;
    public static ScriptManager SCRIPT_MANAGER;

    public static StaffManager STAFF_MANAGER;
    public static Window CLICK_GUI;
    public static ConfigManager CONFIG_MANAGER;
    public static StyleManager STYLE_MANAGER;
    public static UserProfile USER_PROFILE;
    public static NotificationManager NOTIFICATION_MANAGER;
    public static AltManager ALT;
    public static AltConfig ALT_CONFIG;

    public static ProxyConnection PROXY_CONN;
}
