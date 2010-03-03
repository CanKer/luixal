/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import pl21.Automata.AutomataFND;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class RegExpToAFND {

    // list of operators, ordered from lower to higher precedence:
    final List<String> operators = Arrays.asList("|","·","*","(",")");

    String regex;
    Stack<String> opStack;
    Stack<String> termStack;
    

    RegExpToAFND() {
        this.regex = "";
        this.opStack = new Stack<String>();
        this.termStack = new Stack<String>();
    }

    RegExpToAFND(String regex) {
        this.regex = regex;
        this.opStack = new Stack<String>();
        this.termStack = new Stack<String>();
    }

    public boolean hasHigherPriority(String opA, String opB) {
        return (this.operators.indexOf(opA) > this.operators.indexOf(opB));
    }

    // Generates a new AFND with just the term as a transition.
    public AutomataFND unaryOperation(String term) {
        AutomataFND afnd = new AutomataFND();
        afnd.addState("e0");
        afnd.addState("e1");
        afnd.setInitState("e0");
        afnd.setFinalState("e1");
        afnd.addTransition("e0", "e1", term);
        return afnd;
    }

    // Operates unary operations: *, +, ?
    public AutomataFND unaryOperation(AutomataFND term, String op) {
        AutomataFND result = new AutomataFND(term);
        if (op.equals("*")) {
            Integer prefinal = result.getNumberOfStates() - 1;
            result.addFinalState("e" + result.getNumberOfStates().toString());
            result.addTransition("e" + prefinal, result.getFinalState(), "#");
            result.addTransition(result.getInitState(), result.getFinalState(), "#");
            result.addTransition(result.getFinalState(), result.getInitState(), "#");
        }
        if (op.equals("+")) {
            Integer prefinal = result.getNumberOfStates() - 1;
            result.addFinalState("e" + result.getNumberOfStates().toString());
            result.addTransition("e" + prefinal, result.getFinalState(), "#");
            result.addTransition(result.getFinalState(), result.getInitState(), "#");
        }
        if (op.equals("?")) {
            Integer prefinal = result.getNumberOfStates() - 1;
            result.addFinalState("e" + result.getNumberOfStates().toString());
            result.addTransition("e" + prefinal, result.getFinalState(), "#");
            result.addTransition(result.getInitState(), result.getFinalState(), "#");
        }
        return result;
    }

    // Operates binary operations: |, ·
    public void binaryOperation(String termA, String termB, String op) {
    }

    public void TwoStacksAlgorithm(String regex) {
        // init variables (just in case):
        this.regex = regex;
        this.opStack = new Stack<String>();
        this.termStack = new Stack<String>();
        // calling algorithm:
        this.TwoStacksAlgorithm();
    }

    public void TwoStacksAlgorithm() {
    }

    public static void main(String args[]) {
        RegExpToAFND test = new RegExpToAFND();
        AutomataFND autoA = test.unaryOperation("a");
        autoA.setId("A");
        AutomataFND autoB = test.unaryOperation("b");
        autoB.setId("B");
        System.out.println(autoA);
        System.out.println(autoB);
        AutomataFND result = test.unaryOperation(autoA, "?");
        result.setId("R");
        System.out.println(result);
    }
}
