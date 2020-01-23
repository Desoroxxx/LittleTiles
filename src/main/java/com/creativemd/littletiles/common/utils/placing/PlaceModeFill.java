package com.creativemd.littletiles.common.utils.placing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.place.PlacePreviews;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;

import net.minecraft.util.math.BlockPos;

public class PlaceModeFill extends PlacementMode {
	
	public PlaceModeFill(String name, PreviewMode mode) {
		super(name, mode, false);
	}
	
	@Override
	public boolean checkAll() {
		return false;
	}
	
	@Override
	public List<BlockPos> getCoordsToCheck(HashMap<BlockPos, PlacePreviews> splittedTiles, BlockPos pos) {
		return null;
	}
	
	@Override
	public List<LittleTile> placeTile(TileEntityLittleTiles te, LittleTile tile, List<LittleTile> unplaceableTiles, List<LittleTile> removedTiles, boolean requiresCollisionTest) {
		List<LittleTile> tiles = new ArrayList<>();
		if (!requiresCollisionTest) {
			tiles.add(tile);
			return tiles;
		}
		
		List<LittleBox> cutout = new ArrayList<>();
		List<LittleBox> boxes = te.cutOut(tile.box, cutout);
		
		for (LittleBox box : boxes) {
			LittleTile newTile = tile.copy();
			newTile.box = box;
			tiles.add(newTile);
		}
		
		for (LittleBox box : cutout) {
			LittleTile newTile = tile.copy();
			newTile.box = box;
			unplaceableTiles.add(newTile);
		}
		
		return tiles;
	}
}
