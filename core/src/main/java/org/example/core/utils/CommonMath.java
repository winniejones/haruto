package org.example.core.utils;

public class CommonMath {
    /**
     * Convert bytes to a human readable format.
     * Eg: 26673720 -> 25.44MB
     * Credit: icza, stackoverflow.com/questions/3758606/#24805871
     *
     * @param bytes
     * @return bytes with SI postfix
     */
    public static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int z = (63 - Long.numberOfLeadingZeros(bytes)) / 10;
        return String.format("%.2f%sB", (double) bytes / (1L << (z * 10)), " KMGTPE".charAt(z));
    }
    /**
     * Round value with specified precision.
     *
     * @param value
     * @param precision number of decimal points
     * @return rounded value
     */
    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static float angleTo(float x1, float y1, float x2, float y2) {
        return (float) -(Math.atan2(x2 - x1, y2 - y1)) - 1.57f;
    }
}
