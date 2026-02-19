package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.TierModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity implements TierModifier {
    @Unique
    private Text czskTierSuffix = null;
    
    @Override
    public Text getSuffixText() {
        return czskTierSuffix;
    }
    
    @Override
    public void setSuffixText(Text value) {
        this.czskTierSuffix = value;
    }
    
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void onGetDisplayName(CallbackInfoReturnable<Text> cir) {
        Text suffix = this.czskTierSuffix;
        
        if (suffix != null) {
            Text original = cir.getReturnValue();
            Text newText = original.copy()
                .append(Text.literal(" "))
                .append(suffix);
            cir.setReturnValue(newText);
        }
    }
}
