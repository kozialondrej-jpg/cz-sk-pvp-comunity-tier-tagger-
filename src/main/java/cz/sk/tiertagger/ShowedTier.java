package cz.sk.tiertagger;

import cz.sk.tiertagger.tiers.PlayerInfo;
import cz.sk.tiertagger.tiers.Tier;

import java.util.*;

public class ShowedTier {
    private static final Map<String, Integer> TIER_IMPORTANCE = Map.ofEntries(
        Map.entry("-", 0),
        Map.entry("1", 1),    // LT5
        Map.entry("2", 2),    // HT5
        Map.entry("3", 3),    // LT4
        Map.entry("5", 4),    // HT4
        Map.entry("10", 5),   // LT3
        Map.entry("16", 6),   // HT3
        Map.entry("22", 7),   // RLT2
        Map.entry("24", 8),   // LT2
        Map.entry("29", 9),   // RHT2
        Map.entry("32", 10),  // HT2
        Map.entry("43", 11),  // RLT1
        Map.entry("48", 12),  // LT1
        Map.entry("54", 13),  // RHT1
        Map.entry("60", 14)   // HT1
    );
    
    // Unicode znaky pro kity - mapování na ikony z font/tiers.json
    private static final Map<String, String> KIT_ICON = Map.ofEntries(
        Map.entry("Crystal", "\uEE00"),  // Crystal icon
        Map.entry("Axe", "\uEE01"),      // Axe icon
        Map.entry("Sword", "\uEE02"),    // Sword icon
        Map.entry("UHC", "\uEE03"),      // UHC icon
        Map.entry("NPot", "\uEE04"),     // NPot icon
        Map.entry("Pot", "\uEE05"),      // Pot icon
        Map.entry("SMP", "\uEE06"),      // SMP icon
        Map.entry("DiaSMP", "\uEE07"),   // DiaSMP icon
        Map.entry("Mace", "\uEE08")      // Mace icon
    );
    
    // Unicode znaky pro tiery - mapování na ikony z font/tiers.json
    private static final Map<String, String> TIER_ICON = Map.ofEntries(
        Map.entry("1", "\uEF00"),    // LT5
        Map.entry("2", "\uEF01"),    // HT5
        Map.entry("3", "\uEF02"),    // LT4
        Map.entry("5", "\uEF03"),    // HT4
        Map.entry("10", "\uEF04"),   // LT3
        Map.entry("16", "\uEF05"),   // HT3
        Map.entry("24", "\uEF06"),   // LT2
        Map.entry("32", "\uEF07"),   // HT2
        Map.entry("48", "\uEF08"),   // LT1
        Map.entry("60", "\ued09"),   // HT1
        Map.entry("22", "\uEE09"),   // RLT2
        Map.entry("29", "\uEE0a"),   // RHT2
        Map.entry("43", "\uEE0b"),   // RLT1
        Map.entry("54", "\uEE0c")    // RHT1
    );
    
    private static final Map<String, String> KIT_DISPLAY_NAMES = Map.ofEntries(
        Map.entry("Mace", "Mace"),
        Map.entry("SMP", "SMP"),
        Map.entry("UHC", "UHC"),
        Map.entry("Pot", "Pot"),
        Map.entry("Crystal", "CPVP"),
        Map.entry("Sword", "Sword"),
        Map.entry("DiaSMP", "DiaSMP"),
        Map.entry("NPot", "NPot"),
        Map.entry("Axe", "Axe")
    );
    
    private static final Map<String, String> TIER_NAMES = Map.ofEntries(
        Map.entry("1", "LT5"),
        Map.entry("2", "HT5"),
        Map.entry("3", "LT4"),
        Map.entry("5", "HT4"),
        Map.entry("10", "LT3"),
        Map.entry("16", "HT3"),
        Map.entry("24", "LT2"),
        Map.entry("32", "HT2"),
        Map.entry("48", "LT1"),
        Map.entry("60", "HT1"),
        Map.entry("22", "RLT2"),
        Map.entry("29", "RHT2"),
        Map.entry("43", "RLT1"),
        Map.entry("54", "RHT1")
    );
    
    public static String showedTier(PlayerInfo info) {
        ModConfig config = ConfigManager.getConfig();
        
        if (Objects.equals(config.gamemode, "Mod Off")) {
            return "";
        }
        
        if (info == null || info.tiers == null || info.tiers.isEmpty()) {
            return "";
        }
        
        // Pokud je vybrán konkrétní gamemode
        if (!Objects.equals(config.gamemode, "All")) {
            for (Tier tier : info.tiers) {
                if (Objects.equals(tier.category, config.gamemode)) {
                    String tierIcon = TIER_ICON.getOrDefault(tier.tier, "?");
                    String kitIcon = KIT_ICON.getOrDefault(tier.category, "?");
                    return "[" + tierIcon + kitIcon + "]";
                }
            }
            return "";
        }
        
        // Pro "All" - najdi nejlepší tier
        Tier bestTier = null;
        int bestImportance = -1;
        
        for (Tier tier : info.tiers) {
            int importance = TIER_IMPORTANCE.getOrDefault(tier.tier, 0);
            if (importance > bestImportance) {
                bestImportance = importance;
                bestTier = tier;
            }
        }
        
        if (bestTier != null) {
            String tierIcon = TIER_ICON.getOrDefault(bestTier.tier, "?");
            String kitIcon = KIT_ICON.getOrDefault(bestTier.category, "?");
            return "[" + tierIcon + kitIcon + "]";
        }
        
        return "";
    }
    
    public static String showedMessage(PlayerInfo info) {
        if (info == null) {
            return "HRÁČ NENALEZEN";
        }
        
        StringBuilder msg = new StringBuilder();
        msg.append("=== CZSK TIERLIST ===\n");
        msg.append("HRÁČ: ").append(info.nick).append("\n");
        msg.append("TIERY:");
        
        if (info.tiers != null && !info.tiers.isEmpty()) {
            // Seřaď tiery podle důležitosti
            List<Tier> sortedTiers = new ArrayList<>(info.tiers);
            sortedTiers.sort((a, b) -> {
                int impA = TIER_IMPORTANCE.getOrDefault(a.tier, 0);
                int impB = TIER_IMPORTANCE.getOrDefault(b.tier, 0);
                return Integer.compare(impB, impA);
            });
            
            for (Tier tier : sortedTiers) {
                String tierIcon = TIER_ICON.getOrDefault(tier.tier, "?");
                String kitIcon = KIT_ICON.getOrDefault(tier.category, "?");
                String tierName = TIER_NAMES.getOrDefault(tier.tier, tier.tier);
                String kitName = KIT_DISPLAY_NAMES.getOrDefault(tier.category, tier.category);
                msg.append("\n  ").append(tierIcon).append(kitIcon).append(" - ").append(kitName).append(": ").append(tierName);
            }
        } else {
            return "HRÁČ NEHODNOCEN";
        }
        
        return msg.toString();
    }
}
