package me.aquavit.liquidsense.ui.client.clickgui.neverlose.module;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Main;
import me.aquavit.liquidsense.ui.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.stream.Collectors;

public class DrawModule {

    public static int keyevent = 0;
    public static Translate translate = new Translate(0f, 0f);

    public static void drawModule(int mouseX, int mouseY, Main main) {
        switch (Impl.theType) {
            case CLIENT:
                int lmodulesize = 0;
                int rmodulesize = 0;
                int modulesize = 0;
                int line = 0;
                int line2 = 0;
                int positiony = 0;
                int positiony2 = 0;

                //执行排除和排序
                main.modules = LiquidSense.moduleManager.getModules().stream()
                        .filter(module -> {
                            String search = Impl.Search.toLowerCase();
                            return (module.getName().toLowerCase().contains(search) && !search.isEmpty()) ||
                                    (module.getCategory().displayName.equals(Impl.selectedCategory) && search.isEmpty()) ||
                                    (search.equals("enabler") && module.getState());
                        })
                        .collect(Collectors.toList());

                //简化for循环 遍历绘制
                for (int index = 0; index < main.modules.size(); index++) {
                    Module module = main.modules.get(index);

                    if (index % 2 == 0) {

                        //当功能绘制每超过或者等于两个的时候将下一个功能向下扩展
                        if (index == 2 * line) line++;

                        //计算当前这个功能头顶一个功能的总长
                        if (index - 2 >= 0)
                            positiony += main.modules.get(index - 2).getValues().size() * 20 + main.modules.get(index - 2).getOutvalue() * 12;

                        //模块顶部
                        int moduletop = (25 * line);

                        //模块坐标
                        int positionY = (int) (Impl.coordinateY + 40 + moduletop + positiony + main.lwheeltranslate.getY());

                        //超过显示的范围不会绘制
                        if (positionY < Impl.coordinateY + 325 && positionY + (module.getOutvalue() * 12 + module.getValues().size() * 20) > Impl.coordinateY) {
                            CustomModule nModule = new CustomModule((int) (Impl.coordinateX + 103), positionY, mouseX, mouseY, module, main);
                            nModule.drawModule();
                        }

                        //左边功能总数加
                        lmodulesize++;
                    }

                    if (index % 2 != 0) {
                        //当功能绘制每超过或者等于两个的时候将下一个功能向下扩展
                        if (index >= line2) line2++;
                        //计算当前这个功能头顶一个功能的总长
                        if (index - 2 >= 0)
                            positiony2 += main.modules.get(index - 2).getValues().size() * 20 + main.modules.get(index - 2).getOutvalue() * 12;
                        //模块顶部
                        int moduletop = (25 * line2);
                        //模块坐标
                        int positionY = (int) (Impl.coordinateY + 40 + moduletop + positiony2 + main.rwheeltranslate.getY());
                        //超过显示的范围不会绘制
                        if (positionY < Impl.coordinateY + 325 && positionY + (module.getOutvalue() * 12 + module.getValues().size() * 20) > Impl.coordinateY) {
                            CustomModule nModule = new CustomModule((int) (Impl.coordinateX + 103 + 178), positionY, mouseX, mouseY, module, main);
                            nModule.drawModule();
                        }

                        //右边功能总数加
                        rmodulesize++;
                    }

                    modulesize++;
                }

                try {
                    //防止功能是空的崩溃
                    if (!main.modules.isEmpty()) {

                        //左边功能的总长
                        int leftLastValueSize =
                                ((lmodulesize + rmodulesize >= 2) ? modulesize : lmodulesize) % 2 == 0 ?
                                        main.modules.get(main.modules.size() - 2).getValues().size() * 20 +
                                                main.modules.get(main.modules.size() - 2).getOutvalue() * 12 :
                                        main.modules.get(main.modules.size() - 1).getValues().size() * 20 +
                                                main.modules.get(main.modules.size() - 1).getOutvalue() * 12;

                        //右边功能的总长
                        int rightLastValueSize =
                                ((lmodulesize + rmodulesize >= 2) ? modulesize : rmodulesize) % 2 == 0 ?
                                        main.modules.get(main.modules.size() - 1).getValues().size() * 20 +
                                                main.modules.get(main.modules.size() - 1).getOutvalue() * 12 :
                                        main.modules.get(main.modules.size() - 2).getValues().size() * 20 +
                                                main.modules.get(main.modules.size() - 2).getOutvalue() * 12;

                        //右边功能的总长
                        int leftMax = line * 25 + positiony + leftLastValueSize + 50;
                        int rightMax = line2 * 25 + positiony2 + rightLastValueSize + 50;
                        int maxWheel = leftMax + rightMax;

                        //将滚轮坐标锁定到 205 的范围里面
                        float wheelValue = ((285 - 85) * Math.abs(main.lwheeltranslate.getY() + main.rwheeltranslate.getY()) /
                                (float) (maxWheel - (650 - ((leftMax < 350 ? 325 - leftMax : 0) + (rightMax < 350 ? 325 - rightMax : 0)))));
                        //滚轮draw
                        int rectColor;
                        int wheelColor;
                        switch (Impl.hue) {
                            case "black":
                                rectColor = new Color(29, 35, 37).getRGB();
                                wheelColor = new Color(11, 11, 11).getRGB();
                                break;
                            case "white":
                                rectColor = new Color(213, 213, 213).getRGB();
                                wheelColor = new Color(245, 245, 245).getRGB();
                                break;
                            default:
                                rectColor = new Color(29, 35, 37).getRGB();
                                wheelColor = new Color(21, 96, 135).getRGB();
                                break;
                        }

                        RenderUtils.drawRect(Impl.coordinateX + 456, Impl.coordinateY + 65, Impl.coordinateX + 458, Impl.coordinateY + 335,
                                rectColor);
                        RenderUtils.drawRect(Impl.coordinateX + 456, Impl.coordinateY + 65 + wheelValue, Impl.coordinateX + 458,
                                Impl.coordinateY + 50 + 85 + wheelValue, wheelColor);

                        //滚轮动画
                        main.lwheeltranslate.translate(0f, Impl.lwheel, 3.0);
                        main.rwheeltranslate.translate(0f, Impl.rwheel, 3.0);

                        //左边 【当搜索不是空的时候换一种算法 防止打开ListValue 卡住】
                        int leftValue = 320 - leftMax;
                        if (Math.abs(Impl.lwheel) > leftValue && leftMax > leftValue && leftMax < 320)
                            Impl.lwheel = 0f;

                        //右边 【当搜索不是空的时候换一种算法 防止打开ListValue 卡住】
                        int rightValue = 320 - rightMax;
                        if (Math.abs(Impl.rwheel) > rightValue && rightMax > rightValue && rightMax < 320)
                            Impl.rwheel = 0f;

                        //左边 滚轮超过最长的 反弹
                        if ((Math.abs(Impl.lwheel) > leftValue - 320 && leftValue > 320))
                            Impl.lwheel = -(leftMax - 320f);
                        //右边 滚轮超过最长的 反弹
                        if (Math.abs(Impl.rwheel) > rightMax - 320 && rightMax > 320) Impl.rwheel = -(rightMax - 320f);

                        //判断鼠标悬浮的位置在模块背景上
                        if (main.hovertoFloatL(Impl.coordinateX + 95f, Impl.coordinateY, Impl.coordinateX + 430f,
                                Impl.coordinateY + 345f, mouseX, mouseY, false)) {
                            //为什么在这里新建 是为了防止卡滚轮 不刷新
                            int dWheel = Mouse.getDWheel();
                            //左边滚轮主体
                            if (leftMax > 325) {
                                for (int i = 0; i < 10; i++) {
                                    if (dWheel < 0 && Math.abs(Impl.lwheel) < leftMax - 325) {
                                        Impl.lwheel -= i;
                                    } else if (dWheel > 0) {
                                        Impl.lwheel += i;
                                        if (Impl.lwheel > 0)
                                            Impl.lwheel = 0f;
                                    }
                                }
                            }
                            //右边滚轮主体
                            if (rightMax > 325) {
                                for (int i = 0; i < 10; i++) {
                                    if (dWheel < 0 && Math.abs(Impl.rwheel) < rightMax - 325) {
                                        Impl.rwheel -= i;
                                    } else if (dWheel > 0) {
                                        Impl.rwheel += i;
                                        if (Impl.rwheel > 0)
                                            Impl.rwheel = 0f;
                                    }
                                }
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
        }

        // 中间管理空的时候返回
        if (Impl.midmangermodule == null) return;

        // 中间管理开启的时候动画
        if (!Impl.midmangermodule.getArray()) {
            translate.translate(10f, Impl.openmidmanger ? 1f : 0f, 5.0);
        } else {
            translate.translate(0f, Impl.openmidmanger ? 1f : 0f, 5.0);
        }

        // 动画已经在运动的时候
        if (translate.getY() > 0) {
            // 横向坐标
            float positionx = (Impl.coordinateX + Impl.midmangerPositionX) / translate.getY();
            // 竖向坐标
            float positiony = (Impl.coordinateY + Impl.midmangerPositionY) / translate.getY();

            // 按下中间的时候以鼠标位中兴 执行想右下的动画
            GlStateManager.pushMatrix();
            GlStateManager.scale(translate.getY(), translate.getY(), translate.getY());
            // 绘制背景阴影双倍增加透明值
            RenderUtils.drawShader(positionx, positiony, 125f, 100f, new Color(30, 190, 255).getRGB());
            RenderUtils.drawShader(positionx, positiony, 125f, 100f, new Color(30, 190, 255).getRGB());

            int rectColor;
            int topColor;
            int textColor;
            // 绘制背景
            switch (Impl.hue) {
                case "black":
                    rectColor = new Color(11, 11, 11).getRGB();
                    topColor = new Color(29, 29, 29).getRGB();
                    textColor = new Color(255, 255, 255).getRGB();
                    break;
                case "white":
                    rectColor = new Color(240, 245, 248).getRGB();
                    topColor = new Color(213, 213, 213).getRGB();
                    textColor = new Color(90, 90, 90).getRGB();
                    break;
                default:
                    rectColor = new Color(0, 14, 26).getRGB();
                    topColor = new Color(16, 31, 33).getRGB();
                    textColor = new Color(255, 255, 255).getRGB();
                    break;
            }

            RenderUtils.drawRect(positionx, positiony, positionx + 125, positiony + 100, rectColor);
            // topRect
            RenderUtils.drawRect(positionx + 2, positiony + 15, positionx + 123, positiony + 16, topColor);
            // 开启中间管理的功能名字
            Fonts.font18.drawString(Impl.midmangermodule.getName(), positionx + 5, positiony + 5, textColor);

            // 按键绑定
            // keybind
            String keyname = Keyboard.getKeyName(Impl.midmangermodule.getKeyBind());
            float kbpositionx = positionx + 80f - 0.25f;
            boolean hoverkeybind = main.hoverConfig(kbpositionx, positiony + 19, kbpositionx + 40f, positiony + 31f, mouseX, mouseY, false);

            int stringColor;
            int hoverBorderColor;
            int nameColor;
            int hideColor;
            int backgroundColor;
            Color circleColor;
            int borderColor;
            switch (Impl.hue) {
                case "black":
                    stringColor = -1;
                    hoverBorderColor = !hoverkeybind ? new Color(13, 13, 13).getRGB() : new Color(30, 30, 30).getRGB();
                    nameColor = new Color(175, 175, 175).getRGB();
                    hideColor = !Impl.midmangermodule.getArray() ? -1 : new Color(175, 175, 175).getRGB();
                    backgroundColor = !Impl.midmangermodule.getArray() ? new Color(3, 23, 46).getRGB() : new Color(7, 19, 31).getRGB();
                    circleColor = !Impl.midmangermodule.getArray() ? new Color(3, 168, 245) : new Color(74, 87, 97);
                    borderColor = new Color(13, 13, 13).getRGB();
                    break;
                case "white":
                    stringColor = new Color(90, 90, 90).getRGB();
                    hoverBorderColor = !hoverkeybind ? new Color(255, 255, 255).getRGB() : new Color(230, 230, 230).getRGB();
                    nameColor = new Color(0, 90, 90).getRGB();
                    hideColor = !Impl.midmangermodule.getArray() ? new Color(90, 90, 90).getRGB() : new Color(145, 145, 145).getRGB();
                    backgroundColor = !Impl.midmangermodule.getArray() ? new Color(0, 120, 194).getRGB() : new Color(230, 230, 230).getRGB();
                    circleColor = !Impl.midmangermodule.getArray() ? new Color(94, 222, 255) : new Color(255, 255, 255);
                    borderColor = new Color(255, 255, 255).getRGB();
                    break;
                default:
                    stringColor = -1;
                    hoverBorderColor = !hoverkeybind ? new Color(2, 5, 12).getRGB() : new Color(8, 15, 24).getRGB();
                    nameColor = new Color(175, 175, 175).getRGB();
                    hideColor = !Impl.midmangermodule.getArray() ? -1 : new Color(175, 175, 175).getRGB();
                    backgroundColor = !Impl.midmangermodule.getArray() ? new Color(3, 23, 46).getRGB() : new Color(3, 5, 13).getRGB();
                    circleColor = !Impl.midmangermodule.getArray() ? new Color(3, 168, 245) : new Color(74, 87, 97);
                    borderColor = new Color(2, 5, 12).getRGB();
                    break;
            }

            Fonts.font18.drawString("Keybind", positionx + 5, positiony + 22, stringColor);

            // 排除其他按键
            if (Keyboard.getEventKey() != keyevent && hoverkeybind && Keyboard.getKeyName(Keyboard.getEventKey()).length() < 2 || Keyboard.getEventKey() == Keyboard.KEY_DELETE) {
                Impl.midmangermodule.setKeyBind(Keyboard.getEventKey() == Keyboard.KEY_DELETE ? Keyboard.KEY_NONE : Keyboard.getEventKey());
            }
            keyevent = Keyboard.getEventKey();

            // 绘制按键绑定背景
            RenderUtils.drawRoundedRect(kbpositionx, positiony + 19 - 0.25f, 40f - 0.5f, 13f - 0.5f, 2f,
                    hoverBorderColor, 0.2f, new Color(45, 45, 45).getRGB());
            // 绘制按键绑定名字
            Fonts.font17.drawString(keyname, positionx + 83, positiony + 23f, nameColor);

            // 从arraylist中隐藏按钮
            // hide
            Fonts.font18.drawString("Hide", positionx + 5, positiony + 40, hideColor);
            RenderUtils.drawNLRect(positionx + 90f, positiony + 41, positionx + 108f, positiony + 47, 2.5f, backgroundColor);
            RenderUtils.drawFullCircle(positionx + 95f + translate.getX(), positiony + 44, 4f, 0f, circleColor);
            RenderUtils.drawRoundedRect(positionx + 5, positiony + 69 - 0.25f, 115f - 0.5f, 13f - 0.5f, 2f,
                    borderColor, 0.2f, new Color(45, 45, 45).getRGB());

            GlStateManager.resetColor();

            // 悬浮
            boolean hoverarray = main.hoverConfig(positionx + 90f, positiony + 38, positionx + 110f, positiony + 50, mouseX, mouseY, true);
            if (hoverarray) Impl.midmangermodule.setArray(!Impl.midmangermodule.getArray());

            // 设置假名 也是在arraylis显示 展示不可以更改
            Fonts.font18.drawString("SetName", positionx + 5, positiony + 57, stringColor);
            Fonts.font18.drawString(Impl.midmangerSetnameString, positionx + 8, positiony + 73, stringColor);
            GlStateManager.resetColor();
            GlStateManager.popMatrix();
        }
    }

}

