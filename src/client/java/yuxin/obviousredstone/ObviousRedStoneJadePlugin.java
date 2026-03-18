package yuxin.obviousredstone;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class ObviousRedStoneJadePlugin implements IWailaPlugin {
    public static final Identifier REDSTONE_POWER_UID = Identifier.fromNamespaceAndPath(
            ObviousRedStone.MOD_ID,
            "redstone_power");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(RedstoneWirePowerProvider.INSTANCE,
                RedStoneWireBlock.class);
    }

    private static final class RedstoneWirePowerProvider implements
            IBlockComponentProvider {
        private static final RedstoneWirePowerProvider INSTANCE = new RedstoneWirePowerProvider();

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor,
                IPluginConfig config) {
            BlockState state = accessor.getBlockState();
            if (!(state.getBlock() instanceof RedStoneWireBlock)) {
                return;
            }

            int power = state.getValue(RedStoneWireBlock.POWER);
            tooltip.add(Component.translatable("tooltip.obviousredstone.redstone_power",
                    power));
        }

        @Override
        public Identifier getUid() {
            return REDSTONE_POWER_UID;
        }
    }

}
