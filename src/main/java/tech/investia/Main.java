package tech.investia;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    private long startTime;
    private boolean running = false;

    @Override
    public void start(Stage stage) {
        Font digitalFont = Font.loadFont(
                getClass().getResourceAsStream(
                        "/fonts/digital-7.ttf"
                ),
                120
        );

        Label h1 = createDigitLabel(digitalFont);
        Label h2 = createDigitLabel(digitalFont);

        Label sec1 = createDigitLabel(digitalFont);
        Label sec2 = createDigitLabel(digitalFont);

        Label colon1 = createDigitLabel(digitalFont);
        Label colon2 = createDigitLabel(digitalFont);
        colon1.setText(":");
        colon2.setText(":");

        Label ms1 = createDigitLabel(digitalFont);
        Label ms2 = createDigitLabel(digitalFont);
        Label ms3 = createDigitLabel(digitalFont);

        h1.setText("0");
        h2.setText("0");

        sec1.setText("3");
        sec2.setText("0");

        ms1.setText("0");
        ms2.setText("0");
        ms3.setText("0");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void start() {
                startTime = System.nanoTime();
                super.start();
            }

            @Override
            public void handle(long now) {
                if (!running) {
                    return;
                }

                double elapsedSeconds = (now - startTime) / 1_000_000_000.0;
                double value;

                if (elapsedSeconds <= 10) {
                    value = 30 - elapsedSeconds;
                } else {
                    double t = elapsedSeconds - 10;
                    value = 20 / (1 + 0.02 * Math.pow(t, 1.2));
                }

                int seconds = (int) value;
                int milliseconds = (int) ((value - seconds) * 1000);
                String secString = String.format("%02d", seconds);
                String msString = String.format("%03d", milliseconds);

                sec1.setText(String.valueOf(secString.charAt(0)));
                sec2.setText(String.valueOf(secString.charAt(1)));
                ms1.setText(String.valueOf(msString.charAt(0)));
                ms2.setText(String.valueOf(msString.charAt(1)));
                ms3.setText(String.valueOf(msString.charAt(2)));
                colon1.setVisible((now / 500_000_000) % 2 == 0);
                colon2.setVisible((now / 500_000_000) % 2 == 0);
            }
        };

        timer.start();

        HBox root = new HBox(-25, h1, h2, colon1, sec1, sec2, colon2, ms1, ms2, ms3);
        root.setAlignment(Pos.CENTER);
        root.setStyle("""
                    -fx-background-color: black;
                    -fx-padding: 30;
                """);
        Scene scene = new Scene(root, 600, 300);
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE && !running) {
                startTime = System.nanoTime();
                running = true;
            }
        });
        stage.setTitle("30 sek. Timer");
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    private Label createDigitLabel(Font font) {
        Label label = new Label("0");
        label.setFont(font);
        label.setTextFill(Color.RED);
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(120);
        label.setStyle("""
                    -fx-effect:
                        dropshadow(
                            gaussian,
                            rgba(255,0,0,0.8),
                            25,
                            0.5,
                            0,
                            0
                        );
                    -fx-font-smoothing-type: gray;
                """);

        return label;
    }

    public static void main(String[] args) {
        launch();
    }

}