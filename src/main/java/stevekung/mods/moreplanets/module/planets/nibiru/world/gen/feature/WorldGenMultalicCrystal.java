package stevekung.mods.moreplanets.module.planets.nibiru.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import stevekung.mods.moreplanets.module.planets.nibiru.blocks.NibiruBlocks;
import stevekung.mods.moreplanets.util.CachedEnumUtil;
import stevekung.mods.moreplanets.util.helper.BlockStateHelper;

public class WorldGenMultalicCrystal extends WorldGenerator
{
    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        for (EnumFacing facing : CachedEnumUtil.valuesEnumFacingCached())
        {
            Block block = NibiruBlocks.MULTALIC_CRYSTAL;

            if (world.isAirBlock(pos) && block.canPlaceBlockOnSide(world, pos, facing))
            {
                world.setBlockState(pos, block.getDefaultState().withProperty(BlockStateHelper.FACING_ALL, facing), 2);
            }
        }
        return true;
    }
}