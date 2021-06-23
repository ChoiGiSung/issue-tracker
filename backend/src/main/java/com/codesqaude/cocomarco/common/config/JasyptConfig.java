package com.codesqaude.cocomarco.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${key}")
    private String key;

    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key);
        config.setPoolSize(1);
        encryptor.setConfig(config);
        return encryptor;
    }

    public String encrypt(String password) {
        StringEncryptor stringEncryptor = stringEncryptor();
        return stringEncryptor.encrypt(password);
    }

    public String decrypt(String password) {
        StringEncryptor stringEncryptor = stringEncryptor();
        return stringEncryptor.decrypt(password);
    }
}
