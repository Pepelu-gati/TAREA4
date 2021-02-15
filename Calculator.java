package com.Bravo_Gonzalez_JoseLuis.calculator;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.awt.Color;
import javax.swing.*;
import java.lang.Math;
/**
 * Clase implementa una calculadora gráfica
 * @author josele desarrollo de hourizegai
 * @version 2.0
 */
 public class Calculator {

    private static final int WINDOW_WIDTH = 410;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 70;
    private static final int MARGIN_X = 20;
    private static final int MARGIN_Y = 60;

    private JFrame ventana; // Main window
    private JComboBox<String> comboCalcType, comboTheme;
    private JTextField inText; // Input
    private JButton btnC, btnBack, btnMod, btnDiv, btnMul, btnSub, btnAdd,
            btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
            btnPoint, btnEqual, btnRoot, btnPower, btnLog;

    private char opt = ' '; // Save the operator
    private boolean go = true; // For calculate with Opt != (=)
    private boolean addWrite = true; // Connect numbers in display
    private double val = 0; // Save the value typed for calculation

    /*
        Mx Calculator: 
        X = Row
        Y = Column
    
        +-------------------+
        |   +-----------+   |   y[0]
        |   |           |   |
        |   +-----------+   |
        |                   |
        |   C  <-   %   /   |   y[1]
        |   7   8   9   *   |   y[2]
        |   4   5   6   -   |   y[3]
        |   1   2   3   +   |   y[4]
        |   .   0     =     |   y[5]
        +-------------------+
         x[0] x[1] x[2] x[3]
    
    */
    
    /*    
        +-------------------+
        |   +-----------+   |   y[0]
        |   |           |   |
        |   +-----------+   |
        |                   |
        |   0   1   1   3   |   y[1]
        |   4   5   6   7   |   y[2]
        |   8   9   10  11  |   y[3]
        |   12  13  14  15  |   y[4]
        |   16  17    18    |   y[5]
        +-------------------+
         x[0] x[1] x[2] x[3]
    
    */
    /**
     * Constructor de la clase
     */
    public Calculator() {
        set_Ventana(new JFrame("Calculator"));
        get_Ventana().setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        get_Ventana().setLocationRelativeTo(null); // Move window to center

        setComboTheme(initCombo(new String[]{"Simple", "Colored"}, 230, 30, "Theme", themeSwitchEventConsumer));

        comboCalcType = initCombo(new String[]{"Standard", "Scientific"}, 20, 30, "Calculator type", calcTypeSwitchEventConsumer);

        int[] x = {MARGIN_X, MARGIN_X + 90, 200, 290, 380};
        int[] y = {MARGIN_Y, MARGIN_Y + 100, MARGIN_Y + 180, MARGIN_Y + 260, MARGIN_Y + 340, MARGIN_Y + 420};

        setInText(new JTextField("0"));
        getInText().setBounds(x[0], y[0], 350, 70);
        getInText().setEditable(false);
        getInText().setBackground(Color.WHITE);
        getInText().setFont(new Font("Comic Sans MS", Font.PLAIN, 33));
        get_Ventana().add(getInText());

        btnC = initBtn("C", x[0], y[1], event -> {
            repaintFont();
            getInText().setText("0");
            setOpt(' ');
            setVal(0);
        });

        btnBack = initBtn("<-", x[1], y[1], event -> {
            repaintFont();
            String str = getInText().getText();
            StringBuilder str2 = new StringBuilder();
            for (int i = 0; i < (str.length() - 1); i++) {
                str2.append(str.charAt(i));
            }
            if (str2.toString().equals("")) {
                getInText().setText("0");
            } else {
                getInText().setText(str2.toString());
            }
        });

        btnMod = initBtn("%", x[2], y[1], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(calc(getVal(), getInText().getText(), getOpt(), 0));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }
                    setOpt('%');
                    setGo(false);
                    setAddWrite(false);
                }
        });

        btnDiv = initBtn("/", x[3], y[1], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(calc(getVal(), getInText().getText(), getOpt(), 0));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }
                    setOpt('/');
                    setGo(false);
                    setAddWrite(false);
                } else {
                    setOpt('/');
                }
        });

        btn7 = initBtn("7", x[0], y[2], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("7");
                } else {
                    getInText().setText(getInText().getText() + "7");
                }
            } else {
                getInText().setText("7");
                setAddWrite(true);
            }
            setGo(true);
        });

        btn8 = initBtn("8", x[1], y[2], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("8");
                } else {
                    getInText().setText(getInText().getText() + "8");
                }
            } else {
                getInText().setText("8");
                setAddWrite(true);
            }
            setGo(true);
        });

        btn9 = initBtn("9", x[2], y[2], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("9");
                } else {
                    getInText().setText(getInText().getText() + "9");
                }
            } else {
                getInText().setText("9");
                setAddWrite(true);
            }
            setGo(true);
        });

        btnMul = initBtn("*", x[3], y[2], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(calc(getVal(), getInText().getText(), getOpt(), 0));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }
                    setOpt('*');
                    setGo(false);
                    setAddWrite(false);
                } else {
                    setOpt('*');
                }
        });

        btn4 = initBtn("4", x[0], y[3], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("4");
                } else {
                    getInText().setText(getInText().getText() + "4");
                }
            } else {
                getInText().setText("4");
                setAddWrite(true);
            }
            setGo(true);
        });

        btn5 = initBtn("5", x[1], y[3], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("5");
                } else {
                    getInText().setText(getInText().getText() + "5");
                }
            } else {
                getInText().setText("5");
                setAddWrite(true);
            }
            setGo(true);
        });

        btn6 = initBtn("6", x[2], y[3], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("6");
                } else {
                    getInText().setText(getInText().getText() + "6");
                }
            } else {
                getInText().setText("6");
                setAddWrite(true);
            }
            setGo(true);
        });

        btnSub = initBtn("-", x[3], y[3], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(calc(getVal(), getInText().getText(), getOpt(), 0));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }

                    setOpt('-');
                    setGo(false);
                    setAddWrite(false);
                } else {
                    setOpt('-');
                }
        });

        btn1 = initBtn("1", x[0], y[4], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("1");
                } else {
                    getInText().setText(getInText().getText() + "1");
                }
            } else {
                getInText().setText("1");
                setAddWrite(true);
            }
            setGo(true);
        });

        btn2 = initBtn("2", x[1], y[4], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("2");
                } else {
                    getInText().setText(getInText().getText() + "2");
                }
            } else {
                getInText().setText("2");
                setAddWrite(true);
            }
            setGo(true);
        });

        btn3 = initBtn("3", x[2], y[4], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("3");
                } else {
                    getInText().setText(getInText().getText() + "3");
                }
            } else {
                getInText().setText("3");
                setAddWrite(true);
            }
            setGo(true);
        });

        btnAdd = initBtn("+", x[3], y[4], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(calc(getVal(), getInText().getText(), getOpt(), 0));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }
                    setOpt('+');
                    setGo(false);
                    setAddWrite(false);
                } else {
                    setOpt('+');
                }
        });

        btnPoint = initBtn(".", x[0], y[5], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (!getInText().getText().contains(".")) {
                    getInText().setText(getInText().getText() + ".");
                }
            } else {
                getInText().setText("0.");
                setAddWrite(true);
            }
            setGo(true);
        });

        btn0 = initBtn("0", x[1], y[5], event -> {
            repaintFont();
            if (isAddWrite()) {
                if (Pattern.matches("[0]*", getInText().getText())) {
                    getInText().setText("0");
                } else {
                    getInText().setText(getInText().getText() + "0");
                }
            } else {
                getInText().setText("0");
                setAddWrite(true);
            }
            setGo(true);
        });

        btnEqual = initBtn("=", x[2], y[5], event -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(calc(getVal(), getInText().getText(), getOpt(), 0));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }
                    setOpt('=');
                    setAddWrite(false);
                }
        });
        btnEqual.setSize(2 * BUTTON_WIDTH + 10, BUTTON_HEIGHT);

        btnRoot = initBtn("√", x[4], y[1], event -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(Math.sqrt(Double.parseDouble(getInText().getText())));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }
                    setOpt('√');
                    setAddWrite(false);
                }
        });
        btnRoot.setVisible(false);

        btnPower = initBtn("pow", x[4], y[2], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(calc(getVal(), getInText().getText(), getOpt(), 0));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }
                    setOpt('^');
                    setGo(false);
                    setAddWrite(false);
                } else {
                    setOpt('^');
                }
        });
        btnPower.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        btnPower.setVisible(false);

        setBtnLog(initBtn("ln", x[4], y[3], event -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", getInText().getText()))
                if (isGo()) {
                    setVal(Math.log(Double.parseDouble(getInText().getText())));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(getVal()))) {
                        getInText().setText(String.valueOf((int) getVal()));
                    } else {
                        getInText().setText(String.valueOf(getVal()));
                    }
                    setOpt('l');
                    setAddWrite(false);
                }
        }));
        getBtnLog().setVisible(false);

        get_Ventana().setLayout(null);
        get_Ventana().setResizable(false);
        get_Ventana().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close button clicked? = End The process
        get_Ventana().setVisible(true);
    }
    /**
     * Función privada de la clase
     * @param items
     * @param x
     * @param y
     * @param toolTip
     * @param consumerEvent
     * @return
     */
    private JComboBox<String> initCombo(String[] items, int x, int y, String toolTip, Consumer consumerEvent) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBounds(x, y, 140, 25);
        combo.setToolTipText(toolTip);
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        combo.addItemListener(consumerEvent::accept);
        get_Ventana().add(combo);

        return combo;
    }
    /**
     * Funcion privada de clase
     * @param label
     * @param x
     * @param y
     * @param event
     * @return
     */
    private JButton initBtn(String label, int x, int y, ActionListener event) {
        JButton btn = new JButton(label);
        btn.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        btn.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(event);
        get_Ventana().add(btn);

        return btn;
    }
