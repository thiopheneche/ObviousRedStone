package com.example;


// import net.fabricmc.api.ClientModInitializer;




// public class ExampleModClient implements ClientModInitializer {
// 	@Override
// 	public void onInitializeClient() {
// 		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
// 	}
// }


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        
        // 注册一个在实体渲染之后执行的渲染事件
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;

            // 获取渲染需要的核心工具
            Camera camera = context.camera();
            Vec3d cameraPos = camera.getPos();
            MatrixStack matrixStack = context.matrixStack();
            VertexConsumerProvider vertexConsumers = context.consumers();
            TextRenderer textRenderer = client.textRenderer;

            // 获取玩家当前坐标，设置扫描半径为 8 个方块
            BlockPos playerPos = client.player.getBlockPos();
            int radius = 8;

            // 遍历玩家周围的三维坐标网格
            for (BlockPos pos : BlockPos.iterate(playerPos.add(-radius, -radius, -radius), playerPos.add(radius, radius, radius))) {
                BlockState state = client.world.getBlockState(pos);

                // 判断这个坐标上的方块是不是红石线
                if (state.isOf(Blocks.REDSTONE_WIRE)) {
                    // 提取红石线的能量值 (0 - 15)
                    int power = state.get(RedstoneWireBlock.POWER);

                    matrixStack.push();

                    // 1. 平移：将渲染中心移动到红石块的正上方一点点
                    double x = pos.getX() + 0.5 - cameraPos.x;
                    double y = pos.getY() + 0.15 - cameraPos.y; 
                    double z = pos.getZ() + 0.5 - cameraPos.z;
                    matrixStack.translate(x, y, z);

                    // 2. 旋转：让文字永远面向摄像机（玩家视角）
                    matrixStack.multiply(camera.getRotation());

                    // 3. 缩放：Minecraft的三维空间中默认文字非常巨大，需要大幅缩小
                    matrixStack.scale(-0.025f, -0.025f, 0.025f);

                    // 4. 准备绘制文字内容
                    String text = String.valueOf(power);
                    // 计算偏移量，让文字水平居中
                    float xOffset = -textRenderer.getWidth(text) / 2.0f;
                    
                    // 根据能量值设置颜色：有能量显示红色，0能量显示白色
                    int color = (power > 0) ? 0xFF0000 : 0xFFFFFF;

                    // 5. 执行绘制文字 (注意：如果这里有红线报错，请立刻告诉我！)
                    textRenderer.draw(text, xOffset, 0, color, false, matrixStack.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);

                    matrixStack.pop();
                }
            }
        });
    }
}
