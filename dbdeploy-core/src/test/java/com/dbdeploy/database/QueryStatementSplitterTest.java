package com.dbdeploy.database;

import org.apache.commons.lang.SystemUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class QueryStatementSplitterTest {
    private QueryStatementSplitter splitter;

    @Before
    public void setUp() throws Exception {
        splitter = new QueryStatementSplitter();
    }


    @Test
    public void shouldNotSplitStatementsThatHaveNoDelimter() throws Exception {
        List<String> result = splitter.split("SELECT 1");
        assertThat(result, hasItem("SELECT 1"));
        assertThat(result.size(), is(1));
    }

    @Test
    public void shouldIgnoreSemicolonsInTheMiddleOfALine() throws Exception {
        List<String> result = splitter.split("SELECT ';'");
        assertThat(result, hasItem("SELECT ';'"));
        assertThat(result.size(), is(1));
    }

    @Test
    public void shouldSplitStatementsOnASemicolonAtTheEndOfALine() throws Exception {
        List<String> result = splitter.split("SELECT 1;\nSELECT 2;");
        assertThat(result, hasItems("SELECT 1", "SELECT 2"));
        assertThat(result.size(), is(2));
    }

    @Test
    public void shouldSplitStatementsOnASemicolonAtTheEndOfALineEvenWithWindowsLineEndings() throws Exception {
        List<String> result = splitter.split("SELECT 1;\r\nSELECT 2;");
        assertThat(result, hasItems("SELECT 1", "SELECT 2"));
        assertThat(result.size(), is(2));
    }

    @Test
    public void shouldSplitStatementsOnASemicolonAtTheEndOfALineIgnoringWhitespace() throws Exception {
        List<String> result = splitter.split("SELECT 1;  \nSELECT 2;  ");
        assertThat(result, hasItems("SELECT 1", "SELECT 2"));
        assertThat(result.size(), is(2));
    }

    @Test
    public void shouldLeaveLineBreaksAlone() throws Exception {
        assertThat(splitter.split("SELECT\n1;"), hasItems("SELECT" + SystemUtils.LINE_SEPARATOR + "1"));
    }

    @Test
    @Ignore
    public void shouldSupportDefinedNewLineCharacters() throws Exception {
        splitter.setOutputLineEnding(LineEnding.crlf);
        assertThat(splitter.split("SELECT\n1"), hasItems("SELECT\r\n1"));
        assertThat(splitter.split("SELECT\r\n1"), hasItems("SELECT\r\n1"));

        splitter.setOutputLineEnding(LineEnding.cr);
        assertThat(splitter.split("SELECT\n1"), hasItems("SELECT\r1"));
        assertThat(splitter.split("SELECT\r\n1"), hasItems("SELECT\r1"));


        splitter.setOutputLineEnding(LineEnding.lf);
        assertThat(splitter.split("SELECT\n1"), hasItems("SELECT\n1"));
        assertThat(splitter.split("SELECT\r\n1"), hasItems("SELECT\n1"));


        splitter.setOutputLineEnding(LineEnding.platform);
        assertThat(splitter.split("SELECT\n1"), hasItems("SELECT" + SystemUtils.LINE_SEPARATOR + "1"));
        assertThat(splitter.split("SELECT\r\n1"), hasItems("SELECT" + SystemUtils.LINE_SEPARATOR + "1"));
    }

    @Test
    public void shouldNotSplitOnNewLinesInData() throws Exception {
        String statement = "SELECT * FROM Foo WHERE Bar='foo\nbar'";
        assertThat(splitter.split(statement), hasItem(statement));
    }
}
