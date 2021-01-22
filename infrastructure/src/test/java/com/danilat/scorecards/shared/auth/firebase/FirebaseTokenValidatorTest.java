package com.danilat.scorecards.shared.auth.firebase;

import static org.junit.Assert.assertEquals;

import com.danilat.scorecards.shared.auth.firebase.TokenValidator.Token;
import com.google.firebase.auth.FirebaseAuthException;
import org.junit.Ignore;
import org.junit.Test;

public class FirebaseTokenValidatorTest {

  @Ignore
  @Test
  public void checkThatWorks() throws FirebaseAuthException {
    String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA4MGU0NWJlNGIzMTE4MzA5M2RhNzUyYmIyZGU5Y2RjYTNlNmU4ZTciLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiRGFuaSBMYXRvcnJlIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hLS9BT2gxNEdnMWd3Y0YwYUd1bVpLcWZjcWNFaHNLb2ZKR25zN2Z0RG4wS29vbWVnIiwiaXNzIjoiaHR0cHM6Ly9zZWN1cmV0b2tlbi5nb29nbGUuY29tL3Njb3JlY2FyZHMtYmllcmEiLCJhdWQiOiJzY29yZWNhcmRzLWJpZXJhIiwiYXV0aF90aW1lIjoxNjA5NTA3OTIwLCJ1c2VyX2lkIjoiQ0FJOHRzeTR6ME00UXlsbHlKc2JzTFU0MDFOMiIsInN1YiI6IkNBSTh0c3k0ejBNNFF5bGx5SnNic0xVNDAxTjIiLCJpYXQiOjE2MDk1MDc5MjAsImV4cCI6MTYwOTUxMTUyMCwiZW1haWwiOiJkYW5pbGF0ODNAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZ29vZ2xlLmNvbSI6WyIxMTQ0MjM0MDQ0OTM0ODY2MjMyMjYiXSwiZW1haWwiOlsiZGFuaWxhdDgzQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6Imdvb2dsZS5jb20ifX0.piEpi_kkik5qlNr8OBDJ1ZYehNlcXZ70PNPAoW-KlxOUf29gqoRqvFH6UI_kKxuX_Eq3fBaFpt4jFPS_70k7oi5PqDsrvReLFrU0co-CmUULkGCOUa1VYaI6yW8O4dL0fONieZuehuN2qfvjnQ4o_z7FDzQzxIKRtfPeZTL6PCc6SlRPSdTn6vz_9i709M1We3saS6oTQuT9MqhBTkF0lIzH1YgjDQf8e24MIvLninPmdGR8JFKpJFaEOBi_CikLq2y1jKgT0xgdnsly_aFDuPk0dGdNcn0xwNzHaKVZWMRIrq6lw-ZgTcBX3L5BBIxuZXfVSvHtB1pBavHYi_EDZQ";
    Token decodedToken = new TokenValidator().validateToken(idToken);
    assertEquals("Dani Latorre", decodedToken.getName());
    assertEquals("danilat83@gmail.com", decodedToken.getEmail());
    assertEquals("https://lh3.googleusercontent.com/a-/AOh14Gg1gwcF0aGumZKqfcqcEhsKofJGns7ftDn0Koomeg",
        decodedToken.getPicture());
  }
}