package com.astlix.es_android_c72_astlixpruebas.tools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Utils {
    @SuppressWarnings("ConstantConditions")
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public static String getSku(String epctemp) {
        int valor=0;
        for(int i=7, j=0; j<7; i++, j++) {
            int up = val_dec(Integer.parseInt(epctemp.substring(i, i+1), 16)) & 0x3;
            up <<= 2;
            int lo = val_dec(Integer.parseInt(epctemp.substring(i+1, i+2), 16)) & 0xc;
            lo >>= 2;
            valor <<=4;
            valor += (up | lo);
        }
        return String.format(Locale.getDefault(), "%d", valor);
    }

    private static int val_dec(int tagChar) {
        int dec = tagChar - 0x30;
        if(dec>0x10) {
            dec-=7;
        }
        return dec;
    }
}
