package n2lf.wirelesscontroller.utilities;

public class KeyCode {
    
    
    
    
    
    
    
    
    
    /**
    part of keyboard
    */
 /*   public static final int KEY_FIRST = 400;//Useless
    public static final int KEY_LAST = 402;
    public static final int KEY_TYPED = 400;
    public static final int KEY_PRESSED = 401;
    public static final int KEY_RELEASED = 402;  */
    public static final int VK_ENTER = '\n';
    public static final int VK_BACK_SPACE = '\b';
    public static final int VK_TAB = '\t';
    public static final int VK_CANCEL = 3;
    public static final int VK_CLEAR = 12;
    public static final int VK_SHIFT = 16;
    public static final int VK_CONTROL = 17;
    public static final int VK_ALT = 18;
    public static final int VK_PAUSE = 19;
    public static final int VK_CAPS_LOCK = 20;
    public static final int VK_ESCAPE = 27;
    public static final int VK_SPACE = 32;
    public static final int VK_PAGE_UP = 33;
    public static final int VK_PAGE_DOWN = 34;
    public static final int VK_END = 35;
    public static final int VK_HOME = 36;
    public static final int VK_LEFT = 37;
    public static final int VK_UP = 38;
    public static final int VK_RIGHT = 39;
    public static final int VK_DOWN = 40;
    public static final int VK_COMMA = 44;
    public static final int VK_MINUS = 45;
    public static final int VK_PERIOD = 46;
    public static final int VK_SLASH = 47;
    public static final int VK_0 = 48;
    public static final int VK_1 = 49;
    public static final int VK_2 = 50;
    public static final int VK_3 = 51;
    public static final int VK_4 = 52;
    public static final int VK_5 = 53;
    public static final int VK_6 = 54;
    public static final int VK_7 = 55;
    public static final int VK_8 = 56;
    public static final int VK_9 = 57;
    public static final int VK_SEMICOLON = 59;
    public static final int VK_EQUALS = 61;
    public static final int VK_A = 65;
    public static final int VK_B = 66;
    public static final int VK_C = 67;
    public static final int VK_D = 68;
    public static final int VK_E = 69;
    public static final int VK_F = 70;
    public static final int VK_G = 71;
    public static final int VK_H = 72;
    public static final int VK_I = 73;
    public static final int VK_J = 74;
    public static final int VK_K = 75;
    public static final int VK_L = 76;
    public static final int VK_M = 77;
    public static final int VK_N = 78;
    public static final int VK_O = 79;
    public static final int VK_P = 80;
    public static final int VK_Q = 81;
    public static final int VK_R = 82;
    public static final int VK_S = 83;
    public static final int VK_T = 84;
    public static final int VK_U = 85;
    public static final int VK_V = 86;
    public static final int VK_W = 87;
    public static final int VK_X = 88;
    public static final int VK_Y = 89;
    public static final int VK_Z = 90;
    public static final int VK_OPEN_BRACKET = 91;
    public static final int VK_BACK_SLASH = 92;
    public static final int VK_CLOSE_BRACKET = 93;
    public static final int VK_NUMPAD0 = 96;
    public static final int VK_NUMPAD1 = 97;
    public static final int VK_NUMPAD2 = 98;
    public static final int VK_NUMPAD3 = 99;
    public static final int VK_NUMPAD4 = 100;
    public static final int VK_NUMPAD5 = 101;
    public static final int VK_NUMPAD6 = 102;
    public static final int VK_NUMPAD7 = 103;
    public static final int VK_NUMPAD8 = 104;
    public static final int VK_NUMPAD9 = 105;
    public static final int VK_MULTIPLY = 106;
    public static final int VK_ADD = 107;
 //   public static final int VK_SEPARATER = 108;
    public static final int VK_SEPARATOR = 108;
    public static final int VK_SUBTRACT = 109;
    public static final int VK_DECIMAL = 110;
    public static final int VK_DIVIDE = 111;
    public static final int VK_DELETE = 127;
    public static final int VK_NUM_LOCK = 144;
    public static final int VK_SCROLL_LOCK = 145;
    public static final int VK_F1 = 112;
    public static final int VK_F2 = 113;
    public static final int VK_F3 = 114;
    public static final int VK_F4 = 115;
    public static final int VK_F5 = 116;
    public static final int VK_F6 = 117;
    public static final int VK_F7 = 118;
    public static final int VK_F8 = 119;
    public static final int VK_F9 = 120;
    public static final int VK_F10 = 121;
    public static final int VK_F11 = 122;
    public static final int VK_F12 = 123;
    public static final int VK_F13 = 61440;
    public static final int VK_F14 = 61441;
    public static final int VK_F15 = 61442;
    public static final int VK_F16 = 61443;
    public static final int VK_F17 = 61444;
    public static final int VK_F18 = 61445;
    public static final int VK_F19 = 61446;
    public static final int VK_F20 = 61447;
    public static final int VK_F21 = 61448;
    public static final int VK_F22 = 61449;
    public static final int VK_F23 = 61450;
    public static final int VK_F24 = 61451;
    public static final int VK_PRINTSCREEN = 154;
    public static final int VK_INSERT = 155;
    public static final int VK_HELP = 156;
    public static final int VK_META = 157;
    public static final int VK_BACK_QUOTE = 192;
    public static final int VK_QUOTE = 222;
    public static final int VK_KP_UP = 224;
    public static final int VK_KP_DOWN = 225;
    public static final int VK_KP_LEFT = 226;
    public static final int VK_KP_RIGHT = 227;
    public static final int VK_DEAD_GRAVE = 128;
    public static final int VK_DEAD_ACUTE = 129;
    public static final int VK_DEAD_CIRCUMFLEX = 130;
    public static final int VK_DEAD_TILDE = 131;
    public static final int VK_DEAD_MACRON = 132;
    public static final int VK_DEAD_BREVE = 133;
    public static final int VK_DEAD_ABOVEDOT = 134;
    public static final int VK_DEAD_DIAERESIS = 135;
    public static final int VK_DEAD_ABOVERING = 136;
    public static final int VK_DEAD_DOUBLEACUTE = 137;
    public static final int VK_DEAD_CARON = 138;
    public static final int VK_DEAD_CEDILLA = 139;
    public static final int VK_DEAD_OGONEK = 140;
    public static final int VK_DEAD_IOTA = 141;
    public static final int VK_DEAD_VOICED_SOUND = 142;
    public static final int VK_DEAD_SEMIVOICED_SOUND = 143;
    public static final int VK_AMPERSAND = 150;
    public static final int VK_ASTERISK = 151;
    public static final int VK_QUOTEDBL = 152;
    public static final int VK_LESS = 153;
    public static final int VK_GREATER = 160;
    public static final int VK_BRACELEFT = 161;
    public static final int VK_BRACERIGHT = 162;
    public static final int VK_AT = 512;
    public static final int VK_COLON = 513;
    public static final int VK_CIRCUMFLEX = 514;
    public static final int VK_DOLLAR = 515;
    public static final int VK_EURO_SIGN = 516;
    public static final int VK_EXCLAMATION_MARK = 517;
    public static final int VK_INVERTED_EXCLAMATION_MARK = 518;
    public static final int VK_LEFT_PARENTHESIS = 519;
    public static final int VK_NUMBER_SIGN = 520;
    public static final int VK_PLUS = 521;
    public static final int VK_RIGHT_PARENTHESIS = 522;
    public static final int VK_UNDERSCORE = 523;
    public static final int VK_WINDOWS = 524;
    public static final int VK_CONTEXT_MENU = 525;
    public static final int VK_FINAL = 24;
    public static final int VK_CONVERT = 28;
    public static final int VK_NONCONVERT = 29;
    public static final int VK_ACCEPT = 30;
    public static final int VK_MODECHANGE = 31;
    public static final int VK_KANA = 21;
    public static final int VK_KANJI = 25;
    public static final int VK_ALPHANUMERIC = 240;
    public static final int VK_KATAKANA = 241;
    public static final int VK_HIRAGANA = 242;
    public static final int VK_FULL_WIDTH = 243;
    public static final int VK_HALF_WIDTH = 244;
    public static final int VK_ROMAN_CHARACTERS = 245;
    public static final int VK_ALL_CANDIDATES = 256;
    public static final int VK_PREVIOUS_CANDIDATE = 257;
    public static final int VK_CODE_INPUT = 258;
    public static final int VK_JAPANESE_KATAKANA = 259;
    public static final int VK_JAPANESE_HIRAGANA = 260;
    public static final int VK_JAPANESE_ROMAN = 261;
    public static final int VK_KANA_LOCK = 262;
    public static final int VK_INPUT_METHOD_ON_OFF = 263;
    public static final int VK_CUT = 65489;
    public static final int VK_COPY = 65485;
    public static final int VK_PASTE = 65487;
    public static final int VK_UNDO = 65483;
    public static final int VK_AGAIN = 65481;
    public static final int VK_FIND = 65488;
    public static final int VK_PROPS = 65482;
    public static final int VK_STOP = 65480;
    public static final int VK_COMPOSE = 65312;
    public static final int VK_ALT_GRAPH = 65406;
    public static final int VK_BEGIN = 65368;
 /*   public static final int VK_UNDEFINED = 0;
    public static final char CHAR_UNDEFINED = '\uffff';
    public static final int KEY_LOCATION_UNKNOWN = 0;
    public static final int KEY_LOCATION_STANDARD = 1;
    public static final int KEY_LOCATION_LEFT = 2;
    public static final int KEY_LOCATION_RIGHT = 3;
    public static final int KEY_LOCATION_NUMPAD = 4; */
    
