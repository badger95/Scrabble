package scrabbleAISrcPckg;

import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Objects;

public class Letter extends StackPane {
    private Rectangle rectangle;
    private Text text;
    private String originalValue;
    private boolean isCommittedToBoard = false;

    Letter() {
    }

    public Letter(char character) {
        rectangle = new Rectangle(50,50);
        text = new Text(character+"");
        rectangle.setFill(Color.SADDLEBROWN);
        rectangle.setStroke(Color.CHOCOLATE);
        getChildren().addAll(rectangle,text);

        setOnDragDetected(event -> {
            if (!Objects.equals(text.getText(), "") || text.getText().length() > 1 || text.getText().equals("★")) {
                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                db.setDragView(snapshot(null, new WritableImage(51, 51)));
                db.setDragViewOffsetX(35);
                db.setDragViewOffsetY(35);
                ClipboardContent content = new ClipboardContent();
                content.putString(text.getText());
                originalValue = text.getText();
                rectangle.setFill(Color.TAN);
                text.setText("");
                db.setContent(content);

                event.consume();
            }
        });

        setOnDragDone(event -> {
            // successful drop
            if (event.getTransferMode() == TransferMode.MOVE) {
                text.setText("");
                rectangle.setFill(Color.TAN);
            }
            // failed drop
            else {
                text.setText(originalValue);
                rectangle.setFill(Color.SADDLEBROWN);
            }

            event.consume();
        });

        setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != this &&
                    event.getDragboard().hasString() && text.getText().equals("")) {
                rectangle.setFill(Color.YELLOW);
            }
            event.consume();
        });

        setOnDragExited(event -> {
            if (text.getText().equals("")) {
                rectangle.setFill(Color.TAN);
            }
            else {
                rectangle.setFill(Color.SADDLEBROWN);
            }
            event.consume();
        });

        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            event.acceptTransferModes(TransferMode.MOVE);
            boolean success = false;
            if (db.hasString() && text.getText().equals("")) {
                text.setText(db.getString());
                rectangle.setFill(Color.SADDLEBROWN);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

}
