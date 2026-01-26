package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {
    
    @Unique
    private AbstractClientPlayerEntity currentPlayer;
    
    @ModifyArg(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V",
               at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V"),
               index = 1)
    private Text modifyLabelText(Text originalText) {
        // Získáme jméno hráče z původního textu
        try {
            String playerName = originalText.getString();
            
            // Odstraň případné formátování a získej čisté jméno
            if (playerName.contains(" ")) {
                playerName = playerName.split(" ")[0];
            }
            
            PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
            if (info != null) {
                String suffix = ShowedTier.showedTier(info);
                if (suffix != null && !suffix.isEmpty()) {
                    return originalText.copy()
                        .append(Text.literal(" " + suffix)
                            .styled(s -> s.withColor(Formatting.GOLD)));
                }
            }
        } catch (Exception e) {
            // Ignoruj chyby
        }
        return originalText;
    }
}
