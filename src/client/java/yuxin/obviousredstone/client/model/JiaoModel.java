
package yuxin.obviousredstone.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import yuxin.obviousredstone.client.renderer.JiaoRenderState;
import net.minecraft.util.Mth;

public class JiaoModel extends EntityModel<JiaoRenderState> {
    // 统一使用你项目的 modid 和实体标识符
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("obviousredstone", "jiao_entity"), "main");

    private final ModelPart Waist;
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;

    public JiaoModel(ModelPart root) {
        super(root); // 1.21.2 强制要求的父类调用，负责接管底层渲染
        this.Waist = root.getChild("Waist");
        this.Head = this.Waist.getChild("Head");
        this.Body = this.Waist.getChild("Body");
        this.RightArm = this.Waist.getChild("RightArm");
        this.LeftArm = this.Waist.getChild("LeftArm");
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // 以下是 Blockbench 精确计算的几何坐标，原封不动保留
        PartDefinition Waist = partdefinition.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition Head = Waist.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
        .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition Body = Waist.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
        .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition RightArm = Waist.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
        .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, -10.0F, 0.0F));

        PartDefinition LeftArm = Waist.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
        .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, -10.0F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
        .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
        .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    // 1.21.2 版本的动画方法，只接收 state
   @Override
    public void setupAnim(JiaoRenderState state) {
        super.setupAnim(state);

        // 1. 头部跟随视线转动 (Yaw是左右摇头，Pitch是上下点头)
        // 乘以 (PI / 180) 是为了把角度转换为弧度
        this.Head.yRot = state.yRot * ((float)Math.PI / 180F);
        this.Head.xRot = state.xRot * ((float)Math.PI / 180F);

        // 2. 走路时的四肢摆动
        // state.walkAnimationPos 是行走的时间进度，state.walkAnimationSpeed 是行走的速度快慢
        // 右手和左腿方向一致，左手和右腿方向一致 (相差一个 PI 也就是 180 度，形成交替摆臂)
        this.RightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float)Math.PI) * 2.0F * state.walkAnimationSpeed * 0.5F;
        this.LeftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 2.0F * state.walkAnimationSpeed * 0.5F;

        this.RightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
        this.LeftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float)Math.PI) * 1.4F * state.walkAnimationSpeed;

        // 3. 确保手臂在不走路时不会发生奇怪的扭曲 (归零)
        this.RightArm.zRot = 0.0F;
        this.LeftArm.zRot = 0.0F;
        this.RightArm.yRot = 0.0F;
        this.LeftArm.yRot = 0.0F;
    }
}