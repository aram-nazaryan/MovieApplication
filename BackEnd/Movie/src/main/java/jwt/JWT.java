package jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

public class JWT {
    static Dotenv dotenv = Dotenv.configure().load();
    static String apiKey = dotenv.get("SECRET");

    public String generateJWT(String id, String subject, long ttlMillis) {

        SignatureAlgorithm signatureAlgorithm = HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, signingKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    //method to verify JWT
    public Boolean verifyJWT(String jwt) {

        String[] chunks = jwt.split("\\.");
        SignatureAlgorithm signatureAlgorithm = HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);

        Key secretKeySpec = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        try {
            String tokenWithoutSignature = chunks[0] + "." + chunks[1];
            String signature = chunks[2];
            DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(signatureAlgorithm, secretKeySpec);
            return validator.isValid(tokenWithoutSignature, signature);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //Getting id of User
    public int getUserId(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
                .parseClaimsJws(jwt).getBody();

        return Integer.parseInt(claims.getId());
    }
}