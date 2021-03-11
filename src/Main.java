import lexical.LexicalScanner;

public class Main {

    public static void main(String[] args) {
        //Reads filename and arguments
        Options options = new Options(args);

        //Will analyze the .asm for tokens
        LexicalScanner lexicalScanner = new LexicalScanner(options.getInputFile());

        //Using the lexical analyzer, parse them to generate a line of statements
        Parser parser = new Parser(lexicalScanner, lexicalScanner.getKeywords());
        parser.parseTokens();

        //Copy over the (IR?) sequential list of line statements to be processed
        //TODO: Uncomment after parser is fixed for sprint 3
        //CodeGenerator codeGen = new CodeGenerator(lexicalScanner, lexicalScanner.getKeywords(), options.getFileName(), parser.getIR());
        //codeGen.generateListing();

        System.out.println("Done creating TestImmediate.lst file.");
    }
}
