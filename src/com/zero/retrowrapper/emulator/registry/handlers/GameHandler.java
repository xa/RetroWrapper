package com.zero.retrowrapper.emulator.registry.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.zero.retrowrapper.emulator.registry.EmulatorHandler;
import com.zero.retrowrapper.emulator.registry.IHandler;

public class GameHandler extends EmulatorHandler implements IHandler
{
	public GameHandler()
	{
		super("/game/");
	}

	@Override
	public void handle(OutputStream os, String get, byte[] data) throws IOException
	{
		os.write("0".getBytes());
	}
}
