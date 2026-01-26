package cz.sk.tiertagger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import cz.sk.tiertagger.tiers.PlayerInfo;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandManager {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("czsktiers")
            .then(argument("jmeno", StringArgumentType.word())
                .suggests(playerNameSuggester())
                .executes(context -> {
                    String name = StringArgumentType.getString(context, "jmeno");
                    
                    new Thread(() -> {
                        PlayerInfo info = DataFetcher.getPlayerInfo(name);
                        MinecraftClient client = MinecraftClient.getInstance();
                        
                        if (client.player != null) {
                            if (info != null) {
                                Text text = Text.literal(ShowedTier.showedMessage(info))
                                    .styled(s -> s.withColor(Formatting.GOLD));
                                client.player.sendMessage(text, false);
                            } else {
                                client.player.sendMessage(
                                    Text.literal("Hráč '" + name + "' nebyl nalezen v CZSK Tierlistu")
                                        .styled(s -> s.withColor(Formatting.RED)), 
                                    false
                                );
                            }
                        }
                    }).start();
                    
                    return 1;
                }))
        );
        
        dispatcher.register(literal("czsktiers")
            .then(literal("refresh")
                .executes(context -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null) {
                        client.player.sendMessage(
                            Text.literal("Obnovuji CZSK Tierlist data...").styled(s -> s.withColor(Formatting.YELLOW)), 
                            false
                        );
                    }
                    
                    new Thread(() -> {
                        DataFetcher.refreshCache();
                        if (client.player != null) {
                            client.player.sendMessage(
                                Text.literal("Data úspěšně obnovena!").styled(s -> s.withColor(Formatting.GREEN)), 
                                false
                            );
                        }
                    }).start();
                    
                    return 1;
                }))
        );
    }
    
    private static SuggestionProvider<FabricClientCommandSource> playerNameSuggester() {
        return (context, builder) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getNetworkHandler() == null) {
                return builder.buildFuture();
            }

            Collection<PlayerListEntry> players = client.getNetworkHandler().getPlayerList();
            List<String> names = players.stream()
                .map(entry -> entry.getProfile().name())
                .collect(Collectors.toList());

            for (String name : names) {
                builder.suggest(name);
            }

            return builder.buildFuture();
        };
    }
}