/**
 * Funcion que realiza los calculos
 * @param x
 * @param input
 * @param opt
 * @param cantidad
 * @return
 */
    public double calc(double x, String input, char opt, float cantidad) {
        getInText().setFont(getInText().getFont().deriveFont(Font.PLAIN));
        double y = Double.parseDouble(input);
        switch (opt) {
            case '+':
                return x + y;
            case '-':
                return x - y;
            case '*':
                return x * y;
            case '/':
                return x / y;
            case '%':
                return x % y;
            case '^':
                return Math.pow(x, y);
            default:
                getInText().setFont(getInText().getFont().deriveFont(Font.PLAIN));
                return y;
        }
    }
    /**
     * Funcion privada de la clase
     */
    private void repaintFont() {
        getInText().setFont(getInText().getFont().deriveFont(Font.PLAIN));
    }
    /**
     * Funcion privada de la clase
     */
    private Consumer<ItemEvent> calcTypeSwitchEventConsumer = event -> {
        if (event.getStateChange() != ItemEvent.SELECTED) return;

        String selectedItem = (String) event.getItem();
        switch (selectedItem) {
            case "Standard":
                get_Ventana().setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                btnRoot.setVisible(false);
                btnPower.setVisible(false);
                getBtnLog().setVisible(false);
                break;
            case "Scientific":
                get_Ventana().setSize(WINDOW_WIDTH + 80, WINDOW_HEIGHT);
                btnRoot.setVisible(true);
                btnPower.setVisible(true);
                getBtnLog().setVisible(true);
                break;
        }
    };
    /**
     * Función privada de la clase
     */
    private Consumer<ItemEvent> themeSwitchEventConsumer = event -> {
        if (event.getStateChange() != ItemEvent.SELECTED) return;

        String selectedTheme = (String) event.getItem();
        switch (selectedTheme) {
            case "Simple":
                btnC.setBackground(null);
                btnBack.setBackground(null);
                btnMod.setBackground(null);
                btnDiv.setBackground(null);
                btnMul.setBackground(null);
                btnSub.setBackground(null);
                btnAdd.setBackground(null);
                btnRoot.setBackground(null);
                getBtnLog().setBackground(null);
                btnPower.setBackground(null);
                btnEqual.setBackground(null);
                btn0.setBackground(null);
                btn1.setBackground(null);
                btn2.setBackground(null);
                btn3.setBackground(null);
                btn4.setBackground(null);
                btn5.setBackground(null);
                btn6.setBackground(null);
                btn7.setBackground(null);
                btn8.setBackground(null);
                btn9.setBackground(null);
                btnPoint.setBackground(null);

                btnC.setForeground(Color.BLACK);
                btnBack.setForeground(Color.BLACK);
                btnMod.setForeground(Color.BLACK);
                btnDiv.setForeground(Color.BLACK);
                btnMul.setForeground(Color.BLACK);
                btnSub.setForeground(Color.BLACK);
                btnAdd.setForeground(Color.BLACK);
                btnEqual.setForeground(Color.BLACK);
                getBtnLog().setForeground(Color.BLACK);
                btnPower.setForeground(Color.BLACK);
                btnRoot.setForeground(Color.BLACK);
                break;
            case "Colored":
                btnC.setBackground(Color.RED);
                btnBack.setBackground(Color.ORANGE);
                btnMod.setBackground(Color.GREEN);
                btnDiv.setBackground(Color.PINK);
                btnMul.setBackground(Color.PINK);
                btnSub.setBackground(Color.PINK);
                btnAdd.setBackground(Color.PINK);
                btnRoot.setBackground(Color.PINK);
                getBtnLog().setBackground(Color.PINK);
                btnPower.setBackground(Color.PINK);
                btnEqual.setBackground(Color.BLUE);
                btn0.setBackground(Color.WHITE);
                btn1.setBackground(Color.WHITE);
                btn2.setBackground(Color.WHITE);
                btn3.setBackground(Color.WHITE);
                btn4.setBackground(Color.WHITE);
                btn5.setBackground(Color.WHITE);
                btn6.setBackground(Color.WHITE);
                btn7.setBackground(Color.WHITE);
                btn8.setBackground(Color.WHITE);
                btn9.setBackground(Color.WHITE);
                btnPoint.setBackground(Color.WHITE);

                btnC.setForeground(Color.WHITE);
                btnBack.setForeground(Color.WHITE);
                btnMod.setForeground(Color.WHITE);
                btnDiv.setForeground(Color.WHITE);
                btnMul.setForeground(Color.WHITE);
                btnSub.setForeground(Color.WHITE);
                btnAdd.setForeground(Color.WHITE);
                btnEqual.setForeground(Color.WHITE);
                getBtnLog().setForeground(Color.WHITE);
                btnPower.setForeground(Color.WHITE);
                btnRoot.setForeground(Color.WHITE);
                break;
        }
    };
    /**
     * Funcion get de ventana
     * @return
     */
	private JFrame get_Ventana() {
		return ventana;
	}
	/**
	 * Funcion set de ventana
	 * @param ventana
	 */
	private void set_Ventana(JFrame ventana) {
		this.ventana = ventana;
	}
	/**
	 * Funcion get de comboTheme
	 */
	private JComboBox<String> getComboTheme() {
		return comboTheme;
	}
