package com.creativemd.littletiles.common.api.te;

import com.creativemd.littletiles.common.mods.chiselsandbits.ChiselsAndBitsManager;

import net.minecraftforge.fml.common.Optional.Method;

public interface ILittleTileTE {
	
	/**
	 * Returns a converted version of the TileEntity.
	 * @param force if false an exception will be thrown if a tile cannot be converted, if true it will try to convert it anyway
	 * @return VoxelBlob object from C&B
	 * @throws Exception
	 */
	@Method(modid = ChiselsAndBitsManager.chiselsandbitsID)
	public Object getVoxelBlob(boolean force) throws Exception;

}
