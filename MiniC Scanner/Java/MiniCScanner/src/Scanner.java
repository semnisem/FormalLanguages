import java.io.*;

public class Scanner {
 
    private boolean isEof = false; //Eof인가를 true/false로 체크
    private char ch = ' '; //ch는 일단 공백문자로 초기화
    private BufferedReader input; //입력을 Enter기준으로 string 타입으로 받는다.
    private String line = ""; //line은 문자열을 저장
    private static String title= ""; //file이름을 저장
    private int lineno = 0;
    private int col = 1;
    private final String letters = "abcdefghijklmnopqrstuvwxyz" 
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //알파벳 전부를 가지고 있는 letters
    private final String digits = "0123456789"; //숫자 전부를 가지고 있는 digits
    private final char eolnCh = '\n'; //엔터 문자
    private final char eofCh = '\004'; //??모르겠다
    
    //생성자함수
    public Scanner (String fileName) { // source filename
    	title=fileName;
    	System.out.println("\n\nBegin scanning... programs/" + fileName + "\n");
        try { //파일 한 라인씩 읽는 핸들러 생성하여 input에
            input = new BufferedReader (new FileReader(fileName));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        }
    }
    
    //다음 문자를 반환 해줌
    private char nextChar() { // Return next char
        if (ch == eofCh) //이미 지금이 끝인 경우 에러
        	error("Attempt to read past end of file");
        col++; //열증가(다음문자가 존재)
        if (col >= line.length()) { //열이 현재라인 길이 이상이면
            try {
                line = input.readLine( ); //다음라인으로 움직인다.
            } catch (IOException e) {
                System.err.println(e);
                System.exit(1);
            } // try
            if (line == null) // at end of file 그 라인이 파일 종료인경우
                line = "" + eofCh; //파일종료임을 저장해준다.
            else { //정상적인 라인인 경우
                // System.out.println(lineno + ":\t" + line); //라인넘버와 함께 그 내용을 출력
                lineno++; //라인넘버 증가 시킨 뒤
                line += eolnCh; //라인내용에 개행문자 추가
            } // if line
            col = 0; //라인넘어갔으므로
        } // if col
        return line.charAt(col); //아무튼. 라인의 col위치의 문자를 반환한다
    }
            
    
    //다음 토큰을 반환해준다.
    public Token next( ) { // Return next token
        do {
            if (isLetter(ch) || ch == '_') { // ident or keyword
                String spelling = concat(letters + digits + '_');
                return Token.keyword(spelling);
            } 
            //////////////// 숫자 인식 수정 부분 ///////////////////////
            else if (isDigit(ch)||ch=='.') { // double literal or int literal
            	
            	String number;
            	if(ch!='.') {
            		number= concat(digits);
            		//double literal (digit.digit)
                    if(ch=='.') { //
                    	String fraction;
                    	ch = nextChar(); // 소수 첫째자리 or 수가 아닌 거 in ch
                    	if(!isDigit(ch)) { //double literal (digit.)
    	                	fraction="0";
    	                }
                    	else{
                    		fraction=concat(digits);
                    	}
                    	return Token.mkDoubleLiteral(number+"."+fraction); 
                    }
                    
                    //int literal(digit)
                    else { 
    	                return Token.mkIntLiteral(number); 
                    }
            	}
            	//double literal (.digit)
            	else { 
            		number="0";
            		String fraction;
            		ch=nextChar(); //pass '.'
                	fraction=concat(digits);
                	return Token.mkDoubleLiteral(number+"."+fraction); 
            	}
            } 
            ////////////////숫자 인식 수정 부분 종료 ///////////////////////
            
            else switch (ch) {
            case ' ': case '\t': case '\r': case eolnCh:
                ch = nextChar();
                break;
            
            case '/':  // divide or divAssign or comment
                ch = nextChar();
                if (ch == '=')  { // divAssign
                	ch = nextChar();
                	return Token.divAssignTok;
                }
                
                // divide
                if (ch != '*' && ch != '/') return Token.divideTok;
                
                //************** 주석 추가 ***************//
                if (ch == '*') {
                	ch= nextChar();
                	if(ch=='*') { //doucummented comments (/** ~)
                		String sss="";
        				do {
        					while (ch != '*') {
        						ch = nextChar();
        						sss+=ch;
        					}
        					ch = nextChar();
        					sss+=ch;
        				} while (ch != '/');
        				sss=sss.substring(0,sss.length()-2);
                		System.out.println("Documented Comments (/**~*/) --------> "+sss); 
        				ch = nextChar();
                	}
                	else { //multi line comment (/* ~)
                		do {
        					while (ch != '*') {
        						ch = nextChar();
        					}
        					ch = nextChar();
        				} while (ch != '/');
        				ch = nextChar();
                	}
                	
                }
                else if (ch == '/')  {
                	ch =nextChar();
                	if (ch=='/') { //single line documented(///) comments
                		String ss="";
                		do {
    	                    ch = nextChar();
    	                    ss+=ch;
    	                } while (ch != eolnCh);
                		ss=ss.substring(0,ss.length()-1);
                		System.out.println("Documented Comments (///)--------> "+ss); //docummented contents
    	                ch = nextChar();
                	}
                	else { // single line comment (//)          		
                		while (ch != eolnCh) {
    	                    ch = nextChar();  
    	                }
    	                ch = nextChar();
                	}
                }
            //***************** 주석 추가 종료 ***************//
                break;
            
            //***************** Literal 추가 ***************//
            case '\'':  // char literal (작은따옴표를 의미)
                char ch1 = nextChar();
                nextChar(); // get '
                ch = nextChar();
                return Token.mkCharLiteral("" + ch1);
                
            case '\"': // String literal (큰따옴표를 의미)
            	String str="";
            	nextChar(); // get "
            	nextChar();
            	while (ch != '\"') {
            		str+=ch;
					ch = nextChar();
				} ;
				ch = nextChar();
            	return Token.mkStringLiteral(str);
           //***************** Literal 추가종료 ***************// 
            	
    
            case eofCh: return Token.eofTok;
            
            case '+': 
            	ch = nextChar();
	            if (ch == '=')  { // addAssign
	            	ch = nextChar();
	            	return Token.addAssignTok;
	            }
	            else if (ch == '+')  { // increment
	            	ch = nextChar();
	            	return Token.incrementTok;
	            }
                return Token.plusTok;

            case '-': 
            	ch = nextChar();
                if (ch == '=')  { // subAssign
                	ch = nextChar();
                	return Token.subAssignTok;
                }
	            else if (ch == '-')  { // decrement
	            	ch = nextChar();
	            	return Token.decrementTok;
	            }
                return Token.minusTok;

            case '*': 
            	ch = nextChar();
                if (ch == '=')  { // multAssign
                	ch = nextChar();
                	return Token.multAssignTok;
                }
                return Token.multiplyTok;

            case '%': 
            	ch = nextChar();
                if (ch == '=')  { // remAssign
                	ch = nextChar();
                	return Token.remAssignTok;
                }
                return Token.reminderTok;

            case '(': ch = nextChar();
            return Token.leftParenTok;

            case ')': ch = nextChar();
            return Token.rightParenTok;

            case '{': ch = nextChar();
            return Token.leftBraceTok;

            case '}': ch = nextChar();
            return Token.rightBraceTok;
            
            ///****** 기호 추가 *********
            case '[': ch= nextChar();
            return Token.leftBracketTok;
            
            case ']': ch= nextChar();
            return Token.rightBracketTok;
            
            //******** 추가 종료 **********
            case ';': ch = nextChar();
            return Token.semicolonTok;
            
            //******** operator 추가 -- swtich 추가에 따른 콜론 연산자 ******
            case ':': ch = nextChar();
            return Token.colonTok;
            //******** operator 추가 종료 ******

            case ',': ch = nextChar();
            return Token.commaTok;
                
            case '&': check('&'); return Token.andTok;
            case '|': check('|'); return Token.orTok;

            case '=':
                return chkOpt('=', Token.assignTok,
                                   Token.eqeqTok);

            case '<':
                return chkOpt('=', Token.ltTok,
                                   Token.lteqTok);
            case '>': 
                return chkOpt('=', Token.gtTok,
                                   Token.gteqTok);
            case '!':
                return chkOpt('=', Token.notTok,
                                   Token.noteqTok);
              
            default:  error("Illegal character " + ch); 
            } // switch
        } while (true);
    } // next

