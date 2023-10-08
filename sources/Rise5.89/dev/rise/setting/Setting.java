package dev.rise.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Setting {
    public String name;
    public boolean hidden;
}
