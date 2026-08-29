# Computerised Rebuilt

A modern reconstruction of the original **Computerised** Minecraft 1.10.2
modpack.

The pack is built around one physical logistics loop:

1. Find large, geographically constrained mineral deposits.
2. Extract metals using programmable CC:Tweaked turtles.
3. Use Create machinery for tunnelling and local processing.
4. Move bulk material between zones using scheduled Create freight trains.
5. Use Stargates for passengers, never for bulk freight.

The original 2018 instance is preserved separately and is not copied into this
repository.

## Initial target

- Minecraft 1.20.1
- Forge
- Create 6
- CC:Tweaked
- Packwiz-managed distribution

See [DESIGN.md](DESIGN.md) for the gameplay rules and prototype acceptance
criteria, and [ORIGINAL_GEOLOGY.md](ORIGINAL_GEOLOGY.md) for the recovered ore
distribution specification from the original pack.

## Build the prototype

On Windows PowerShell:

```powershell
.\scripts\build.ps1
```

The script bootstraps Go and Packwiz inside the ignored `.tools` directory,
refreshes the Packwiz index, and writes an importable Modrinth package to
`build/computerised-rebuilt-0.1.0-prototype.mrpack`.

See [TESTING.md](TESTING.md) for automated results and the remaining in-game
acceptance procedure.

The geology prototype must be tested in a fresh world. See
[GEOLOGY_TESTING.md](GEOLOGY_TESTING.md).
