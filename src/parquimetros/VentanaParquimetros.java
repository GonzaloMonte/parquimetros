package parquimetros;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTable;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class VentanaParquimetros extends JFrame {

	private JPanel contentPane;
	private JTable table;
	protected Connection conexionBD;
	String calleSeleccionada;
	String alturaSeleccionada;
	String id="";

	
	public VentanaParquimetros(Connection cnx) {
		conexionBD=cnx;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 624, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		table = new JTable();
		table.setBounds(310, 30, 277, 361);
		contentPane.add(table);
		
		Button buttonVolver = new Button("Volver");
		buttonVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gui_LoginParquimetro GuiLogin=new Gui_LoginParquimetro();
				cerrarVentana();
				GuiLogin.setVisible(true);
			}
			
		});
		buttonVolver.setBounds(108, 369, 70, 22);
		contentPane.add(buttonVolver);
		
		JComboBox BoxParquimetros = new JComboBox();
		BoxParquimetros.setBounds(10, 158, 268, 34);
		contentPane.add(BoxParquimetros);
		
		
		JComboBox BoxTarjeta = new JComboBox();
		BoxTarjeta.setBounds(10, 241, 267, 34);
		contentPane.add(BoxTarjeta);
		
		
		JComboBox BoxUbicaciones = new JComboBox();
		BoxUbicaciones.setBounds(10, 82, 268, 34);
		contentPane.add(BoxUbicaciones);
		BoxUbicaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BoxParquimetros.removeAllItems();
				calleSeleccionada= Filtrarcalle(BoxUbicaciones.getSelectedItem().toString());
				alturaSeleccionada=Filtraraltura(BoxUbicaciones.getSelectedItem().toString());
				AgregarParquimetros(BoxParquimetros,calleSeleccionada,alturaSeleccionada);
				BoxParquimetros.setSelectedItem(null);
				
			}
		});
		
		AgregarUbicaciones(BoxUbicaciones);
		AgregarTarjetas(BoxTarjeta);
		
		
		JLabel lblSeleccionarUbicacion = new JLabel("Seleccionar Ubicacion ");
		lblSeleccionarUbicacion.setBounds(71, 57, 168, 14);
		contentPane.add(lblSeleccionarUbicacion);
		
		JLabel lblSeleccionarParquimetro = new JLabel("Seleccionar Parquimetro");
		lblSeleccionarParquimetro.setBounds(71, 133, 143, 14);
		contentPane.add(lblSeleccionarParquimetro);
		
		JLabel lblSeleccionarTarjeta = new JLabel("Seleccionar ID tarjeta ");
		lblSeleccionarTarjeta.setBounds(71, 216, 128, 14);
		contentPane.add(lblSeleccionarTarjeta);
		
		JButton btnGenerarTabla = new JButton("Generar Tabla");
		btnGenerarTabla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnGenerarTabla.setBounds(85, 327, 125, 23);
		contentPane.add(btnGenerarTabla);
	}
	 private void cerrarVentana() {
	        this.dispose();
	    }
	 private void AgregarUbicaciones(JComboBox Box) {
			try {
				Statement st= this.conexionBD.createStatement();
				ResultSet rs= st.executeQuery("select calle,altura from ubicaciones");
				while (rs.next()) {
					String calle=rs.getString("calle");
					String altura=rs.getString("altura");
					Box.addItem(calle+' '+altura+'.');
				}
				st.close();
				rs.close();
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	 
	 
	 
	 protected String Filtrarcalle (String texto) {
			String calle = "";
	        if (texto == null) {
	        	JOptionPane.showMessageDialog(getContentPane(), "Seleccione un parquimetro", "Seleccione un parquimetro",
						JOptionPane.WARNING_MESSAGE);            return "";
	        }
	        else {
	            int puntero = 0;           
	            while (texto.charAt(puntero)!=(' ')) {
	                calle+=texto.charAt(puntero);
	                puntero++;
	            }
	           }
	        return calle;
	       }

		
		protected String Filtraraltura(String texto) {
			String numero = "";
	        if (texto == null) {
	        	JOptionPane.showMessageDialog(getContentPane(), "Seleccione un parquimetro", "Seleccione un parquimetro",
						JOptionPane.WARNING_MESSAGE);            return "";
	        }
	        else {
	        	int puntero=0;
	        	while(texto.charAt(puntero)!=(' ')){
	        		puntero++;
	        	} 
	        	puntero++;
	            while (texto.charAt(puntero)!=('.')) {
	                numero+=texto.charAt(puntero);
	                puntero++;
	            }
	        }
	        return numero;
	    }
		private void AgregarParquimetros (JComboBox Box, String calle, String numero) {
			try {
				Statement st=this.conexionBD.createStatement();
				ResultSet rs=st.executeQuery("select id_parq,numero,calle,altura from parquimetros where calle='"+calle+"' AND altura='"+numero+"'");
				while (rs.next()) {
					id=rs.getString("id_parq");
					String id= "ID:"+rs.getString("id_parq");
					String num= "num:" + rs.getString("numero");
					String callea= "calle:"+ rs.getString("calle");
					String altura= "altura:"+ rs.getString("altura");
					Box.addItem(id + " " +num +" "+callea+" "+altura);				
				}
				rs.close();
				st.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		private void AgregarTarjetas(JComboBox Box) {
			try {
				Statement st= this.conexionBD.createStatement();
				ResultSet rs= st.executeQuery("select id_tarjeta from tarjetas");
				while (rs.next()) {
					int tarjeta=rs.getInt("id_tarjeta");
					Box.addItem(tarjeta);
				}
				st.close();
				rs.close();
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
}
