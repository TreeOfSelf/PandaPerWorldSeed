package me.TreeOfSelf.PandaPerWorldSeed.mixin;

import me.TreeOfSelf.PandaPerWorldSeed.PandaPerWorldSeed;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Inject(method = "getSeed", at = @At("RETURN"), cancellable = true)
	private void modifySeed(CallbackInfoReturnable<Long> cir) {
		ServerWorld world = (ServerWorld) (Object) this;
		String dimensionId = world.getDimensionEntry().getIdAsString();
		Long customSeed = PandaPerWorldSeed.getSeed(dimensionId);

		if (customSeed != null) {
			cir.setReturnValue(customSeed);
		}
	}
}