package digitalpyme.crm.users.infrastructure.repository.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Instant;
import org.apache.commons.codec.binary.Base32;

public class TOTPUtil {

    private static final int TIME_STEP = 30;
    private static final int CODE_DIGITS = 6;

    public static String generateSecret() {
        SecureRandom securerandom = new SecureRandom();
        byte[] bytes = new byte[20];
        securerandom.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static boolean verifyCode(String secret, String code) {
        long interval = Instant.now().getEpochSecond() / TIME_STEP;

        for (int i = -1; i <= 1; i++) {
            if (generateTOTP(secret, interval + i).equals(code)) {
                return true;
            }
        }
        return false;
    }

    private static String generateTOTP(String secret, long interval) {
        try {
            Base32 base32 = new Base32();
            byte[] key = base32.decode(secret);

            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putLong(interval);

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(buffer.array());

            int offset = hash[hash.length - 1] & 0xF;
            int binary =
                    ((hash[offset] & 0x7F) << 24) |
                            ((hash[offset + 1] & 0xFF) << 16) |
                            ((hash[offset + 2] & 0xFF) << 8) |
                            (hash[offset + 3] & 0xFF);

            int otp = binary % (int) Math.pow(10, CODE_DIGITS);

            return String.format("%06d", otp);
        } catch (Exception e) {
            throw new RuntimeException("Error generating TOTP", e);
        }
    }
}