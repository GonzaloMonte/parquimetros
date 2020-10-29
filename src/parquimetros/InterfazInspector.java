package parquimetros;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.List;

public class InterfazInspector extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazInspector frame = new InterfazInspector();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InterfazInspector() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAgregarPatente = new JButton("Agregar Patente");
		btnAgregarPatente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//HAY QUE AGREGAR UN AVISO PARA INSERTAR PATENTE Y EN ELLA VERIFICAR QUE ESTE TODO CORRECTO
			}
		});
		btnAgregarPatente.setBounds(5, 5, 424, 23);
		contentPane.add(btnAgregarPatente);
		//ESTE COMBO BOX TENDRA LAS UBICACIONES QUE TIENE ACCESO SEGUN LEGAJO.
		JComboBox UbicacionesBox = new JComboBox();
		UbicacionesBox.setBounds(10, 39, 263, 211);
		contentPane.add(UbicacionesBox);
		
		List ListaMultas = new List();
		ListaMultas.setBounds(292, 46, 132, 205);
		contentPane.add(ListaMultas);
	}
}