       public static String getKeyBoardKeyName(int keyCode){
            switch (keyCode){
                case VK_ENTER: 
                    return "Enter";
                case VK_BACK_SPACE: 
                    return "BackSpace";
                case VK_TAB: 
                    return "Tab";
                case VK_CANCEL: 
                    return "Cancel";
                case VK_CLEAR: 
                    return "Clear";
                case VK_SHIFT: 
                    return "Shift";
                case VK_CONTROL: 
                    return "Ctrl";
                case VK_ALT: 
                    return "Alt";
                case VK_PAUSE: 
                    return "Pause";
                case VK_CAPS_LOCK: 
                    return "CapsLock";
                case VK_ESCAPE: 
                    return "Escape";
                case VK_SPACE: 
                    return "Space";
                case VK_PAGE_UP: 
                    return "PageUp";
                case VK_PAGE_DOWN: 
                    return "PageDown";
                case VK_END: 
                    return "End";
                case VK_HOME: 
                    return "Home";
                case VK_LEFT: 
                    return "Left";
                case VK_UP: 
                    return "Up";
                case VK_RIGHT: 
                    return "Right";
                case VK_DOWN: 
                    return "Down";
                case VK_COMMA: 
                    return ",";
                case VK_MINUS: 
                    return "-";
                case VK_PERIOD: 
                    return ".";
                case VK_SLASH: 
                    return "/";
                case VK_0: 
                    return "0";
                case VK_1: 
                    return "1";
                case VK_2: 
                    return "2";
                case VK_3: 
                    return "3";
                case VK_4: 
                    return "4";
                case VK_5: 
                    return "5";
                case VK_6: 
                    return "6";
                case VK_7: 
                    return "7";
                case VK_8: 
                    return "8";
                case VK_9: 
                    return "9";
                case VK_SEMICOLON: 
                    return ";";
                case VK_EQUALS: 
                    return "=";
                case VK_A: 
                    return "A";
                case VK_B: 
                    return "B";
                case VK_C: 
                    return "C";
                case VK_D: 
                    return "D";
                case VK_E: 
                    return "E";
                case VK_F: 
                    return "F";
                case VK_G: 
                    return "G";
                case VK_H: 
                    return "H";
                case VK_I: 
                    return "I";
                case VK_J: 
                    return "J";
                case VK_K: 
                    return "K";
                case VK_L: 
                    return "L";
                case VK_M: 
                    return "M";
                case VK_N: 
                    return "N";
                case VK_O: 
                    return "O";
                case VK_P: 
                    return "P";
                case VK_Q: 
                    return "Q";
                case VK_R: 
                    return "R";
                case VK_S: 
                    return "S";
                case VK_T: 
                    return "T";
                case VK_U: 
                    return "U";
                case VK_V: 
                    return "V";
                case VK_W: 
                    return "W";
                case VK_X: 
                    return "X";
                case VK_Y: 
                    return "Y";
                case VK_Z: 
                    return "Z";
                case VK_OPEN_BRACKET: 
                    return "[";
                case VK_BACK_SLASH: 
                    return "\\";
                case VK_CLOSE_BRACKET: 
                    return "]";
                case VK_NUMPAD0: 
                    return "Numpad0";
                case VK_NUMPAD1: 
                    return "Numpad1";
                case VK_NUMPAD2: 
                    return "Numpad2";
                case VK_NUMPAD3: 
                    return "Numpad3";
                case VK_NUMPAD4: 
                    return "Numpad4";
                case VK_NUMPAD5: 
                    return "Numpad5";
                case VK_NUMPAD6: 
                    return "Numpad6";
                case VK_NUMPAD7: 
                    return "Numpad7";
                case VK_NUMPAD8: 
                    return "Numpad8";
                case VK_NUMPAD9: 
                    return "Numpad9";
                case VK_MULTIPLY: 
                    return "Multiply";
                case VK_ADD: 
                    return "Add";
      //          case VK_SEPARATER: 
      //              return "";
                case VK_SEPARATOR: 
                    return "Separator";
                case VK_SUBTRACT: 
                    return "Subtract";
                case VK_DECIMAL: 
                    return "Decimal";
                case VK_DIVIDE: 
                    return "Divide";
                case VK_DELETE: 
                    return "Delete";
                case VK_NUM_LOCK: 
                    return "NumLock";
                case VK_SCROLL_LOCK: 
                    return "ScrollLock";
                case VK_F1: 
                    return "F1";
                case VK_F2: 
                    return "F2";
                case VK_F3: 
                    return "F3";
                case VK_F4: 
                    return "F4";
                case VK_F5: 
                    return "F5";
                case VK_F6: 
                    return "F6";
                case VK_F7: 
                    return "F7";
                case VK_F8: 
                    return "F8";
                case VK_F9: 
                    return "F9";
                case VK_F10: 
                    return "F10";
                case VK_F11: 
                    return "F11";
                case VK_F12: 
                    return "F12";
                case VK_F13: 
                    return "F13";
                case VK_F14: 
                    return "F14";
                case VK_F15: 
                    return "F15";
                case VK_F16: 
                    return "F16";
                case VK_F17: 
                    return "F17";
                case VK_F18: 
                    return "F18";
                case VK_F19: 
                    return "F19";
                case VK_F20: 
                    return "F20";
                case VK_F21: 
                    return "F21";
                case VK_F22: 
                    return "F22";
                case VK_F23: 
                    return "F23";
                case VK_F24: 
                    return "F24";
                case VK_PRINTSCREEN: 
                    return "PrintScreen";
                case VK_INSERT: 
                    return "Insert";
                case VK_HELP: 
                    return "Help";
                case VK_META: 
                    return "Meta";
                case VK_BACK_QUOTE: 
                    return "BackQuote";
                case VK_QUOTE: 
                    return "Quote";
                case VK_KP_UP: 
                    return "KpUp";//keypad
                case VK_KP_DOWN: 
                    return "KpDown";
                case VK_KP_LEFT: 
                    return "KpLeft";
                case VK_KP_RIGHT: 
                    return "KpRight";
                case VK_DEAD_GRAVE: 
                    return "DeadGrave";
                case VK_DEAD_ACUTE: 
                    return "DeadAcute";
                case VK_DEAD_CIRCUMFLEX: 
                    return "DeadCircumflex";
                case VK_DEAD_TILDE: 
                    return "DeadTilde";
                case VK_DEAD_MACRON: 
                    return "DeadMacron";
                case VK_DEAD_BREVE: 
                    return "DeadBreve";
                case VK_DEAD_ABOVEDOT: 
                    return "DeadAboveDot";
                case VK_DEAD_DIAERESIS: 
                    return "DeadDiaeresis";
                case VK_DEAD_ABOVERING: 
                    return "DeadAbovering";
                case VK_DEAD_DOUBLEACUTE: 
                    return "DeadDoubleAcute";
                case VK_DEAD_CARON: 
                    return "DeadCaron";
                case VK_DEAD_CEDILLA: 
                    return "DeadCedilla";
                case VK_DEAD_OGONEK: 
                    return "DeadOgonek";
                case VK_DEAD_IOTA: 
                    return "DeadIota";
                case VK_DEAD_VOICED_SOUND: 
                    return "DeadVoicedSound";
                case VK_DEAD_SEMIVOICED_SOUND: 
                    return "DeadSemiVoicedSound";
                case VK_AMPERSAND: 
                    return "Ampersand";
                case VK_ASTERISK: 
                    return "Asterisk";
                case VK_QUOTEDBL: 
                    return "Quotedbl";
                case VK_LESS: 
                    return "Less";
                case VK_GREATER: 
                    return "Greater";
                case VK_BRACELEFT: 
                    return "BraceLeft";
                case VK_BRACERIGHT: 
                    return "BraceRight";
                case VK_AT: 
                    return "@";
                case VK_COLON: 
                    return ":";
                case VK_CIRCUMFLEX: 
                    return "^";
                case VK_DOLLAR: 
                    return "$";
                case VK_EURO_SIGN: 
                    return "EuroSign";
                case VK_EXCLAMATION_MARK: 
                    return "!";
                case VK_INVERTED_EXCLAMATION_MARK: 
                    return "InvertedExclamationMark";
                case VK_LEFT_PARENTHESIS: 
                    return "(";
                case VK_NUMBER_SIGN: 
                    return "#";
                case VK_PLUS: 
                    return "+";
                case VK_RIGHT_PARENTHESIS: 
                    return ")";
                case VK_UNDERSCORE: 
                    return "_";
                case VK_WINDOWS: 
                    return "Windows";
                case VK_CONTEXT_MENU: 
                    return "ContextMenu";
                case VK_FINAL: 
                    return "Final";
                case VK_CONVERT: 
                    return "Convert";
                case VK_NONCONVERT: 
                    return "NonConvert";
                case VK_ACCEPT: 
                    return "Accept";
                case VK_MODECHANGE: 
                    return "ModeChange";
                case VK_KANA: 
                    return "Kana";
                case VK_KANJI: 
                    return "Kanji";
                case VK_ALPHANUMERIC: 
                    return "Alphanumeric";
                case VK_KATAKANA: 
                    return "Katakana";
                case VK_HIRAGANA: 
                    return "Hiragana";
                case VK_FULL_WIDTH: 
                    return "FullWidth";
                case VK_HALF_WIDTH: 
                    return "HalfWidth";
                case VK_ROMAN_CHARACTERS: 
                    return "RomanCharacters";
                case VK_ALL_CANDIDATES: 
                    return "AllCandidates";
                case VK_PREVIOUS_CANDIDATE: 
                    return "PreviousCandidate";
                case VK_CODE_INPUT: 
                    return "CodeInput";
                case VK_JAPANESE_KATAKANA: 
                    return "JapaneseKatakana";
                case VK_JAPANESE_HIRAGANA: 
                    return "JapaneseHiragana";
                case VK_JAPANESE_ROMAN: 
                    return "JapaneseRoman";
                case VK_KANA_LOCK: 
                    return "KanaLock";
                case VK_INPUT_METHOD_ON_OFF: 
                    return "InputMethodOn/Off ";
                case VK_CUT: 
                    return "Cut";
                case VK_COPY: 
                    return "Copy";
                case VK_PASTE: 
                    return "Paste";
                case VK_UNDO: 
                    return "Undo";
                case VK_AGAIN: 
                    return "Again";
                case VK_FIND: 
                    return "Find";
                case VK_PROPS: 
                    return "Props";
                case VK_STOP: 
                    return "Stop";
                case VK_COMPOSE: 
                    return "Compose";
                case VK_ALT_GRAPH: 
                    return "AltGraph";
                case VK_BEGIN: 
                    return "Begin";
                default : 
                    return "KeyBoardKeyCodeNotFound:"+keyCode;
                }
            }
        public static int[] getKeyBoardCodeList(){
            int codeList[] = {'\n','\b','\t',3,12,16,17,18,19,20,27,32,33,34,35,36,
            37,38,39,40,44,45,46,47,48,49,50,51,52,53,54,55,56,57,59,61,65,66,67,68,
            69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,
            93,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,127,144,145,
            112,113,114,115,116,117,118,119,120,121,122,123,61440,61441,61442,61443,61444,
            61445,61446,61447,61448,61449,61450,61451,154,155,156,157,192,222,224,225,226,
            227,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,150,151,
            152,153,160,161,162,512,513,514,515,516,517,518,519,520,521,522,523,524,525,
            24,28,29,30,31,21,25,240,241,242,243,244,245,256,257,258,259,260,261,262,263,
            65489,65485,65487,65483,65481,65488,65482,65480,65312,65406,65368};
            return codeList;
            }
        
