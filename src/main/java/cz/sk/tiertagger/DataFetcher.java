package cz.sk.tiertagger;

import cz.sk.tiertagger.tiers.PlayerInfo;
import cz.sk.tiertagger.tiers.Tier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class DataFetcher {
    private static final HttpClient client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();
    private static final String CSV_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vTsYd1Hv8XjsdskgT2O-_Otwe3DKxXTXECPE0s4JcPwPPnLMMpknU_-y8EHNBZTtVEQgzicFKcgluSU/pub?output=csv";
    
    private static Map<String, PlayerInfo> cachedData = new HashMap<>();
    private static long lastFetchTime = 0;
    private static final long CACHE_DURATION = 300000; // 5 minut cache
    
    private static final String[] KIT_NAMES = {"Crystal", "Axe", "Sword", "UHC", "Npot", "Pot", "SMP", "DiaSMP", "Mace"};
    
    public static PlayerInfo getPlayerInfo(String playerName) {
        // Aktualizuj cache pokud je starý
        if (System.currentTimeMillis() - lastFetchTime > CACHE_DURATION || cachedData.isEmpty()) {
            fetchAllData();
        }
        
        return cachedData.get(playerName.toLowerCase());
    }
    
    private static void fetchAllData() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CSV_URL))
                .GET()
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                parseCSVData(response.body());
                lastFetchTime = System.currentTimeMillis();
            }
        } catch (Exception e) {
            System.err.println("Chyba při stahování CZSK Tier dat: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void parseCSVData(String csvData) {
        try {
            String[] lines = csvData.split("\n");
            if (lines.length < 2) return;
            
            // Parsuj hlavičku
            String[] headers = parseCSVLine(lines[0]);
            Map<String, Integer> columnIndices = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                columnIndices.put(headers[i].trim(), i);
            }
            
            cachedData.clear();
            
            // Projdi všechny řádky
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) continue;
                
                String[] values = parseCSVLine(line);
                
                PlayerInfo player = new PlayerInfo();
                
                // Načti UUID a Nick
                Integer uuidIndex = columnIndices.get("UUID");
                Integer nickIndex = columnIndices.get("Nick");
                
                if (uuidIndex != null && uuidIndex < values.length) {
                    player.uuid = values[uuidIndex].trim();
                }
                if (nickIndex != null && nickIndex < values.length) {
                    player.nick = values[nickIndex].trim();
                }
                
                if (player.nick == null || player.nick.isEmpty()) continue;
                
                // Načti tiery pro všechny kity
                List<Tier> tiers = new ArrayList<>();
                int totalScore = 0;
                
                for (String kitName : KIT_NAMES) {
                    Integer colIndex = columnIndices.get(kitName);
                    
                    if (colIndex != null && colIndex < values.length) {
                        String tierValue = values[colIndex].trim();
                        if (!tierValue.isEmpty() && !tierValue.equals("-")) {
                            Tier tier = new Tier();
                            tier.category = kitName.equals("Npot") ? "NPot" : kitName;
                            tier.tier = tierValue;
                            tiers.add(tier);
                            
                            try {
                                totalScore += Integer.parseInt(tierValue);
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
                
                player.tiers = tiers;
                player.score = totalScore;
                
                cachedData.put(player.nick.toLowerCase(), player);
            }
            
            System.out.println("[CZSK Tier Tagger] Načteno " + cachedData.size() + " hráčů z CZSK Tierlist");
            
        } catch (Exception e) {
            System.err.println("Chyba při parsování CSV dat: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result.toArray(new String[0]);
    }
    
    public static void refreshCache() {
        lastFetchTime = 0;
        fetchAllData();
    }
}
