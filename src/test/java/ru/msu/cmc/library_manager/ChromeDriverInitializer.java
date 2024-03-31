package ru.msu.cmc.library_manager;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ChromeDriverInitializer {
    @Value("${chromeDriverLocation}")
    private String chromeDriverLocation;
    @Value("${websiteUrl}")
    private String url;

    @Bean
    public ChromeDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().setPosition(new Point(0, 0));
        chromeDriver.manage().window().setSize(new Dimension(1920, 1080));
        chromeDriver.get(url);
        return chromeDriver;
    }
}
