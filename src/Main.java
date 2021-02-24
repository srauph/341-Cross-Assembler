import lexical.Lexical;

import java.util.HashMap;

public class Main {


    public static void main(String[] args) {
        //Reads filename and arguments
        Options options = new Options(args);

        //Will analyze the .asm for tokens
        Lexical lexical = new Lexical(options.getInputFile());

        //Using the lexical analyzer, parse them to generate a line of statements
        Parser parser = new Parser(lexical, lexical.getKeywords());
        parser.parseTokens();

        //Copy over the (IR?) sequential list of line statements to be processed
        CodeGenerator codeGen = new CodeGenerator(lexical, lexical.getKeywords(), options.getFileName());
        codeGen.copyIR(parser.getIR());
        codeGen.generateListing();

        System.out.println("Done creating TestInherentMnemonics.lst file.");
    }
}
