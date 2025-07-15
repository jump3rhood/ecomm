package org.john.personal.userservice.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class JwtService {

    @Value("${keyStore.path}")
    private String keyStorePath;
    @Value("${keyStore.password}")
    private String keyStorePassword;

    //wrapper around RSA public key
    private JWSVerifier verifier;

    @PostConstruct
    public void init() throws Exception{
        loadPublicKey();
    }
    public JWTClaimsSet validateTokenAndExtractClaims(String token){
        try{
            // parse the jwt -> object with header, ClaimsSet and signature
            SignedJWT signedJWT = SignedJWT.parse(token);

            // verify the signature with public key verifier
            if(!signedJWT.verify(verifier)){
                System.out.println("Invalid signature");
                return null; // Invalid signature
            }

            // Get Claims
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            Date expirationTime = claims.getExpirationTime();
            if(expirationTime != null && expirationTime.before(new Date())){
                System.out.println("expired "+ expirationTime.toString());
                return null; // Token expired
            }

            return claims;

        }catch (ParseException | JOSEException e){

            System.out.println("Invalid Token " + this.keyStorePath + this.keyStorePassword);
            System.out.println(e.getMessage());
            return null;
        }
    }
    /*
    * Extract the roles and convert to granted authorities to
    * put in the authentication token (UsernamePasswordAuthenticationToken) for simplicity
    * */
    public Collection<SimpleGrantedAuthority> extractAuthorities(JWTClaimsSet claims){
        try{
            List<String> roles = claims.getStringListClaim("roles");
            if(roles == null || roles.isEmpty()){
                return List.of(); // No roles found
            }
            System.out.println("Roles from JWT: " + roles.toString());
            Collection<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
            System.out.println("Created authorities: " + authorities);
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            return null;
        }
    }


    public String extractFirstName(JWTClaimsSet claims){
        try {
            return claims.getStringClaim("firstName");
        } catch (ParseException e) {
            return null;
        }
    }
    public String extractLastName(JWTClaimsSet claims){
        try {
            return claims.getStringClaim("lastName");
        } catch (ParseException e) {
            return null;
        }
    }
    public String getSubject(JWTClaimsSet claims){
        return claims.getSubject();
    }
    public Long extractUserId(JWTClaimsSet claims){
        try {
            return claims.getLongClaim("userId");
        }catch(ParseException e){
            return null;
        }
    }

    /**
     * Loads the RSA public key from keystore for token verification
     */
    private void loadPublicKey() throws KeyStoreException, IOException,
            CertificateException, NoSuchAlgorithmException, JOSEException {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new ClassPathResource(keyStorePath).getInputStream(),
                keyStorePassword.toCharArray());
        System.out.println("Keystore loaded successfully");
        RSAPublicKey publicKey = (RSAPublicKey) keyStore.getCertificate("auth-server").getPublicKey();
        this.verifier = new RSASSAVerifier(publicKey);
    }


    @Bean
    public JwtEncoder jwtEncoder() throws UnrecoverableKeyException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        RSAKey rsaKey = loadRSAKey();
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    private RSAKey loadRSAKey () throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new ClassPathResource(keyStorePath).getInputStream(), keyStorePassword.toCharArray());
        RSAPublicKey pubKey = (RSAPublicKey) keyStore.getCertificate("auth-server").getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey("auth-server", keyStorePassword.toCharArray());
        return new RSAKey.Builder(pubKey).privateKey(privateKey).keyID("auth-server").build();
    }

}
