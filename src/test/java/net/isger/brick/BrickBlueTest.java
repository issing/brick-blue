package net.isger.brick;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.isger.brick.blue.ClassSeal;
import net.isger.brick.blue.Marks.ACCESS;
import net.isger.brick.blue.Marks.MISC;
import net.isger.brick.blue.Marks.OPCODES;
import net.isger.brick.blue.Marks.TYPE;
import net.isger.brick.blue.Marks.VERSION;
import net.isger.brick.blue.MethodSeal;

public class BrickBlueTest extends TestCase {

    public BrickBlueTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(BrickBlueTest.class);
    }

    public static interface TestA {

        public int getId();

        public String getName();

    }

    public static class TestB implements TestA {

        public int getId() {
            return 2;
        }

        public String getName() {
            return "TestB";
        }

    }

    public static interface TestC {

        /**
         * 注册终端
         * 
         * @param terminal
         * @return
         */
        public Object register(Object terminal);

        /**
         * 绑定终端
         * 
         * @param userTerminal
         * @return
         */
        public Object bind(Object userTerminal);

        /**
         * 终端
         *
         * @param terminal
         * @return
         */
        public Object terminal(Object terminal);

        /**
         * 用户终端
         * 
         * @param userTerminal
         * @return
         */
        public Map<String, Object> userTerminal(Object userTerminal);

        /**
         * 用户终端
         * 
         * @param userTerminal
         * @return
         */
        public List<Map<String, Object>> userTerminals(Object userTerminal);

        /**
         * 变更状态
         * 
         * @param terminal
         * @return
         */
        public Object status(Object terminal);

        /**
         * 解绑终端
         * 
         * @param userTerminal
         */
        public Object unbind(Object userTerminal);

    }

    public void testExample() throws Exception {
        Class<?> exampleClass = null;
        ClassSeal cs = null;
        MethodSeal ms = null;
        // public class Example
        cs = ClassSeal.create(VERSION.V0104.value, ACCESS.PUBLIC.value,
                "Example", TYPE.OBJECT.name);
        {
            // public Example()
            ms = cs.makeMethod(ACCESS.PUBLIC.value, TYPE.VOID.name, MISC.INIT);
            {
                // super();
                ms.coding(MISC.THIS, MISC.METH_SUPER);
                // System.out.println("greeting");
                ms.markConst("greeting", "This is blue of isger(init).");
                ms.coding(MISC.OUT, MISC.METH_PRINTLN, "greeting");
            }
            // public static void main(String[])
            ms = cs.makeMethod(ACCESS.PUBLIC.value | ACCESS.STATIC.value,
                    TYPE.VOID.name, "main", TYPE.STRINGS.name);
            {
                // ms.markOperate("println(int)", TYPE.PRINTSTREAM.name,
                // OPCODES.INVOKEVIRTUAL.value, "void", "println",
                // TYPE.INT.name);
                // ms.markCoding("args.length", "array", "length", "args[0]");
                // ms.coding("out", "println(int)", "args.length");

                // System.out.println(new Example());
                ms.markOperate("Example()", "Example",
                        OPCODES.INVOKESPECIAL.value, TYPE.VOID.name, MISC.INIT);
                ms.markCoding("new Example()", MISC.NEW, "Example()");
                ms.coding(MISC.OUT, MISC.METH_PRINTLN, "new Example()");

                // Example e = new Example();
                String e = ms.coding(MISC.NEW, "Example()");
                // System.out.println(e);
                ms.coding(MISC.OUT, MISC.METH_PRINTLN, e);
                // System.out.println(Example.class);
                ms.coding(MISC.OUT, MISC.METH_PRINTLN, MISC.CLASS);
            }

        }

        byte[] code = net.isger.brick.blue.Compiler.compile(cs);
        exampleClass = new BlueClassLoader().load("Example", code, 0,
                code.length);
        Method[] methods = exampleClass.getMethods();
        methods[0].invoke(null, new Object[] { new String[] {} });
    }

    public void testBlue() throws Exception {
        // public class Blue
        ClassSeal cs = ClassSeal.create(VERSION.V0104.value,
                ACCESS.PUBLIC.value, "Blue", TYPE.OBJECT.name,
                TestA.class.getName());
        {
            // public Blue()
            MethodSeal ms = cs.makeMethod(ACCESS.PUBLIC.value, "void",
                    "<init>");
            {
                // super();
                ms.coding("this", "super()");
            }
            // public int getId()
            ms = cs.makeMethod(ACCESS.PUBLIC.value, "int", "getId");
            {
                // return 1;
                ms.markConst("id", 1);
                ms.coding("return", null, "id");
            }
            // public String getName()
            ms = cs.makeMethod(ACCESS.PUBLIC.value, TYPE.STRING.name,
                    "getName");
            {
                // return "Blue";
                ms.markConst("name", "Blue");
                ms.coding("return", null, "name");
            }
        }

        byte[] code = net.isger.brick.blue.Compiler.compile(cs);
        Class<?> clazz = new BlueClassLoader().load("Blue", code, 0,
                code.length);
        TestA instance = (TestA) clazz.newInstance();
        System.out.println(instance.getId());
        System.out.println(instance.getName());

        cs = ClassSeal.create(VERSION.V0104.value, ACCESS.PUBLIC.value, "BlueB",
                TestB.class.getName());
        {
            // public BlueB()
            MethodSeal ms = cs.makeMethod(ACCESS.PUBLIC.value, "void",
                    "<init>");
            {
                // super();
                ms.coding("this", "super()");
            }
            // public int getId()
            ms = cs.makeMethod(ACCESS.PUBLIC.value, "int", "getId");
            {
                // return 1;
                ms.markConst("id", 1);
                ms.coding("return", null, "id");
            }
            // public String getName()
            ms = cs.makeMethod(ACCESS.PUBLIC.value, TYPE.STRING.name,
                    "getName");
            {
                // return super.getName();
                ms.markOperate("getName()", TestB.class.getName(),
                        OPCODES.INVOKESPECIAL.value, TYPE.STRING.name,
                        "getName");
                ms.markCoding("super.getName()", "this", "getName()");
                ms.coding("return", null, "super.getName()");
            }
        }
        code = net.isger.brick.blue.Compiler.compile(cs);
        clazz = new BlueClassLoader().load("BlueB", code, 0, code.length);
        instance = (TestA) clazz.newInstance();
        System.out.println(instance.getId());
        System.out.println(instance.getName());
    }

    private static class BlueClassLoader extends ClassLoader {

        public Class<?> load(String name, byte[] code, int off, int len) {
            return this.defineClass(name, code, off, len);
        }

    }

}
