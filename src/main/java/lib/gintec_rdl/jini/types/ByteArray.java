package lib.gintec_rdl.jini.types;

import java.util.Base64;

/**
 * A simple class for working with byte arrays
 */
public class ByteArray {
    private byte[] bytes;

    public ByteArray(){
        this(null);
    }

    public ByteArray(byte[] bytes){
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] fromBase64(){
        return Base64.getDecoder().decode(bytes);
    }

    public String fromBase64ToString(){
        return new String(fromBase64());
    }
}
