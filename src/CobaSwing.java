import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.sqakrljabodetabek.modules.SpeechRecognizer;

public class CobaSwing extends JFrame 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SpeechRecognizer sr = new SpeechRecognizer(false);
	JTextArea textArea;
	int i = 0;

	public CobaSwing()
	{
		initUI();
		Thread th = new Thread() {
		      public void run() {
		        runQueries();
		      }
	    };
	    
	    th.start();
	}
	
	private void runQueries()
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				textArea.setText(sr.listen() + "drone" + i);
				i++;
			}
		});
		
	}
	
	private void initUI()
	{
		JButton quitButton = new JButton("Quit");
		textArea = new JTextArea();

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        createLayout(textArea);

        setTitle("Quit button");

		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
    private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );
    }
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				CobaSwing cb = new CobaSwing();
				cb.setVisible(true);
				
			}
		});
	}
	
}
