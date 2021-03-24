import errorhandling.ErrorReporter;
import lexical.LexicalScanner;
import options.Options;
import parser.Parser;

public class Main {

    public static void main(String[] args) {
        //Reads filename and arguments
        Options options = new Options(args);

        // Create error reporter
        ErrorReporter errorReporter = new ErrorReporter(options);

        //Will analyze the .asm for tokens
        LexicalScanner lexicalScanner = new LexicalScanner(options.getInputFile());

        //Using the lexical analyzer, parse them to generate a line of statements

        Parser parser = new Parser(lexicalScanner, lexicalScanner.getKeywords(), errorReporter);
        parser.parseTokens();
        errorReporter.report();

        //Copy over the (IR?) sequential list of line statements to be processed
        //TODO: Uncomment after parser is fixed for sprint 3
        //codeGenerator.CodeGenerator codeGen = new codeGenerator.CodeGenerator(lexicalScanner, lexicalScanner.getKeywords(), options.getFileName(), parser.getIR());
        //codeGen.generateListing();

        System.out.println("Done creating TestImmediate.lst file.");
    }
}
