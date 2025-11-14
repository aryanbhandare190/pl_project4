
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        Expr e = parseT();
        if (tokens != null) {
            throw new Exception("Extra tokens after parse");
        }
        return e;
    }

    private Expr parseT() throws Exception {
        Expr left = parseF();
        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = null;
            if (peek(TokenType.PLUS, 0)) {
                op = consume(TokenType.PLUS);
            } else {
                op = consume(TokenType.MINUS);
            }
            Expr right = parseT();
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(left, right);
            } else {
                return new MinusExpr(left, right);
            }
        } else {
            return left;
        }
    }

    private Expr parseF() throws Exception {
        Expr left = parseLit();
        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = null;
            if (peek(TokenType.TIMES, 0)) {
                op = consume(TokenType.TIMES);
            } else {
                op = consume(TokenType.DIV);
            }
            Expr right = parseF();
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(left, right);
            } else {
                return new DivExpr(left, right);
            }
        } else {
            return left;
        }
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token t = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(t.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr e = parseT();
            consume(TokenType.RPAREN);
            return e;
        } else {
            throw new Exception("Unexpected token in literal");
        }
    }

}
