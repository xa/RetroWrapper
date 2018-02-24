package com.zero.retrowrapper.emulator.registry.handlers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.zero.retrowrapper.emulator.ByteUtils;
import com.zero.retrowrapper.emulator.RetroEmulator;
import com.zero.retrowrapper.emulator.registry.EmulatorHandler;
import com.zero.retrowrapper.emulator.registry.IHandler;

public class SaveHandler extends EmulatorHandler implements IHandler
{
	public SaveHandler()
	{
		super("/level/save.html");
	}

	@Override
	@SuppressWarnings("unused")
	public void handle(OutputStream os, String get, byte[] data) throws IOException
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

   		String username = ByteUtils.readString(dis);
   		String token = ByteUtils.readString(dis);
   		String levelName = ByteUtils.readString(dis);
   		byte id = dis.readByte();
   		   		
   		int levelLength = dis.readInt();
   		System.out.println(levelLength+";"+data.length);
   		byte[] level = new byte[levelLength];
   		dis.readFully(level);
   		
   		os.write("ok\n".getBytes());
		dis.close();
		
		File fileMap = new File(RetroEmulator.getInstance().getMapsDirectory(), "map" + id + ".mclevel");
		File fileMapMeta = new File(RetroEmulator.getInstance().getMapsDirectory(), "map" + id + ".txt");
		
		try(FileOutputStream fos = new FileOutputStream(fileMap))
		{
			fos.write(level);
			fos.close();
		}
		
		try(FileOutputStream fos = new FileOutputStream(fileMapMeta))
		{
			fos.write(levelName.getBytes());
			fos.close();
		}
	}
}
