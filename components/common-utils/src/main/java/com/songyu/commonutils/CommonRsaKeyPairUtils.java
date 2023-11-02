package com.songyu.commonutils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <p>
 * 通用 rsa 密钥对工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 9:09
 */
public class CommonRsaKeyPairUtils {

    /**
     * 生成 RSA 密钥对
     *
     * @return RSA 密钥对
     */
    public static KeyPair generateRsaKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            Security.addProvider(new BouncyCastleProvider());
            keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("生成 RSA 密钥对失败", e);
        }
        try {
            keyPairGenerator.initialize(new RSAKeyGenParameterSpec(4096, RSAKeyGenParameterSpec.F4));
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("生成 RSA 密钥对失败", e);
        }
        return keyPairGenerator.genKeyPair();
    }

    public static String exportX509PublicPEMKey(PublicKey publicKey) {
        return toPem(getX509KeySpec(publicKey).getEncoded(), true);
    }

    private static X509EncodedKeySpec getX509KeySpec(PublicKey publicKey) {
        return new X509EncodedKeySpec(publicKey.getEncoded());
    }

    public static String exportPKCS8PrivatePEMKey(PrivateKey privateKey) {
        return toPem(getPKCS8KeySpec(privateKey).getEncoded(), false);
    }

    private static PKCS8EncodedKeySpec getPKCS8KeySpec(PrivateKey privateKey) {
        KeyFactory rsa;
        try {
            rsa = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        PKCS8EncodedKeySpec keySpec;
        try {
            return rsa.getKeySpec(privateKey, PKCS8EncodedKeySpec.class);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static String exportX509PublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(getX509KeySpec(publicKey).getEncoded());
    }

    public static String exportPKCS8PrivateKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(getPKCS8KeySpec(privateKey).getEncoded());
    }

    private static String toPem(byte[] bytes, boolean isPublic) {
        String keyType = isPublic ? "PUBLIC" : "PRIVATE";
        String str = Base64.getEncoder().encodeToString(bytes);
        StringBuilder stringBuilder = new StringBuilder(str);
        for (int i = 64; i <= str.length(); i += 65) {
            stringBuilder.insert(i, "\n");
        }
        return String.format("-----BEGIN %s KEY-----\n%s\n-----END %s KEY-----", keyType, stringBuilder, keyType);
    }

    public static PublicKey importX509PublicKey(String x509Key) {
        KeyFactory rsa;
        try {
            rsa = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("根据 PEM 格式密钥生成公钥对象失败", e);
        }
        byte[] dePem = DatatypeConverter.parseBase64Binary(
                x509Key
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
        );
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(dePem);
            return rsa.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("根据 PEM 格式密钥生成公钥对象失败", e);
        }
    }

    public static PrivateKey importPKCS8PrivateKey(String privateKey) {
        return null;
    }
}
