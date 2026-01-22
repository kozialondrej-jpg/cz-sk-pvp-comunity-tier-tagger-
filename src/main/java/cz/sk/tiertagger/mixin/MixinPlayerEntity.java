package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.TierModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity implements TierModifier {
    @Unique
    private String czskTierSuffix = null;
    
    @Override
    public String getSuffix() {
        return czskTierSuffix;
    }
    
    @Override
    public void setSuffix(String value) {
        this.czskTierSuffix = value;
    }
    
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void onGetDisplayName(CallbackInfoReturnable<Text> cir) {
        String suffix = this.czskTierSuffix;
        
        if (suffix != null && !suffix.isEmpty()) {
            Text original = cir.getReturnValue();
            Text newText = original.copy()
                .append(Text.literal(" " + suffix)
                    .styled(s -> s.withColor(Formatting.GOLD)));
            cir.setReturnValue(newText);
        }
    }
}
