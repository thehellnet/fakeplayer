package org.thehellnet.thnolg.fakeplayer.protocol;

/**
 * Created by sardylan on 23/06/16.
 */
public class Packet {

    private long sequenceNumber;
    private byte[] rawData;

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}
