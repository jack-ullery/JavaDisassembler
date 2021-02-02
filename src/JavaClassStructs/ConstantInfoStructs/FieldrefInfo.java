package JavaClassStructs.ConstantInfoStructs;

import java.io.IOException;
import java.io.InputStream;
import misc.StreamFunctions;

/**
 * As defined in:
 * https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2
 *
 * @author Jack Ullery
 */
public class FieldrefInfo extends ConstantInfo {

    private final short class_index;
    private final short name_and_type_index;

    public FieldrefInfo(InputStream data) throws IOException {
        super(data);
        class_index = StreamFunctions.readShort(data);
        name_and_type_index = StreamFunctions.readShort(data);
    }

    @Override
    public String toString() {
        return String.format("FieldrefInfo: [%d, %d]", class_index, name_and_type_index);
    }

}
