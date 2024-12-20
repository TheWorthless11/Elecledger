package com.example.electricity_billing_system;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Splash extends Application {



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //primaryStage.initStyle(StageStyle.UNDECORATED);
          primaryStage.setTitle("Splash");
          Pane pane = new Pane();


        Image image = new Image(getClass().getResource("/icon/splash.jpg").toExternalForm());

        ImageView imageView =new ImageView(image);

        imageView.setFitWidth(1060);
        imageView.setFitHeight(594);
        imageView.setPreserveRatio(true);

        pane.getChildren().add(imageView);

        Scene scene = new Scene(pane, 1060, 590);
        primaryStage.setScene(scene);
          primaryStage.show();

          new  Thread(()->{
              try {
                  Thread.sleep((2000));
                  javafx.application.Platform.runLater(()->{
                      primaryStage.close();
                     try {
                         Login login = new Login();
                         login.start(new Stage());
                     }catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                  });
              } catch (InterruptedException e){
                  e.printStackTrace();
              }
          }).start();






    }
}
