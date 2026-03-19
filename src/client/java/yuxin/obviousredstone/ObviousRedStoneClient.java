package yuxin.obviousredstone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import yuxin.obviousredstone.client.model.JiaoModel;
import yuxin.obviousredstone.client.renderer.JiaoRenderer;
import yuxin.obviousredstone.entities.testEntities;
import yuxin.obviousredstone.entities.testEntity;

public class ObviousRedStoneClient implements ClientModInitializer {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static EntityRendererProvider<testEntity> pigRendererProvider() {
		return (EntityRendererProvider) PigRenderer::new;
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as
		// rendering.
		EntityRendererRegistry.register(testEntities.TEST_ENTITY, pigRendererProvider());
		EntityModelLayerRegistry.registerModelLayer(JiaoModel.LAYER_LOCATION, JiaoModel::createBodyLayer);
        EntityRendererRegistry.register(testEntities.JIAO_ENTITY, JiaoRenderer::new);
    }
}
