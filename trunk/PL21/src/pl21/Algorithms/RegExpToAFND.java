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
    final List<String> operators = Arrays.asList("|","·","*", "+", "?","(",")");
    final List<String> unitaryOperators = Arrays.asList("*","+","?");
    final List<String> binaryOperators = Arrays.asList("|","·");

    String regex;
    Stack<String> opStack;
    Stack<AutomataFND> termStack;
    

    RegExpToAFND() {
        this.regex = "";
        this.opStack = new Stack<String>();
        this.termStack = new Stack<AutomataFND>();
    }

    RegExpToAFND(String regex) {
        this.regex = regex;
        this.opStack = new Stack<String>();
        this.termStack = new Stack<AutomataFND>();
    }

    public boolean hasHigherPriority(String opA, String opB) {
        return ((this.operators.indexOf(opA) > this.operators.indexOf(opB)));
    }

    public boolean isOperator(String op) {
        return this.operators.contains(op);
    }
    
    public boolean isUnaryOperator(String op) {
        return this.unitaryOperators.contains(op);
    }

    public boolean isBinaryOperator(String op) {
        return this.binaryOperators.contains(op);
    }

    // Generates a new AFND with just the term as a transition.
    public AutomataFND operate(String term) {
        AutomataFND afnd = new AutomataFND();
        afnd.addState("e0");
        afnd.addState("e1");
        afnd.setInitState("e0");
        afnd.setFinalState("e1");
        afnd.addTransition("e0", "e1", term);
        return afnd;
    }

    // Operates unary operations: *, +, ?
    public AutomataFND operate(AutomataFND term, String op) {
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
    public AutomataFND operate(AutomataFND termA, AutomataFND termB, String op) {
        // creating and initializing a new AFND for the result:
        AutomataFND result = new AutomataFND();
        termA.renameStates(0);
        termB.renameStates(termA.getNumberOfStates());
        result.addAutomataFND(termA);
        result.addAutomataFND(termB);
        // getting previously initial and final states:
        String preA = termA.getInitState();
        String preB = termB.getInitState();
        String finA = termA.getFinalState();
        String finB = termB.getFinalState();

        if (op.equals("|")) {
            // adding states needed (as initial and final):
            result.addInitState("e" + result.getNumberOfStates());
            result.addFinalState("e" + result.getNumberOfStates());
            // adding needed transitions:
            result.addTransition(result.getInitState(), preA, "#");
            result.addTransition(result.getInitState(), preB, "#");
            result.addTransition(finA, result.getFinalState(), "#");
            result.addTransition(finB, result.getFinalState(), "#");
        }
        if (op.equals("·")) {
            // no states needed to be added here! just little changes:
            result.setInitState(preA);
            result.setFinalState(finB);
            // adding needed transition:
            result.addTransition(finA, preB, "#");
        }

        return result;
    }

    public AutomataFND TwoStacksAlgorithm(String regex) {
        // init variables (just in case):
        this.regex = regex;
        this.opStack = new Stack<String>();
        this.termStack = new Stack<AutomataFND>();
        // calling algorithm:
        return this.TwoStacksAlgorithm();
    }

    public AutomataFND TwoStacksAlgorithm() {
        for (int i = 0; i < this.regex.length(); i++) {
            String input = String.valueOf(this.regex.charAt(i));
            if (this.isOperator(input)) {
                // stuff for operators...
                if (this.opStack.isEmpty()) {
                    // if the operator's stack is empty, just push the operator:
                    this.opStack.push(input);
                } else {
                    // if the operator's stack is not empty, check the priority for this and the top of the stack:
                    if (this.hasHigherPriority(input, this.opStack.peek())) {
                        // if input has higher priority, just push the operator:
                        this.opStack.push(input);
                    } else {
                        // if aux has lower priority, operate the top of the stack:
                        String aux = this.opStack.pop();
                        this.opStack.push(input);
                        if (this.isUnaryOperator(aux)) {
                            // pop term, operate and push term:
                            this.termStack.push(this.operate(this.termStack.pop(), aux));
                        } else {
                            // pop term, pop term, operate and push term:
                            AutomataFND afndB = this.termStack.pop();
                            AutomataFND afndA = this.termStack.pop();
                            this.termStack.push(this.operate(afndA, afndB, aux));
                        }
                    }
                }
            } else {
                // stuff for terminals...
                // so far let's put it in the terminals stack:
                this.termStack.push(this.operate(input));
            }
        }

        if (this.opStack.isEmpty()) {
            return this.termStack.pop();
        } else {
            if (this.opStack.size() == 1) {
                if (this.isUnaryOperator(this.opStack.peek())) {
                    this.termStack.push(this.operate(this.termStack.pop(), this.opStack.pop()));
                } else {
                    AutomataFND afndB = this.termStack.pop();
                    AutomataFND afndA = this.termStack.pop();
                    this.termStack.push(this.operate(afndA, afndB, this.opStack.pop()));
                }
                return this.termStack.pop();
            } else {
                System.out.println("ERROR!! :: TwoStacksAlgorithm");
                System.out.println("opStack: " + this.opStack.toString());
                System.out.println("termStack: " + this.termStack.toString());
                return new AutomataFND();
            }
        }
    }

    public static void main(String args[]) {
        RegExpToAFND test = new RegExpToAFND();
//        AutomataFND autoA = test.operate("a");
//        autoA.setId("A");
//        AutomataFND autoB = test.operate("b");
//        autoB.setId("B");
//        System.out.println(autoA);
//        System.out.println(autoB);
//        AutomataFND result = test.operate(autoA, autoB, "·");
//        result.setId("R");
//        System.out.println(result);
        AutomataFND result = test.TwoStacksAlgorithm("a·b|c");
        System.out.println("RESULT:\n" + result);
    }
}
