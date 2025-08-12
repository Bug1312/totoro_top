package com.bug1312.totoro_top;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

public class TopEntityLoopSound extends MovingSoundInstance {
	private final Entity entity;

	public TopEntityLoopSound(Entity entity) {
		super(TotoroTop.TOP_SOUND_EVENT, SoundCategory.NEUTRAL, SoundInstance.createRandom());
		this.repeat = true;
		this.repeatDelay = 0;
		this.attenuationType = SoundInstance.AttenuationType.LINEAR;
		this.volume = 1.0F;

		this.entity = entity;
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
	}

	@Override
	public boolean canPlay() {
		return !this.entity.isSilent();
	}

	@Override
	public void tick() {
		if (entity.isAlive()) {
			Vec3d pos = entity.getPos();
			this.x = pos.x;
			this.y = pos.y;
			this.z = pos.z;
		} else this.setDone();
	}
}
