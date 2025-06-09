package me.TreeOfSelf.PandaPerWorldSeed.mixin;

import me.TreeOfSelf.PandaPerWorldSeed.PandaPerWorldSeed;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
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

	@Shadow public abstract ServerWorld toServerWorld();


	@Shadow @Final private MinecraftServer server;

	@Shadow @NotNull public abstract MinecraftServer getServer();

	/**
	 * @author TreeOfSelf
	 * @reason Overwrite get seed to get based on world
	 */
	@Overwrite
	public long getSeed() {
		String dimensionId = this.toServerWorld().getDimensionEntry().getIdAsString();
		Long seed = PandaPerWorldSeed.getSeed(dimensionId);
		return seed != null ? seed : this.server.getSaveProperties().getGeneratorOptions().getSeed();
	}
}