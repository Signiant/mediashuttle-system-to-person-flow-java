import com.workflows.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.workflows"})
public class Main {

    @Autowired
    CommonService common;

    @Value("${developer.key}")
    private String devKey;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public void configureAuth(){
        common.setAuthentication(devKey);
    }
}
