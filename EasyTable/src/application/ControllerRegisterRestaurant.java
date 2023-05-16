package application;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
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
import model.Restaurant;

public class ControllerRegisterRestaurant implements Initializable{
	
	private boolean visible = false;
	
	ControllerRegisterRestaurant controllerRegisterRestaurant;
	
	static final String USER = "pri_easytable";
    static final String PASS = "123easytable123";
    String sql;
    Connection conexion = null;
    PreparedStatement prst = null;
    ResultSet rs = null;
	
	@FXML
    private AnchorPane totalPane, view;

    @FXML
    private Pane pane1, pane2, pane3, pane4;

    @FXML
    private JFXButton restaurantSingUp;

    @FXML
    private JFXTextField storename;

    @FXML
    private JFXCheckBox checkbox;

    @FXML
    private JFXTextField mailRestaurant, floor, storeaddress, name, secondsurname, firstsurname;

    @FXML
    private JFXPasswordField passwordHidden;

    @FXML
    private JFXTextField passwordText;

    @FXML
    private JFXPasswordField passwordHidden1;

    @FXML
    private JFXTextField passwordText1;
		
	@FXML
	private JFXCheckBox checkBox;
	
	@FXML
    void getConfirmPasswordRestaurant(ActionEvent event) {

    }

    @FXML
    void getFirstSurnameRestaurant(ActionEvent event) {

    }

    @FXML
    void getMailRestaurant(ActionEvent event) {

    }

    @FXML
    void getNameRestaurant(ActionEvent event) {

    }

    @FXML
    void getPasswordRestaurant(ActionEvent event) {

    }

    @FXML
    void getSecondSurnameRestaurant(ActionEvent event) {

    }

    @FXML
    void getStoreAddress(ActionEvent event) {

    }

    @FXML
    void getStoreFloor(ActionEvent event) {

    }

