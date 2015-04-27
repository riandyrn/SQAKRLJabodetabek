import java.sql.Time;
import java.util.Random;


public class Coba {
	
	public String content;
	
	public static void main(String[] args) {
		
		Coba a = new Coba();
		a.content = "Hello!";
		
		System.out.println(a.content);
		
		Coba b = a;
		
		System.out.println(b.content);
		
		b.content = "drone";
		
		System.out.println(a.content);
		
		Random rand = new Random();
		long milisecond = rand.nextLong();
		Time date = new Time(milisecond);
		System.out.println(milisecond);
		System.out.println(date.toString());

	}

}
