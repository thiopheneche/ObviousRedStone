package yuxin.obviousredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import yuxin.obviousredstone.Items.testBlock;
import yuxin.obviousredstone.Items.testItem;
import yuxin.obviousredstone.entities.testEntities;
import yuxin.obviousredstone.entities.testEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObviousRedStone implements ModInitializer {
	public static final String MOD_ID = "obviousredstone";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		testItem.initialize();
		testBlock.initialize();
		testEntities.initialize();
		FabricDefaultAttributeRegistry.register(testEntities.TEST_ENTITY, testEntity.createAttributes());

		LOGGER.info("Hello Fabric world!");
		LOGGER.info("Registered custom item: {}", MOD_ID + ":custom_item");
		LOGGER.info("Registered test entity: {}", MOD_ID + ":test_entity");
	}
}