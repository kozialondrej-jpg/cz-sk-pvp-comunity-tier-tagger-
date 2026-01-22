# CZSK Tier Tagger

Minecraft Fabric mod pro zobrazování CZSK PvP tierů u hráčů na serveru.

## ✨ Funkce

- **Automatické zobrazení tierů** - Tier tagy se automaticky zobrazují u jmen hráčů
- **Podpora více gamemodů** - Crystal, Sword, UHC, Pot, NPot, SMP, Axe, DiaSMP, Mace
- **Přepínání gamemodů** - Klávesová zkratka pro změnu zobrazovaného gamemodu
- **Příkazy** - `/czsktiers <jméno>` pro zobrazení detailů hráče
- **Automatická aktualizace** - Data se načítají z [CZSK Tierlist](https://b0tfleyz.github.io/CZSKtiers/)

## 📦 Instalace

1. Ujistěte se, že máte nainstalován [Fabric Loader](https://fabricmc.net/use/)
2. Stáhněte si [Fabric API](https://modrinth.com/mod/fabric-api)
3. Stáhněte mod ze složky `build/libs/czsk-tier-tagger-1.0.0.jar`
4. Vložte oba JAR soubory do složky `.minecraft/mods/`
5. Spusťte Minecraft 1.21.4

## 🎮 Použití

### Příkazy

- `/czsktiers <jméno>` - Zobrazí detailní informace o tierech hráče
- `/czsktiers refresh` - Znovu načte data z CZSK Tierlist

### Klávesové zkratky

- **Změna gamemodu** - Ve výchozím nastavení není přiřazena žádná klávesa
  - Nastavte v Minecraft: Options → Controls → Key Binds → CZSK Tier Tagger

### Gamemody

Mod podporuje přepínání mezi následujícími režimy:

- **All** - Zobrazuje nejlepší tier hráče (výchozí)
- **Crystal** - Crystal PvP
- **Sword** - Sword PvP
- **UHC** - UHC
- **Pot** - Pot PvP
- **NPot** - No Potion PvP
- **SMP** - SMP
- **Axe** - Axe PvP
- **DiaSMP** - Diamond SMP
- **Mace** - Mace PvP
- **Mod Off** - Vypne zobrazování tierů

## 🔧 Konfigurace

Konfigurační soubor se nachází v `.minecraft/config/czsk_tier_tagger.json`

```json
{
  "gamemode": "All"
}
```

## 📊 Tier Systém

Mod zobrazuje následující tiery (od nejlepšího):

- **HT1** (60 bodů) - Highest Tier 1
- **LT1** (48 bodů) - Lower Tier 1
- **RHT1** (54 bodů) - Retired Highest Tier 1
- **RLT1** (43 bodů) - Retired Lower Tier 1
- **HT2** (32 bodů) - Highest Tier 2
- **LT2** (24 bodů) - Lower Tier 2
- **RHT2** (29 bodů) - Retired Highest Tier 2
- **RLT2** (22 bodů) - Retired Lower Tier 2
- **HT3** (16 bodů)
- **LT3** (10 bodů)
- **HT4** (5 bodů)
- **LT4** (3 body)
- **HT5** (2 body)
- **LT5** (1 bod)

## 🛠️ Použité technologie

- **Minecraft** 1.21.4
- **Fabric Loader** 0.16.9+
- **Fabric API** 0.109.0+
- **MixinExtras** - Pro pokročilé mixiny
- **Apache POI** - Pro čtení Excel souborů

## 🏗️ Sestavení ze zdrojového kódu

```bash
git clone https://github.com/kozialondrej-jpg/cz-sk-pvp-comunity-tier-tagger-.git
cd cz-sk-pvp-comunity-tier-tagger-
gradle build
```

Sestavený mod najdete v `build/libs/czsk-tier-tagger-1.0.0.jar`

## 📝 Poznámky

- Data se automaticky cachují na 5 minut
- Mod funguje pouze na straně klienta
- Vyžaduje připojení k internetu pro načtení dat
- Ikony tierů jsou zobrazeny pomocí custom fontu

## 🔗 Odkazy

- [CZSK Tierlist Web](https://b0tfleyz.github.io/CZSKtiers/)
- [Discord Server](https://discord.gg/rAnR4hfKzw)
- [Zdrojový kód](https://github.com/kozialondrej-jpg/cz-sk-pvp-comunity-tier-tagger-)

## 📜 Licence

CC0-1.0 - Volně k použití

## ⚠️ Upozornění

Tento mod načítá data z veřejného Google Sheets dokumentu. Pro správnou funkčnost je nutné připojení k internetu.

## 🐛 Hlášení chyb

Pokud najdete chybu nebo máte nápad na vylepšení, vytvořte issue na GitHubu.
