package pe.edu.techlab.security;
import java.time.Instant;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class TokenApi {
    private final AuthenticationManager manager;
    private final JwtEncoder encoder;
    public TokenApi(AuthenticationManager manager, JwtEncoder encoder) {
        this.manager=manager; this.encoder=encoder;
    }
    public record Login(@NotBlank @Size(max=80) String username,
                        @NotBlank @Size(max=72) String password) {}
    public record Token(String accessToken,String tokenType,long expiresIn) {}
    @PostMapping("/token") ResponseEntity<Token> token(@Valid @RequestBody Login login) {
        try {
            var auth=manager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(
                login.username(),login.password()));
            var roles=auth.getAuthorities().stream().map(a -> a.getAuthority())
                .filter(a -> a.startsWith("ROLE_")).map(a -> a.substring(5)).toList();
            Instant now=Instant.now();
            var claims=JwtClaimsSet.builder().issuer(SecurityConfig.ISSUER)
                .audience(List.of(SecurityConfig.AUDIENCE)).subject(auth.getName())
                .issuedAt(now).expiresAt(now.plusSeconds(900)).claim("roles",roles).build();
            var jwt=encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims));
            return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(new Token(jwt.getTokenValue(),"Bearer",900));
        } catch(AuthenticationException e) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
    }
}
