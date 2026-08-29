# Computerised Rebuilt — Handover

Last updated: 2026-08-28

## Project location

`D:\MinecraftModding\Computerised-Rebuilt`

The preserved original Minecraft 1.10.2 CurseForge instance is located at:

`D:\CurseForge\Instances\Computerised`

Do not develop inside or modify the original instance. It is the archaeological
reference for the rebuild.

## Recovered original intent

Computerised is an exploration and automation pack built around this loop:

1. Locate large, rare, geographically constrained mineral deposits.
2. Establish remote mining sites.
3. Extract metal deposits using programmable turtles.
4. Use physical freight trains to carry bulk resources between zones.
5. Use Stargates for player travel, not freight transfer.
6. Process and store resources in central, computer-coordinated factories.

The name **Computerised** primarily refers to creating programmable turtle
quarries instead of crafting a turnkey quarry block.

The concise identity statement is:

> Turtles mine it. Create processes and moves it. Trains carry it. Stargates
> move the player. Computers coordinate the system.

See `DESIGN.md` for the full design specification.

## Technical direction

- Minecraft: 1.20.1
- Loader: Forge 47.4.23
- Pack format: Packwiz 1.1.0
- Java: 17
- Initial industrial and railway backbone: Create
- Programmable quarry system: CC:Tweaked turtles
- Global datapack loader: Paxi
- Candidate passenger travel: Stargate Journey
- Candidate tool/foundry progression: Tinkers' Construct

Minecraft 1.20.1 Forge was chosen because it has the strongest overlap between
Create, CC:Tweaked, Stargate Journey, Tinkers' Construct, AE2, and Botania.

Create trains were selected over Immersive Railroading for the initial build
because Create supplies schedules, pathfinding, signals, junction handling,
cargo conditions, item/fluid interfaces, and CC:Tweaked integration.

## Critical mining rule

Create drills are allowed to excavate bore tunnels, railway cuttings, shafts,
and ordinary geology. They must stop at metal deposits, leaving turtles or
players to extract the valuable material.

Create drills may mine these bulk minerals without stopping:

- Coal
- Redstone
- Lapis
- Nether quartz

The initial protected set is:

- Iron
- Copper
- Zinc
- Gold
- Diamond
- Emerald

This is implemented using Create's `create:non_breakable` block tag at:

`pack/config/paxi/datapacks/computerised_drill_rules/data/create/tags/blocks/non_breakable.json`

Because the tag only affects Create's block-breaking kinetics, player tools and
CC:Tweaked turtles should remain able to mine the protected blocks.

## Current repository state

The Git repository has been initialized on branch `main`, but no commit has yet
been created.

Created files include:

- `README.md`
- `DESIGN.md`
- `HANDOVER.md`
- `.gitignore`
- `pack/pack.toml`
- `pack/index.toml`
- `pack/.packwizignore`
- Packwiz mod metadata under `pack/mods/`
- The prototype drill-rule datapack under `pack/config/paxi/datapacks/`

Pinned initial mods:

| Mod | Version |
|---|---|
| Create | 1.20.1-6.0.8 |
| CC:Tweaked | 1.20.1 Forge 1.120.2 |
| Paxi | 1.20 Forge 4.0 |
| YUNG's API | 1.20 Forge 4.0.6 |

Required Create runtime libraries are also pinned:

| Library | Version |
|---|---|
| Flywheel | 1.0.5 |
| Ponder | 1.0.91 |
| Vanillin | 1.0.0 |
| Just Enough Items | 15.20.0.106 |
| Chop Down Updated | 1.4.0 |
| Dynamic Ore Veins | 1.0.0 for 1.20.x Forge |

The mod metadata download URLs were checked successfully and returned HTTP 200.
The actual JAR files are intentionally not tracked by Git.

## Packwiz tooling

Packwiz is bootstrapped locally rather than installed globally. Run
`scripts/build.ps1`; it installs Go and Packwiz under the ignored `.tools`
directory, refreshes the index, and exports the prototype `.mrpack` under
`build/`.

The current index hash is:

`1947a9920a77eb94094031cadea3907c622cac054d280c1f6f0301a0f5706cdb`

The generated prototype export passed packaging validation. See `TESTING.md`.

## Completed milestone: runnable drill prototype

Do not add the full mod list yet. First prove the defining mechanical rule.

The core rule passed interactive testing: the Create drill mined ordinary
blocks, coal, redstone, and lapis; protected ores resisted it; players and
CC:Tweaked turtles could mine the protected ores. Nether quartz and resumption
after obstruction removal were explicitly deferred by the operator. See
`TESTING.md` for the complete record.

## Completed milestone: freight prototype

The operator confirmed the complete loop on 2026-08-29: a turtle fed the Mine
buffer, Portable Storage Interfaces loaded and unloaded the train, a repeating
schedule moved cargo without player driving, and Create signals safely managed
a second train. See `TESTING.md`.

## Next milestone: deposit geology

Port the recovered original Custom Ore Generation design documented in
`ORIGINAL_GEOLOGY.md`. It covers biome-constrained, poor-ore motherlodes for
iron/nickel, gold, copper, tin, lead, silver, zinc, aluminium, and platinum;
vertical redstone and lapis veins; coal-marked diamond pipes; mountain emerald
pipes; and strategic Nether quartz clouds. Remove ordinary generation for the
replaced resources and preserve the accepted Create drill-protection rules.
Coal, redstone, lapis, and quartz remain breakable by drills even though their
generation is customized.

An initial Overworld implementation now exists in the
`computerised_geology` Paxi datapack using Dynamic Ore Veins. It replaces
vanilla scattered generation for iron, gold, copper, zinc, diamond, emerald,
coal, redstone, and lapis. The dedicated server loaded all nine definitions
without registry or datapack errors. This is a balancing prototype and still
requires fresh-world visual testing; follow `GEOLOGY_TESTING.md`.

Nether quartz, dedicated poor-ore blocks, and metals not yet supplied by the
pack (tin, lead, silver, nickel, aluminium, and platinum) remain for later
passes.

## Deferred work

- Poor-ore representation and processing yields
- Final metal list and duplicate-worldgen removal
- Stargate Journey evaluation and player-only traversal restrictions
- Tinkers' Construct progression and conventional tool restrictions
- AE2 and Botania progression
- Custom `computerised` Forge mod, if datapacks cannot enforce all rules
- Recreation or replacement of the original gilded-steel concepts

## Important cautions

- Do not copy the old mod/config folders wholesale into the rebuild.
- Do not begin with a large kitchen-sink mod list.
- Do not add Create quarry contraptions that bypass the turtle role.
- Avoid broad `#forge:ores` protection: coal, redstone, and lapis are deliberate
  exceptions.
- Treat Stargates as passenger infrastructure; prevent non-player freight
  traversal if the selected mod allows it by default.
- Keep long-distance wireless item/fluid transfer disabled or tightly limited.
