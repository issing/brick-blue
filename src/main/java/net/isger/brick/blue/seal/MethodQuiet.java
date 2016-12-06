package net.isger.brick.blue.seal;

import net.isger.brick.blue.Marks.TYPE;
import net.isger.brick.blue.MethodSeal;

class MethodQuiet extends MethodSeal {

    private static final String PREFIX_CLASS_NAME = "Brick$blue$className_";

    public MethodQuiet(String owner, int access, String type, String name,
            String[] argTypes) {
        super(owner, access, type, name, argTypes);
    }

    public void markConst(String alias, Object value) {
        if (value != null && value instanceof TYPE) {
            TYPE type = (TYPE) value;
            if (type.sort == TYPE.OBJECT.sort) {
                String name = PREFIX_CLASS_NAME + type.name;
                this.markConst(name, type.name);
                this.markCoding(alias, null, "class", name);
                return;
            }
        }
        super.markConst(alias, value);
    }

}
