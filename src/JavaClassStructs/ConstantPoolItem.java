 package JavaClassStructs;

import JavaClassStructs.ConstantPoolInfo.ClassInfo;
import JavaClassStructs.ConstantPoolInfo.DoubleInfo;
import JavaClassStructs.ConstantPoolInfo.FieldrefInfo;
import JavaClassStructs.ConstantPoolInfo.FloatInfo;
import JavaClassStructs.ConstantPoolInfo.IntegerInfo;
import JavaClassStructs.ConstantPoolInfo.InterfaceMethodrefInfo;
import JavaClassStructs.ConstantPoolInfo.InvokeDynamicInfo;
import JavaClassStructs.ConstantPoolInfo.LongInfo;
import JavaClassStructs.ConstantPoolInfo.MethodHandleInfo;
import JavaClassStructs.ConstantPoolInfo.MethodTypeInfo;
import JavaClassStructs.ConstantPoolInfo.MethodrefInfo;
import JavaClassStructs.ConstantPoolInfo.NameAndTypeInfo;
import JavaClassStructs.ConstantPoolInfo.StringInfo;
import JavaClassStructs.ConstantPoolInfo.Utf8Info;
import JavaClassStructs.ConstantPoolItem;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import misc.DebugStream;
import misc.StreamFunctions;

/**
 * As defined in:
 * https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4
 *
 * @author Jack Ullery
 */
public abstract class ConstantPoolItem {

    private static final PrintStream debug1 = DebugStream.OFF;
    private static final PrintStream debug2 = DebugStream.OFF;

    public static ConstantPoolItem[] readArray(InputStream data, final short ucount) throws IOException {
        int count = Short.toUnsignedInt(ucount);
        ConstantPoolItem[] arr = new ConstantPoolItem[count];
        for (int i = 0; i < count; i++) {
            debug1.println("1 - Constant #" + i);
            arr[i] = create(data);
            debug1.println(arr[i]);
        }
        debug1.println("End of first loop.");
        debug2.println("Start of second loop.");
        for (int i = 0; i < count; i++) {
            debug2.println("2 - Constant #" + i);
            debug2.println(arr[i]);
            arr[i].findChild(arr);
        }
        debug2.println("End of second loop.");
        debug1.println("End of constant pool!");
        return arr;
    }

    // Builds an element of this class from the InputStream.
    public static ConstantPoolItem create(InputStream data) throws IOException {
        byte tag = StreamFunctions.readByte(data);
        switch (tag) {
            case 1:
                return new Utf8Info(data);
            case 3:
                return new IntegerInfo(data);
            case 4:
                return new FloatInfo(data);
            case 5:
                return new LongInfo(data);
            case 6:
                return new DoubleInfo(data);
            case 7:
                return new ClassInfo(data);
            case 8:
                return new StringInfo(data);
            case 9:
                return new FieldrefInfo(data);
            case 10:
                return new MethodrefInfo(data);
            case 11:
                return new InterfaceMethodrefInfo(data);
            case 12:
                return new NameAndTypeInfo(data);
            case 15:
                return new MethodHandleInfo(data);
            case 16:
                return new MethodTypeInfo(data);
            case 18:
                return new InvokeDynamicInfo(data);
            default:
                throw new IllegalArgumentException(String.format("Invalid tag (%d) recieved", tag));
        }
    }

    /* METHODS FOR SUBCLASSES */
    protected ConstantPoolItem(InputStream data) throws IOException {
        // This constructor requires subclasses to have an InputStream.
    }

    protected void checkRecursion(ConstantPoolItem[] arr, int index) {
        if (arr[index] == this) {
            throw new IllegalArgumentException("Unexpected recursive call for ConstantPool entry");
        }
    }

    /**
     * For classes that have a child, find its child from the array.
     *
     * @param arr The array that contains this ConstantInfoStruct and its
     * children.
     * @throws ClassCastException when an incorrect ConstantPoolItem type is
 pointed to.
     */
    public abstract void findChild(ConstantPoolItem[] arr);

    @Override
    public abstract String toString();
}
