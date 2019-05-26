package cn.core.utils;

import org.python.util.PythonInterpreter;

public class TestPython {
    public static void main(String args[]) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("import sys ");
        interpreter.exec("a = input('Enter a:')");
        interpreter.exec("b = input('Enter b:')");
        interpreter.exec("print('%s * %s = %s' %(a, b, a*b))");
    }
}
