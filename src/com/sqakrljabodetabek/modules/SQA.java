package com.sqakrljabodetabek.modules;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class SQA {

	private JFrame frmSistemTanyajawabLayanan;
	private JTextField textSistem;
	private JTextField textPengguna;
	private DialogueManager dm;
	private boolean isSpeaking = false;
	private ArrayList<String> recognizedUtterances;
	private final static String TEXT_MULAI_BICARA = "Tekan tombol 'Mulai bicara!' untuk memulai...";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SQA window = new SQA();
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
	public SQA() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSistemTanyajawabLayanan = new JFrame();
		frmSistemTanyajawabLayanan.setTitle("Sistem Tanya-Jawab Layanan KRL Jabodetabek");
		frmSistemTanyajawabLayanan.setResizable(false);
		frmSistemTanyajawabLayanan.setBounds(100, 100, 450, 130);
		frmSistemTanyajawabLayanan.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JButton btnNewButton_2 = new JButton("<=");
		final JButton btnNewButton_1 = new JButton("Submit");
		final JButton btnNewButton = new JButton("Mulai bicara!");
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isSpeaking = false;
				processUtterances();
				btnNewButton.setEnabled(true);
				btnNewButton_1.setEnabled(false);
				btnNewButton_2.setEnabled(false);
			}
		});
		
		btnNewButton.setBounds(0, 80, 269, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isSpeaking = true;
				textPengguna.setText("Mendengarkan...");
				clearTextSistem();
				captureUtterances();
				btnNewButton.setEnabled(false);
				btnNewButton_1.setEnabled(true);
				btnNewButton_2.setEnabled(true);
			}
		});
		frmSistemTanyajawabLayanan.getContentPane().setLayout(null);
		frmSistemTanyajawabLayanan.getContentPane().add(btnNewButton);
		
		
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setBounds(344, 80, 100, 23);
		frmSistemTanyajawabLayanan.getContentPane().add(btnNewButton_1);
		
		
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(recognizedUtterances.size() > 0)
				{
					recognizedUtterances.remove(recognizedUtterances.size() - 1);
					updateTextPengguna();
				}
			}
		});
		btnNewButton_2.setBounds(269, 80, 76, 23);
		frmSistemTanyajawabLayanan.getContentPane().add(btnNewButton_2);
		
		JTextField txtPengguna = new JTextField();
		txtPengguna.setText(TEXT_MULAI_BICARA);
		txtPengguna.setEditable(false);
		txtPengguna.setBackground(Color.WHITE);
		txtPengguna.setBounds(94, 13, 340, 23);
		frmSistemTanyajawabLayanan.getContentPane().add(txtPengguna);
		txtPengguna.setColumns(10);
		
		JLabel lblPengguna = new JLabel("Pengguna:");
		lblPengguna.setBounds(10, -1, 74, 50);
		frmSistemTanyajawabLayanan.getContentPane().add(lblPengguna);
		
		JLabel lblSistem = new JLabel("Sistem:");
		lblSistem.setBounds(10, 44, 74, 23);
		frmSistemTanyajawabLayanan.getContentPane().add(lblSistem);
		
		JTextField txtSistem = new JTextField();
		txtSistem.setEditable(false);
		txtSistem.setBackground(Color.WHITE);
		txtSistem.setBounds(94, 44, 340, 23);
		frmSistemTanyajawabLayanan.getContentPane().add(txtSistem);
		txtSistem.setColumns(10);
		
		textPengguna = txtPengguna;
		textSistem = txtSistem;
		
		setupComponents();
	}
	
	protected void processUtterances() {
		
		String submittedUtterance = convertUtterancesToString(recognizedUtterances);
		clearTextPengguna();
		updateTextSistem(submittedUtterance);
		recognizedUtterances = new ArrayList<>();
		System.out.println("submitted utterance: " + submittedUtterance);
		
	}

	private void updateTextSistem(String submittedUtterance) 
	{
		final String response = dm.executeDMBehavior(submittedUtterance);
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				textSistem.setText(response);
			}
		});
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
						recognizedUtterances.add(utterance);
					}
					
					System.out.println("Ini dari thread");
					
					if(isSpeaking) updateTextPengguna();
				}
			}
		};
		
		t.start();
	}

	private void setupComponents()
	{
		dm = new DialogueManager(false);
		recognizedUtterances = new ArrayList<>();
	}
	
	protected String convertUtterancesToString(ArrayList<String> recognizedUtterances) {
		StringBuilder ret = new StringBuilder();
		
		for (String utterance : recognizedUtterances) {
			ret.append(utterance + " ");
		}
		
		return ret.toString();
	}

	private void updateTextPengguna()
	{
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				textPengguna.setText(convertUtterancesToString(recognizedUtterances));
			}
		});
	}
	
	private void clearTextSistem()
	{
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				textSistem.setText("");
			}
		});
	}
	
	private void clearTextPengguna()
	{
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				textPengguna.setText("");
			}
		});
	}
}
