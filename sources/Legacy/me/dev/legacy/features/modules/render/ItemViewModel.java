package me.dev.legacy.features.modules.render;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class ItemViewModel extends Module {

    public static ItemViewModel INSTANCE;

    public ItemViewModel( ) {
        super ( "ItemViewModel" , "Changes to the viewmodel." , Category.RENDER , false , false , false );
        INSTANCE = this;
    }

    public final Setting<Integer> translateX = this.register(new Setting("TranslateX", 0, -100, 100));
    public final Setting<Integer> translateY = this.register(new Setting("TranslateY", 0, -100, 100));
    public final Setting<Integer> translateZ = this.register(new Setting("TranslateZ", 0, -100, 100));

    public final Setting<Integer> rotateX = this.register(new Setting("RotateX", 0, -100, 100));
    public final Setting<Integer> rotateY = this.register(new Setting("RotateY", 0, -100, 100));
    public final Setting<Integer> rotateZ = this.register(new Setting("RotateZ", 0, -100, 100));

    public final Setting<Integer> scaleX = this.register(new Setting("ScaleX", 100, 0, 100));
    public final Setting<Integer> scaleY = this.register(new Setting("ScaleY", 100, 0, 100));
    public final Setting<Integer> scaleZ = this.register(new Setting("ScaleZ", 100, 0, 100));

}
