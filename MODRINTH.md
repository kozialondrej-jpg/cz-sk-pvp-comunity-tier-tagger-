# 🎯 CZSK Tier Tagger

**See CZSK PvP community tier rankings directly in Minecraft!**

This client-side Fabric mod automatically displays tier tags from the [CZSK PvP Tierlist](https://b0tfleyz.github.io/CZSKtiers/overall) above players' heads and in the TAB player list.

---

## ✨ Features

- **Automatic Nametag Display** — Tier tags appear next to player names (e.g., `[Sword LT4]`)
- **TAB List Integration** — View player tiers in the TAB player overview
- **9 Gamemodes Supported** — Crystal, Sword, UHC, Pot, NPot, SMP, Axe, DiaSMP, Mace
- **Gamemode Switching** — Keybind to cycle through different PvP modes
- **Player Lookup** — `/czsktiers <name>` to see detailed tier info for any player
- **Smart Caching** — Async data fetching with 5-minute cache (no game freezes!)
- **Live Updates** — Data synced from the official CZSK Tierlist

---

## 📋 Commands

| Command | Description |
|---------|-------------|
| `/czsktiers <name>` | Show detailed tier information for a player |
| `/czsktiers refresh` | Reload tier data from CZSK Tierlist |

---

## ⌨️ Keybinds

Configure in: **Options → Controls → Key Binds → CZSK Tiertagger**

- **Cycle Gamemode** — Switch between tier display modes (Crystal, Sword, All, etc.)

---

## 🎮 Supported Gamemodes

| Mode | Description |
|------|-------------|
| **All** | Shows player's best tier across all modes (default) |
| **Crystal** | Crystal PvP rankings |
| **Sword** | Sword PvP rankings |
| **UHC** | UHC rankings |
| **Pot** | Potion PvP rankings |
| **NPot** | No Potion PvP rankings |
| **SMP** | SMP rankings |
| **Axe** | Axe PvP rankings |
| **DiaSMP** | Diamond SMP rankings |
| **Mace** | Mace PvP rankings |
| **Mod Off** | Disable tier display |

---

## 📦 Requirements

- **Minecraft** 1.21.11
- **Fabric Loader** 0.16.0+
- **Fabric API** (required)

---

## ⚙️ Configuration

Config file location: `.minecraft/config/czsk_tier_tagger.json`

```json
{
  "gamemode": "All"
}
```

---

## 🏆 Tier System

| Tier | Points | Description |
|------|--------|-------------|
| HT1 | 60 | Highest Tier 1 |
| RHT1 | 54 | Retired Highest Tier 1 |
| LT1 | 48 | Lower Tier 1 |
| RLT1 | 43 | Retired Lower Tier 1 |
| HT2 | 32 | Highest Tier 2 |
| RHT2 | 29 | Retired Highest Tier 2 |
| LT2 | 24 | Lower Tier 2 |
| RLT2 | 22 | Retired Lower Tier 2 |
| HT3 | 16 | Highest Tier 3 |
| LT3 | 10 | Lower Tier 3 |
| HT4 | 5 | Highest Tier 4 |
| LT4 | 3 | Lower Tier 4 |
| HT5 | 2 | Highest Tier 5 |
| LT5 | 1 | Lower Tier 5 |

---

## 🔗 Links

- 🌐 [CZSK Tierlist Website](https://b0tfleyz.github.io/CZSKtiers/overall)
- 💬 [Discord Server](https://discord.gg/rAnR4hfKzw)
- 📁 [Source Code](https://github.com/kozialondrej-jpg/cz-sk-pvp-comunity-tier-tagger-)

---

## 📝 Notes

- **Client-side only** — No server installation required
- **Requires internet** — For initial tier data fetch
- Data cached for 5 minutes (use `/czsktiers refresh` to force reload)
