package com.dbdeploy.database;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.util.List;

public class QueryStatementSplitter {

    public List<String> split(String input) {
        ANTLRStringStream in = new ANTLRStringStream(input);
        SQLLexer lexer = new SQLLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SQLParser parser = new SQLParser(tokens);
        try {
            return parser.parse();
        } catch (RecognitionException e) {
            throw new IllegalArgumentException("Invalid sql input: " + input);
        }
    }

    public void setDelimiter(String delimiter) {

    }

    public void setDelimiterType(DelimiterType delimiterType) {

    }

	public void setOutputLineEnding(LineEnding lineEnding) {

	}
}
