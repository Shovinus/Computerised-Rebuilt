# Geology Prototype Testing

The geology datapack changes world generation. Existing generated chunks will
not be rewritten, so test in a newly created default world after importing the
latest pack.

## Current scope

Implemented in the Overworld:

- Rare iron deposits in plains and taiga biomes
- Rare gold deposits in badlands biomes
- Rare copper deposits in forest biomes
- Rare zinc deposits in swamp biomes
- Very rare diamond deposits with coal geology
- Rare emerald deposits in mountain biomes
- Large coal deposits
- Narrow vertical redstone systems
- Narrow vertical lapis systems
- Removal of the corresponding vanilla scattered ore features
- Removal of Create's ordinary zinc feature
- Removal of vanilla large iron and copper veins

The first pass uses low-density ordinary ore blocks to approximate the original
poor-ore deposits. Dedicated poor-ore blocks and the absent tin, lead, silver,
nickel, aluminium, and platinum resources require later content decisions.
Nether quartz strategic clouds are also deferred to a separate Nether pass.

## Test procedure

1. Import the latest `.mrpack` into a new launcher instance.
2. Create a new default creative world. Do not reuse the drill or freight test
   worlds for generation conclusions.
3. Confirm the world loads without a datapack error screen.
4. Use `/locate biome` to visit the intended biome for each deposit.
5. Use spectator mode to inspect underground geology across several hundred
   blocks. Normal scattered ores should be absent and deposits should be
   conspicuously larger than vanilla clusters.
6. Confirm iron appears only in the configured plains/taiga families, copper in
   forests, gold in badlands, zinc in swamps, and emerald in mountain families.
7. Confirm a diamond-bearing region is visibly associated with coal ore or coal
   blocks.
8. Mine part of each deposit with a turtle and confirm Create drills still stop
   at protected ores.
9. Report deposits that are too frequent, too sparse, too rich, too thin, or too
   large. The initial noise thresholds are deliberately a balancing prototype.

World-generation changes require another fresh world or unexplored chunks for
reliable retesting.
