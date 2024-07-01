package me.sebastian420.PandaPerWorldSeed.mixin;

import me.sebastian420.PandaPerWorldSeed.PandaPerWorldSeed;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.level.ServerWorldProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
	@Shadow @Final private ServerWorldProperties worldProperties;

	@Shadow public abstract ServerWorld toServerWorld();

	@Shadow @NotNull public abstract MinecraftServer getServer();

	@Shadow public abstract StructureAccessor getStructureAccessor();

	/**
	 * @author Sebastian420
	 * @reason Damn im faded off the kush right now and some good alcohol
	 */
	@Overwrite
	public long getSeed() {
		RegistryEntry<DimensionType> dimensionEntry = this.toServerWorld().getDimensionEntry();

		if (dimensionEntry.matchesKey(DimensionTypes.OVERWORLD)) {
			return PandaPerWorldSeed.OVERWORLD_SEED;
		}else if (dimensionEntry.matchesKey(DimensionTypes.THE_NETHER)){
			return PandaPerWorldSeed.THE_NETHER_SEED;
		}else if (dimensionEntry.matchesKey(DimensionTypes.THE_END)){
			return PandaPerWorldSeed.THE_END_SEED;
		}else {
			return this.getServer().getSaveProperties().getGeneratorOptions().getSeed();
		}

	}
}