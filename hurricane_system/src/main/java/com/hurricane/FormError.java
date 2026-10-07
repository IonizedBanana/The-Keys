package com.hurricane;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 * Shows form errors: a banner with the message, and a red outline on the
 * fields that need fixing. A field's outline clears as soon as the user types in it.
 *
 * <p>The {@code :error} pseudo-class is set on the field and on the
 * {@code .field-icon} beside it in the same parent, so the CSS can color both.
 * (Selecting on the parent instead, like {@code .input:error .text-field},
 * trips a JavaFX bug that drops the text colors of unfocused fields.)</p>
 */
final class FormError {

    private static final PseudoClass ERROR = PseudoClass.getPseudoClass("error");
    private static final Duration SHOW_TIME = Duration.millis(220);

    private FormError() {
    }

    /**
     * Shows an error message, marks the given fields, and moves focus to the first one.
     *
     * @param banner  the label that shows the message
     * @param message what went wrong
     * @param fields  the fields to mark; the first one gets focus
     */
    static void show(Label banner, String message, TextField... fields) {
        banner.setText(message);
        banner.setManaged(true);
        banner.setVisible(true);
        playShow(banner);

        for (TextField field : fields) {
            mark(field);
        }
        if (fields.length > 0) {
            fields[0].requestFocus();
        }
    }

    /**
     * Hides the banner and clears every field's error outline.
     *
     * @param banner the label that shows the message
     * @param fields the fields to clear
     */
    static void clear(Label banner, TextField... fields) {
        banner.setText("");
        banner.setManaged(false);
        banner.setVisible(false);
        for (TextField field : fields) {
            setError(field, false);
        }
    }

    /** Marks a field, and clears the mark the first time the user edits it. */
    private static void mark(TextField field) {
        setError(field, true);
        ChangeListener<String> clearOnEdit = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldText, String newText) {
                setError(field, false);
                field.textProperty().removeListener(this);
            }
        };
        field.textProperty().addListener(clearOnEdit);
    }

    private static void setError(TextField field, boolean error) {
        field.pseudoClassStateChanged(ERROR, error);
        for (Node icon : field.getParent().lookupAll(".field-icon")) {
            icon.pseudoClassStateChanged(ERROR, error);
        }
    }

    /** Fades the banner in and drops it into place, so a repeated error is still noticed. */
    private static void playShow(Label banner) {
        FadeTransition fade = new FadeTransition(SHOW_TIME, banner);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition drop = new TranslateTransition(SHOW_TIME, banner);
        drop.setFromY(-6);
        drop.setToY(0);
        drop.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(fade, drop).play();
    }
}
