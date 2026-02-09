package cz.sk.tiertagger.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import cz.sk.tiertagger.CzskTierTagger;
import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    
    @ModifyArg(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"
        ),
        index = 1
    )
    private Text modifyPlayerListText(Text text, @Local PlayerListEntry entry) {
        CzskTierTagger.LOGGER.debug("[TabList] Volána metoda modifyPlayerListText pro text: {}", text.getString());
        try {
            if (entry != null && entry.getProfile() != null) {
                String playerName = entry.getProfile().name();
                CzskTierTagger.LOGGER.debug("[TabList] Zpracovávám hráče: {}", playerName);
                if (playerName != null && !playerName.isEmpty()) {
                    PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
                    CzskTierTagger.LOGGER.debug("[TabList] PlayerInfo pro {}: {}", playerName, info != null ? "nalezen" : "nenalezen");
                    if (info != null) {
                        String suffix = ShowedTier.showedTier(info);
                        CzskTierTagger.LOGGER.info("[TabList] Hráč: {} má suffix: '{}'", playerName, suffix);
                        if (suffix != null && !suffix.isEmpty()) {
                            Text result = text.copy()
                                .append(Text.literal(" " + suffix)
                                    .styled(s -> s.withColor(Formatting.GOLD)));
                            CzskTierTagger.LOGGER.info("[TabList] Upravený text: {}", result.getString());
                            return result;
                        }
                    }
                }
            }
        } catch (Exception e) {
            CzskTierTagger.LOGGER.error("[TabList] Chyba: {}", e.getMessage(), e);
        }
        return text;
    }
}
