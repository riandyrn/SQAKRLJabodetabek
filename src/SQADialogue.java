import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;

import com.sqakrljabodetabek.modules.DialogueManager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class SQADialogue {

	private JFrame frmSistemTanyajawabLayanan;
	private JTextField txtPengguna;
	private JTextArea txtDialog;
	private boolean isSpeaking = false;
	private DialogueManager dm;
	private ArrayList<String> recognizedUtterances;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SQADialogue window = new SQADialogue();
					window.frmSistemTanyajawabLayanan.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SQADialogue() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSistemTanyajawabLayanan = new JFrame();
		frmSistemTanyajawabLayanan.setResizable(false);
		frmSistemTanyajawabLayanan.setTitle("Sistem Tanya-Jawab Layanan KRL Jabodetabek");
		frmSistemTanyajawabLayanan.setBounds(100, 100, 483, 295);
		frmSistemTanyajawabLayanan.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSistemTanyajawabLayanan.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		frmSistemTanyajawabLayanan.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		txtDialog = new JTextArea();
		txtDialog.setLineWrap(true);
		txtDialog.setColumns(10);
		txtDialog.setText("> ");
		txtDialog.setEditable(false);
		scrollPane.setViewportView(txtDialog);
		
		JPanel panel = new JPanel();
		frmSistemTanyajawabLayanan.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		txtPengguna = new JTextField();
		txtPengguna.setText("Tekan tombol rekam untuk memulai...");
		txtPengguna.setBackground(Color.WHITE);
		txtPengguna.setEditable(false);
		panel_1.add(txtPengguna);
		txtPengguna.setColumns(10);
		
		String record_image = "<html><img src='" + SQADialogue.class.getResource("/com/sqakrljabodetabek/images/record.png") + "'></html>";
		String clear_image = "<html><img src='" + SQADialogue.class.getResource("/com/sqakrljabodetabek/images/clear.png") + "'></html>";		
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		final JButton btnRekam = new JButton(record_image);
		final JButton btnKirim = new JButton("Submit");
		btnKirim.setEnabled(false);
		final JButton btnUlangi = new JButton(clear_image);
		btnUlangi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(recognizedUtterances.size() > 0) recognizedUtterances.remove(recognizedUtterances.size() - 1);
				updateTxtPengguna();
			}
		});
		btnUlangi.setEnabled(false);
		
		btnRekam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnKirim.setEnabled(true);
				btnUlangi.setEnabled(true);
				btnRekam.setEnabled(false);
				txtPengguna.setText("Mendengarkan...");
				isSpeaking = true;
				captureUtterances();
			}
		});
		
		btnKirim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				processUtterances();
				btnKirim.setEnabled(false);
				btnUlangi.setEnabled(false);
				btnRekam.setEnabled(true);
				isSpeaking = false;
				txtPengguna.setText("Tekan tombol rekam untuk memulai...");
			}
		});
		
		panel_3.add(btnRekam);
		panel_3.add(btnUlangi);
	
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		
		panel_2.add(btnKirim);
		
		setupComponents();
	}
	
	protected void captureUtterances() 
	{
		Thread t = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				
				while(isSpeaking)
				{
					String utterance = dm.listenUtterance();
					
					if(!utterance.isEmpty())
					{
						System.out.println(utterance);
						recognizedUtterances.add(utterance);
					}
					
					System.out.println("Ini dari thread");
					
					if(isSpeaking) updateTxtPengguna();
				}
			}
		};
		
		t.start();
	}
	
	private void updateTxtPengguna() 
	{
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txtPengguna.setText(convertUtterancesToString(recognizedUtterances));
			}
		});
	}

	private void setupComponents()
	{
		dm = new DialogueManager(false);
		recognizedUtterances = new ArrayList<>();
	}
	
	private String convertUtterancesToString(ArrayList<String> recognizedUtterances) 
	{
		StringBuilder ret = new StringBuilder();
		
		for (String utterance : recognizedUtterances) {
			ret.append(utterance + " ");
		}
		
		return ret.toString();
	}
	
	private void processUtterances() 
	{
		String submittedUtterance = convertUtterancesToString(recognizedUtterances);
		updateTxtDialog(submittedUtterance);
		recognizedUtterances = new ArrayList<>();
		System.out.println("submitted utterance: " + submittedUtterance);
		
	}

	private void updateTxtDialog(final String submittedUtterance) 
	{
		final String response = dm.executeDMBehavior(submittedUtterance);
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txtDialog.append(submittedUtterance + "\n");
				txtDialog.append(response + "\n\n> ");
			}
		});
		
	}
}
