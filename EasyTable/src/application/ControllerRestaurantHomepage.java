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
import java.util.ResourceBundle;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Restaurant;

public class ControllerRestaurantHomepage implements Initializable {
	
	ControllerLogin controllerLogin;
	ControllerRegisterRestaurant controllerRegisterRestaurant;
	ControllerRestaurantHomepage controllerRestaurantHomepage;
	ControllerRestaurantReserva controllerRestaurantReserva;
	
	static final String USER = "pri_easytable";
    static final String PASS = "123easytable123";
    String sql;
    Connection conexion = null;
    PreparedStatement prst = null;
    ResultSet rs = null;
	
	@FXML
    private AnchorPane totalPane, view, opacityPane, drawerPane;

	@FXML
	private Label nombreRestaurante, luminosidad, calidadAire, humedad, temperatura, ruido;
	
	@FXML
	private Pane pane1, pane2, pane3, pane4;

    @FXML
    private ImageView drawerImage;
    
    @FXML
    private JFXButton btnReservas;
    
    
    // Se recibe el nombre del local que se ha registado por parte de la pantallade del login
    public void recibeParametros(ControllerLogin vistaLogin, String nomRestaurante) {
		nombreRestaurante.setText(nomRestaurante);
    	controllerLogin=vistaLogin;
    	mostrarDatosRestaurante(nomRestaurante);
	}
    
    // Se recibe el nombre del local que se ha registado por parte de la pantallade del registro
    public void recibeParametrosRegistro(ControllerRegisterRestaurant vistaRegistrarRestaurante, String nomRestaurante) {
		nombreRestaurante.setText(nomRestaurante);
		controllerRegisterRestaurant=vistaRegistrarRestaurante;
		mostrarDatosRestaurante(nomRestaurante);
	}
    
