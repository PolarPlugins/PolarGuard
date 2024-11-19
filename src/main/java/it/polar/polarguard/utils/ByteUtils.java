package it.polar.polarguard.utils;

import com.github.luben.zstd.Zstd;

public class ByteUtils {
    public static byte[] compressBytes(byte[] input) {
        return Zstd.compress(input);
    }

    public static byte[] decompressBytes(byte[] input) {
        return Zstd.decompress(input, (int) Zstd.decompressedSize(input));
    }
}
