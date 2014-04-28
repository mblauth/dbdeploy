grammar SQL;
@header{package com.dbdeploy.database;}
@lexer::header{package com.dbdeploy.database;}


parse returns [List<String> result] @init{$result = new ArrayList<String>();} :
    WS* (s=statement {$result.add($s.text);} SEMI WS*)* EOF;

statement returns [String statement] @init{StringBuilder builder = new StringBuilder();}:
    b=LABEL {builder.append($b.text);}
    ((c=LABEL|WS {builder.append($c.text);})|(QUOTE d=data_string QUOTE {builder.append($d.text);}))*
    {$statement = builder.toString();};

data_string returns [String data] @init{StringBuilder builder = new StringBuilder();}:
    (options{greedy=true;} :
        c=srch_cond {builder.append($c.text);}
    )*
    {$data = "'" + builder.toString() + "'";};

srch_cond : ESC_QUOTE|LABEL|BACKSLASH|SEMI|WS;

WS : '\r'|'\n'|' ';
LABEL : ~('\\'|'\''|';'|'\n'|'\r'|' ')+;
ESC_QUOTE options{k=2;} : '\\''\'';
BACKSLASH : '\\';
QUOTE : '\'';
SEMI : ';';
