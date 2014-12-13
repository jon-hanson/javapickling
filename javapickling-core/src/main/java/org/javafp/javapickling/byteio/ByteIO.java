package org.javafp.javapickling.byteio;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * ByteIO wraps either a DataInput or a DataOutput.
 */
public class ByteIO {
    public final DataInput input;
    public final DataOutput output;

    public ByteIO(DataInput input) {
        this.input = input;
        this.output = null;
    }

    public ByteIO(DataOutput output) {
        this.input = null;
        this.output = output;
    }

    public void writeString(String s) throws IOException {
        output.writeInt(s.length());
        output.writeChars(s);
    }

    public String readString() throws IOException {
        final int len = input.readInt();
        final char[] charArray = new char[len];
        for (int i = 0; i < len; ++i)
            charArray[i] = input.readChar();
        return new String(charArray);
    }
}
