package lexical;

import lexical.token.Token;
import utils.SymbolTable;

public interface ILexicalScanner {
    void initKeywordTable();

    Token getNextToken();

    SymbolTable<String, Token> getKeywords();
}
