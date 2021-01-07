import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

//File_Deflater_Cipher.java
public class FDC {
	private static SecretKeySpec secretKey;
	private static byte[] key;

	public FDC(String s) {
		setKey(s);
	}
	
	public void enc(String s) {
		encrypt(s);
	}

	public void dec(String s) {
		decrypt(s);
	}	

	private static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void encrypt(String s) {
		try {
			FileInputStream fin = new FileInputStream(s);
			FileOutputStream fout = new FileOutputStream(s + ".enc");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			DeflaterInputStream defl = new DeflaterInputStream(fin);
			CipherInputStream cin = new CipherInputStream(defl, cipher);
			byte[] input = new byte[1024];
			while (true) {
				int bytesRead = cin.read(input);
				if (bytesRead == -1)
					break;
				fout.write(input, 0, bytesRead);
			}
			cin.close();
			fout.flush();
			fout.close();

		} catch (Exception e) {
			System.out.println("Error while encrypting !");
		}

	}

	private static void decrypt(String s) {
		try {
			FileInputStream fin = new FileInputStream(s);
			FileOutputStream fout = new FileOutputStream(s + ".dec");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			CipherInputStream cin = new CipherInputStream(fin, cipher);
			InflaterInputStream infl = new InflaterInputStream(cin);
			byte[] input = new byte[1024];
			while (true) {
				int bytesRead = infl.read(input);
				if (bytesRead == -1)
					break;
				fout.write(input, 0, bytesRead);
			}
			cin.close();
			fout.flush();
			fout.close();
		} catch (Exception e) {
			System.out.println("Error while decrypting !");
		}

	}

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("------------------");
		System.out.println(" JAVA FILE CIPHER");
		System.out.println("------------------");
		//System.out.println("file:" + args[1]);
		//System.out.print("key:");
		//setKey(reader.readLine());
		
		switch (args[0]) {
		case "-enc":
			System.out.print("key:");
			setKey(reader.readLine());
			encrypt(args[1]);
			break;
		case "-dec":
			System.out.print("key:");
			setKey(reader.readLine());
			decrypt(args[1]);
			break;
		default:
			System.out.println("usage: FDC [-enc|-dec] <file>");
			break;
		}
		
	}
}
