package parquimetros;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InicioSesion extends JFrame {

	private JPanel contentPane;
	private  VentanaAdmin ventanaAdmin;
	private Gui_LoginInspector ventanaInspector;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InicioSesion frame = new InicioSesion();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public InicioSesion() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.add(contentPane);
		contentPane.setLayout(null);
		JLabel TituloLabel = new JLabel("Seleccione Usuario");
		TituloLabel.setBounds(178, 60, 118, 32);
		contentPane.add(TituloLabel);
		
		JButton btnAdmin = new JButton("Admin");
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ventanaAdmin=new VentanaAdmin();
				cerrarVentana();
				ventanaAdmin.setVisible(true);
			}
		});
		btnAdmin.setBounds(56, 175, 129, 44);
		contentPane.add(btnAdmin);
		
		JButton btnInspector = new JButton("Inspector");
		btnInspector.setBounds(291, 175, 118, 44);
		btnInspector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ventanaInspector=new Gui_LoginInspector();
				cerrarVentana();
				ventanaInspector.setVisible(true);
			}
		});
		contentPane.add(btnInspector);
	}
	private void cerrarVentana() {
		this.dispose();
	}
}
