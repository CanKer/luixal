/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author nemesis
 */
public abstract class Automata {

    protected String id;              // Automata's IDentificator
    protected HashSet states;         // state's container
    protected HashMap transitions;    // transition's container (key = origin state, value = transition/s, depending on Automata's condition)
}
