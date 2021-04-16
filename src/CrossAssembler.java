import errorhandling.ErrorReporter;
import lexical.LexicalScanner;
import options.Options;
import parser.Parser;
import codegen.CodeGenerator;

public class CrossAssembler {

    public static void main(String[] args) {
        //Reads filename and arguments
        Options options = new Options(args);

        // Create error reporter
        ErrorReporter errorReporter = new ErrorReporter(options);

        //Will analyze the .asm for tokens
        LexicalScanner lexicalScanner = new LexicalScanner(options.getInputFile());

        //Using the lexical analyzer, parse them to generate a line of statements

        Parser parser = new Parser(lexicalScanner, lexicalScanner.getKeywords(), options.getVerbose(), errorReporter);
        parser.parseTokens();
        errorReporter.checkReports();

        //Copy over the (IR?) sequential list of line statements to be processed
        CodeGenerator codeGen = new CodeGenerator(lexicalScanner, lexicalScanner.getKeywords(), options.getFileName(), parser.getIR(), options.getVerbose(), options.getListing());
        codeGen.generateListing();

    }
}
