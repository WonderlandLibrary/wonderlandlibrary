package koks.api.interfaces;

import koks.api.util.*;

/**
 * @author kroko
 * @created on 30.10.2020 : 17:24
 */
public interface Wrapper {

    ESPUtil espUtil = new ESPUtil();
    Logger logger = new Logger();
    LoginUtil loginUtil = new LoginUtil();
    MovementUtil movementUtil = new MovementUtil();
    RandomUtil randomUtil = new RandomUtil();
    RayCastUtil rayCastUtil = new RayCastUtil();
    RenderUtil renderUtil = new RenderUtil();
    RotationUtil rotationUtil = new RotationUtil();
    InventoryUtil inventoryUtil = new InventoryUtil();
    NumbersUtil numbersUtil = new NumbersUtil();
    CustomUtil customUtil = new CustomUtil();

}
