# Computerised OreGen port

This project ports the generation engine of CustomOreGen to Forge 1.20.1. It
does not register poor ores, metals, or processing recipes. Ore compositions
refer to arbitrary block registry IDs so content remains the modpack's choice.

## Compatibility target

The first target is semantic compatibility with the distributions used by the
original Computerised pack, rather than immediate compatibility with every
historical CustomOreGen module.

Required distribution families:

1. branching motherlodes and child distributions;
2. vertical and pipe veins;
3. irregular clouds with attached hint veins;
4. vanilla-style clusters and substitution distributions;
5. weighted block mixtures, replacement rules, biome restrictions, and
   deterministic generation across chunk boundaries.

## Implementation boundary

The reusable distribution mathematics, geometry, probability distributions,
and XML vocabulary come from CustomOreGen. Minecraft-facing code is replaced
with a Forge 1.20.1 implementation based on registered world-generation
features and modern block, biome, dimension, and tag registry IDs.

Large deposits must be planned from the world seed independently of chunk load
order. Each chunk rasterises only the pieces intersecting it; generation must
never synchronously write into neighbouring chunks. This avoids cascading
world generation.

## First executable milestone

Load a reduced XML configuration for the recovered iron distribution and place
one deterministic branching motherlode across multiple chunks. The engine must
select its configured composition by weight (70% poor iron, 20% poor nickel,
10% normal iron), but the test configuration may use temporary vanilla block
IDs until the pack supplies the poor-ore blocks.

## Implemented engine surface

The `computerised_oregen:motherlode` configured-feature type currently supports:

- occurrence frequency per source chunk, including fractional frequencies;
- bounded uniform and normal distributions with CustomOreGen-compatible normal
  sampling;
- independently sampled ellipsoidal motherlode axes;
- configurable branch count, length, inclination and vertical limits;
- segmented tube branches with turn and pitch variation;
- recursive forks with configurable frequency and remaining-length multiplier;
- weighted arbitrary block states;
- ore density, replacement block tags and optional biome tags;
- deterministic seed salts; and
- multi-chunk deposits whose intersecting geometry is reconstructed and written
  only inside the chunk currently generating.

The built-in `development_iron_motherlode` exercises the recovered iron
parameters. Its temporary 70/20/10 iron/copper/gold block mixture exists only
to make composition visible until real poor-iron and poor-nickel registry IDs
are supplied.

Still to port are child-distribution attachment rules, Bezier interpolation,
clouds, substitution passes, standard clusters, XML parsing and legacy command
visualisation.

## Configuration shape

Configured features use normal 1.20.1 datapack JSON. Important fields are:

- `frequency`, `height`, and `salt` for placement;
- `shape` for motherlode and branch distributions;
- `blocks` for weighted block states;
- `replaceable_tag` for eligible host rock; and
- optional `biome_tag` for geographic restriction.

See
`src/main/resources/data/computerised_oregen/worldgen/configured_feature/development_iron_motherlode.json`
for a complete configuration.

## Development launches

- `run-client.ps1` launches the Forge development client.
- `debug-client.ps1` launches it suspended and listening for a debugger on
  `localhost:5005`.
- Open `../../Computerised-OreGen.code-workspace` in VS Code to use the
  generated Forge launch configurations. Select `Minecraft Client (Run or
  Debug)` in Run and Debug; use **F5** to debug or **Ctrl+F5** to run normally.

## Attribution

Derived from CustomOreGen, originally by JRoush and revived by lawremi and its
contributors. CustomOreGen is distributed under the Artistic License 2.0. All
modern-port changes must remain documented and the corresponding source must be
available with distributed builds.

## Verification

On 2026-08-29 the project compiled under Java 17 and Forge 47.4.23. A clean
dedicated development server decoded the configured and placed features, loaded
the biome modifier, generated its spawn region, and reached `Done` without mod,
registry, codec, or world-generation errors.
