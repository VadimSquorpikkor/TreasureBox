package com.squorpikkor.app.treasurebox.crypto;

import java.security.GeneralSecurityException;

/**Класс для шифрования / дешифрования строки. Использует симметричное шифрование AES.
 * В основе класс AESCrypt (com.scottyab:aescrypt:0.0.1 см. GitHub: bagas-adi / AESCrypt-Android)*/
public class Encrypter2 {

    /**Пароль по умолчанию*/
    public static final String DEFAULT_PASSWORD = "password";

    /**Зашифровывает строку симметричным шифрованием AES
     *
     * @param password пароль
     * @param message строка для шифрования
     * @return зашифрованная строка
     */
    public static String encrypt(String password, String message) {
        if (message.equals("")) return "";
        String encryptedMsg = "";
        try {
            encryptedMsg = AESCrypt.encrypt(password, message);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return encryptedMsg;
    }

    /**Зашифровывает строку симметричным шифрованием AES. Используется пароль по умолчанию
     *
     * @param message строка для шифрования
     * @return зашифрованная строка
     */
    public static String encrypt_(String message) {
        return encrypt(DEFAULT_PASSWORD, message);
    }

    /**Расшифровывает строку, зашифрованную симметричным шифрованием AES
     *
     * @param password пароль
     * @param encryptedMsg зашифрованная строка
     * @return расшифрованная строка
     */
    public static String decrypt(String password, String encryptedMsg) {
        if (encryptedMsg.equals("")) return "";
        String messageAfterDecrypt = "";
        try {
            messageAfterDecrypt = AESCrypt.decrypt(password, encryptedMsg);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return messageAfterDecrypt;
    }

    /**Расшифровывает строку, зашифрованную симметричным шифрованием AES.
     * Используется пароль по умолчанию
     *
     * @param encryptedMsg зашифрованная строка
     * @return расшифрованная строка
     */
    public static String decrypt_(String encryptedMsg) {
        return decrypt(DEFAULT_PASSWORD, encryptedMsg);
    }

}
