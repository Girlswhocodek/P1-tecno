/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interpreter.analex.interfaces;

import interpreter.events.TokenEvent;

/**
 *
 * @author dell
 */
public interface ITokenEventListener {
    
    void help(TokenEvent event);   
    void cliente(TokenEvent event); 
    void proveedor(TokenEvent event);    
    void producto(TokenEvent event);
    void ingreso(TokenEvent event);
    void detalleingreso(TokenEvent event);
    void orden(TokenEvent event);
    void detalleorden(TokenEvent event);
    void usuario(TokenEvent event);
    void error(TokenEvent event);
    void categoria(TokenEvent event);
    void estado(TokenEvent event);
    void pago(TokenEvent event);
    //agregar mas casos de uso
}
