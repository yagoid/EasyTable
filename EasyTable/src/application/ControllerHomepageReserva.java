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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Reserva;
import model.Restaurant;

public class ControllerHomepageReserva implements Initializable{
	
	ControllerHomepageEditInfo controllerHomepageEditInfo;
	ControllerHomepage controllerHomepage;
	ControllerHomepageReserva controllerHomepageReserva;
	
	static final String USER = "pri_easytable";
    static final String PASS = "123easytable123";
    String sql;
    Connection conexion = null;
    PreparedStatement prst = null;
    ResultSet rs = null;
	
	@FXML
    private AnchorPane totalPane, view, opacityPane, drawerPane;

    @FXML
    private ImageView drawerImage;
    
    @FXML
    private JFXButton hacerReservas, editarInfo, btnReservar, btnAplicarFiltro;
    
    @FXML
	private JFXListView<String> lvRestaurantes;
    
    @FXML
	private Label nombreRestaurante, luminosidad, calidadAire, humedad, temperatura, ruido, lblInvisibleNombre, lblReservaOcupada, lblMaximaTemp;
    
    @FXML
    private DatePicker dpFechaReserva;
    
    @FXML
    private ComboBox<String> cbHoraReserva, cbNumClientes, cbFiltrarTemp;
    

    
    // Se recibe el nombre del cliente que se ha logeado por parte de la pantallade del login
    public void recibeParametros(ControllerHomepage vistaHomepage, String nomCliente) {
		lblInvisibleNombre.setText(nomCliente);
    	controllerHomepage=vistaHomepage;
	}
    
