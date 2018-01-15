package org.usfirst.frc.team1317.robot;
import java.io.*;
import java.net.*;
import java.util.*;

import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSONObjectException;

//This class is the class that was used last year to retrieve messages from the Raspberry Pi.
//The Raspberry Pi processed images from the camera and sent information to the RoboRIO
class PacketReader {
	
	DatagramSocket serverSocket;
	byte[] rData;
	
	PacketReader()
	{
		rData = new byte[1024];
	}
	
	public void InitializeSocket()
	{
		try {
			serverSocket = new DatagramSocket(5800);
			serverSocket.setSoTimeout(1);
		} catch (SocketException e) {
			serverSocket = null;
			e.printStackTrace();
		}
	}
	
	public Map<String,Object> getPacket()
	{
		if(serverSocket == null)
		{
			InitializeSocket();
		}
		if(serverSocket != null)
		{
			Map<String, Object> c=null;
			try
			{
				DatagramPacket rPacket = new DatagramPacket(rData,rData.length);
				serverSocket.receive(rPacket);
				String msg = new String( rPacket.getData() );
				// System.out.println("Got: " + msg);
				c = JSON.std.mapFrom(msg);
				System.out.println(c);
			}
			catch (SocketTimeoutException e){/*System.out.println("Did not receive packet within time period");*/}
			catch (JSONObjectException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return c;
		}
		else return null;
	}
  }
