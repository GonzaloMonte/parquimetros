package parquimetros;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import quick.dbtable.DBTable;

import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.SwingConstants;

public class Gui_LoginParquimetro extends JFrame {

	private JPanel contentPane;
	private JTextField ingresoUsu;
	public DBTable table; // Es la tabla que usaremos para la conexion a la BD
	private VentanaParquimetros nuevaGUI;
	protected Connection conexionBD=null;

	
	
	public Gui_LoginParquimetro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ingresoUsu = new JTextField();
		ingresoUsu.setBounds(42, 153, 236, 20);
		contentPane.add(ingresoUsu);
		
		JLabel lblIngreseContrasea = new JLabel("Ingrese Usuario");
		lblIngreseContrasea.setBounds(42, 128, 128, 14);
		contentPane.add(lblIngreseContrasea);
		
		JButton btnIniciarSesion = new JButton("Iniciar Sesion");
		btnIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectarBD();
				if (String.valueOf(ingresoUsu.getText()).equals("parquimetro")){
				nuevaGUI=new VentanaParquimetros(conexionBD);
				cerrarVentana();
				nuevaGUI.setVisible(true);
			}else
				JOptionPane.showMessageDialog(getContentPane(),"Password incorrecta","ERROR",JOptionPane.WARNING_MESSAGE);
				
			}
		});
		btnIniciarSesion.setBounds(288, 152, 110, 23);
		contentPane.add(btnIniciarSesion);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InicioSesion nueva=new InicioSesion();
				cerrarVentana();
				nueva.setVisible(true);
			}
		});
		btnVolver.setBounds(288, 205, 110, 23);
		contentPane.add(btnVolver);
		
		JLabel lblIngresoParquimetro = new JLabel("Ingreso parquimetro");
		lblIngresoParquimetro.setHorizontalAlignment(SwingConstants.CENTER);
		lblIngresoParquimetro.setForeground(Color.BLACK);
		lblIngresoParquimetro.setBounds(136, 38, 166, 14);
		contentPane.add(lblIngresoParquimetro);
	}
	private void conectarBD()
	   {
	         try
	         {
	            String driver ="com.mysql.cj.jdbc.Driver";
	        	String servidor = "localhost:3306";
	        	String baseDatos = "parquimetros"; 
	        	String usuario = "parquimetro";
	        	String clave = "parq";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	        	                     baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
	            
	            
	       //establece una conexión con la  B.D. "parquimetros"  usando directamante una tabla DBTable    
	           
	            this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
	           
	         }
	         catch (SQLException ex)
	         {
	            JOptionPane.showMessageDialog(this,
	                           "Se produjo un error al intentar conectarse a la base de datos.\n" 
	                            + ex.getMessage(),
	                            "Error",
	                            JOptionPane.ERROR_MESSAGE);
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	       
	      
	   }

	   private void desconectarBD()
	   {
	         try
	         {
	            table.close();            
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }      
	   }
	   private void cerrarVentana() {
	        this.dispose();
	    }

}
