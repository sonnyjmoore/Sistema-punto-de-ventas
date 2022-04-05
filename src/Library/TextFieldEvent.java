/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Library;

import java.awt.event.KeyEvent;

/**
 *
 * @author Andres Del Rio
 */
public class TextFieldEvent {
    public void textKeyPress(KeyEvent evt){
        char car = evt.getKeyChar();
        if ((car < 'a'|| car > 'z')&& (car < 'A' || car > 'Z') && 
                (car != (char)KeyEvent.VK_BACK_SPACE) && (car !=(char) KeyEvent.VK_SPACE)){ //si es menor (valido) se va a ejecutar la condicion 
            evt.consume();
        } else {
        }
        
    }
}
