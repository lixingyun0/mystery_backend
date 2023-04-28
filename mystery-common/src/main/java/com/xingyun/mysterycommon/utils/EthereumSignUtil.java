package com.xingyun.mysterycommon.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;

public class EthereumSignUtil {

    private static Logger log = LoggerFactory.getLogger(EthereumSignUtil.class);

    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";


    public static boolean isSignatureValid(final String address, final String signature, final String message) throws SignatureException {
        log.info("isSignatureValid invoked for Address {} with Signature {} and Message {} ", address, signature,
                message);

        String originText = PERSONAL_MESSAGE_PREFIX + message.length() + message;

        final byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        //v r s
        final Sign.SignatureData sd = new Sign.SignatureData(v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        BigInteger bigInteger = Sign.signedMessageToKey(originText.getBytes(StandardCharsets.UTF_8), sd);
        String address1 = "0x" + Keys.getAddress(bigInteger);

        if (address.equalsIgnoreCase(address1)){
            return true;
        }
        BigInteger bigInteger2 = Sign.signedMessageToKey(message.getBytes(StandardCharsets.UTF_8), sd);
        String address2 = "0x" + Keys.getAddress(bigInteger2);

        return address.equalsIgnoreCase(address2);
    }

    //0x1df3f2acfb48eff2fed67eebccda4b7b0728668b
    //085b0dd9f4f522a6fbd2513eafdb6982c4f387993b316ceea3e850daafa76c2d
    public static String signMessage(String message, String privateKey){

        byte[] bytes = ByteArray.fromHexString(privateKey);

        ECKeyPair ecKeyPair = ECKeyPair.create(bytes);

        BigInteger publicKey = ecKeyPair.getPublicKey();
        System.out.println("publicKey: 0x"+Keys.getAddress(publicKey));

        String originText = PERSONAL_MESSAGE_PREFIX + message.length() + message;
        //String originText = PERSONAL_MESSAGE_PREFIX + "32" + message;


        Sign.SignatureData signatureData = Sign.signMessage(originText.getBytes(StandardCharsets.UTF_8), ecKeyPair,true);

        byte[] sig_data = ByteBuffer.allocate(signatureData.getR().length + signatureData.getS().length + signatureData.getV().length)
                .put(signatureData.getR())
                .put(signatureData.getS())
                .put(signatureData.getV())
                .array();
        final String signature = Numeric.toHexStringNoPrefix(sig_data);

        System.out.println("0x"+signature);
        return signature;

    }
    public static String signMessage2(String message, String privateKey){

        byte[] bytes = ByteArray.fromHexString(privateKey);

        ECKeyPair ecKeyPair = ECKeyPair.create(bytes);

        BigInteger publicKey = ecKeyPair.getPublicKey();
        System.out.println("publicKey: 0x"+Keys.getAddress(publicKey));

        String originText = message;
        //String originText = PERSONAL_MESSAGE_PREFIX + "32" + message;


        Sign.SignatureData signatureData = Sign.signMessage(originText.getBytes(StandardCharsets.UTF_8), ecKeyPair,true);

        byte[] sig_data = ByteBuffer.allocate(signatureData.getR().length + signatureData.getS().length + signatureData.getV().length)
                .put(signatureData.getR())
                .put(signatureData.getS())
                .put(signatureData.getV())
                .array();
        final String signature = Numeric.toHexStringNoPrefix(sig_data);

        System.out.println("0x"+signature);
        return signature;

    }

    /**
     * 创建eth账号
     * @return
     */
    public static String[] createEthAccount() throws Exception{
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
        String privateKey = privateKeyInDec.toString(16);
        String address = Keys.getAddress(ecKeyPair.getPublicKey());
        System.out.println("privateKey:"+privateKey);
        System.out.println("address:"+address);

        if (address.startsWith("0x")) {
            address = address.substring(2).toLowerCase();
        } else {
            address = address.toLowerCase();
        }
        address = "0x" + address;
        System.out.println(address);
        //privateKey = encrypt(privateKey, PUBLIC_KEY);
        return new String[]{address, privateKey};
    }

    public static void main(String[] args) throws Exception {

        String address = "0xfa7e751f6802437553c9880438f1e99a3ad8240f";



        String signature = "0x65e403a0cabf667ba5b823c11430539003407311e553e5618b3d64d79a211326462824ed8d11868ace177bdb6dbf9ec722f36145d03584ebf55dc8d86390e8bc1b";

        String originText = "zheshiyigeceshiqianming";


        boolean zzz = isSignatureValid(address, signature, originText);

        System.out.println(zzz);
        //sign4();


    }

    //0x4f0333424e3777abaee9219de8eb34e07d7a67c6
    private static void sign1(){
        String privateKey = "6298c9e4b05bab32e5de53958edeb353631255ff5018c72486a6b38437be1fbd";

        String originText = "zheshiyigeceshiqianming: "+System.currentTimeMillis();

        System.out.println(originText);
        signMessage(originText,privateKey);
    }

    //0x2d7f9c69ae60670d498481d09de2b117dbcf33f2
    private static void sign2(){
        String privateKey = "75eef455cdce2ca0a7b243e287be707ce8c08a52f65a123b6e63f82cef1fa27f";

        String originText = "zheshiyigeceshiqianming: "+System.currentTimeMillis();

        System.out.println(originText);
        signMessage(originText,privateKey);
    }

    private static void sign3(){
        String privateKey = "019ade57e7357ae347b841291c80834787d980cee3b679c942f1ce3b746be0c6";

        String originText = "zheshiyigeceshiqianming: "+System.currentTimeMillis();

        System.out.println(originText);
        signMessage(originText,privateKey);
    }

    //carv_test
    private static void sign4(){
        String privateKey = "d283c254cbaea4f38d78fd25775ef2ccacef186515c4a7d0ed0cf2c56f355b55";

        String originText = "zheshiyigeceshiqianming";

        System.out.println(originText);
        signMessage(originText,privateKey);
    }
    //ef01f10f828fa60d3a048dcc62e81c3bd121086eccf728204566cd8919510d31

    private static void sign5(){
        String privateKey = "ef01f10f828fa60d3a048dcc62e81c3bd121086eccf728204566cd8919510d31";

        String originText = "zheshiyigeceshiqianming: "+System.currentTimeMillis();

        System.out.println(originText);
        signMessage(originText,privateKey);
    }

    private static void ecrecover(){

    }


}
