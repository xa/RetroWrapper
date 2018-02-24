package com.zero.retrowrapper.emulator.registry.handlers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import com.zero.retrowrapper.emulator.RetroEmulator;
import com.zero.retrowrapper.emulator.registry.EmulatorHandler;
import com.zero.retrowrapper.emulator.registry.IHandler;

public class ListmapsHandler extends EmulatorHandler implements IHandler
{
	public ListmapsHandler()
	{
		super("/listmaps.jsp");
	}

	@Override
	public void handle(OutputStream os, String get, byte[] data) throws IOException
	{
		for(int i=0;i<5;i++)
		{
			File file = new File(RetroEmulator.getInstance().getMapsDirectory(), "map"+i+".txt");
			String name = "-;";
			
			if(file.exists())
			{
				name = new Scanner(file).nextLine()+";";
			}
			
			os.write(name.getBytes());
		}
	}
}