    // Se recibe el nombre del cliente que se ha registado por parte de la pantallade del registro
    public void recibeParametrosEditInfo(ControllerHomepageEditInfo vistaEditInfo, String nomCliente) {
		nombreRestaurante.setText(nomCliente);
		controllerHomepageEditInfo=vistaEditInfo;
		//mostrarDatosRestaurante(nomRestaurante);
	}
    	
    
    @FXML
    void botonEditarInfo(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/HomepageEditinfo.fxml"));
			view = loader.load();
			Stage st = new Stage();
			
			ControllerHomepageEditInfo controllerHomepageEditarInfoInstancia = (ControllerHomepageEditInfo)loader.getController();
			controllerHomepageEditarInfoInstancia.recibeParametrosReserva(controllerHomepageReserva, lblInvisibleNombre.getText());
			
			st.setScene(new Scene(view));
			st.centerOnScreen();
			st.show();
			closeStage();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void botonHacerReservas(ActionEvent event) {
    	/*try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/HomepageReserva.fxml"));
			view = loader.load();
			Stage st = new Stage();
			st.initStyle(StageStyle.UNDECORATED);
			st.setScene(new Scene(view));
			st.centerOnScreen();
			st.show();
			closeStage();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
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
    
    
    @SuppressWarnings("deprecation")
	@FXML
    void reservar(ActionEvent event) throws ParseException {
    	
        String localname = nombreRestaurante.getText();
        String nomCliente = lblInvisibleNombre.getText();
        String numPersonas = cbNumClientes.getValue();
        Date datetime = new java.util.Date();
        String fecha;
        
        LocalDate localDate = dpFechaReserva.getValue();
        String strHoraElegida = cbHoraReserva.getValue();
        
        //CONVERTIR DE LOCALDATE A DATE
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        
        //Creamos la hora con formato del api Java
        SimpleDateFormat formatoHoras = new SimpleDateFormat("HH:mm:ss");
        Date date2 = formatoHoras.parse(strHoraElegida);
        
        // Convertimos de Date a Calendar
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        
        // Se convinan la fecha elegida con la hora elegida
        calendar.add(Calendar.HOUR, date2.getHours());
        calendar.add(Calendar.MINUTE, date2.getMinutes());
        
        fecha = calendar.getTime().toString();
        String strDatetime = datetime.toString();
        
        Reserva newReserva = new Reserva(localname, nomCliente, numPersonas, fecha);
        
        // Se añaden las reservas a la lista
    	Vector<Reserva> baseReservas = new Vector<Reserva>();
    	
    	for(int i=0; i<desserializarAArrayReserva().size(); i++) {
    		baseReservas.addElement(desserializarAArrayReserva().elementAt(i));
		}
    	
    	boolean blOcupado = false;
    	
    	// Se comprueba que la fecha de la reserva y el numero de personas estén disponibles
    	for(int i = 0; i < baseReservas.size(); i++) {
    		
    		// Si ya está pcupada la reserva para ese restaurante, no s epuede reservar
    		if (localname.equals(baseReservas.elementAt(i).getLocalname()) && numPersonas.equals(baseReservas.elementAt(i).getNumPersonas()) 
    				&& fecha.equals(baseReservas.elementAt(i).getFechaReserva())) {
    			
    			blOcupado = true;
    			lblReservaOcupada.setStyle("-fx-text-fill: red;");
    			lblReservaOcupada.setText("Lo sentimos, esta hora está ocupada. Pruebe con otra");
    			
    			break;
			}
    	}
    	
    	// Si la hora elegida está libre, reserva sin problemas
		if (!blOcupado) {
			baseReservas.addElement(newReserva);
			serializarArrayAJsonReserva(baseReservas);
			lblReservaOcupada.setStyle("-fx-text-fill: #4d4d4d;");
			lblReservaOcupada.setText("Reserva realizada con éxito");
			
			
			// Se mete en la base de datos
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
	            
	            sqll = "Insert into reservas(reserva_fecha, fecha, numero_personas, usuario_cli, usuario_local) values('"+fecha+"', '"+strDatetime+"','"+numPersonas+"','"+nomCliente+"','"+localname+"');";
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
	
    
    @FXML
    void aplicarFiltro(ActionEvent event) {
    	
    	String strTemperatura = cbFiltrarTemp.getValue();
    	
    	// Convertir a Double
        double dblTempElegida = Double.parseDouble(strTemperatura);
    	
    	// Se añaden los nombres de los restaurantes a la lista filtrados
    	Vector<Restaurant> baseRestaurantes = new Vector<Restaurant>();
    	
    	for(int i = 0; i < desserializarAArrayRestaurant().size(); i++) {
			baseRestaurantes.addElement(desserializarAArrayRestaurant().elementAt(i));
		}
    	
    	lvRestaurantes.getItems().clear();
    	
    	double dblTempLocal;
    	for(int i = 0; i<baseRestaurantes.size(); i++) {
    		
    		dblTempLocal = baseRestaurantes.elementAt(i).getTemperatura();
    		
    		if (dblTempLocal >= dblTempElegida-2.5 && dblTempLocal <= dblTempElegida+2.5) {
    			lvRestaurantes.getItems().add(baseRestaurantes.elementAt(i).getStoreName());
    		}
    	}
    	
    	btnAplicarFiltro.setDisable(true);
    }
    

    
    
    // ALGORITMO DE DIVIDE Y VENCERÁS DE LA ASIGNATURA TPA
    private static double calcularMaximo(double[] array, int inicio, int fin) {
		
		if (inicio == fin) {
			return array[inicio];
		
		} else {
			int mitad = (inicio + fin) /2;
			
			double maxIzq = calcularMaximo(array, inicio, mitad);
			double maxDer = calcularMaximo(array, mitad+1, fin);
			
			if (maxIzq > maxDer) {
				return maxIzq;
			} else {
				return maxDer;
			}
		}
	}
    // FIN ALGORITMO DIVIDE Y VENCERÁS
    
	
    
	
	private void closeStage() {
		Stage st = (Stage) totalPane.getScene().getWindow();
		st.close();
	}
	
	
	public void closeApp() {
		System.exit(0);
	}

	
	private void configurarListaFiltros() {
		btnAplicarFiltro.setDisable(true);
		
		// Se añaden las temperaturas para filtrar
        cbFiltrarTemp.getItems().addAll("15.0", "20.0", "25.0", "30.0", "35.0");

        cbFiltrarTemp.valueProperty().addListener((ov, p1, p2) -> {
         
            btnAplicarFiltro.setDisable(false);
        });
	}
	

	private void configurarListaRestaurantes() {
		
		double[] arrayTemperatura = new double[10];
		
		btnReservar.setDisable(true);
		cbHoraReserva.setDisable(true);
		cbNumClientes.setDisable(true);
		
		
		// Se añaden los nombres de los restaurantes a la lista
    	Vector<Restaurant> baseRestaurantes = new Vector<Restaurant>();
    	
    	for(int i=0; i<desserializarAArrayRestaurant().size(); i++) {
			baseRestaurantes.addElement(desserializarAArrayRestaurant().elementAt(i));
		}
    	
    	for(int i = 0; i<baseRestaurantes.size(); i++) {
    		lvRestaurantes.getItems().add(baseRestaurantes.elementAt(i).getStoreName());
    		
    		// Se añaden las temperaturas existentes a la lista de temperaturas
    		arrayTemperatura[i] = (baseRestaurantes.elementAt(i).getTemperatura());
    	}
    	
    	// Se muestra la temperatura mas alta en los restaurantes mediante un algoritmo de TPA. (Divide y venceras)
    	lblMaximaTemp.setText(String.valueOf(calcularMaximo(arrayTemperatura,0,arrayTemperatura.length-1)) + "ºC");
    	
    	
    	// modifica el tipo de selección de la lista.
    	lvRestaurantes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	// Si se seleciona un restaurante se muestra los datos de este.
    	lvRestaurantes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable, String oldValue, String strRestauranteElegido) {
    	    	
    	    	nombreRestaurante.setText(strRestauranteElegido);
    	    	
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
    	            String strTiend = strRestauranteElegido;
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
    	                
    	            } else {
    	            	temperatura.setText("Temperatura: No disponible");
	            		humedad.setText("Humedad: No disponible");
	            		luminosidad.setText("Luminosidad: No disponible");
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
    	    	
    	        //System.out.println(strRestauranteElegido);
    	        /*
    	        for(int i = 0; i<baseRestaurantes.size(); i++) {
    	        	if(strRestauranteElegido.equals(baseRestaurantes.elementAt(i).getStoreName())){
    					
    	        		String strNombreLocal = baseRestaurantes.elementAt(i).getStoreName();
    					Double dblValoracion = baseRestaurantes.elementAt(i).getValoracion();
    					Double dblIca = baseRestaurantes.elementAt(i).getIca();
    					Double dblHumedad = baseRestaurantes.elementAt(i).getHumedad();
    					Double dblTemperatura = baseRestaurantes.elementAt(i).getTemperatura();
    					Double dblRuido = baseRestaurantes.elementAt(i).getRuido();
    					
    					nombreRestaurante.setText(strNombreLocal);
    					
    					if (dblValoracion != 0.0) {
    						luminosidad.setText("Valoración: " + dblValoracion + " estrellas");
    					} else {
    						luminosidad.setText("Valoración: no disponible");
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
    	    	}*/
    	    
    	        
    	        // Se pone en la fecha a elegir la actual
    	        dpFechaReserva.setValue(LocalDate.now());
    	        
    	        cbHoraReserva.setDisable(false);
    	        
    	        // Se añaden las horas a elegir
    	        cbHoraReserva.getItems().addAll("10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00"
    	        		, "16:00:00", "16:30:00", "17:00:00", "17:30:00", "19:00:00", "19:30:00", "20:00:00", "20:30:00", "21:00:00", "21:30:00", "22:00:00", "22:30:00");

    	        cbHoraReserva.valueProperty().addListener((ov, p1, p2) -> {
    	        	cbNumClientes.setDisable(false);
    	        });
    	        
    	        
    	        // Se añaden los numeros de personas para la reserva a elegir
    	        cbNumClientes.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    	        cbNumClientes.valueProperty().addListener((ov, p1, p2) -> {
    	         
    	            btnReservar.setDisable(false);
    	        });
    	        
    	    }
    	});
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		controllerHomepageReserva = this;
		configurarListaRestaurantes();
		configurarListaFiltros();
        transition();
		
	}

}
