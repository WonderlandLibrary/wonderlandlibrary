package Method.Client.modmaker;


import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;

import java.io.Serializable;

@SuppressWarnings("UnstableApiUsage")
public class CatchCodeExecuter extends CodeExecuter implements Serializable {

    // this is here by defualt, you can turn off Code Executer for optimal performance

    public CatchCodeExecuter(Module module) {
        super(module);
    }

    @Override
    public double solveNumerical(DragableBlock insertableBubble, int index, Object event) {
        try {
            return super.solveNumerical(insertableBubble, index, event);
        } catch (Exception e) {
            ChatUtils.warning("Error in numerical solve: " + e.getMessage());
            if (insertableBubble.inputBoxes.get(index) != null)
                ChatUtils.message(insertableBubble.inputBoxes.get(index).containedBlock.thisblock.getName() + " Failure!");
        }
        return 0;
    }

    @Override
    public boolean solveBoolean(DragableBlock insertableBubble, int index, Object event) {
        try {
            return super.solveBoolean(insertableBubble, index, event);
        } catch (Exception e) {
            if (insertableBubble.inputBoxes.get(index) != null)
                ChatUtils.message(insertableBubble.inputBoxes.get(index).containedBlock.thisblock.getName() + " Failure!");

        }
        return false;
    }

    @Override
    public Object solveObject(DragableBlock insertableBubble, int index, Object event) {
        try {
            return super.solveObject(insertableBubble, index, event);
        } catch (Exception e) {
            if (insertableBubble.inputBoxes.get(index) != null)
                ChatUtils.message(insertableBubble.inputBoxes.get(index).containedBlock.thisblock.getName() + " Failure!");

        }
        return null;
    }

    @Override
    public void headerRun(Headers headers, net.minecraftforge.fml.common.eventhandler.Event event) {
        try {
            super.headerRun(headers, event);
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtils.warning("Error in Header solve: " + e.getMessage());
            ChatUtils.message(headers.name() + " Failure!");
        }
    }



    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        try {
            return super.onPacket(packet, side);
        } catch (Exception e) {
            ChatUtils.warning("Error in Packet solve: " + e.getMessage());
            ChatUtils.message("Packet Failure!");


        }
        return true;
    }

    @Override
    public boolean onDisablePacket(Object packet) {
        try {
            return super.onDisablePacket(packet);
        } catch (Exception e) {
            ChatUtils.warning("Error in Packet solve: " + e.getMessage());
            ChatUtils.message("Disable Packet Failure!");
        }
        return true;
    }

}
