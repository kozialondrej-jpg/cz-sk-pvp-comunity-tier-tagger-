package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.CzskTierTagger;
import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer {
    
    // TODO: Tato funkce je dočasně zakázána kvůli změnám v 1.21.10 render API
    // Nametağy momentálně nefungují, pouze tab list
    /*
    @ModifyArg(
        method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"),
        index = 1
    )
    private Text modifyNameTag(Text text) {
        // TODO: Implement nametag modification for 1.21.10
        return text;
    }
    */
}
