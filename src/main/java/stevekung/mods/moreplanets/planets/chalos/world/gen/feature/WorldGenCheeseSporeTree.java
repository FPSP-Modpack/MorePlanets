package stevekung.mods.moreplanets.planets.chalos.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.stevekunglib.utils.BlockStateProperty;
import stevekung.mods.stevekunglib.utils.BlockStateProperty.EnumAxis;

public class WorldGenCheeseSporeTree extends WorldGenAbstractTree
{
    private final int blockMaxHigh;
    private final boolean genSpore;

    public WorldGenCheeseSporeTree(int blockMaxHigh, boolean genSpore)
    {
        super(false);
        this.blockMaxHigh = blockMaxHigh;
        this.genSpore = genSpore;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int i = rand.nextInt(3) + this.blockMaxHigh;
        boolean flag = true;

        if (pos.getY() >= 1 && pos.getY() + i + 1 <= 256)
        {
            for (int j = pos.getY(); j <= pos.getY() + 1 + i; ++j)
            {
                int k = 1;

                if (j == pos.getY())
                {
                    k = 0;
                }
                if (j >= pos.getY() + 1 + i - 2)
                {
                    k = 2;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = pos.getX() - k; l <= pos.getX() + k && flag; ++l)
                {
                    for (int i1 = pos.getZ() - k; i1 <= pos.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < 256)
                        {
                            if (!this.isReplaceable(world,blockpos$mutableblockpos.setPos(l, j, i1)))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                BlockPos down = pos.down();
                Block blockSoil = world.getBlockState(down).getBlock();

                if (blockSoil != MPBlocks.CHEESE_GRASS_BLOCK && blockSoil != MPBlocks.CHEESE_DIRT && blockSoil != MPBlocks.CHEESE_COARSE_DIRT)
                {
                    return false;
                }
                else
                {
                    blockSoil.onPlantGrow(world.getBlockState(down), world, down, pos);

                    for (int b = 0; b < this.blockMaxHigh; b++)
                    {
                        if (this.isReplaceable(world, new BlockPos(x, y + b, z)))
                        {
                            this.setBlockAndNotifyAdequately(world, new BlockPos(x, y + b, z), MPBlocks.CHEESE_SPORE_STEM.getDefaultState());
                        }
                    }

                    if (this.genSpore)
                    {
                        if (this.isReplaceable(world, new BlockPos(x, y + this.blockMaxHigh, z)))
                        {
                            this.setBlockAndNotifyAdequately(world, new BlockPos(x, y + this.blockMaxHigh, z), MPBlocks.CHEESE_SPORE.getDefaultState());
                        }
                    }
                    if (this.isReplaceable(world, new BlockPos(x + 1, y, z)))
                    {
                        blockSoil.onPlantGrow(world.getBlockState(new BlockPos(x + 1, y - 1, z)), world, new BlockPos(x + 1, y - 1, z), pos);
                        this.setBlockAndNotifyAdequately(world, new BlockPos(x + 1, y, z), MPBlocks.CHEESE_SPORE_STEM.getDefaultState().withProperty(BlockStateProperty.AXIS, EnumAxis.X));
                    }
                    if (this.isReplaceable(world, new BlockPos(x - 1, y, z)))
                    {
                        blockSoil.onPlantGrow(world.getBlockState(new BlockPos(x - 1, y - 1, z)), world, new BlockPos(x - 1, y - 1, z), pos);
                        this.setBlockAndNotifyAdequately(world, new BlockPos(x - 1, y, z), MPBlocks.CHEESE_SPORE_STEM.getDefaultState().withProperty(BlockStateProperty.AXIS, EnumAxis.X));
                    }
                    if (this.isReplaceable(world, new BlockPos(x, y, z + 1)))
                    {
                        blockSoil.onPlantGrow(world.getBlockState(new BlockPos(x, y - 1, z + 1)), world, new BlockPos(x, y - 1, z + 1), pos);
                        this.setBlockAndNotifyAdequately(world, new BlockPos(x, y, z + 1), MPBlocks.CHEESE_SPORE_STEM.getDefaultState().withProperty(BlockStateProperty.AXIS, EnumAxis.Z));
                    }
                    if (this.isReplaceable(world, new BlockPos(x, y, z - 1)))
                    {
                        blockSoil.onPlantGrow(world.getBlockState(new BlockPos(x, y - 1, z - 1)), world, new BlockPos(x, y - 1, z - 1), pos);
                        this.setBlockAndNotifyAdequately(world, new BlockPos(x, y, z - 1), MPBlocks.CHEESE_SPORE_STEM.getDefaultState().withProperty(BlockStateProperty.AXIS, EnumAxis.Z));
                    }

                    this.setBlockAndNotifyAdequately(world, new BlockPos(x + 1, y + this.blockMaxHigh, z), MPBlocks.CHEESE_SPORE_STEM.getDefaultState().withProperty(BlockStateProperty.AXIS, EnumAxis.X));
                    this.setBlockAndNotifyAdequately(world, new BlockPos(x - 1, y + this.blockMaxHigh, z), MPBlocks.CHEESE_SPORE_STEM.getDefaultState().withProperty(BlockStateProperty.AXIS, EnumAxis.X));
                    this.setBlockAndNotifyAdequately(world, new BlockPos(x, y + this.blockMaxHigh, z + 1), MPBlocks.CHEESE_SPORE_STEM.getDefaultState().withProperty(BlockStateProperty.AXIS, EnumAxis.Z));
                    this.setBlockAndNotifyAdequately(world, new BlockPos(x, y + this.blockMaxHigh, z - 1), MPBlocks.CHEESE_SPORE_STEM.getDefaultState().withProperty(BlockStateProperty.AXIS, EnumAxis.Z));
                    this.setBlockAndNotifyAdequately(world, new BlockPos(x + 2, y + this.blockMaxHigh + 1, z), MPBlocks.CHEESE_SPORE_STEM.getDefaultState());
                    this.setBlockAndNotifyAdequately(world, new BlockPos(x - 2, y + this.blockMaxHigh + 1, z), MPBlocks.CHEESE_SPORE_STEM.getDefaultState());
                    this.setBlockAndNotifyAdequately(world, new BlockPos(x, y + this.blockMaxHigh + 1, z + 2), MPBlocks.CHEESE_SPORE_STEM.getDefaultState());
                    this.setBlockAndNotifyAdequately(world, new BlockPos(x, y + this.blockMaxHigh + 1, z - 2), MPBlocks.CHEESE_SPORE_STEM.getDefaultState());

                    int i4 = 3;
                    int i5 = 4;

                    for (int k1 = y - i4 + i5; k1 <= y + i5; ++k1)
                    {
                        int height = k1 - (y + 4);
                        int width = 1 - height / 2;

                        for (int i2 = x - width; i2 <= x + width; ++i2)
                        {
                            int j2 = i2 - x;

                            for (int k2 = z - width; k2 <= z + width; ++k2)
                            {
                                int l2 = k2 - z;

                                if (this.genSpore)
                                {
                                    if (height != 1)
                                    {
                                        this.setBlockAndNotifyAdequately(world, new BlockPos(i2, this.blockMaxHigh + k1 + 1, k2), MPBlocks.CHEESE_SPORE.getDefaultState());
                                    }
                                    if ((Math.abs(j2) != width || Math.abs(l2) != width) && height != 0)
                                    {
                                        this.setBlockAndNotifyAdequately(world, new BlockPos(i2, this.blockMaxHigh + k1 + 2, k2), MPBlocks.CHEESE_SPORE.getDefaultState());
                                    }
                                    if (Math.abs(j2) != width || Math.abs(l2) != width)
                                    {
                                        Block block1 = world.getBlockState(new BlockPos(i2, this.blockMaxHigh + k1, k2)).getBlock();

                                        if (block1.isAir(world.getBlockState(new BlockPos(i2, this.blockMaxHigh + k1, k2)), world, new BlockPos(i2, this.blockMaxHigh + k1, k2)))
                                        {
                                            this.setBlockAndNotifyAdequately(world, new BlockPos(i2, this.blockMaxHigh + k1, k2), MPBlocks.CHEESE_SPORE.getDefaultState());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    protected boolean canGrowInto(Block blockType)
    {
        return super.canGrowInto(blockType) || blockType == MPBlocks.CHEESE_SPORE_STEM || blockType == MPBlocks.CHEESE_GRASS || blockType == MPBlocks.CHEESE_SPORE_FLOWER;
    }
}