        /**
        part of mouse
        */
        public static final int SHIFT_DOWN_MASK = 1 << 6;
        public static final int CTRL_DOWN_MASK = 1 << 7;
        public static final int META_DOWN_MASK = 1 << 8;
        public static final int ALT_DOWN_MASK = 1 << 9;
        public static final int BUTTON1_DOWN_MASK = 1 << 10;//左键
        public static final int BUTTON2_DOWN_MASK = 1 << 11;//中键
        public static final int BUTTON3_DOWN_MASK = 1 << 12;//右键
        public static final int ALT_GRAPH_DOWN_MASK = 1 << 13;
        public static final int BUTTON4_DOWN_MASK = 1 << 14;
        public static final int BUTTON5_DOWN_MASK = 1 << 15;
        public static final int BUTTON6_DOWN_MASK = 1 << 16;
        public static final int BUTTON7_DOWN_MASK = 1 << 17;
        public static final int BUTTON8_DOWN_MASK = 1 << 18;
        public static final int BUTTON9_DOWN_MASK = 1 << 19;
        public static final int BUTTON10_DOWN_MASK = 1 << 20;
        public static final int BUTTON11_DOWN_MASK = 1 << 21;
        public static final int BUTTON12_DOWN_MASK = 1 << 22;
        public static final int BUTTON13_DOWN_MASK = 1 << 23;
        public static final int BUTTON14_DOWN_MASK = 1 << 24;
        public static final int BUTTON15_DOWN_MASK = 1 << 25;
        public static final int BUTTON16_DOWN_MASK = 1 << 26;
        public static final int BUTTON17_DOWN_MASK = 1 << 27;
        public static final int BUTTON18_DOWN_MASK = 1 << 28;
        public static final int BUTTON19_DOWN_MASK = 1 << 29;
        public static final int BUTTON20_DOWN_MASK = 1 << 30;

