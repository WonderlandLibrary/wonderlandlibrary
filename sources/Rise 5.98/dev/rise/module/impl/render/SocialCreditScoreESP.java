package dev.rise.module.impl.render;

import dev.rise.Rise;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "SocialCreditScoreESP", description = "Gives people social credit score like in China", category = Category.RENDER)
public class SocialCreditScoreESP extends Module {

    @Override
    protected void onEnable() {
        Rise.addChatMessage("SocialCreditScoreESP may lag on large servers");
    }
}