/**
 * Funcion set de comboTheme
 * @param comboTheme
 */
	private void setComboTheme(JComboBox<String> comboTheme) {
		this.comboTheme = comboTheme;
	}
	/**
	 * Funcion get de inText
	 * @return
	 */
	private JTextField getInText() {
		return inText;
	}
	/**
	 * Funcion set de inText
	 * @param inText
	 */
	private void setInText(JTextField inText) {
		this.inText = inText;
	}
	/**
	 * Funcion get de BtnLog
	 * @return
	 */
	private JButton getBtnLog() {
		return btnLog;
	}
	/**
	 * Funcion set btnLog
	 * @param btnLog
	 */
	private void setBtnLog(JButton btnLog) {
		this.btnLog = btnLog;
	}
	/**
	 * Funcion get de opt
	 * @return
	 */
	private char getOpt() {
		return opt;
	}
	/**
	 * Funcion set de opt
	 * @param opt
	 */
	private void setOpt(char opt) {
		this.opt = opt;
	}
	/**
	 * Funcion get de go
	 * @return
	 */
	private boolean isGo() {
		return go;
	}
	/**
	 * Funcion set de go
	 * @param go
	 */
	private void setGo(boolean go) {
		this.go = go;
	}
	/**
	 * Funcion get de addWrite
	 * @return
	 */
	private boolean isAddWrite() {
		return addWrite;
	}
	/**
	 * Funcion set de addWrite
	 * @param addWrite
	 */
	private void setAddWrite(boolean addWrite) {
		this.addWrite = addWrite;
	}
	/**
	 * Funcion get de val
	 * @return
	 */
	private double getVal() {
		return val;
	}
	/**
	 * Función set de val
	 * @param val
	 */
	private void setVal(double val) {
		this.val = val;
	}
	/**
	 * Función de ejecucucion de la clase
	 */
	public static void main(String[] args) {
        new Calculator();
    }
}
