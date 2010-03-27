/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.InputOutput;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author nemesis
 */
public class Output {

    String filename;
    BufferedWriter obw;

    String separatorChar = "*";

    public Output(String filename) throws IOException {
        this.filename = filename;
        this.obw = new BufferedWriter(new FileWriter(this.filename));
    }

    public void appendSeparator() throws IOException {
        String aux = this.separatorChar;
        for (int i = 0; i < 200; i++) {
            aux += this.separatorChar;
        }
        this.write(aux);
    }

    public void write(String string) throws IOException {
        this.obw.append(string + "\n");
        this.obw.flush();
    }

    public static void main(String[] args) throws IOException {
        Output out = new Output("newfile");
        out.write("mmmmuuuuaHAHAHAHAHAHAHHAAHAHAHHAAAAAA!!!!\nasdfklñjasdkfljahdklfjh\n\n");
        out.appendSeparator();
        out.write("asñdlfkajsñdlfkjslñdkfjaslñjk");
        out.appendSeparator();
    }
}
