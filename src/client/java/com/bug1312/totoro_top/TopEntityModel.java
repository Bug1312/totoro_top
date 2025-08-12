package com.bug1312.totoro_top;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationDefinition;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModel;

public class TopEntityModel extends EntityModel<TopRenderState> {
	private static final AnimationDefinition IDLING = AnimationDefinition.Builder.create(2.0F).looping()
		.addBoneAnimation("main", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 1080.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();
	private final Animation idlingAnimation;

	protected TopEntityModel(ModelPart root) {
		super(root);
		this.idlingAnimation = IDLING.createAnimation(root);
	}

	@Override
	public void setAngles(TopRenderState state) {
		super.setAngles(state);
		this.idlingAnimation.apply(state.idleAnimationState, state.age);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild(
			"main",
			ModelPartBuilder.create()
				.uv(20, 25).cuboid(-1.5F, -12.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
				.uv(20, 18).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-5.0F, -10.0F, -5.0F, 10.0F, 3.0F, 10.0F, new Dilation(0.0F)),
			ModelTransform.of(0.0F, 1.0F, 0.0F, (float) Math.PI, 0.0F, 0.0F)
		);

		main.addChild("diag_r1", ModelPartBuilder.create().uv(0, 13).cuboid(0.0F, 0.0F, -5.0F, 0.0F, 5.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -7.0F, 0.0F, 0.0F, 0.0F, -0.829F));
		main.addChild("diag_r2", ModelPartBuilder.create().uv(10, 3).cuboid(0.0F, 0.0F, -5.0F, 0.0F, 5.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.829F));
		main.addChild("diag_r3", ModelPartBuilder.create().uv(20, 13).cuboid(-5.0F, 0.0F, 0.0F, 10.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.0F, -5.0F, 0.829F, 0.0F, 0.0F));
		main.addChild("diag_r4", ModelPartBuilder.create().uv(10, 13).cuboid(-5.0F, 0.0F, 0.0F, 10.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.0F, 5.0F, -0.829F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}

}