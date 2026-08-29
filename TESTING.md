# Prototype Testing

Last updated: 2026-08-28

## Automated verification

Environment:

- Minecraft 1.20.1 dedicated server
- Forge 47.4.23
- Java 17.0.17 (Eclipse Temurin)
- Create 6.0.8
- CC:Tweaked 1.120.2
- Paxi 4.0
- Ponder 1.0.91
- YUNG's API 4.0.6
- Chop Down Updated 1.4.0

Result: **pass**.

The server completed mod loading and world creation. Paxi reported
`computerised_drill_rules (paxi)` in the enabled datapack list.

Live `execute if block ... #create:non_breakable` checks passed for:

- Iron and deepslate iron ore
- Copper and deepslate copper ore
- Zinc and deepslate zinc ore
- Gold and deepslate gold ore
- Diamond and deepslate diamond ore
- Emerald and deepslate emerald ore

Live inverse checks confirmed these blocks are not protected:

- Stone
- Deepslate
- Coal ore
- Redstone ore
- Lapis ore
- Nether quartz ore

Create 6.0.8 bytecode was also inspected. Both stationary kinetic breakers and
contraption-mounted block breakers delegate to
`BlockBreakingKineticBlockEntity.isBreakable`, which returns false when a block
matches Create's `NON_BREAKABLE` block tag.

## Packaging verification

Result: **pass**.

- `packwiz refresh` completed successfully.
- `packwiz list` reported all ten required entries, including client-side JEI,
  Chop Down Updated, and Dynamic Ore Veins.
- A Modrinth package exported successfully to
  `build/computerised-rebuilt-0.1.0-prototype.mrpack`.
- Export SHA-256:
  `a050c3fa04e1630e930038bdd037ec2059915d4c37d1263de0d430499c4479d2`.

Chop Down Updated 1.4.0 also passed a dedicated-server startup test alongside
the prototype stack. Forge generated its common configuration successfully and
the server reached `Done` without mod-loading errors.

The operator subsequently confirmed that Chop Down Updated works in the
graphical client and in-world gameplay.

## Geology prototype

Status: **server validation passed; fresh-world balancing pending**.

- Dynamic Ore Veins 1.0.0 loaded successfully on Forge 47.4.23.
- Paxi enabled `computerised_geology` automatically.
- Dynamic Ore Veins parsed all nine custom Overworld vein definitions.
- The server reached `Done` without registry or datapack errors.
- All 21 geology JSON resources passed local JSON parsing.
- Visual distribution, rarity, size, and yield still require a new-world test.

See `GEOLOGY_TESTING.md` for the operator procedure.

The export is generated output and is not tracked by Git.

## Interactive acceptance test

Status: **accepted for Prototype 1**.

Operator results reported on 2026-08-28:

- **Pass:** the Create drill broke ordinary blocks.
- **Pass:** the Create drill broke coal, lapis, and redstone ore.
- **Pass:** protected ores stopped or resisted the Create drill.
- **Pass:** a Mining Turtle could break every tested protected ore.
- **Pass:** a player could break every tested protected ore.
- **Deferred by operator:** Nether quartz behaviour.
- **Deferred by operator:** whether the contraption resumes after removing a
  protected obstruction.

Original procedure:

1. Import the generated `.mrpack` into Prism Launcher or another compatible
   launcher.
2. Create a flat creative world.
3. Build a slow linear Create contraption with a Mechanical Drill and chest.
4. Put stone, deepslate, coal, redstone, lapis, and quartz in its path. Confirm
   they are broken and collected.
5. Repeat with iron, copper, zinc, gold, diamond, and emerald ores, including
   deepslate variants. Confirm the machine stops at each deposit.
6. Mine a protected block with a player pickaxe.
7. Put a Mining Turtle against a protected block and run `turtle.dig()`.
8. Remove the obstruction and confirm the contraption resumes.

The four core behavioural boundaries were observed: the drill stops, ordinary
and tested bulk blocks break, the player succeeds, and the turtle succeeds.
Prototype 1 is therefore accepted; the two deferred checks can be revisited if
later pack changes expose a related regression.

## Freight prototype

Status: **accepted for Prototype 2**.

Operator result reported on 2026-08-29:

- **Pass:** a Create train was assembled and operated successfully.
- **Pass:** cargo loaded through a Portable Storage Interface.
- **Pass:** the train travelled automatically using a repeating schedule.
- **Pass:** cargo unloaded into the Factory buffer.
- **Pass:** a CC:Tweaked turtle fed cargo into the Mine buffer.
- **Pass:** two trains operated safely using Create signals.

The complete physical freight boundary was observed: turtle-produced cargo was
loaded into train storage, moved by an automatic repeating schedule, unloaded
into the Factory buffer, and shared track safely with a second signalled train.
Prototype 2 is accepted.
