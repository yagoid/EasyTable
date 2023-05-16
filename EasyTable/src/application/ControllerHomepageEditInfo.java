package application;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerHomepageEditInfo implements Initializable{
	
	ControllerHomepage controllerHomepage;
	ControllerHomepageReserva controllerHomepageReserva;
	ControllerHomepageEditInfo controllerHomepageEditInfo;
	
	
	@FXML
    private AnchorPane view;

    @FXML
    private JFXButton btnReservar;
    
    @FXML
    private AnchorPane totalPane;

    @FXML
    private ImageView drawerImage;

    @FXML
    private JFXTextField nombre;

    @FXML
    private JFXTextField primerApllido;

    @FXML
    private JFXTextField segundoApellido;

    @FXML
    private JFXTextField nusuario;

    @FXML
    private JFXTextField contraseña;

    @FXML
    private JFXButton confirmCambios;

    @FXML
    private AnchorPane opacityPane;

    @FXML
    private AnchorPane drawerPane;

    @FXML
    private JFXButton hacerReservas1;

    @FXML
    private JFXButton editarInfo;
    
    @FXML
    private Label invisibleName;
    
    static final String USER = "pri_easytable";
    static final String PASS = "123easytable123";
    String sql;
    Connection conexion = null;
    PreparedStatement prst = null;
    ResultSet rs = null;
    
    @FXML
    void botonConfirmarCambios(ActionEvent event) {
    	
    	Connection conn = null;
        Statement stmt = null;

        try {
            //STEP 1: Register JDBC driver
        	Class.forName("org.mariadb.jdbc.Driver");

            //STEP 2: Open a connection
            System.out.println("Connecting to a selected database...");

            conn = DriverManager.getConnection(
                    "jdbc:mariadb://195.235.211.197/prieasytable", USER, PASS);
            System.out.println("Connectado a la Base de Datos...");
            //CAMBIAR PARAMETROS-------------------------------
            
            String nombre_ = nombre.getText();
			String apellido1_ = primerApllido.getText();
			String apellido2_ =segundoApellido.getText();
			String nusuario_ = nusuario.getText();
			String contraseña_ = contraseña.getText();
            
			sql = "Update clientes SET  nombre = '"+nombre_+"', first_name = '"+apellido1_+"', last_name = '"+apellido2_+"', usuario_cli= '"+nusuario_+"', contraseña = '"+contraseña_+"'  WHERE usuario_cli = '"+invisibleName.getText()+"'";
			System.out.println("sql Insert: "+sql);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);  
			stmt.close();
            
            /*
            String nombre_ = nombre.getText();
			String apellido1_ = primerApllido.getText();
			String apellido2_ =segundoApellido.getText();
			String nusuario_ = nusuario.getText();
			String contraseña_ = contraseña.getText();
            
            stmt = conn.createStatement();
            stmt.executeUpdate("Insert into clientes(nombre) values('"+nombre_+"');");  
            stmt.executeUpdate("Insert into clientes(first_name) values('"+apellido1_+"');");  
            stmt.executeUpdate("Insert into clientes(last_name) values('"+apellido2_+"');");  
            stmt.executeUpdate("Insert into clientes(usuario_cli) values('"+nusuario_+"');");  
            stmt.executeUpdate("Insert into clientes(contraseña) values('"+contraseña_+"');");
            stmt.executeUpdate("Insert into clientes(contraseña) values('2021-12-07 04:25:00');");
            stmt.close();
            */
            //HASTA AQUI-------------------------------
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }
    }
    	
    	
    	/*
    	Vector<Client> baseClientes = new Vector<Client>();
    	String nombreUsuario;
    	for(int i=0; i<desserializarAArrayClientes().size(); i++) {
			baseClientes.addElement(desserializarAArrayClientes().elementAt(i));
		}
    	nombreUsuario=invisibleName.getText();
    	for(int i=0; i<baseClientes.size();i++) {
    		if(nombreUsuario.equals(baseClientes.elementAt(i).getUsername())) {
    			String nombre_ = nombre.getText();
    			String apellido1_ = primerApllido.getText();
    			String apellido2_ =segundoApellido.getText();
    			String nusuario_ = nusuario.getText();
    			String contraseña_ = contraseña.getText();
    			
    			baseClientes.elementAt(i).setName(nombre_);
    			baseClientes.elementAt(i).setSurname1(apellido1_);
    			baseClientes.elementAt(i).setSurname2(apellido2_);
    			baseClientes.elementAt(i).setUsername(nusuario_);
    			baseClientes.elementAt(i).setPassword(contraseña_);
    			}
    		}
    		serializarArrayAJsonClientes(baseClientes);
    }*/

    /*@FXML
    void botonEditarInfo(ActionEvent event) {

    }*/

    @FXML
    void botonHacerReservas(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/HomepageReserva.fxml"));
			view = loader.load();
			Stage st = new Stage();
			
			ControllerHomepageReserva controllerHomepageReservaInstancia = (ControllerHomepageReserva)loader.getController();
			controllerHomepageReservaInstancia.recibeParametros(controllerHomepage, invisibleName.getText());
			
			st.setScene(new Scene(view));
			st.centerOnScreen();
			st.show();
			closeStage();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
	public void openLogin() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Login.fxml"));
			view = loader.load();
			Stage st = new Stage();
			st.initStyle(StageStyle.UNDECORATED);
			st.setScene(new Scene(view));
			st.centerOnScreen();
			st.show();
			closeStage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    
    
    
    private void configurarPantallaEdit() {
    	
    	Connection conn = null;
        Statement stmt = null;
        
        try {
            //STEP 1: Register JDBC driver
        	Class.forName("org.mariadb.jdbc.Driver");

            //STEP 2: Open a connection
            System.out.println("Connecting to a selected database...");

            conn = DriverManager.getConnection(
                    "jdbc:mariadb://195.235.211.197/prieasytable", USER, PASS);
            System.out.println("Connectado a la Base de Datos...");
            
            
            prst=conn.prepareStatement("SELECT * FROM clientes WHERE usuario_cli = ?");
            String us=invisibleName.getText();
            System.out.println(us);
            prst.setString(1, us);
         
            rs = prst.executeQuery();
            
            if(rs.next()) {
            	System.out.println(rs.getString("nombre"));
            	nombre.setText(rs.getString("nombre"));
            	primerApllido.setText(rs.getString("first_name"));
            	segundoApellido.setText(rs.getString("last_name"));
            	nusuario.setText(rs.getString("usuario_cli"));
            	contraseña.setText(rs.getString("contraseña"));
            }
            conn.close();
            
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }
    	/*
    	Vector<Client> baseClientes = new Vector<Client>();
    	String nombreUsuario;
    	for(int i=0; i<desserializarAArrayClientes().size(); i++) {
			baseClientes.addElement(desserializarAArrayClientes().elementAt(i));
		}
    	
    	nombreUsuario=invisibleName.getText();
    	for(int i=0; i<baseClientes.size();i++) {
    		if(nombreUsuario.equals(baseClientes.elementAt(i).getUsername())) {
    			nombre.setPromptText(baseClientes.elementAt(i).getName());
    			primerApllido.setPromptText(baseClientes.elementAt(i).getSurname1());
    			segundoApellido.setPromptText(baseClientes.elementAt(i).getSurname2());
    			nusuario.setPromptText(nombreUsuario);
    			contraseña.setPromptText(baseClientes.elementAt(i).getPassword());
    			
    			nombre.setText(baseClientes.elementAt(i).getName());
    			primerApllido.setText(baseClientes.elementAt(i).getSurname1());
    			segundoApellido.setText(baseClientes.elementAt(i).getSurname2());
    			nusuario.setText(nombreUsuario);
    			contraseña.setText(baseClientes.elementAt(i).getPassword());
    		}
    	}*/
    	
    	
    	
    }
    // Se recibe el nombre del cliente que se ha logeado por parte de la pantallade del login
    public void recibeParametros(ControllerHomepage vistaHomepage, String nomCliente) {
		invisibleName.setText(nomCliente);
    	controllerHomepage=vistaHomepage;
    	configurarPantallaEdit();
    	//mostrarDatosRestaurante(nomRestaurante);
    	
	}
    public void recibeParametrosReserva(ControllerHomepageReserva vistaReserva, String nomCliente) {
		invisibleName.setText(nomCliente);
		controllerHomepageReserva=vistaReserva;
		configurarPantallaEdit();
    	//mostrarDatosRestaurante(nomCliente);
	}
    
    /*// Se recibe el nombre del cliente que se ha registado por parte de la pantallade del registro
    public void recibeParametrosRegistro(ControllerRegisterClient vistaRegistrarCliente, String nomCliente) {
		nombreRestaurante.setText(nomCliente);
		controllerRegisterClient=vistaRegistrarCliente;
		//mostrarDatosRestaurante(nomRestaurante);
	}*/
	
	private void closeStage() {
		Stage st = (Stage) totalPane.getScene().getWindow();
		st.close();
	}
	
	
	public void closeApp() {
		System.exit(0);
	}
	
	
	// Array del Restaurante
	public Vector<Client> desserializarAArrayClientes(){
		Vector<Client> clientes = new Vector<Client>();
		try (Reader reader =new FileReader("Clients.json")){
			Gson gson = new Gson();
			java.lang.reflect.Type tipoListaClientes = new TypeToken<Vector<Client>>() {}.getType();
			clientes = gson.fromJson(reader,tipoListaClientes);
		}catch(IOException e) {
			System.out.print("No se encuentra el archivo");
			e.printStackTrace();
		}
		return clientes;
	}
	
	public void serializarArrayAJsonClientes(Vector<Client> clientes) {
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		try(FileWriter writer = new FileWriter("Clients.json")){
			prettyGson.toJson(clientes, writer);
		}catch(IOException e) {
			System.out.print("No se encuentra el archivo");
			e.printStackTrace();
		}
	}
	
	// Array de Reservas


   
	

	private void transition() {
		opacityPane.setVisible(false);

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),drawerPane);
        translateTransition.setByX(-200);
        translateTransition.play();

        /*pane1.setStyle("-fx-background-image: url('img/panoramicaClient.jpg'); -fx-background-repeat: no-repeat;-fx-background-size: 100%");
        pane2.setStyle("-fx-background-image: url('img/panoramicaClient2.jpg'); -fx-background-repeat: no-repeat;-fx-background-size: 100%");
        pane3.setStyle("-fx-background-image: url('img/panoramicaClient3.jpg'); -fx-background-repeat: no-repeat;-fx-background-size: 100%");
        pane4.setStyle("-fx-background-image: url('img/panoramicaClient4.jpg'); -fx-background-repeat: no-repeat;-fx-background-size: 100%");

        Animation();*/

        drawerImage.setOnMouseClicked(event -> {

            opacityPane.setVisible(true);

            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.4);
            fadeTransition1.play();

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(200);
            translateTransition1.play();
        });

        opacityPane.setOnMouseClicked(event -> {

            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0.4);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {
                opacityPane.setVisible(false);
            });

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(-200);
            translateTransition1.play();
        });
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		controllerHomepageEditInfo = this;
		transition();
		
	}

}
