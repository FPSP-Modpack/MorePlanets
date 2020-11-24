package stevekung.mods.moreplanets.utils;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.stevekunglib.utils.client.GLConstants;
import stevekung.mods.stevekunglib.utils.client.RenderUtils;
import stevekung.mods.stevekunglib.utils.enums.CachedEnum;

@SideOnly(Side.CLIENT)
public class ClientRendererUtils
{
    public static void renderModel(IBlockState state)
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        int i = mc.getBlockColors().colorMultiplier(state, null, null, 0);
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);

        if (EntityRenderer.anaglyphEnable)
        {
            i = TextureUtil.anaglyphColor(i);
        }
        float f = (i >> 16 & 255) / 255.0F;
        float f1 = (i >> 8 & 255) / 255.0F;
        float f2 = (i & 255) / 255.0F;
        ClientRendererUtils.renderModelBrightnessColor(state, model, 1.0F, f, f1, f2);
    }

    public static void renderModelBrightnessColor(IBlockState state, IBakedModel model, float brightness, float red, float green, float blue)
    {
        for (EnumFacing facing : CachedEnum.facingValues)
        {
            ClientRendererUtils.renderModelBrightnessColorQuads(brightness, red, green, blue, model.getQuads(state, facing, 0L));
        }
        ClientRendererUtils.renderModelBrightnessColorQuads(brightness, red, green, blue, model.getQuads(state, null, 0L));
    }

    private static void renderModelBrightnessColorQuads(float brightness, float red, float green, float blue, List<BakedQuad> listQuads)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        int i = 0;

        for (int j = listQuads.size(); i < j; ++i)
        {
            BakedQuad bakedquad = listQuads.get(i);
            vertexbuffer.begin(GLConstants.QUADS, DefaultVertexFormats.ITEM);
            vertexbuffer.addVertexData(bakedquad.getVertexData());

            if (bakedquad.hasTintIndex())
            {
                ClientRendererUtils.putColorRGB_F4(vertexbuffer, red * brightness, green * brightness, blue * brightness);
            }
            else
            {
                ClientRendererUtils.putColorRGB_F4(vertexbuffer, brightness, brightness, brightness);
            }
            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            vertexbuffer.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
            tessellator.draw();
        }
    }

    private static void putColorRGB_F4(BufferBuilder vertexbuffer, float red, float green, float blue)
    {
        for (int i = 0; i < 4; ++i)
        {
            ClientRendererUtils.putColorRGB_F(vertexbuffer, red, green, blue, i + 1);
        }
    }

    private static void putColorRGB_F(BufferBuilder vertexbuffer, float red, float green, float blue, int vertexIndex)
    {
        int i = vertexbuffer.getColorIndex(vertexIndex);
        int j = MathHelper.clamp((int)(red * 255.0F), 0, 255);
        int k = MathHelper.clamp((int)(green * 255.0F), 0, 255);
        int l = MathHelper.clamp((int)(blue * 255.0F), 0, 255);
        vertexbuffer.putColorRGBA(i, j, k, l, 127);
    }

    public static void renderBeam(double x, double y, double z, float partialTicks, double prevX, double prevY, double prevZ, int ticksExisted, double targetX, double targetY, double targetZ)
    {
        float f = (float)(targetX - prevX);
        float f1 = (float)(targetY - 1.0D - prevY);
        float f2 = (float)(targetZ - prevZ);
        float f3 = MathHelper.sqrt(f * f + f2 * f2);
        float f4 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 2.0F, (float)z);
        GlStateManager.rotate((float)-Math.atan2(f2, f) * (180F / (float)Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)-Math.atan2(f3, f1) * (180F / (float)Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        RenderUtils.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(7425);
        float f5 = 0.0F - (ticksExisted + partialTicks) * 0.01F;
        float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0F - (ticksExisted + partialTicks) * 0.01F;
        vertexbuffer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (int j = 0; j <= 8; ++j)
        {
            float f7 = MathHelper.sin(j % 8 * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
            float f8 = MathHelper.cos(j % 8 * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
            float f9 = j % 8 / 8.0F;
            vertexbuffer.pos(f7 * 0.2F, f8 * 0.2F, 0.0D).tex(f9, f5).color(0, 0, 0, 255).endVertex();
            vertexbuffer.pos(f7, f8, f4).tex(f9, f6).color(255, 255, 255, 255).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderUtils.enableLighting();
        GlStateManager.popMatrix();
    }
}