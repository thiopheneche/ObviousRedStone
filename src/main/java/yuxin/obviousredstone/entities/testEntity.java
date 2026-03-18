package yuxin.obviousredstone.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.Level;

public class testEntity extends Pig {
    protected testEntity(EntityType<? extends Pig> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Pig.createAttributes();
    }
}
