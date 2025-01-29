package com.ezycollect.payment.service;

import com.ezycollect.payment.domain.entity.Payment;
import com.ezycollect.payment.repository.PaymentRepository;
import com.ezycollect.payment.util.RsaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;

@AutoConfigureMockMvc
@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void givenPayment_whenSave_thenReturnPayment() throws Exception {
        Payment payment = new Payment();
        payment.setCardNumber("441256893254");
        payment.setFirstName("Test");
        payment.setLastName("Beta");
        payment.setZipCode("55893");

        //when
        ResultActions response2 = mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment)));

        //then
        response2.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(payment.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(payment.getLastName())))
                .andExpect(jsonPath("$.zipCode",
                        is(payment.getZipCode())));

    }



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
