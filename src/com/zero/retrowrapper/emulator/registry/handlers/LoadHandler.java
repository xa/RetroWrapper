package com.zero.retrowrapper.emulator.registry.handlers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.zero.retrowrapper.emulator.ByteUtils;
import com.zero.retrowrapper.emulator.RetroEmulator;
import com.zero.retrowrapper.emulator.registry.EmulatorHandler;
import com.zero.retrowrapper.emulator.registry.IHandler;

public class LoadHandler extends EmulatorHandler implements IHandler
{
	public LoadHandler()
	{
		super("/level/load.html?id=");
	}

	@Override
	public void handle(OutputStream os, String get, byte[] data) throws IOException
	{
		String id = get.replace("/level/load.html?id=", "").split("&")[0];
		try(FileInputStream fis = new FileInputStream(new File(RetroEmulator.getInstance().getMapsDirectory(), "map" + id + ".mclevel")))
		{
			byte[] bytes = ByteUtils.readFully(fis);
			DataOutputStream dis = new DataOutputStream(os);
			dis.writeUTF("ok");
			dis.write(bytes);
		}
	}
}
