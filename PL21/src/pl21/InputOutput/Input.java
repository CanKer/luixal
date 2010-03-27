/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.InputOutput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author nemesis
 */
public class Input {

    String filename;
    BufferedReader ibr;

    public Input(String filename) throws FileNotFoundException, IOException {
        this.filename = filename;
        this.ibr = new BufferedReader(new FileReader(this.filename));
        this.ibr.mark(500);
    }

    public ArrayList<String> readAllFile() throws IOException {
        ArrayList<String> result = new ArrayList();
        String aux = this.ibr.readLine();
        while (aux != null) {
            result.add(aux);
            aux = this.ibr.readLine();
        }
        return result;
    }

    public String readFirstLine() throws IOException {
        return this.ibr.readLine();
    }

    public void reset() throws FileNotFoundException {
        try {
            this.ibr.reset();
        } catch (IOException ex) {
            this.ibr = new BufferedReader(new FileReader(this.filename));
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Input input = new Input("jejefile");
        System.out.println(input.readAllFile());
    }
}
