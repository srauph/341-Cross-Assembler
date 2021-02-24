import lexical.Lexical;
import lexical.token.Mnemonic;
import lexical.token.Token;
import utils.StringUtils;
import utils.SymbolTable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class CodeGenerator {
    private final LinkedList<LineStatement> intermediateRep = new LinkedList<>();
    private final Lexical lexical;
    private final SymbolTable<String, Token> keyword;
    // private final SymbolTable<??, ??> labels;  //future use in resolving labels during code generation
    private final String fileName;

    public CodeGenerator(Lexical lexical, SymbolTable<String, Token> keyword, String fileName) {
        this.lexical = lexical;
        this.fileName = fileName;
        this.keyword = keyword;
    }

    public void copyIR(LinkedList<LineStatement> ir) {
        this.intermediateRep.addAll(ir);
    }

    public void generateListing() {
        StringBuilder lst = new StringBuilder();
        lst.append("Line Addr Code          Label         Mne   Operand       Comments\r\n");
        int linePosition = 1;
        int addr = 0;
        for (LineStatement ls : intermediateRep) {
            if (ls == null) {
                //Error, should not have a null line statement though
                continue;
            }
            //Begin Generating Opening of line statement
            //Line
            lst.append(StringUtils.getCustomFormat(5, linePosition));
            ++linePosition;

            //Addr
            lst.append(StringUtils.getCustomFormat(5, StringUtils.getHexFromDecimal(addr, 4, false)));
            addr++;

            //Code
            Mnemonic mne = (Mnemonic) keyword.get(ls.getMnemonic().getName());
            //Temp mne null check, will function much smarter later on

            lst.append(StringUtils.getCustomFormat(4, StringUtils.getHexFromDecimal(mne == null ? -1 : mne.getOpCode(), 2, false)));
            //End Generating Opening of line statement

            lst.append(StringUtils.getCustomFormat(10, " ")); // Padding between Code and Mne
            //Begin Generating Closing of line statement
            //Label
            lst.append(StringUtils.getCustomFormat(5, " "));

            lst.append(StringUtils.getCustomFormat(9, " "));// Padding between Label and Mne
            //Mne
            lst.append(StringUtils.getCustomFormat(6, ls.getMnemonic().getName()));
            //Operand
            //Comments
            //End Generating Closing of line statement
            lst.append("\r\n");
        }

        try {
            FileOutputStream out = new FileOutputStream(fileName + ".lst");
            out.write(lst.toString().getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
