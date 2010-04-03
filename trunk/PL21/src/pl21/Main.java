/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import pl21.Algorithms.AFDtoAFDmin;
import pl21.Algorithms.AFNDtoAFD;
import pl21.Algorithms.RegExpToAFND;
import pl21.Automata.AutomataFD;
import pl21.Automata.AutomataFND;
import pl21.InputOutput.ImageOutput;
import pl21.InputOutput.Input;
import pl21.InputOutput.Output;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class Main {

    static String input;
    static String output;
    static HashMap<String, String> REs;
    // Automatas:
    static ArrayList<AutomataFND> automatasFND;
    static ArrayList<AutomataFD> automatasFD;

    public static boolean processArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("--i")) {
                input = args[i+1];
            }
            if (args[i].equalsIgnoreCase("--o")) {
                output = args[i+1];
            }
        }
        return (input != null && output != null);
    }

    public static void getInputREs(String filename) throws FileNotFoundException, IOException {
        REs = new HashMap<String, String>();
        Input in = new Input(input);
        ArrayList<String> aux = in.readAllFile();
        
        for (String s:aux) {
            System.out.println("\n- " + s);
            StringTokenizer st = new StringTokenizer(s, "=");
            REs.put(st.nextToken(), st.nextToken());
        }
    }

    public static void generateAFNDs() {
        automatasFND = new ArrayList<AutomataFND>();
        for (String s:REs.keySet()) {
            RegExpToAFND alg = new RegExpToAFND(REs.get(s));
            System.out.print("\tGenerating AFND for '" + s + "'... ");
            AutomataFND afnd = new AutomataFND(alg.TwoStacksAlgorithm());
            System.out.println("DONE!");
            afnd.setId(s);
            automatasFND.add(afnd);
        }
    }

    public static void generateAFDs() {
        automatasFD = new ArrayList<AutomataFD>();
        for (AutomataFND afnd:automatasFND) {
            AFNDtoAFD afndtoafd = new AFNDtoAFD(afnd);
            System.out.print("\tGenerating AFND for '" + afnd.getId() + "'... ");
            afndtoafd.conversion();
            AutomataFD afd = new AutomataFD(afndtoafd.getAFD());
            afd.renameStates(0);
            System.out.print(" minimizing... ");
            AFDtoAFDmin minimizer = new AFDtoAFDmin(afd);
            minimizer.minimize();
            minimizer.buildNewAFD();
            minimizer.getAFD().renameStates(0);
            minimizer.getAFD().setId(afnd.getId());
            System.out.println("DONE!");
            automatasFD.add(minimizer.getAFD());
        }
    }

    public static void writeOutput(boolean writeImages) throws IOException {
        Output out = new Output(output);
        out.write("Regular Expressions:");
        out.appendSeparator();
        for (String s:REs.keySet()) {
            out.write(s + " =" + REs.get(s));
        }
        out.appendSeparator();
        out.appendSeparator();
        out.write("\nAutomatas AFNDs:");
        out.appendSeparator();
        for (AutomataFND a:automatasFND) {
            out.write(a.toString());
            out.appendSeparator();
            if (writeImages) {
                ImageOutput io = new ImageOutput(a, "AFND - " + a.getId() + ".png");
                io.writeFile();
            }
        }
        out.appendSeparator();
        out.write("\nAutomatas AFDs:");
        out.appendSeparator();
        for (AutomataFD a:automatasFD) {
            out.write(a.toString());
            out.appendSeparator();
            if (writeImages) {
                ImageOutput io = new ImageOutput(a, "AFD - " + a.getId() + ".png");
                System.out.println(io.convertGraphToDotFormat());
                io.writeFile();
            }
        }
    }

    public static AutomataFND buildNewAFND() {
        AutomataFND afnd = new AutomataFND("AFND - Resultant AFND");
        // adding new initial state:
        String ni = "Ei";
        String nf = "Ef";
        afnd.addInitState(ni);
        afnd.addFinalState(nf);
        afnd.renameStates(0);
        // adding AFDs to the new AFND:
        for (AutomataFD afd:automatasFD) {
            afnd.addAutomataFD(afd, afnd.getInitState(), "#");
        }
        // returning the new AFND:
        return afnd;
    }

    /**
     * 
     * @param afnd
     * @return
     */
    public static AutomataFD buildNewAFD(AutomataFND afnd) {
        AFNDtoAFD conversor = new AFNDtoAFD(afnd);
        conversor.conversion();
        return conversor.getAFD();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("UEM - Language Processors 2.1 - Luis Alberto Pérez García - 19903316");
        System.out.println("--------------------------------------------------------------------\n");
//        if (!processArgs(args)) {
//            BufferedReader keybInput = new BufferedReader(new InputStreamReader(System.in));
//            System.out.print("Input file (RE's file): ");
//            input = keybInput.readLine();
//            System.out.print("Output file (Automata's file): ");
//            output = keybInput.readLine();
//        }
        input = "resfile2";
        output = "outfile";

        System.out.println("\nReading REs from input file... ");
        getInputREs(input);
        System.out.println("\nGenerating AFNDs from REs... ");
        generateAFNDs();
        System.out.println("\nGenerating AFDs from AFNDs... ");
        generateAFDs();
        System.out.println("\nWriting output... ");
        writeOutput(true);
        AutomataFND afnd = new AutomataFND(buildNewAFND());
        System.out.println("\n\n///////////////\n" + afnd.toString());
        ImageOutput io = new ImageOutput(afnd, "AFND - " + afnd.getId() + ".png");
        io.writeFile();
        AutomataFD afd = new AutomataFD(buildNewAFD(afnd));
        AFDtoAFDmin minimizer = new AFDtoAFDmin(afd);
        minimizer.minimize();
        minimizer.buildNewAFD();
        afd = new AutomataFD(minimizer.getAFD());
        ImageOutput io2 = new ImageOutput(afd, "AFD - " + afd.getId() + ".png");
        io2.writeFile();
        System.out.println("\n\n");
        System.out.println("Is the input 'B' valid? " + afd.isValid("B"));
        System.out.println("Is the input 'C' valid? " + afd.isValid("C"));
        System.out.println("Is the input 'A' valid? " + afd.isValid("A"));
        System.out.println("Is the input 'BC' valid? " + afd.isValid("BC"));
        System.out.println("Is the input 'ABC' valid? " + afd.isValid("ABC"));
        System.out.println("Is the input 'AB' valid? " + afd.isValid("AB"));
        System.out.println("Is the input 'ABABABAB' valid? " + afd.isValid("ABABABAB"));
        System.out.println("Is the input 'ABABABABC' valid? " + afd.isValid("ABABABABC"));
    }

}
