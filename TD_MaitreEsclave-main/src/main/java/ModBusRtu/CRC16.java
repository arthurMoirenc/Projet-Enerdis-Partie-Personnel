package ModBusRtu;

public class CRC16 {
    public int calculCrc16(byte[] octets) {
        int crc = 0xffff;
        for (int p = 0; p < octets.length; p++) {
            crc ^= (octets[p] & 0xFF);
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }

    public int getInitialValue() {
        return 0xffff;
    }

    public int getStdPoly() {
        return 0xA001;
    }

    public int calculCrc16(byte[] tramWithoutCRC16, int initialValue, int stdPoly) {
        int crc = initialValue;
        for (int p = 0; p < tramWithoutCRC16.length; p++) {
            crc ^= (tramWithoutCRC16[p] & 0xFF);
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >> 1) ^ stdPoly;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }
}
