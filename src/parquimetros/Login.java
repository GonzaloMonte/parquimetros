package parquimetros;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

public class Login extends javax.swing.JInternalFrame  {

	
	protected Connection conexionBD = null;
	private JPanel pnlConsulta;
	private JTextArea ingresoUsuario;
	private JPasswordField ingresoContraseña;
	private JButton botonBorrar;
	private JButton btnEjecutar;
	private String clave;
	private String usuario;
	
	
	public Login() {
		setPreferredSize(new Dimension(800, 600));
        this.setBounds(0, 0, 800, 600);
        setVisible(true);
        pnlConsulta=new JPanel();
        ingresoUsuario=new JTextArea();
        ingresoContraseña=new JPasswordField();
        ingresoUsuario=new JTextArea();
        pnlConsulta.add(ingresoUsuario);
        pnlConsulta.add(ingresoContraseña);
        ingresoContraseña.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	clave=String.valueOf(ingresoContraseña.getPassword());
            
            }
            });

        
	}
	
	
	 private void conectarBD()
	   {
	      if (this.conexionBD == null)
	      {               
	         try
	         {
	        	usuario=ingresoUsuario.getText();
	            String servidor = "localhost:3306";
	            String baseDatos = "batallas";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	                   baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";
	   
	            this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
	         }
	         catch (SQLException ex)
	         {
	            JOptionPane.showMessageDialog(this,
	             "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
	              "Error", JOptionPane.ERROR_MESSAGE);
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	      }
	   }
}
