
public class Token {
	
	//KEYWORDS는 TokenType의 Eof가 몇번째인지에 대한 정보를 가짐.
    private static final int KEYWORDS = TokenType.Eof.ordinal();
    
    //Eof인덱스 크기의 문자열 클래스 배열인 reserved 객체
    private static final String[] reserved = new String[KEYWORDS];
    //Eof인덱스 크기의 Token 클래스 배열인 token 객체.
    private static Token[] token = new Token[KEYWORDS];
    
    //Token클래스의 각 상수 ---Tok는 파라미터로 받은 타입과, 문자열로 매칭된다.
    public static final Token eofTok = new Token(TokenType.Eof, "<<EOF>>");
    public static final Token constTok = new Token(TokenType.Const, "const");
    public static final Token returnTok = new Token(TokenType.Return, "return");
    public static final Token voidTok = new Token(TokenType.Void, "void");
    public static final Token elseTok = new Token(TokenType.Else, "else");
    public static final Token ifTok = new Token(TokenType.If, "if");
    public static final Token intTok = new Token(TokenType.Int, "int");
    public static final Token whileTok = new Token(TokenType.While, "while");
    public static final Token leftBraceTok = new Token(TokenType.LeftBrace, "{");
    public static final Token rightBraceTok = new Token(TokenType.RightBrace, "}");
    public static final Token leftBracketTok = new Token(TokenType.LeftBracket, "[");
    public static final Token rightBracketTok = new Token(TokenType.RightBracket, "]");
    public static final Token leftParenTok = new Token(TokenType.LeftParen, "(");
    public static final Token rightParenTok = new Token(TokenType.RightParen, ")");
    public static final Token semicolonTok = new Token(TokenType.Semicolon, ";");
    public static final Token commaTok = new Token(TokenType.Comma, ",");
    public static final Token assignTok = new Token(TokenType.Assign, "=");
    public static final Token eqeqTok = new Token(TokenType.Equals, "==");
    public static final Token ltTok = new Token(TokenType.Less, "<");
    public static final Token lteqTok = new Token(TokenType.LessEqual, "<=");
    public static final Token gtTok = new Token(TokenType.Greater, ">");
    public static final Token gteqTok = new Token(TokenType.GreaterEqual, ">=");
    public static final Token notTok = new Token(TokenType.Not, "!");
    public static final Token noteqTok = new Token(TokenType.NotEqual, "!=");
    public static final Token plusTok = new Token(TokenType.Plus, "+");
    public static final Token minusTok = new Token(TokenType.Minus, "-");
    public static final Token multiplyTok = new Token(TokenType.Multiply, "*");
    public static final Token divideTok = new Token(TokenType.Divide, "/");
    public static final Token reminderTok = new Token(TokenType.Reminder, "%");
    public static final Token addAssignTok = new Token(TokenType.AddAssign, "+=");
    public static final Token subAssignTok = new Token(TokenType.SubAssign, "-=");
    public static final Token multAssignTok = new Token(TokenType.MultAssign, "*=");
    public static final Token divAssignTok = new Token(TokenType.DivAssign, "/=");
    public static final Token remAssignTok = new Token(TokenType.RemAssign, "%=");
    public static final Token incrementTok = new Token(TokenType.Increment, "++");
    public static final Token decrementTok = new Token(TokenType.Decrement, "--");
    public static final Token andTok = new Token(TokenType.And, "&&");
    public static final Token orTok = new Token(TokenType.Or, "||");
    
    //keywords ****추가한 것***********
    public static final Token charTok = new Token(TokenType.Char, "char");
    public static final Token doubleTok=new Token(TokenType.Double, "double");
    
    public static final Token forTok = new Token(TokenType.For, "for");
    public static final Token doTok = new Token(TokenType.Do, "do");
    public static final Token gotoTok = new Token(TokenType.Goto, "goto");
    
    public static final Token switchTok = new Token(TokenType.Switch, "switch");
    public static final Token caseTok = new Token(TokenType.Case, "case");
    public static final Token breakTok = new Token(TokenType.Break, "break");
    public static final Token defaultTok = new Token(TokenType.Default, "default");

    //operator ***추가한 것****
    public static final Token colonTok = new Token(TokenType.Colon, ":");

    //토큰타입 객체변수 type과 스트링 변수 value (일단은 "")으로 초기화.
    private TokenType type;
    private String value = "";
    
    //추가 token properties
    private static String fileName;
    private int lineNumber=-1;
    private int columnNumber=-1;
    
    //Token클래스의 생성자. 파라미터로 받은 토큰타입과 문자열을 각각 type, value로 갖게됨.
    private Token (TokenType t, String v) {
        type = t;
        value = v;;
        if (t.compareTo(TokenType.Eof) < 0) { //t를 Eof위치와 비교하여 그보다 앞에 있을 경우
            int ti = t.ordinal(); //t의 인덱스를 ti라 하자.
            reserved[ti] = v; //reserved의 ti인덱스에 v를 값으로 할당하여
            token[ti] = this; //token배열의 ti인덱스는 이 Token객체를 할당받게 됨.
        }
    }
    
    //이 객체의 type과 value을 알려줌
    public TokenType type( ) { return type; }
    public String value( ) { return value; }
    //토큰 만들 때마다 세팅해줌.
    public void setting(String title, int lineno, int col) {fileName=title; lineNumber=lineno; columnNumber=col;}
    
    //Identifier토큰을 리턴한다
    public static Token keyword  ( String name) {
        char ch = name.charAt(0);
        if (ch >= 'A' && ch <= 'Z') return mkIdentTok(name); //ch가 대문자알파벳인 경우, identifier 토큰으로 결정하여 반환
        for (int i = 0; i < KEYWORDS; i++) //대문자가 아니더라도 Eof인덱스 크기만큼 (0~Eof인덱스-1) 해당 이름이 reserved배열에 존재한다면 그 토큰배열의 원소를 리턴
           if (name.equals(reserved[i]))  return token[i];
        return mkIdentTok(name);
    } // keyword
    
    //Idenfitifer타입을 type으로, name을 value로 갖는 객체 레퍼런스를 반환
    public static Token mkIdentTok (String name) {
        return new Token(TokenType.Identifier, name);
    }
    
    //IntLiteral타입을 type으로, name을 value로 갖는 Token 객체 레퍼런스 반환
    public static Token mkIntLiteral (String name) {
        return new Token(TokenType.IntLiteral, name);
    }
    //************* 리터럴 추가 **********//
    public static Token mkDoubleLiteral(String name)  {
        return new Token(TokenType.DoubleLiteral, name);
    }
    public static Token mkCharLiteral (String name) {
        return new Token(TokenType.CharLiteral, name);
    }
    public static Token mkStringLiteral (String name) {
        return new Token(TokenType.StringLiteral, name);
    }
    
    
    //해당 객체의 타입이 Identifier토큰타입 위치와 비교하여 그보다 앞에 있을 경우 value를 그대로 반환, 같거나 뒤에 있을 경우 type과 value를 함께 반환
    public String toString ( ) {
        return type+"("+type.ordinal()+", "+value+", "+fileName+", "+lineNumber+", "+columnNumber+")";
    } // toString
    	
    //main함수
    public static void main (String[] args) {
    	// test
        System.out.println(eofTok); //출력결과: <<EOF>>
        System.out.println(whileTok); //출력결과: while
    }
} // Token
