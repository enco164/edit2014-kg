package com.comtrade.device;

import android.util.Log;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.EquipmentData;
import com.dataart.android.devicehive.device.CommandResult;
import com.dataart.android.devicehive.device.Equipment;

public class TestEquipment extends Equipment {

	private static final String TAG = "TestEquipment";
	

	public TestEquipment() {
		super(equipmentData());
	}

	public TestEquipment(EquipmentData equipmentData){
		super(equipmentData);
	}
	
	private static EquipmentData equipmentData() {
		return new EquipmentData("Location",
				"Location Code", "Current Location of device");
		
	}

	
	@Override
	public void onBeforeRunCommand(Command command) {
		Log.d(TAG, "onBeforeRunCommand: " + command.getCommand());
	}

	@Override
	public boolean shouldRunCommandAsynchronously(final Command command) {
		return false;
	}

	@Override
	public CommandResult runCommand(final Command command) {
		Log.d(TAG, "runCommand: " + command.getCommand());
		
		// run command
		
		return new CommandResult(CommandResult.STATUS_COMLETED,
				"Executed on Android test equipment!");
	}

	@Override
	protected boolean onRegisterEquipment() {
		Log.d(TAG, "onRegisterEquipment");
		return true;
	}

	@Override
	protected boolean onUnregisterEquipment() {
		Log.d(TAG, "onUnregisterEquipment");
		return true;
	}

	@Override
	protected void onStartProcessingCommands() {
		Log.d(TAG, "onStartProcessingCommands");
	}

	@Override
	protected void onStopProcessingCommands() {
		Log.d(TAG, "onStopProcessingCommands");
	}

}
