package cz.sk.tiertagger;

import cz.sk.tiertagger.tiers.PlayerInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CzskTierTagger implements ClientModInitializer {
    public static final String MOD_ID = "czsk_tier_tagger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final int TIER_UPDATE_INTERVAL_TICKS = 40;
    private static final Map<UUID, Text> originalTabNames = new HashMap<>();
    private static int tickCounter = 0;

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

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.world == null || client.getNetworkHandler() == null) {
                    return;
                }

                tickCounter++;
                if (tickCounter % TIER_UPDATE_INTERVAL_TICKS != 0) {
                    return;
                }

                updatePlayerNametags(client);
                updateTabList(client);
            });

            LOGGER.info("CZSK Tier Tagger úspěšně inicializován!");
        } catch (Exception e) {
            LOGGER.error("Kritická chyba při inicializaci CZSK Tier Tagger", e);
        }
    }

    private static void updatePlayerNametags(MinecraftClient client) {
        for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
            try {
                String playerName = player.getName().getString();
                PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
                String suffix = info != null ? ShowedTier.showedTierTag(info) : null;
                ((TierModifier) player).setSuffix(suffix);
            } catch (Exception e) {
                LOGGER.error("Chyba při aktualizaci nametagu", e);
            }
        }
    }

    private static void updateTabList(MinecraftClient client) {
        for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
            if (entry == null || entry.getProfile() == null) {
                continue;
            }

            String playerName = entry.getProfile().getName();
            if (playerName == null || playerName.isEmpty()) {
                continue;
            }

            PlayerInfo info = DataFetcher.getPlayerInfo(playerName);
            String suffix = info != null ? ShowedTier.showedTierTag(info) : null;
            UUID playerId = entry.getProfile().getId();

            if (suffix == null || suffix.isEmpty()) {
                Text original = originalTabNames.remove(playerId);
                if (original != null) {
                    entry.setDisplayName(original);
                } else {
                    entry.setDisplayName(null);
                }
                continue;
            }

            Text base = originalTabNames.computeIfAbsent(playerId, id -> {
                Text displayName = entry.getDisplayName();
                return displayName != null ? displayName : Text.literal(playerName);
            });

            Text updated = base.copy()
                .append(Text.literal(" " + suffix)
                    .styled(s -> s.withColor(Formatting.GOLD)));
            entry.setDisplayName(updated);
        }
    }
}