    // Se recibe el nombre del local desde 
    public void recibeParametrosReserva(ControllerRestaurantReserva vistaRestaurantReserva, String nomRestaurante) {
		nombreRestaurante.setText(nomRestaurante);
		controllerRestaurantReserva=vistaRestaurantReserva;
		mostrarDatosRestaurante(nomRestaurante);
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
    
    
    @FXML
	public void openReservas() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/RestaurantHomepageReservas.fxml"));
			view = loader.load();
			Stage st = new Stage();
			
			// Se pasa el nombre del restaurante a la siguiente ventana
			ControllerRestaurantReserva controllerRestaurantReservaInstancia = (ControllerRestaurantReserva)loader.getController();
			controllerRestaurantReservaInstancia.recibeParametros(controllerRestaurantHomepage, nombreRestaurante.getText());
			
			st.setScene(new Scene(view));
			st.centerOnScreen();
			st.show();
			closeStage();
			
		} catch (IOException e) {
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
	
	
	private void closeStage() {
		Stage st = (Stage) totalPane.getScene().getWindow();
		st.close();
	}
	
	
	public void closeApp() {
		System.exit(0);
	}
	
    
    private void mostrarDatosRestaurante(String localname) {
    	
    	// Se recogen los datos de los sensores del restaurante
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
            
            
            prst = conn.prepareStatement("SELECT * FROM restaurantesensor WHERE usuario_local = ?");
            String strTiend = nombreRestaurante.getText();
            prst.setString(1, strTiend);
            
            rs = prst.executeQuery();
            
            int intId;
            String strTemperatura = "0.00";
        	String strHumedad = "0.00";
        	String strLuminosidad = "0.00";
            
            if(rs.next()) {
            	intId = rs.getInt("id_sensor");
            	
            	prst = conn.prepareStatement("SELECT * FROM sensor WHERE id_sensor = ? AND id IN(SELECT MAX(id) FROM sensor GROUP BY id_sensor)");
                prst.setInt(1, intId);
                
                rs = prst.executeQuery();
                
                if(rs.next()) {
                	
                	strTemperatura = rs.getString("temp");
                	strHumedad = rs.getString("hum");
                	strLuminosidad = rs.getString("lumi");
                	
                	temperatura.setText("Temperatura: " + strTemperatura + " °C");
            		humedad.setText("Humedad: " + strHumedad + " %");
            		luminosidad.setText("Luminosidad: " + strLuminosidad);
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
		
    	Vector<Restaurant> baseRestaurantes = new Vector<Restaurant>();
    	
    	for(int i=0; i<desserializarAArrayRestaurant().size(); i++) {
			baseRestaurantes.addElement(desserializarAArrayRestaurant().elementAt(i));
		}
    	
    	/*
    	// Se muestran los datos del restaurante
		for(int i = 0; i<baseRestaurantes.size(); i++) {
			
			if(localname.equals(baseRestaurantes.elementAt(i).getStoreName())){
				
				//String strMail = baseRestaurantes.elementAt(i).getMail();
				//String strDireccion = baseRestaurantes.elementAt(i).getMail();
				Double dblValoracion = baseRestaurantes.elementAt(i).getValoracion();
				Double dblIca = baseRestaurantes.elementAt(i).getIca();
				Double dblHumedad = baseRestaurantes.elementAt(i).getHumedad();
				Double dblTemperatura = baseRestaurantes.elementAt(i).getTemperatura();
				Double dblRuido = baseRestaurantes.elementAt(i).getRuido();
				
				if (dblValoracion != 0.0) {
					valoracion.setText("Valoración: " + dblValoracion + " estrellas");
				} else {
					valoracion.setText("Valoración: no disponible");
				}
				
				if (dblIca != 0.0) {
					calidadAire.setText("Calidad del aire: " + dblIca + " ICA");
				} else {
					calidadAire.setText("Calidad del aire: no disponible");
				}
				
				if (dblHumedad != 0.0) {
					humedad.setText("Humedad: " + dblHumedad + " %");
				} else {
					humedad.setText("Humedad: no disponible");
				}
				
				if (dblTemperatura != 0.0) {
					temperatura.setText("Temperatura: " + dblTemperatura + " °C");
				} else {
					temperatura.setText("Temperatura: no disponible");
				}
				
				if (dblRuido != 0.0) {
					ruido.setText("Ruido: " + dblRuido + " decibelios");
				} else {
					ruido.setText("Ruido: no disponible");
				}
			}
		}	*/
	}

	public void transition() {
    	
    	opacityPane.setVisible(false);

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),drawerPane);
        translateTransition.setByX(-200);
        translateTransition.play();

        pane1.setStyle("-fx-background-image: url('img/panoramica1.jpg')");
        pane2.setStyle("-fx-background-image: url('img/panoramica2.jpg')");
        pane3.setStyle("-fx-background-image: url('img/panoramica3.jpg')");
        pane4.setStyle("-fx-background-image: url('img/panoramica4.jpg')");

        Animation();

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

    public  void  Animation(){


        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(3),pane4);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        fadeTransition.setOnFinished(event -> {
            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(3),pane3);
            fadeTransition1.setFromValue(1);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {

                FadeTransition fadeTransition2=new FadeTransition(Duration.seconds(3),pane2);
                fadeTransition2.setFromValue(1);
                fadeTransition2.setToValue(0);
                fadeTransition2.play();

                fadeTransition2.setOnFinished(event2 -> {

                    FadeTransition fadeTransition00=new FadeTransition(Duration.seconds(3),pane2);
                    fadeTransition00.setFromValue(0);
                    fadeTransition00.setToValue(1);
                    fadeTransition00.play();


                    fadeTransition00.setOnFinished(event3 -> {
                        FadeTransition fadeTransition11=new FadeTransition(Duration.seconds(3),pane3);
                        fadeTransition11.setFromValue(0);
                        fadeTransition11.setToValue(1);
                        fadeTransition11.play();

                        fadeTransition11.setOnFinished(event4 -> {
                            FadeTransition fadeTransition22=new FadeTransition(Duration.seconds(3),pane4);
                            fadeTransition22.setFromValue(0);
                            fadeTransition22.setToValue(1);
                            fadeTransition22.play();

                            fadeTransition22.setOnFinished(event5 -> {
                                Animation();
                            });
                        });


                    });
                });

            });
        });
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		controllerRestaurantHomepage=this;
		
		transition();
		
	}
}
