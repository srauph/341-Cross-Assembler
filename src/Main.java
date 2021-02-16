import lexical.Lexical;
import lexical.Token;

public class Main {

    public static void main(String[] args) {
        //Temp way to get file  name, will eventually read from arguments
        String fileName = "TestInherentMnemonics.asm";

        //Will analyze the .asm for tokens
        Lexical lexical = new Lexical(fileName);

    }
}
