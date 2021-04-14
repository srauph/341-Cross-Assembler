import lexical.LexicalScanner;

public class CrossAssembler {

    public static void main(String[] args) {
        //Reads filename and arguments
        Options options = new Options(args);

        //Will analyze the .asm for tokens
        LexicalScanner lexicalScanner = new LexicalScanner(options.getInputFile());

        //Using the lexical analyzer, parse them to generate a line of statements
        ErrorReporter errorReporter = new ErrorReporter(options);
        Parser parser = new Parser(lexicalScanner, lexicalScanner.getKeywords(), errorReporter);
        parser.parseTokens();
     //   errorReporter.checkReports();

        //Copy over the (IR?) sequential list of line statements to be processed
        //TODO: Uncomment after parser is fixed for sprint 3 <-- Uncommented! :D
        CodeGenerator codeGen = new CodeGenerator(lexicalScanner, lexicalScanner.getKeywords(), options.getFileName(), parser.getIR());
        codeGen.generateListing();

        System.out.println("Done creating TestImmediate.lst file.");
    }
}
