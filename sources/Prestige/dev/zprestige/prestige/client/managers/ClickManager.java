package dev.zprestige.prestige.client.managers;

import dev.zprestige.prestige.api.mixin.IMouse;
import dev.zprestige.prestige.client.util.MC;
import dev.zprestige.prestige.client.util.impl.ClickUtil;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import net.minecraft.client.MinecraftClient;

public class ClickManager implements MC {

    private ClickUtil clickUtil;

    public boolean click() {
        if (clickUtil != null) {
            if (System.currentTimeMillis() - clickUtil.getTime() > clickUtil.getIdk() && !clickUtil.shouldClick()) {
                ((IMouse)getMc().mouse).handleMouseButton(getMc().getWindow().getHandle(), clickUtil.getMode(), 1, 0);
                clickUtil.setClick(true);
            }
            if (System.currentTimeMillis() - clickUtil.getTime() > clickUtil.getIdk()) {
                if (System.currentTimeMillis() - clickUtil.getTime() > clickUtil.getIdk() + clickUtil.getRandom()) {
                    ((IMouse)getMc().mouse).handleMouseButton(getMc().getWindow().getHandle(), clickUtil.getMode(), 0, 0);
                    clickUtil = null;
                }
            }
            return true;
        }
        return false;
    }

    public void setClick(int n, float f) {
        clickUtil = new ClickUtil(n, System.currentTimeMillis(), f, RandomUtil.INSTANCE.randomInRange(RandomUtil.INSTANCE.randomInRange(5, 15), RandomUtil.INSTANCE.randomInRange(35, 55)), false);
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }
}
