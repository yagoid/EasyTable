package application;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Admin;

public class ControllerRegisterAdmin {
	
	private boolean visible = false;
	
	@FXML
	private AnchorPane view;
	
	@FXML
	private AnchorPane totalPane;
	
	@FXML
    private Pane pane1;

    @FXML
    private Pane pane2;

    @FXML
    private Pane pane3;

    @FXML
    private Pane pane4;
    
    @FXML
	private JFXPasswordField adminpasswordHidden;
	
	@FXML
	private JFXTextField passwordText;
		
    @FXML
    private JFXCheckBox checkbox;

    @FXML
    private JFXTextField adminname;
    
    @FXML
    private ImageView drawerImage;
    
    @FXML
    private AnchorPane opacityPane;

    @FXML
    private AnchorPane drawerPane;
    
    @FXML
    void getAdminName(ActionEvent event) {

    }

    @FXML
    void getAdminPassword(ActionEvent event) {

    }

    @FXML
	void changeVisibility(ActionEvent event) {
		if (!visible) {
			passwordText.setText(adminpasswordHidden.getText());
			adminpasswordHidden.setVisible(false);
			passwordText.setVisible(true);
			
			visible = true;
		} else {
			adminpasswordHidden.setText(passwordText.getText());
			adminpasswordHidden.setVisible(true);
			passwordText.setVisible(false);
			
			visible = false;
		}
	}
    
    static final String USER = "pri_easytable";
    static final String PASS = "123easytable123";
    
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
    
    
    @FXML
    void openAdminRegisterSignUp(ActionEvent event) {
    	
    	// El color de todos los campos se vuelven normales
    	cambiarColorTextField();
    	
    	String adminname_ = adminname.getText();
    	String adminpassword_ = adminpasswordHidden.getText();
    	
    	boolean coincide = true;
    	
    	//Admin newAdmin = new Admin(adminname_,adminpassword_);
    	
    	//Vector<Admin> baseAdmins = new Vector<Admin>();
    	
		/*for(int i = 0; i < desserializarAdminArray().size(); i++) {
			baseAdmins.addElement(desserializarAdminArray().elementAt(i));
		}*/
		
		// Si los campos están vacíos se ponen en rojo las cajas de texto
		if (adminname_.isEmpty()) {
			coincide=false;
			adminname.setStyle("-jfx-unfocus-color: red;");
			
		}
		
		if (adminpassword_.isEmpty()) {
			coincide=false;
			adminpasswordHidden.setStyle("-jfx-unfocus-color: red;");
		}
			
		// Se comprueba que el username no están en la base de datos
		/*for(int i = 0; i < baseAdmins.size(); i++) {
			
			if (adminname_.equals(baseAdmins.elementAt(i).getAdminName())) {
				coincide=false;
				adminname.setText("");
				adminname.setPromptText("Este username ya existe. Pruebe con otro");
				adminname.setStyle("-fx-prompt-text-fill: red; -jfx-unfocus-color: red;");
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
	            
	            sqll = "Insert into admin(usuario_admin, contraseña) values('"+adminname_+"','"+adminpassword_+"');";
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
		adminname.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
		adminname.setPromptText("Username");
    	
    	adminpasswordHidden.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
    	passwordText.setStyle("-fx-prompt-text-fill: #4d4d4d; -jfx-focus-color: #4d4d4d; -jfx-unfocus-color: #4d4d4d;");
	}
    
    private void closeStage() {
		Stage st = (Stage) totalPane.getScene().getWindow();
		st.close();
	} 
	
	
	/*public Vector<Admin> desserializarAdminArray(){
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

	public void serializarArrayAJson(Vector<Admin> admins) {
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		try(FileWriter writer = new FileWriter("Admins.json")){
			prettyGson.toJson(admins, writer);
		}catch(IOException e) {
			System.out.print("No se encuentra el archivo");
			e.printStackTrace();
		}
	}*/
	public void openAdminHomepage() {
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
	}
	
	
	public void initialize() {

		transition();
	}
	
	private void transition() {
		opacityPane.setVisible(false);

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),drawerPane);
        translateTransition.setByX(-200);
        translateTransition.play();

        /*pane1.setStyle("-fx-background-image: url('img/panoramicaClient.jpg'); -fx-background-repeat: no-repeat;-fx-background-size: 100%");
        pane2.setStyle("-fx-background-image: url('img/panoramicaClient2.jpg'); -fx-background-repeat: no-repeat;-fx-background-size: 100%");
        pane3.setStyle("-fx-background-image: url('img/panoramicaClient3.jpg'); -fx-background-repeat: no-repeat;-fx-background-size: 100%");
        pane4.setStyle("-fx-background-image: url('img/panoramicaClient4.jpg'); -fx-background-repeat: no-repeat;-fx-background-size: 100%");*/

        backgroundAnimation();

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
}
