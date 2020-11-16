package parquimetros;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import quick.dbtable.DBTable;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.awt.event.ActionEvent;

public class Gui_LoginInspector extends JFrame {

	private JPanel contentPane;
	private JTextField Legajotext;
	private JPasswordField LegajoPass;
	public DBTable table; // Es la tabla que usaremos para la conexion a la BD
	private String legajo;
	private String password;
	protected Connection conexionBD=null;
	protected InterfazInspector nuevaGUI;
	
	
	public Gui_LoginInspector() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIngreseLegajo = new JLabel("Ingrese legajo");
		lblIngreseLegajo.setBounds(56, 96, 192, 14);
		contentPane.add(lblIngreseLegajo);
		
		Legajotext = new JTextField();
		Legajotext.setBounds(56, 121, 167, 20);
		contentPane.add(Legajotext);
		Legajotext.setColumns(10);
		
		JLabel lblIngreseContrasea = new JLabel("Ingrese contraseña");
		lblIngreseContrasea.setBounds(56, 172, 192, 20);
		contentPane.add(lblIngreseContrasea);
		
		LegajoPass = new JPasswordField();
		LegajoPass.setBounds(56, 203, 167, 20);
		contentPane.add(LegajoPass);
		
		JLabel lblBienvenido = new JLabel("Bienvenido");
		lblBienvenido.setBounds(177, 31, 129, 33);
		contentPane.add(lblBienvenido);
		
		JButton btnIniciarSesion = new JButton("Iniciar Sesion");
		btnIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectarBD();
				if (checkInspector(Legajotext.getText(),String.valueOf(LegajoPass.getPassword()))) {
					nuevaGUI=new InterfazInspector(Legajotext.getText(),conexionBD);
                    cerrarVentana();
                    nuevaGUI.setVisible(true);
				}else
				JOptionPane.showMessageDialog(getContentPane(), "Legajo o contraseña incorrectos", "ERROR",
							JOptionPane.WARNING_MESSAGE);
				
				
			}
		});
		btnIniciarSesion.setBounds(283, 120, 103, 23);
		contentPane.add(btnIniciarSesion);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setBounds(283, 202, 103, 23);
		contentPane.add(btnVolver);
}
	private void conectarBD()
	   {
	         try
	         {
	            String driver ="com.mysql.cj.jdbc.Driver";
	        	String servidor = "localhost:3306";
	        	String baseDatos = "parquimetros"; 
	        	String usuario = "inspector";
	        	String clave = "inspector";
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

	
	//chequea que el inspector este en la tabla de usuarios para activar su interfaz
	private boolean checkInspector(String user, String pw) {
		boolean salida = true;
		try {
			Statement st = this.conexionBD.createStatement();
			ResultSet rs = st.executeQuery("select nombre,apellido from inspectores where legajo='" + user
					+ "' and password=md5('" + pw + "')");
		
			salida = rs.next();
			st.close();
			rs.close();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return salida;
	}
	private void cerrarVentana() {
        this.dispose();
    }
}
	
	
	