    @FXML
    void getStoreName(ActionEvent event) {

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
    
    @FXML
    void openRestaurantRegisterSingUp(ActionEvent event) {
    	
    	// El color de todos los campos se vuelven normales
    	changeColorTextField();
    	
    	String storename_ = storename.getText();
    	String storeaddress_ = storeaddress.getText();
    	String floor_ = floor.getText();
    	String name_ = name.getText();
    	String firstsurname_ = firstsurname.getText();
    	String secondsurname_ = secondsurname.getText();
    	String mail_ = mailRestaurant.getText();
    	String password_ = passwordHidden.getText();
    	String password1_ = passwordHidden1.getText();
    	Date datetime = new java.util.Date();
    	/*Double valoracion_ = 0.0;
    	Double ica_ = 0.0;
    	Double humedad_ = 0.0;
    	Double temperatura_ = 0.0;
    	Double ruido_ = 0.0;*/
    	
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	String dateToStr = dateFormat.format(datetime);
    	
    	boolean coincide = true;
    	
    	//Restaurant newRestaurant = new Restaurant(storename_,storeaddress_,floor_,name_,firstsurname_,secondsurname_,mail_,password_, valoracion_, ica_,humedad_,temperatura_, ruido_);
    		
    	//Vector<Restaurant> baseRestaurantes = new Vector<Restaurant>();
    	
		/*for(int i=0; i < desserializarAArray().size(); i++) {
			baseRestaurantes.addElement(desserializarAArray().elementAt(i));
		}*/
		
		
		// Se comprueba si el mail está en un formato correcto
		Pattern patron = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher mat = patron.matcher(mail_);
		
		
		// Si los campos están vacíos se ponen en rojo las cajas de texto
		if (storename_.isEmpty()) {
			coincide=false;
			storename.setStyle("-jfx-unfocus-color: red;");
			
		} else if (storeaddress_.isEmpty()) {
			coincide=false;
			storeaddress.setStyle("-jfx-unfocus-color: red;");
			
		} else if (floor_.isEmpty() || !floor_.matches("[0-9]*")) {
			coincide=false;
			floor.setText("");
    		floor.setStyle("-fx-prompt-text-fill: red; -jfx-focus-color: red; -jfx-unfocus-color: red;");
			
		} else if (name_.isEmpty()) {
			coincide=false;
			name.setStyle("-jfx-unfocus-color: red;");
			
		} else if (firstsurname_.isEmpty()) {
			coincide=false;
			firstsurname.setStyle("-jfx-unfocus-color: red;");
			
		} else if (secondsurname_.isEmpty()) {
			coincide=false;
			secondsurname.setStyle("-jfx-unfocus-color: red;");
			
		} else if (mail_.isEmpty() || !mat.matches()) {
			coincide=false;
			mailRestaurant.setText("");
			mailRestaurant.setPromptText("Formato de mail incorrecto");
			mailRestaurant.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
			
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
		
		
		// Se comprueba que el mail y el storename no están en la base de datos
		/*for(int i=0; i < baseRestaurantes.size(); i++) {
			
			if (mail_.equals(baseRestaurantes.elementAt(i).getMail())) {
				coincide=false;
				mailRestaurant.setText("");
				mailRestaurant.setPromptText("Este mail ya existe. Pruebe con otro");
				mailRestaurant.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
				
			} else if (storename_.equals(baseRestaurantes.elementAt(i).getStoreName())) {
				coincide=false;
				storename.setText("");
				storename.setPromptText("Este store name ya existe. Pruebe con otro");
				storename.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
			}
		}
		
		// Si todo está correcto, se registran los datos
		if (coincide) {
			baseRestaurantes.addElement(newRestaurant);
    		serializarArrayAJson(baseRestaurantes);
    		openHomepage(storename_);
		}
    }*/
		
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
	            
	            sqll = "Insert into restaurantes(usuario_local, contraseña, nombre, first_name, last_name, email, direccion, fecha) values('"+storename_+"','"+password_+"','"+name_+"','"+firstsurname_+"','"+secondsurname_+"','"+mail_+"','"+storeaddress_+"','"+dateToStr+"');";
	            //System.out.println("sql Insert: "+sqll);
	            stmt = conn.createStatement();
	            stmt.executeUpdate(sqll);
	            
	            sqll = "Insert into restaurantesensor(usuario_local) values('"+storename_+"');";
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

	private void changeColorTextField() {
		storename.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	storename.setPromptText("Store Name");
    	
    	storeaddress.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	
    	floor.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	floor.setPromptText("Floor");
    	
    	firstsurname.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	name.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	firstsurname.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	secondsurname.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	
    	mailRestaurant.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	mailRestaurant.setPromptText("Mail");
    	
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
	
	public Vector<Restaurant> desserializarAArray(){
		Vector<Restaurant> restaurantes = new Vector<Restaurant>();
		try (Reader reader =new FileReader("Restaurants.json")){
			Gson gson = new Gson();
			java.lang.reflect.Type tipoListaRestaurant = new TypeToken<Vector<Restaurant>>() {}.getType();
			restaurantes = gson.fromJson(reader,tipoListaRestaurant);
		}catch(IOException e) {
			System.out.print("No se encuentra el archivo");
			e.printStackTrace();
		}
		return restaurantes;
	}
	
	public void serializarArrayAJson(Vector<Restaurant> restaurantes) {
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		try(FileWriter writer = new FileWriter("Restaurants.json")){
			prettyGson.toJson(restaurantes, writer);
		}catch(IOException e) {
			System.out.print("No se encuentra el archivo");
			e.printStackTrace();
		}
	}
	
	public void openHomepage(String storename) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/RestaurantHomepage.fxml"));
			view = loader.load();
			Stage st = new Stage();
			
			// Se pasa el nombre de el restaurante a la siguiente ventana
			ControllerRestaurantHomepage controllerRestaurantHomepageInstancia = (ControllerRestaurantHomepage)loader.getController();
			controllerRestaurantHomepageInstancia.recibeParametrosRegistro(controllerRegisterRestaurant, storename);
			
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
		controllerRegisterRestaurant=this;
		
	}
}
