package com.zero.retrowrapper.emulator.register.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.zero.retrowrapper.emulator.register.IHandler;
import com.zero.retrowrapper.emulator.register.EmulatorHandler;

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
