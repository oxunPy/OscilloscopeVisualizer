import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class FancyScrollPane extends Application {

    @Override
    public void start(Stage primaryStage) {
        ScrollPane scrollPane = new ScrollPane();
        Pane content = new Pane();
        scrollPane.setContent(content);

        // adding background
        content.getChildren().add(new Rectangle(500, 500, Color.GREEN));

        Circle immovableObject = new Circle(30, Color.RED);
        content.getChildren().add(immovableObject);

        primaryStage.setScene(new Scene(scrollPane, 300, 300));
        primaryStage.show();

        immovableObject.layoutXProperty().bind(
                scrollPane.hvalueProperty()
                        .multiply(
                                content.widthProperty()
                                        .subtract(
                                                new ScrollPaneViewPortWidthBinding(scrollPane))));
    }

    // we need this class because Bounds object doesn't support binding
    private static class ScrollPaneViewPortHeightBinding extends DoubleBinding {

        private final ScrollPane root;

        public ScrollPaneViewPortHeightBinding(ScrollPane root) {
            this.root = root;
            super.bind(root.viewportBoundsProperty());
        }

        @Override
        protected double computeValue() {
            return root.getViewportBounds().getHeight();
        }
    }
    // we need this class because Bounds object doesn't support binding
    private static class ScrollPaneViewPortWidthBinding extends DoubleBinding {

        private final ScrollPane root;

        public ScrollPaneViewPortWidthBinding(ScrollPane root) {
            this.root = root;
            super.bind(root.viewportBoundsProperty());
        }

        @Override
        protected double computeValue() {
            return root.getViewportBounds().getWidth();
        }
    }
    public static void main(String[] args) { launch(); }
}