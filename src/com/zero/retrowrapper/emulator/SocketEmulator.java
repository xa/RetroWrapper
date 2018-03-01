package com.zero.retrowrapper.emulator;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.zero.retrowrapper.emulator.registry.IHandler;

public class SocketEmulator
{
	private Socket socket;
	
	public SocketEmulator(Socket socket)
	{
		this.socket = socket;
	}
	
	public void parseIncoming() throws IOException
	{	   	
	   	InputStream is = socket.getInputStream();
	   	OutputStream os = socket.getOutputStream();
	   	
		DataInputStream dis = new DataInputStream(is);
	   	int length = -1;
	   	
	   	String get = "";
	   	
	   	int limit = 0;
	   	
	   	while(limit < 20)
	   	{			   		
	   		String line = ByteUtils.readLine(dis).trim();
	   		if(limit == 0)
	   		{
	   			get = line.split(" ")[1];
	   		}
	   		else if(line.startsWith("Content-Length: "))
	   		{
	   			length = Integer.valueOf(line.replace("Content-Length: ", ""));
	   		}
	   		else if(line.length() < 2)
	   		{
	   			break;
	   		}
	   		
	   		limit++;
	   	}
	   	
	   	byte[] data = null;
	   	
	   	if(length != -1)
	   	{
	   		data = new byte[length];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();						   	

			byte[] buffer = new byte[8192];
			int read = 0;
			int buffered = 0;
			while(read < length)
			{
				int readd = is.read(buffer, 0, Math.min(length-8192, buffer.length));
				read += readd;
				bos.write(buffer, 0, readd);
				buffered += read;
				
				if(buffered > 1024 * 1024)
				{
					bos.flush();
					buffered = 0;
				}
			}

			data = bos.toByteArray();
	   	}	   		   	
	   	
	   	IHandler handler = RetroEmulator.getInstance().getRegistry().getHandlerByUrl(get);
	   	
	   	if(handler != null)
	   	{
	   		try
	   		{
	   			System.out.println("Request: "+get);
	   			handler.sendHeaders(os);
	   			handler.handle(os, get, data);
	   		}catch(Exception e)
	   		{
		   		System.out.println("***************************************");
		   		System.out.println("   Exception in handling URL: "+get);
		   		System.out.println("***************************************");
	   			e.printStackTrace();
	   		}
	   	}else
	   	{
	   		System.out.println("***************************************");
	   		System.out.println("   No handler for URL: "+get);
	   		System.out.println("***************************************");
	   	}
	   	
	   	os.flush();
	   	socket.close();
	}
}
