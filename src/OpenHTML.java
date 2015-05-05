import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import com.sqakrljabodetabek.modules.CommonHelper;


public class OpenHTML {

	public static void main(String args[])
	{
		/*try {
			try {
				System.out.println(OpenHTML.class.getResource("/com/sqakrljabodetabek/map/index.html").getPath());
				Desktop.getDesktop().browse(OpenHTML.class.getResource("/com/sqakrljabodetabek/map/index.html").toURI());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		CommonHelper.writeToFile("Halo!", "/com/sqakrljabodetabek/map", "test.txt");
	}
}
