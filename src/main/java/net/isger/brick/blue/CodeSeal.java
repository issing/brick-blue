package net.isger.brick.blue;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 代码图章
 * 
 * @author issing
 * 
 */
public class CodeSeal {

    private static final Format FORMAT = new SimpleDateFormat("yyyyMMddhhmmss");

    private static final String FMT_CID = "CID-%s-%d";

    private static final Object LOCKED = new Object();

    private static long CID_AMOUNT = 0;

    private String id;

    private String owner;

    private String operate;

    private String[] args;

    public CodeSeal(String owner, String operate, String[] args) {
        this.owner = owner;
        this.operate = operate;
        this.args = args;
        synchronized (LOCKED) {
            this.id = String.format(FMT_CID, FORMAT.format(new Date()),
                    CID_AMOUNT++);
            if (CID_AMOUNT == Integer.MAX_VALUE) {
                CID_AMOUNT = 0;
            }
        }
    }

    public String getId() {
        return this.id;
    }

    public String getOwner() {
        return owner;
    }

    public String getOperate() {
        return operate;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean hasRefer(String id) {
        boolean isRefer = id.equals(owner);
        if (!isRefer) {
            for (String arg : args) {
                if (id.equals(arg)) {
                    isRefer = true;
                    break;
                }
            }
        }
        return isRefer;
    }

}
