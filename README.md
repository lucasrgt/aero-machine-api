# AeroMachineAPI

Tech mod infrastructure for Minecraft Beta 1.7.3 — standardized interfaces for energy, fluids, gases, side configuration, and multiblock ports.

> **Compatibility:** Java 8 | Minecraft Beta 1.7.3 | RetroMCP | ModLoader / Forge 1.0.6

## Features

- **Energy system** — `Aero_IEnergyReceiver` for machines that consume RN (Retro Newtons)
- **Fluid system** — `Aero_IFluidHandler` with typed fluids (Water, Heavy Water) in millibuckets
- **Gas system** — `Aero_IGasHandler` with typed gases (Hydrogen, Oxygen, Ozone)
- **Side configuration** — Per-side, per-type IO modes (None/Input/Output/I+O) with `Aero_ISideConfigurable`
- **Item slot access** — `Aero_ISlotAccess` for external item insertion/extraction
- **Multiblock ports** — `Aero_IPort` interface + `Aero_PortRegistry` for port→controller linking

## Quick Start

### Energy receiver

```java
public class MyMachine extends TileEntity implements Aero_IEnergyReceiver {
    private int energy = 0;
    private static final int MAX = 1000;

    public int receiveEnergy(int amount) {
        int accepted = Math.min(amount, MAX - energy);
        energy += accepted;
        return accepted;
    }
    public int getStoredEnergy() { return energy; }
    public int getMaxEnergy() { return MAX; }
}
```

### Fluid handler

```java
public class MyTank extends TileEntity implements Aero_IFluidHandler {
    private int fluidType = Aero_FluidType.NONE;
    private int amount = 0;
    private static final int CAPACITY = 8000;

    public int receiveFluid(int type, int amountMB) {
        if (fluidType != Aero_FluidType.NONE && fluidType != type) return 0;
        int accepted = Math.min(amountMB, CAPACITY - amount);
        if (accepted > 0) { fluidType = type; amount += accepted; }
        return accepted;
    }

    public int extractFluid(int type, int amountMB) {
        if (fluidType != type) return 0;
        int extracted = Math.min(amountMB, amount);
        amount -= extracted;
        if (amount == 0) fluidType = Aero_FluidType.NONE;
        return extracted;
    }

    public int getFluidType() { return fluidType; }
    public int getFluidAmount() { return amount; }
    public int getFluidCapacity() { return CAPACITY; }
}
```

### Side configuration

```java
public class MyConfigurableMachine extends TileEntity implements Aero_ISideConfigurable {
    private int[] sideConfig = new int[24]; // 6 sides x 4 types

    {
        // Default: accept energy from all sides
        for (int s = 0; s < 6; s++)
            Aero_SideConfig.set(sideConfig, s, Aero_SideConfig.TYPE_ENERGY,
                Aero_SideConfig.MODE_INPUT);
    }

    public int[] getSideConfig() { return sideConfig; }

    public void setSideMode(int side, int type, int mode) {
        Aero_SideConfig.set(sideConfig, side, type, mode);
    }

    public boolean supportsType(int type) {
        return type == Aero_SideConfig.TYPE_ENERGY;
    }

    public int[] getAllowedModes(int type) {
        return new int[]{ Aero_SideConfig.MODE_NONE, Aero_SideConfig.MODE_INPUT };
    }
}
```

## Interfaces

| Interface | Purpose |
|-----------|---------|
| `Aero_IEnergyReceiver` | Accept energy (RN) from cables |
| `Aero_IFluidHandler` | Send/receive typed fluids via pipes |
| `Aero_IGasHandler` | Send/receive typed gases via pipes |
| `Aero_ISideConfigurable` | Per-side IO mode configuration |
| `Aero_ISlotAccess` | Expose insert/extract slot indices for automation |
| `Aero_IPort` | Multiblock port → controller linking |

## Types & Utilities

| Class | Purpose |
|-------|---------|
| `Aero_FluidType` | Fluid type constants: `NONE`, `WATER`, `HEAVY_WATER` |
| `Aero_GasType` | Gas type constants: `NONE`, `HYDROGEN`, `OXYGEN`, `OZONE` |
| `Aero_SideConfig` | Static helpers for side config array (get/set by side+type) |
| `Aero_PortRegistry` | Registry mapping IO types to port block IDs |

## Side Config Storage

Side config is a flat `int[24]` array: `index = side * 4 + type`

- **Sides**: 0=Bottom, 1=Top, 2=North, 3=South, 4=West, 5=East
- **Types**: `TYPE_ENERGY=0`, `TYPE_FLUID=1`, `TYPE_GAS=2`, `TYPE_ITEM=3`
- **Modes**: `MODE_NONE=0`, `MODE_INPUT=1`, `MODE_OUTPUT=2`, `MODE_INPUT_OUTPUT=3`

## Documentation

See [DOC.md](DOC.md) for the full API reference, architecture diagrams, and integration examples.

## Author

**lucasrgt** — [aerocoding.dev](https://aerocoding.dev)

## License

MIT
