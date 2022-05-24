package edu.nus.iss.vttpfinalapplication.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nus.iss.vttpfinalapplication.repositories.UserRepository;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepo;
    
    public String hashingPassword(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(password.getBytes());

            BigInteger num = new BigInteger(1, messageDigest);

            String hashtext = num.toString(16);

            while(hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // System.out.println("New hash is " +hashtext);
            return hashtext;

        } catch(NoSuchAlgorithmException e) {
            System.out.println(">>>> Error in hashing: " + e.getStackTrace());

            return "";
        }
    }

    public Boolean autheticationWithDB(String username, String hashPassword) {

        return userRepo.selectUser(username, hashPassword);  
    }

    public int createUserWithDB(String username, String hashPassword) {
        if(!userRepo.selectUserByUsername(username)) {
            userRepo.createUser(username, hashPassword);
            return 1;
        } else {
            return 0;
        }
    }
    
}
