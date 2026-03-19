package yuxin.obviousredstone.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import yuxin.obviousredstone.client.model.JiaoModel;
import yuxin.obviousredstone.entities.JiaoEntity;

public class JiaoRenderer extends MobRenderer<JiaoEntity, JiaoRenderState, JiaoModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("obviousredstone", "textures/entity/jiao_entity.png");

    public JiaoRenderer(EntityRendererProvider.Context context) {
        super(context, new JiaoModel(context.bakeLayer(JiaoModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public Identifier getTextureLocation(JiaoRenderState state) {
        return TEXTURE;
    }

    @Override
    public JiaoRenderState createRenderState() {
        return new JiaoRenderState();
    }

    @Override
    public void extractRenderState(JiaoEntity entity, JiaoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
    }
}