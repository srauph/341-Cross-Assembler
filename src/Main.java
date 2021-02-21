import lexical.Lexical;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        //Temp way to get file  name, will eventually read from arguments
        String inputFile = "TestInherentMnemonics.asm";
        String fileName = inputFile.replace(".asm", "");


        HashMap<String, Integer> keyWordTable;
        //Will analyze the .asm for tokens
        Lexical lexical = new Lexical(inputFile);

        //Using the lexical analyzer, parse them to generate a line of statements
        Parser parser = new Parser(lexical, lexical.getKeywords());
        parser.parseTokens();

        //Copy over the (IR?) sequential list of line statements to be processed
        CodeGenerator codeGen = new CodeGenerator(lexical, lexical.getKeywords(), fileName);
        codeGen.copyIR(parser.getIR());
        codeGen.generateListing();
    }
}
