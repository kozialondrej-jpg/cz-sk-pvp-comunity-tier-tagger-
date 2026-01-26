package cz.sk.tiertagger;

import cz.sk.tiertagger.tiers.PlayerInfo;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class Keybinds {
    private static KeyBinding changeGamemode;
    
    static List<String> gamemodes = List.of(
        "All",
        "Crystal",
        "Sword",
        "UHC",
        "Pot",
        "NPot",
        "SMP",
        "Axe",
        "DiaSMP",
        "Mace",
        "Mod Off"
    );
    
    public static void registerKeybinds() {
        changeGamemode = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.czsk_tier_tagger.change_gamemode",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            KeyBinding.Category.MISC
        ));
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (changeGamemode.wasPressed()) {
                ModConfig config = ConfigManager.getConfig();
                String actualGamemode = config.gamemode;
                Integer index = gamemodes.indexOf(actualGamemode);
                
                if (index == -1) index = 0;
                
                index++;
                if (index >= gamemodes.size()) {
                    index = 0;
                }
                
                String newGamemode = gamemodes.get(index);
                config.gamemode = newGamemode;
                ConfigManager.save();
                
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.sendMessage(
                        Text.literal("Gamemode zvolen: ")
                            .append(Text.literal(newGamemode).styled(s -> s.withColor(Formatting.AQUA))),
                        true
                    );
                }
                
                // Aktualizuj všechny hráče
                ClientWorld world = MinecraftClient.getInstance().world;
                if (world != null) {
                    for (AbstractClientPlayerEntity player : world.getPlayers()) {
                        new Thread(() -> {
                            PlayerInfo info = DataFetcher.getPlayerInfo(player.getName().getString());
                            if (info != null) {
                                ((TierModifier) player).setSuffix(ShowedTier.showedTier(info));
                            }
                        }).start();
                    }
                }
            }
        });
    }
}
