package application;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import model.Client;
import model.Restaurant;
import model.Admin;

public class ControllerLogin implements Initializable {
	
	private boolean visible = false;
	
	static final String USER = "pri_easytable";
    static final String PASS = "123easytable123";
    String sql;
    Connection conexion = null;
    PreparedStatement prst = null;
    ResultSet rs = null;
	
	ControllerLogin controllerLogin;
	
	@FXML
	private AnchorPane view, totalPane;
	
	@FXML
	private Pane pane1, pane2, pane3, pane4;
	
	@FXML
	private JFXPasswordField passwordHidden;
	
	@FXML
	private JFXTextField passwordText, usertext;
		
	@FXML
	private JFXCheckBox checkBox;
	
	@FXML
	private JFXButton signupb;

	@FXML
	void changeVisibility(ActionEvent event) {
		if (!visible) {
			passwordText.setText(passwordHidden.getText());
			passwordHidden.setVisible(false);
			passwordText.setVisible(true);
			
			visible = true;
		} else {
			passwordHidden.setText(passwordText.getText());
			passwordHidden.setVisible(true);
			passwordText.setVisible(false);
			
			visible = false;
		}
	}
	
	
	@FXML
	public void openStageRegister() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/RegisterSelectType.fxml"));
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
	
	
	@FXML
    void pulsaEnter(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			openStageHomepage();
		}
    }
	
	
	@FXML
	public void openStageHomepage() {
		
		// El color de los campos usuario y contraseña se vuelven normales
		usertext.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
		passwordHidden.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
		
		// Si el campo de user y password no están vacíos se ejecuta el codigo de iniciar sesión
		if (!usertext.getText().isEmpty() && !passwordHidden.getText().isEmpty()) {
			
			
			Connection conn = null;
	        Statement stmt = null;
	        String strUser;
	        String strContrasenia = "";
	        
	        try {
	            //STEP 1: Register JDBC driver
	        	Class.forName("org.mariadb.jdbc.Driver");

	            //STEP 2: Open a connection
	            System.out.println("Connecting to a selected database...");

	            conn = DriverManager.getConnection(
	                    "jdbc:mariadb://195.235.211.197/prieasytable", USER, PASS);
	            System.out.println("Connectado a la Base de Datos...");
	            
	            
	            // SE MIRA EN LA BASE DE DATOS SI EL USUAIRO ES RESTAUTANTE
	            prst = conn.prepareStatement("SELECT * FROM restaurantes WHERE usuario_local = ?");
	            strUser = usertext.getText();
	            System.out.println(strUser);
	            prst.setString(1, strUser);
	            
	            rs = prst.executeQuery();
	            
	            strContrasenia = "";
	            
	            if(rs.next()) {
	            	strContrasenia = rs.getString("contraseña");
	            	
	            	if (strContrasenia.equals(passwordHidden.getText())) {
	            		try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/RestaurantHomepage.fxml"));
							view = loader.load();
							Stage st = new Stage();
							
							// Se pasa el nombre de el restaurante a la siguiente ventana
							ControllerRestaurantHomepage controllerRestaurantHomepageInstancia = (ControllerRestaurantHomepage)loader.getController();
							controllerRestaurantHomepageInstancia.recibeParametros(controllerLogin, usertext.getText());
							
							st.setScene(new Scene(view));
							st.centerOnScreen();
							st.show();
							closeStage();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
	            	}
	            }
	            
	            // SE MIRA EN LA BASE DE DATOS SI EL USUAIRO ES CLIENTE
	            prst = conn.prepareStatement("SELECT * FROM clientes WHERE usuario_cli = ?");
	            System.out.println(strUser);
	            prst.setString(1, strUser);
	            
	            rs = prst.executeQuery();
	            
	            strContrasenia = "";
	            
	            if(rs.next()) {
	            	strContrasenia = rs.getString("contraseña");
	            	
	            	if (strContrasenia.equals(passwordHidden.getText())) {
	            		try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Homepage.fxml"));
							view = loader.load();
							Stage st = new Stage();
							
							// Se pasa el nombre del cliente a la siguiente ventana
							ControllerHomepage controllerHomepageInstancia = (ControllerHomepage)loader.getController();
							controllerHomepageInstancia.recibeParametros(controllerLogin, usertext.getText());
							
							st.setScene(new Scene(view));
							st.centerOnScreen();
							st.show();
							closeStage();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
	            	}
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
			
		
			Vector<Client> baseClientes = new Vector<Client>();
			Vector<Restaurant> baseRestaurantes = new Vector<Restaurant>();
			Vector<Admin> baseAdmins = new Vector<Admin>();
			
			for(int i=0; i<desserializarAArray().size(); i++) {
				baseClientes.addElement(desserializarAArray().elementAt(i));
			}
			
			for(int i=0; i<desserializarAArrayRestaurant().size(); i++) {
				baseRestaurantes.addElement(desserializarAArrayRestaurant().elementAt(i));
			}
			
			for(int i=0; i<desserializarAdminArray().size(); i++) {
				baseAdmins.addElement(desserializarAdminArray().elementAt(i));
			}
			
			
			/*
			// Se mira si el usuario introducido es un cliente
			for(int i = 0; i<baseClientes.size(); i++) {
				
				if(passwordHidden.getText().equals(baseClientes.elementAt(i).getPassword()) && usertext.getText().equals(baseClientes.elementAt(i).getUsername())){
					
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Homepage.fxml"));
						view = loader.load();
						Stage st = new Stage();
						
						// Se pasa el nombre del cliente a la siguiente ventana
						ControllerHomepage controllerHomepageInstancia = (ControllerHomepage)loader.getController();
						controllerHomepageInstancia.recibeParametros(controllerLogin, usertext.getText());
						
						st.setScene(new Scene(view));
						st.centerOnScreen();
						st.show();
						closeStage();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				// Si las credenciales son incorrectas se vuelve rojo el texto
				} else {
					usertext.setStyle("-jfx-unfocus-color: red;");
					passwordHidden.setStyle("-jfx-unfocus-color: red;");
				}
			}*/
			/*
			// Se mira si el usuario introducido es un restaurante
			for(int i = 0; i<baseRestaurantes.size(); i++) {
				
				if(passwordHidden.getText().equals(baseRestaurantes.elementAt(i).getPasswordr()) && usertext.getText().equals(baseRestaurantes.elementAt(i).getStoreName())){
					
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/RestaurantHomepage.fxml"));
						view = loader.load();
						Stage st = new Stage();
						
						// Se pasa el nombre de el restaurante a la siguiente ventana
						ControllerRestaurantHomepage controllerRestaurantHomepageInstancia = (ControllerRestaurantHomepage)loader.getController();
						controllerRestaurantHomepageInstancia.recibeParametros(controllerLogin, usertext.getText());
						
						st.setScene(new Scene(view));
						st.centerOnScreen();
						st.show();
						closeStage();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				
				}
			}*/
			
			// Se mira si el usuario introducido es un admin
			for(int i = 0; i<baseAdmins.size(); i++) {
				
				if(passwordHidden.getText().equals(baseAdmins.elementAt(i).getAdminPassword()) && usertext.getText().equals(baseAdmins.elementAt(i).getAdminName())){
					
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AdminHomepage.fxml"));
						view = loader.load();
						Stage st = new Stage();
						st.setScene(new Scene(view));
						st.centerOnScreen();
						st.show();
						closeStage();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				// Si las credenciales son incorrectas se vuelve rojo el texto
				} else {
					usertext.setStyle("-jfx-unfocus-color: red;");
					passwordHidden.setStyle("-jfx-unfocus-color: red;");
				}
			}
			
			
			
		// Si el campo de usuario está vacío se pone en rojo
		} else if (usertext.getText().isEmpty() && !passwordHidden.getText().isEmpty()){
			
			usertext.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
			passwordHidden.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
			
		// Si el campo de contraseña está vacío se pone en rojo
		} else if (!usertext.getText().isEmpty() && passwordHidden.getText().isEmpty()) {
			
			passwordHidden.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
			usertext.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
				
		// Si el campo de usuario y contraseña están vacíos se ponen en rojo
		} else {
			
			usertext.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
			passwordHidden.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
		}
	}	
	
	
	private void closeStage() {
		Stage st = (Stage) totalPane.getScene().getWindow();
		st.close();
	}
	
	
	public void closeApp() {
		System.exit(0);
	}
	
	// Array del Cliente
	public Vector<Client> desserializarAArray(){
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
	
	public void serializarArrayAJson(Vector<Client> clientes) {
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		try(FileWriter writer = new FileWriter("Clients.json")){
			prettyGson.toJson(clientes, writer);
		}catch(IOException e) {
			System.out.print("No se encuentra el archivo");
			e.printStackTrace();
		}
	}
	
	// Array del Restaurante
	public Vector<Restaurant> desserializarAArrayRestaurant(){
		Vector<Restaurant> restaurantes = new Vector<Restaurant>();
		try (Reader reader =new FileReader("Restaurants.json")){
			Gson gson = new Gson();
			java.lang.reflect.Type tipoListaRestaurantes = new TypeToken<Vector<Restaurant>>() {}.getType();
			restaurantes = gson.fromJson(reader,tipoListaRestaurantes);
		}catch(IOException e) {
			System.out.print("No se encuentra el archivo");
			e.printStackTrace();
		}
		return restaurantes;
	}
	
	public void serializarArrayAJsonRestaurant(Vector<Restaurant> restaurantes) {
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		try(FileWriter writer = new FileWriter("Restaurants.json")){
			prettyGson.toJson(restaurantes, writer);
		}catch(IOException e) {
			System.out.print("No se encuentra el archivo");
			e.printStackTrace();
		}
	}
	
	// Array del Admin
		public Vector<Admin> desserializarAdminArray(){
			Vector<Admin> admins = new Vector<Admin>();
			try (Reader reader =new FileReader("Admins.json")){
				Gson gson = new Gson();
				java.lang.reflect.Type tipoListaAdmins = new TypeToken<Vector<Admin>>() {}.getType();
				admins = gson.fromJson(reader,tipoListaAdmins);
			}catch(IOException e) {
				System.out.print("No se encuentra el archivo");
				e.printStackTrace();
			}
			return admins;
		}
		
		public void serializarAdminArrayAJson(Vector<Admin> admin) {
			Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
			try(FileWriter writer = new FileWriter("Admins.json")){
				prettyGson.toJson(admin, writer);
			}catch(IOException e) {
				System.out.print("No se encuentra el archivo");
				e.printStackTrace();
			}
		}
	
	private void backgroundAnimation() {

		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(5),
				pane4);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.play();

		fadeTransition
				.setOnFinished(event -> {

					FadeTransition fadeTransition1 = new FadeTransition(
							Duration.seconds(3), pane3);
					fadeTransition1.setFromValue(1);
					fadeTransition1.setToValue(0);
					fadeTransition1.play();

					fadeTransition1.setOnFinished(event1 -> {
						FadeTransition fadeTransition2 = new FadeTransition(
								Duration.seconds(3), pane2);
						fadeTransition2.setFromValue(1);
						fadeTransition2.setToValue(0);
						fadeTransition2.play();

						fadeTransition2.setOnFinished(event2 -> {

							FadeTransition fadeTransition0 = new FadeTransition(
									Duration.seconds(3), pane2);
							fadeTransition0.setFromValue(0);
							fadeTransition0.setToValue(1);
							fadeTransition0.play();

							fadeTransition0.setOnFinished(event3 -> {

								FadeTransition fadeTransition11 = new FadeTransition(
										Duration.seconds(3), pane3);

								fadeTransition11.setFromValue(0);
								fadeTransition11.setToValue(1);
								fadeTransition11.play();

								fadeTransition11.setOnFinished(event4 -> {

									FadeTransition fadeTransition22 = new FadeTransition(
											Duration.seconds(3), pane4);

									fadeTransition22.setFromValue(0);
									fadeTransition22.setToValue(1);
									fadeTransition22.play();

									fadeTransition22.setOnFinished(event5 -> {

										backgroundAnimation();
									});

								});

							});

						});
					});

				});

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		controllerLogin=this;
		
		backgroundAnimation();
		
	}
	
	

}
