package stevekung.mods.moreplanets.util;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class CachedEnumUtil
{
    private static EnumHand[] handValues = EnumHand.values();
    private static EnumDyeColor[] dyeColorValues = EnumDyeColor.values();
    private static IRocketType.EnumRocketType[] rocketValues = IRocketType.EnumRocketType.values();
    private static BiomeType[] biomeValues = BiomeType.values();
    private static Axis[] axisValues = Axis.values();
    private static TextFormatting[] textFormatValues = TextFormatting.values();
    private static EnumFacing[] facingValues = EnumFacing.values();

    public static EnumHand[] valuesHandCached()
    {
        return CachedEnumUtil.handValues;
    }

    public static EnumDyeColor[] valuesDyeCached()
    {
        return CachedEnumUtil.dyeColorValues;
    }

    public static IRocketType.EnumRocketType[] valuesRocketCached()
    {
        return CachedEnumUtil.rocketValues;
    }

    public static BiomeType[] valuesBiomeCached()
    {
        return CachedEnumUtil.biomeValues;
    }

    public static Axis[] valuesAxisCached()
    {
        return CachedEnumUtil.axisValues;
    }

    public static TextFormatting[] valuesTextFormattingCached()
    {
        return CachedEnumUtil.textFormatValues;
    }

    public static EnumFacing[] valuesEnumFacingCached()
    {
        return CachedEnumUtil.facingValues;
    }
}