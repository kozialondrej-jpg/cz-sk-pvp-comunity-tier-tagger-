package cz.sk.tiertagger.mixin;

import com.llamalad7.mixinextras.sugar.Local;
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
    
    @ModifyArg(
        method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/OrderedRenderCommandQueue;Lnet/minecraft/client/render/CameraRenderState;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/OrderedRenderCommandQueue;Lnet/minecraft/client/render/CameraRenderState;)V"
        ),
        index = 1
    )
    private Text modifyNameTag(Text text, @Local(argsOnly = true) AbstractClientPlayerEntity player) {
        try {
            if (player != null) {
                String playerName = player.getName().getString();
                PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
                
                if (info != null) {
                    String suffix = ShowedTier.showedTier(info);
                    CzskTierTagger.LOGGER.debug("[Nametag] Hráč: {} má suffix: {}", playerName, suffix);
                    if (suffix != null && !suffix.isEmpty()) {
                        return text.copy()
                            .append(Text.literal(" " + suffix)
                                .styled(s -> s.withColor(Formatting.GOLD)));
                    }
                }
            }
        } catch (Exception e) {
            CzskTierTagger.LOGGER.error("[Nametag] Chyba: {}", e.getMessage());
        }
        return text;
    }
}
