package com.example.chatfunction;

import com.example.chatfunction.model.UserAccountModel;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.beans.BeanProperty;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.JDBCType;
import java.util.Objects;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ChatFunctionApplication implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = ChatFunctionApplication.class.getClassLoader();

        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
        FileInputStream serviceAccount =
                new FileInputStream(file.getAbsolutePath());


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://chatfunction-5ab46-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();

        FirebaseApp.initializeApp(options);

        SpringApplication.run(ChatFunctionApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String sql = 'SELECT * from chat';
        JdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(UserAccountModel));
    }
}
