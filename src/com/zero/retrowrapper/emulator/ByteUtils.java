package com.zero.retrowrapper.emulator;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteUtils
{
	public static String readString(DataInputStream dis) throws IOException
	{
		int len = dis.readUnsignedShort();
		System.out.println(len);
		byte[] bytes = new byte[len];
		dis.read(bytes);
		return new String(bytes);
	}
	
	public static byte[] readFully(InputStream is) throws IOException
	{		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();						   	

		byte[] buffer = new byte[8192];
		int read = 0;
		int buffered = 0;
		while((read = is.read(buffer)) > -1)
		{
			bos.write(buffer, 0, read);
			buffered += read;
			
			if(buffered > 1024 * 1024)
			{
				bos.flush();
				buffered = 0;
			}
		}

		return bos.toByteArray();
	}

	public static String readLine(DataInputStream dis) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		while (true)
		{
			int b = dis.read();
			if (b == 0x0a)
			{
				break;
			}
			bos.write(b);
		}
		
		return new String(bos.toByteArray());
	}
}
