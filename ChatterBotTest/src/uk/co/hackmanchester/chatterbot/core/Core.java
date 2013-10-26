package uk.co.hackmanchester.chatterbot.core;

import java.util.ArrayList;
import java.util.List;

import com.google.code.chatterbotapi.ChatterBotSession;

public class Core {
	private List threadPool;
	private int maxThreads;
	
	public Core(int maxThreads) {
		threadPool = new ArrayList<Worker>();

		this.maxThreads = maxThreads;
		
	}
	
	//Start it all
	public void messageToBot(long mobileNumber, String msg) {
		System.out.println("Rec'd from " + mobileNumber + " ---> " + msg);
		
		Worker worker = initThread(mobileNumber, msg);
		worker.run();
	}
	
	//This could potentially fuck everything up :)
	private Worker initThread(long mobileNumber, String message) {
		Worker retVal = null;
		
		if(threadPool.size() < maxThreads)
			retVal = new Worker(mobileNumber, message);
		else {
			try {
				wait(1000);
			} 
			catch (InterruptedException e) { //Should never be interrupted
				e.printStackTrace();
			}
			retVal = initThread(mobileNumber, message);
		}
		
		return retVal;
	}

	private class Worker extends Thread {
		long mobileNumber;
		String message;
		String response = "No response yet";
		
		public Worker(long mobileNumber, String message) {
			this.mobileNumber = mobileNumber;
			this.message = message;
		}
		
		@Override
		public void run() {
			try {
				ChatterBotSession sesh = SessionPool.getSession(mobileNumber);
				response = sesh.think(message);
				replyToSender();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			
			threadPool.remove(this);
		}
		
		private void replyToSender() {
			//TODO: Trigger reply message here
			System.out.println("Sending to " + mobileNumber + " ---> " + response);
		}
	}
}
