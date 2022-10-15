package com.ettransit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@ExtendWith(MockitoExtension.class)
class EtTransitApplicationTests {

  @Mock private ConfigurableApplicationContext context;

  @AfterEach
  void tearDown() {
    if (context != null) {
      context.close();
    }
  }

  @Test
  void testClassConstructor() {
    Assertions.assertDoesNotThrow(EtTransitApplication::new);
  }

  /** Test the main method with mocked application context. */
  @Test
  void contextLoads() {
    try (var mockStatic = Mockito.mockStatic(SpringApplication.class)) {
      mockStatic
          .when(() -> context = SpringApplication.run(EtTransitApplication.class))
          .thenReturn(context);

      EtTransitApplication.main(new String[] {});

      mockStatic.verify(() -> context = SpringApplication.run(EtTransitApplication.class));
    }
  }
}
