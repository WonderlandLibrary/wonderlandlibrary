package epsilon.util.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.BlockPos;

public class BlockBypassInfo {

	private BlockPos b;
	
	public BlockBypassInfo(BlockPos b) {
		this.b = b;
	}
	
	public boolean blockExposed() {
		
		List<BlockPos> range = getBlocksInSpiral(b, 6);
		
		for(BlockPos bp : range) {
			
		}
		
		
		return false;
		
	}
	
	public static List<BlockPos> getAllBlocksInRangeTopDown(final BlockPos from, final BlockPos to) {
        List<BlockPos> blocks = new ArrayList<BlockPos>();
        
        for(double x = from.getX(); x<to.getX(); x++) {

            for(double y = from.getY(); y<to.getY(); y++) {

                for(double z = from.getZ(); z<to.getZ(); z++) {
                	
                	blocks.add(new BlockPos(x,y,z));
                	
                }
            }
        }
		
		return blocks;
		
	}
	
	public static List<BlockPos> getBlocksInSpiral(final BlockPos center, final int spread) {
		
        List<BlockPos> blocks = new ArrayList<BlockPos>();

        
        int rowBegin = 0, columnBegin = 0;
        int columnEnd = 0;
        int dir;
        
        while(rowBegin<=spread) {
        	
        	
        	
        }
        
		return blocks;
		
	}
	
	
	
}
