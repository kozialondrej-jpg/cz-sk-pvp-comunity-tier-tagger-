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
        
        try {
            // Načti konfiguraci
            ConfigManager.load();
            
            // Registruj keybindy
            Keybinds.registerKeybinds();
            
            // Registruj příkazy
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                CommandManager.registerCommands(dispatcher);
                LOGGER.info("CZSK Tier příkazy zaregistrovány");
            });
            
            // Načti data při startu (na pozadí) - refreshCache() už používá vlastní vlákno
            LOGGER.info("Spouštím načítání CZSK Tier dat...");
            DataFetcher.refreshCache();
            
            // Vypíše stav po 5 sekundách
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    LOGGER.info("=== CZSK Tier Tagger Status ===");
                    LOGGER.info("Data loaded: " + DataFetcher.isDataLoaded());
                    LOGGER.info("Cached players: " + DataFetcher.getCachedPlayerCount());
                    LOGGER.info("===============================");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
            
            // Hook do načítání entit - přidej tier suffix k hráčům
            ClientEntityEvents.ENTITY_LOAD.register((entity, clientWorld) -> {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    TierModifier modifier = (TierModifier) player;
                    
                    if (modifier.getSuffix() == null) {
                        try {
                            String playerName = player.getName().getString();
                            // Zkus načíst data synchronně
                            PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
                            if (info != null) {
                                String suffix = ShowedTier.showedTier(info);
                                modifier.setSuffix(suffix);
                                LOGGER.debug("Přidán tier pro hráče: " + playerName);
                            }
                        } catch (Exception e) {
                            LOGGER.error("Chyba při přidání tiera hráči", e);
                        }
                    }
                }
            });

            LOGGER.info("CZSK Tier Tagger úspěšně inicializován!");
        } catch (Exception e) {
            LOGGER.error("Kritická chyba při inicializaci CZSK Tier Tagger", e);
        }
    }
}
