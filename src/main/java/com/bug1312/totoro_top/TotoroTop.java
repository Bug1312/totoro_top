package com.bug1312.totoro_top;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class TotoroTop implements ModInitializer {
	public static final String MOD_ID = "totoro_top";
	
	private static final Identifier TOP_ID = Identifier.of(MOD_ID, "top");

	public static final EntityType<TopEntity> TOP_ENTITY = Registry.register(
		Registries.ENTITY_TYPE, TOP_ID,
		EntityType.Builder
			.create(TopEntity::new, SpawnGroup.MISC)
			.dimensions(0.75f, 0.8125F)
			.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, TOP_ID))
	);

	public static final Item TOP_ITEM = Registry.register(
		Registries.ITEM, TOP_ID, 
		new TopItem(TOP_ENTITY, new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, TOP_ID)))
	);

	private static final Identifier TOP_SOUND_ID = Identifier.of(MOD_ID, "entity.totoro_top.top.spin");
	public static final SoundEvent TOP_SOUND_EVENT = Registry.register(
		Registries.SOUND_EVENT, TOP_SOUND_ID, 
		SoundEvent.of(TOP_SOUND_ID)
	);

	@Override
	public void onInitialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
			.register(content -> content.add(TOP_ITEM));
	}
}