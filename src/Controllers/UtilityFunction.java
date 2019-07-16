/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

/**
 *
 * @author Ananda Mohon Ghosh
 */
public class UtilityFunction {
    
    public  int max(int a, int b) {
        return a > b ? a : b;
    }

    public  int min(int a, int b) {
        return a > b ? b : a;
    }

    public  int abs(int a) {
        return a > 0 ? a : -a;
    }
}