package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.CzskTierTagger;
import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinPlayerEntityRenderer<T extends Entity, S extends EntityRenderState> {
    
    @Inject(method = "updateRenderState", at = @At("TAIL"))
    public void modifyDisplayName(T entity, S state, float tickProgress, CallbackInfo ci) {
        try {
            if (entity instanceof AbstractClientPlayerEntity player && state.displayName != null) {
                String playerName = player.getName().getString();
                PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
                
                if (info != null) {
                    Text suffix = ShowedTier.showedTierText(info);
                    CzskTierTagger.LOGGER.debug("[Nametag] Hráč: {} má suffix: {}", playerName, suffix != null ? suffix.getString() : "null");
                    if (suffix != null) {
                        state.displayName = state.displayName.copy()
                            .append(Text.literal(" "))
                            .append(suffix);
                    }
                }
            }
        } catch (Exception e) {
            CzskTierTagger.LOGGER.error("[Nametag] Chyba: {}", e.getMessage());
        }
    }
}
