package examples;

import java.io.*;

public class EasyIO {
    private final BufferedReader input;
    private final PrintStream output;

    public EasyIO(InputStream input, PrintStream output) {
        this.input = new BufferedReader(new InputStreamReader(input));
        this.output = output;
    }

    public void greet() throws IOException {
        output.println("What is your name?");
        String name = input.readLine();
        output.println("Hello, " + name);
    }

    public static void main(String[] args) throws IOException {
        EasyIO io = new EasyIO(System.in, System.out);
        io.greet();
    }
}
