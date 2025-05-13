package com.spring.zoocare.services;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public String generatePassword() {
        PasswordGenerator passwordGenerator= new PasswordGenerator();

        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "GENERIC_ERROR";
            }

            public String getCharacters() {
                return "!@#$%&()_+=?";
            }
        };

        CharacterRule specialCharsRule = new CharacterRule(specialChars);
        specialCharsRule.setNumberOfCharacters(2);

        return passwordGenerator.generatePassword(8, specialCharsRule, lowerCaseRule, upperCaseRule, digitRule);
    }
}
