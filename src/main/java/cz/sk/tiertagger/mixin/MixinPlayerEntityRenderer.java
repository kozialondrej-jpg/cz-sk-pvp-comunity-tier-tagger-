package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.CzskTierTagger;
import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {
    
    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true)
    private void onRenderLabelIfPresent(PlayerEntityRenderState state, Text text, MatrixStack matrices, 
                                        VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        try {
            // Získáme jméno hráče z renderState
            String playerName = state.name;
            if (playerName == null || playerName.isEmpty()) {
                return;
            }
            
            PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
            if (info != null) {
                String suffix = ShowedTier.showedTier(info);
                CzskTierTagger.LOGGER.debug("[Nametag] Hráč: {} má suffix: {}", playerName, suffix);
                if (suffix != null && !suffix.isEmpty()) {
                    // Modifikujeme playerName v renderState
                    Text newText = text.copy()
                        .append(Text.literal(" " + suffix)
                            .styled(s -> s.withColor(Formatting.GOLD)));
                    
                    // Zavoláme původní metodu s modifikovaným textem - ale musíme to udělat jinak
                    // Můžeme modifikovat state.playerName přímo
                    if (state.playerName != null) {
                        state.playerName = state.playerName.copy()
                            .append(Text.literal(" " + suffix)
                                .styled(s -> s.withColor(Formatting.GOLD)));
                    }
                }
            }
        } catch (Exception e) {
            CzskTierTagger.LOGGER.error("[Nametag] Chyba: {}", e.getMessage());
        }
    }
}
