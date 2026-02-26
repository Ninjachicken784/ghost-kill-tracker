# Ghost Kill Tracker — Fabric Mod (1.21.1)

A Fabric client-side mod that displays a live HUD overlay tracking your ghost kills per session.

## HUD Display
Located in the **top-right** corner of your screen:

| Label | Meaning              |
|-------|----------------------|
| T/k   | Total kills this session |
| K/h   | Kills per hour       |
| C/k   | Average coins per kill |
| Coins | Total coins earned   |

## Keybinds (rebindable in Controls)
| Key | Action |
|-----|--------|
| `H` | Toggle HUD on/off |
| `J` | Reset session (zeroes all stats) |

## Installation
1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft **1.21.1**
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Drop the compiled `.jar` into your `mods/` folder

## Building from Source
```bash
./gradlew build
```
The compiled jar will appear in `build/libs/`.

## Chat Message Detection
The mod listens for chat messages matching ghost kill patterns. It is designed for **Hypixel Skyblock** ghost farming. If your server uses different kill messages, edit the regex patterns in:

`src/main/java/com/ghostkilltracker/mixin/ClientPlayNetworkHandlerMixin.java`

Look for `GHOST_KILL_PATTERN`, `COIN_PATTERN`, and `HYPIXEL_GHOST_KILL` — adjust them to match your server's kill/coin messages exactly.

## Compatibility
- Minecraft 1.21.1
- Fabric Loader ≥ 0.16.0
- Fabric API required
