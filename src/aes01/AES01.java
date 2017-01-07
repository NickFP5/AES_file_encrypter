/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aes01;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import java.security.*; 

/**
 *
 * @author Nick Pagano
 */
public class AES01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException {
        // TODO code application logic here
        KeyGenerator keygen;
        keygen = KeyGenerator.getInstance("AES");
        SecretKey k = keygen.generateKey();
        

        Cipher aesCipher;
        
        // Create the cipher
        aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
       
        // Initialize the cipher for encryption
        aesCipher.init(Cipher.ENCRYPT_MODE, k);

        // Our plaintext
        //String plaintext = "Prova";
        byte b1 = 127;
        byte b2 = 112;
        byte[] cleartext = new byte[2];
        cleartext[0] = b1;
        cleartext[1] = b2;
        System.out.println("Plaintext: " + cleartext);
        System.out.println("Key: " + k);
        
        // Encrypt the cleartext
        byte[] ciphertext = aesCipher.doFinal(cleartext);
        System.out.println("Ciphertext: " + ciphertext[0] + " " + ciphertext[1]);
        
        
        // Initialize the same cipher for decryption
        aesCipher.init(Cipher.DECRYPT_MODE, k);

        // Decrypt the ciphertext
        byte[] cleartext1 = aesCipher.doFinal(ciphertext);
        
        System.out.println("Ciphertext decrypted: " + cleartext1[0] + " " + cleartext1[1]);
        
        FileReader fpin;
        FileWriter fpout;
        FileWriter fpout2;
        byte[] plaintext;
        String s, aux, aux2;
        fpin = new FileReader("in.txt");
        fpout = new FileWriter("out.txt");
        fpout2 = new FileWriter("out2.txt");
        
        BufferedReader br = new BufferedReader(fpin);
        BufferedWriter bw = new BufferedWriter(fpout);
        BufferedWriter bw2 = new BufferedWriter(fpout2);


            s = br.readLine();
            if(s!=null) {
                do{
                    aesCipher.init(Cipher.ENCRYPT_MODE, k);
                    ciphertext = aesCipher.doFinal(s.getBytes("UTF-8"));
                    aux = Base64.encode(ciphertext);
                    //aux = new String(ciphertext, "UTF-8");
                    bw.write(aux);
                    /*bw.write(ciphertext[0]);
                    bw.newLine();
                    System.out.println("Ciphertext: " + ciphertext.toString());
                    System.out.println("aux: " + aux.toString());*/
                    
                    aesCipher.init(Cipher.DECRYPT_MODE, k);
                    ciphertext = Base64.decode(aux);
                    plaintext = aesCipher.doFinal(ciphertext);
                    aux = new String(plaintext, "UTF-8");
                    bw2.write(aux);
                    bw2.newLine();
                    //System.out.println("Plaintext: " + plaintext.toString());
                    //System.out.println("aux: " + aux.toString());
                    
                    s = br.readLine();
                }while(s!=null);
                bw.flush();
                bw2.flush();
            }
            Thread T= new Thread(new AESEncrypterGUI());
            T.start();
    }
    
}