    //파라미터로 전달하는 문자가 알파벳인 경우 true 아니면 false
    private boolean isLetter(char c) {
        return (c>='a' && c<='z' || c>='A' && c<='Z');
    }
    //파라미터로 전달하는 문자가 숫자인 경우 true 아니면 false
    private boolean isDigit(char c) {
        return (c>='0' && c<='9');
    }
    //파라미터로 전달하는 문자가 다음문자와 동일하지 않은 경우 에러내고 동일하면 다음문자 읽어서 ch에.
    private void check(char c) {
        ch = nextChar();
        if (ch != c) 
            error("Illegal character, expecting " + c);
        ch = nextChar();
    }
    //파라미터로 전달하는 문자가 다음문자와 동일하지 않은 경우 one 토큰이고 동일하다면 다음문자가 한 칸 더 이동하고 two 토큰임
    private Token chkOpt(char c, Token one, Token two) {
        ch = nextChar();
        if (ch != c)
            return one;
        ch = nextChar();
        return two;
    }
    //파라미터로 전달하는 문자열 내 ch가 존재하는 경우 반복적으로 다음 문자를 읽어 연결하다가 존재하지 않는다면 연결된 문자열 리턴.
    private String concat(String set) {
        String r = "";
        do {
            r += ch;
            ch = nextChar();
        } while (set.indexOf(ch) >= 0);
        return r;
    }
    //에러임을 출력하여 종료
    public void error (String msg) {
        System.err.print(line);
        System.err.println("Error: column " + col + " " + msg);
        System.exit(1);
    }
    //메인함수
    static public void main ( String[] argv ) {
    	for(int id=0; id<10; id++) {
	        Scanner lexer = new Scanner(argv[id]); //파일이름을 매개로 객체 생성하여 lexer 참조변수
	        Token tok = lexer.next( );
	        while (tok != Token.eofTok) {
	        	tok.setting(argv[id], lexer.lineno, lexer.col);
	            System.out.println("Token --------> "+tok.toString());
	            tok = lexer.next( );
	        }
    	}
    } // main
}
