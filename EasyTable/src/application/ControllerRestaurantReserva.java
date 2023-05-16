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
import com.jfoenix.controls.JFXListView;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Reserva;

public class ControllerRestaurantReserva implements Initializable {
	
	ControllerRestaurantHomepage controllerRestaurantHomepage;
	ControllerRestaurantReserva controllerRestaurantReserva;
	
	static final String USER = "pri_easytable";
    static final String PASS = "123easytable123";
    String sqll;
    Connection conexion = null;
    PreparedStatement prst = null;
    ResultSet rs = null;
	
	@FXML
    private AnchorPane totalPane, view, opacityPane, drawerPane;
	
	@FXML
    private ImageView drawerImage;
	
	@FXML
	private Pane pane1, pane2, pane3, pane4;
    
    @FXML
	private JFXListView<String> lvReservas;
    
    @FXML
	private Label lblNombreCliente, lblNumPersonas, lblFechaReserva, lblInvisibleNombre, lblInvisibleNumPers, lblInvisibleFecha;
    
    @FXML
	private JFXButton btnEliminarReserva;
    
    
    // Se recibe el nombre del restaurante
    public void recibeParametros(ControllerRestaurantHomepage vistaRestaurantHomepage, String nomRestaurante) {
    	
    	lblInvisibleNombre.setText(nomRestaurante);
    	controllerRestaurantHomepage=vistaRestaurantHomepage;
    	
    	configurarListaReservas();
    	
	}
    
    @FXML
	public void openRestaurantHomepage() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/RestaurantHomepage.fxml"));
			view = loader.load();
			Stage st = new Stage();
			
			// Se pasa el nombre del restaurante a la siguiente ventana
			ControllerRestaurantHomepage controllerRestaurantHomepageInstancia = (ControllerRestaurantHomepage)loader.getController();
			controllerRestaurantHomepageInstancia.recibeParametrosReserva(controllerRestaurantReserva, lblInvisibleNombre.getText());
			
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
    
    
    private void closeStage() {
		Stage st = (Stage) totalPane.getScene().getWindow();
		st.close();
	}
    
    @FXML
    void eliminarReserva(ActionEvent event) {
    	
    	// Se añaden las reservas a la lista
    	Vector<Reserva> baseReservas = new Vector<Reserva>();
    	
    	for(int i=0; i<desserializarAArrayReserva().size(); i++) {
    		baseReservas.addElement(desserializarAArrayReserva().elementAt(i));
		}
    	
    	//Reserva eliminarReserva = new Reserva(lblInvisibleNombre.getText(), lblNombreCliente.getText(), lblNumPersonas.getText(), lblFechaReserva.getText());
    	
    	for(int i=0; i<baseReservas.size();i++) {
			if(lblNombreCliente.getText().equals(baseReservas.elementAt(i).getCliente()) && lblInvisibleFecha.getText().equals(baseReservas.elementAt(i).getFechaReserva()) 
					&& lblInvisibleNumPers.getText().equals(baseReservas.elementAt(i).getNumPersonas())) {
				
				baseReservas.remove(i);
			}
		}
    	serializarArrayAJsonReserva(baseReservas);
    	
    	String strReservaFecha = lblInvisibleFecha.getText();
    	String strNumPersonas = lblInvisibleNumPers.getText();
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

            //STEP 5: Realizando una consulta
    		sql = "Delete FROM reservas where reserva_fecha = '"+strReservaFecha+"' AND numero_personas = '"+strNumPersonas+"'";
    		//System.out.println("sql Select: "+sql);
    		stmt = conn.createStatement();
    		stmt.executeUpdate(sql);
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
    	
    	btnEliminarReserva.setDisable(true);
    }
    
    
    private void configurarListaReservas() {
    	
    	btnEliminarReserva.setDisable(true);
    	
    	// Se añaden las reservas a la lista
    	Vector<Reserva> baseReservas = new Vector<Reserva>();
    	
    	for(int i=0; i<desserializarAArrayReserva().size(); i++) {
    		baseReservas.addElement(desserializarAArrayReserva().elementAt(i));
		}
    	    	
    	//System.out.println(lblInvisibleNombre.getText() + "  ff");
    	
    	for(int i = 0; i<baseReservas.size(); i++) {
    		
    		if (lblInvisibleNombre.getText().equals(baseReservas.elementAt(i).getLocalname())) {
    			lvReservas.getItems().add(baseReservas.elementAt(i).getCliente());
    		}
    	}
    	
    	// modifica el tipo de selección de la lista.
    	lvReservas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	
    	// Si se seleciona un restaurante se muestra los datos de este.
    	lvReservas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable, String oldValue, String strClientElegido) {
    	    	
    	    	
    	        // Se añaden a la lista todos los usuarios que hayan reservado en su restaurante
    	        for(int i = 0; i<baseReservas.size(); i++) {
    	        	
    	        	if(strClientElegido.equals(baseReservas.elementAt(i).getCliente())){
    					
    	        		String strNumPersonas = baseReservas.elementAt(i).getNumPersonas();
    					String strFechaReserva = baseReservas.elementAt(i).getFechaReserva();
    					
    					lblNombreCliente.setText(strClientElegido);
    					
    					lblNumPersonas.setText("Numero de personas: " + strNumPersonas);
    					lblFechaReserva.setText("Fecha de la reserva: " + strFechaReserva);
    					
    					lblInvisibleFecha.setText(strFechaReserva);
    					lblInvisibleNumPers.setText(strNumPersonas);
    					
    					btnEliminarReserva.setDisable(false);
    					
    				}
    	    	}   
    	    }
    	});
	}
    
    
    // Array de Reservas
 	public Vector<Reserva> desserializarAArrayReserva(){
 		Vector<Reserva> reservas = new Vector<Reserva>();
 		try (Reader reader =new FileReader("Reservas.json")){
 			Gson gson = new Gson();
 			java.lang.reflect.Type tipoListaReservas = new TypeToken<Vector<Reserva>>() {}.getType();
 			reservas = gson.fromJson(reader,tipoListaReservas);
 		}catch(IOException e) {
 			System.out.print("No se encuentra el archivo");
 			e.printStackTrace();
 		}
 		return reservas;
 	}
 	
 	public void serializarArrayAJsonReserva(Vector<Reserva> reservas) {
 		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
 		try(FileWriter writer = new FileWriter("Reservas.json")){
 			prettyGson.toJson(reservas, writer);
 		}catch(IOException e) {
 			System.out.print("No se encuentra el archivo");
 			e.printStackTrace();
 		}
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		controllerRestaurantReserva=this;
		configurarListaReservas();
		transition();
		
	}

}
