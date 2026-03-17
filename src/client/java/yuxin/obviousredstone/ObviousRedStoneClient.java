package yuxin.obviousredstone;

import net.fabricmc.api.ClientModInitializer;
import yuxin.obviousredstone.Items.FanBlock;

public class ObviousRedStoneClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		FanBlock.initialize();
	}
}