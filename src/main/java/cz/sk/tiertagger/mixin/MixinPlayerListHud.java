package cz.sk.tiertagger.mixin;

import cz.sk.tiertagger.CzskTierTagger;
import cz.sk.tiertagger.DataFetcher;
import cz.sk.tiertagger.ShowedTier;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    
    @ModifyVariable(
        method = "renderLatencyIcon",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 1
    )
    private Text modifyPlayerName(Text text, PlayerListEntry entry) {
        CzskTierTagger.LOGGER.debug("[TabList] Volána metoda modifyPlayerName pro text: {}", text.getString());
        try {
            if (entry != null && entry.getProfile() != null) {
                String playerName = entry.getProfile().name();
                CzskTierTagger.LOGGER.debug("[TabList] Zpracovávám hráče: {}", playerName);
                if (playerName != null && !playerName.isEmpty()) {
                    PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
                    CzskTierTagger.LOGGER.debug("[TabList] PlayerInfo pro {}: {}", playerName, info != null ? "nalezen" : "nenalezen");
                    if (info != null) {
                        Text suffix = ShowedTier.showedTierText(info);
                        CzskTierTagger.LOGGER.info("[TabList] Hráč: {} má suffix: '{}'", playerName, suffix != null ? suffix.getString() : "null");
                        if (suffix != null) {
                            Text result = text.copy()
                                .append(Text.literal(" "))
                                .append(suffix);
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
