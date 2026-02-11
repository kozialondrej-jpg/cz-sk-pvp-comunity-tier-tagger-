# CZSK Tier Tagger

Minecraft Fabric mod pro zobrazování CZSK PvP tierů u hráčů na serveru.

## ✨ Funkce

- **Automatické zobrazení tierů v nametazích** - Tier tagy se automaticky zobrazují vedle jmen hráčů
- **Zobrazení tierů v tab listu** - Vidíš tiery hráčů také v TAB přehledu
- **Textové formáty** - Tiery se zobrazují jako text (např. `[Sword LT4]`)
- **Podpora více gamemodů** - Crystal, Sword, UHC, Pot, NPot, SMP, Axe, DiaSMP, Mace
- **Přepínání gamemodů** - Klávesová zkratka pro dynamickou změnu zobrazovaného gamemodu
- **Príkazy** - `/czsktiers <jméno>` pro zobrazení detailů hráče, `/czsktiers refresh` pro reload cache
- **Automatická aktualizace** - Data se načítají z [CZSK Tierlist](https://b0tfleyz.github.io/CZSKtiers/overall)
- **Asynchronní cache** - Hra se nezmrazí při načítání dat (cache na 5 minut)

---

## 🏗️ Sestavení modu (Build)

### Požadavky
- **Java 21** nebo novější
- **Git**

### Postup

**1. Klonování repozitáře:**
```bash
git clone https://github.com/kozialondrej-jpg/cz-sk-pvp-comunity-tier-tagger-.git
cd cz-sk-pvp-comunity-tier-tagger-
```

**2. Sestavení modu:**

Na Linux/macOS:
```bash
./gradlew build
```

Na Windows:
```cmd
gradlew.bat build
```

**3. Výstupní soubor:**
```
build/libs/czsk-tier-tagger-1.0.1.jar
```

---

## 📦 Instalace do Minecraftu

### Požadavky
- **Minecraft 1.21.5**
- **[Fabric Loader](https://fabricmc.net/use/)** 0.16.10+
- **[Fabric API](https://modrinth.com/mod/fabric-api)** 0.115.1+

### Postup
1. Stáhněte a nainstalujte [Fabric Loader](https://fabricmc.net/use/) pro Minecraft 1.21.5
2. Stáhněte [Fabric API](https://modrinth.com/mod/fabric-api) a vložte do `.minecraft/mods/`
3. Sestavte mod (viz výše) nebo stáhněte JAR z [Modrinth](https://modrinth.com/mod/czsk-tiertagger)
4. Vložte JAR soubor do `.minecraft/mods/`
5. Spusťte Minecraft

---

## 🎮 Použití

### Příkazy

| Příkaz | Popis |
|--------|-------|
| `/czsktiers <jméno>` | Zobrazí detailní informace o tierech hráče |
| `/czsktiers refresh` | Znovu načte data z CZSK Tierlist |

### Klávesové zkratky

- **Změna gamemodu** (výchozí: Unassigned)
  - Nastavte v: `Options → Controls → Key Binds → CZSK Tiertagger → Změnit zobrazovaný gamemode`
  - Stisknutím klávesy se gamemody budou cyklicky přepínat

### Gamemody

| Gamemode | Popis |
|----------|-------|
| **All** | Zobrazuje nejlepší tier hráče (výchozí) |
| **Crystal** | Crystal PvP |
| **Sword** | Sword PvP |
| **UHC** | UHC |
| **Pot** | Pot PvP |
| **NPot** | No Potion PvP |
| **SMP** | SMP |
| **Axe** | Axe PvP |
| **DiaSMP** | Diamond SMP |
| **Mace** | Mace PvP |
| **Mod Off** | Vypne zobrazování tierů |

---

## 🔧 Konfigurace

Konfigurační soubor: `.minecraft/config/czsk_tier_tagger.json`

```json
{
  "gamemode": "All"
}
```

---

## 📊 Tier Systém

| Tier | Body | Popis |
|------|------|-------|
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

## 🛠️ Použité technologie

- **Minecraft** 1.21.5
- **Fabric Loader** 0.16.10+
- **Fabric API** 0.115.1+
- **Mixin & MixinExtras** - Pro modifikaci renderování nametaguů
- **Gson** - Pro práci s JSON
- **Google Sheets API** - Pro načítání tier dat

---

## 📝 Poznámky

- Data se automaticky cachují na 5 minut (lze resetovat příkazem `/czsktiers refresh`)
- Tierové tagy se zobrazují v nametazích ve formátu `[Kit Tier]` (např. `[Sword LT4]`)
- Tierové tagy se zobrazují v TAB listu (přehledu hráčů)
- Koláčové tagy v příkazech se zobrazují bez hranatých závorek
- Mod funguje pouze na straně klienta (client-side mod)
- Vyžaduje připojení k internetu pro inicializaci dat

---

## 🔗 Odkazy

- 🌐 [CZSK Tierlist Web](https://b0tfleyz.github.io/CZSKtiers/overall)
- 💬 [Discord Server](https://discord.gg/rAnR4hfKzw)
- 📁 [Zdrojový kód](https://github.com/kozialondrej-jpg/cz-sk-pvp-comunity-tier-tagger-)

---

## 📜 Licence

CC0-1.0 - Volně k použití

---

## 🐛 Hlášení chyb

Pokud najdete chybu nebo máte nápad na vylepšení, vytvořte [Issue](https://github.com/kozialondrej-jpg/cz-sk-pvp-comunity-tier-tagger-/issues) na GitHubu.
