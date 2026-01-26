package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.CzskTierTagger;
import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {
    
    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At("HEAD"))
    private void onRenderLabelIfPresent(PlayerEntityRenderState state, MatrixStack matrices, 
                                        OrderedRenderCommandQueue renderCommandQueue, CameraRenderState cameraState, CallbackInfo ci) {
        try {
            // Získáme jméno hráče z renderState
            String playerName = state.playerName != null ? state.playerName.getString() : null;
            if (playerName == null || playerName.isEmpty()) {
                return;
            }
            
            PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
            if (info != null) {
                String suffix = ShowedTier.showedTier(info);
                CzskTierTagger.LOGGER.debug("[Nametag] Hráč: {} má suffix: {}", playerName, suffix);
                if (suffix != null && !suffix.isEmpty()) {
                    // Modifikujeme playerName v renderState přímo
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
