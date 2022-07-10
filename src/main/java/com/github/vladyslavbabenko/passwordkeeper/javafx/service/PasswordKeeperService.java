package com.github.vladyslavbabenko.passwordkeeper.javafx.service;

import com.github.vladyslavbabenko.passwordkeeper.entity.PasswordEntity;
import com.github.vladyslavbabenko.passwordkeeper.util.AESUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Comparator;
import java.util.List;

@Service
public class PasswordKeeperService {
    @Value("${filePath}")
    private String filePath;
    private final AESUtils aesUtils;
    private final ObservableList<PasswordEntity> passwords = FXCollections.observableArrayList();

    @Autowired
    public PasswordKeeperService(AESUtils aesUtils) {
        this.aesUtils = aesUtils;
    }

    /**
     * Adds entry to ObservableList<PasswordEntity>
     *
     * @param passwordEntity new entry
     */
    public void addEntry(PasswordEntity passwordEntity) {
        passwords.add(passwordEntity);
    }

    /**
     * Updates the file on the computer where the entries are located and sets ObservableList<PasswordEntity> up-to-date state
     */
    public void saveEntry() {
        serializePasswordEntities(passwords);
        deserializePasswordEntities();
    }

    /**
     * Deletes the old entry and saves the updated version of the entry
     *
     * @param updatedPasswordEntity updated entry
     */
    public void editEntry(PasswordEntity updatedPasswordEntity) {
        deleteEntry(updatedPasswordEntity.getId());
        addEntry(updatedPasswordEntity);
    }

    /**
     * Deletes the entry if it exists
     *
     * @param id id of the entry to be deleted
     */
    public void deleteEntry(int id) {
        passwords.removeIf(passwordEntityFormList -> passwordEntityFormList.getId() == id);
    }

    /**
     * Calculates the next ID based on the maximum ID of an existing entry
     *
     * @return max ID + 1
     */
    public int getEntryId() {
        return passwords.stream()
                .max(Comparator.comparing(PasswordEntity::getId))
                .map(passwordEntity -> passwordEntity.getId() + 1)
                .orElse(1);
    }

    /**
     * Generates a random string value
     *
     * @param length generated string length
     * @return generated random string using characters (a-z, 0-9, A-Z, #-*)
     */
    public String generatePassword(int length) {
        char[][] symbols = {{'a', 'z'}, {'0', '9'}, {'A', 'Z'}, {'#', '*'}};
        return new RandomStringGenerator.Builder().withinRange(symbols).build().generate(length);
    }

    /**
     * @return up-to-date ObservableList<PasswordEntity>
     */
    public ObservableList<PasswordEntity> getPasswords() {
        deserializePasswordEntities();
        return passwords;
    }

    /**
     * Encrypts input string via AES
     *
     * @param inputString string to encrypt
     * @return encrypted string via AES
     */
    private String encryptString(String inputString) {
        return aesUtils.getEncryptString(inputString);
    }

    /**
     * Decrypts input string via AES
     *
     * @param inputString string to decrypt
     * @return decrypted string via AES
     */
    private String decryptString(String inputString) {
        return aesUtils.getDecryptString(inputString);
    }

    /**
     * Encrypts each field of passwordEntity via AES
     *
     * @param passwordEntity passwordEntity to encrypt
     */
    private void encryptPasswordEntry(PasswordEntity passwordEntity) {
        passwordEntity.setTitle(encryptString(passwordEntity.getTitle()));
        passwordEntity.setPassword(encryptString(passwordEntity.getPassword()));
    }

    /**
     * Decrypts each field of passwordEntity via AES
     *
     * @param passwordEntity passwordEntity to decrypt
     */
    private void decryptPasswordEntry(PasswordEntity passwordEntity) {
        passwordEntity.setTitle(decryptString(passwordEntity.getTitle()));
        passwordEntity.setPassword(decryptString(passwordEntity.getPassword()));
    }

    /**
     * Serializes an ObservableList<PasswordEntity> to a file on the computer.
     *
     * @param passwords ObservableList to serialize
     */
    private void serializePasswordEntities(ObservableList<PasswordEntity> passwords) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(passwords.stream().peek(this::encryptPasswordEntry).toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializes ObservableList<PasswordEntity> from a file on a computer
     */
    private void deserializePasswordEntities() {
        if (!passwords.isEmpty()) {
            passwords.clear();
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            List<PasswordEntity> passwordEntities = (List<PasswordEntity>) objectInputStream.readObject();
            passwordEntities.stream().peek(this::decryptPasswordEntry).forEach(this::addEntry);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}