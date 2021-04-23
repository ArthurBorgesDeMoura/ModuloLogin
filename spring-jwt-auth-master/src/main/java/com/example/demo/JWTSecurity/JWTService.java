package com.example.demo.JWTSecurity;

import com.example.demo.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTService {

    @Value("${security.jwt.expiracao}")
    private String expiração;

    @Value("${security.jwt.chaveAssinatura}")
    private String chaveAssinatura;

    public String geraToken(User user)  {
        long expString = Long.valueOf(expiração);
        LocalDateTime horaExpiracao = LocalDateTime.now().plusMinutes(expString);
        Date date = Date.from(horaExpiracao.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder().
                    setSubject(user.getUsername()).
                    setExpiration(date).
                    signWith(SignatureAlgorithm.HS512, chaveAssinatura).compact();
    }

    public Claims obterClaims(String token) throws ExpiredJwtException{
        return Jwts.parser().setSigningKey(chaveAssinatura).parseClaimsJws(token).getBody();
    }

    public boolean tokenValido( String token){
        try{
            Claims claims = obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime date = dataExpiracao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(date);
        }
        catch(Exception e){
            return false;
        }
    }
    public String obterLoginUsuario (String token) throws ExpiredJwtException{
    return (String) obterClaims(token).getSubject();
    }

}
