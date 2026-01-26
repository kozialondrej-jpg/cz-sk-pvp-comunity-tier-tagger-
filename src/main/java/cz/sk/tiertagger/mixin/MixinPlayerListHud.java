package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void onGetPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (entry == null || entry.getProfile() == null) {
            return;
        }
        
        String playerName = entry.getProfile().getName();
        if (playerName == null || playerName.isEmpty()) {
            return;
        }
        
        try {
            PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
            if (info != null) {
                String suffix = ShowedTier.showedTier(info);
                if (suffix != null && !suffix.isEmpty()) {
                    Text original = cir.getReturnValue();
                    Text newText = original.copy()
                        .append(Text.literal(" " + suffix)
                            .styled(s -> s.withColor(Formatting.GOLD)));
                    cir.setReturnValue(newText);
                }
            }
        } catch (Exception e) {
            // Ignoruj chyby, abychom nezpůsobili crash
        }
    }
}
