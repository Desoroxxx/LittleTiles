package team.creative.littletiles.common.placement.mode;

import java.util.List;
import java.util.Set;

import com.creativemd.littletiles.common.action.block.LittleActionDestroyBoxes;

import net.minecraft.core.BlockPos;
import team.creative.littletiles.common.action.LittleActionException;
import team.creative.littletiles.common.block.little.tile.LittleTile;
import team.creative.littletiles.common.math.box.LittleBox;
import team.creative.littletiles.common.placement.PlacementContext;
import team.creative.littletiles.common.structure.LittleStructure;

public class PlacementModeStencil extends PlacementMode {
    
    public PlacementModeStencil(String name, PreviewMode mode) {
        super(name, mode, true);
    }
    
    @Override
    public boolean shouldConvertBlock() {
        return true;
    }
    
    @Override
    public boolean checkAll() {
        return false;
    }
    
    @Override
    public List<BlockPos> getCoordsToCheck(Set<BlockPos> splittedTiles, BlockPos pos) {
        return null;
    }
    
    @Override
    public boolean placeTile(PlacementContext context, LittleStructure structure, LittleTile tile) throws LittleActionException {
        if (!context.collisionTest)
            return false;
        
        for (LittleBox box : tile)
            for (LittleTile lt : LittleActionDestroyBoxes.removeBox(context.getBE(), context.block.getGrid(), box, false))
                context.addRemoved(lt);
            
        return false;
    }
}
