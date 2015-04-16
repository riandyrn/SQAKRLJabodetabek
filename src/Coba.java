
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

	}

}
