# Recovered Original Geology

Source inspected read-only on 2026-08-29:

`D:\CurseForge\Instances\Computerised\config\CustomOreGen\modules\custom\Vanilla.xml`

The shared presets are defined in:

`D:\CurseForge\Instances\Computerised\config\CustomOreGen\CustomOreGen_Config.xml`

This is the authoritative recovered design intent. Registry names and biome
names are from Minecraft 1.10.2 and must be translated rather than copied into
the rebuild.

## Global replacement

The original generator first replaced naturally generated coal, diamond,
emerald, gold, iron, lapis, and redstone with stone. It also replaced granite,
diorite, and andesite. Nether quartz was replaced with netherrack in the
Nether. The resources were then regenerated using custom distributions.

This means the original design did **not** leave coal, redstone, lapis, or
quartz on vanilla generation. Their exemption from Create drill protection is
a mining rule, not a world-generation rule.

## Rare metal motherlodes

Most metals inherited `PresetRareMotherloads`:

- Frequency: `0.4 * 0.0065 * oreFreq`
- Motherlode size: average `100 * 2.3 * oreSize`, range
  `25 * 0.5 * oreSize`
- Height: average 26, range 10, normal distribution
- Range limit: average 16, range 8
- Branches: average 3, range 2
- Branch inclination: average 0, range 0.55
- Branch length: average 0, range 60
- Branch height limit: 16
- Fork frequency: 0.20
- Segment length: average 15, range 6
- Segment radius: average `0.5 * oreSize`, range `0.3 * oreSize`
- Ore density: 0.25
- Ore radius multiplier: average 0.5, range 0.1

At default multipliers, these were enormous but very rare deposits centred
around Y 26, with several irregular branches.

| Deposit | Biome constraint | Composition |
|---|---|---|
| Iron | Plains or Taiga | 70% poor iron, 20% poor nickel, 10% normal iron |
| Gold | Mesa or Badlands | 98% poor gold, 2% normal gold |
| Copper | Forest | 95% poor copper, 5% normal copper |
| Tin | Taiga | 95% poor tin, 5% normal tin |
| Lead | Frozen or Ice | 95% poor lead, 5% normal lead |
| Silver | Deep Ocean | 95% poor silver, 5% normal silver |
| Zinc | Swampland | 95% poor zinc, 5% normal zinc |
| Aluminium | Extreme Hills | 95% custom poor aluminium, 5% normal aluminium |
| Platinum | Mushroom biomes | 95% custom poor platinum, 5% normal platinum |

Platinum used the same family but multiplied frequency by 10 and size by 0.1,
making it more frequent and much smaller than the other motherlodes.

## Other resource shapes

### Redstone

Redstone used thick vertical veins across all biomes, centred around Y 8 with
a range of 8. Its frequency was 2.406 times the vertical-vein preset and its
segment radius was doubled.

### Lapis

Lapis used vertical veins across all biomes, fixed around Y 16. Its frequency
was 1.787 times the vertical-vein preset and its segment radius was multiplied
by 1.5.

### Diamond

Diamond used rare pipe-like deposits across all biomes around Y 8. The diamond
core was five times the normal pipe motherlode size and 0.3 times its frequency.
It was deliberately surrounded and extended by coal:

- A dense coal cover around the diamond body
- Ten long coal branches averaging 86 blocks
- A solid coal-block component at the core

Coal was therefore a geological indicator leading prospectors toward diamond.

### Emerald

Emerald used short, sparse pipe veins restricted to mountain biomes, centred
around Y 16 with a range of 12.

### Nether quartz

Nether quartz used rare strategic clouds rather than vanilla clusters. These
were huge, irregular, lightly filled regions spanning multiple chunks, with
radius and thickness scaled by 1.707 and frequency by 2.914. Small hint veins
were attached to help players find the larger cloud.

## Rebuild implications

- Reproduce distribution roles and proportions, not obsolete block IDs.
- The initial rebuild metal list was incomplete: the original also designed
  deposits for tin, lead, silver, aluminium, nickel (inside iron), and platinum.
- A poor-ore block or equivalent low-yield processing mechanic was central to
  the metal motherlodes.
- Keep coal, redstone, lapis, and quartz breakable by Create drills, while still
  restoring their custom geological shapes.
- Translate old biome-name regular expressions to modern biome tags.
