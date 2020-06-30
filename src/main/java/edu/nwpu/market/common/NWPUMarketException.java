package edu.nwpu.market.common;

public class NWPUMarketException extends RuntimeException {

    public NWPUMarketException() {
    }

    public NWPUMarketException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new NWPUMarketException(message);
    }

}
