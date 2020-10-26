package parquimetros;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class VentanaPrincipal  extends javax.swing.JFrame {
		private JDesktopPane panel;
		protected Connection conexionBD = null;
		private JPanel pnlConsulta;
		private JTextField ingresoUsuario;
		private JPasswordField ingresoContraseña;
		 
		private JButton entrar;
		private String clave;
		private String usuario;
		private JLabel etiUsu;
		private JLabel etiCon;
		
		
		   public static void main(String[] args) 
		   {
		      SwingUtilities.invokeLater(new Runnable() {
		         public void run() {
		            VentanaPrincipal inst = new VentanaPrincipal();
		            inst.setLocationRelativeTo(null);
		            inst.setVisible(true);
		         }
		      });
		   }
	
	public VentanaPrincipal() 
	   {
	      super();
	      this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
          this.setSize(800, 600);

	      panel = new JDesktopPane();
          getContentPane().add(panel, BorderLayout.CENTER);
          panel.setPreferredSize(new java.awt.Dimension(800, 600));
          
          this.getContentPane().add(panel, BorderLayout.CENTER);
          
          pnlConsulta=new JPanel();
          pnlConsulta.setLayout(new GridLayout(3,2));
          this.setBounds(0, 0, 800, 600);
          
          entrar=new JButton("Ingresar");
          entrar.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e) {
            	  usuario=ingresoUsuario.getText();
            	  //conectarBD();
                
                }
                });
          
          etiUsu=new JLabel("Ingrese Usuario :");
          
          etiCon=new JLabel("Ingrese Contraseña :");
   
          ingresoUsuario=new JTextField();         
          ingresoContraseña=new JPasswordField();

          
          pnlConsulta.add(etiUsu);
          pnlConsulta.add(ingresoUsuario);
          pnlConsulta.add(etiCon);
          pnlConsulta.add(ingresoContraseña);
          pnlConsulta.add(entrar);
          
          panel.add(pnlConsulta);
          ingresoContraseña.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e) {
              	clave=String.valueOf(ingresoContraseña.getPassword());
              
              }
              });
          this.getContentPane().add(pnlConsulta);
          setVisible(true);
          
	     
	   }
	 private void conectarBD()
	   {
	      if (this.conexionBD == null)
	      {               
	         try
	         { if (usuario.equals("admin")) {
	        	 //deberia abrir para podrir introducion secuencias sql
	         }
	         if(usuario.equals("inspector")) {
	        	 //deberia abrir unidad del ispector
	         }
	            String servidor = "localhost:3306";
	            String baseDatos = "batallas";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	                   baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";
	   
	            this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
	            
	            
	            pnlConsulta.setVisible(false);
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
