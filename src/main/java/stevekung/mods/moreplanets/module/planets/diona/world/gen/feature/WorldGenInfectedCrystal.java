package stevekung.mods.moreplanets.module.planets.diona.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import stevekung.mods.moreplanets.module.planets.diona.blocks.DionaBlocks;
import stevekung.mods.moreplanets.util.CachedEnumUtil;
import stevekung.mods.moreplanets.util.helper.BlockStateHelper;

public class WorldGenInfectedCrystal extends WorldGenerator
{
    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        for (int i = 0; i < 2; ++i)
        {
            for (EnumFacing facing : CachedEnumUtil.valuesEnumFacingCached())
            {
                Block block = DionaBlocks.LARGE_INFECTED_CRYSTALLIZE;

                if (world.isAirBlock(pos) && block.canPlaceBlockOnSide(world, pos, facing))
                {
                    world.setBlockState(pos, block.getDefaultState().withProperty(BlockStateHelper.FACING_ALL, facing), 2);
                }
            }
        }
        return true;
    }
}