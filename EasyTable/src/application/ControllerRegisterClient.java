package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Client;
import application.Database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControllerRegisterClient implements Initializable {
	
	private boolean visible = false;
	ControllerRegisterClient controllerRegisterClient;
	
	@FXML
	private AnchorPane view, totalPane;
	
	@FXML
    private Pane pane1, pane2, pane3, pane4;
    
    @FXML
	private JFXPasswordField passwordHidden1;
	
	@FXML
	private JFXTextField passwordText1;
    
    @FXML
	private JFXPasswordField passwordHidden;
	
	@FXML
	private JFXTextField passwordText;
		
	@FXML
	private JFXCheckBox checkBox;

    @FXML
    private JFXCheckBox checkbox;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField secondsurname;

    @FXML
    private JFXTextField firstsurname;

    @FXML
    private JFXTextField username1;
    
    @FXML
    void getConfirmPasswordClient(ActionEvent event) {

    }

    @FXML
    void getFirstSurnameClient(ActionEvent event) {

    }

    @FXML
    void getMailClient(ActionEvent event) {

    }

    @FXML
    void getNameClient(ActionEvent event) {

    }

    @FXML
    void getPasswordClient(ActionEvent event) {

    }

    @FXML
    void getSecondSurnameClient(ActionEvent event) {

    }

    @FXML
    void getUsernameClient(ActionEvent event) {

    }
	
    @FXML
	void changeVisibility(ActionEvent event) {
		if (!visible) {
			passwordText.setText(passwordHidden.getText());
			passwordHidden.setVisible(false);
			passwordText.setVisible(true);
			passwordText1.setText(passwordHidden1.getText());
			passwordHidden1.setVisible(false);
			passwordText1.setVisible(true);
			
			visible = true;
		} else {
			passwordHidden.setText(passwordText.getText());
			passwordHidden.setVisible(true);
			passwordText.setVisible(false);
			passwordHidden1.setText(passwordText1.getText());
			passwordHidden1.setVisible(true);
			passwordText1.setVisible(false);
			
			visible = false;
		}
	}
    
    static final String USER = "pri_easytable";
    static final String PASS = "123easytable123";
    String sql;
    Connection conexion = null;
    PreparedStatement prst = null;
    ResultSet rs = null;
    
    /*boolean validarDatos(String usuario, String email) {
    	
    	Connection conn = null;
        Statement stmt = null;
        String sql;
        try {
            //STEP 1: Register JDBC driver
        	Class.forName("org.mariadb.jdbc.Driver");

            //STEP 2: Open a connection
            System.out.println("Connecting to a selected database...");

            conn = DriverManager.getConnection(
                    "jdbc:mariadb://195.235.211.197/prieasytable", USER, PASS);
            System.out.println("Connectado a la Base de Datos...");
            
            
            sql = "SELECT * FROM clients";
            System.out.println("sql Select: "+sql);
            stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( sql );
			if(rs.next()) {
				String  user = rs.getString("usuario_cli");
				String  correo = rs.getString("email");
				if(user.equals(usuario)||correo.equals(email)) {
					return false;
				}else {
					return true;
				}
			}
            
            conn.close();
            
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
            return false;
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
            return false;
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
    	
    }*/
    
    @FXML
    void openClientRegisterSignUp(ActionEvent event) {
    	
    	// El color de todos los campos se vuelven normales
    	cambiarColorTextField();
    	
    	String sql;
    	String username_ = username.getText();
    	String name_ = name.getText();
    	String surname1_ = firstsurname.getText();
    	String surname2_ = secondsurname.getText();
    	String mail_ = username1.getText();
    	String password_ = passwordHidden.getText();
    	String password1_ = passwordHidden1.getText();
    	Date datetime = new java.util.Date();
    	
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	String dateToStr = dateFormat.format(datetime);
    	
    	
    	boolean coincide = true;
    	
    	//Client newClient = new Client(username_,name_,surname1_,surname2_,mail_,password_);
    	
    	/*Vector<Client> baseClientes = new Vector<Client>();
    	
		for(int i = 0; i < desserializarAArray().size(); i++) {
			baseClientes.addElement(desserializarAArray().elementAt(i));
		}*/
		
		
		// Se comprueba si el mail está en un formato correcto
		Pattern patron = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher mat = patron.matcher(mail_);
		
		
		// Si los campos están vacíos se ponen en rojo las cajas de texto
		if (name_.isEmpty()) {
			coincide=false;
			name.setStyle("-jfx-unfocus-color: red;");
			
		} else if (surname1_.isEmpty()) {
			coincide=false;
			firstsurname.setStyle("-jfx-unfocus-color: red;");
			
		} else if (surname2_.isEmpty()) {
			coincide=false;
			secondsurname.setStyle("-jfx-unfocus-color: red;");
			
		} else if (username_.isEmpty()) {
			coincide=false;
			username.setStyle("-jfx-unfocus-color: red;");
			
		} else if (mail_.isEmpty() || !mat.matches()) {
			coincide=false;
			username1.setStyle("-jfx-unfocus-color: red;");
			
		} else if (password_.isEmpty()) {
			coincide=false;
			passwordHidden.setStyle("-jfx-unfocus-color: red;");
			
		} else if (password1_.isEmpty() || !password_.equals(password1_)) {
			coincide=false;
			passwordHidden.setStyle("-jfx-unfocus-color: red;");
			passwordText.setStyle("-jfx-unfocus-color: red;");
			passwordHidden1.setStyle("-jfx-unfocus-color: red;");
			passwordText1.setStyle("-jfx-unfocus-color: red;");
		}
		
		// Se comprueba que el mail y el username no están en la base de datos
		/*for(int i = 0; i < baseClientes.size(); i++) {
			
			if (mail_.equals(baseClientes.elementAt(i).getMail())) {
				coincide=false;
				username1.setText("");
				username1.setPromptText("Este mail ya existe. Pruebe con otro");
				username1.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
				
			} else if (username_.equals(baseClientes.elementAt(i).getUsername())) {
				coincide=false;
				username.setText("");
				username.setPromptText("Este username ya existe. Pruebe con otro");
				username.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
			}
		}*/
		
		// Si todo está correcto, se registran los datos
		if(coincide) {
			Connection conn = null;
	        Statement stmt = null;
	        String sqll;
	        try {
	            //STEP 1: Register JDBC driver
	        	Class.forName("org.mariadb.jdbc.Driver");

	            //STEP 2: Open a connection
	            System.out.println("Connecting to a selected database...");

	            conn = DriverManager.getConnection(
	                    "jdbc:mariadb://195.235.211.197/prieasytable", USER, PASS);
	            System.out.println("Connectado a la Base de Datos...");

	            
	            //STEP 4: Insertando un valor.
	            
	            sqll = "Insert into clientes(usuario_cli, contraseña, nombre, first_name, last_name, email, fecha) values('"+username_+"','"+password_+"','"+name_+"','"+surname1_+"','"+surname2_+"','"+mail_+"','"+dateToStr+"');";
	            //System.out.println("sql Insert: "+sqll);
	            stmt = conn.createStatement();
	            stmt.executeUpdate(sqll);
	            stmt.close();
				
				//STEP 6: Cerrando conexion.
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
	        }//end try
		}
    }

	private void cambiarColorTextField() {
		username.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	username.setPromptText("Username");
    	
    	name.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	secondsurname.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	firstsurname.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	
    	username1.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	username1.setPromptText("Mail");
    	
    	passwordHidden1.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	passwordText1.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	passwordHidden.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	passwordText.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
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
    
    private void closeStage() {
		Stage st = (Stage) totalPane.getScene().getWindow();
		st.close();
	} 
	
    public void closeApp() {
		System.exit(0);
	}
	
	public void initialize() {

		backgroundAnimation();
	}
	
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
	public void openHomepage(String nomClient) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Homepage.fxml"));
			view = loader.load();
			Stage st = new Stage();
			
			// Se pasa el nombre de el restaurante a la siguiente ventana
			ControllerHomepage controllerHomepageInstancia = (ControllerHomepage)loader.getController();
			controllerHomepageInstancia.recibeParametrosRegistro(controllerRegisterClient, nomClient);
			
			st.setScene(new Scene(view));
			st.centerOnScreen();
			st.show();
			closeStage();
		} catch (IOException e) {
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
		controllerRegisterClient=this;
		
	}
}
