package parquimetros;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import quick.dbtable.DBTable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.List;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.AbstractListModel;
import java.awt.Color;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class InterfazInspector extends JFrame {
	private String legajo;
	private JPanel contentPane;
	private JTextField patArea;
	private DefaultListModel<String> listaPatentes;
	private JList<String> list;
	protected Connection conexionBD=null;
	protected DBTable table;
	private DBTable tabla;    
	String calleSeleccionada;
	String alturaSeleccionada;
	String id="";


	public InterfazInspector(String legajo,Connection conexion) {
		this.conexionBD=conexion;
		setTitle("Interfaz Inspector");
		setResizable(true);
		this.legajo=legajo;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		DefaultListModel<String> listaPatentes = new DefaultListModel<String>();
		
		JComboBox parquimetrosBox = new JComboBox();
		parquimetrosBox.setSelectedItem(null);
		parquimetrosBox.setBounds(179, 153, 255, 23);
		contentPane.add(parquimetrosBox);
		
		JButton btnAgregarPatente = new JButton("Agregar Patente");
		btnAgregarPatente.setBounds(238, 5, 191, 23);
		contentPane.add(btnAgregarPatente);
		//ESTE COMBO BOX TENDRA LAS UBICACIONES QUE TIENE ACCESO SEGUN LEGAJO.
		JComboBox UbicacionesBox = new JComboBox();
		UbicacionesBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parquimetrosBox.removeAllItems();
				calleSeleccionada= Filtrarcalle(UbicacionesBox.getSelectedItem().toString());
				alturaSeleccionada=Filtraraltura(UbicacionesBox.getSelectedItem().toString());	
				AgregarParquimetros(parquimetrosBox,calleSeleccionada,alturaSeleccionada);
				parquimetrosBox.setSelectedItem(null);
			}
		});
		UbicacionesBox.setMaximumRowCount(10);	
		
		AgregarUbicaciones(UbicacionesBox,legajo);		
		
		
		UbicacionesBox.setBounds(179, 68, 255, 28);
		contentPane.add(UbicacionesBox);
		
		JTextField patArea= new JTextField();
		patArea.setBounds(10, 6, 218, 20);
		contentPane.add(patArea);
		patArea.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 68, 153, 203);
		contentPane.add(panel);
		panel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(55, 69, 2, 2);
		panel.add(scrollPane);
		JScrollPane scroll= new JScrollPane();
		scroll.setBounds(62, 5, 81, 187);
		panel.add(scroll);
		
		tabla = new DBTable();
		tabla.setEditable(false);
		contentPane.add(tabla);
		tabla.setVisible(true);
		tabla.setBounds(458,5,587,331);
		
		JList JListPatente= new JList();
		scroll.setViewportView(JListPatente);
		JListPatente.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		JLabel lblUbicaciones = new JLabel("Ubicaciones");
		lblUbicaciones.setBounds(271, 39, 73, 23);
		contentPane.add(lblUbicaciones);
		
		JLabel lblParquimetros = new JLabel("Parquimetros");
		lblParquimetros.setBounds(271, 119, 90, 23);
		contentPane.add(lblParquimetros);
		
		
		
		JButton btnGenerarmultas = new JButton("GenerarMultas");
		btnGenerarmultas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	Calendar calendario = new GregorianCalendar();
            	int horaActual, minutos, segundos,ainoActual,mesActual,diaActual;
            	ainoActual = calendario.get(Calendar.YEAR);
            	mesActual = calendario.get(Calendar.MONTH)+1;
            	diaActual = calendario.get(Calendar.DAY_OF_MONTH);
            	horaActual =calendario.get(Calendar.HOUR_OF_DAY);
            	minutos = calendario.get(Calendar.MINUTE);
            	segundos = calendario.get(Calendar.SECOND);
            	String fecha = ainoActual+"-"+mesActual+"-"+diaActual;
            	String horario = horaActual+":"+minutos+":"+segundos;
            	if(!checkUbicacion(legajo,calleSeleccionada,alturaSeleccionada,horaActual,minutos)) {
    					JOptionPane.showMessageDialog(null, "Ubicacion no permitida en este horario","Mensaje Error", JOptionPane.WARNING_MESSAGE);
                    }
            	else {
            	registrarAcceso(legajo,id,fecha,horario);
            	DefaultListModel<String> patentesMultadas=new DefaultListModel<String>();
            	if(listaPatentes.isEmpty()) {
            		JOptionPane.showMessageDialog(null, "NO HAY PATENTES CARGADAS","Mensaje Error", JOptionPane.WARNING_MESSAGE);
                }
            	patentesMultadas=EncontrarMultados(listaPatentes,fecha,horario);
            	generarTabla(fecha,horario);  
            	}
            	listaPatentes.clear();
            	JListPatente.removeAll();
            
			}
		});
		btnGenerarmultas.setBounds(238, 226, 137, 34);
		contentPane.add(btnGenerarmultas);
		
		JButton btnVolver = new JButton("Volver ");
		btnVolver.setBounds(271, 299, 89, 23);
		
		btnVolver.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Gui_LoginInspector nueva=new Gui_LoginInspector();
				cerrarVentana();
				nueva.setVisible(true);
			}
		});
		contentPane.add(btnVolver);
		
		
		btnAgregarPatente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chequearPatente(patArea.getText())) {
				listaPatentes.addElement(patArea.getText());
				patArea.setText("");
				JListPatente.setModel(listaPatentes);
			}
				else {
					JOptionPane.showMessageDialog(getContentPane(), "Ingrese una patente válida", "Patente inválida",
							JOptionPane.WARNING_MESSAGE);
					patArea.setText("");
				}
				}
		});
				

	
		
	}
	
	private void AgregarUbicaciones(JComboBox Box,String legajo) {
		try {
			Statement st= this.conexionBD.createStatement();
			ResultSet rs= st.executeQuery("select calle,altura from asociado_con where legajo='"+legajo+"'");
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


	   public boolean checkUbicacion(String legajo,String calle,String altura,int hora,int minutos) {
			boolean pertenece = false;
			Calendar calendario = new GregorianCalendar();
			String turno="''";
			String [] semana = {"'do'","'lu'","'ma'","'mi'","'ju'","'vi'","'sa'"};
			String dia=semana[calendario.get(Calendar.DAY_OF_WEEK)-1];
			if(hora>7 && hora<14) {
				turno ="'m'";
			}
			if(hora>=14 && (hora<20 || (hora==20 && minutos==00))) {
				turno ="'t'";
			}
			try {
				ResultSet rs = conexionBD.createStatement().executeQuery("SELECT legajo FROM asociado_con where turno="+turno+" AND dia="+dia+" AND calle='"+calle+"' AND altura="+altura+" AND legajo="+legajo+";");
				if (rs.next())
					pertenece=true;
			} catch (SQLException ex) {
				   System.out.println("SQLException: " + ex.getMessage());
		            System.out.println("SQLState: " + ex.getSQLState());
		            System.out.println("VendorError: " + ex.getErrorCode());
			}
			
			return pertenece;
		}
	   public void registrarAcceso(String legajoInsp, String id_parq, String fecha, String horario) {
			try {
				conexionBD.createStatement().execute("INSERT INTO accede VALUES("+legajoInsp+","+id_parq+",'"+fecha+"','"+horario+"');");
			} catch (SQLException ex) {
				   System.out.println("SQLException: " + ex.getMessage());
		            System.out.println("SQLState: " + ex.getSQLState());
		            System.out.println("VendorError: " + ex.getErrorCode());
			}
}
	   public DefaultListModel<String> EncontrarMultados(DefaultListModel<String> lista,String fecha, String horario) {
		   DefaultListModel<String> multados= new DefaultListModel<String>();
		   String id_asociado="";
		   Connection c=this.conexionBD;
		   ListModel<String> aux=lista;
		   try {
		   Statement s=c.createStatement();
		   for(int i=0 ; i<aux.getSize() ; i++) {
			   boolean encontro=false;
			   ResultSet rs=s.executeQuery("select patente from estacionados where patente='"+aux.getElementAt(i)+"';");
			   if(rs.next()) {

			   }
			   else {

				   ResultSet rs2=s.executeQuery("select id_asociado_con from asociado_con where legajo="+legajo+";");
				   if(rs2.next()) {
					   id_asociado=rs2.getString("id_asociado_con");
				   }			
				   multados.addElement((String) aux.getElementAt(i));
				   s.execute("insert into multa(fecha,hora,patente,id_asociado_con) VALUE ('"+fecha+"','"+horario+"','"+aux.getElementAt(i)+"','"+id_asociado+"');");
			   }		  		   
		   	}
		   
		   return multados;
		   
		   }catch(SQLException e1) {
			   System.out.println("SQLException: "+ e1.getMessage());
			   System.out.println("SQLSTate: "+ e1.getSQLState());
			   System.out.println("VendorError: "+e1.getErrorCode());
			   return null;
		   }
	   }
	   public void generarTabla(String fecha,String hora) {
		   try {
		   Statement stmt = this.conexionBD.createStatement();
		  
	    	 String sql="select numero,fecha,hora,patente,calle,altura,legajo from multa natural join asociado_con where fecha='"+fecha+"' AND hora='"+hora+"'";
	    	 ResultSet rs= stmt.executeQuery(sql);
 	 		tabla.refresh(rs);
 	 		 for (int i = 0; i < tabla.getColumnCount(); i++)
	    	  { // para que muestre correctamente los valores de tipo hora	   		  
	    		 if	 (tabla.getColumn(i).getType()==Types.TIME)  
	    		 {    		 
	    		    tabla.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    	
	    		 if	 (tabla.getColumn(i).getType()==Types.DATE)
	    		 {
	    		    tabla.getColumn(i).setDateFormat("dd/MM/YYYY");
	    		 }
	          }
		   }
		     catch (SQLException ex)
		      {
		         // se muestra porque ocurre el error
		         System.out.println("SQLException: " + ex.getMessage());
		         System.out.println("SQLState: " + ex.getSQLState());
		         System.out.println("VendorError: " + ex.getErrorCode());
		         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
		                                       ex.getMessage() + "\n", 
		                                       "Error al ejecutar la consulta.",
		                                       JOptionPane.ERROR_MESSAGE);
		         
		      }
	   }
	   private void cerrarVentana() {
			this.dispose();
		}
	   
	   private boolean chequearPatente(String a){
			Boolean esValida=false;
			Pattern p = Pattern.compile("[a-zA-Z]{3}[0-9]{3}");//formato de la patente
			Matcher m=p.matcher(a);
			if(m.matches()){
			esValida=true;
			}
			return esValida;
		}

}