        public static String getMouseKeyName(int mousecode){
            switch (mousecode){
                    //mouse
                case SHIFT_DOWN_MASK :
                    return "MouseShift";
                case CTRL_DOWN_MASK :
                    return "MousrCtrl";
                case META_DOWN_MASK :
                    return "MouseMeta";
                case ALT_DOWN_MASK :
                    return "MouseAlt";
                case BUTTON1_DOWN_MASK :
                    return "MouseLeftButton";
                case BUTTON2_DOWN_MASK :
                    return "MouseMiddleButton";
                case BUTTON3_DOWN_MASK :
                    return "MouseRightButton";
                case ALT_GRAPH_DOWN_MASK :
                    return "MouseAltGraph";
                case BUTTON4_DOWN_MASK :
                    return "MouseButton4";
                case BUTTON5_DOWN_MASK :
                    return "MouseButton5";
                case BUTTON6_DOWN_MASK :
                    return "MouseButton6";
                case BUTTON7_DOWN_MASK :
                    return "MouseButton7";
                case BUTTON8_DOWN_MASK :
                    return "MouseButton8";
                case BUTTON9_DOWN_MASK :
                    return "MouseButton9";
                case BUTTON10_DOWN_MASK :
                    return "MouseButton10";
                case BUTTON11_DOWN_MASK :
                    return "MouseButton11";
                case BUTTON12_DOWN_MASK :
                    return "MouseButton12";
                case BUTTON13_DOWN_MASK :
                    return "MouseButton13";
                case BUTTON14_DOWN_MASK :
                    return "MouseButton14";
                case BUTTON15_DOWN_MASK :
                    return "MouseButton15";
                case BUTTON16_DOWN_MASK :
                    return "MouseButton16";
                case BUTTON17_DOWN_MASK :
                    return "MouseButton17";
                case BUTTON18_DOWN_MASK :
                    return "MouseButton18";
                case BUTTON19_DOWN_MASK :
                    return "MouseButton19";
                case BUTTON20_DOWN_MASK :
                    return "MouseButton20";
                default :
                    return "MouseKeyCodeNotFound:"+mousecode;
            }
        }
        
