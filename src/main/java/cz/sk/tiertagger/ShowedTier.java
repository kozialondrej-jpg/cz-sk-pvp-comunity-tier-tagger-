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
    
    private static final Map<String, String> TIER_EMOJI = Map.ofEntries(
        Map.entry("-", ""),
        Map.entry("1", "\uEE00"),   // LT5
        Map.entry("2", "\uEE01"),   // HT5
        Map.entry("3", "\uEE02"),   // LT4
        Map.entry("5", "\uEE03"),   // HT4
        Map.entry("10", "\uEE04"),  // LT3
        Map.entry("16", "\uEE05"),  // HT3
        Map.entry("24", "\uEE06"),  // LT2
        Map.entry("32", "\uEE07"),  // HT2
        Map.entry("48", "\uEE08"),  // LT1
        Map.entry("60", "\uEE09"),  // HT1
        Map.entry("22", "\uEE0a"),  // RLT2
        Map.entry("29", "\uEE0b"),  // RHT2
        Map.entry("43", "\uEE0c"),  // RLT1
        Map.entry("54", "\uEE0d")   // RHT1
    );
    
    private static final Map<String, String> KIT_EMOJI = Map.ofEntries(
        Map.entry("Mace", "\uEF00"),
        Map.entry("SMP", "\uEF01"),
        Map.entry("UHC", "\uEF02"),
        Map.entry("Pot", "\uEF03"),
        Map.entry("Crystal", "\uEF04"),
        Map.entry("Sword", "\uEF05"),
        Map.entry("DiaSMP", "\uEF06"),
        Map.entry("NPot", "\uEF07"),
        Map.entry("Axe", "\uEF08")
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
                    return " " + 
                           TIER_EMOJI.getOrDefault(tier.tier, "") + 
                           KIT_EMOJI.getOrDefault(tier.category, "");
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
            return " " + 
                   TIER_EMOJI.getOrDefault(bestTier.tier, "") + 
                   KIT_EMOJI.getOrDefault(bestTier.category, "");
        }
        
        return "";
    }
    
    public static String showedMessage(PlayerInfo info) {
        if (info == null) {
            return "HRÁČ NENALEZEN";
        }
        
        StringBuilder msg = new StringBuilder();
        msg.append("\ued09 CZSK TIERLIST \ued09\n");
        msg.append("HRÁČ: ").append(info.nick).append("\n");
        msg.append("CELKOVÉ SKÓRE: ").append(info.score).append(" bodů\n");
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
                String tierName = TIER_NAMES.getOrDefault(tier.tier, tier.tier);
                msg.append("\n      ")
                   .append(TIER_EMOJI.getOrDefault(tier.tier, ""))
                   .append(" ")
                   .append(KIT_EMOJI.getOrDefault(tier.category, ""))
                   .append(" ")
                   .append(tier.category)
                   .append(" - ")
                   .append(tierName)
                   .append(" (")
                   .append(tier.tier)
                   .append(" bodů)");
            }
        } else {
            return "HRÁČ NEHODNOCEN";
        }
        
        return msg.toString();
    }
}
