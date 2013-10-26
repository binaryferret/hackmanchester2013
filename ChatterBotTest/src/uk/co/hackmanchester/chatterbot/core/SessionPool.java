package uk.co.hackmanchester.chatterbot.core;

import java.util.concurrent.ConcurrentHashMap;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;

public final class SessionPool {

	private static SessionPool sPool = new SessionPool();
	private static ChatterBotFactory factory = new ChatterBotFactory();
	
	public static ChatterBotSession getSession(long mobileNumber) throws Exception {
		ChatterBotSession retVal = null;
		
		if(sPool.dataMap.containsKey(new Long(mobileNumber)))
			retVal = sPool.dataMap.get(new Long(mobileNumber));
		else {
			sPool.createSesh(mobileNumber);
			retVal = sPool.dataMap.get(new Long(mobileNumber));
		}
		
		
		return retVal;
	}

	
	
	private ConcurrentHashMap<Long, ChatterBotSession> dataMap;
	
	private SessionPool() {
		dataMap = new ConcurrentHashMap<Long, ChatterBotSession>();
	}
	
	private ChatterBotSession createSesh(long mobileNumber) throws Exception {
        ChatterBot bot = factory.create(ChatterBotType.CLEVERBOT);
        
        return dataMap.put(new Long(mobileNumber), bot.createSession());
	}
}
