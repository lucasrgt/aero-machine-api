package aero.machineapi;

public interface Aero_IFluidHandler {
	int receiveFluid(int fluidType, int amountMB);
	int extractFluid(int fluidType, int amountMB);
	int getFluidType();
	int getFluidAmount();
	int getFluidCapacity();
}
