# Computerised — Design Specification

## Identity

Computerised is an exploration and automation pack in which programmable
turtles mine geographically separated mineral deposits, Create machinery
processes them, and scheduled freight trains connect remote industrial zones.
Fast travel moves players; physical transport moves industry.

> Turtles mine it. Create processes and moves it. Trains carry it. Stargates
> move the player. Computers coordinate the system.

## Non-negotiable rules

### Extraction

- Programmable CC:Tweaked turtles are the primary automated metal miners.
- Turnkey quarry blocks and digital miners are excluded.
- Create drills may excavate tunnels, shafts, stone, soil, and bulk minerals.
- Create block-breaking machinery must stop at protected metal deposits.
- Players and turtles must be able to mine those protected deposits normally.
- Coal, redstone, lapis, and Nether quartz do not stop Create drills.

### Geology

- Metal deposits are large, rare, and geographically constrained.
- Ordinary chunk-by-chunk strip mining should be ineffective compared with
  prospecting for deposits.
- Duplicate ore generation from content mods must be disabled.
- Initial protected resources: iron, copper, zinc, gold, diamond, and emerald.
- Candidate later metals must justify a unique gameplay role before inclusion.
- Diamond deposits may use coal as a visible geological halo.
- Preserve the recovered original deposit identities, biome constraints,
  poor-ore ratios, and distinct redstone, lapis, diamond, emerald, and quartz
  shapes. See `ORIGINAL_GEOLOGY.md`.

### Logistics

- Create trains are the primary long-distance item and fluid transport.
- Trains use stations, schedules, cargo conditions, pathfinding, and signals.
- Local belts, chutes, and pipes feed stations and factories.
- Cross-zone wireless item or fluid transfer is disallowed or tightly limited.

### Travel

- Stargates provide rapid player travel between established zones.
- Stargates must not transport loose items, fluids, contraptions, minecarts,
  turtles, or freight vehicles.
- A zone should be reached and developed physically before convenient gate
  travel becomes available.

### Progression

- Tinkers' Construct is the intended metal-tool and foundry system.
- Vanilla wooden and stone tools may remain for the opening stage.
- Create is infrastructure, processing, and freight—not the principal metal
  quarry system.
- Applied Energistics 2, if included, is late central storage rather than a
  long-distance transport bypass.

## Resource classification

| Class | Examples | Create drills |
|---|---|---|
| Construction geology | Stone, dirt, deepslate, gravel | Break normally |
| Bulk minerals | Coal, redstone, lapis, quartz | Break normally |
| Metal deposits | Iron, copper, zinc, gold | Stop |
| Precious cores | Diamond, emerald | Stop |

## Prototype 1: drill boundary

The first prototype uses Create's `create:non_breakable` block tag to protect
metal and precious ores.

Acceptance criteria:

1. A moving Create drill bores through stone and deepslate.
2. It mines coal, redstone, lapis, and Nether quartz.
3. It stops at iron, copper, zinc, gold, diamond, and emerald ore.
4. A player can mine the protected blocks normally.
5. A CC:Tweaked mining turtle can mine the protected blocks normally.
6. Removing the obstruction allows the boring machine to resume.

## Prototype 2: freight loop

1. A turtle deposits mined material into a station buffer.
2. A scheduled Create train loads it through a Portable Storage Interface.
3. Create signals safely manage at least two trains sharing track.
4. The train unloads into a central processing buffer.
5. The service repeats without player driving.

## Deferred decisions

- Exact deposit shapes and biome mappings
- Whether poor ore is represented by separate blocks or processing yield
- Stargate Journey restriction implementation
- Tinkers tool recipe restrictions
- Optional Botania and AE2 progression
- Final list of special metals and alloys
