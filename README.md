# CZSK Tier Tagger

Minecraft Fabric mod pro zobrazování CZSK PvP tierů u hráčů na serveru.

## ✨ Funkce

- **Automatické zobrazení tierů** - Tier tagy se automaticky zobrazují u jmen hráčů
- **Podpora více gamemodů** - Crystal, Sword, UHC, Pot, NPot, SMP, Axe, DiaSMP, Mace
- **Přepínání gamemodů** - Klávesová zkratka pro změnu zobrazovaného gamemodu
- **Příkazy** - `/czsktiers <jméno>` pro zobrazení detailů hráče
- **Automatická aktualizace** - Data se načítají z [CZSK Tierlist](https://b0tfleyz.github.io/CZSKtiers/overall)

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
build/devlibs/czsk-tier-tagger-1.0.0-dev.jar
```

---

## 📦 Instalace do Minecraftu

### Požadavky
- **Minecraft 1.21.4**
- **[Fabric Loader](https://fabricmc.net/use/)** 0.16.9+
- **[Fabric API](https://modrinth.com/mod/fabric-api)**

### Postup
1. Stáhněte a nainstalujte [Fabric Loader](https://fabricmc.net/use/) pro Minecraft 1.21.4
2. Stáhněte [Fabric API](https://modrinth.com/mod/fabric-api) a vložte do `.minecraft/mods/`
3. Sestavte mod (viz výše) nebo stáhněte JAR z [Releases](https://github.com/kozialondrej-jpg/cz-sk-pvp-comunity-tier-tagger-/releases)
4. Vložte `czsk-tier-tagger-1.0.0-dev.jar` do `.minecraft/mods/`
5. Spusťte Minecraft

---

## 🎮 Použití

### Příkazy

| Příkaz | Popis |
|--------|-------|
| `/czsktiers <jméno>` | Zobrazí detailní informace o tierech hráče |
| `/czsktiers refresh` | Znovu načte data z CZSK Tierlist |

### Klávesové zkratky

- **Změna gamemodu** - Ve výchozím nastavení není přiřazena žádná klávesa
  - Nastavte v: `Options → Controls → Key Binds → CZSK Tier Tagger`

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

- **Minecraft** 1.21.4
- **Fabric Loader** 0.16.9+
- **Fabric API** 0.109.0+
- **Mixin** - Pro modifikaci hry
- **Gson** - Pro práci s JSON

---

## 📝 Poznámky

- Data se automaticky cachují na 5 minut
- Mod funguje pouze na straně klienta
- Vyžaduje připojení k internetu pro načtení dat

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
