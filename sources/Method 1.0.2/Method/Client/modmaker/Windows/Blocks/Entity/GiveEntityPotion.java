package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class GiveEntityPotion extends Block {

    public GiveEntityPotion() {
        super("GiveEntityPotion", MainBlockType.Default, Tabs.Entity,BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter);

        this.description = "Give a Entity A potion effect" + "\n" + "(EntityLivingBase).addPotionEffect";
        this.words[0]="Entity";
        this.words[1]="Potion";
        this.words[2]="Duration";
        this.words[3]="Amplifier";
        this.words[4]="Ambient";
        this.words[5]="ShowParticles";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Potion));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        // Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn
        PotionEffect s = new PotionEffect((Potion) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event),(int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event)
        ,(int)dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event),dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,4, event),dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,5, event));
        ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).addPotionEffect(s);
        super.runCode(dragableBlock, event);
    }

}
