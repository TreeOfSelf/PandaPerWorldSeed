package me.TreeOfSelf.PandaPerWorldSeed.mixin;

import me.TreeOfSelf.PandaPerWorldSeed.PandaPerWorldSeed;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	@Inject(method = "getSeed", at = @At("RETURN"), cancellable = true)
	private void modifySeed(CallbackInfoReturnable<Long> cir) {
		ServerLevel level = (ServerLevel) (Object) this;
		String dimensionId = level.dimension().identifier().toString();
		Long customSeed = PandaPerWorldSeed.getSeed(dimensionId);

		if (customSeed != null) {
			cir.setReturnValue(customSeed);
		}
	}
}
