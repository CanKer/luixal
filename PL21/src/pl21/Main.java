/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    // Token priority:
    static ArrayList<String> tokens;

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
        tokens = new ArrayList<String>();
        Input in = new Input(input);
        ArrayList<String> aux = in.readAllFile();

        System.out.println();
        for (String s:aux) {
            System.out.println("- " + s);
            StringTokenizer st = new StringTokenizer(s, "=");
            String token = st.nextToken();
            REs.put(token, st.nextToken());
            tokens.add(token);
        }
    }

    public static String getPriorityER(String ERs) {
        StringTokenizer st = new StringTokenizer(ERs, ",");
        Integer index = tokens.size() + 1;

        while (st.hasMoreTokens()) {
            Integer auxIndex = tokens.size() + 1;
            String aux = st.nextToken();
            if (tokens.contains(aux)) auxIndex = tokens.indexOf(aux);
            if (auxIndex < index) index = auxIndex;
        }

        return tokens.get(index);
    }

    public static void generateAFNDs() {
        automatasFND = new ArrayList<AutomataFND>();
        for (String s:REs.keySet()) {
            RegExpToAFND alg = new RegExpToAFND(REs.get(s));
            System.out.print("\tGenerating AFND for '" + s + "'... ");
            AutomataFND afnd = new AutomataFND(alg.TwoStacksAlgorithm());
            System.out.println("DONE!");
            afnd.setId(s);
            afnd.getFinalStates().put(afnd.getFinalState(), s); // setting the token for the finalState
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
            minimizer.getAFD().getFinalStates().put(minimizer.getAFD().getFinalState(), afnd.getId());
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
//                System.out.println(io.convertGraphToDotFormat());
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

    /*
     * Reads an input file for analyze with the generated AFDmin.
     *
     * @param filename Name of the file to analyze.
     */
    public static ArrayList<String> readFileForAnalyze(String filename) throws IOException {
        BufferedReader ibr = new BufferedReader(new FileReader(filename));
//        HashMap<String, String> result = new HashMap<String, String>();
        ArrayList<String> result = new ArrayList<String>();
//        Integer counter = 0;
        String aux = ibr.readLine();
        while (aux != null) {
//            result.put(counter.toString(), aux);
            result.add(aux);
            aux = ibr.readLine();
//            counter++;
        }
        return result;
    }

    public static String AnalyzeInputFile(String filename, AutomataFD afd) throws IOException {
        String result = "";
        ArrayList<String> entries = readFileForAnalyze(filename);
        // analyzing lines:
        for (int i = 0; i < entries.size(); i++) {
            Integer counterH = 0;
            StringTokenizer words = new StringTokenizer(entries.get(i));
            while (words.hasMoreTokens()) {
                String word = words.nextToken();
                if (afd.isValid(word)) {
                    result +="Found " + word + " at (" + i + "," + counterH + ") with token '" + getPriorityER(afd.getFinalStates().get(afd.getValidState(word))) + "'.\n";
                }
                counterH += word.length() + 1;
            }
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("UEM - Language Processors 2.4 - Luis Alberto Pérez García - 19903316");
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
        ImageOutput io = new ImageOutput(afnd, afnd.getId() + ".png");
        io.writeFile();
        System.out.println("AFND " + afnd.getId() + "\n" + afnd);
        AutomataFD afd = new AutomataFD(buildNewAFD(afnd));
        io = new ImageOutput(afd, "AFD - Resultant - NO Minimizado.png");
        io.writeFile();
        System.out.println("AFD Resultant NO Minimizado:" + "\n" + afd);
        System.out.println("TOKENS SIN MINIMIZAR: " + afd.getFinalStates());
        AFDtoAFDmin minimizer = new AFDtoAFDmin(afd);
        minimizer.minimize();
        minimizer.buildNewAFD();
        afd = new AutomataFD(minimizer.getAFD());
        System.out.println("TOKENS TRAS MINIMIZAR: " + afd.getFinalStates());
        afd.setId("AFD Resultant Minimized");
        ImageOutput io2 = new ImageOutput(afd, afd.getId() + ".png");
        io2.writeFile();
        System.out.println("AFD " + afnd.getId() + "\n" + afd);

        System.out.println("Tokens ordered: " + tokens);
//        String inputa = "aaaaaabcF";
//        System.out.print("Is the input '" + inputa + "' valid? ");
//        if (afd.isValid(inputa)) {
//            System.out.println("Yeah, token: " + getPriorityER(afd.getFinalStates().get(afd.getValidState(inputa))));
//        } else {
//            System.out.println("Noup!");
//        }
//        System.out.println("Is the input 'C' valid? " + afd.isValid("C"));
//        System.out.println("Is the input 'A' valid? " + afd.isValid("A"));
//        System.out.println("Is the input 'BC' valid? " + afd.isValid("BC"));
//        System.out.println("Is the input 'ABC' valid? " + afd.isValid("ABC"));
//        System.out.println("Is the input 'AB' valid? " + afd.isValid("AB"));
//        System.out.println("Is the input 'ABABABAB' valid? " + afd.isValid("ABABABAB"));
//        System.out.println("Is the input 'ABABABABC' valid? " + afd.isValid("ABABABABC"));
        System.out.println("\nAnalysis for input file (input.lex):\n" + AnalyzeInputFile("input.lex", afd));
    }

}
