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
    
    private static final Map<String, PlayerInfo> cachedData = new HashMap<>();
    private static long lastFetchTime = 0;
    private static final long CACHE_DURATION = 300000; // 5 minut cache
    private static volatile boolean isFetching = false;
    private static volatile boolean initialFetchDone = false;
    
    private static final String[] KIT_NAMES = {"Crystal", "Axe", "Sword", "UHC", "Npot", "Pot", "SMP", "DiaSMP", "Mace"};
    
    public static PlayerInfo getPlayerInfo(String playerName) {
        // Pokud cache je starý, spusť refresh na pozadí (ale neblokuj)
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastFetchTime > CACHE_DURATION || (!initialFetchDone && cachedData.isEmpty())) && !isFetching) {
            synchronized (DataFetcher.class) {
                // Double-check locking pattern
                if (!isFetching && ((currentTime - lastFetchTime > CACHE_DURATION) || (!initialFetchDone && cachedData.isEmpty()))) {
                    isFetching = true;
                    new Thread(() -> fetchAllData(), "CZSK-Tier-Fetcher").start();
                }
            }
        }
        
        return cachedData.get(playerName.toLowerCase());
    }
    
    private static void fetchAllData() {
        try {
            CzskTierTagger.LOGGER.info("[DataFetcher] Zahajuji stahování CZSK Tier dat...");
            long startTime = System.currentTimeMillis();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CSV_URL))
                .GET()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(java.time.Duration.ofSeconds(15))
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            CzskTierTagger.LOGGER.debug("[DataFetcher] HTTP Status: " + response.statusCode());
            
            if (response.statusCode() == 200) {
                String body = response.body();
                if (body == null || body.isEmpty()) {
                    CzskTierTagger.LOGGER.error("[DataFetcher] Response body je prázdný!");
                    return;
                }
                
                CzskTierTagger.LOGGER.debug("[DataFetcher] Staženo " + body.length() + " bytů");
                parseCSVData(body);
                lastFetchTime = System.currentTimeMillis();
                initialFetchDone = true;
                
                long duration = System.currentTimeMillis() - startTime;
                CzskTierTagger.LOGGER.info("[DataFetcher] Dokončeno za " + duration + "ms");
            } else {
                CzskTierTagger.LOGGER.error("[DataFetcher] Chyba: HTTP " + response.statusCode());
                String responseBody = response.body();
                if (responseBody != null && !responseBody.isEmpty()) {
                    CzskTierTagger.LOGGER.error("[DataFetcher] Response: " + responseBody.substring(0, Math.min(200, responseBody.length())));
                }
            }
        } catch (Exception e) {
            CzskTierTagger.LOGGER.error("[DataFetcher] Kritická chyba při stahování: " + e.getMessage(), e);
        } finally {
            isFetching = false;
        }
    }
    
    private static void parseCSVData(String csvData) {
        try {
            String[] lines = csvData.split("\n");
            if (lines.length < 2) {
                CzskTierTagger.LOGGER.error("[CSV Parser] Chyba: CSV má méně než 2 řádky");
                return;
            }
            
            // Parsuj hlavičku
            String[] headers = parseCSVLine(lines[0]);
            Map<String, Integer> columnIndices = new HashMap<>();
            
            CzskTierTagger.LOGGER.debug("[CSV Parser] Nalezena sloupce: " + Arrays.toString(headers));
            
            for (int i = 0; i < headers.length; i++) {
                String headerName = headers[i].trim();
                columnIndices.put(headerName, i);
            }
            
            // Ověř, že existují důležité sloupce
            if (!columnIndices.containsKey("Nick")) {
                CzskTierTagger.LOGGER.error("[CSV Parser] Sloupec 'Nick' nenalezen!");
                CzskTierTagger.LOGGER.error("[CSV Parser] Dostupné sloupce: " + columnIndices.keySet());
                return;
            }
            
            // Vytvoř novou mapu místo čištění staré (thread-safe)
            Map<String, PlayerInfo> newData = new HashMap<>();
            int loadedPlayers = 0;
            int skippedPlayers = 0;
            
            // Projdi všechny řádky
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) {
                    skippedPlayers++;
                    continue;
                }
                
                String[] values = parseCSVLine(line);
                
                if (values.length < 2) {
                    skippedPlayers++;
                    continue;
                }
                
                try {
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
                    
                    // Validuj, že máme alespoň nick
                    if (player.nick == null || player.nick.isEmpty()) {
                        skippedPlayers++;
                        continue;
                    }
                    
                    // Načti tiery pro všechny kity
                    List<Tier> tiers = new ArrayList<>();
                    int totalScore = 0;
                    
                    for (String kitName : KIT_NAMES) {
                        Integer colIndex = columnIndices.get(kitName);
                        
                        if (colIndex != null && colIndex < values.length) {
                            String tierValue = values[colIndex].trim();
                            if (!tierValue.isEmpty() && !tierValue.equals("-")) {
                                try {
                                    Tier tier = new Tier();
                                    tier.category = kitName.equals("Npot") ? "NPot" : kitName;
                                    tier.tier = tierValue;
                                    tiers.add(tier);
                                    
                                    totalScore += Integer.parseInt(tierValue);
                                } catch (NumberFormatException e) {
                                    CzskTierTagger.LOGGER.debug("Chyba pri parsovani tier hodnoty: " + tierValue);
                                }
                            }
                        }
                    }
                    
                    player.tiers = tiers;
                    player.score = totalScore;
                    
                    newData.put(player.nick.toLowerCase(), player);
                    loadedPlayers++;
                } catch (Exception e) {
                    CzskTierTagger.LOGGER.warn("Chyba pri zpracovani radku " + i + ": " + e.getMessage());
                    skippedPlayers++;
                }
            }
            
            // Atomicky nahraď starou cache novou
            synchronized (DataFetcher.class) {
                cachedData.clear();
                cachedData.putAll(newData);
            }
            
            CzskTierTagger.LOGGER.info("[CSV Parser] Úspěšně načteno " + loadedPlayers + " hráčů");
            if (skippedPlayers > 0) {
                CzskTierTagger.LOGGER.debug("[CSV Parser] Přeskočeno " + skippedPlayers + " řádků");
            }
            
        } catch (Exception e) {
            CzskTierTagger.LOGGER.error("Chyba při parsování CSV dat: " + e.getMessage(), e);
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
        synchronized (DataFetcher.class) {
            if (!isFetching) {
                lastFetchTime = 0;
                isFetching = true;
                new Thread(() -> fetchAllData(), "CZSK-Tier-Fetcher-Manual").start();
            } else {
                CzskTierTagger.LOGGER.info("[DataFetcher] Fetch již probíhá, přeskakuji...");
            }
        }
    }
    
    public static boolean isDataLoaded() {
        return initialFetchDone && !cachedData.isEmpty();
    }
    
    public static int getCachedPlayerCount() {
        return cachedData.size();
    }
}