        public static int[] getMouseCodeList(){
            int[] mouseList = {1<<6,1<<7,1<<8,1<<9,1<<10,1<<11,1<<12,1<<13,1<<14,1<<15,1<<16,
            1<<17,1<<18,1<<19 ,1<<20,1<<21,1<<22,1<<23,1<<24,1<<25,1<<26,1<<27,1<<28,1<<29,1<<30};
            return mouseList;
        }
        
        public static boolean isMouseKeyCode(int keycode){
        if(
            (keycode == 1<<6) ||
            (keycode == 1<<7) ||
            (keycode == 1<<8) ||
            (keycode == 1<<9) ||
            (keycode == 1<<10) || 
            (keycode == 1<<11) ||
            (keycode == 1<<12) ||
            (keycode == 1<<13) ||
            (keycode == 1<<14) ||
            (keycode == 1<<15) ||
            (keycode == 1<<16) ||
            (keycode == 1<<17) ||
            (keycode == 1<<18) ||
            (keycode == 1<<19) ||
            (keycode == 1<<20) ||
            (keycode == 1<<21) ||
            (keycode == 1<<22) ||
            (keycode == 1<<23) ||
            (keycode == 1<<24) ||
            (keycode == 1<<25) ||
            (keycode == 1<<26) ||
            (keycode == 1<<27) ||
            (keycode == 1<<28) ||
            (keycode == 1<<29) ||
            (keycode == 1<<30))
        {
            return true;
        }
        return false;
    }
}
