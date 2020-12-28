import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

public class filecipher01 {
	private static SecretKeySpec secretKey;
	private static byte[] key;

	public filecipher01(String s) {
		setKey(s);
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
			CipherInputStream cin = new CipherInputStream(fin, cipher);
			byte[] input = new byte[64];
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
			byte[] input = new byte[64];
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
			System.out.println("Error while decrypting !");
		}

	}

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("------------------");
		System.out.println(" JAVA FILE CIPHER");
		System.out.println("------------------");
		System.out.println("file:" + args[1]);
		System.out.println("key:");
		setKey(reader.readLine());

		switch (args[0]) {
		case "-enc":
			encrypt(args[1]);
			break;
		case "-dec":
			decrypt(args[1]);
			break;
		}
	}
}
