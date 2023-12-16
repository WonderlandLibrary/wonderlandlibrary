// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import moonsense.config.utils.AnchorPoint;
import moonsense.config.ModuleConfig;
import moonsense.features.SCModule;

public class BoxUtils
{
    public static int getOffsetX(final SCModule SCModule, final int width) {
        switch (ModuleConfig.INSTANCE.getPosition(SCModule).getAnchorPoint()) {
            case TOP_CENTER:
            case BOTTOM_CENTER:
            case CENTER: {
                return 0;
            }
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            case CENTER_RIGHT: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    public static int getOffsetY(final SCModule SCModule, final int height) {
        switch (ModuleConfig.INSTANCE.getPosition(SCModule).getAnchorPoint()) {
            case CENTER_LEFT:
            case CENTER:
            case CENTER_RIGHT: {
                return 0;
            }
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    public static int getBoxOffX(final SCModule SCModule, final int x, final int width) {
        switch (ModuleConfig.INSTANCE.getPosition(SCModule).getAnchorPoint()) {
            case TOP_CENTER:
            case BOTTOM_CENTER:
            case CENTER: {
                return x;
            }
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            case CENTER_RIGHT: {
                return x;
            }
            default: {
                return x;
            }
        }
    }
    
    public static int getBoxOffY(final SCModule SCModule, final int y, final int height) {
        switch (ModuleConfig.INSTANCE.getPosition(SCModule).getAnchorPoint()) {
            case CENTER_LEFT:
            case CENTER:
            case CENTER_RIGHT: {
                return y;
            }
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT: {
                return y;
            }
            default: {
                return y;
            }
        }
    }
}
