package com.ezycollect.payment.service;

import com.ezycollect.payment.util.RsaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PaymentServiceTest {

    @Test
    void givenRSAKeyPairAndData_whenEncryptAndDecrypt_then() throws Exception {

        int rsaKeySize = 2048;
        KeyPair keyPair = RsaService.generateRsaKeyPair(rsaKeySize);
        assertNotNull(keyPair);

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        String plainCardNumber = "452633651548";
        //encrypt
        RSAPublicKey rsaPublicKey = RsaService.base64StringToPublicKey(base64PublicKey);
        String encryptedData = RsaService.encryptDataRsa(plainCardNumber, rsaPublicKey);

        //decrypt
        RSAPrivateKey rsaPrivateKey = RsaService.base64StringToPrivateKey(base64PrivateKey);
        String decryptedData = RsaService.decryptDataRsa(encryptedData, rsaPrivateKey);

        //assert
        assertEquals(plainCardNumber, decryptedData);
    }

}
