package com.masterof13fps;

import com.masterof13fps.manager.eventmanager.EventManager;
import com.masterof13fps.manager.fontmanager.FontManager;
import com.masterof13fps.utils.LoginUtil;
import com.masterof13fps.utils.MathUtils;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

public interface Wrapper {
    Minecraft mc = Client.main().getMc();
    Client cl = Client.getInstance();
    RenderUtils renderUtils = new RenderUtils();
    FontManager fontManager = new FontManager();
    LoginUtil loginUtil = new LoginUtil();
    NotifyUtil notify = new NotifyUtil();
    MathUtils mathUtils = new MathUtils();
    EventManager eventManager = new EventManager();
}
