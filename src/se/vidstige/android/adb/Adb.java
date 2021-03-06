package se.vidstige.android.adb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Adb {
	
	private AdbDevice device;

	public Adb() { this(AdbDevice.any()); }
	
	public Adb(AdbDevice device)
	{
		this.device = device;		
	}
	
	public List<AdbDevice> getDevices() throws AdbException
	{
		try
		{
			InputStream adbOutput = sendCommand(true, "devices");
			BufferedReader input = new BufferedReader(new InputStreamReader(adbOutput));
			return parseDevices(input);
		}
		catch (IOException e)
		{
			throw new AdbException("Could not list adb devices", e);			
		}
	}
	
	private List<AdbDevice> parseDevices(BufferedReader input) throws IOException
	{
		List<AdbDevice> devices = new ArrayList<AdbDevice>(2);
		String line;
		while ((line = input.readLine()) != null) {
			String[] parts = line.split("\t");
			if (parts.length >= 2)
			{
				devices.add(new AdbDevice(parts[0]));
			}
		}
		return devices;
	}
	
	public void sendCommand(String ... arguments) throws AdbException 
	{
		sendCommand(true, Arrays.asList(arguments));
	}
	
	public InputStream sendCommand(boolean parseErrors, String ... arguments) throws AdbException
	{
		return sendCommand(parseErrors, Arrays.asList(arguments));
	}
	
	public InputStream sendCommand(boolean parserErrors, List<String> arguments) throws AdbException
	{
		if (arguments == null) throw new IllegalArgumentException("arguments cannot be null");
		
		String android_home = System.getenv("ANDROID_HOME");
		if (android_home == null) throw new AdbException("Environment variable ANDROID_HOME not set");
		
		try
		{
			List<String> command_and_arguments = new ArrayList<String>(arguments);
			command_and_arguments.add(0, android_home + "/platform-tools/adb");
			if (device != AdbDevice.any())
			{
				command_and_arguments.add(1, "-s");
				command_and_arguments.add(2, device.getSerial());
			}
			ProcessBuilder processBuilder = new ProcessBuilder(command_and_arguments);
						
			Process p = processBuilder.start();
			if (parserErrors) {
				parseErrors(new BufferedReader(new InputStreamReader(p.getErrorStream())));
			}
			return p.getInputStream();
		}
		catch (IOException e)
		{
			throw new AdbException("Could not send adb command", e);
		}
	}

	private void parseErrors(BufferedReader input) throws AdbException, IOException {
		String line;
		while ((line = input.readLine()) != null)			
		{
			throw new AdbException(line);
		}
	}

	public void push(File localFile, String remotePath) throws AdbException {
		sendCommand(false, "push", localFile.getPath(), remotePath);
	}
}
