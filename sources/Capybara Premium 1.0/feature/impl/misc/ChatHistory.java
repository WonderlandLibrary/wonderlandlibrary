package fun.expensive.client.feature.impl.misc;

import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;

public class ChatHistory
        extends Feature {
    public ChatHistory() {
        super("ChatHistory", "Ќе удал€ет историю чата при перезаходе на сервер", FeatureCategory.Misc);
    }
}
