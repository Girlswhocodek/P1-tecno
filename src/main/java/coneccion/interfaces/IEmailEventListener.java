/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package coneccion.interfaces;

import coneccion.utils.Email;
import java.util.List;

/**
 *
 * @author dell
 */
public interface IEmailEventListener {
    void onReceiveEmailEvent(List<Email> emails);
}
