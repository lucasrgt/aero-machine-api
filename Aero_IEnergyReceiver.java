package aero.machineapi;

public interface Aero_IEnergyReceiver {
	int receiveEnergy(int amount);
	int getStoredEnergy();
	int getMaxEnergy();
}
