package com.zero.retrowrapper.emulator.registry.handlers;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.zero.retrowrapper.emulator.ByteUtils;
import com.zero.retrowrapper.emulator.registry.EmulatorHandler;
import com.zero.retrowrapper.emulator.registry.IHandler;

public class ResourcesHandlerBeta extends EmulatorHandler implements IHandler
{
	public ResourcesHandlerBeta()
	{
		super("/MinecraftResources/");
	}
	
	@Override
	public void handle(OutputStream os, String get, byte[] data) throws IOException
	{
		URL resourceURL = new URL("http://s3.amazonaws.com"+get);
		InputStream is = resourceURL.openStream();
		os.write(ByteUtils.readFully(is));
	}
}
