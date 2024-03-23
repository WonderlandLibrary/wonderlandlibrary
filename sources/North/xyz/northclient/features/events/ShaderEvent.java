package xyz.northclient.features.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.northclient.features.Event;

@AllArgsConstructor
@Getter
public class ShaderEvent extends Event {
    private ShaderStage stage;
    public enum ShaderStage {
        BLUR,
        BLOOM;
    }
}
