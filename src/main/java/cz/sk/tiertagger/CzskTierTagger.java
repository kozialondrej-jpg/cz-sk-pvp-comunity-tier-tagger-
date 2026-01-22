package cz.sk.tiertagger;

import cz.sk.tiertagger.tiers.PlayerInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CzskTierTagger implements ClientModInitializer {
    public static final String MOD_ID = "czsk_tier_tagger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Inicializuji CZSK Tier Tagger...");
        
        // Načti konfiguraci
        ConfigManager.load();
        
        // Registruj keybindy
        Keybinds.registerKeybinds();
        
        // Registruj příkazy
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            CommandManager.registerCommands(dispatcher);
        });
        
        // Načti data při startu (na pozadí)
        new Thread(() -> {
            DataFetcher.refreshCache();
        }).start();
        
        // Hook do načítání entit - přidej tier suffix k hráčům
        ClientEntityEvents.ENTITY_LOAD.register((entity, clientWorld) -> {
            if (entity instanceof PlayerEntity) {
                if (((TierModifier) entity).getSuffix() == null) {
                    new Thread(() -> {
                        PlayerInfo info = DataFetcher.getPlayerInfo(entity.getName().getString());
                        if (info != null) {
                            ((TierModifier) entity).setSuffix(ShowedTier.showedTier(info));
                        }
                    }).start();
                }
            }
        });

        LOGGER.info("CZSK Tier Tagger úspěšně inicializován!");
    }
}
