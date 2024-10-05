/*
 * @ (#) AutoCompleteComboBoxListener.java       1.0     05/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   05/10/2024
 * version: 1.0
 */
public class AutoCompleteComboBoxListener<T> implements EventHandler<javafx.scene.input.KeyEvent> {

    private ComboBox comboBox;
    private StringBuilder sb;
    private ObservableList<T> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public AutoCompleteComboBoxListener(final ComboBox comboBox) {
        this.comboBox = comboBox;
        sb = new StringBuilder();
        data = comboBox.getItems();

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {

            @Override
            public void handle(javafx.scene.input.KeyEvent keyEvent) {
                comboBox.hide();
            }
        });
        this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
    }

    @Override
    public void handle(javafx.scene.input.KeyEvent event) {

        if(event.getSource() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getSource() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getSource() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getSource() == KeyCode.DELETE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }

        if (event.getSource() == KeyCode.RIGHT || event.getSource() == KeyCode.LEFT
                || event.isControlDown() || event.getSource() == KeyCode.HOME
                || event.getSource() == KeyCode.END || event.getSource() == KeyCode.TAB) {
            return;
        }

        ObservableList list = FXCollections.observableArrayList();
        for (int i=0; i<data.size(); i++) {
            if(data.get(i).toString().toLowerCase().startsWith(
                    AutoCompleteComboBoxListener.this.comboBox
                            .getEditor().getText().toLowerCase())) {
                list.add(data.get(i));
            }
        }
        String t = comboBox.getEditor().getText();

        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            comboBox.show();
        }
    }

    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }

}
