
public enum TokenType {
    Const, Void, Else, If, Int, While, Return,
    Eof, 
    
    LeftBrace, RightBrace, LeftBracket, RightBracket,
    LeftParen, RightParen, Semicolon, Comma, Assign, AddAssign, SubAssign, MultAssign, DivAssign, RemAssign,
    
    Equals, Less, LessEqual, Greater, GreaterEqual,
    Not, NotEqual, Plus, Minus, Multiply, Reminder,
    Increment, Decrement,
    Divide, And, Or, Identifier, IntLiteral,

    //keywords
    Char, Double, For, Do, Goto, Switch, Case, Break, Default,

    //operator
    Colon,

    //literals
    CharLiteral,StringLiteral,DoubleLiteral, //.123,   123. 과 같은 숏폼연산자


}
