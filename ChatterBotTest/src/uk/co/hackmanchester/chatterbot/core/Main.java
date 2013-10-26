package uk.co.hackmanchester.chatterbot.core;
import com.google.code.chatterbotapi.*;

public class Main {
	
	public static void main(String... args) throws Exception {

		Core c = new Core(5);

		c.messageToBot(4432432532L, "Bonjure");
		
		c.messageToBot(447527156515L, "Hello there");
		
		c.messageToBot(4432432532L, "dammit");
		
		c.messageToBot(447527156515L, "My name is Hal, what is yours?");
		
		c.messageToBot(4432432532L, "i'm english");
		
		c.messageToBot(447527156515L, "Actually, my name isn't Hal is Sally");
		
		c.messageToBot(4432432532L, "what are you");
	}

//	public static void main(String... args) throws Exception {
//		ChatterBotFactory factory = new ChatterBotFactory();
//
//        ChatterBot bot1 = factory.create(ChatterBotType.CLEVERBOT);
//        ChatterBotSession bot1session = bot1.createSession();
//
//        ChatterBot bot2 = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
//        ChatterBotSession bot2session = bot2.createSession();
//        
//
//        String s = "Hi";
//        while (true) {
//
//            System.out.println("bot1> " + s);
//
//            s = bot2session.think(s);
//            System.out.println("bot2> " + s);
//
//            s = bot1session.think(s);
//        }
//	}
}