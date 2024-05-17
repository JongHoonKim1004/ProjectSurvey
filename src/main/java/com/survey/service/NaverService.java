package com.survey.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.survey.dto.NaverDTO;
import com.survey.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class NaverService {
    @Autowired
    private UsersRepository usersRepository;





    // 접근 토큰 요청
    public String getAccessToken(String code, String state){
        log.info("ACCESS GET ACCESS_TOKEN");
        String access_token = "";

        String reqURL = "https://nid.naver.com/oauth2.0/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=3kmcSAzePM8TYBI6aCLw");
            sb.append("&client_secret=nXTx_4PY7f");
            sb.append("&state=" + state);
            sb.append("&code=" + code);
            br.write(sb.toString());
            br.flush();

            int responseCode = conn.getResponseCode();
            log.info(String.valueOf(responseCode));

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            log.info("RESPONSE BODY: " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);


            access_token = element.getAsJsonObject().get("access_token").getAsString();
            log.info("ACCESS TOKEN: " + access_token);

        } catch (Exception e){
            e.printStackTrace();
        }

        return access_token;
    }


    // 개인정보 요청
    public NaverDTO getUserInfo(String accessToken){
        log.info("GET USER INFO");
        NaverDTO naverDTO = new NaverDTO();

        String reqURL = "https://openapi.naver.com/v1/nid/me";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            log.info("responseCode: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while((line = br.readLine()) != null) {
                result += line;
            }

            log.info("responseBody: " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject obj = element.getAsJsonObject().get("response").getAsJsonObject();
            String name = obj.get("name").getAsString();
            String email = obj.get("email").getAsString();

            naverDTO.setName(name);
            naverDTO.setEmail(email);
            naverDTO.setAccess_token(accessToken);

        } catch (Exception e){
            e.printStackTrace();
        }

        return naverDTO;
    }
}
