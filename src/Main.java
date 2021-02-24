import lexical.Scanner;

public class Main {


    public static void main(String[] args) {
        //Temp way to get file  name, will eventually read from arguments
        String inputFile = "TestInherentMnemonics.asm";
        String fileName = inputFile.replace(".asm", "");


        //Will analyze the .asm for tokens
        Scanner scanner = new Scanner(inputFile);

        //Using the lexical analyzer, parse them to generate a line of statements
        Parser parser = new Parser(scanner, scanner.getKeywords());
        parser.parseTokens();

        //Copy over the (IR?) sequential list of line statements to be processed
        CodeGenerator codeGen = new CodeGenerator(scanner, scanner.getKeywords(), fileName);
        codeGen.copyIR(parser.getIR());
        codeGen.generateListing();

        System.out.println("Done creating TestInherentMnemonics.lst file.");
    }
}
