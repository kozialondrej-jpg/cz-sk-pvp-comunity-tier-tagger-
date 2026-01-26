package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.CzskTierTagger;
import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    
    public MixinPlayerEntityRenderer(net.minecraft.client.render.entity.EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }
    
    @Redirect(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V"))
    private void redirectRenderLabel(LivingEntityRenderer<?, ?> instance, Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta) {
        Text modifiedText = text;
        
        try {
            if (entity instanceof AbstractClientPlayerEntity player) {
                String playerName = player.getName().getString();
                PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
                
                if (info != null) {
                    String suffix = ShowedTier.showedTier(info);
                    CzskTierTagger.LOGGER.debug("[Nametag] Hráč: {} má suffix: {}", playerName, suffix);
                    if (suffix != null && !suffix.isEmpty()) {
                        modifiedText = text.copy()
                            .append(Text.literal(" " + suffix)
                                .styled(s -> s.withColor(Formatting.GOLD)));
                    }
                }
            }
        } catch (Exception e) {
            CzskTierTagger.LOGGER.error("[Nametag] Chyba: {}", e.getMessage());
        }
        
        super.renderLabelIfPresent((AbstractClientPlayerEntity)entity, modifiedText, matrices, vertexConsumers, light, tickDelta);
    }
}
